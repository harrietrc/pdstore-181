package pdstore.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStoreException;
import pdstore.Transaction;
import pdstore.notify.PDListener;

public interface PDStoreServerI extends Remote {

	/**** Misc functions ****/

	GUID getRepository() throws PDStoreException, RemoteException;

	/**** Transaction management ****/

	GUID begin(Transaction<GUID, Object, GUID> transaction)
			throws PDStoreException, RemoteException;

	GUID commit(GUID transaction) throws PDStoreException, RemoteException;

	void rollback(GUID transaction) throws PDStoreException, RemoteException;

	/**** Reading & writing ****/

	Collection<PDChange<GUID, Object, GUID>> getChanges(
			PDChange<GUID, Object, GUID> change) throws PDStoreException,
			RemoteException;

	void addChange(PDChange<GUID, Object, GUID> change)
			throws PDStoreException, RemoteException;

	/**** Listeners ****/

	List<PDListener<GUID, Object, GUID>> getDetachedListenerList()
			throws PDStoreException, RemoteException;

	List<PDListener<GUID, Object, GUID>> getInterceptorList()
			throws PDStoreException, RemoteException;

	/**
	 * This method is a method specifically for the remote PDStore It serves as
	 * the reverse implementation of a listener. The method is blocked until the
	 * next transaction commits.
	 * 
	 * @return
	 * @throws RemoteException
	 */
	List<PDChange<GUID, Object, GUID>> nextTransaction() throws RemoteException;

	List<List<PDChange<GUID, Object, GUID>>> newTransactions()
			throws RemoteException;
}
