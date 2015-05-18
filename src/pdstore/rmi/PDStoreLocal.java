package pdstore.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.Blob;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStoreException;
import pdstore.concurrent.ConcurrentStore;
import pdstore.dal.PDWorkingCopy;
import pdstore.notify.ListenerDispatcher;
import pdstore.notify.PDListener;
import replica.ReplicaClientDetachedListener;

/**
 * Mix between local and remote pdstore. On creation a link is created with a
 * remote server and a local delegate server is created. All calls to this store
 * are delegated to the local store. On commit the changes are pushed to the
 * remote store. A reverse listener is used to pull down commits from the remote
 * store.
 * 
 * @author jker040, mfow017
 * 
 */
public class PDStoreLocal extends pdstore.PDStore {

	public final Object commitLock = "COMMIT_LOCK";

	private String name;
	private PDStoreServerI server;
	private final pdstore.PDStore local;
	private final ReverseListener reverseListener;
	private final Thread reverseListenerThread;
	private final List<PDListener<GUID, Object, GUID>> interceptorList = new ArrayList<PDListener<GUID, Object, GUID>>();
	private final List<PDListener<GUID, Object, GUID>> detachedListenerList = new ArrayList<PDListener<GUID, Object, GUID>>();
	private final ReplicaClientDetachedListener detachedListener;

	private final Set<GUID> recentTransactions = new HashSet<GUID>();

	public Set<GUID> getRecentTransactions() {
		return this.recentTransactions;
	}

	public PDStoreServerI getServer() {
		return this.server;
	}

	/**
	 * getLatest is an improvised approach to obtaining local transaction IDs.
	 * This is mostly an issue because the identifying GUIDs are not preserved
	 * between server and client at present. Correcting this would probably
	 * require modifications to the underlying store. An alternative would be to
	 * force commit to synchronise on some queue structure, and use this to
	 * derive the local GUID after propagation.
	 * 
	 * @return the durable GUID of the most recently committed transaction
	 */
	public GUID getLatest() {
		return ((ConcurrentStore) this.local.getStore())
				.getLatestDurableTransaction();
	}

	public PDStoreLocal(String host) {
		this("PDStore", host, "PDStore");
	}

	public PDStoreLocal(String localName, String host, String rmiKey) {
		super(Void.class); // Step into the non-init constructor, allowing the
							// server to be correctly constructed before trying
							// to add links, etc.
		this.name = localName;
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(host);
			this.server = (PDStoreServerI) registry.lookup(rmiKey);
		} catch (Exception e) {
			throw new PDStoreException("Cannot connect to server", e);
		}
		this.local = new pdstore.PDStore(localName);
		this.detachedListener = new ReplicaClientDetachedListener(this.local,
				this);
		initRemotePDStore();

		this.reverseListener = new ReverseListener(this.server, this);
		this.reverseListenerThread = new Thread(this.reverseListener);
		this.reverseListenerThread.start();
	}

	public ListenerDispatcher<GUID, Object, GUID> getListenerDispatcher() {
		return this.local.getListenerDispatcher();
	}

	public String getStoreName() {
		return name;
	}

	protected void initRemotePDStore() {
		// add the listener & interceptor dispatchers
		// Transplanted locally from pdstore.PDStore to ensure setup
		ListenerDispatcher<GUID, Object, GUID> listenerDispatcher = new ListenerDispatcher<GUID, Object, GUID>();
		ListenerDispatcher<GUID, Object, GUID> interceptorDispatcher = new ListenerDispatcher<GUID, Object, GUID>();
		ListenerDispatcher<GUID, Object, GUID> immediateDispatcher = new ListenerDispatcher<GUID, Object, GUID>();
		ListenerDispatcher<GUID, Object, GUID> readDispatcher = new ListenerDispatcher<GUID, Object, GUID>();

		getDetachedListenerList().add(listenerDispatcher);
		/* This listener applies remote changes */
		getDetachedListenerList().add(this.detachedListener);

		getInterceptorList().add(interceptorDispatcher);
		immediateDispatcher.getMatchingTemplates().add(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_NOW_ADDED,
						null, null, null, null));
		getInterceptorList().add(immediateDispatcher);
		getInterceptorList().add(readDispatcher);
	}

	/**
	 * Gets all the instances of the given type that are stored in the database.
	 * 
	 * @param transactionId
	 *            transaction id on which to search
	 * @param typeid
	 *            ID of the type
	 * @return all stored instances of that type
	 * @throws PDStoreException
	 */
	@Override
	public Collection<Object> getAllInstancesOfType(GUID transaction, GUID type)
			throws PDStoreException {
		return this.local.getAllInstancesOfType(transaction, type);
	}

	/**
	 * @see PDWorkingCopy#getId(String)
	 * @param transaction
	 *            - current transaction id
	 * @param name
	 * @return
	 * @throws PDStoreException
	 */
	@Override
	public GUID getId(GUID transaction, String name) throws PDStoreException {
		return this.local.getId(transaction, name);
	}

	/**
	 * @see PDWorkingCopy#getIds(String)
	 * @param transaction
	 *            - current transaction id
	 * @param name
	 * @return
	 * @throws PDStoreException
	 */
	@Override
	public Collection<GUID> getIds(GUID transaction, String name)
			throws PDStoreException {
		return this.local.getIds(transaction, name);
	}

	/**
	 * @see PDWorkingCopy#getName(GUID)
	 * @param transaction
	 *            - current transaction id
	 * @param instanceId
	 * @return
	 * @throws PDStoreException
	 */
	public String getName(GUID transaction, GUID instanceId)
			throws PDStoreException {
		return this.local.getName(transaction, instanceId);
	}

	/**
	 * @see PDWorkingCopy#getIcon(GUID)
	 * @param transaction
	 *            - current transaction id
	 * @param instanceId
	 * @return
	 * @throws PDStoreException
	 */
	public Blob getIcon(GUID transaction, GUID instanceId)
			throws PDStoreException {
		return this.local.getIcon(transaction, instanceId);
	}

	@Override
	public GUID getRepository() throws PDStoreException {
		return this.local.getRepository();
	}

	@Override
	public GUID getAccessorType(GUID transaction, GUID role2Id) {
		return this.local.getAccessorType(transaction, role2Id);
	}

	@Override
	public Collection<Object> getAllInstancesInRole(GUID transaction, GUID role)
			throws PDStoreException {
		return this.local.getAllInstancesInRole(transaction, role);
	}

	@Override
	public Object getInstance(GUID transaction, Object instance1, GUID role2)
			throws PDStoreException {
		Debug.assertTrue(instance1 != null, "instance1 must not be null.");
		Debug.assertTrue(role2 != null, "role2 must not be null.");

		return this.local.getInstance(transaction, instance1, role2);
	}

	@Override
	public Collection<Object> getInstances(GUID transaction, Object instance1,
			GUID role2) throws PDStoreException {
		Debug.assertTrue(instance1 != null, "instance1 must not be null.");
		Debug.assertTrue(role2 != null, "role2 must not be null.");

		Collection<Object> instances = this.local.getInstances(transaction,
				instance1, role2);

		return instances;
	}

	@Override
	public boolean instanceExists(GUID transaction, Object instance)
			throws PDStoreException {
		Debug.assertTrue(instance != null,
				"instance to look for in instanceExists must not be null.");

		return this.local.instanceExists(transaction, instance);
	}

	@Override
	public void addChange(PDChange<GUID, Object, GUID> change)
			throws PDStoreException {
		try {
			this.server.addChange(change);
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
		this.local.addChange(change);
	}

	@Override
	public GUID commit(GUID transaction) throws PDStoreException {
		GUID tid = null;
		synchronized (commitLock) {
			try {
				tid = this.server.commit(transaction);
				this.recentTransactions.add(tid);
			} catch (RemoteException e) {
				throw new PDStoreException("Server error", e);
			}
			this.local.getTransaction(transaction).setDurableId(tid);
			GUID localDtid = this.local.commit(transaction);
		}

		return tid;
	}

	@Override
	public void rollback(GUID transaction) throws PDStoreException {
		try {
			this.server.rollback(transaction);
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
		this.local.rollback(transaction);

	}

	@Override
	public Iterator<PDChange<GUID, Object, GUID>> iterator() {
		return this.local.iterator();
	}

	@Override
	public List<PDListener<GUID, Object, GUID>> getDetachedListenerList()
			throws PDStoreException {
		return this.detachedListenerList;
	}

	@Override
	public List<PDListener<GUID, Object, GUID>> getInterceptorList()
			throws PDStoreException {
		return this.interceptorList;
	}

	@Override
	public List<PDChange<GUID, Object, GUID>> nextTransaction()
			throws PDStoreException {
		return this.local.nextTransaction();
	}

	@Override
	public List<List<PDChange<GUID, Object, GUID>>> newTransactions()
			throws PDStoreException {
		return this.local.newTransactions();
	}
}
