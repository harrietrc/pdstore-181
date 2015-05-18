package pdstore.deduction;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.junit.BeforeClass;
import org.junit.Test;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.sparql.InsertionPseudoFilter;
import pointsto.PointsToRules;

public class PointsToTest {

    static PDStore store;

    @BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "MatchTest-" + System.nanoTime();
		store = new PDStore(fileName);
		
		Debug.addDebugTopic(RuleListener.class.getName()+"basic");
		PointsToRules.addRules(store);
	}

    /**
	 * Testing the alloc rule.
	 */
	@Test
	public final void test1() {

		// h1: v1 = new C();
		GUID h1 = new GUID();
		store.setName(h1, "h1");
		GUID v1 = new GUID();
		store.setName(v1, "v1");
		store.addLink(v1, PointsToRules.ALLOC, h1);
		store.commit();

		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();
		store.commit();

		Collection<Object> result1 = store.getInstances(v1, PointsToRules.POINTS_TO);
		assertTrue(result1.size() == 1);
		assertTrue(result1.contains(h1));
	}

	/**
	 * Testing a the Store/Load Pair rule.
	 * Testing a the assign rule.
	 */
	@Test
	public final void testStoreLoad() {

		Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		Debug.addDebugTopic(RuleListener.class.getName());

		GUID hTarget = new GUID();
		store.setName(hTarget, "hTarget1");
		GUID field = new GUID();
		store.setName(field, "field1");
		GUID store_op = new GUID();
		store.setName(store_op, "store_op1");
		GUID load_op = new GUID();
		store.setName(load_op, "load_op1");
		GUID hTmp = new GUID();
		store.setName(hTmp, "hTmp1");
		GUID vStoreTmp = new GUID();
		store.setName(vStoreTmp, "vStoreTmp1");
		GUID vStoreTarget = new GUID();
		store.setName(vStoreTarget, "vStoreTarget1");
		GUID vLoadTmp = new GUID();
		store.setName(vLoadTmp, "vLoadTmp1");
		GUID vLoadTarget = new GUID();
		store.setName(vLoadTarget, "vLoadTarget1");

		store.addLink(store_op, PointsToRules.STORE_LEFT, vStoreTmp);
		store.addLink(store_op, PointsToRules.STORE_RIGHT, vStoreTarget);
		store.addLink(store_op, PointsToRules.STORE_FIELD, field);

		store.addLink(load_op, PointsToRules.LOAD_LEFT, vLoadTarget);
		store.addLink(load_op, PointsToRules.LOAD_RIGHT, vLoadTmp);
		store.addLink(load_op, PointsToRules.LOAD_FIELD, field);

		store.addLink(vLoadTmp, PointsToRules.POINTS_TO, hTmp);
		store.addLink(vStoreTmp, PointsToRules.POINTS_TO, hTmp);
		store.addLink(vStoreTarget, PointsToRules.POINTS_TO, hTarget);

		store.commit();

		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();

		store.commit();
		Collection<Object> result1 = store.getInstances(vLoadTarget, PointsToRules.POINTS_TO);
		assertTrue(result1.size() == 1);
		assertTrue(result1.contains(hTarget));
	}

	/**
	 * Testing the assign rule.
	 */
	@Test
	public final void test2() {

		Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		Debug.addDebugTopic(RuleListener.class.getName());

		// h1: v1 = new C();
		GUID h1 = new GUID();
		store.setName(h1, "h1");
		GUID v1 = new GUID();
		store.setName(v1, "v1");
		store.addLink(v1, PointsToRules.ALLOC, h1);

		// v2 = v1;
		GUID v2 = new GUID();
		store.setName(v2, "v2");
		store.addLink(v2, PointsToRules.ASSIGN, v1);

		store.commit();

		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();

		store.commit();
		Collection<Object> result1 = store.getInstances(v1, PointsToRules.POINTS_TO);
		assertTrue(result1.size() == 1);
		assertTrue(result1.contains(h1));

		Collection<Object> result2 = store.getInstances(v2, PointsToRules.POINTS_TO);
		System.out.println(result2);
		assertTrue(result2.size() >= 1);
		assertTrue(result2.contains(h1));
	}

	/**
	 * Testing a the Store/Load Pair rule.
	 * Testing a the assign rule.
	 */
	@Test
	public final void testStoreLoadOrder2() {
	
		Debug.addDebugTopic(InsertionPseudoFilter.DEBUG_TOPIC);
		//Debug.addDebugTopic(RuleListener.class.getName());
	
		GUID hTarget = new GUID();
		store.setName(hTarget, "hTarget1");
		GUID field = new GUID();
		store.setName(field, "field1");
		GUID store_op = new GUID();
		store.setName(store_op, "store_op1");
		GUID load_op = new GUID();
		store.setName(load_op, "load_op1");
		GUID hTmp = new GUID();
		store.setName(hTmp, "hTmp1");
		GUID vStoreTmp = new GUID();
		store.setName(vStoreTmp, "vStoreTmp1");
		GUID vStoreTarget = new GUID();
		store.setName(vStoreTarget, "vStoreTarget1");
		GUID vLoadTmp = new GUID();
		store.setName(vLoadTmp, "vLoadTmp1");
		GUID vLoadTarget = new GUID();
		store.setName(vLoadTarget, "vLoadTarget1");
	
		store.addLink(store_op, PointsToRules.STORE_LEFT, vStoreTmp);
		store.addLink(store_op, PointsToRules.STORE_RIGHT, vStoreTarget);
		store.addLink(store_op, PointsToRules.STORE_FIELD, field);
	
		store.addLink(vLoadTmp, PointsToRules.POINTS_TO, hTmp);
		store.addLink(vStoreTmp, PointsToRules.POINTS_TO, hTmp);
		store.addLink(vStoreTarget, PointsToRules.POINTS_TO, hTarget);
		store.commit();
	
		store.addLink(load_op, PointsToRules.LOAD_LEFT, vLoadTarget);
		store.addLink(load_op, PointsToRules.LOAD_RIGHT, vLoadTmp);
		store.commit();
		store.waitForDetachedListeners();
		
		store.addLink(load_op, PointsToRules.LOAD_FIELD, field);
	
		store.commit();
	
		// The following is necessary to give asynchronous listener time to finish
		store.waitForDetachedListeners();
	
		store.commit();
		Collection<Object> result1 = store.getInstances(vLoadTarget, PointsToRules.POINTS_TO);
		assertTrue(result1.size() == 1);
		assertTrue(result1.contains(hTarget));
	}

}
