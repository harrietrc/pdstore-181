package pointsto;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.Stopwatch;

import org.junit.Before;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import pdstore.GUID;
import pdstore.PDStore;
import pdstore.deduction.CSVImport;
import pdstore.sparql.Query;

public class FindCircles {

	private static final Long TEMP_MARK = -10L;

	private static final String ROLE_FILE_PREFIX = "role-";

	// Valid folders with test files, checked into
	// private static final String RELATIONS_PATH =
	// PDStore.DEFAULT_FILEPATH+"testrelations/testrel0/";
	private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH			+ "testrelations/relations/";
	// static String RELATIONS_PATH =
	// PDStore.DEFAULT_FILEPATH+"testrelations/SmallComplete/";
	// private static final String RELATIONS_PATH =
	// PDStore.DEFAULT_FILEPATH+"testrelations/testrelFirstReal/";
	// private static final String RELATIONS_PATH =
	// PDStore.DEFAULT_FILEPATH+"testrelations/relationsSmall/";
	// private static final String RELATIONS_PATH =
	// PDStore.DEFAULT_FILEPATH+"testrelations/testrel1/";

	public final static GUID ALIAS = new GUID(
	        "63dad9b3e3f011e3992228d2446fc01a");
	
	public final static GUID ALIAS2 = new GUID(
	        "5a1bf2d1e4c811e3b9c328d2446fc01a");
	
	public final static GUID STRATUM = new GUID(
	        "44f044e0e3f011e3b1c428d2446fc01a");

	public final static GUID CIRCLE = new GUID(
	        "f15cfc0fe4a011e3bd0928d2446fc01a");
	
	public static GUID currentCircleRole;
	
	
	static PDStore store;


	private static int replacecount = 0;

	private static int tempCircleCount = 1000;
	private static long totalOrderCount = 0;

	private static long maxrenamecount;

	private static Stopwatch stopw;

	private int attempts;

	/**
	 * trying to replace circles. A good number to find would be:
	 * maxRenameCount: 3059 totalAttempts: 12140258 TODO: add asserts.
	 */
	@Test
	public final void testCircles() {

		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "TestStore" + FindCircles.class.getSimpleName()
				+ System.nanoTime();
		PDStore.isNotConcurrentNotPersistent = true;
		Query.isUsingVariableRoles = false;
		store = new PDStore(fileName);
		PointsToRules.setRuleRoleNames(store);
		CSVImport.readTriplesFixedRole("assign", RELATIONS_PATH
                + ROLE_FILE_PREFIX, store, true);

		Set<GUID> allGuids = CSVImport.debugMap.keySet();
		GUID[] a = new GUID[1];
		a = allGuids.toArray(a);
		Random rn = new Random(1234567890997L);
		List<Object> parents = new ArrayList<Object>();
		maxrenamecount = 0;
		int totalAttempts = 0;
		stopw = new Stopwatch();
		stopw.start();
		for (attempts = 0; attempts < 10; ++attempts) {
			// change direction to get more huts
			GUID direction = PointsToRules.ASSIGN;
			if (attempts % 2 == 0)
				direction = PointsToRules.ASSIGN.getPartner();
			if (stopw.seconds() > 3600) {
				System.err.println("\ntimeout! ");
			}
			for (GUID instance : a) {
				totalAttempts++;
				progressReport(totalAttempts);
				parents.clear();
				currentCircleRole = new GUID();
				Object instance1 = instance;
				while (parents.size() < 1000) {
					int pos = findAndProcessCircles(parents, instance1, ALIAS2);
					if(pos!=-1) {
						break;
//						if(parents.size()==0L) break;
//						instance1 = parents.get(parents.size()-1);
//						parents.remove(parents.size()-1);
//						continue;
					}
					
					Collection<Object> next = store.getInstances(instance1,
							direction);
					Object nextInstance = null;
					Object[] options = next.toArray();
					long maxStratum = -1;
					for(Object nextInstance1:options){
						Long mark1 = (Long)store.getInstance(
								nextInstance1, STRATUM); 
						// TODO: cycle-detect, needs parents.add(instance1);
						if(mark1!=null) {
							if(mark1>maxStratum) {
							   maxStratum = mark1;
							   next.remove(nextInstance1);
							}
						} else {
							nextInstance = nextInstance1;		
						}
					}
					if (nextInstance == null) {
						// has only sorted parents
						store.setLink(instance1, STRATUM, maxStratum+1);						
						store.setLink(instance1, ALIAS, totalOrderCount++);	
						// break;
						if(parents.size()==0L) break;
						instance1 = parents.get(parents.size()-1);
						parents.remove(parents.size()-1);
						continue;
					} else {
					  parents.add(instance1);
				      //instance1 = nextInstance;		
					  Object[] options1 = next.toArray();
					  instance1 = options1[rn.nextInt(options1.length)];

					}
					
				}
              store.indexOnlyStore.deleteRole(currentCircleRole);
			} // end for AllObjects
		} // end for attempts
		System.err.println("\nmaxRenameCount: " + maxrenamecount);
		System.err.println("totalAttempts: " + totalAttempts);

		String relationsOutPath = PDStore.DEFAULT_FILEPATH
				+ "testrelations/relationsCycleFree/";

		renameInFile(RELATIONS_PATH + "record-store", relationsOutPath
				+ "record-store", false);
		System.err.println("\nreplaces: " + replacecount);
		replacecount = 0;
		renameInFile(RELATIONS_PATH + "record-load", relationsOutPath
				+ "record-load", false);
		System.err.println("\nreplaces: " + replacecount);
		replacecount = 0;
		renameInFile(RELATIONS_PATH + ROLE_FILE_PREFIX + "alloc",
				relationsOutPath + ROLE_FILE_PREFIX + "alloc", false);
		System.err.println("\nreplaces: " + replacecount);
		replacecount = 0;
		renameInFile(RELATIONS_PATH + ROLE_FILE_PREFIX + "assign",
				relationsOutPath + ROLE_FILE_PREFIX + "assign", true);
		System.err.println("\nreplaces: " + replacecount);
		replacecount = 0;
	}

	public int findAndProcessCircles(List<Object> parents, Object instance1, GUID aliasRole) {
		int pos = -1;
			int parentsSize = parents.size();
		Object tempAlias = store.getInstance(
				instance1, currentCircleRole);
		//if (tempAlias != null && tempAlias instanceof Long) {
		if (parents.contains(instance1)) {
			long temp = (Long)tempAlias;
			pos = (int)temp;
			int i = parentsSize - pos;
			tempCircleCount++;
			if (i > 9) {
				System.err.println("circle!! l=" + i + " ratio: "
						+ ((double) i) / parentsSize);
			}

			// test for existing alias Links
			Long newAlias = (long) -tempCircleCount;
			Long alias = null;
			long renameCount = 0;

			// find existing aliases
			for (int j = pos; j < parentsSize; ++j) {
				Object instance2 = store.getInstance(
						parents.get(j), ALIAS);
			    if (!TEMP_MARK.equals(instance2)) {
					newAlias = (Long) instance2;
					if(newAlias<TEMP_MARK){
						renameCount = store.getInstances(newAlias,
								ALIAS.getPartner()).size();
						break;
					}
				}
			}

			// set or replace aliases, set circle
			for (int j = pos; j < parentsSize; ++j) {
				store.setLink(parents.get(j), currentCircleRole, -pos);
				Object instance2 = store.getInstance(
						parents.get(j), ALIAS);
				if (!TEMP_MARK.equals(instance2)) {
					alias = (Long) instance2;
					if (!newAlias.equals(alias))
						// replace existing alias with new alias
						renameCount = replaceAlias(alias, newAlias,
								renameCount, ALIAS, pos);
					if (renameCount > maxrenamecount) {
						maxrenamecount = renameCount;
						// reset attempt loop
						attempts = 0;
					}
				} else {
					store.setLink(parents.get(j), ALIAS, newAlias);
					++renameCount;
				}
			}
			if (renameCount > 13) {
				System.err.println("\nrenameCount: " + renameCount);
			}

		} else {
	       store.setLink(instance1, currentCircleRole, parentsSize+1);
		   // temporarily mark elements with -10
			Object instance2 = store.getInstance(
					instance1, ALIAS);
			if (instance2 == null){
			       store.setLink(instance1, ALIAS, TEMP_MARK);
			}
		}
		return pos;
	}

	private long replaceAlias(Long alias2, Long newAlias, long count, GUID aliasRole, int pos) {
		Collection<Object> circleElements = store.getInstances(alias2,
				aliasRole.getPartner());
		for (Object instance : circleElements) {
			store.setLink(instance, aliasRole, newAlias);
			store.setLink(instance, currentCircleRole, pos);
			count++;
		}
		return count;
	}

	public static void progressReport(int j) {
		if (j % 100 == 0) {
			if (j % 10000 == 0) {
				System.err.println();
				System.err.print(j / 1000);
				System.err.print(",000 ");
			}
			System.err.print(",");
		}
	}

	/**
	 * Writes out a new file with the renamed variables.
	 * 
	 * @param filename
	 *            The file to process
	 * @param outFileName
	 *            TODO
	 * @param isAssign
	 *            assign records is treated differently
	 */
	public static void renameInFile(String filename, String outFileName,
			boolean isAssign) {
		System.err.println("\nrename " + filename);
		CSVReader reader;
		CSVWriter writer;

		int identicals = 0;

		try {
			reader = new CSVReader(new FileReader(filename + ".csv"));
			writer = new CSVWriter(new FileWriter(outFileName + ".csv"), ',');
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String[] nextLine;
		try {
			int i = 0;
			while ((nextLine = reader.readNext()) != null) {
				String[] writeLine = new String[3];
				if (nextLine.length > 2)
					writeLine[2] = nextLine[2];
				writeLine[0] = replace(nextLine[0], null);
				writeLine[1] = replace(nextLine[1], null);
				if (isAssign && writeLine[0].equals(writeLine[1])) {
					// assign to itself is not written to file assign
					identicals++;
				} else
					writer.writeNext(writeLine);
				++i;
				progressReport(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("\n" + filename + " identicals: " + identicals);
		System.err.println();
	}

	private static String replace(String string, List<String> parents) {
		Object instance = store.getInstance(store.getGUIDwithName(string),
				ALIAS);
		if (instance != null && instance instanceof Long) {
			Long alias = (Long) instance;
			++replacecount;
			return "alias" + alias.toString();
		} else
			return string;

	}

}
