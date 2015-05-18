package pdstore.deduction;

import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.IteratorBasedCollection;

import org.junit.BeforeClass;
import org.junit.Test;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.deduction.ListenerRule;
import pdstore.deduction.RuleListener;
import pdstore.sparql.Assignment;
import pdstore.sparql.FilterChange;
import pdstore.sparql.IndexIteratorTest;
import pdstore.sparql.InsertionPseudoFilter;
import pdstore.sparql.Query;
import pdstore.sparql.Variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RuleTest {

    static PDStore store;

    @BeforeClass
    public static void setUpClass() {
        // create a brand new store (including the metamodel) to prevent
        // interference with earlier test runs
        String fileName = "MatchTest-" + System.nanoTime();
        store = new PDStore(fileName);
    }

    /**
     * A working test adapted from other test classes, used to develop
     * the real tests.
     */
    @Test
    public final void testXRI_YRI_Pseudo() {

        GUID role2 = new GUID();
        GUID transactionID = store.begin();
        GUID plus3 = new GUID();
        GUID plus6 = new GUID();


        PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, 2, plus3, 5);
        PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, 3, role2, 5);
        PDChange<GUID, Object, GUID> l3 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, 5, plus3, 8);
        store.addChange(l1);
        store.addChange(l2);
        store.addChange(l3);
        transactionID = store.commit(transactionID);

        List<Variable> select = new ArrayList<Variable>();
        List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

        Variable varZ = new Variable("z");
        Variable varX = new Variable("x");
        Variable varY = new Variable("y");

        PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
        PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, varY, plus3, varZ);
        FilterChange<GUID, Object, GUID> filter1 = new InsertionPseudoFilter<GUID, Object, GUID>(
                new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                        transactionID, varX, plus6, varZ), store
        );

        Query query = new ListenerRule(where, null, null, null, store, null);
        query.getWhere().add(w1);
        query.getWhere().add(w2);
        query.setFilter(filter1);
        Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
                .execute(null);
        IteratorBasedCollection<Assignment<GUID, Object, GUID>> result =
                new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);

        assertEquals(1, result.size());

        assertTrue(IndexIteratorTest.assertContains(varX, result, 2L));


        assertTrue(IndexIteratorTest.assertContains(varY, result, 5L));

        assertTrue(IndexIteratorTest.assertContains(varZ, result, 8L));

        // arbitrary countercheck:
        assertTrue(!IndexIteratorTest.assertContains(varX, result, 6L));
        // countercheck with other value from DB
        assertTrue(!IndexIteratorTest.assertContains(varX, result, 3L));

        store.commit();
        // assert about pseudofilters.
        //GUID t2 = store.begin();
        PDChange<GUID, Object, GUID> QueryInsertiontemplate = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, null, plus6, null);
        Collection<PDChange<GUID, Object, GUID>> result1;
        result1 = store.getChanges(QueryInsertiontemplate);
        assertTrue(result1.size() == 1);
        assertTrue(result1.iterator().next().getRole2().getFirst().equals(plus6.getFirst()));


    }

    /**
     * Test adapted so that the Rule sparql Query matches unique tuples
     */
    @Test
    public final void testXRI_YRI_RuleTemplate() {
        GUID role2 = new GUID().getFirst();
        GUID plus3 = new GUID().getFirst();
        GUID plus6 = new GUID().getFirst();

		GUID plus4 = new GUID().getFirst();
		GUID plus7 = new GUID().getFirst();
		
		
        GUID transactionID = store.begin();
 

        PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, 2, plus3, 5);
        PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, 3, role2, 5);
        PDChange<GUID, Object, GUID> l3 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, 5, plus3, 8);
        store.addChange(new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, transactionID, 5, plus4, 9));
        store.addChange(l1);
        store.addChange(l2);
        store.addChange(l3);
        transactionID = store.commit(transactionID);

        List<Variable> select = new ArrayList<Variable>();
        List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

        Variable varZ = new Variable("z");
        Variable varX = new Variable("x");
        Variable varY = new Variable("y");

        PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, null, varX, plus3, varY);
        PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, null, varY, plus4, varZ);
        FilterChange<GUID, Object, GUID> filter1 = new InsertionPseudoFilter<GUID, Object, GUID>(
                new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                        null, varX, plus7, varZ), store
        );

        Query query = new ListenerRule(where, null, null, null, store, null);
        query.getWhere().add(w1);
        query.getWhere().add(w2);
        query.setFilter(filter1);
        Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
                .execute(null);
        IteratorBasedCollection<Assignment<GUID, Object, GUID>> result =
                new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);

        assertEquals(1, result.size());

        assertTrue(IndexIteratorTest.assertContains(varX, result, 2L));


        assertTrue(IndexIteratorTest.assertContains(varY, result, 5L));

        assertTrue(IndexIteratorTest.assertContains(varZ, result, 9L));

        // arbitrary countercheck:
        assertTrue(!IndexIteratorTest.assertContains(varX, result, 6L));
        // countercheck with other value from DB
        assertTrue(!IndexIteratorTest.assertContains(varX, result, 3L));

        store.commit();
        // assert about pseudofilters.
        //GUID t2 = store.begin();
        PDChange<GUID, Object, GUID> QueryInsertiontemplate = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, null, plus7, null);
        Collection<PDChange<GUID, Object, GUID>> result1;
        result1 = store.getChanges(QueryInsertiontemplate);
        assertTrue(result1.size() == 1);
        assertTrue(result1.iterator().next().getRole2().getFirst().equals(plus7.getFirst()));


    }

    /**
     * Testing a first rule
     */
    @Test
    public final void testXRI_YRI_Rule() {

		Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		Debug.addDebugTopic(RuleListener.class.getName());
		
 		GUID role2 = new GUID("d68d74a0bfb411e3b25a93debacbbbbb").getFirst();
		GUID plus3 = new GUID("d68d74a0bfb411e3b25a93debacaaaaa").getFirst();
		GUID plus4 = new GUID("d68d74a0bfb411e3b25a93debacccccc").getFirst();
		GUID plus7 = new GUID("d68d74a0bfb411e3b25a93debacddddd").getFirst();
		
       store.setName(role2, "role2");

        store.setName(plus3, "plus3");

        store.setName(plus4, "plus4");

        store.setName(plus7, "plus7");

        ///////////////////////////////////////////
        List<Variable> select = new ArrayList<Variable>();
        List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
        Variable varZ = new Variable("z");
        Variable varX = new Variable("x");
        Variable varY = new Variable("y");

        PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, null, varX, plus3, varY);
        PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, null, varY, plus4, varZ);
        FilterChange<GUID, Object, GUID> filter1 = new InsertionPseudoFilter<GUID, Object, GUID>(
                new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                        null, varX, plus7, varZ), store
        );

        ListenerRule rule = new ListenerRule(where, null, filter1, store);
        rule.getWhere().add(w1);
        rule.getWhere().add(w2);
        //////////////////////////////////////

        rule.addTemplates(store.getListenerDispatcher());

        PDChange<GUID, Object, GUID> l1 = new PDChange<GUID, Object, GUID>(
        		ChangeType.LINK_ADDED, store.getCurrentTransaction(), 2, plus3, 5);
        store.addChange(l1);
        {
        	store.commit();
        	store.waitForDetachedListeners();
        	PDChange<GUID, Object, GUID> QueryInsertiontemplate = new PDChange<GUID, Object, GUID>(ChangeType.LINK_EFFECTIVE,
        			null, null, plus7, null);
        	Collection<PDChange<GUID, Object, GUID>> result1;
        	result1 = store.getChanges(QueryInsertiontemplate);
        	assertTrue(result1.size() == 0);
        }
        PDChange<GUID, Object, GUID> l2 = new PDChange<GUID, Object, GUID>(
        		ChangeType.LINK_ADDED, store.getCurrentTransaction(), 3, role2, 5);
        store.addChange(l2);
        {
        	store.commit();
        	store.waitForDetachedListeners();
        	Collection<PDChange<GUID, Object, GUID>> result1;
        	result1 = store.getChanges(new PDChange<GUID, Object, GUID>(ChangeType.LINK_EFFECTIVE,
        			null, null, plus7, null));
        	assertTrue(result1.size() == 0);
        }
        PDChange<GUID, Object, GUID> l3 = new PDChange<GUID, Object, GUID>(
        		ChangeType.LINK_ADDED, store.getCurrentTransaction(), 5, plus3, 8);
        store.addChange(l3);
        {
        	store.commit();
        	store.waitForDetachedListeners();
        	Collection<PDChange<GUID, Object, GUID>> result1;
        	result1 = store.getChanges(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
        			null, null, plus7, null));
        	assertTrue(result1.size() == 0);
        }

        store.addChange(new PDChange<GUID, Object, GUID>(
        		ChangeType.LINK_ADDED, store.getCurrentTransaction(), 5, plus4, 9));
        {
        	store.commit();
        	store.waitForDetachedListeners();
        	// The following is necessary to give asynchronous listener
        	// time to finish
        	try {
        		Thread.sleep(10);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        	store.commit();

        	Collection<PDChange<GUID, Object, GUID>> result1;
        	result1 = store.getChanges(new PDChange<GUID, Object, GUID>(ChangeType.LINK_EFFECTIVE,
        			null, null, plus7.getFirst(), null));
        	assertTrue(result1.size() >= 1);
        	assertTrue(result1.iterator().next().getRole2().getFirst().equals(plus7.getFirst()));
        }


    }
}
