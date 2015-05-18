package pdstore.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.ConcatenationIterator;
import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.IteratorBasedCollection;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.IsolationLevel;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.PDStoreException;
import pdstore.PersistenceLevel;
import pdstore.Transaction;
import pdstore.concurrent.ConcurrentStore;
import pdstore.dal.PDInstance;
import pdstore.notify.ListenerDispatcher;
import pdstore.notify.PDListener;

public class GenericPDStore<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		implements PDStoreI<TransactionType, InstanceType, RoleType> {

	/**
	 * A PDStore object that can be used for debugging code.
	 */
	protected static PDStore debugStore;

	/**
	 * Gets a static PDStore that can be used for debugging, e.g.
	 * pretty-printing instances with their names, if no other PDStore is at
	 * hand.
	 * 
	 * The debug store is the last PDStore that was instantiated from a file (as
	 * opposed to a store instantiated from another store object), or null.
	 * 
	 * @return a PDStore object that can be read from when printing debug output
	 */
	public static PDStore getDebugStore() {
		return debugStore;
	}

	/**
	 * The adaptor object that encapsulates all operations on generic arguments.
	 */
	@SuppressWarnings("unchecked")
	public final TypeAdapter<TransactionType, InstanceType, RoleType> typeAdapter = (TypeAdapter<TransactionType, InstanceType, RoleType>) (Object) GlobalTypeAdapter.typeAdapter;

	protected PDStoreI<TransactionType, InstanceType, RoleType> store;
	private ListenerDispatcher<TransactionType, InstanceType, RoleType> viewDispatcher = new ListenerDispatcher<TransactionType, InstanceType, RoleType>();

	protected ListenerDispatcher<TransactionType, InstanceType, RoleType> listenerDispatcher = new ListenerDispatcher<TransactionType, InstanceType, RoleType>();

	protected ListenerDispatcher<TransactionType, InstanceType, RoleType> interceptorDispatcher = new ListenerDispatcher<TransactionType, InstanceType, RoleType>();

	protected ListenerDispatcher<TransactionType, InstanceType, RoleType> immediateDispatcher = new ListenerDispatcher<TransactionType, InstanceType, RoleType>();

	protected ListenerDispatcher<TransactionType, InstanceType, RoleType> readDispatcher = new ListenerDispatcher<TransactionType, InstanceType, RoleType>();

	/**
	 * Not used directly, but necessary as a superconstructor for the subclass
	 * constructors.
	 */
	public GenericPDStore() {
	}

	/**
	 * This constructor creates a GenericPDStore just as a facade to use the
	 * convenience functions, does not do the initPDStore().
	 * 
	 * @param store
	 */
	public GenericPDStore(
			PDStoreI<TransactionType, InstanceType, RoleType> store) {
		this.store = store;
	}

	@Override
	public Collection<PDChange<TransactionType, InstanceType, RoleType>> getChanges(
			PDChange<TransactionType, InstanceType, RoleType> change) {
		// getChanges method doesn't support LINK_NOW_DELETED and
		// LINK_NOW_ADDED
		if (change.getChangeType() == ChangeType.LINK_NOW_ADDED)
			change.setChangeType(ChangeType.LINK_ADDED);
		if (change.getChangeType() == ChangeType.LINK_NOW_REMOVED)
			change.setChangeType(ChangeType.LINK_REMOVED);

		ArrayList<PDChange<TransactionType, InstanceType, RoleType>> viewResult = new ArrayList<PDChange<TransactionType, InstanceType, RoleType>>();
		List<PDChange<TransactionType, InstanceType, RoleType>> matchedChanges = new ArrayList<PDChange<TransactionType, InstanceType, RoleType>>();
		matchedChanges.add(change);

		// The call that executes all ViewListeners.
		getViewDispatcher().transactionCommitted(viewResult, matchedChanges,
				store);
		Collection<PDChange<TransactionType, InstanceType, RoleType>> changes = store
				.getChanges(change);

		if (viewResult.isEmpty())
			return changes;
		return new IteratorBasedCollection<PDChange<TransactionType, InstanceType, RoleType>>(
				new ConcatenationIterator<PDChange<TransactionType, InstanceType, RoleType>>(
						changes.iterator(), viewResult.iterator()));
	}

	public Collection<PDChange<TransactionType, InstanceType, RoleType>> getChanges(
			TransactionType transaction, InstanceType instance1, RoleType role2) {
		return getChanges(new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_EFFECTIVE, transaction, instance1, role2, null));
	}

	public Collection<PDChange<TransactionType, InstanceType, RoleType>> getChanges(
			InstanceType instance1, RoleType role2) {
		return getChanges(new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_EFFECTIVE, getCurrentTransaction(), instance1,
				role2, null));
	}

	
	/**
	 * The Listener Dispatcher where View Listeners should be registered.
	 * These ViewListeners are executed during every query with
	 * getChanges(PDChange).
	 * 
	 * These ViewListeners can be used to realize computed views.
	 * 
	 * @return
	 */
	public ListenerDispatcher<TransactionType, InstanceType, RoleType> getViewDispatcher() {
		return viewDispatcher;
	}

	public Collection<InstanceType> getInstances(TransactionType transaction,
			InstanceType instance1, RoleType role2) {
		Debug.assertTrue(instance1 != null, "instance1 must not be null.");
		Debug.assertTrue(role2 != null, "role2 must not be null.");
		Debug.assertTrue(
				!(instance1 instanceof PDInstance),
				"instance1 must not be of type PDInstance. Use the GUID of the PDInstance instead: use instance1.getId() as parameter.");

		Collection<PDChange<TransactionType, InstanceType, RoleType>> changes = getChanges(new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_EFFECTIVE, transaction, instance1, role2, null));
		Debug.println("getInstances", "ReadUncommitted");

		Collection<InstanceType> result = new ArrayList<InstanceType>();

		Iterator<PDChange<TransactionType, InstanceType, RoleType>> changesIterator = changes
				.iterator();
		while (changesIterator.hasNext()) {

			PDChange<TransactionType, InstanceType, RoleType> next = changesIterator
					.next();
			if (next.getChangeType().equals(ChangeType.LINK_ADDED))
				// add only the LINK_ADDED, not the LINK_REMOVED
				result.add(next.getInstance2());
		}
		return result;
	}

	public Collection<InstanceType> getInstances(InstanceType instance1,
			RoleType role2) {
		return getInstances(getCurrentTransaction(), instance1, role2);
	}

	public InstanceType getInstance(TransactionType transaction,
			InstanceType instance1, RoleType role2) {
		Debug.assertTrue(instance1 != null, "instance1 must not be null.");
		Debug.assertTrue(role2 != null, "role2 must not be null.");

		Collection<PDChange<TransactionType, InstanceType, RoleType>> changes = getChanges(new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_EFFECTIVE, transaction, instance1, role2, null));

		Iterator<PDChange<TransactionType, InstanceType, RoleType>> changesIterator = changes
				.iterator();
		while (changesIterator.hasNext()) {
			PDChange<TransactionType, InstanceType, RoleType> next = changesIterator
					.next();
			if (next.getChangeType().equals(ChangeType.LINK_ADDED))
				// add only the LINK_ADDED, not the LINK_REMOVED
				return next.getInstance2();
		}
		return null;
	}

	public InstanceType getInstance(InstanceType instance1, RoleType role2) {
		return getInstance(getCurrentTransaction(), instance1, role2);
	}

	public TransactionType getRepository() throws PDStoreException {
		return store.getRepository();
	}

	// //////// implicit transaction code //////////

	/**
	 * The transaction that is currently open, or null if none is open.
	 */
	private TransactionType currentTransaction;

	/**
	 * Gets the transaction that is currently open. If there is no open
	 * transaction, a new one is opened.
	 * 
	 * @return ID of the current open transaction of this PDStore
	 */
	public TransactionType getCurrentTransaction() {
		if(PDStore.isNotConcurrentNotPersistent)
			return getRepository();
		if (currentTransaction == null)
			// transactions are only started on demand
			currentTransaction = begin();
		return currentTransaction;
	}

	public boolean isInTransaction() {
		return (currentTransaction != null);
	}

	/**
	 * Sets the current transaction to be used for all methods that don't
	 * specify a transaction ID explicitly. If there is already a different
	 * current transaction set, then this is rolled back first.
	 * 
	 * @param transactionId
	 */
	public void setTransactionId(TransactionType transactionId) {
		// if there is already a transaction running, roll it back
		if (currentTransaction != null
				&& !currentTransaction.equals(transactionId))
			rollback();

		this.currentTransaction = transactionId;
	}

	public TransactionType begin() throws PDStoreException {
		return begin(IsolationLevel.SNAPSHOT);
	}

	public TransactionType begin(IsolationLevel isolationLevel) {
		return begin(typeAdapter.getBranchID(getRepository()), isolationLevel);
	}

	public TransactionType begin(IsolationLevel isolationLevel,
			PersistenceLevel persistenceLevel) {
		return begin(typeAdapter.getBranchID(getRepository()), isolationLevel,
				persistenceLevel);
	}

	public TransactionType begin(TransactionType branch) {
		return begin(branch, IsolationLevel.SNAPSHOT);
	}

	public TransactionType begin(TransactionType branch,
			IsolationLevel isolationLevel) {
		return begin(branch, isolationLevel, PersistenceLevel.LOG_AND_INDEX);
	}

	public TransactionType begin(TransactionType branch,
			IsolationLevel isolationLevel, PersistenceLevel persistenceLevel) {
		// make sure open transaction is non-first GUID
		if(PDStore.isNotConcurrentNotPersistent)
			return getCurrentTransaction();
		TransactionType transactionId = typeAdapter.getPartner(typeAdapter
				.getFirst(typeAdapter.newTransactionId(branch)));
		Transaction<TransactionType, InstanceType, RoleType> transaction = new Transaction<TransactionType, InstanceType, RoleType>(
				transactionId);
		transaction.setIsolationLevel(isolationLevel);
		transaction.setPersistenceLevel(persistenceLevel);
		return begin(transaction);
	}

	/**
	 * The most general begin method, which is called by all other begin
	 * methods.
	 */
	@Override
	public TransactionType begin(
			Transaction<TransactionType, InstanceType, RoleType> transaction) {
		return store.begin(transaction);
	}

	/**
	 * Commits the given transaction and returns a durable transaction ID if the
	 * commit succeeded, otherwise null.
	 */
	public TransactionType commit(TransactionType transaction) {
		TransactionType durableID = store.commit(transaction);

		/*
		 * if the transaction was the current transaction and committed
		 * successfully, then clear the current transaction.
		 */
		if (transaction.equals(currentTransaction) && durableID != null)
			currentTransaction = null;

		return durableID;
	}

	/**
	 * Commits the curent transaction and returns a durable transaction ID if
	 * the commit succeeded, otherwise null.
	 */
	public TransactionType commit() {
		if(currentTransaction==null) return null;
		return commit(getCurrentTransaction());
	}

	public void rollback(TransactionType transaction) {
		if (transaction.equals(currentTransaction))
			currentTransaction = null;

		store.rollback(transaction);
	}

	public void rollback() {
		rollback(getCurrentTransaction());
	}

	public void addLink(TransactionType transaction, InstanceType instance1,
			RoleType role2, InstanceType instance2) {
		Debug.assertTrue(instance1 != null, "instance1 must not be null.");
		Debug.assertTrue(role2 != null, "role2 must not be null.");
		Debug.assertTrue(instance2 != null, "instance2 must not be null.");
		Debug.assertTrue(
				!(instance1 instanceof PDInstance),
				"instance1 must not be of type PDInstance. Use the GUID of the PDInstance instead: use instance1.getId() as parameter.");
		Debug.assertTrue(
				!(instance2 instanceof PDInstance),
				"instance2 must not be of type PDInstance. Use the GUID of the PDInstance instead: use instance2.getId() as parameter.");

		addChange(new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_ADDED, transaction, instance1, role2, instance2));
	}

	public void addLink(InstanceType instance1, RoleType role2,
			InstanceType instance2) {
		addLink(getCurrentTransaction(), instance1, role2, instance2);
	}

	@Override
	public void addChange(
			PDChange<TransactionType, InstanceType, RoleType> change) {
		store.addChange(change);
	}

	/**
	 * Gets the ListenerDispatcher of this store, i.e. the object that performs
	 * template matching on the incoming changes and calls change listeners. You
	 * need to use this object to register new change listeners.
	 * 
	 * @return the ListenerDispatcher of this store
	 */
	public ListenerDispatcher<TransactionType, InstanceType, RoleType> getListenerDispatcher() {
		return listenerDispatcher;
	}

	/**
	 * Analogous to getListenerDispatcher(), works for interceptors. See javadoc
	 * in getListenerDispatcher() and in getInterceptorList() for further
	 * explanations.
	 * 
	 * @return
	 */
	public ListenerDispatcher<TransactionType, InstanceType, RoleType> getInterceptorDispatcher() {
		return interceptorDispatcher;
	}

	/**
	 * @return the immediateDispatcher
	 */
	public ListenerDispatcher<TransactionType, InstanceType, RoleType> getImmediateDispatcher() {
		return immediateDispatcher;
	}

	/**
	 * @return the readDispatcher
	 */
	public ListenerDispatcher<TransactionType, InstanceType, RoleType> getReadDispatcher() {
		return readDispatcher;
	}

	/**
	 * Modifies an existing link from instance1 using role2. If there is no
	 * existing link, it simply adds the given link.
	 * 
	 * @param transaction
	 *            the transaction to modify the link on
	 * @param instance1
	 *            the instance1 of the link to modify
	 * @param role2
	 *            the role2 of the link to modify
	 * @param instance2
	 *            the new instance2 of the link
	 */
	public void setLink(TransactionType transaction, InstanceType instance1,
			RoleType role2, InstanceType instance2) {
		InstanceType oldInstance = getInstance(transaction, instance1, role2);

		if (oldInstance != null) {
			// if link is already set, nothing to do
			if (oldInstance.equals(instance2))
				return;

			// if different link is set, remove it
			removeLink(transaction, instance1, role2, oldInstance);
		}

		addLink(transaction, instance1, role2, instance2);
	}

	/**
	 * Modifies an existing link from instance1 using role2. If there is no
	 * existing link, it simply adds the given link.
	 * 
	 * @param instance1
	 *            the instance1 of the link to modify
	 * @param role2
	 *            the role2 of the link to modify
	 * @param instance2
	 *            the new instance2 of the link
	 */
	public void setLink(InstanceType instance1, RoleType role2,
			InstanceType instance2) {
		setLink(getCurrentTransaction(), instance1, role2, instance2);
	}

	/**
	 * This method ensures that a link exists, i.e. if it is not added or is
	 * deleted, it adds it.
	 * 
	 * @param transaction
	 *            the transaction to read and write on
	 * @param instance1
	 *            the instance1 of the link
	 * @param role2
	 *            the role2 of the link
	 * @param instance2
	 *            the instance2 of the link
	 */
	public void ensureLink(TransactionType transaction, InstanceType instance1,
			RoleType role2, InstanceType instance2) {

		if (linkExists(transaction, instance1, role2, instance2))
			return;

		PDChange<TransactionType, InstanceType, RoleType> change2 = new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_ADDED, transaction, instance1, role2, instance2);
		store.addChange(change2);
	}

	public void ensureLink(InstanceType instance1, RoleType role2,
			InstanceType instance2) {
		ensureLink(getCurrentTransaction(), instance1, role2, instance2);
	}

	/**
	 * Checks whether the given link exists.
	 * 
	 * @param transaction
	 *            the transaction to read on
	 * @param instance1
	 *            the instance1 of the link
	 * @param role2
	 *            the role2 of the link
	 * @param instance2
	 *            the instance2 of the link
	 * @return true iff the given link exists
	 */
	public boolean linkExists(TransactionType transaction,
			InstanceType instance1, RoleType role2, InstanceType instance2) {
		boolean linkExists;
		PDChange<TransactionType, InstanceType, RoleType> change = new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_EFFECTIVE, transaction, instance1, role2,
				instance2);
		Collection<PDChange<TransactionType, InstanceType, RoleType>> list = getChanges(change);
		linkExists = !list.isEmpty()
				&& list.iterator().next().getChangeType() == ChangeType.LINK_ADDED;
		return linkExists;
	}

	public Collection<InstanceType> getAllInstancesInRole(
			TransactionType transaction, RoleType role) {
		return getInstances(transaction, typeAdapter.instanceFromRole(role
				.getPartner()), typeAdapter.USES_ROLE_ROLEID().getPartner());
	}

	public Collection<InstanceType> getAllInstancesInRole(RoleType role) {
		return getAllInstancesInRole(getCurrentTransaction(), role);
	}

	/**
	 * Checks if there is a link involving the given instance. An instance
	 * without a link is assumed to be non-existent.
	 * 
	 * @param transaction
	 * @param instance
	 * @return
	 */
	public boolean instanceExists(TransactionType transaction,
			InstanceType instance) {
		Debug.assertTrue(instance != null,
				"instance to look for in instanceExists must not be null.");
		Collection<PDChange<TransactionType, InstanceType, RoleType>> changes = getChanges(new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_EFFECTIVE, transaction, instance, null, null));

		Iterator<PDChange<TransactionType, InstanceType, RoleType>> changesIterator = changes
				.iterator();
		return changesIterator.hasNext();
	}

	public void removeLink(TransactionType transaction, InstanceType instance1,
			RoleType role2, InstanceType instance2) {
		Debug.assertTrue(instance1 != null, "instance1 must not be null.");
		Debug.assertTrue(role2 != null, "role2 must not be null.");
		Debug.assertTrue(instance2 != null, "instance2 must not be null.");
		Debug.assertTrue(
				!(instance1 instanceof PDInstance),
				"instance1 must not be of type PDInstance. Use the GUID of the PDInstance instead: use instance1.getId() as parameter.");
		Debug.assertTrue(
				!(instance2 instanceof PDInstance),
				"instance2 must not be of type PDInstance. Use the GUID of the PDInstance instead: use instance2.getId() as parameter.");

		PDChange<TransactionType, InstanceType, RoleType> change = new PDChange<TransactionType, InstanceType, RoleType>(
				ChangeType.LINK_REMOVED, transaction, instance1, role2,
				instance2);
		addChange(change);
	}

	public void removeLink(InstanceType instance1, RoleType role2,
			InstanceType instance2) {
		removeLink(getCurrentTransaction(), instance1, role2, instance2);
	}

	public List<PDListener<TransactionType, InstanceType, RoleType>> getDetachedListenerList()
			throws PDStoreException {
		return store.getDetachedListenerList();
	}

	public List<PDListener<TransactionType, InstanceType, RoleType>> getInterceptorList()
			throws PDStoreException {
		return store.getInterceptorList();
	}

	@Override
	public TransactionType addTransaction(
			Transaction<TransactionType, InstanceType, RoleType> transaction)
			throws PDStoreException {
		return store.addTransaction(transaction);
	}

	public Iterator<PDChange<TransactionType, InstanceType, RoleType>> iterator() {
		return store.iterator();
	}
}