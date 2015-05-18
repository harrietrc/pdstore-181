package pdstore.sparql;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;

public class OptionalTest {

	// GUID at 11:30am 27, Aug 0b0837c5b16711dfa6b3c34768ca8db4

	PDStore store = new PDStore("PDStoreSPARQLTest" + Math.random());
	GUID hasName, hasAge, phoneNo, likes, livesin, akl, hml;
	private Variable varZ;
	private Variable varX;
	private Variable varY;
	private Variable varT;
	private Variable varA;
	private Variable varB;
	private Variable varC;

	@Before
	public void setUp() throws Exception {
		hasName = new GUID("9c1d831183ef11dfbce200217029671e");
		hasAge = new GUID("9c1d831183ef11dfbce200217029671f");
		phoneNo = new GUID("6c1d831083ef11dfbce200297029675e");
		livesin = new GUID("6c1d831083ef11dfbce200217029675f");
		likes = new GUID("6c1d831083ef11dfbce200217029675e");
		akl = new GUID("6c1d831083ef11dfbce200217349675f");
		hml = new GUID("9c1d831183ef11dfbce200217029637f");
		varZ = new Variable("z");
		varX = new Variable("x");
		varY = new Variable("y");
		varT = new Variable("t");
		varA = new Variable("a");
		varB = new Variable("b");
		varC = new Variable("c");
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * WHERE ?x likes ?y
	 * 		 ?y livesin "akl". OPTIONAL { ?x hasAge ?z }
	 */
	@Test
	public final void test_optional1() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<Query> lstoptional = new ArrayList<Query>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", likes, "P2");
		store.addLink(transactionID, "p1", likes, "P3");
		store.addLink(transactionID, "p2", likes, "P3");
		store.addLink(transactionID, "P2", livesin, akl);
		store.addLink(transactionID, "P3", livesin, akl);
		store.addLink(transactionID, "p1", hasAge, 25);

		transactionID = store.commit(transactionID);
		// case 1
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, likes, varY);// varX,
																			// likes,
																			// varY);

		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, livesin, akl);

		Query query1 = new Query(select, where, null, lstoptional, store);
		where.add(w1);
		where.add(w2);
		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);

		Query optional1 = new Query(select, optionalWhere, null, null, store);
		optionalWhere.add(ow1);
		lstoptional.add(optional1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(3, count);
	}

	/**
	 * WHERE ?x hasName ?y.
	 * 		 ?x hasAge ?z. 
	 * OPTIONAL ?x livesin ?a
	 * OPTIONAL ?a likes ?b.
	 */
	@Test
	public final void testMultipleoptionaltest() {
		// add data
		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p2", hasName, "Sarah Jones");
		store.addLink(transactionID, "p3", hasName, "John Smith");
		store.addLink(transactionID, "p4", hasName, "Matt Jones");
		store.addLink(transactionID, "p1", hasAge, 23);
		store.addLink(transactionID, "p2", hasAge, 25);
		store.addLink(transactionID, "p1", livesin, "akl");
		store.addLink(transactionID, "p1", livesin, "hml");
		store.addLink(transactionID, "akl", likes, "hml");
		transactionID = store.commit(transactionID);

		// create main query
		// add WHERE
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		where.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				transactionID, varX, hasName, varY));
		where.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				transactionID, varX, hasAge, varZ));

		// add OPTIONAL 1
		List<Query> lstoptional = new ArrayList<Query>();
		List<PDChange<GUID, Object, GUID>> optionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		optionalWhere1.add(new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, livesin, varA));
		lstoptional.add(new Query(null, optionalWhere1, null, null, store));

		// add OPTIONAL 2
		List<PDChange<GUID, Object, GUID>> optionalWhere2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		optionalWhere2.add(new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varA, likes, varB));
		lstoptional.add(new Query(null, optionalWhere2, null, null, store));
		Query query1 = new Query(null, where, null, lstoptional, store);

		// execute query
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> assignment = assignmentIterator
					.next();
			System.out.println(assignment);
			count++;
		}
		assertEquals(3, count);
	}

	/**
	 * WHERE ?x hasName ?y
	 * OPTIONAL { ?x livesin ?a }
	 * OPTIONAL { ?x likes ?b }
	 * OPTIONAL { ?x phoneNo ?c }
	 */
	@Test
	public final void testMultipleoptionaltest2() {
		// add data
		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p1", phoneNo, "123456");
		store.addLink(transactionID, "p1", livesin, "akl");
		store.addLink(transactionID, "p1", likes, "Tea");
		store.addLink(transactionID, "p1", likes, "Coffee");
		store.addLink(transactionID, "p1", likes, "Beer");
		transactionID = store.commit(transactionID);

		// create main query
		// add WHERE
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		where.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				transactionID, varX, hasName, varY));
		
		// add OPTIONAL 1
		List<Query> lstoptional = new ArrayList<Query>();
		List<PDChange<GUID, Object, GUID>> optionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		optionalWhere1.add(new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, livesin, varA));
		lstoptional.add(new Query(null, optionalWhere1, null, null, store));

		// add OPTIONAL 2
		List<PDChange<GUID, Object, GUID>> optionalWhere2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		optionalWhere2.add(new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, likes, varB));
		lstoptional.add(new Query(null, optionalWhere2, null, null, store));
		
		// add OPTIONAL 3
		List<PDChange<GUID, Object, GUID>> optionalWhere3 = new ArrayList<PDChange<GUID, Object, GUID>>();
		optionalWhere3.add(new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, phoneNo, varC));
		lstoptional.add(new Query(null, optionalWhere3, null, null, store));
	
		
		Query query1 = new Query(null, where, null, lstoptional, store);

		// execute query
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		List<Object> results = new ArrayList<Object>();
		while (assignmentIterator.hasNext()) {
			Assignment<GUID, Object, GUID> assignment = assignmentIterator
					.next();
			results.add(assignment.get(varB));
			count++;
		}
		assertEquals(3, count);
		results.contains("Tea");
		results.contains("Coffee");
		results.contains("Beer");
	}

	/**
	 * WHERE ?x hasName ?y.
	 * OPTIONAL {?x hasAge  ?z. OPTIONAL { ?x livesin ?a. OPTIONAL { ?x likes ?c }}}
	 * OPTIONAL { ?x phoneNo ?b }
	 */
	@Test
	public final void testMultiNestedOptional() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> nestedOptionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> nestedOptionalWhere11 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<Query> lstoptional = new ArrayList<Query>();
		List<Query> lstNestedoptional = new ArrayList<Query>();
		List<Query> lstNestedoptional1 = new ArrayList<Query>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p2", hasName, "Sarah Jones");
		store.addLink(transactionID, "p3", hasName, "John Smith");
		store.addLink(transactionID, "p4", hasName, "Matt Jones");

		store.addLink(transactionID, "p4", hasAge, 24);
		store.addLink(transactionID, "p3", hasAge, 23);
		store.addLink(transactionID, "p1", hasAge, 21);
		store.addLink(transactionID, "p3", livesin, "akl");
		store.addLink(transactionID, "p4", livesin, "hml");
		store.addLink(transactionID, "p4", livesin, "Wellington");
		store.addLink(transactionID, "p4", likes, 4);
		store.addLink(transactionID, "p4", likes, 44);
		store.addLink(transactionID, "p3", likes, 3);

		store.addLink(transactionID, "p1", phoneNo, "1111");
		store.addLink(transactionID, "p2", phoneNo, "2222");
		store.addLink(transactionID, "p2", phoneNo, "220022");
		store.addLink(transactionID, "p4", phoneNo, "4444");
		store.addLink(transactionID, "p4", phoneNo, "440044");

		transactionID = store.commit(transactionID);
		// case 1
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasName, varY);
		Query query1 = new Query(select, where, null, lstoptional, store);
		where.add(w1);

		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);

		PDChange<GUID, Object, GUID> ow2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, livesin, varA);

		PDChange<GUID, Object, GUID> ow3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, phoneNo, varB);

		PDChange<GUID, Object, GUID> ow4 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, likes, varC);

		Query optional1 = new Query(select, optionalWhere1, null,
				lstNestedoptional, store);
		optionalWhere1.add(ow1);
		lstoptional.add(optional1);

		Query nestedOptional1 = new Query(select, nestedOptionalWhere1, null,
				lstNestedoptional1, store);
		nestedOptionalWhere1.add(ow2);
		lstNestedoptional.add(nestedOptional1);

		Query nestedOptional11 = new Query(select, nestedOptionalWhere11, null,
				null, store);
		nestedOptionalWhere11.add(ow4);
		lstNestedoptional1.add(nestedOptional11);

		Query optional2 = new Query(select, optionalWhere2, null, null, store);
		optionalWhere2.add(ow3);
		lstoptional.add(optional2);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(12, count);

	}

	/**
	 * WHERE ?x hasName ?y.
	 * OPTIONAL {?x hasAge  ?z. OPTIONAL { ?x livesin ?a}}
	 * OPTIONAL { ?x phoneNo ?b }
	 */
	@Test
	public final void test_NestedOptional_1() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> nestedOptionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<Query> lstoptional = new ArrayList<Query>();
		List<Query> lstNestedoptional = new ArrayList<Query>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p2", hasName, "Sarah Jones");
		store.addLink(transactionID, "p3", hasName, "John Smith");
		store.addLink(transactionID, "p4", hasName, "Matt Jones");

		store.addLink(transactionID, "p4", hasAge, 24);
		store.addLink(transactionID, "p3", hasAge, 23);
		store.addLink(transactionID, "p1", hasAge, 21);
		store.addLink(transactionID, "p3", livesin, "akl");
		store.addLink(transactionID, "p4", livesin, "hml");
		store.addLink(transactionID, "p4", livesin, "Wellington");

		store.addLink(transactionID, "p1", phoneNo, "1111");
		store.addLink(transactionID, "p2", phoneNo, "2222");
		store.addLink(transactionID, "p2", phoneNo, "220022");
		store.addLink(transactionID, "p4", phoneNo, "4444");
		store.addLink(transactionID, "p4", phoneNo, "440044");

		transactionID = store.commit(transactionID);
		// case 1
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasName, varY);
		Query query1 = new Query(select, where, null, lstoptional, store);
		where.add(w1);

		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);

		PDChange<GUID, Object, GUID> ow2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, livesin, varA);

		PDChange<GUID, Object, GUID> ow3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, phoneNo, varB);

		Query optional1 = new Query(select, optionalWhere1, null,
				lstNestedoptional, store);
		optionalWhere1.add(ow1);
		lstoptional.add(optional1);

		Query nestedOptional1 = new Query(select, nestedOptionalWhere1, null,
				null, store);
		nestedOptionalWhere1.add(ow2);
		lstNestedoptional.add(nestedOptional1);

		Query optional2 = new Query(select, optionalWhere2, null, null, store);
		optionalWhere2.add(ow3);
		lstoptional.add(optional2);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(8, count);

	}

	/**
	 * WHERE ?x hasName ?y.
	 * OPTIONAL {?x hasAge  ?z. OPTIONAL { ?x livesin ?a}}
	 * OPTIONAL { ?x phoneNo ?b }
	 */
	@Test
	public final void test_NestedOptiona_2() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> nestedOptionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<Query> lstoptional = new ArrayList<Query>();
		List<Query> lstNestedoptional = new ArrayList<Query>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p2", hasName, "Sarah Jones");
		store.addLink(transactionID, "p3", hasName, "John Smith");
		store.addLink(transactionID, "p4", hasName, "Matt Jones");

		store.addLink(transactionID, "p4", hasAge, 24);
		store.addLink(transactionID, "p3", hasAge, 23);
		store.addLink(transactionID, "p1", hasAge, 21);
		store.addLink(transactionID, "p3", livesin, "akl");
		store.addLink(transactionID, "p4", livesin, "hml");

		store.addLink(transactionID, "p1", phoneNo, "1111");
		store.addLink(transactionID, "p2", phoneNo, "2222");
		store.addLink(transactionID, "p4", phoneNo, "4444");

		transactionID = store.commit(transactionID);
		// case 1
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasName, varY);
		Query query1 = new Query(select, where, null, lstoptional, store);
		where.add(w1);

		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);

		PDChange<GUID, Object, GUID> ow2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, livesin, varA);

		PDChange<GUID, Object, GUID> ow3 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, phoneNo, varB);

		Query optional1 = new Query(select, optionalWhere1, null,
				lstNestedoptional, store);
		optionalWhere1.add(ow1);
		lstoptional.add(optional1);

		Query nestedOptional1 = new Query(select, nestedOptionalWhere1, null,
				null, store);
		nestedOptionalWhere1.add(ow2);
		lstNestedoptional.add(nestedOptional1);

		Query optional2 = new Query(select, optionalWhere2, null, null, store);
		optionalWhere2.add(ow3);
		lstoptional.add(optional2);

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);

	}

	@Test
	public final void testOptional2() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<Query> lstoptional = new ArrayList<Query>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p2", hasName, "Sarah Jones");
		store.addLink(transactionID, "p3", hasName, "John Smith");
		store.addLink(transactionID, "p4", hasName, "Matt Jones");
		store.addLink(transactionID, "p4", hasAge, 23);
		store.addLink(transactionID, "p3", hasAge, 25);
		store.addLink(transactionID, "p1", hasAge, 21);
		store.addLink(transactionID, "p3", livesin, "akl");
		store.addLink(transactionID, "p4", livesin, "akl");

		transactionID = store.commit(transactionID);
		// case 1
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasName, varY);
		Query query1 = new Query(select, where, null, lstoptional, store);
		where.add(w1);
		// query1.filter = new EqualExpression(varX, "p1");
		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);
		// where.add(ow1);

		PDChange<GUID, Object, GUID> ow2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, livesin, varA);
		/*
		 * //query1.where.add(ow1); query1.where.add(ow2);
		 */

		Query optional1 = new Query(select, optionalWhere1, null, null, store);
		optionalWhere1.add(ow1);
		optionalWhere1.add(ow2);
		// optional1.filter= new EqualExpression(varZ, 25);
		// optionalWhere1.add(ow2);
		lstoptional.add(optional1);
		/*
		 * Query optional2 = new Query(select, optionalWhere2, null, null,
		 * store); optionalWhere2.add(ow3); lstoptional.add(optional2);
		 */

		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);
	}

	@Test
	public final void testOptionalTest1() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<Query> lstoptional = new ArrayList<Query>();
		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p2", hasName, "Sarah Jones");
		store.addLink(transactionID, "p3", hasName, "John Smith");
		store.addLink(transactionID, "p4", hasName, "Matt Jones");
		store.addLink(transactionID, "p1", hasAge, 23);
		store.addLink(transactionID, "p3", hasAge, 25);

		transactionID = store.commit(transactionID);
		// case 1
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasName, varY);
		Query query1 = new Query(select, where, null, lstoptional, store);
		where.add(w1);

		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);

		Query optional1 = new Query(select, optionalWhere, null, null, store);
		optionalWhere.add(ow1);
		lstoptional.add(optional1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);

		// case2
		List<PDChange<GUID, Object, GUID>> where2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		lstoptional.clear();
		Query query2 = new Query(select, where2, null, lstoptional, store);
		where2.add(w1);
		where2.add(ow1);
		assignmentIterator = query2.execute(null);
		count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(2, count);

		// case3
		List<PDChange<GUID, Object, GUID>> where3 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere3 = new ArrayList<PDChange<GUID, Object, GUID>>();
		lstoptional.clear();
		FilterExpression fe3 = null;
		Query query3 = new Query(select, where3, null, lstoptional, store);
		where3.add(w1);
		Query optional3 = new Query(select, optionalWhere3, fe3, null, store);
		optionalWhere3.add(ow1);
		fe3 = new GreaterThanExpression(varZ, 24);
		lstoptional.add(optional3);
		assignmentIterator = query3.execute(null);
		count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);

		// case4
		List<PDChange<GUID, Object, GUID>> where4 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere4 = new ArrayList<PDChange<GUID, Object, GUID>>();
		lstoptional.clear();
		FilterExpression fe4 = null;
		Query query4 = new Query(select, where4, fe4, lstoptional, store);
		where4.add(w1);
		Query optional4 = new Query(select, optionalWhere4, null, null, store);
		optionalWhere4.add(ow1);
		fe4 = new OrExpression(new GreaterThanExpression(varZ, 24),
				new UnBoundVariable(varZ));
		lstoptional.add(optional4);
		assignmentIterator = query4.execute(null);
		count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);

	}

	/**
	 * case5 WHERE ?x hasName ?y. OPTIONAL { ?x hasAge ?z , ?x phoneNo ?t }
	 * OPTIONAL { }
	 * 
	 * case6 WHERE ?x hasName ?y. OPTIONAL { ?x hasAge ?z } OPTIONAL { ?x
	 * phoneNo ?t }
	 * 
	 * case7 WHERE ?x hasName ?y. OPTIONAL { ?x hasAge ?z } OPTIONAL { ?x
	 * phoneNo ?t } FILTER (?t < 24)
	 */
	@Test
	public final void testOptionalTest2() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where5 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere5 = new ArrayList<PDChange<GUID, Object, GUID>>();

		List<Query> lstoptional = new ArrayList<Query>();

		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", phoneNo, 12345);
		store.addLink(transactionID, "p1", hasName, "Becky Smith");
		store.addLink(transactionID, "p2", hasName, "Sarah Jones");
		store.addLink(transactionID, "p3", hasName, "John Smith");
		store.addLink(transactionID, "p4", hasName, "Matt Jones");
		store.addLink(transactionID, "p1", hasAge, 23);
		store.addLink(transactionID, "p3", hasAge, 25);

		transactionID = store.commit(transactionID);
		// case 5
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasName, varY);
		Query query5 = new Query(select, where5, null, lstoptional, store);
		where5.add(w1);

		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);
		PDChange<GUID, Object, GUID> ow2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, phoneNo, varT);
		Query optional5 = new Query(select, optionalWhere5, null, null, store);
		optionalWhere5.add(ow1);
		optionalWhere5.add(ow2);
		lstoptional.add(optional5);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query5
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);

		System.out.println("Case5:");

		// case6
		lstoptional.clear();
		List<PDChange<GUID, Object, GUID>> where6 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere6 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere6_1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		Query query6 = new Query(select, where6, null, lstoptional, store);
		Query optional6 = new Query(select, optionalWhere6, null, null, store);
		Query optional6_1 = new Query(select, optionalWhere6_1, null, null,
				store);
		where6.add(w1);
		optionalWhere6.add(ow1);
		optionalWhere6_1.add(ow2);

		lstoptional.add(optional6);
		lstoptional.add(optional6_1);
		assignmentIterator = query6.execute(null);
		count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);

		// case7
		FilterExpression fe7 = null;
		lstoptional.clear();
		List<PDChange<GUID, Object, GUID>> where7 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere7 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere7_1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		Query query7 = new Query(select, where7, fe7, lstoptional, store);
		fe7 = new OrExpression(new LessThanExpression(varT, 24),
				new UnBoundVariable(varT));
		Query optional7 = new Query(select, optionalWhere7, null, null, store);
		Query optional7_1 = new Query(select, optionalWhere7_1, null, null,
				store);
		where7.add(w1);
		optionalWhere7.add(ow1);
		optionalWhere7_1.add(ow2);

		lstoptional.add(optional7);
		lstoptional.add(optional7_1);
		assignmentIterator = query7.execute(null);
		count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(4, count);

	}

	/**
	 * case1 WHERE ?x hasName ?y. OPTIONAL { ?x hasAge ?z }
	 * 
	 * case2 WHERE ?x hasName ?y. ?x hasAge ?z
	 * 
	 * case3 WHERE ?x hasName ?y. OPTIONAL { ?x hasAge ?z . FILTER (?Z > 24) }
	 * 
	 * case4 WHERE ?x hasName ?y. OPTIONAL { ?x hasAge ?z } FILTER (?Z > 24 ||
	 * !BoundVariable(?z))
	 */
	@Test
	public final void test_crosssubjectObject_filter() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> optionalWhere = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<Query> lstoptional = new ArrayList<Query>();
		GUID transactionID = store.begin();
		store.addLink(transactionID, "p1", likes, "P2");
		store.addLink(transactionID, "p1", likes, "P3");
		// store.addLink(transactionID, "p2", likes, "P3");
		store.addLink(transactionID, "p2", livesin, akl);
		store.addLink(transactionID, "p3", livesin, hml);

		transactionID = store.commit(transactionID);
		// case 1
		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, likes, varY);

		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, livesin, akl);

		Query query1 = new Query(select, where, null, lstoptional, store);
		where.add(w1);
		where.add(w2);
		PDChange<GUID, Object, GUID> ow1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, hasAge, varZ);

		Query optional1 = new Query(select, optionalWhere, null, null, store);
		optionalWhere.add(ow1);
		// query1.optionals.add(optional1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query1
				.execute(null);
		int count = 0;
		while (assignmentIterator.hasNext()) {
			assignmentIterator.next();
			count++;
		}
		assertEquals(0, count);
	}
}
