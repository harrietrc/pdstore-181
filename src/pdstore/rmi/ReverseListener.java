package pdstore.rmi;

import java.rmi.RemoteException;
import java.util.List;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.PDStoreI;
import pdstore.notify.PDListener;

/**
 * This class is running on the client side in a separate thread, listening for
 * remote changes. This current version is prototypical, a later version must
 * receive a list of transactions.
 * 
 * @author Gerald
 * 
 */
public class ReverseListener implements Runnable {
	
	private final PDStoreI<GUID, Object, GUID> core;
	private final PDStoreServerI serverInterface;

	/**
	 * Receives the local PDStore.
	 * 
	 * @param source
	 */
	public ReverseListener(PDStore source) {
		super();
		this.serverInterface = source.getServer();
		this.core = source;
	}

	public ReverseListener(PDStoreServerI serverInterface, PDStoreI<GUID, Object, GUID> core) {
		super();
		this.serverInterface = serverInterface;
		this.core = core;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (;;) {
			long time = System.nanoTime();
			List<PDChange<GUID, Object, GUID>> nextTransaction;
			try {
				nextTransaction = this.serverInterface.nextTransaction();
			} catch (RemoteException e) {
				// capture if the operation was closed by remote host
				// but don't try too often.
				long now = System.nanoTime();
				if (now - time < 10000000) { // was less than 10 ms
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e1);

					}
				}
				continue;
			}

			// This seems somewhat of a reversal of the existing dynamic
			for (PDListener<GUID, Object, GUID> listener : this.core
					.getDetachedListenerList()) {
				listener.transactionCommitted(nextTransaction, null, this.core);
			}
		}
	}

}
