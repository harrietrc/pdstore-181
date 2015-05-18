package pdstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import pdstore.sparql.Variable;

/**
 * These are the main tests for PDStore. Note that this tests only functions
 * that do NOT make use of models. Hence, to make debugging easier, the PD
 * metamodel is NOT included in the pds file used for the tests.
 * 
 * All the testing of models (i.e. typed instances and relations) is done in
 * class PDStoreModelsTest.
 * 
 * @author clut002
 * 
 */
public class PDStoreTest {

	static PDStore store;

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "PDStoreTest-" + System.nanoTime();
		store = new PDStore(fileName);
	}

	@Test
	public final void testGetRepositoryGuid() {
		GUID repository = store.getRepository();
		assertTrue(repository != null);
	}

	/*
	 * TODO add again when has-type links are again inferred using the new view
	 * concept public final void testGetHasLink() { GUID transaction =
	 * store.begin();
	 * 
	 * assertEquals(PDStore.STRING_TYPEID, store.getInstance(transaction,
	 * "hello", PDStore.HAS_TYPE_ROLEID)); assertEquals(PDStore.INTEGER_TYPEID,
	 * store.getInstance(transaction, 123, PDStore.HAS_TYPE_ROLEID));
	 * assertEquals(PDStore.DOUBLE_PRECISION_TYPEID,
	 * store.getInstance(transaction, 0.123, PDStore.HAS_TYPE_ROLEID));
	 * assertEquals(PDStore.BOOLEAN_TYPEID, store.getInstance(transaction, true,
	 * PDStore.HAS_TYPE_ROLEID));
	 * 
	 * store.commit(transaction); }
	 */
	@Test
	public final void testReadWriteLink_GUID_GUID() {
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		GUID guid3 = new GUID();
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, guid1, roleA, guid2);
		Collection<Object> result = store.getInstances(transaction, guid1,
				roleA);
		assertTrue(result.contains(guid2));

		store.removeLink(transaction, guid1, roleA, guid2);
		result = store.getInstances(transaction, guid1, roleA);

		assertTrue(!result.contains(guid2));

		store.addLink(transaction, guid1, roleA, guid2);
		store.addLink(transaction, guid1, roleA, guid3);
		store.commit(transaction);
		transaction = store.begin();
		result = store.getInstances(transaction, guid1, roleA);
		assertTrue(result.size() >= 2);
		assertTrue(result.contains(guid2));
		assertTrue(result.contains(guid3));

		store.commit(transaction);
	}

	@Test
	public final void testReadWriteLink_String_String() {
		String stringHello = "hello";
		String stringWorld = "world";
		String stringGood = "good";
		String stringMorning = "morning";
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, stringHello, roleA, stringWorld);
		Collection<Object> result = store.getInstances(transaction,
				stringHello, roleA);
		assertTrue(result.contains(stringWorld));

		store.removeLink(transaction, stringHello, roleA, stringWorld);
		result = store.getInstances(transaction, stringHello, roleA);
		assertTrue(!result.contains(stringWorld));

		store.addLink(transaction, stringHello, roleA, stringGood);
		store.addLink(transaction, stringHello, roleA, stringMorning);
		store.commit(transaction);
		transaction = store.begin();
		result = store.getInstances(transaction, stringHello, roleA);
		assertTrue(result.size() >= 2);
		assertTrue(result.contains(stringGood));
		assertTrue(result.contains(stringMorning));

		store.commit(transaction);
	}

	@Test
	public final void testReadWriteLink_longString_longString() {
		String stringNews = "Nearly half a million Kiwi driving licences have expired. Photo  File Quickread Nearly half a million New Zealand drivers licences are expired and thousands of motorists are apparently";
		String stringWorld = "world";
		String stringGood = "good";
		String stringNews1 = "Firefighters have managed to subdue the massive fire that englufed a Masterton garden centre earlier this afternoon";
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, stringNews, roleA, stringWorld);
		Collection<Object> result = store.getInstances(transaction,
				stringNews, roleA);
		assertTrue(result.contains(stringWorld));

		store.removeLink(transaction, stringNews, roleA, stringWorld);
		result = store.getInstances(transaction, stringNews, roleA);
		assertTrue(!result.contains(stringWorld));

		store.addLink(transaction, stringNews, roleA, stringGood);
		store.addLink(transaction, stringNews, roleA, stringNews1);
		store.commit(transaction);
		transaction = store.begin();
		result = store.getInstances(transaction, stringNews, roleA);
		assertTrue(result.size() >= 2);
		assertTrue(result.contains(stringGood));
		assertTrue(result.contains(stringNews1));

		store.commit(transaction);
	}
	
	@Test
	public final void testReadWriteLink_int_String() {
		String stringNews = "Nearly half a million Kiwi driving licences have expired. Photo  File Quickread Nearly half a million New Zealand drivers licences are expired and thousands of motorists are apparently";
		int aRanndomeNumber = 10;
		int anotherRanndomeNumber = 1000;
		String stringNews1 = "Firefighters have managed to subdue the massive fire that englufed a Masterton garden centre earlier this afternoon";
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, stringNews, roleA, aRanndomeNumber);
		Collection<Object> result = store.getInstances(transaction,
				stringNews, roleA);
		assertTrue(result.contains((long)aRanndomeNumber));

		store.removeLink(transaction, stringNews, roleA, aRanndomeNumber);
		result = store.getInstances(transaction, stringNews, roleA);
		assertTrue(!result.contains(aRanndomeNumber));

		store.addLink(transaction, stringNews, roleA, anotherRanndomeNumber);
		store.addLink(transaction, stringNews, roleA, stringNews1);
		store.commit(transaction);
		transaction = store.begin();
		result = store.getInstances(transaction, stringNews, roleA);
		assertTrue(result.size() >= 2);
		assertTrue(result.contains((long)anotherRanndomeNumber));
		assertTrue(result.contains(stringNews1));

		store.commit(transaction);
	}
	
	@Test
	public final void testReadWriteLink_double_string_blob() {
		Blob ablob = new Blob(new byte[] { 9, 9, 9, 9, 127});
		double aRanndomeNumber = 1.01234231234;
		double anotherRanndomeNumber = 1000.9999999;
		String stringNews1 = "One of the things about having terminal cancer is that your imminent death drags on";
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, ablob, roleA, aRanndomeNumber);
		Collection<Object> result = store.getInstances(transaction,
				ablob, roleA);
		assertTrue(result.contains(aRanndomeNumber));

		store.removeLink(transaction, ablob, roleA, aRanndomeNumber);
		result = store.getInstances(transaction, ablob, roleA);
		assertTrue(!result.contains(aRanndomeNumber));

		store.addLink(transaction, ablob, roleA, anotherRanndomeNumber);
		store.addLink(transaction, ablob, roleA, stringNews1);
		store.addLink(transaction, stringNews1, roleA, ablob);
		store.commit(transaction);
		transaction = store.begin();
		result = store.getInstances(transaction, ablob, roleA);
		assertTrue(result.size() >= 2);
		assertTrue(result.contains(anotherRanndomeNumber));
		assertTrue(result.contains(stringNews1));
		
		result = store.getInstances(transaction, stringNews1, roleA);
		assertTrue(result.contains(ablob));
		store.commit(transaction);
	}
	
	@Test
	public final void testReadWriteLink_boolean_timestamp_blob() {
		Blob ablob = new Blob(new byte[] { 9, 9, 9, 9, 127});
		boolean alwaysTrue = false;
		Timestamp aTimeStamp = new Timestamp(100000000L); 
		Blob anotherblob = new Blob(new byte[] { -127, 9, 9, 9, 127});
		GUID roleA = new GUID();
		
		GUID transaction = store.begin();
		store.addLink(transaction, ablob, roleA, alwaysTrue);
		Collection<Object> result = store.getInstances(transaction,
				ablob, roleA);
		assertTrue(result.contains(alwaysTrue));

		store.removeLink(transaction, ablob, roleA, alwaysTrue);
		result = store.getInstances(transaction, ablob, roleA);
		assertTrue(!result.contains(alwaysTrue));

		store.addLink(transaction, ablob, roleA, aTimeStamp);
		store.addLink(transaction, ablob, roleA, anotherblob);
		store.addLink(transaction, ablob, roleA, alwaysTrue);
		store.addLink(transaction, aTimeStamp, roleA, anotherblob);
		
		store.commit(transaction);
		transaction = store.begin();
		result = store.getInstances(transaction, ablob, roleA);
		assertTrue(result.size() >= 3);
		assertTrue(result.contains(aTimeStamp));
		assertTrue(result.contains(anotherblob));
		assertTrue(result.contains(alwaysTrue));
		
		result = store.getInstances(transaction, aTimeStamp, roleA);
		assertTrue(result.contains(anotherblob));
		store.commit(transaction);
	}
	
	
	//added this test because of the failure of generating large number of tripples using CreateLibaryBenchmark
	@Test
	public final void stressTestReadWriteLink_double_string_blob() {
		Blob ablob = new Blob(new byte[] { 9, 9, 9, 9, 127});
		double anotherRanndomeNumber = 1000.9999999;
		String stringNews1 = "One of the things about having terminal cancer is that your imminent death drags on";
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		
		for (int i = 0; i < 100; i++)
		{
			store.addLink(transaction, ablob, roleA, anotherRanndomeNumber + i);
			store.addLink(transaction, ablob, roleA, stringNews1 + " " + i);
			store.addLink(transaction, stringNews1, roleA, ablob);
		}

		store.addLink(transaction, ablob, roleA, anotherRanndomeNumber);
		store.addLink(transaction, ablob, roleA, stringNews1);
		store.addLink(transaction, stringNews1, roleA, ablob);
		
		store.commit(transaction);
		transaction = store.begin();
		Collection<Object> result = store.getInstances(transaction, ablob, roleA);
		assertTrue(result.size() >= 201);
		assertTrue(result.contains(anotherRanndomeNumber));
		assertTrue(result.contains(stringNews1));
		
		result = store.getInstances(transaction, stringNews1, roleA);
		assertTrue(result.contains(ablob));
		store.commit(transaction);
	}
	
	@Test
	public final void stressTestReadWriteLink_string_int() {
		String aString = "Full Day of Fishing, Rod Hire, Bait ";
		int anotherRanndomeNumber = 1000;
		String stringNews1 = "One of the things about having terminal cancer is that your imminent death drags on";
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		
		for (int i = 0; i < 100; i++)
		{
			store.addLink(transaction, aString, roleA, anotherRanndomeNumber + i);
			store.addLink(transaction, aString, roleA, stringNews1 + " " + i);
			store.addLink(transaction, stringNews1, roleA, aString);
		}

		store.addLink(transaction, aString, roleA, anotherRanndomeNumber);
		store.addLink(transaction, aString, roleA, stringNews1);
		store.addLink(transaction, stringNews1, roleA, aString);
		
		store.commit(transaction);
		transaction = store.begin();
		Collection<Object> result = store.getInstances(transaction, aString, roleA);
		assertTrue(result.size() >= 201);
		
		assertTrue(result.contains((long)anotherRanndomeNumber));
		assertTrue(result.contains(stringNews1));
		
		result = store.getInstances(transaction, stringNews1, roleA);
		assertTrue(result.contains(aString));
		store.commit(transaction);
	}
	
	@Test
	public final void testTransactionGuids() {
		GUID transaction = store.begin();
		assertTrue(!transaction.isFirst());

		store.addLink(transaction, new GUID(), new GUID(), new GUID());
		GUID durableTransaction = store.commit(transaction);
		assertTrue(durableTransaction.isFirst());
	}

	@Test
	public final void testGetInstances() {
		GUID myAccount = new GUID();
		GUID numberRoleId = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, myAccount, numberRoleId, 99);
		store.addLink(transaction, myAccount, numberRoleId, 98);
		transaction = store.commit(transaction);

		Collection<Object> result = store.getInstances(transaction, myAccount,
				numberRoleId);

		// This must support both number types currently
		// Since cached stores return small ints.
		assertTrue(result.contains(99L) || result.contains(99));
		assertTrue(result.contains(98L) || result.contains(98));
	}

	@Test
	public final void testGetId() {
		GUID guid1 = new GUID();

		GUID transaction = store.begin();
		store.setName(transaction, guid1, "Hello");
		assertEquals(guid1, store.getId(transaction, "Hello"));

		GUID durableTransaction = store.commit(transaction);
		assertEquals(guid1, store.getId(durableTransaction, "Hello"));
	}

	@Test
	public final void testGetIds() {
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		String name = "testGetIds";

		GUID transaction = store.begin();
		store.setName(transaction, guid1, name);
		store.setName(transaction, guid2, name);

		Collection<GUID> result = store.getIds(transaction, name);
		assertEquals(result.size(), 2);
		assertTrue(result.contains(guid1));
		assertTrue(result.contains(guid2));

		// retest after commit
		store.commit(transaction);
		result = store.getIds(transaction, name);
		assertEquals(result.size(), 2);
		assertTrue(result.contains(guid1));
		assertTrue(result.contains(guid2));
	}

	@Test
	public final void testSetGetRemoveName() {
		GUID guid1 = new GUID();

		GUID transaction = store.begin();
		store.setName(transaction, guid1, "Hello");
		String name = store.getName(transaction, guid1);
		assertEquals("Hello", name);

		store.setName(transaction, guid1, "Hello2");
		name = store.getName(transaction, guid1);
		assertEquals("Hello2", name);

		// TODO what is the semantics of remove? Does it remove all instances?
		store.removeName(transaction, guid1);
		name = store.getName(transaction, guid1);
		assertTrue(name == null);

		store.commit(transaction);
	}

	@Test
	public final void testRemoveIcon() {
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		Blob blob999 = new Blob(new byte[] { 9, 9, 9 });
		GUID role2Id = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, guid1, role2Id, guid2);
		store.setIcon(transaction, guid1, blob999);
		store.removeIcon(transaction, guid1);
		assertEquals(store.getIcon(transaction, guid1), null);
	}

	@Test
	public final void testRemoveName() {
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		GUID role2Id = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, guid1, role2Id, guid2);
		store.setName(transaction, guid1, "HiHello");
		store.removeName(transaction, guid1);
		assertEquals(store.getName(transaction, guid1), null);
	}

	@Test
	public final void testSetAndGetIcon() {
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		Blob blob999 = new Blob(new byte[] { 9, 9, 9 });
		GUID role2Id = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, guid1, role2Id, guid2);
		store.setIcon(transaction, guid1, blob999);
		assertEquals(store.getIcon(transaction, guid1), blob999);
	}

	@Test
	public final void testRollback() {
		GUID transaction = store.begin();
		store.rollback(transaction);
	}

	@Test
	public final void testReadWrite_Double() {
		GUID guid1 = new GUID();
		GUID roleA = new GUID();

		GUID transaction1 = store.begin();
		store.addLink(transaction1, guid1, roleA, 9.9);
		Collection<Object> result = store.getInstances(transaction1, guid1,
				roleA);
		assertTrue(result.contains(9.9));
		transaction1 = store.commit(transaction1);

		GUID transaction2 = store.begin();
		store.removeLink(transaction2, guid1, roleA, 9.9);
		store.addLink(transaction2, guid1, roleA, 1.1);
		result = store.getInstances(transaction2, guid1, roleA);
		assertTrue(result.contains(1.1));
		transaction2 = store.commit(transaction2);

		result = store.getInstances(transaction1, guid1, roleA);
		assertTrue(result.contains(9.9));
	}

	@Test
	public final void testHistoryDoubleInstance() {
		GUID RateRoleId = new GUID();
		GUID myAccount = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, myAccount, RateRoleId, 9.9);
		assertEquals(9.9, store.getInstance(transaction, myAccount, RateRoleId));
		GUID transaction1 = store.commit(transaction);

		Object result = store.getInstance(transaction1, myAccount, RateRoleId);
		assertEquals(9.9, result);

		transaction = store.begin();
		store.removeLink(transaction, myAccount, RateRoleId, 9.9);
		store.addLink(transaction, myAccount, RateRoleId, 1.1);
		GUID transaction2 = store.commit(transaction);

		Object result2 = store.getInstance(transaction2, myAccount, RateRoleId);
		assertEquals(1.1, result2);
	}

	@Test
	public final void testBooleanInstance() {
		GUID guid1 = new GUID();
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, guid1, roleA, true);
		store.commit(transaction);

		transaction = store.begin();
		assertEquals(true, store.getInstance(transaction, guid1, roleA));
		assertEquals(guid1,
				store.getInstance(transaction, true, roleA.getPartner()));
	}

	@Test
	public final void testStringInstance() {
		String stringHello = "hello";
		String stringWorld = "world";
		GUID role2Id = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, stringHello, role2Id, stringWorld);
		transaction = store.commit(transaction);
		assertEquals(store.getInstance(transaction, stringHello, role2Id),
				stringWorld);
	}

	@Test
	public final void testTimestampInstance() {
		Timestamp ts1 = new Timestamp(1);
		Timestamp ts2 = new Timestamp(2);
		GUID role2Id = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, ts1, role2Id, ts2);
		GUID durableTransaction = store.commit(transaction);

		assertEquals(store.getInstance(durableTransaction, ts1, role2Id), ts2);

		// check existence and transactionID of the new link
		assertEquals(
				store.getChanges(
						new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
								durableTransaction, ts1, role2Id, ts2))
						.iterator().next().getTransaction(), durableTransaction);
		assertEquals(
				store.getChanges(
						new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
								durableTransaction, ts2, role2Id.getPartner(),
								ts1)).iterator().next().getTransaction(),
				durableTransaction);
	}

	@Test
	public final void testReadWriteOneStoreConcurrently() {
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		GUID guid3 = new GUID();
		GUID roleA = new GUID();

		PDStore store1 = store;
		PDStore store2 = store;
		GUID transaction1 = store1.begin();
		GUID transaction2 = store2.begin();
		Collection<Object> result;

		// connection 1
		store1.addLink(transaction1, guid1, roleA, guid3);
		store1.removeLink(transaction1, guid1, roleA, guid2);
		result = store1.getInstances(transaction1, guid1, roleA);
		assertTrue(result.contains(guid3));
		assertTrue(!result.contains(guid2));

		// connection 2
		store2.addLink(transaction2, guid1, roleA, guid2);
		store2.removeLink(transaction2, guid1, roleA, guid3);
		result = store2.getInstances(transaction2, guid1, roleA);
		assertTrue(result.contains(guid2));
		assertTrue(!result.contains(guid3));
		store2.commit(transaction2);

		// check that transaction1 still cannot see transaction2
		result = store1.getInstances(transaction1, guid1, roleA);
		assertTrue(result.contains(guid3));
		assertTrue(!result.contains(guid2));

		GUID resultTransaction1 = store1.commit(transaction1);

		// a new transaction should see committed transaction1 as current
		// database state
		transaction2 = store2.begin();
		result = store.getInstances(resultTransaction1, guid1, roleA);
		assertTrue(result.contains(guid3));
		store2.commit(transaction2);
	}

	@Test
	public final void testSnapshotIsolation() {
		// TODO: put this test in own test class
		GUID guid1 = new GUID();
		GUID guid3 = new GUID();
		GUID guid4 = new GUID();
		GUID guid6 = new GUID();
		GUID roleA = new GUID();

		// TODO if this is put into own test class, then
		// the following should be two different connections to server
		// if test is executed remotely

		PDStore store1 = store;
		PDStore store2 = store;
		Collection<Object> result;

		// make sure guid6 is added
		GUID transaction0 = store2.begin();
		result = store.getInstances(transaction0, guid1, roleA);
		store1.addLink(transaction0, guid1, roleA, guid6);

		GUID durableTransaction = store2.commit(transaction0);
		assertTrue(durableTransaction != null);

		// now test two concurrent transactions
		GUID transaction1 = store1.begin();
		GUID transaction2 = store2.begin();

		// make sure state is unchanged at first in t1
		result = store1.getInstances(transaction1, guid1, roleA);
		assertFalse(result.contains(guid3));
		assertFalse(result.contains(guid4));
		assertTrue(result.contains(guid6));

		result = store2.getInstances(transaction2, guid1, roleA);
		assertFalse(result.contains(guid3));
		assertFalse(result.contains(guid4));
		assertTrue(result.contains(guid6));

		// t1 sets value to guid3
		store1.removeLink(transaction1, guid1, roleA, guid6);
		store1.addLink(transaction1, guid1, roleA, guid3);
		result = store1.getInstances(transaction1, guid1, roleA);
		assertFalse(result.contains(guid6));
		assertTrue(result.contains(guid3));

		// t2 sets value to guid4
		result = store2.getInstances(transaction2, guid1, roleA);
		assertTrue(result.contains(guid6));
		store2.removeLink(transaction2, guid1, roleA, guid6);
		store2.addLink(transaction2, guid1, roleA, guid4);
		result = store2.getInstances(transaction2, guid1, roleA);
		assertFalse(result.contains(guid6));
		assertTrue(result.contains(guid4));

		// t2 commits first
		@SuppressWarnings("unused")
		GUID resultTransaction2 = store2.commit(transaction2);

		// check that transaction1 still cannot see transaction2
		result = store1.getInstances(transaction1, guid1, roleA);
		assertTrue(result.contains(guid3));
		assertFalse(result.contains(guid4));

		// commit of t1 should fail because "first committer (t2) wins"
		GUID resultTransaction1 = store1.commit(transaction1);
		assertTrue(resultTransaction1 == null);

		// a new transaction should see committed transaction2
		// but not transaction 1
		GUID transaction9 = store2.begin();
		result = store.getInstances(transaction9, guid1, roleA);
		assertFalse(result.contains(guid3));
		assertTrue(result.contains(guid4));
		store2.commit(transaction9);
	}

	@Test
	public final void testIsolationLevelNone() {
		// copied from testSnapShotIsolation
		// only some asserts changed/
		// TODO: refactor as parameterized test,
		// TODO: add more isolevels,
		GUID guid1 = new GUID();
		GUID guid3 = new GUID();
		GUID guid4 = new GUID();
		GUID guid6 = new GUID();
		GUID roleA = new GUID();

		// TODO if this is put into own test class, then
		// the following should be two different connections to server
		// if test is executed remotely

		PDStore store1 = store;
		PDStore store2 = store;
		Collection<Object> result;

		// make sure guid6 is added
		GUID transaction0 = store2.begin();
		result = store.getInstances(transaction0, guid1, roleA);
		store1.addLink(transaction0, guid1, roleA, guid6);

		GUID durableTransaction = store2.commit(transaction0);
		assertTrue(durableTransaction != null);

		// now test two concurrent transactions
		GUID transaction1 = store1.begin(IsolationLevel.NONE);
		GUID transaction2 = store2.begin(IsolationLevel.SNAPSHOT);

		// make sure state is unchanged at first in t1
		result = store1.getInstances(transaction1, guid1, roleA);
		assertFalse(result.contains(guid3));
		assertFalse(result.contains(guid4));
		assertTrue(result.contains(guid6));

		result = store2.getInstances(transaction2, guid1, roleA);
		assertFalse(result.contains(guid3));
		assertFalse(result.contains(guid4));
		assertTrue(result.contains(guid6));

		// t1 sets value to guid3
		store1.removeLink(transaction1, guid1, roleA, guid6);
		store1.addLink(transaction1, guid1, roleA, guid3);
		result = store1.getInstances(transaction1, guid1, roleA);
		assertFalse(result.contains(guid6));
		assertTrue(result.contains(guid3));

		// t2 sets value to guid4
		result = store2.getInstances(transaction2, guid1, roleA);
		assertTrue(result.contains(guid6));
		store2.removeLink(transaction2, guid1, roleA, guid6);
		store2.addLink(transaction2, guid1, roleA, guid4);
		result = store2.getInstances(transaction2, guid1, roleA);
		assertFalse(result.contains(guid6));
		assertTrue(result.contains(guid4));

		// t2 commits first
		@SuppressWarnings("unused")
		GUID resultTransaction2 = store2.commit(transaction2);

		result = store1.getInstances(transaction1, guid1, roleA);
		assertTrue(result.contains(guid3));
		// check that transaction1 now can already see transaction2
		assertTrue(result.contains(guid4));

		// commit of t1 should succeed because it has Isolation Level NONE
		GUID resultTransaction1 = store1.commit(transaction1);
		assertTrue(resultTransaction1 != null);

		// a new transaction should see committed transaction2
		// and transaction 1
		GUID transaction9 = store2.begin();
		result = store.getInstances(transaction9, guid1, roleA);
		assertTrue(result.contains(guid3));
		assertTrue(result.contains(guid4));
		store2.commit(transaction9);
	}

	@Test
	public final void testReadWriteLink_Blob_Blob() {
		Blob blob111 = new Blob(new byte[] { 1, 1, 1 });
		Blob blob999 = new Blob(new byte[] { 9, 9, 9 });
		GUID roleA = new GUID();

		assertTrue(blob111.equals(new Blob(new byte[] { 1, 1, 1 })));
		assertTrue(!blob111.equals(blob999));

		GUID transaction = store.begin();
		store.addLink(transaction, blob111, roleA, blob999);
		final GUID durableTransaction = store.commit(transaction);

		Blob result = (Blob) store.getInstance(durableTransaction, blob111,
				roleA);
		assertTrue(result.equals(blob999));
		result = (Blob) store.getInstance(durableTransaction, blob999,
				roleA.getPartner());
		assertTrue(result.equals(blob111));
	}

	@Test
	public final void testSetAndGet() {
		String instanceA = "A";
		String instanceB = "B";
		String instanceC = "C";
		String instanceD = "D";
		GUID roleA = new GUID();

		// check if set works for the first time

		GUID transaction = store.begin();
		store.setLink(transaction, instanceA, roleA, instanceB);

		// check result within the transaction
		assertTrue(store.getInstance(transaction, instanceA, roleA).equals(
				instanceB));
		GUID durableTransaction = store.commit(transaction);

		// check result after transaction
		assertTrue(durableTransaction != null);
		assertTrue(store.getInstance(durableTransaction, instanceA, roleA)
				.equals(instanceB));

		// check result in new transaction
		transaction = store.begin();
		assertTrue(store.getInstance(transaction, instanceA, roleA).equals(
				instanceB));

		// check more calls to set
		store.setLink(transaction, instanceA, roleA, instanceC);
		assertTrue(store.getInstance(transaction, instanceA, roleA).equals(
				instanceC));
		store.setLink(transaction, instanceA, roleA, instanceD);
		assertTrue(store.getInstance(transaction, instanceA, roleA).equals(
				instanceD));
		durableTransaction = store.commit(transaction);

		assertTrue(durableTransaction != null);
		assertTrue(store.getInstance(durableTransaction, instanceA, roleA)
				.equals(instanceD));
	}

	@Test
	public final void testUsesRole() {
		GUID instance1 = new GUID();
		Object instance2 = new GUID();
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, instance1, roleA, instance2);
		GUID durableTransaction = store.commit(transaction);
		assertTrue(durableTransaction != null);

		// testing setting of USES_ROLE
		Collection<Object> roles = store.getInstances(durableTransaction,
				instance1, PDStore.USES_ROLE_ROLEID);
		assertTrue(roles.contains(roleA));
	}

	@Test
	public final void testGetChanges() {
		GUID myAccount = new GUID();
		GUID numberRoleId = new GUID();
		GUID roleC = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, myAccount, numberRoleId, 888L);
		transaction = store.commit(transaction);

		PDChange<GUID, Object, GUID> change = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transaction, myAccount, numberRoleId,
				888L);
		Collection<PDChange<GUID, Object, GUID>> result = store
				.getChanges(change);
		assertEquals(1, result.size());
		assertEquals(888L, result.iterator().next().getInstance2());

		// when instance1 and intance2 are variables
		transaction = store.begin();
		store.addLink(transaction, 1, roleC, 2);
		store.addLink(transaction, 3, roleC, 4);
		transaction = store.commit(transaction);

		change = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				transaction, new Variable("x"), roleC, new Variable("y"));
		result = store.getChanges(change);
		assertEquals(2, result.size());
	}

	/* TODO fix this test
	@Test
	public final void testIsInLog() {
		// TODO is the test wrong? it looks as if it only disables storage of a
		// change in the log, not the index, but then tests if a change can be
		// queried from the index or not

		GUID myAccount = new GUID();
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, myAccount, roleA, 78);
		transaction = store.commit(transaction);

		GUID transaction1 = store.begin();
		store.addLink(transaction1, roleA.getFirst(),
				PDStore.IS_NOT_IN_LOG_ROLEID, 1);
		store.commit(transaction1);

		GUID transaction11 = store.begin();
		store.addLink(transaction11, myAccount, roleA, 79);
		transaction11 = store.commit(transaction11);

		Collection<Object> result = store.getInstances(transaction11,
				myAccount, roleA);

		// This must support both number types currently
		// Since cached stores return small ints.
		assertTrue(result.contains(78L) || result.contains(78));
		assertFalse(result.contains(79L) || result.contains(79));
	}
	*/

	@Test
	public final void testIsInIndex() {
		GUID myAccount = new GUID();
		GUID sampleRole = new GUID();

		GUID transaction = store.begin();
		store.addLink(transaction, myAccount, sampleRole, 88);
		transaction = store.commit(transaction);

		GUID transaction1 = store.begin();
		store.addLink(transaction1, sampleRole.getFirst(),
				PDStore.IS_NOT_IN_INDEX_ROLEID, 1);
		store.commit(transaction1);

		GUID transaction11 = store.begin();

		store.addLink(transaction11, myAccount, sampleRole, 89);
		transaction11 = store.commit(transaction11);

		Collection<Object> result = store.getInstances(transaction11,
				myAccount, sampleRole);

		// This must support both number types currently
		// Since cached stores return small ints.
		assertFalse(result.contains(89L) || result.contains(89));
		assertTrue(result.contains(88L) || result.contains(88));
	}

	@Test
	public final void testGetAllInstancesInRole() {
		GUID roleA = new GUID();

		store.addLink("1", roleA, "A");
		store.addLink("2", roleA, "B");
		store.addLink("3", roleA, "C");
		Collection<Object> result = store.getAllInstancesInRole(roleA);
		assertTrue(result.contains("A"));
		assertTrue(result.contains("B"));
		assertTrue(result.contains("C"));
		store.commit();

		// retest for committed transaction
		result = store.getAllInstancesInRole(roleA);
		assertTrue(result.contains("A"));
		assertTrue(result.contains("B"));
		assertTrue(result.contains("C"));
	}

    @Test
    public final void testGetRoles() {
        GUID roleA = new GUID();
        GUID roleB = new GUID();

        store.addLink("1", roleA, "2");
        store.addLink("1", roleB, "3");

        assertTrue(store.getRoles("1").contains(roleA));
        assertTrue(store.getRoles("1").contains(roleB));
        store.commit();

        // retest for committed transaction
        assertTrue(store.getRoles("1").contains(roleA));
        assertTrue(store.getRoles("1").contains(roleB));
    }

    @Test
    public final void testGetRoleFromInstance() {
        GUID roleA = new GUID();
        store.setName(roleA, "myrole");
        GUID roleB = new GUID();
        store.setName(roleB, "myrole");

        store.addLink("A", roleA, "1");
        store.addLink("B", roleB, "2");

        assertEquals(roleA, store.getRoleFromInstance("A", "myrole"));
        assertEquals(roleB, store.getRoleFromInstance("B", "myrole"));
        store.commit();

        // retest for committed transaction
        assertEquals(roleA, store.getRoleFromInstance("A", "myrole"));
        assertEquals(roleB, store.getRoleFromInstance("B", "myrole"));
    }
}
