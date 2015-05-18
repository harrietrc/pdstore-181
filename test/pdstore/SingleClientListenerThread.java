package pdstore;

import java.util.concurrent.Semaphore;
import pdstore.rmi.PDStore;

public class SingleClientListenerThread extends ReverseListenerThread {

	public SingleClientListenerThread(PDStore server, Semaphore waitLock) {
		super(server, waitLock);
	}
	
	public void reverseListenerCall() {
		serverForThread.newTransactions();
	}
}
