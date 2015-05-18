package pdstore.deduction;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.sparql.Query;
import pointsto.PointsToRules;

public class CompletePointsToTest {

	private static final String ROLE_FILE_PREFIX = "role-";
	private static final String BENCHMARK_PATH = "C:/hg/pdstore-benchmarks/pddata/";

	// Valid folders with test files, checked into 
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel0/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel1/";
	private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/SmallComplete/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/SmallComplete/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/testrelShort/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/testrelFirstReal/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSmall/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSomeReal/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrelShort/";


	static PDStore store;


	/**  
	 * Loads records from a .csv file.
	 * The numver of roles must match the columns.
	 * Every row of the file becomes one reified record.
	 * A GUID for the record is created and becomes instance1
	 * all fields in the file are connected over the respective role2.
	 * the fields are the instance2.
	 * 
	 * @param fileName    used to load fileName+".csv"
	 * @param store  
	 * @param roles      First role is used for first column, and so on.
	 */
	public static void readRecords(String fileName, PDStore store, GUID ... roles ) {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(fileName+".csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		String [] nextLine;
		store.commit();
		try {
			int j=0;
			while ((nextLine = reader.readNext()) != null) {
				Object left = getNewInstance();

				for(int i = 0; i<roles.length; ++i) {
					Object right =  getInstanceForString(store,  nextLine[1]); 
					store.addLink(left, roles[i], right);
				}
				store.commit();
				store.waitForDetachedListeners();
				++j;
				consoleProgressReport(j);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void consoleProgressReport(int j) {
		if(j%100==0) {
			if(j%10000==0) {
			System.err.println();
			System.err.print(j/1000);
			System.err.print(",000");
		}
		System.err.print(",");
		}
	}

	/**
	 *  Reads triples for a single role from a .csv file
	 * @param role2name The name of role2 for all triples, becomes part of the filename
	 * @param prefix the filename is prefix+role2name+".csv"
	 * @param store
	 */
	public static void readTriplesFixedRole(String role2name, String prefix, PDStore store) {
		GUID role2 = store.getId(role2name);
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(prefix+role2name+".csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		String [] nextLine;
		store.commit();
		try {
			int i=0;
			while ((nextLine = reader.readNext()) != null) {
				Object left = getInstanceForString(store,  nextLine[0]);
				Object right =  getInstanceForString(store,  nextLine[1]); 
				store.addLink(left, role2, right);
				store.commit();
				// Important for preventing setName Heisenbugs and other similar effects
				store.waitForDetachedListeners();
				++i;
				consoleProgressReport(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PointsToRules.printRuleCount(role2name);
		PointsToRules.printRuleCount("storeLoad");

	}

	/**  Needed to track the setName heisenbug.
	 * @param store
	 * @param name1
	 * @return
	 */
	public static Object getInstanceForString(PDStore store, String name1) {
		return store.getGUIDwithName(name1);

	}
	
	public static Object getNewInstance() {
		return new GUID();

	}


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
				Object left = getInstanceForString(store,  nextLine[0]);
				Object right =  getInstanceForString(store,  nextLine[1]); 
				Collection<Object> result1 = store.getInstances(left, testRole);
				// enact asserDirection by using XOR, i.e. ==
				Debug.warningAssertTrue(result1.contains(right) ==assertDirection, "left:"+left+" right:"+right+" not "+assertDirection);
				store.commit();
				++i;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "TestStore"+CompletePointsToTest.class.getSimpleName() + System.nanoTime();
		PDStore.isNotConcurrentNotPersistent=true;
		Query.isUsingVariableRoles=false;
		PDStore.isStoringIsolationlevel = false;
		store = new PDStore(fileName);

		//Debug.addDebugTopic(RuleListener.class.getName()+"basic");

	}



	/**
	 * Testing a the assign rule.
	 * TODO: add asserts.
	 */
	@Test
	public final void testFilesLoadStore() {
	
		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());
		

		//Debug.println("read record-store", this.getClass().getName());
		System.err.println("read record-store");		
		readRecords(RELATIONS_PATH+"record-store", store, PointsToRules.STORE_LEFT, PointsToRules.STORE_RIGHT, PointsToRules.STORE_FIELD);
		
		//Debug.println("\nread record-load", this.getClass().getName());
		System.err.println("\nread record-load");
		readRecords(RELATIONS_PATH+"record-load", store, PointsToRules.LOAD_LEFT, PointsToRules.LOAD_RIGHT, PointsToRules.LOAD_FIELD);
		
		PointsToRules.addRules(store);

		// ALLOC
		//Debug.println("\nread alloc", this.getClass().getName());
		System.err.println("\nread alloc");
		readTriplesFixedRole("alloc", RELATIONS_PATH+ROLE_FILE_PREFIX, store);
		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();
		assertFixedRole(RELATIONS_PATH+ROLE_FILE_PREFIX+"alloc", store, "vP", true);
		
		// ASSIGN
		//Debug.println("\nread assign", this.getClass().getName());
		System.err.println("\nread assign");
		
		readTriplesFixedRole("assign", RELATIONS_PATH+ROLE_FILE_PREFIX, store);
		
		
		
		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();
		PointsToRules.printRuleCount("storeLoad");

		// TODO: add asserts.
		//assertFixedRole(RELATIONS_PATH+"test-loadstore-vP", store, "vP", true);
		//assertFixedRole(RELATIONS_PATH+"test-loadstore-N-vP", store, "vP", false);		
		
		
		store.commit();
	}

}
