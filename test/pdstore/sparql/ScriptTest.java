package pdstore.sparql;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.junit.BeforeClass;
import org.junit.Test;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.scripting.Interpreter;

public class ScriptTest {

	static PDStore store;

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "MatchTest-" + System.nanoTime();
		store = new PDStore(fileName);
	}

	@Test
	public final void testScript_XRY() {

		Interpreter i = new Interpreter(store);
		i.getQuery = true;
		i.executePDLString("2 $11223344556677889900112233445566 5; "
				+ "3 $11223344556677889900112233445566 2; " + "commit; "
				+ "?x $11223344556677889900112233445566 ?y ? " + "commit; ");

		List<Object> results = new ArrayList<Object>();
		while (i.lastResult.hasNext()) {
			Assignment<GUID, Object, GUID> resultRow = i.lastResult.next();
			Object var = resultRow.getVar("?x");
			Object xValue = resultRow.get(var);
			results.add(xValue);
			results.add(resultRow.get(resultRow.getVar("?y")));
			results.add(resultRow.get(resultRow.getVar("?z")));
		}
		assertTrue(results.contains(2L));
		assertTrue(results.contains(5L));
		assertTrue(results.contains(3L));
	}

	@Test
	public final void testScript_XRY_YRZ() {

		Interpreter i = new Interpreter(store);
		i.getQuery = true;
		i.executePDLString("2 $11223344556677889900112233445566 5; "
				+ "3 $11223344556677889900112233445566 2; " + "commit; "
				+ "?x, $11223344556677889900112233445566 ?y "
				+ "?y $11223344556677889900112233445566 ?z ? " + "commit; ");

		List<Object> results = new ArrayList<Object>();
		while (i.lastResult.hasNext()) {
			Assignment<GUID, Object, GUID> resultRow = i.lastResult.next();
			Object var = resultRow.getVar("?x");
			Object xValue = resultRow.get(var);
			results.add(xValue);
			results.add(resultRow.get(resultRow.getVar("?y")));
			results.add(resultRow.get(resultRow.getVar("?z")));
		}
		assertTrue(results.contains(2L));
		assertTrue(results.contains(5L));
		assertTrue(results.contains(3L));
	}

}
