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

public class SparqlViewListenerRecursiveTest extends TestCase {

	// use local embedded store
	protected PDStore store;

	public void setUp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		Date date = new Date();

		// create new store without metamodel; give it a timestamp name
		store = new PDStore("ViewAsListenerTest-" + dateFormat.format(date));
	}

	public final void testGenericRelationInference() {
		GUID transactionID = store.begin();
		GUID isFriendsWith = new GUID();
		GUID isEnemiesWith = new GUID();
		GUID hasSomeRelationTo = new GUID();
		store.addLink("Jill", isFriendsWith, "Karen");
		store.addLink("Gerry", isEnemiesWith, "Tom");

		Variable varX = SparqlViewListener.INSTANCE1VAR2;
		Variable varY = SparqlViewListener.INSTANCE2VAR2;
		Variable varZ = SparqlViewListener.ROLE2VAR2;

		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, varX, varZ, varY);
		where.add(w1);
		Query query = new Query(select, where, null, null, store);
		query.setFilter(new AndExpression( new NotEqualExpression(varZ, hasSomeRelationTo), new NotEqualExpression(varZ, hasSomeRelationTo.getPartner()) ));

		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, null, hasSomeRelationTo, null);
		SparqlViewListener listener = new SparqlViewListener(query,
				changeTemplate);
		listener.addMyself(store.getViewDispatcher());

		Iterator<PDChange<GUID, Object, GUID>> assignmentIterator = (store
				.getChanges(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_ADDED, null, null, hasSomeRelationTo,
						null))).iterator();

		assertTrue(assignmentIterator.hasNext());

	}

	public final void testComparativeIntegerWithVariable() {
		/*
		 * Incorrect code, how to compare whether the ages from two distinct
		 * transactions?
		 */

		// adding data for testing
		GUID transactionID = store.begin();
		GUID hasAge = new GUID();
		GUID isOlderThan = new GUID();
		store.addLink(transactionID, "John", hasAge, 25);
		store.addLink(transactionID, "David", hasAge, 19);
		store.addLink(transactionID, "Helen", hasAge, 34);
		store.commit(transactionID);

		// Setting up variables for query
		Variable personOne = SparqlViewListener.INSTANCE1VAR2;
		Variable ageOne = SparqlViewListener.INSTANCE2VAR2;
		Variable personTwo = SparqlViewListener.INSTANCE1VAR2;
		Variable ageTwo = SparqlViewListener.INSTANCE2VAR2;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, personOne, hasAge, ageOne);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, personTwo, hasAge, ageTwo);
		where.add(w1);
		where.add(w2);
		Query query = new Query(select, where, null, null, store);
		query.setFilter(new GreaterThanExpression(ageOne, ageTwo));

		// assertTrue(query.execute(null).hasNext());

		// setting up changeTemplate for SparqlViewListener
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, null, isOlderThan, null);
		SparqlViewListener listener = new SparqlViewListener(query,
				changeTemplate);
		listener.addMyself(store.getViewDispatcher());

		// Testing ViewListener
		Iterator<PDChange<GUID, Object, GUID>> assignmentIterator = (store
				.getChanges(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_ADDED, null, null, isOlderThan, "Helen")))
				.iterator();
		assertTrue(assignmentIterator.hasNext());
		PDChange<GUID, Object, GUID> result = assignmentIterator.next();
		assertTrue(result.getInstance1().equals("David"));
		assertTrue(!assignmentIterator.hasNext());

	}

}
