package replica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import nz.ac.auckland.se.genoupe.tools.Debug;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;
import pdstore.rmi.PDStoreLocal;

/**
 * Detached listener that recieves remote transactions and applies them to a local {@link PDStore}.
 * @author jker040, mfow017
 */
public class ReplicaClientDetachedListener implements PDListener<GUID, Object, GUID> {
	
	private final PDStoreLocal localRmi;
	private final PDStore local;
	
	public ReplicaClientDetachedListener(PDStore local, PDStoreLocal localRmi) {
		this.local = local;
		this.localRmi = localRmi;
	}

	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction, //are these for the same transaction?
			List<PDChange<GUID, Object, GUID>> matchedChanges, //what are these?
			PDCoreI<GUID, Object, GUID> core) //ditto? 
	{
		GUID liveTransactionID = null;
		/* Wait until local commits have finished */
		synchronized (this.localRmi.commitLock) {
			if (transaction != null && transaction.size() > 0) {
				liveTransactionID = transaction.get(0).getTransaction();
				/* Extract the list of transactions */
				Set<GUID> allTransactions = new HashSet();
				for(PDChange<GUID, Object, GUID> change : transaction) {
					allTransactions.add(change.getTransaction());
				}
				GUID durable = allTransactions.iterator().next();
				
				/* If this transaction originated from the local pdstore we can ignore the commit */
				if(!this.localRmi.getRecentTransactions().removeAll(allTransactions)) {
					Debug.println("[" + this.localRmi.getStoreName() + "] Pulling transactions " + allTransactions + " from replica-server");
					liveTransactionID = this.local.begin();
					for (PDChange<GUID, Object, GUID> change : transaction) {
						change.setTransaction(liveTransactionID);
						this.local.addChange(change);
					}
					Debug.println("[" + this.localRmi.getStoreName() + "] set transaction '" + liveTransactionID + "' dtid='" + durable + "'" );
					this.local.getTransaction(liveTransactionID).setDurableId(durable);
					GUID dtid = this.local.commit(liveTransactionID); 
					Debug.println("[" + this.localRmi.getStoreName() + "] dtid: " + dtid);
				}
				else {
					Debug.println("[" + this.localRmi.getStoreName() + "] Not processing transaction '" + liveTransactionID + "'");
				}
			
			}
		}
	}
	
	@Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		// can this be used to only listen to certain changes?
		return null;
	}
}
