package pdstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.junit.Test;

import pdstore.dal.PDGen;
import pdstore.dal.PDModel;
import pdstore.dal.PDSimpleWorkingCopy;
import pdstore.dal.PDWorkingCopy;
import pdstore.ui.graphview.dal.PDNode;

public class PDGenDALTest extends TestCase {

	// use local embedded store
	PDStore store = new PDStore("PDGenTest");;

	public GUID guid1 = new GUID("11111111111111111111111111111111");
	public GUID guid2 = new GUID("22222222222222222222222222222222");
	public GUID guid3 = new GUID("33333333333333333333333333333333");
	public GUID guid4 = new GUID("44444444444444444444444444444444");
	public GUID guid5 = new GUID("55555555555555555555555555555555");

	public GUID roleA = new GUID("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	public GUID roleB = new GUID("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
	public GUID roleC = new GUID("cccccccccccccccccccccccccccccccc");

	public String stringHello = "hello";
	public String stringWorld = "world";
	public String stringGood = "good";
	public String stringMorning = "morning";

	public Timestamp ts1 = new Timestamp(0);
	public Timestamp ts2 = new Timestamp(1);;

	public Blob blob111 = new Blob(new byte[] { 1, 1, 1 });
	public Blob blob999 = new Blob(new byte[] { 9, 9, 9 });

	protected void setUp() {
		store = new PDStore("PDGenTest");
	}

	private void simpleModelSetup() {

		GUID transaction = store.begin();
		GUID modelId = new GUID("f7b81e92685211dfb501020054554e00");
		GUID personId = new GUID("f7b81e92685211dfb501020054554e02");
		GUID nameId = new GUID("f7b81e92685211dfb501020054554e03");
		GUID hasRole = new GUID("f7b81e92685211dfb501020054554e04");

		store.createModel(transaction, modelId, "Detail");
		store.createType(transaction, modelId, personId, "Person");
		store.createType(transaction, modelId, nameId, "FullName");
		store.createRelation(transaction, personId, "Person", "FullName",
				hasRole, nameId);

		store.commit(transaction);
	}

	/**
	 * In this test the PDGen class generation is tested. This model should
	 * generate 2 Classes PDPerson & PDFullName.
	 */
	public final void testPDGen() {
		simpleModelSetup();
		GUID transaction = store.begin();
		// Generate PDClass
		try {
			PDGen.generate(store, "Detail", null, "src",
					"pdstore.dal.generated");
		} catch (Exception e) {
			fail();
		}

		// Get File
		String person = "";
		String fullName = "";
		if (System.getProperty("os.name").contains("Windows")) {
			person = "src\\pdstore\\dal\\generated\\PDPerson.java";
			fullName = "src\\pdstore\\dal\\generated\\PDFullName.java";
		} else {
			person = "src/pdstore/dal/PDPerson.java";
			fullName = "src/pdstore/dal/PDFullName.java";
		}

		File personFile = new File(person);
		File nameFile = new File(fullName);
		store.commit(transaction);

		assertTrue(personFile.exists());
		assertTrue(nameFile.exists());

		// Cleanup
		int count = 0;
		// while (!personFile.delete() && count < 10){
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// }
		// count++;
		// }
		// if (count > 9){
		// System.err.println("Cannot Delete personFile. Please Delete It yourself.");
		// }
		// count = 0;
		// while (!nameFile.delete()&& count < 10){
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// }
		// count++;
		// }
		// if (count > 9){
		// System.err.println("Cannot Delete nameFile. Please Delete It yourself.");
		// }

	}

	private void simpleModelWithStringSetup() {

		GUID transaction = store.begin();
		GUID modelId = new GUID();
		GUID personId = new GUID();
		GUID hasRole = new GUID();

		store.createModel(transaction, modelId, "DetailV2");
		store.createType(transaction, modelId, personId, "Indivdual");
		store.createRelation(transaction, personId, "", "hasName", hasRole,
				PDStore.STRING_TYPEID);

		store.commit(transaction);
	}

	public final void testCleanUp() {
		assertTrue(removePDStore());
	}

	private boolean removePDStore() {
		store = null;
		System.gc();
		int count = 0;
		String pdstore = "";
		if (System.getProperty("os.name").contains("Windows")) {
			pdstore = "pddata\\PDGenTest.pds";

		} else {
			pdstore = "pddata/PDGenTest.pds";

		}
		File dafile = new File(pdstore);
		System.out.println("PDStore Exist: " + dafile.exists());
		boolean success = dafile.delete();
		while (!success && count < 10) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			success = dafile.delete();
			count++;
		}
		if (count > 9) {
			System.err
					.println("Cannot Delete PDStore. Please Delete It yourself.");
			dafile.deleteOnExit();
		}

		return success;
	}
}
