package pdstore.deduction;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.junit.BeforeClass;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.sparql.Query;
import pointsto.PointsToRules;

public class PointsToTestHashed {

	private static final String PRE_FILLED_ALLOC = "TestStorePointsToTestHashedPreFilledAlloc";
	private static final String PRE_FILLED_LOAD_STORE = "TestStorePointsToTestHashedPreFilleLoadStore";
	private static final String ROLE_FILE_PREFIX = "role-";
	private static final String BENCHMARK_PATH = "C:/hg/pdstore-benchmarks/pddata/";

    // Valid folders with test files, checked into 
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel0/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel1/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/testrelShort/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/testrelFirstReal/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSmall/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/SmallComplete/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSomeReal/";
	private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsRewrite/";


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
				progressReport(j);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void progressReport(int j) {
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
				progressReport(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**  Needed to track the setName heisenbug.
	 * @param store
	 * @param name1
	 * @return
	 */
	public static Object getInstanceForString(PDStore store, String name1) {
		return (Long)myhash(name1);
		//return (long)name1.hashCode();

	}
	
	public static long myhash(String name1) {
		String name2 = name1.substring(name1.length()/2);
		int hash2 = name2.hashCode();
		int hash1 = name1.hashCode();

		return (long)(hash1+(hash2<<30));

		// return (long)(hash1+Integer.MAX_VALUE*hash2);

	}

	static Random rand = new Random();
	public static Object getNewInstance() {
		return rand.nextLong();

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
				Debug.warningAssertTrue(result1.contains(right) ==assertDirection, "left:"+nextLine[0]+" right:"+nextLine[1]+" not "+assertDirection);
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
		Query.isUsingVariableRoles=false;
		PDStore.isNotConcurrentNotPersistent=true;

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
		
		File f = new File(PDStore.DEFAULT_FILEPATH+PRE_FILLED_LOAD_STORE+".log");
		boolean f_exists = (f.exists() && !f.isDirectory());
		File f1 = new File(PDStore.DEFAULT_FILEPATH+PRE_FILLED_ALLOC+".log");
		boolean f1_exists = (f1.exists() && !f1.isDirectory());
		if(!f_exists && !f1_exists) {
			String fileName = "TestStore"+PointsToTestHashed.class.getSimpleName() + System.nanoTime();
			store = new PDStore(fileName);			

			System.err.println("read record-store");
			readRecords(RELATIONS_PATH+"record-store", store, PointsToRules.STORE_LEFT, PointsToRules.STORE_RIGHT, PointsToRules.STORE_FIELD);
			System.err.println();
			System.err.println("read record-load");
			readRecords(RELATIONS_PATH+"record-load", store, PointsToRules.LOAD_LEFT, PointsToRules.LOAD_RIGHT, PointsToRules.LOAD_FIELD);
			store.waitForDetachedListeners();
		} 
		if(!f1_exists && f_exists) {
			System.err.println("Using "+PRE_FILLED_LOAD_STORE);
			store = new PDStore(PRE_FILLED_LOAD_STORE);			
		}
		if(!f1_exists) {
			
			PointsToRules.setRuleRoleNames(store);
			PointsToRules.addRuleAlloc(store);

			// ALLOC
			System.err.println();
			System.err.println("read alloc");
			
			// Load command that loads the original alloc role, and triggers listeners
			readTriplesFixedRole("alloc", RELATIONS_PATH+ROLE_FILE_PREFIX, store);
			
			// Load command that only loads the vP triples, thus saving the inactive "alloc" triples			
			//readTriplesFixedRole("vP", RELATIONS_PATH+ROLE_FILE_PREFIX, store);
			
			// The following is necessary to give asynchronous listener time to finish
			store.waitForDetachedListeners();
			
			System.err.println();
			System.err.println("System.gc()");
			System.gc();

		} else {
			System.err.println("Using "+PRE_FILLED_ALLOC);
			store = new PDStore(PRE_FILLED_ALLOC);			
		} 
		//TODO: add assert
		assertFixedRole(RELATIONS_PATH+ROLE_FILE_PREFIX+"alloc", store, "vP", true);
		
		PointsToRules.addRuleAssign(store);
		PointsToRules.addRuleLoadStore(store);

		// ASSIGN
		System.err.println();
		System.err.println("read assign");
		readTriplesFixedRole("assign", RELATIONS_PATH+ROLE_FILE_PREFIX, store);
		
		
		
		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();
	
		// TODO: add asserts.
		//assertFixedRole(RELATIONS_PATH+"test-loadstore-vP", store, "vP", true);
		//assertFixedRole(RELATIONS_PATH+"test-loadstore-N-vP", store, "vP", false);		
		
		store.commit();
		
		
	}

}
