package pdstore.deduction;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.junit.Before;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.sparql.Query;
import pointsto.PointsToRules;

public class PointsToFromCSVTest {

	private static final String ROLE_FILE_PREFIX = "role-";

    // Valid folders with test files, checked into 
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel0/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relations/";
	static String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/SmallComplete/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrelFirstReal/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSmall/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel1/";


	static PDStore store;


    /**
	 * Reads asserts from a file that should look the same as a role File used by readTriplesFixedRole().
	 * @param filename
	 * @param store
	 * @param testRoleName  The role that should be tested, left variable to make the method more flexible.
	 * @param assertDirection if true, then the test is for assertTrue
	 */
	public static void assertFixedRole(String filename, PDStore store, String testRoleName, boolean assertDirection) {
		GUID testRole = store.getId(testRoleName);
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(filename+".csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		String [] nextLine;
		store.commit();
		try {
			int i=0;
			while ((nextLine = reader.readNext()) != null) {
				GUID left = store.getId(nextLine[0]);
				if(left==null) { 
					Debug.println( "Not Defined: "+nextLine[0]);
				}
				GUID right = store.getId(nextLine[1]);	
				if(right==null) { 
					Debug.println( "Not Defined: "+nextLine[1]);
				}
				Collection<Object> result1 = store.getInstances(left, testRole);
				// enact assert direction by using XOR, i.e. ==
				Debug.assertTrue(result1.contains(right) ==assertDirection, "left:"+left+" right:"+right+" not "+assertDirection);
				store.commit();
				++i;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Before
	public void setUp() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "TestStore"+PointsToFromCSVTest.class.getSimpleName() + System.nanoTime();
		PDStore.isNotConcurrentNotPersistent = true;
		Query.isUsingVariableRoles = false;
		store = new PDStore(fileName);
	}

// TODO do we really need this? It interferes with inherited test classes,as they cannot easily be executed without running all these tests (but only testFilesLoadStore should be used).
//	/**
//	 * Testing a the assign rule.
//	 */
//	@Test
//	public final void testFileAlloc() {
//
//		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
//		PointsToTest.addRules(store);
//
//		readTriplesFixedRole("alloc", RELATIONS_PATH+ROLE_FILE_PREFIX, store, false);
//		// The following is necessary to give asynchronous listener time to finish
//		store.waitForDetachedListeners();
//
//		assertFixedRole(RELATIONS_PATH+ROLE_FILE_PREFIX+"alloc", store, "vP", true);
//		store.commit();
//	}
//
//	/**
//	 * Testing a the assign rule.
//	 * TODO: add asserts.
//	 */
//	@Test
//	public final void testFileAssign() {
//
//		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
//		//Debug.addDebugTopic(RuleListener.class.getName());
//		PointsToTest.addRules(store);
//
//		readTriplesFixedRole("alloc", RELATIONS_PATH+ROLE_FILE_PREFIX, store, false);
//		readTriplesFixedRole("assign", RELATIONS_PATH+ROLE_FILE_PREFIX, store, true);
//		// The following is necessary to give asynchronous listener time to finish
//		store.waitForDetachedListeners();
//
//		// TODO: add asserts.
//		assertFixedRole(RELATIONS_PATH+"test-assign-vP", store, "vP", true);
//		assertFixedRole(RELATIONS_PATH+"test-assign-N-vP", store, "vP", false);
//		store.commit();
//	}

	/**
	 * Testing a the assign rule.
	 * TODO: add asserts.
	 */
	@Test
	public final void testFilesLoadStore() {
	
		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());

		PointsToRules.setRuleRoleNames(store);
		PointsToRules.addRuleAlloc(store);
        CSVImport.readTriplesFixedRole("alloc", RELATIONS_PATH + ROLE_FILE_PREFIX, store, false);

		PointsToRules.addRuleAssign(store);
        CSVImport.readTriplesFixedRole("assign", RELATIONS_PATH + ROLE_FILE_PREFIX, store, true);

        //InsertionPseudoFilter.printInsert = true;
        CSVImport.readRecords(RELATIONS_PATH + "record-store", store, PointsToRules.STORE_RIGHT, PointsToRules.STORE_LEFT, PointsToRules.STORE_FIELD);
        PointsToRules.addNewLoadStore(store);
        //PointsToTest.addRuleLoadStore(store);
		CSVImport.readRecords(RELATIONS_PATH + "record-load", store, PointsToRules.LOAD_RIGHT, PointsToRules.LOAD_LEFT, PointsToRules.LOAD_FIELD);
		
		PointsToRules.printRuleCount("storeLoad");

		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();
	
		// TODO: add asserts.
		assertFixedRole(RELATIONS_PATH+"test-loadstore-vP", store, "vP", true);
		assertFixedRole(RELATIONS_PATH+"test-loadstore-N-vP", store, "vP", false);		
		assertFixedRole(RELATIONS_PATH+"test-assign-N-vP", store, "vP", false);
		store.commit();
	}
}
