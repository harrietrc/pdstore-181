package pdstore.sparql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;

public class SparqlViewListenerTest extends TestCase {

	// use local embedded store
	protected PDStore store;

	public void setUp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		Date date = new Date();

		// create new store without metamodel; give it a timestamp name
		store = new PDStore("ViewAsListenerTest-" + dateFormat.format(date));
	}

	public final void testFixedIntegerWithConstant() {

		GUID transactionID = store.begin();
		GUID hasIncome = new GUID();
		GUID isRicherThan = new GUID();
		store.addLink(transactionID, "Bill", hasIncome, 50);
		store.addLink(transactionID, "Bob", hasIncome, 150);
		store.commit(transactionID);
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		Variable varX = SparqlViewListener.INSTANCE1VAR2;
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, varX, hasIncome, 150);
		where.add(w1);

		Query query = new Query(select, where, null, null, store);
		// PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID,
		// Object, GUID>(ChangeType.LINK_ADDED, null, null, isRicherThan, null);
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, null, isRicherThan, "Bill");
		SparqlViewListener listener = new SparqlViewListener(query,
				changeTemplate);
		listener.addMyself(store.getViewDispatcher());

		Iterator<PDChange<GUID, Object, GUID>> assignmentIterator = (store
				.getChanges(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_ADDED, null, null, isRicherThan, "Bill")))
				.iterator();

		/*
		 * select = new ArrayList<Variable>(); where = new
		 * ArrayList<PDChange<GUID, Object, GUID>>(); Query secondQuery = new
		 * Query(select, where, null, null, store); Variable varY = new
		 * Variable("Y"); Variable varZ = new Variable("Z"); w1 = new
		 * PDChange<GUID, Object, GUID>( ChangeType.LINK_ADDED, null, varY,
		 * isRicherThan, "Bill"); secondQuery.where.add(w1);
		 * Iterator<ResultElement<GUID, Object, GUID>> assignmentIterator =
		 * secondQuery.execute(null);
		 */

		assertTrue(assignmentIterator.hasNext());
		PDChange<GUID, Object, GUID> result = assignmentIterator.next();
		assertTrue(result.getInstance1().equals("Bob"));
		assertTrue(!assignmentIterator.hasNext());
		// store.rollback(transactionID);

	}

	public final void testFixedIntegerWithVariables() {

		GUID transactionID = store.begin();
		GUID hasIncome = new GUID();
		GUID isRicherThan = new GUID();
		store.addLink(transactionID, "Bill", hasIncome, 50);
		store.addLink(transactionID, "Bob", hasIncome, 150);
		store.commit(transactionID);
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		Variable varX = SparqlViewListener.INSTANCE1VAR2;
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, varX, hasIncome, 150);
		where.add(w1);

		Query query = new Query(select, where, null, null, store);
		// PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID,
		// Object, GUID>(ChangeType.LINK_ADDED, null, null, isRicherThan, null);
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, null, isRicherThan, "Bill");
		SparqlViewListener listener = new SparqlViewListener(query,
				changeTemplate);
		listener.addMyself(store.getViewDispatcher());

		Collection<PDChange<GUID, Object, GUID>> assignmentIterator = store
				.getChanges(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_ADDED, null, null, isRicherThan, null));

		/*
		 * select = new ArrayList<Variable>(); where = new
		 * ArrayList<PDChange<GUID, Object, GUID>>(); Query secondQuery = new
		 * Query(select, where, null, null, store); Variable varY = new
		 * Variable("Y"); Variable varZ = new Variable("Z"); w1 = new
		 * PDChange<GUID, Object, GUID>( ChangeType.LINK_ADDED, null, varY,
		 * isRicherThan, "Bill"); secondQuery.where.add(w1);
		 * Iterator<ResultElement<GUID, Object, GUID>> assignmentIterator =
		 * secondQuery.execute(null);
		 */

		assertTrue(assignmentIterator.size() > 0);
		assertTrue(assignmentIterator.size() == 1);
		Iterator<PDChange<GUID, Object, GUID>> iterator = assignmentIterator
				.iterator();
		PDChange<GUID, Object, GUID> result = iterator.next();
		assertTrue(result.getInstance1().equals("Bob"));
		assertTrue(!iterator.hasNext());

	}

	public final void testInstance1RoleListener() {

		Variable varX = SparqlViewListener.INSTANCE1VAR2;
		Variable varY = SparqlViewListener.INSTANCE2VAR2;
		Variable varZ = SparqlViewListener.ROLE2VAR2;

		// Copied from testXRY_XRZ_withIncomingAssignment()
		// copying is necessary to set up the right local variables.
		// TODO: look into some reuse method, might require
		// using fields, or even subtyping of Test classes
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
				ChangeType.LINK_ADDED, transactionID, varX, likes, "donuts");
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
		Debug.assertTrue(results.get(2) == "donuts", "should return donuts.");
		Debug.assertTrue(results.size() == 3,
				"the results should have size of 3.");
		// ///////////

		final GUID role2Id = new GUID();
		Object instance1 = "Ernie";
		GUID instance2 = null;
		// PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID,
		// Object, GUID>(null, null, instance1var, role2Id, null);
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, null, role2Id, null);

		SparqlViewListener listener = new SparqlViewListener(query,
				changeTemplate);
		listener.addMyself(store.getViewDispatcher());
		// old: exactly wrong?
		// store.getViewDispatcher().addListener(listener, changeTemplate);

		GUID transaction = store.begin();
		PDChange<GUID, Object, GUID> changeTemplate2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transaction, instance1, role2Id,
				instance2);
		Collection<PDChange<GUID, Object, GUID>> result = store
				.getChanges(changeTemplate2);
		assertTrue(!result.isEmpty());
		PDChange<GUID, Object, GUID> firstResultElement = result.iterator()
				.next();
		assertTrue(firstResultElement.getInstance1().equals("Ernie"));
		assertTrue(firstResultElement.getInstance2().equals("Bert"));

		// TODO: the following assert fails, but should succeed:
		// Since before there was a resultelement with instance2=Bert.
		// This testcase probably hints at an error in addMyself().
		changeTemplate2.setInstance2("Bert");
		result = store.getChanges(changeTemplate2);
		assertTrue(!result.isEmpty());

		// alternative take on same test:
		PDChange<GUID, Object, GUID> changeTemplate3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transaction, instance1, role2Id, "Bert");
		Collection<PDChange<GUID, Object, GUID>> result3 = store
				.getChanges(changeTemplate3);
		assertTrue(!result3.isEmpty());

	}

}
