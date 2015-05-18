package pdstore.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.Blob;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.IsolationLevel;
import pdstore.PDChange;
import pdstore.PDStoreException;
import pdstore.Transaction;
import pdstore.dal.PDWorkingCopy;
import pdstore.notify.ListenerDispatcher;
import pdstore.notify.PDListener;

public class PDStore extends pdstore.PDStore {
	private PDStoreServerI server;
	private final ReverseListener reverseListener;
	private final Thread reverseListenerThread;
	private final List<PDListener<GUID, Object, GUID>> interceptorList = new ArrayList<PDListener<GUID, Object, GUID>>();
	private final List<PDListener<GUID, Object, GUID>> detachedListenerList = new ArrayList<PDListener<GUID, Object, GUID>>();

	public PDStoreServerI getServer() {
		return this.server;
	}

	public PDStore(String host) {
		this(host, "PDStore");
	}

	public PDStore(String host, String rmiKey) {
		super(Void.class); // Step into the non-init constructor, allowing the
							// server to be correctly constructed before trying
							// to add links, etc.
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(host);
			this.server = (PDStoreServerI) registry.lookup(rmiKey);
		} catch (Exception e) {
			throw new PDStoreException("Cannot connect to server " + host
					+ " using RMI key " + rmiKey, e);
		}
		initRemotePDStore();

		this.reverseListener = new ReverseListener(this);
		this.reverseListenerThread = new Thread(this.reverseListener);
		this.reverseListenerThread.start();
	}

	protected void initRemotePDStore() {
		// add the listener & interceptor dispatchers
		// Transplanted locally from pdstore.PDStore to ensure setup
		ListenerDispatcher<GUID, Object, GUID> listenerDispatcher = new ListenerDispatcher<GUID, Object, GUID>();
		ListenerDispatcher<GUID, Object, GUID> interceptorDispatcher = new ListenerDispatcher<GUID, Object, GUID>();
		ListenerDispatcher<GUID, Object, GUID> immediateDispatcher = new ListenerDispatcher<GUID, Object, GUID>();
		ListenerDispatcher<GUID, Object, GUID> readDispatcher = new ListenerDispatcher<GUID, Object, GUID>();

		getDetachedListenerList().add(listenerDispatcher);
		getInterceptorList().add(interceptorDispatcher);
		immediateDispatcher.getMatchingTemplates().add(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_NOW_ADDED,
						null, null, null, null));
		getInterceptorList().add(immediateDispatcher);
		getInterceptorList().add(readDispatcher);
	}

	/**** Misc functions ****/

	@Override
	public GUID getRepository() throws PDStoreException {
		try {
			return this.server.getRepository();
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}

	/**** Transaction management ****/

	@Override
	public GUID begin(Transaction<GUID, Object, GUID> transaction)
			throws PDStoreException {
		try {
			return this.server.begin(transaction);
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}

	@Override
	public GUID commit(GUID transaction) throws PDStoreException {
		// System.err.println("Committing " + transaction +
		// " on a remote-active PDStore instance");
		try {
			return this.server.commit(transaction);
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}

	@Override
	public void rollback(GUID transaction) throws PDStoreException {
		try {
			this.server.rollback(transaction);
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}

	/**** Reading & writing ****/

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getChanges(
			PDChange<GUID, Object, GUID> change) throws PDStoreException {
		try {
			return this.server.getChanges(change);
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}

	@Override
	public void addChange(PDChange<GUID, Object, GUID> change)
			throws PDStoreException {
		try {
			this.server.addChange(change);
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}

	@Override
	public Iterator<PDChange<GUID, Object, GUID>> iterator() {
		throw new PDStoreException("Unsupported when using a remote server");
	}

	/**** Listeners ****/

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
		try {
			return this.server.nextTransaction();
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}

	@Override
	public List<List<PDChange<GUID, Object, GUID>>> newTransactions()
			throws PDStoreException {
		try {
			return this.server.newTransactions();
		} catch (RemoteException e) {
			throw new PDStoreException("Server error", e);
		}
	}
}
