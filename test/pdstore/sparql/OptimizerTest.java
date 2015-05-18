package pdstore.sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import nz.ac.auckland.se.genoupe.tools.Stopwatch;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.PDStoreI;
import pdstore.sparql.Query.OptimimzerType;

public class OptimizerTest extends TestCase {

	static PDStore store;

	private Variable varA;
	private Variable varB;
	private Variable varC;
	private Variable varD;
	private Variable varE;
	private Variable varF;
	private Variable varG;
	private Variable varH;
	private Variable varI;
	private Variable varJ;
	private Variable varK;
	private Variable varL;
	private Variable varM;
	private Variable varN;
	private Variable varO;
	private Variable varP;
	private Variable varQ;
	private Variable varR;
	private Variable varS;
	private Variable varT;
	// Start
	private Variable varT1;
	private Variable varT2;
	private Variable varT3;
	private Variable varT4;
	private Variable varT5;
	private Variable varT6;
	private Variable varT7;
	private Variable varT8;
	private Variable varT9;
	private Variable varT10;
	private Variable varT11;
	private Variable varT12;
	private Variable varT13;
	private Variable varT14;
	private Variable varT15;
	private Variable varT16;
	// End
	private Variable varU;
	private Variable varV;
	private Variable varW;
	private Variable varX;
	private Variable varY;
	private Variable varZ;

	private GUID likes;
	private GUID livesIn;
	private GUID isMarriedTo;

	@Before
	public void setUp() throws Exception {

		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "QueryOptimizerTest-" + System.nanoTime();
		store = new PDStore(fileName);

		varA = new Variable("a");
		varB = new Variable("b");
		varC = new Variable("C");
		varD = new Variable("d");
		varE = new Variable("e");
		varF = new Variable("f");
		varG = new Variable("g");
		varH = new Variable("h");
		varI = new Variable("i");
		varJ = new Variable("j");
		varK = new Variable("k");
		varL = new Variable("l");
		varM = new Variable("m");
		varN = new Variable("n");
		varO = new Variable("o");
		varP = new Variable("p");
		varQ = new Variable("q");
		varR = new Variable("r");
		varS = new Variable("s");
		varT = new Variable("t");
		varU = new Variable("u");
		varV = new Variable("v");
		varW = new Variable("w");
		varX = new Variable("x");
		varY = new Variable("y");
		varZ = new Variable("Z");

		varT1 = new Variable("t1");
		varT2 = new Variable("t2");
		varT3 = new Variable("t3");
		varT4 = new Variable("t4");
		varT5 = new Variable("t5");
		varT6 = new Variable("t6");
		varT7 = new Variable("t7");
		varT8 = new Variable("t8");
		varT9 = new Variable("t9");
		varT10 = new Variable("t10");
		varT11 = new Variable("t11");
		varT12 = new Variable("t12");
		varT13 = new Variable("t13");
		varT14 = new Variable("t14");
		varT15 = new Variable("t15");
		varT16 = new Variable("t16");

		// Creating a sample dataset
		likes = new GUID();
		livesIn = new GUID();
		isMarriedTo = new GUID();

		GUID transactionID = store.begin();

		store.addLink(transactionID, "Pete", isMarriedTo, "Katie");

		store.addLink(transactionID, "John", livesIn, "Auckland");
		store.addLink(transactionID, "Jack", livesIn, "London");
		store.addLink(transactionID, "Bob", livesIn, "Tokyo");

		store.addLink(transactionID, "John", likes, "Apple");
		store.addLink(transactionID, "John", likes, "Orange");
		store.addLink(transactionID, "John", likes, "Pear");
		store.addLink(transactionID, "John", likes, "Berry");

		store.addLink(transactionID, "Pete", likes, "Pizza");
		store.addLink(transactionID, "Pete", likes, "Apple");
		store.addLink(transactionID, "Pete", likes, "Orange");
		store.addLink(transactionID, "Pete", likes, "Pear");
		store.addLink(transactionID, "Pete", likes, "Berry");

		store.addLink(transactionID, "Jack", likes, "Orange");
		store.addLink(transactionID, "Jack", likes, "Pear");
		store.addLink(transactionID, "Jack", likes, "Berry");

		store.addLink(transactionID, "Bob", likes, "Orange");
		store.addLink(transactionID, "Bob", likes, "Pear");
		store.addLink(transactionID, "Bob", likes, "Berry");

		transactionID = store.commit(transactionID);

	}

	@After
	public void tearDown() throws Exception {
	}

	public final void test_EstimateFilterStats() {
		// TODO:later
	}

	public final void test_optimizer_buildStats() {
		Query query;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, likes, varY);

		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, isMarriedTo, varZ);

		where.add(w2);
		where.add(w1);

		query = new Query(select, where, null, null, store);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
		}
		query.canBuildStatistics = true;
		query.buildQueryStatistics(store);

		Collection<PDChange<GUID, Object, GUID>> likescount = store.getChanges(
				likes, varX);
		Collection<PDChange<GUID, Object, GUID>> isMarriedTocount = store
				.getChanges(isMarriedTo, varX);

		assertNotNull(likescount);
		assertNotNull(isMarriedTocount);
	}

	public final void test_optimizer_LSO() {
		Query query = null;
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, "Pete", likes, varY);

		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, varT, varX, isMarriedTo, varZ);

		where.add(w1);
		where.add(w2);
		// where.add(w2);

		query = new Query(select, where, null, null, store);
		query.currentOptimizerType = OptimimzerType.LSO;

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
		}
		query.canBuildStatistics = true;
		query.buildQueryStatistics(store);

		// Re-Run the query (this time we have stats build)

		Query query1 = new Query(select, where, null, null, store);
		query1.currentOptimizerType = OptimimzerType.LSO;

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator1 = query1
				.execute(null);

		while (assignmentIterator1.hasNext()) {
			assignmentIterator1.next();
		}
		query.canBuildStatistics = true;
		query.buildQueryStatistics(store);

		// shows that pattern has been re-arranged based on LSO
		assertTrue(query.where.get(1).equals(w2));

	}

}
