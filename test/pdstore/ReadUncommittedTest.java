package pdstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.junit.BeforeClass;
import org.junit.Test;

import pdqueue.dal.PDItem;

/**
 * Testing the READ_UNCOMMITTED Isolation level TODO: consider merging with
 * ReadCommitted Test and use parameterized test example.
 * 
 * @author gweb017
 * @author clut002
 * @author mcai023
 */
public class ReadUncommittedTest {

	static PDStore store;

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "ReadUncommittedTest-" + System.nanoTime();
		store = new PDStore(fileName);
	}

	@Test
	public final void testReadWriteLink_GUID_GUID() {
		GUID t1 = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID t2 = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID t3 = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID t4 = store.begin(store.getRepository(), IsolationLevel.SNAPSHOT);
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		GUID guid3 = new GUID();
		GUID guid4 = new GUID();
		GUID roleA = new GUID();

		assertEquals(t3.getBranchID(), t4.getBranchID());
		assertEquals(t2.getBranchID(), t4.getBranchID());
		assertEquals(t1.getBranchID(), t4.getBranchID());

		// READ UNCOMMITTED transactions
		store.addLink(t1, guid1, roleA, guid2);
		store.addLink(t2, guid1, roleA, guid3);
		// SNAPSHOT transaction
		store.addLink(t4, guid1, roleA, guid4);

		// Debug.addDebugTopic("ReadUncommitted");

		Collection<Object> resultReadUncommitted = store.getInstances(t3,
				guid1, roleA);
		Collection<Object> resultSNAPSHOT = store
				.getInstances(t4, guid1, roleA);

		Debug.println("assert", "ReadUncommitted");
		assertTrue(resultSNAPSHOT.contains(guid4));
		Debug.println("assert", "ReadUncommitted");
		assertTrue(!resultSNAPSHOT.contains(guid3));
		Debug.println("assert", "ReadUncommitted");
		assertTrue(!resultSNAPSHOT.contains(guid2));

		Debug.println("assert", "ReadUncommitted");
		assertTrue(resultReadUncommitted.contains(guid4));
		Debug.println("assert", "ReadUncommitted");
		assertTrue(resultReadUncommitted.contains(guid3));
		Debug.println("assert", "ReadUncommitted");
		assertTrue(resultReadUncommitted.contains(guid2));
		// Debug.removeDebugTopic("ReadUncommitted");

		store.commit(t1);
		store.commit(t2);
		store.commit(t3);
		store.commit(t4);

		// Read links after commit
		GUID tRU = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID tSS = store.begin(store.getRepository(), IsolationLevel.SNAPSHOT);

		resultReadUncommitted = store.getInstances(tRU, guid1, roleA);
		resultSNAPSHOT = store.getInstances(tSS, guid1, roleA);
		assertTrue(resultReadUncommitted.contains(guid2)
				&& resultReadUncommitted.contains(guid3)
				&& resultReadUncommitted.contains(guid4));
		assertTrue(resultSNAPSHOT.contains(guid4)
				&& resultSNAPSHOT.contains(guid3)
				&& resultSNAPSHOT.contains(guid2));
	}

	@Test
	public final void testReadWriteLink() {
		GUID t1 = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID t2 = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID t3 = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID t4 = store.begin(store.getRepository(), IsolationLevel.SNAPSHOT);
		PDItem item = new PDItem(store);
		String one = "one";
		String two = "two";
		String three = "three";

		// READ UNCOMMITTED transactions
		store.addLink(t1, item.getId(), PDItem.roleMessageId, one);
		store.addLink(t2, item.getId(), PDItem.roleMessageId, two);
		// SNAPSHOT transaction
		store.addLink(t4, item.getId(), PDItem.roleMessageId, three);

		Collection<Object> resultReadUncommitted = store.getInstances(t3,
				item.getId(), PDItem.roleMessageId);
		Collection<Object> resultSNAPSHOT = store.getInstances(t4,
				item.getId(), PDItem.roleMessageId);

		assertTrue(resultSNAPSHOT.contains(three));
		assertTrue(!resultSNAPSHOT.contains(two));
		assertTrue(!resultSNAPSHOT.contains(one));
		assertTrue(resultReadUncommitted.contains(one));
		assertTrue(resultReadUncommitted.contains(two));
		assertTrue(resultReadUncommitted.contains(three));

		store.commit(t1);
		store.commit(t2);
		store.commit(t3);
		store.commit(t4);

		// Read links after commit
		GUID tRU = store.begin(store.getRepository(),
				IsolationLevel.READ_UNCOMMITTED);
		GUID tSS = store.begin(store.getRepository(), IsolationLevel.SNAPSHOT);
		resultReadUncommitted = store.getInstances(tRU, item.getId(),
				PDItem.roleMessageId);
		resultSNAPSHOT = store.getInstances(tSS, item.getId(),
				PDItem.roleMessageId);
		assertTrue(resultReadUncommitted.contains(one)
				&& resultReadUncommitted.contains(two)
				&& resultReadUncommitted.contains(three));
		assertTrue(resultSNAPSHOT.contains(three)
				&& resultSNAPSHOT.contains(two) && resultSNAPSHOT.contains(one));
	}
}
