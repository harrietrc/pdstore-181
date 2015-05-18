package pdqueue.concurrency;

import java.util.ArrayList;
import java.util.List;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.dal.PDSimpleWorkingCopy;

/**
 * PDStore Connector.
 * Limits that at most one PDStore instance exist all the time. 
 * @author mcai023
 *
 */
public class PDSConnection {
	
	private static PDStore store = new PDStore("QueueBase");;
	
	/**
	 * Returns the instance of PDStore
	 * @return
	 */
	public static PDStore getStore() {
		return store;
	}
}
