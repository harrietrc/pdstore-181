package pdstore.rmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.Transaction;
import pdstore.notify.PDListener;
import pdstore.notify.PDStoreListenerService;

/**
 * RMi server for PDStore.
 * 
 * NOTE:
 * 
 * If automatic server setup as done in static initializer does not work you
 * have to ensure the following steps manually:
 * 
 * 1. First, you have to run the rmiregistry tool from the Java JRE.
 * 
 * 2. YOU HAVE TO RUN THIS CLASS WITH THE FOLLOWING VM ARGUMENT:
 * -Djava.rmi.server.codebase=file:${workspace_loc}/pdstore/bin/
 * 
 * You can set the argument in the run configuration.
 * 
 * NOTE:
 * 
 * Before you run PDStoreServer, make sure NO instance of rmiregistry.exe is
 * running already. Terminate it in the task manager if it is already running.
 * 
 * @author clut002
 * 
 */
public class PDStoreServer implements PDStoreServerI {

	PDStore store;

	public PDStore getStore() {
		return store;
	}

	static Process rmiregistry;

	static {
		System.setProperty("java.rmi.server.codebase",
				"file:" + System.getProperty("user.dir") + "/bin/");
		try {
			// server needs debug output in a file
			Debug.err.setOutput(new PrintStream("PDStoreServer.log"));

			// check if rmiregistry is already running
			boolean isRMIStarted = false;
			Process p = null;
			if (System.getProperty("os.name").contains("Windows")) {
				// NH - No header; fi - filters for specific process
				p = Runtime.getRuntime().exec(
						"tasklist /NH /fi " + "\""
								+ "ImageName eq rmiregistry.exe" + "\"");
			} else {
				p = Runtime.getRuntime().exec("ps -A");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("rmiregistry")) {
					isRMIStarted = true;
					break;
				}
			}

			if (!isRMIStarted)
				rmiregistry = Runtime.getRuntime().exec(
						System.getProperty("java.home") + "/bin/rmiregistry");

		} catch (IOException e) {
			Debug.println(e);
		}
	}

	@Override
	protected void finalize() {
		// terminate rmiregistry.exe, so there will be only one instance
		// if PDStoreServer is restarted.
		// This is not working yet, probably due to Java.
		rmiregistry.destroy();
	}

	public static void main(String[] args) {
		try {

			new PDStoreServer();
		} catch (Exception e) {
			Debug.println(e);
		}
	}

	public PDStoreServer() throws RemoteException, AlreadyBoundException {
		this(new PDStore("pdstore"));
	}

	public PDStoreServer(PDStore store) throws RemoteException,
			AlreadyBoundException {
		this(store, "PDStore");
	}

	public PDStoreServer(PDStore store, String rmiKey) throws RemoteException,
			AlreadyBoundException {
		this.store = store;
		store.getDetachedListenerList().add(
				new ServerSideMainListener(this.service));
		PDStoreServerI stub = (PDStoreServerI) UnicastRemoteObject
				.exportObject(this, 0);

		// Bind the remote object's stub in the registry
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind(rmiKey, stub);
	}

	/**** Misc functions ****/

	@Override
	public GUID getRepository() {
		return store.getRepository();
	}

	/**** Transaction management ****/

	@Override
	public GUID begin(Transaction<GUID, Object, GUID> transaction) {
		return store.begin(transaction);
	}

	@Override
	public GUID commit(GUID transaction) {
		return store.commit(transaction);
	}

	@Override
	public void rollback(GUID transaction) {
		store.rollback(transaction);
	}

	/**** Reading & writing ****/

	@Override
	public Collection<PDChange<GUID, Object, GUID>> getChanges(
			PDChange<GUID, Object, GUID> change) {
		/*
		 * Get all result changes from the IteratorBasedCollection returned by
		 * getChanges and copy them into a new, non-iterator-based collection to
		 * send over the network. TODO we may want to change this later on so
		 * that the results are passe chunk-wise to the client, i.e. not all the
		 * result elements need to be retrieved upfront
		 */
		Collection<PDChange<GUID, Object, GUID>> result = new ArrayList<PDChange<GUID, Object, GUID>>(
				store.getChanges(change));
		return result;
	}

	@Override
	public void addChange(PDChange<GUID, Object, GUID> change) {
		store.addChange(change);
	}

	/**** Listeners ****/

	@Override
	public List<PDListener<GUID, Object, GUID>> getDetachedListenerList() {
		return store.getDetachedListenerList();
	}

	@Override
	public List<PDListener<GUID, Object, GUID>> getInterceptorList() {
		return store.getInterceptorList();
	}

	@Override
	public List<PDChange<GUID, Object, GUID>> nextTransaction() {
		return getService().nextMessage();
	}

	@Override
	public List<List<PDChange<GUID, Object, GUID>>> newTransactions() {
		return getService().newMessages();
	}

	private final PDStoreListenerService<List<PDChange<GUID, Object, GUID>>> service = new PDStoreListenerService<List<PDChange<GUID, Object, GUID>>>();

	public PDStoreListenerService<List<PDChange<GUID, Object, GUID>>> getService() {
		return this.service;
	}
}
