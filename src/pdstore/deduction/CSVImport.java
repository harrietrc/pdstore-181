package pdstore.deduction;

import au.com.bytecode.opencsv.CSVReader;
import pdstore.GUID;
import pdstore.PDStore;
import pointsto.PointsToRules;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clut002 on 20-May-14.
 */
public class CSVImport {
    /** Needed for Heisenbug, see todo.txt, 2014-04-19
     *
     */
    public static Map<GUID, String> debugMap = new HashMap<GUID, String>();

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
    public static void readRecords(String fileName, PDStore store, GUID... roles ) {
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
            int j = 0;
            while ((nextLine = reader.readNext()) != null) {
                GUID left = new GUID();

                for(int i = 0; i<roles.length; ++i) {
                    GUID right = store.getGUIDwithName(nextLine[i]);
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

    /**
     *  Reads triples for a single role from a .csv file
* @param role2name The name of role2 for all triples, becomes part of the filename
* @param prefix the filename is prefix+role2name+".csv"
* @param store
* @param swapped
*/
    public static void readTriplesFixedRole(String role2name, String prefix, PDStore store, boolean swapped) {
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
                GUID left = getGUID(store, nextLine[0]);
                GUID right =  getGUID(store, nextLine[1]);
if(swapped)
                    store.addLink(right, role2, left);
else
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


        PointsToRules.printRuleCount(role2name);
        PointsToRules.printRuleCount("storeLoad");
    }

    /**  Needed to track the setName heisenbug.
	 * @param store
	 * @param name1
	 * @return
	 */
	public static GUID getGUID(PDStore store, String name1) {
		GUID id1 = store.getId(name1);
		if(id1!=null)
			return id1;
		id1 = new GUID();
		debugMap.put(id1, name1);
		store.setName(id1, name1);
		return id1;

	}

    public static void progressReport(int j) {
        if(j%100==0) {
            if(j%10000==0) {
                System.err.println();
                System.err.print(j/1000);
                System.err.print(",000 ");
            }
            System.err.print(",");
        }
    }
}
