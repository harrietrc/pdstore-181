package pdstore.changeindex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.PDStoreException;
import pdstore.Transaction;
import pdstore.concurrent.DetachedListenerDispatcher;
import pdstore.generic.PDStoreI;
import pdstore.generic.Pairable;
import pdstore.notify.PDListener;

public class IndexOnlyStore<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
extends
		GenericIndexStore<TransactionType, InstanceType, RoleType> {

	public IndexOnlyStore() {
		concurrentStore = this;
	}

	@Override
	public TransactionType begin() throws PDStoreException {
		return getRepository();
		//return new GUID();
	}

	@Override
	public TransactionType begin(Transaction<TransactionType, InstanceType, RoleType> transaction)
			throws PDStoreException {
		return getRepository();
		//return new GUID();
	}

	@Override
	public TransactionType commit(TransactionType transaction) throws PDStoreException {

		return getRepository();
	}

	@Override
	public void rollback(TransactionType transaction) throws PDStoreException {
	}

	private List<PDListener<TransactionType, InstanceType, RoleType>> listenerList = new CopyOnWriteArrayList<PDListener<TransactionType, InstanceType, RoleType>>();
	@Override
	public List<PDListener<TransactionType, InstanceType, RoleType>> getDetachedListenerList()
			throws PDStoreException {
		// TODO Auto-generated method stub
		return listenerList;
	}

	@Override
	public List<PDListener<TransactionType, InstanceType, RoleType>> getInterceptorList()
			throws PDStoreException {
		// TODO Auto-generated method stub
		return listenerList;
	}
	
	private boolean isRunningListeners=false;
	private LinkedBlockingQueue<PDChange<TransactionType, InstanceType, RoleType>> queue = new LinkedBlockingQueue<PDChange<TransactionType, InstanceType, RoleType>>();

	@Override
	public void addChange(PDChange<TransactionType, InstanceType, RoleType> change) {
		// TODO Auto-generated method stub
		super.addChange(change);
		if(isRunningListeners){
			// Improve execution of recursive  listener effects
			// recursive triggeing of listeners does not lead to increased return stack
			// also no recursive execution of SPARQL
			// instead, new changes added during execution of listeners are added to a queue
			// The listeners keep running until the queue is empty.
		    queue.add(change);
			return;
		} 
		isRunningListeners = true;
		do {
			List<PDChange<TransactionType, InstanceType, RoleType>> changes = new ArrayList<PDChange<TransactionType, InstanceType, RoleType>>();
			changes.add(change);
			for (PDListener<TransactionType, InstanceType, RoleType> listener : getDetachedListenerList()) {
				listener.transactionCommitted(changes, changes, this);
			}
			if(queue.isEmpty()) break;
            try {
				change = queue.take();
			} catch (InterruptedException e) {
				isRunningListeners = false;
				throw new PDStoreException("queue interrupted");
			}
		}while(true);
		isRunningListeners = false;
	}

	@Override
	public TransactionType getRepository() throws PDStoreException {
			return repositoryId;
	}

	
	
}
