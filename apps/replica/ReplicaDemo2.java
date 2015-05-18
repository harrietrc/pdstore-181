package replica;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import nz.ac.auckland.se.genoupe.tools.Debug;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.rmi.PDStoreLocal;
import pdstore.rmi.PDStoreServer;

public class ReplicaDemo2 {

	private final PDStore prime;

	public static void main(String[] args) {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			new ReplicaDemo2();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws AlreadyBoundException
	 * @throws RemoteException
	 */
	public ReplicaDemo2() throws AlreadyBoundException, RemoteException {
		/*
		 * Currently causes infinite loop as client commits propagate to the
		 * server, which results in a commit on the server which comes back to
		 * client and so on..
		 */

		boolean useclient = true;
		this.prime = new PDStore();
		PDStoreServer server = new pdstore.rmi.PDStoreServer(this.prime,
				"PDStore");
		PDStore serverStore = server.getStore();
		Debug.println("Bound server");

		PDStoreLocal sec = PDStore.connectToServerReplica("PDStoreLocal",
				"localhost", "PDStore");
		Debug.println("Acquired server link");

		Integer i2 = new Integer(2);
		GUID tid, dtid;
		GUID role = new GUID("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		if (useclient) {
			Debug.println("Running transaction on client...");
			tid = sec.begin();
			sec.addLink(tid, i2, role, 3);
			dtid = sec.commit(tid);

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			tid = sec.begin();
			sec.addLink(tid, i2, role, 4);
			dtid = sec.commit(tid);
		} else {
			Debug.println("Running transaction on server...");
			tid = serverStore.begin();
			serverStore.addLink(tid, i2, role, 3);
			dtid = serverStore.commit(tid);

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			tid = serverStore.begin();
			serverStore.addLink(tid, i2, role, 4);
			dtid = serverStore.commit(tid);
		}
				
		Debug.println("Found (client): "
				+ sec.getInstances(dtid, i2, role));
		Debug.println("Found (server): "
				+ serverStore.getInstances(dtid, i2, role));
		Debug.println("Using dtid " + dtid);
	}
}