package pdstore.sparql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.parallel.AbstractForEach;
import nz.ac.auckland.se.genoupe.parallel.Applicable;
import nz.ac.auckland.se.genoupe.parallel.SequentialForEach;
import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.IteratorBasedCollection;

import org.junit.BeforeClass;
import org.junit.Test;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import tools.SuffixAdd;

public class IndexIteratorTest {

	static PDStore store;

	static GUID plus3;
	static GUID plus6;
	static GUID transactionID;

	public static final Object PSEUDO_FILTER = "pseudofilter";

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "BranchTest-" + System.nanoTime();
		store = new PDStore(fileName);

		// add some data
		transactionID = store.begin();
		plus3 = new GUID();
		plus6 = new GUID();
		store.addLink(transactionID, 2L, plus3, 5L);
		store.addLink(transactionID, 3L, plus3, 6L);
		store.addLink(transactionID, 4L, plus3, 7L);
		store.addLink(transactionID, 5L, plus3, 8L);
		store.addLink(transactionID, 2L, plus6, 8L);
		transactionID = store.commit(transactionID);
	}
	
	
	public static boolean assertContains(final Variable varX,
			IteratorBasedCollection<Assignment<GUID, Object, GUID>> result, long value) {
     	boolean contains = false;
		for (Assignment<GUID, Object, GUID> element: result) {

	 		  contains = contains || element.get(varX).equals(value);			
	   }
		return contains;
	}

	
	

	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, added ?y plus3 ?z
	 */
	@Test
	public final void testXRX_XRX() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, plus3, varZ);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		assertEquals(1, result.size());
		
     	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(!assertContains(varX, result, 3L));
	   	
	   	assertTrue(assertContains(varY, result, 5L));
	   	
	   	assertTrue(assertContains(varZ, result, 8L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}

	/**
	 * test when exponent is 1 and is (?s, role, ?o)
	 * 
	 * where: * added t ?x plus3 ?y
	 */
	@Test
	public final void testXRX() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		assertEquals(4, result.size());
		
     	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(assertContains(varX, result, 4L));
	   	assertTrue(assertContains(varX, result, 3L));
	   	assertTrue(assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}

	/**
	 * test (subject, role, _)
	 * 
	 * 
	 * where: * added t 2 plus3 ?y, added 3 plus3 6
	 */
	@Test
	public final void testIRX_IRI() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 2L, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, 3L, plus3, 6L);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(1, result.size());
			
     	assertTrue(!assertContains(varY, result, 2L));
	   	assertTrue(!assertContains(varY, result, 4L));
	   	assertTrue(!assertContains(varY, result, 3L));
	   	assertTrue(assertContains(varY, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varY, result, 6L));
	}

	/**
	 * test : (?s, role, object)
	 * 
	 * where: * added t ?x plus3 7, added ?z plus6 8
	 */
	@Test
	public final void testXRI_XRI() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, 7L);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varZ, plus6, 8L);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(1, result.size());
			
     	assertTrue(assertContains(varZ, result, 2L));
     	
	   	assertTrue(assertContains(varX, result, 4L));
	   	
	   	assertTrue(!assertContains(varX, result, 3L));
	   	assertTrue(!assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}

	/**
	 * test (?s1, role1, ?o1), (?s1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, added ?x plus3 6
	 */
	@Test
	public final void testXRX_XRI() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, 6L);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(1, result.size());
			
     	assertTrue(!assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(assertContains(varX, result, 3L));
	   	assertTrue(!assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}

	/**
	 * test (?s1, role1, ?o1), (_, _, ?s1)
	 * 
	 * where: * added t ?x plus3 ?y, added ?z plus3 ?x
	 */
	@Test
	public final void testXRX_XRX1() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varZ, plus3, varX);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(1, result.size());
		
	   	assertTrue(!assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(!assertContains(varX, result, 3L));
	   	assertTrue(assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}

	/**
	 * test (?s1, role1, ?o1), (_, _, ?o1)
	 * 
	 * where: * added t ?x plus3 ?y, added ?z plus6 ?y
	 */
	@Test
	public final void testXRX_XRX2() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varZ, plus6, varY);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(1, result.size());
		
	   	assertTrue(!assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(!assertContains(varX, result, 3L));
	   	assertTrue(assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}

	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, added ?y plus3 ?z, filter: added t ?x plus3
	 * ?y
	 */
	@Test
	public final void testXRX_XRX_F1() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, plus3, varZ);
		FilterChange<GUID, Object, GUID> filter1 = new FilterChange<GUID, Object, GUID>(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
						transactionID, varX, plus3, varY), store, true);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(filter1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(1, result.size());
		
	   	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(!assertContains(varX, result, 3L));
	   	assertTrue(!assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}
	
	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, added ?y plus3 ?z, filter: added t ?x plus3 null
	 */
	@Test
	public final void testXRX_XRX_F1null() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, plus3, varZ);
		FilterChange<GUID, Object, GUID> filter1 = new FilterChange<GUID, Object, GUID>(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
						transactionID, varX, plus3, null), store, true);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(filter1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(1, result.size());
		
	   	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(!assertContains(varX, result, 3L));
	   	assertTrue(!assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}
	
	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, filter: added t ?x plus3 ?y
	 */
	@Test
	public final void testXRX_F1() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		final Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		FilterChange<GUID, Object, GUID> filter1 = new FilterChange<GUID, Object, GUID>(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
						transactionID, varX, plus3, varY), store, true);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(filter1);
		
		
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(4, result.size());
		
	   	assertTrue(assertContains(varX, result, 4L));
	   	assertTrue(assertContains(varX, result, 3L));
	   	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));

	}
	
	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, filter: added t ?x plus3 null
	 */
	@Test
	public final void testXRX_F1null() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		final Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		FilterChange<GUID, Object, GUID> filter1 = new FilterChange<GUID, Object, GUID>(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
						transactionID, varX, plus3, null //  difference to previous testXRX_F1
						), store, true);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(filter1);
		
		
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(4, result.size());
		
	   	assertTrue(assertContains(varX, result, 4L));
	   	assertTrue(assertContains(varX, result, 3L));
	   	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(assertContains(varX, result, 5L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));

	}
	
	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, filter: NOT added t ?x plus6 null
	 */
	@Test
	public final void testXRX_notF1null() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		final Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		FilterChange<GUID, Object, GUID> filter1 = new FilterChange<GUID, Object, GUID>(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
						transactionID, varX, plus6 //  difference to testXRX_F1null
						, null), store, 
						false  //  difference to testXRX_F1null
						);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.setFilter(filter1);
		
		
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);

		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		
		assertEquals(3, result.size());
		
	   	assertTrue(assertContains(varX, result, 4L));
	   	assertTrue(assertContains(varX, result, 3L));
	   	assertTrue(assertContains(varX, result, 5L));
	   	
	   	// filtered out:
	   	assertTrue(!assertContains(varX, result, 2L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));

	}
 

	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, added ?y plus3 ?z, filter: added t ?x plus6
	 * ?z
	 */
	@Test
	public final void testXRX_XRX_F2() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, plus3, varZ);
		FilterChange<GUID, Object, GUID> filter1 = new FilterChange<GUID, Object, GUID>(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
						transactionID, varX, plus6, varZ), store, true);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(filter1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		assertEquals(1, result.size());
		
     	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(!assertContains(varX, result, 3L));
	   	
	   	assertTrue(assertContains(varY, result, 5L));
	   	
	   	assertTrue(assertContains(varZ, result, 8L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));
	}

	/**
	 * test (?s1, role1, ?o1), (?o1, _, _)
	 * 
	 * where: * added t ?x plus3 ?y, added ?y plus3 ?z, filter: added t ?x plus3
	 * ?z
	 */
	@Test
	public final void testXRX_XRX_F4() {
		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, plus3, varZ);
		FilterChange<GUID, Object, GUID> filter1 = new FilterChange<GUID, Object, GUID>(
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
						transactionID, varX, plus3, varZ), store, true);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(filter1);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		assertEquals(0, result.size());
		
	}

	/**
	 * test (?s1, role1, ?o1), (?o1, _, _) where: * added t ?x plus3 ?y, added
	 * ?y plus3 ?z, pseudofilter: added t ?x reallyPlus6 ?z
	 */
	@Test
	public final void testXRX_XRX_PseudoF2() {
		subtestXRX_XRX_PseudoF2(true);
	}

	/**
	 * test (?s1, role1, ?o1), (?o1, _, _) where: * added t ?x plus3 ?y, added
	 * ?y plus3 ?z, pseudofilter: added t ?x reallyPlus6 ?z
	 */
	@Test
	public final void testXRX_XRX_PseudoF2nullTransact() {
		subtestXRX_XRX_PseudoF2(false);
	}
	
	/**
	 * test (?s1, role1, ?o1), (?o1, _, _) where: * added t ?x plus3 ?y, added
	 * ?y plus3 ?z, pseudofilter: added t ?x reallyPlus6 ?z
	 */
	public final void subtestXRX_XRX_PseudoF2(boolean usetransaction) {
		Debug.addDebugTopic(PSEUDO_FILTER);
	   	store.commit();
		 GUID reallyPlus6 = new GUID();


		List<Variable> select = new ArrayList<Variable>();
		List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();

		Variable varZ = new Variable("z");
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");

		PDChange<GUID, Object, GUID> w1 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varX, plus3, varY);
		PDChange<GUID, Object, GUID> w2 = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, transactionID, varY, plus3, varZ);
		PDChange<GUID, Object, GUID> insertiontemplate = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				transactionID, varX, reallyPlus6, varZ);
		InsertionPseudoFilter<GUID, Object, GUID> filter1 = new InsertionPseudoFilter<GUID, Object, GUID>(
				insertiontemplate, store);

		Query query = new Query(select, where, null, null, store);
		query.getWhere().add(w1);
		query.getWhere().add(w2);
		query.setFilter(filter1);
		filter1.setInsertTransaction(usetransaction?store.getCurrentTransaction():null);
		Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = query
				.execute(null);
		IteratorBasedCollection<Assignment<GUID, Object, GUID>> result = 
				new IteratorBasedCollection<Assignment<GUID, Object, GUID>>(assignmentIterator);
		assertEquals(1, result.size());
		
     	assertTrue(assertContains(varX, result, 2L));
	   	assertTrue(!assertContains(varX, result, 4L));
	   	assertTrue(!assertContains(varX, result, 3L));
	   	
	   	assertTrue(assertContains(varY, result, 5L));
	   	
	   	assertTrue(assertContains(varZ, result, 8L));
	   	
	   	// arbitrary countercheck:
	   	assertTrue(!assertContains(varX, result, 6L));

	   	store.commit();
		// assert about pseudofilters. 
		//GUID t2 = store.begin();
		PDChange<GUID, Object, GUID> QueryInsertiontemplate = new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, null, reallyPlus6, null);
		Collection<PDChange<GUID, Object, GUID>> result1;
		result1 = store.getChanges(QueryInsertiontemplate);
		assertTrue(result1.size()==1);

		//store.commit(t2);
		
		
		Debug.removeDebugTopic(PSEUDO_FILTER);
	}
}
