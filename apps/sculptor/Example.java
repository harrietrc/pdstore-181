package sculptor;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pdstore.*;
import pdstore.generic.*;
import pdstore.notify.PDListener;
import pdstore.notify.PDListenerAdapter;

public class Example {

	public final static GUID HISTORY_ID = new GUID(
			"fd8344d005ee11e2863b842b2b9af4fd");
	public final static GUID OPERATION_ROLEID = new GUID(
			"fd836be005ee11e2863b842b2b9af4fd");
	public final static GUID NEXT_ROLEID = new GUID(
			"fd836be105ee11e2863b842b2b9af4fd");
	public final static GUID X_ROLEID = new GUID(
			"fd836be205ee11e2863b842b2b9af4fd");
	public final static GUID Y_ROLEID = new GUID(
			"fd836be305ee11e2863b842b2b9af4fd");
	public final static GUID Z_ROLEID = new GUID(
			"fd836be405ee11e2863b842b2b9af4fd");

	public final static GUID DUMMYfd836be505ee11e2863b842b2b9af4fd = new GUID(
			"fd836be505ee11e2863b842b2b9af4fd");
	public final static GUID DUMMYfd836be605ee11e2863b842b2b9af4fd = new GUID(
			"fd836be605ee11e2863b842b2b9af4fd");
	public final static GUID DUMMYfd836be705ee11e2863b842b2b9af4fd = new GUID(
			"fd836be705ee11e2863b842b2b9af4fd");
	public final static GUID DUMMYfd836be805ee11e2863b842b2b9af4fd = new GUID(
			"fd836be805ee11e2863b842b2b9af4fd");
	public final static GUID DUMMYfd836be905ee11e2863b842b2b9af4fd = new GUID(
			"fd836be905ee11e2863b842b2b9af4fd");

	static PDStore store;
	static GenericLinkedList<GUID, Object, GUID, GUID> history;

	static GUID lastProcessedOperation = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// set up store
		store = new PDStore("sculptor");
		
		// for use in a collaborative setting
		//store = PDStore.connectToServer("232.312.32.35");
		// make sure server (src/pdstore.rmi.PDStoreServer) is running first on the host

		store.setName(HISTORY_ID, "History");
		store.setName(OPERATION_ROLEID, "operation");
		store.setName(X_ROLEID, "x");
		store.setName(Y_ROLEID, "y");
		store.setName(Z_ROLEID, "z");
		store.commit();

		// set up linked list for history of sculpting operations
		history = new GenericLinkedList<GUID, Object, GUID, GUID>(store,
				HISTORY_ID, OPERATION_ROLEID, NEXT_ROLEID);

		// set up event handler to update the mesh whenever an operation was
		// used
		List<PDListener<GUID, Object, GUID>> listeners = store
				.getDetachedListenerList();
		listeners.add(new PDListenerAdapter<GUID, Object, GUID>() {
			public void transactionCommitted(
					List<PDChange<GUID, Object, GUID>> transaction,
					List<PDChange<GUID, Object, GUID>> matchedChanges,
					PDCoreI<GUID, Object, GUID> core) {

				// debugging output; prints out the changes that were received
				for (PDChange<GUID, Object, GUID> change : transaction)
					System.out.println(change);

				// processed operations need only be skipped if operations have
				// already been processed
				Iterator<GUID> operationIterator = history.iterator();
				if (lastProcessedOperation != null) {

					// skip the operations that have already been processed
					while (operationIterator.hasNext()) {
						GUID operationID = operationIterator.next();
						if (operationID.equals(lastProcessedOperation))
							break;
					}
				}

				// if there are unprocessed operations, then process them
				while (operationIterator.hasNext()) {

					GUID newOperationID = operationIterator.next();

					// draw operation on mesh
					double x = (Double) store.getInstance(newOperationID,
							X_ROLEID);
					double y = (Double) store.getInstance(newOperationID,
							Y_ROLEID);
					double z = (Double) store.getInstance(newOperationID,
							Z_ROLEID);
					
					// TODO instead of printing, do the operation on the mesh
					System.out.println(" x=" + x + " y=" + y + " z=" + z);
					
					// remember that newOperationID was processed last
					lastProcessedOperation = newOperationID;
				}
			}
		});

		// initializing the system with the old operations
		store.commit();
		
		useBrush(1, 2, 3);
		useBrush(7, 8, 9);
		useBrush(10, 11, 12);
	}

	// TODO call this for every brush operation
	static void useBrush(double x, double y, double z) {
		GUID operationID = new GUID();
		store.setName(operationID, "Operation-" + new Date());

		store.addLink(operationID, X_ROLEID, x);
		store.addLink(operationID, Y_ROLEID, y);
		store.addLink(operationID, Z_ROLEID, z);

		history.add(operationID);
		store.commit();
	}

}
