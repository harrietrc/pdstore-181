package pointsto;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.apache.poi.hssf.record.formula.functions.Replace;
import org.junit.BeforeClass;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.deduction.ListenerRule;
import pdstore.deduction.RuleListener;

public class CountAliasesLocal {

	private static final String ROLE_FILE_PREFIX = "role-";
	private static final String BENCHMARK_PATH = "C:/hg/pdstore-benchmarks/pddata/";

    // Valid folders with test files, checked into 
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel0/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrel1/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/SmallComplete/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/SmallComplete/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/testrelShort/";
	//private static final String RELATIONS_PATH = BENCHMARK_PATH+"testrelations/testrelFirstReal/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSmall/";
	private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/relations/";
	//private static final String RELATIONS_PATH = PDStore.DEFAULT_FILEPATH+"testrelations/testrelShort/";
	private static long circlecount;


	


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
	 * Processes file and by counting occurrences of left hand side.
	 * @param filename         The file to process
	 * @param variables     The hashmap, key are the variables, value is a pair containing:
	 *                      the count of occurrences on the left hand side in the assign table,
	 *                      and the right-hand side of the first assign.
	 * @param increment  increment 1 is used for the assign table, increment 2 for all other tables
	 *                   this has the effect that one occurrence on the left-hand side of a table
	 *                   other than assign rules this variable out.
	 * @param isAssign  If its the assign table.
	 * @param position TODO
	 */
	public static void processFile(String filename, HashMap<String, HashRecord> variables, int increment, boolean isAssign, int position) {
		System.err.println("\nread "+filename);
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(filename+".csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		String [] nextLine;
		try {
			int i=0;
			while ((nextLine = reader.readNext()) != null) {
				String variable = nextLine[position];
				HashRecord record = variables.get(variable);
				if(record == null){
					record = new HashRecord(0L, null);
				    variables.put(variable, record);		
				}
				record.value += increment;
				if(isAssign&&record.value==1){
					record.lastAssigned = nextLine[0];
				}
 				++i;
 				consoleProgressReport(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Writes out a new file with the renamed variables.
	 * @param filename         The file to process
	 * @param outFileName TODO
	 * @param variables     The hashmap, key are the variables, value is a pair containing:
	 *                      the count of occurrences on the left hand side in the assign table,
	 *                      and the right-hand side of the first assign.
	 * @param isAssign assign records is treated differently
	 */
	public static void renameInFile(String filename, String outFileName, HashMap<String, HashRecord> variables, boolean isAssign) {
		System.err.println("\nrename "+filename);
		CSVReader reader;
		CSVWriter writer;

		circlecount = 0;
		int identicals=0;

		
		try {
			reader = new CSVReader(new FileReader(filename+".csv"));
			writer =	new CSVWriter(new FileWriter(outFileName+".csv"), ',');
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
				writeLine[0] = replace(nextLine[0], variables, null);
				writeLine[1] = replace(nextLine[1], variables, null);
				if(isAssign && writeLine[0].equals(writeLine[1])){
					// assign to itself is not written to file assign
					identicals++;
				} else 
					writer.writeNext(writeLine);
				++i;
				consoleProgressReport(i);
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
		
		System.err.println("\n"+filename+" circlecount: "+circlecount);
		System.err.println("\n"+filename+" identicals: "+identicals);
		System.err.println();

	      
	}

	/**
	 * Writes out a new file with the renamed variables.
	 * @param filename         The file to process
	 * @param outFileName TODO
	 * @param variables     The hashmap, key are the variables, value is a pair containing:
	 *                      the count of occurrences on the left hand side in the assign table,
	 *                      and the right-hand side of the first assign.
	 * @param increment TODO
	 * @param isAssign TODO
	 */
	public static void splitFile(String filename, String outFileName, HashMap<String, HashRecord> variables) {
		System.err.println("\nsplit "+filename);
		CSVReader reader;
		CSVWriter writer;
		CSVWriter writerSplit;

		
		try {
			reader = new CSVReader(new FileReader(filename+".csv"));
			writer =		new CSVWriter(new FileWriter(outFileName+".csv"), ',');
			writerSplit =		new CSVWriter(new FileWriter(outFileName+"Split.csv"), ',');
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String[] nextLine;
		try {
			int i = 0;
			while ((nextLine = reader.readNext()) != null) {
				HashRecord record = variables.get(nextLine[1]);
				if (record == null || record.value != 1 )
				  writer.writeNext(nextLine);
				else 
			    	writerSplit.writeNext(nextLine);
				++i;
				consoleProgressReport(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
			writerSplit.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}

	private static String replace(String string, HashMap<String, HashRecord> variables, List<String> parents) {
		HashRecord record = variables.get(string);
		if (record == null || record.value != 1L )
			return string;
		if(parents==null) parents = new ArrayList<String>();
		if(parents.contains(string)){
			   System.err.println("circle!! l="+parents.size());
			   circlecount++;
			   record.value=2L;
			   return string;
			}
		if(parents.size()>1000){
			   System.err.println("overflow!!");
			   record.value=2L;
			   return string;
			}
		parents.add(string);
		// else exchange
	    //return record.lastAssigned;
	    return replace(record.lastAssigned, variables, parents);
		
	}

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs

		//Debug.addDebugTopic(RuleListener.class.getName()+"basic");

	}



	/**
	 * Testing a the assign rule.
	 * TODO: add asserts.
	 */
	@Test
	public final void testSmallComplete() {
		
		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());
		
		HashMap<String,HashRecord> variables = new HashMap<String, HashRecord>();
	
		String relationsPath = PDStore.DEFAULT_FILEPATH+"testrelations/SmallComplete/";
		long countPureAliases = findPureAliases(variables, relationsPath);
		
		
		System.err.println("\ncount: "+countPureAliases);
		System.err.println();


		
	}
	
	@Test
	public final void testOriginal() {
		
		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());
		
		HashMap<String,HashRecord> variables = new HashMap<String, HashRecord>();
	
		String relationsPath = PDStore.DEFAULT_FILEPATH+"testrelations/relations/";
		long countPureAliases = findPureAliases(variables, relationsPath);
		
		
		System.err.println("\ncount: "+countPureAliases);
		System.err.println();

		
	}
	
	@Test
	public final void SplitOriginal() {
		
		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());
		
		HashMap<String,HashRecord> variables = new HashMap<String, HashRecord>();
	
		String relationsPath = PDStore.DEFAULT_FILEPATH+"testrelations/relations/";
		String relationsOutPath = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSplit/";
		
	
		long countPureAliases = findPureAliases(variables, relationsPath);
		
		System.err.println("\ncount: "+countPureAliases);
		System.err.println();
		splitFile(relationsPath+ROLE_FILE_PREFIX+"assign", relationsOutPath+ROLE_FILE_PREFIX+"assign", variables);

	}
	
	@Test
	public final void reWriteSplitted() {
		
		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());
		
		HashMap<String,HashRecord> variables = new HashMap<String, HashRecord>();
		
		String relationsPath = PDStore.DEFAULT_FILEPATH+"testrelations/relations/";
		
	
		long countPureAliases = findPureAliases(variables, relationsPath);
		
		//updateAliases(variables, 1, true, 1);

		
		System.err.println("\ncount: "+countPureAliases);
		System.err.println();

	
		String relationsSplitPath = PDStore.DEFAULT_FILEPATH+"testrelations/relationsSplit/";
		String relationsOutPath = PDStore.DEFAULT_FILEPATH+"testrelations/relationsRewrite/";
		
		renameInFile(relationsPath+"record-store", relationsOutPath+"record-store", variables, false);
		renameInFile(relationsPath+"record-load", relationsOutPath+"record-load", variables, false);
		renameInFile(relationsPath+ROLE_FILE_PREFIX+"alloc", relationsOutPath+ROLE_FILE_PREFIX+"alloc", variables, false);
		renameInFile(relationsSplitPath+ROLE_FILE_PREFIX+"assign", relationsOutPath+ROLE_FILE_PREFIX+"assign", variables, true);
		renameInFile(relationsSplitPath+ROLE_FILE_PREFIX+"assignSplit", relationsOutPath+ROLE_FILE_PREFIX+"assignSplit", variables, false);
		
		System.err.println();

	}


	
	/**
	 * Writes out a new file with the renamed variables.
	 * @param filename         The file to process
	 * @param outFileName TODO
	 * @param variables     The hashmap, key are the variables, value is a pair containing:
	 *                      the count of occurrences on the left hand side in the assign table,
	 *                      and the right-hand side of the first assign.
	 * @param isAssign TODO
	 */
	public static void renameRenumber(String filename, String outFileName, HashMap<String, HashRecord> variables, boolean isAssign) {
		System.err.println("\nrename "+filename);
		CSVReader reader;
		CSVWriter writer;
		
		try {
			reader = new CSVReader(new FileReader(filename+".csv"));
			writer =	new CSVWriter(new FileWriter(outFileName+".csv"), ',');
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String[] nextLine;
		try {
			int i = 0;
			while ((nextLine = reader.readNext()) != null) {
				String[] writeLine = new String[3];
				if (nextLine.length > 2){
					long nr;
					try {
						nr = Long.parseLong(nextLine[2]);
						if(nr>1000000000)
							throw new NumberFormatException();
						writeLine[2] = nextLine[2];
					} catch (NumberFormatException e) {
			     		writeLine[2] = replaceRenumber(nextLine[2], variables);
				 	}
					
				}
				writeLine[0] = replaceRenumber(nextLine[0], variables);
				writeLine[1] = replaceRenumber(nextLine[1], variables);
				writer.writeNext(writeLine);
				++i;
				consoleProgressReport(i);
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
	      
	}

	private static String replaceRenumber(String string, HashMap<String, HashRecord> variables) {
		HashRecord record = variables.get(string);
		if (record == null) {
			record = new HashRecord(2L, null);
		    variables.put(string, record);		
		}
	    return ((Long)record.runningNumber).toString();		
	}

	public long findPureAliases(HashMap<String, HashRecord> variables,
			String relationsPath) {
		processFile(relationsPath+"record-load", variables, 2, false, 1);
		//processFile(RELATIONS_PATH+"record-store");
		processFile(relationsPath+ROLE_FILE_PREFIX+"alloc", variables, 2, false, 0);
		processFile(relationsPath+ROLE_FILE_PREFIX+"assign", variables, 1, true, 1);
		
		long countPureAliases = 0;
		for(Entry<String, HashRecord> entry : variables.entrySet()) {
			if(entry.getValue().value==1) {
				++countPureAliases;
			}
		}
		return countPureAliases;
	}

	@Test
	public final void renumberRewritten() {
		
		//Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());
		
		HashMap<String,HashRecord> variables = new HashMap<String, HashRecord>();
		
		String relationsPath = PDStore.DEFAULT_FILEPATH+"testrelations/relations/";
		long countPureAliases = findPureAliases(variables, relationsPath);
		System.err.println("\ncount: "+countPureAliases);
		System.err.println();
		
	
	
		String relationsSplitPath = PDStore.DEFAULT_FILEPATH+"testrelations/relationsRewrite/";
		String relationsOutPath = PDStore.DEFAULT_FILEPATH+"testrelations/relationsRenumber2/";
		
		renameRenumber(relationsSplitPath+"record-store", relationsOutPath+"record-store", variables, false);
		renameRenumber(relationsSplitPath+"record-load", relationsOutPath+"record-load", variables, false);
		renameRenumber(relationsSplitPath+ROLE_FILE_PREFIX+"alloc", relationsOutPath+ROLE_FILE_PREFIX+"alloc", variables, false);
		renameRenumber(relationsSplitPath+ROLE_FILE_PREFIX+"assign", relationsOutPath+ROLE_FILE_PREFIX+"assign", variables, true);
		renameRenumber(relationsSplitPath+ROLE_FILE_PREFIX+"assignSplit", relationsOutPath+ROLE_FILE_PREFIX+"assignSplit", variables, true);
		
		System.err.println();
		System.err.println("\nrunningCounter: "+HashRecord.runningCounter);
		System.err.println();

	
	}

}
class HashRecord {
	public HashRecord(Long value, String lastAssigned) {
		super();
		this.value = value;
		this.lastAssigned = lastAssigned;
		this.runningNumber = ++runningCounter;
	}
	Long value = 0L;
	String lastAssigned = null;
	long runningNumber = 0;
    static long runningCounter = 1000000000L;
}
