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

public class MatchTest {

	static PDStore store;

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "MatchTest-" + System.nanoTime();
		store = new PDStore(fileName);
	}

	/**
	 * tuples in where clause: (1) reading kind XRI. writing kind: IRI (2)
	 * reading kind IRI. writing kind: XRI input: removed t Ernie hasBrother
	 * Bert,removed t Ernie likes donuts where: removed t ?x hasBrother Bert,
	 * removed t ?x likes donuts
	 */
	@Test
	public final void testXRI_XRI() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Ernie", likes,
				"Donuts");
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, likes, "Donuts");

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);

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
	 * tuples in where clause: (1) reading kind IRX. writing kind: XRI (2)
	 * reading kind IRI. writing kind: XRI input: added t Ernie hasBrother Bert,
	 * removed t Bert likes donuts where: added t ?x hasBrother ?y, removed t ?y
	 * likes donuts
	 */
	@Test
	public final void testXRY_YRI() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Bert", likes, "Donuts");

		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varY, likes, "Donuts");

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);

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
	}

	/**
	 * input: added t Ernie hasBrother Bert, added t Ernie likes donuts, added t
	 * Ernie hasBrother Bob where: added t ?x hasBrother Bert, added t ?x likes
	 * donuts, added t ?x hasBrother Bob
	 */
	@Test
	public final void testXRI_XRI_XRI() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", likes, "Donuts");
		PDChange<GUID, Object, GUID> l3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bob");

		store.addChange(l1);
		store.addChange(l2);
		store.addChange(l3);
		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, "Bert");
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, likes, "Donuts");
		PDChange<GUID, Object, GUID> w3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, "Bob");

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
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
	}

	/**
	 * input: added t Ernie hasBrother Bert, added t Bert likes donuts, removed
	 * t donuts isCookedBy Bob where: added t ?x hasBrother ?y, added t ?y likes
	 * ?z, removed t ?z isCookedBy Bob
	 */
	@Test
	public final void testXRY_YRZ_ZRI() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		GUID transactionID = store.begin();
		GUID isCookedBy = new GUID();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Bert", likes, "Donuts");
		PDChange<GUID, Object, GUID> l3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Donuts", isCookedBy,
				"Bob");
		store.addChange(l1);
		store.addChange(l2);
		store.addChange(l3);
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
		Debug.assertTrue(results.contains("Donuts"), "should return Donuts.");
	}

	/**
	 * test equal filter transactionID as variable input: added t 2 role2 5
	 * where: added ?t ?x role2 5 filter ?t == transactionID
	 */
	@Test
	public final void testXRI_F() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		GUID role2 = new GUID();
		long a = 5;
		long b = 2;
		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, b, role2, a);
		store.addChange(l1);
		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, a);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(new EqualExpression(varT, transactionID));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains(b), "should return 2.");
	}

	/**
	 * case16: test equal filter , comparing 2 variables input: added t 2 role2
	 * 5, added t 3 role2 5 where: added t ?x role2 5, added t ?y role2 5 filter
	 * t ?x = ?y should return null
	 */
	@Test
	public final void testXRI_YRI_F() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3, role2, 5);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varY, role2, 5);
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(new EqualExpression(varT, new GUID()));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		Debug.assertTrue(!assignmentIterator.hasNext(),
				"should return nothing.");
	}

	/**
	 * test notEqual filter transactionID as variable input: added t 2 role2 5
	 * where: added ?t ?x role2 5 filter ?t != new GUID(), ?x != 4
	 */
	@Test
	public final void testXRI_FF() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(new AndExpression(new NotEqualExpression(varT,
				new GUID()), new NotEqualExpression(varX, 4)));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		assertTrue(results.contains(2L));
	}

	/**
	 * test lessThan filter input: added t 2 role2 5 where: added t ?x role2 5
	 * filter t ?x < 3
	 */
	@Test
	public final void testXRI_F1() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);

		query = new Query(select, where, null, null, store);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, role2, 5);
		query.getWhere().add(w1);
		query.setFilter(new LessThanExpression(varX, 3L));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		assertTrue(results.contains(2L));
	}

	/**
	 * test lessThan filter input: added t 2 role2 5 where: added t ?x role2 5
	 * filter t ?x < 0.1 should return null
	 */
	@Test
	public final void testXRI_F2() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(new LessThanExpression(varX, 0.1));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		assertTrue(!assignmentIterator.hasNext());
	}

	/**
	 * test lessThan filter input: added t 2 role2 5 where: added ?t ?x role2 5
	 * filter t ?t < new GUID(), ?x < 4
	 */
	@Test
	public final void testXRI_FF1() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(new AndExpression(new LessThanExpression(varT,
				new GUID()), new LessThanExpression(varX, 4L)));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		assertTrue("should return 2.", results.contains(2L));
	}

	/**
	 * test lessThan filter input: added t 2 role2 5 where: added ?t ?x role2 5
	 * filter t ?t < new GUID(), ?x < 2 should return null
	 */
	@Test
	public final void tesXRI_FF() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);

		query = new Query(select, where, null, null, store);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		query.getWhere().add(w1);
		query.setFilter(new AndExpression(new LessThanExpression(varT,
				new GUID()), new LessThanExpression(varX, 2L)));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		assertTrue(!assignmentIterator.hasNext());
	}

	/**
	 * test lessThan filter input: added t 2 role2 5 where: added ?t ?x role2 5
	 * filter t new GUID() < ?t should return null
	 */
	@Test
	public final void testXRI_F3() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(new LessThanExpression(new GUID(), varT));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		Debug.assertTrue(!assignmentIterator.hasNext(),
				"should return nothing.");
	}

	/**
	 * test greaterThan filter input: added t 2 role2 5 where: added ?t ?x role2
	 * 5 filter t ?t > new GUID() should return null
	 */
	@Test
	public final void testXRI_FF2() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(new GreaterThanExpression(varT, new GUID()));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		Debug.assertTrue(!assignmentIterator.hasNext(),
				"should return nothing.");
	}

	/**
	 * case24: test greaterThan filter input: added t 2 role2 5 where: added ?t
	 * ?x role2 5 filter t new GUID() > ?t, 4 > ?x
	 */
	@Test
	public final void testXRI_FF3() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(new AndExpression(new GreaterThanExpression(new GUID(),
				varT), new GreaterThanExpression(4L, varX)));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		assertTrue(results.contains(2L));
	}

	/**
	 * case25: test filter input: added t 2 role2 5 , added t 3 role2 6 where:
	 * added ?t ?x role2 5, added t ?y role2 ?z filter: t new GUID() > ?t, 4 >
	 * ?x, ?z!= 4, ?y < 4, ?y > ?x
	 */
	@Test
	public final void testXRI_YRZ_FFFF() {
		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Variable varT = new Variable("t");

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3, role2, 6);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, role2, varZ);
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(new AndExpression(new GreaterThanExpression(new GUID(),
				varT), new GreaterThanExpression(4L, varX),
				new NotEqualExpression(varZ, 4L), new LessThanExpression(varY,
						4L), new GreaterThanExpression(varY, varX)));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		assertTrue(results.contains(2L));
		assertTrue(results.contains(6L));
		assertTrue(results.contains(3L));
	}

	/**
	 * test filter input: added t 2 role2 5 , added t 3 role2 6 where: added ?t
	 * ?x role2 5, added t ?y role2 ?z filter: t new GUID() > ?t, 4 > ?x, ?z!=
	 * 4, ?y < 4, ?y > ?z should return null (y is less than z)
	 */
	@Test
	public final void testXRI_YRZ_FFFF1() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3, role2, 6);
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		query = new Query(select, where, null, null, store);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, role2, varZ);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(new AndExpression(new GreaterThanExpression(new GUID(),
				varT), new GreaterThanExpression(4L, varX),
				new NotEqualExpression(varZ, 4L), new LessThanExpression(varY,
						4L), new GreaterThanExpression(varY, varZ)));

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		assertTrue(!assignmentIterator.hasNext());
	}

	/**
	 * case27: test filter input: added t 2 role2 5 , added t 3 role2 6 where:
	 * added ?t ?x role2 5, added t ?y role2 ?z filter: ?x > 3 or ?y > 2
	 */
	@Test
	public final void testXRI_YRZ_FF() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5L);
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3, role2, 6L);
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		query = new Query(select, where, null, null, store);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5L);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, role2, varZ);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(new OrExpression(new GreaterThanExpression(varX, 3L),
				new GreaterThanExpression(varY, 2L)));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		assertTrue(results.contains(2L));
		assertTrue(results.contains(6L));
		assertTrue(results.contains(3L));
	}

	/**
	 * case28: test filter input: added t 2 role2 5 , added t 3 role2 6 where:
	 * added ?t ?x role2 5, added t ?y role2 ?z filter: ?x >3 or ?y > 3
	 */
	@Test
	public final void testXRI_YRZ_FF1() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3, role2, 6);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, role2, varZ);
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(new OrExpression(new GreaterThanExpression(varX, 3L),
				new GreaterThanExpression(varY, 3L)));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		assertTrue(!assignmentIterator.hasNext());
	}

	/**
	 * case29: test filter input: added t 2 role2 5 , added t 3 role2 6 where:
	 * added ?t ?x role2 5, added t ?y role2 ?z filter: (?x >3 or ?y > 3) and ?z
	 * < 9
	 * 
	 * filter: (?x >3 or ?y > 3) and ?z > 9
	 * 
	 * filter: (?x >3 or ?y > 3) and (?z > 9 or ?x != 9)
	 */
	@Test
	public final void testF() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID role2 = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		Variable varT = new Variable("t");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2, role2, 5);
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3, role2, 6);
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		query = new Query(select, where, null, null, store);
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, role2, 5);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, role2, varZ);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(new AndExpression(new OrExpression(
				new GreaterThanExpression(varX, 3L), new GreaterThanExpression(
						varY, 3L), new LessThanExpression(varZ, 9L))));
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		assertTrue(results.contains(2L));
		assertTrue(results.contains(6L));
		assertTrue(results.contains(3L));

		query.setFilter(new AndExpression(new OrExpression(
				new GreaterThanExpression(varX, 3L), new GreaterThanExpression(
						varY, 3L), new GreaterThanExpression(varZ, 9L))));
		query.isOptimized = false;
		query.queryAssignedVariablesSoFar.clear();
		assignmentIterator = query.execute(null);

		Debug.assertTrue(!assignmentIterator.hasNext(),
				"should return nothing.");

		query.setFilter(new AndExpression(new OrExpression(
				new GreaterThanExpression(varX, 3L), new GreaterThanExpression(
						varY, 3L), new OrExpression(new GreaterThanExpression(
						varZ, 9L), new NotEqualExpression(varX, 9L)))));
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
		Debug.assertTrue(results.contains(2L), "should return 2.");
		Debug.assertTrue(results.contains(6L), "should return 6.");
		Debug.assertTrue(results.contains(3L), "should return 3.");
	}

	/**
	 * input: added t Ernie hasBrother Bert where: added t ?x ?y Bert
	 */
	@Test
	public final void testXYI() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, varY, "Bert");
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
			results.add(result.get(varY));
		}
		Debug.assertTrue(results.contains(hasBrother),
				"should return hasBrother.");
	}

	/**
	 * input: added t Ernie hasBrother Bert where: added t ?x ?y ?z
	 */
	@Test
	public final void testXYZ() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, varY, varZ);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
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
		Debug.assertTrue(results.contains(hasBrother),
				"should return hasBrother.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
	}

	/**
	 * input: added t Ernie hasBrother Bert where: added t "Ernie" ?y ?z
	 */
	@Test
	public final void testIYZ() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID transactionID = store.begin();
		Variable varY = new Variable("y");
		Variable varZ = new Variable("z");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", varY, varZ);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varY));
			results.add(result.get(varZ));
		}
		Debug.assertTrue(results.contains(hasBrother),
				"should return hasBrother.");
		Debug.assertTrue(results.contains("Bert"), "should return Bert.");
	}

	/**
	 * input: added t Ernie hasBrother Bert where: added t ?x hasBrother null
	 */
	@Test
	public final void testXRnull() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasBrother, null);
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
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
	 * input: added t Ernie hasBrother Bert where: added t2 ?x null Bert
	 */
	@Test
	public final void testXnullI() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID transactionID = store.begin();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, varX, null, "Bert");
		store.addChange(l1);
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);
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
	 * : test when transaction ids in input and where-clause are different
	 * input: removed t Ernie hasBrother Bert,removed t Ernie likes donuts
	 * where: removed t ?x hasBrother Bert, removed t ?x likes donuts
	 */
	@Test
	public final void testRetrievingHistoricalData() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		Variable varX = new Variable("x");
		GUID transactionID = store.begin();
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Ernie", likes,
				"Donuts");
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);
		transactionID = store.begin();
		transactionID = store.commit(transactionID);
		query = new Query(select, where, null, null, store);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, varX, likes, "Donuts");
		query.getWhere().add(w1);
		query.getWhere().add(w2);
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
	 * case34: input: LINK_REMOVED t Ernie hasBrother Bert,LINK_REMOVED t Ernie
	 * likes donuts where: LINK_NOW_DELETED t ?x hasBrother Bert,
	 * LINK_NOW_DELETED t ?x likes donuts
	 */
	@Test
	public final void testMatch34() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID transactionID = store.begin();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, transactionID, "Ernie", likes,
				"Donuts");
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_NOW_REMOVED, transactionID, varX, hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_NOW_REMOVED, transactionID, varX, likes,
				"Donuts");

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
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
	 * case34: input: LINK_ADDED t Ernie hasBrother Bert,LINK_ADDED t Ernie
	 * likes donuts where: LINK_NOW_ADDED t ?x hasBrother Bert, LINK_NOW_ADDED t
	 * ?x likes donuts
	 */
	@Test
	public final void testMatch35() {
		Query query;
		List<Variable> select;
		List<PDChange<GUID, Object, GUID>> where;

		GUID hasBrother = new GUID();
		GUID likes = new GUID();
		Variable varX = new Variable("x");
		select = new ArrayList<Variable>();
		where = new ArrayList<PDChange<GUID, Object, GUID>>();
		GUID transactionID = store.begin();

		PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, "Ernie", likes, "Donuts");
		store.addChange(l1);
		store.addChange(l2);
		transactionID = store.commit(transactionID);

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_NOW_ADDED, transactionID, varX, hasBrother,
				"Bert");
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_NOW_ADDED, transactionID, varX, likes, "Donuts");

		query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> result = assignmentIterator.next();
			results.add(result.get(varX));
		}
		Debug.assertTrue(results.contains("Ernie"), "should return Ernie.");
	}
}
