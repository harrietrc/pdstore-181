package replica;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.Debug;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.PDStoreException;
import pdstore.Transaction;
import pdstore.generic.GlobalTypeAdapter;
import pdstore.generic.PDCoreI;
import pdstore.generic.TypeAdapter;
import pdstore.notify.PDListener;
import pdstore.rmi.PDStoreServerI;

/**
 * Detached listener that recieves remote transactions and applies them to a
 * local {@link PDStore}.
 * 
 * @author jker040, mfow017
 */
public class ReplicaServerDetachedListener implements
		PDListener<GUID, Object, GUID> {

	private final PDStoreServerI server;

	public ReplicaServerDetachedListener(PDStoreServerI pdStore) {
		this.server = pdStore;
	}

	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction, // are these for the
															// same transaction?
			List<PDChange<GUID, Object, GUID>> matchedChanges, // what are
																// these?
			PDCoreI<GUID, Object, GUID> core) // ditto?
	{
		try {
			GUID liveTransactionID = null;

			// TODO the begin does not use a proper trabsnaction ID
			// it was changed because it is not ok to use the server
			// object directly like that, and the server interface has changed
			liveTransactionID = this.server
					.begin(new Transaction<GUID, Object, GUID>(new GUID()));
			Debug.println("Pushing transaction '" + liveTransactionID
					+ "' to remote-replica");
			for (PDChange<GUID, Object, GUID> change : transaction) {
				change.setTransaction(liveTransactionID);
				this.server.addChange(change);
			}
			this.server.commit(liveTransactionID);
		} catch (PDStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		// can this be used to only listen to certain changes?
		return null;
	}
}
