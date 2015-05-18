package pdstore.sparql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.junit.BeforeClass;
import org.junit.Test;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.sparql.QueryOrderBy.Order;

//TODO: more test cases for IXX, XXI and XXX
public class QueryExecutionTest {

	static PDStore store;

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "QueryExecutionTest-" + System.nanoTime();
		store = new PDStore(fileName);
	}

	/**
	 * tuples in where clause: reading kind IRI. writing kind: XRI input:
	 * addLink(transactionID, "Ernie", hasBrother, "Bert"); where: added t ?x
	 * hasBrother Bert
	 */
	@Test
	public final void testXRI() {
		GUID hasBrother = new GUID();
		Variable varX = new Variable("x");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, "Bert");
		query = new Query(select, where, null, null, store);
		// TODO: setting of different optimizers must be done either
		// consistently,
		// or define a new testCase.
		// query.currentOptimizer = Query.optimimzerType.CBOV4;
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
	}

	/**
	 * tuples in where clause: reading kind IRI. writing kind: IXI input:
	 * addLink(transactionID, "Ernie", hasBrother, "Bert"); where: added t Ernie
	 * varX Bert
	 */
	@Test
	public final void testIXI() {
		GUID hasBrother = new GUID();
		Variable varX = new Variable("x");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", varX, "Bert");
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains(hasBrother),
				"should return hasBrother.");
	}

	@Test
	public final void testXXX() {
		GUID transactionID = store.begin();

		// add reflexive tuple
		GUID reflexiveGuid = new GUID();
		store.addLink(transactionID, reflexiveGuid, reflexiveGuid,
				reflexiveGuid);

		// query all reflexive tuples
		Variable varX = new Variable("x");
		List<Variable> select = new ArrayList<Variable>();

		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, varX, varX);
		where.add(w1);
		Query query = new Query(null, where, null, null, store);
		query.setFilter(new EqualExpression(varX, reflexiveGuid));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(transactionID);
		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));

		}
		System.out.println(results.size());

		Debug.assertTrue(results.contains(reflexiveGuid),
				"should return reflexiveGuid");
		store.commit(transactionID);
	}

	/**
	 * tuples in where clause: reading kind IRI. writing kind: IRX input:
	 * addLink(transactionID, "Ernie", hasBrother, "Bert"); where: added t Ernie
	 * hasBrother varX
	 */
	@Test
	public final void testIRX() {
		GUID hasBrother = new GUID();
		Variable varX = new Variable("x");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother, varX);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
	}

	/**
	 * tuples in where clause: (1) reading kind XRI. writing kind: XRI (2)
	 * reading kind IRI. writing kind: XRX input: removed t Ernie hasBrother
	 * Bert,removed t Ernie likes donuts where:removed t ?x hasBrother Bert,
	 * removed t ?x likes ?y
	 */
	@Test
	public final void testXRI_XRY() {
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID likes = new GUID();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.removeLink(transactionID, "Ernie", hasBrother, "Bert");
		store.removeLink(transactionID, "Ernie", likes, "donuts");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, likes, varY);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w2);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.contains("donuts"), "should return donuts.");
		Debug.assertTrue(results.size() == 2,
				"the results should have size of 2.");
	}

	/**
	 * tuples in where clause: (1) reading kind XRI. writing kind: IRX (2)
	 * reading kind IRI. writing kind: XRI input: removed t Ernie hasBrother
	 * Bert,removed t Ernie likes donuts where: removed t ?x hasBrother ?y,
	 * removed t ?x likes donuts
	 */
	@Test
	public final void testXRY_XRI() {
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID likes = new GUID();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.removeLink(transactionID, "Ernie", hasBrother, "Bert");
		store.removeLink(transactionID, "Ernie", likes, "donuts");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, hasBrother, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, likes, "donuts");
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w2);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
		Debug.assertTrue(results.size() == 2,
				"the results should have size of 2.");
	}

	/**
	 * tuples in where clause: (1) reading kind XRI. writing kind: IRX (2)
	 * reading kind IRI. writing kind: XRX input: (removed t Ernie hasBrother
	 * Bert), (removed t Ernie likes donuts) where: (removed t ?x hasBrother
	 * ?y), (removed t ?x likes ?z)
	 */
	@Test
	public final void testXRY_XRZ_withIncomingAssignment() {
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID likes = new GUID();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.removeLink(transactionID, "Ernie", hasBrother, "Bert");
		store.removeLink(transactionID, "Ernie", likes, "donuts");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, hasBrother, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, likes, varZ);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w2);
		query.getWhere().add(w1);
		Assignment incomingAssignment = new Assignment();
		incomingAssignment.put(varZ, "donuts");

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null, incomingAssignment);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
		Debug.assertTrue(results.get(2).equals("donuts"),
				"should return donuts.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");
	}

	/**
	 * tuples in where clause: (1) reading kind XRI. writing kind: IRX (2)
	 * reading kind IRI. writing kind: XRX input: removed t Ernie hasBrother
	 * Bert,removed t Ernie likes donuts where:removed t ?x hasBrother ?y,
	 * removed t ?x likes ?z
	 */
	@Test
	public final void testXRY_XRZ() {
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID likes = new GUID();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.removeLink(transactionID, "Ernie", hasBrother, "Bert");
		store.removeLink(transactionID, "Ernie", likes, "donuts");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, hasBrother, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, likes, varZ);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w2);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
		Debug.assertTrue(results.get(2).equals("donuts"),
				"should return donuts.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");
	}

	/**
	 * input: removeLink(transactionID, "Ernie", hasBrother, "Bert"); where:
	 * added t ?x hasBrother Bert no match found
	 * 
	 * where: removed t ?x hasBrother Bert
	 */
	@Test
	public final void testXRI_1() {
		Variable varX = new Variable("x");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.removeLink(transactionID, "Ernie", hasBrother, "Bert");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, "Bert");
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}

		Debug.assertTrue(results.size() == 0,
				"the results should have size of 0.");

		w1 = new PDChange<GUID, Object, GUID>(ChangeType.LINK_REMOVED,
				transactionID, varX, hasBrother, "Bert");
		query.getWhere().set(0, w1);

		assignmentIterator = query.execute(null);

		results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.size() == 1,
				"the results should have size of 1.");
	}

	/**
	 * tuples in where clause: (1) reading kind XRI. writing kind: IRI (2)
	 * reading kind IRI. writing kind: XRI input: removed t Ernie hasBrother
	 * Bert,removed t Ernie likes donuts where: removed t ?x hasBrother Bert,
	 * removed t ?x likes donuts
	 */
	@Test
	public final void testXRI_XRI() {
		Variable varX = new Variable("x");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID likes = new GUID();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.removeLink(transactionID, "Ernie", hasBrother, "Bert");
		store.removeLink(transactionID, "Ernie", likes, "donuts");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, likes, "donuts");
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w2);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.size() == 1,
				"the results should have size of 1.");
	}

	/**
	 * tuples in where clause: (1) reading kind IRI. writing kind: IRX input:
	 * added t Ernie hasBrother Jack, added t Ernie hasBrother Bob where: added
	 * t Ernie hasBrother ?x
	 */
	@Test
	public final void testIRX_1() {
		Variable varX = new Variable("x");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Jack");
		store.addLink(transactionID, "Ernie", hasBrother, "Bob");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother, varX);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains("Jack"), "should return Jack.");
		Debug.assertTrue(results.contains("Bob"), "should return Bob.");
		Debug.assertTrue(results.size() == 2,
				"the results should have size of 2.");
	}

	/**
	 * input:added t Ernie hasBrother Bert, added t Ernie likes donuts where:
	 * added t ?y hasBrother ?x, added t ?x likes donuts should not return
	 * anything
	 * 
	 * * where: added t ?y hasBrother ?x, added t ?y likes donuts, added t ?y
	 * likes ?z, tuples in where clause: (1) reading kind XRI. writing kind: IRX
	 * (1) reading kind XRI. writing kind: IRI (3) reading kind IRI. writing
	 * kind: XRX
	 */
	@Test
	public final void testYRX_YRI_YRZ() {
		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();
		GUID likes = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie", likes, "donuts");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, hasBrother, varX);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, likes, "donuts");
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		assertTrue(!assignmentIterator.hasNext());

		PDChange<GUID, Object, GUID> w3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, likes, varZ);
		w2 = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				transactionID, varY, likes, "donuts");
		query.getWhere().clear();
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.getWhere().set(1, w2);
		query.getWhere().add(w3);
		query.isOptimized = false;
		assignmentIterator = query.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
		Debug.assertTrue(results.get(2).equals("donuts"),
				"should return donuts.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");
	}

	/**
	 * tuples in where clause: (1) reading kind IRI. writing kind: XRI (2)
	 * reading kind IRX. writing kind: XRI (3) reading kind IRI. writing kind:
	 * XRI input: added t Ernie hasBrother Bert, added t Bert likes donuts,
	 * removed t donuts isCookedBy Bob where: added t ?x hasBrother ?y, added t
	 * ?y likes ?z, removed t ?z isCookedBy Bob
	 */
	@Test
	public final void testXRY_YRZ_ZRI() {
		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID isCookedBy = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Bert", likes, "donuts");
		store.removeLink(transactionID, "donuts", isCookedBy, "Bob");

		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, likes, varZ);
		PDChange<GUID, Object, GUID> w3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varZ, isCookedBy, "Bob");
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.getWhere().add(w3);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
		Debug.assertTrue(results.get(2).equals("donuts"),
				"should return donuts.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");
	}

	/**
	 * input: added t 2 plus3 5 , added t 3 plus3 6, added t 4 plus3 7 where:
	 * added t ?x plus3 5 filter t t < new GUID(),t ?x < 4
	 * 
	 * where: added ?t ?x plus3 5.0, added t 3 plus3 ?z, added t 2 plus3 ?y
	 * filter t t > new GUID(),t ?x < 4
	 * 
	 * where: added ?t ?x plus3 5.0, added t 3 plus3 ?z, added t 2 plus3 ?y
	 * filter t t < new GUID(), t ?x < 4, t 10.0 > ?y
	 */
	@Test
	public final void testFilter() {
		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID plus3 = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, 2, plus3, 5);
		store.addLink(transactionID, 3, plus3, 6);
		store.addLink(transactionID, 4, plus3, 7);
		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, 5);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);

		query.setFilter(new AndExpression(new LessThanExpression(transactionID,
				new GUID()), new LessThanExpression(varX, 4L)));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		assertTrue(Integer.parseInt(results.get(0).toString()) == 2);
		assertTrue(results.size() == 1);

		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3, plus3, varZ);
		PDChange<GUID, Object, GUID> w3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, plus3, varY);

		query.getWhere().add(w2);
		query.getWhere().add(w3);

		query.setFilter(new AndExpression(new LessThanExpression(transactionID,
				new GUID()), new LessThanExpression(varX, 4L)));

		query.isOptimized = false;
		query.queryAssignedVariablesSoFar.clear();

		assignmentIterator = query.execute(null);
		results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(Integer.parseInt(results.get(0).toString()) == 2,
				"should return 2.");
		Debug.assertTrue(Integer.parseInt(results.get(1).toString()) == 5,
				"should return 5.");
		Debug.assertTrue(Integer.parseInt(results.get(2).toString()) == 6,
				"should return 6.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");

		query.setFilter(new AndExpression(new LessThanExpression(transactionID,
				new GUID()), new LessThanExpression(varX, 4),
				new GreaterThanExpression(10.0, varY)));
		assignmentIterator = query.execute(null);
		results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(Integer.parseInt(results.get(0).toString()) == 2,
				"should return 2.");
		Debug.assertTrue(Integer.parseInt(results.get(1).toString()) == 5,
				"should return 5.");
		Debug.assertTrue(Integer.parseInt(results.get(2).toString()) == 6,
				"should return 6.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");
	}

	/**
	 * test transactionID as variable input: added t Ernie hasBrother Bert,
	 * added t Bert likes donuts, removed t donuts isCookedBy Bob where: added
	 * ?t ?x hasBrother ?y,
	 */
	@Test
	public final void testXRX() {
		Variable varT = new Variable("t");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID isCookedBy = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Bert", likes, "donuts");
		store.removeLink(transactionID, "donuts", isCookedBy, "Bob");

		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, hasBrother, varY);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		List<Object> resultSet = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			resultSet.add(result.get(varT));
		}
		Debug.assertTrue(resultSet.size() == 1, "should have 1 result");
		Debug.assertTrue(resultSet.contains(transactionID),
				"should contain the transactionID");
	}

	@Test
	public void testMetamodel1() {
		// Debug.addDebugTopic("SPARQL");

		Variable type = new Variable("type");
		Variable typeName = new Variable("type name");

		List<Variable> select = new ArrayList<Variable>();
		select.add(typeName);

		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		where.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null,
				type, PDStore.HAS_TYPE_ROLEID, PDStore.TYPE_TYPEID));
		where.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null,
				type, PDStore.NAME_ROLEID, typeName));
		Query query = new Query(select, where, null, null, store);
		Iterator<Assignment<GUID, Object, GUID>> resultSet = query
				.execute(null);

		List<Object> typeNames = new ArrayList<Object>();
		while (resultSet.hasNext()) {
			Assignment<GUID, Object, GUID> result = resultSet.next();
			typeNames.add(result.get(typeName));
		}

		Debug.assertTrue(
				typeNames.size() > 0,
				"The type name query should return at least the type names of the metamodel types.");
		Debug.assertTrue(
				typeNames.contains("Type"),
				"The type name query should return at least the type names of the metamodel types.");
		Debug.assertTrue(
				typeNames.contains("Role"),
				"The type name query should return at least the type names of the metamodel types.");
		Debug.assertTrue(
				typeNames.contains("Model"),
				"The type name query should return at least the type names of the metamodel types.");
	}

	@Test
	public void testMetamodel2() {
		// Debug.addDebugTopic("SPARQL");

		Variable type = new Variable("type");
		Variable typeName = new Variable("type name");

		List<Variable> select = new ArrayList<Variable>();
		select.add(typeName);

		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		where.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_EFFECTIVE,
				null, type, PDStore.HAS_TYPE_ROLEID, PDStore.TYPE_TYPEID));
		where.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_EFFECTIVE,
				null, type, PDStore.NAME_ROLEID, typeName));
		Query query = new Query(select, where, store);
		Iterator<Assignment<GUID, Object, GUID>> resultSet = query
				.execute(null);

		List<Object> typeNames = new ArrayList<Object>();
		while (resultSet.hasNext()) {
			Assignment<GUID, Object, GUID> result = resultSet.next();
			typeNames.add(result.get(typeName));
		}

		Debug.assertTrue(
				typeNames.size() > 0,
				"The type name query should return at least the type names of the metamodel types.");
		Debug.assertTrue(
				typeNames.contains("Type"),
				"The type name query should return at least the type names of the metamodel types.");
		Debug.assertTrue(
				typeNames.contains("Role"),
				"The type name query should return at least the type names of the metamodel types.");
		Debug.assertTrue(
				typeNames.contains("Model"),
				"The type name query should return at least the type names of the metamodel types.");
	}

	@Test
	public void testQueryincomingassignment() {
		// add test data
		GUID hasBrother = new GUID();
		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "William", hasBrother, "Bert");
		store.addLink(transactionID, "Jack", hasBrother, "Jim");
		store.addLink(transactionID, "Beck", hasBrother, "Mark");
		store.addLink(transactionID, "Tommy", hasBrother, "Paul");
		transactionID = store.commit(transactionID);

		// specify query
		Query query;
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		where.add(w1);
		query = new Query(null, where, null, null, store);
		Assignment incomingAssignment = new Assignment();
		incomingAssignment.put(varY, "Bert");

		// execute query and check result
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null, incomingAssignment);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(2, count);
	}

	@Test
	public void testLimitFunctionality() {
		GUID hasBrother = new GUID();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		Assignment incomingAssignment = new Assignment();
		incomingAssignment.put(varY, "Bert");
		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "William", hasBrother, "Bert");
		store.addLink(transactionID, "Jack", hasBrother, "Jim");
		store.addLink(transactionID, "Beck", hasBrother, "Mark");
		store.addLink(transactionID, "Tommy", hasBrother, "Paul");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		/*
		 * Iterator<Assignment<GUID, Object, GUID>> assignmentIterator =
		 * query.execute(null,incomingAssignment); int count=0; while
		 * (assignmentIterator.hasNext()) { assignmentIterator.next(); count++;
		 * } assertEquals(2,count);
		 */

		// Limit functionality
		query.limit = 1;
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null, incomingAssignment);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(1, count);
	}

	@Test
	public void testOffsetFunctionality() {
		GUID hasBrother = new GUID();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		Assignment incomingAssignment = new Assignment();
		incomingAssignment.put(varY, "Bert");
		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "William", hasBrother, "Bert");
		store.addLink(transactionID, "Jack", hasBrother, "Jim");
		store.addLink(transactionID, "Beck", hasBrother, "Mark");
		store.addLink(transactionID, "Tommy", hasBrother, "Paul");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		/*
		 * Iterator<Assignment<GUID, Object, GUID>> assignmentIterator =
		 * query.execute(null,incomingAssignment); int count=0; while
		 * (assignmentIterator.hasNext()) { assignmentIterator.next(); count++;
		 * } assertEquals(2,count);
		 */

		// offset functionality
		query.offset = 1;
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null, incomingAssignment);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(1, count);
	}

	/**
	 * tuples in where clause: (1) reading kind XRI. writing kind: IRX (2)
	 * reading kind IRI. writing kind: XRX input: removed t Ernie hasBrother
	 * Bert,removed t Ernie likes donuts where:removed t ?x hasBrother ?y,
	 * removed t ?x likes ?z
	 */
	@Test
	public final void testXRY_XRZ_withIncomingAssignmentAdded() {
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID likes = new GUID();
		GUID hasBrother = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie", likes, "donuts");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, likes, varZ);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w2);
		query.getWhere().add(w1);
		Assignment incomingAssignment = new Assignment();
		incomingAssignment.put(varZ, "donuts");

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null, incomingAssignment);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
		Debug.assertTrue(results.get(2).equals("donuts"),
				"should return donuts.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");

	}

	@Test
	public void testDistinct() {
		GUID hasBrother = new GUID();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "jack", hasBrother, "jill");
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "jack", hasBrother, "jill");
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.distinct = true;
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null, null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(2, count);
	}

	@Test
	public void testOrderby() {
		GUID hasBrother = new GUID();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie2", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie3", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie0", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie0", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie0", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie0", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie1", hasBrother, "Bert");
		store.addLink(transactionID, "Ernie1", hasBrother, "Bert");
		/*
		 * store.addLink(transactionID, "jack1", hasBrother, "jill");
		 * store.addLink(transactionID, "Ernie4", hasBrother, "Bert");
		 * store.addLink(transactionID, "Ernie5", hasBrother, "Bert");
		 * store.addLink(transactionID, "jack1", hasBrother, "jill");
		 */

		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.distinct = true;
		QueryOrderBy qo = new QueryOrderBy(varX, Order.asc);
		query.orderby.add(qo);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null, null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			if (count == 0)
				assertEquals("Ernie0", result.get(varX));
			count++;
		}
		assertEquals(4, count);
	}

	@Test
	public void testUnion() {
		GUID hasfriend = new GUID();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query1;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie2", hasfriend, "Bert");
		store.addLink(transactionID, "Ernie3", hasfriend, "Bert");
		store.addLink(transactionID, "Ernie3", hasfriend, "Bert");
		store.addLink(transactionID, "Ernie4", hasfriend, "Bert");
		store.addLink(transactionID, "Ernie5", hasfriend, "Bert");
		/*
		 * store.addLink(transactionID, "Ernie3", hasBrother, "Bert");
		 * store.addLink(transactionID, "Ernie3", hasBrother, "Bert1");
		 */

		transactionID = store.commit(transactionID);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasfriend, varY);

		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasfriend, varY);
		// query 1
		query1 = new Query(select, where, null, null, store);
		query1.getWhere().add(w1);
		// query 2
		Query query2 = new Query(select, where, null, null, store);
		query2.getWhere().add(w2);

		Query finalQuery = new Query(false, query1, query2);
		finalQuery.offset = 1;
		finalQuery.limit = 2;
		QueryOrderBy qo = new QueryOrderBy(varX, Order.desc);
		finalQuery.orderby.add(qo);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = finalQuery
				.execute(null, null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			if (count == 0) {
				assertEquals("Ernie4", result.get(varX));
			}
			count++;
		}
		assertEquals(2, count);
	}

	/**
	 * test transactionID as variable input: added t Ernie hasBrother Bert,
	 * added t Bert likes donuts, removed t donuts isCookedBy Bob where: added
	 * ?t ?x hasBrother ?y,
	 */
	@Test
	public final void testXRXEffectivelyAdded() {
		Variable varT = new Variable("t");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID isCookedBy = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Bert", likes, "donuts");
		store.removeLink(transactionID, "donuts", isCookedBy, "Bob");

		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_EFFECTIVELY_ADDED, varT, varX, hasBrother, varY);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		List<Object> resultSet = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			resultSet.add(result.get(varT));
		}
		Debug.assertTrue(resultSet.size() == 1, "should have 1 result");
		Debug.assertTrue(resultSet.contains(transactionID),
				"should contain the transactionID");
	}

	/**
	 * test transactionID as variable input: added t Ernie hasBrother Bert,
	 * added t Bert likes donuts, removed t donuts isCookedBy Bob where: added
	 * ?t ?x hasBrother ?y,
	 */
	@Test
	public final void testXRXEffective() {
		Variable varT = new Variable("t");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID isCookedBy = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Bert", likes, "donuts");
		store.removeLink(transactionID, "donuts", isCookedBy, "Bob");
		transactionID = store.commit(transactionID);

		GUID transactionID2 = store.begin();
		store.removeLink(transactionID2, "Ernie", hasBrother, "Bert");
		transactionID2 = store.commit(transactionID2);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_EFFECTIVE, varT, varX, hasBrother, varY);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		List<Object> resultSet = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			resultSet.add(result.get(varT));
		}
		Debug.assertTrue(resultSet.size() == 1, "should have 1 result");
		Debug.assertTrue(resultSet.contains(transactionID2),
				"should contain the transactionID");
	}

	/**
	 * test transactionID as variable input: added t Ernie hasBrother Bert,
	 * added t Bert likes donuts, removed t donuts isCookedBy Bob where: added
	 * ?t ?x hasBrother ?y,
	 */
	@Test
	public final void testXRXEffectivelyAddedNoResult() {
		Variable varT = new Variable("t");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID isCookedBy = new GUID();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "Ernie", hasBrother, "Bert");
		store.addLink(transactionID, "Bert", likes, "donuts");
		store.removeLink(transactionID, "donuts", isCookedBy, "Bob");
		transactionID = store.commit(transactionID);

		GUID transactionID2 = store.begin();
		store.removeLink(transactionID2, "Ernie", hasBrother, "Bert");
		transactionID = store.commit(transactionID2);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_EFFECTIVELY_ADDED, varT, varX, hasBrother, varY);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		List<Object> resultSet = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			resultSet.add(result.get(varT));
		}
		Debug.assertTrue(resultSet.size() == 0, "should have 0 results");
	}

}
