package replica;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import nz.ac.auckland.se.genoupe.tools.Debug;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.rmi.PDStoreServer;

public class ReplicaDemoPerfTest {

	private final PDStore prime;

	public static void main(String[] args) {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			new ReplicaDemoPerfTest();
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
	public ReplicaDemoPerfTest() throws AlreadyBoundException, RemoteException {
		/*
		 * Currently causes infinite loop as client commits propagate to the
		 * server, which results in a commit on the server which comes back to
		 * client and so on..
		 */

		boolean useclient = true;
		this.prime = new PDStore();
		PDStoreServer server = new pdstore.rmi.PDStoreServer(this.prime,
				"PDStore");
		Debug.println("Bound server");

//		PDStoreLocal sec = PDStore.connectToServerReplica("PDStoreLocal",
//				"localhost", "PDStore");
		PDStore sec = PDStore.connectToServer("localhost");
		Debug.println("Acquired server link");

		GUID dtid = null;
		GUID role = new GUID("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		int writeCount = 1000;
		int readCount = 0;
		
		long start = System.currentTimeMillis();
		/* Add one link, retrieve one link */
		for (int i = 0 ; i < writeCount; i++) {
			GUID tid = sec.begin();
			sec.addLink(tid, i, role, i);
			dtid = sec.commit(tid);
		}
		
		long endWrite = System.currentTimeMillis();
		
		for (int i = 0 ; i < readCount; i++) {
			sec.getInstances(dtid, i % writeCount, role);
		}
		long end = System.currentTimeMillis();
		Debug.println("Write " + writeCount + " values: " + (endWrite - start) + " ms");
		Debug.println("Read " + readCount + " values: " + (end - endWrite) + " ms");
		Debug.println("Total: " + (end - start) + " ms");
	}
}