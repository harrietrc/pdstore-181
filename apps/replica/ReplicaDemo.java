package replica;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.rmi.PDStoreServer;

public class ReplicaDemo {


	private final PDStore prime;
	
	public static void main(String[] args) {
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			new ReplicaDemo();
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
	public ReplicaDemo() throws AlreadyBoundException, RemoteException {
		boolean useclient = false;
		this.prime = new PDStore();
		PDStoreServer server = new pdstore.rmi.PDStoreServer(this.prime, "PDStore");
		PDStore serverStore = server.getStore();
		System.out.println("Bound server");

		PDStore sec = PDStore.connectToServer("localhost");
		System.out.println("Acquired server link");

		Integer i2 = new Integer(2);
		GUID tid, dtid;
		GUID role = new GUID("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		if (useclient) {
			System.err.println("Running transaction on client...");
			tid = sec.begin();
			sec.addLink(tid, i2, role, 3);
			dtid = sec.commit(tid);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			
			tid = serverStore.begin();
			serverStore.addLink(tid, i2, role, 4);
			dtid = serverStore.commit(tid);

		} else {
			System.err.println("Running transaction on server...");
			tid = serverStore.begin();
			serverStore.addLink(tid, i2, role, 3);
			dtid = serverStore.commit(tid);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			
			tid = serverStore.begin();
			serverStore.addLink(tid, i2, role, 4);
			dtid = serverStore.commit(tid);
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		System.out.println("Found (client): "+    sec.getInstances(dtid, i2, role.getPartner()));
		System.out.println("Found (server): "+ serverStore.getInstances(dtid, i2, role));
	}
}