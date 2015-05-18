package pdstore;

import java.util.Iterator;

import junit.framework.TestCase;

import pdstore.generic.GenericLinkedList;

public class GenericLinkedListTest extends TestCase {

	public PDStore store = new PDStore("GenericLinkedListTest");

	public final void testAddRemove() {
		GUID parent = new GUID();
		GUID collectionRole = new GUID();
		GUID nextRole = new GUID();

		GenericLinkedList<GUID, Object, GUID, String> list = new GenericLinkedList<GUID, Object, GUID, String>(
				store, parent, collectionRole, nextRole);
		assertTrue(list.isEmpty());

		list.add("1");
		assertEquals(1, list.size());
		assertEquals("1", list.get(0));

		list.add("2");
		assertEquals(list.size(), 2);
		assertEquals("1", list.get(0));
		assertEquals("2", list.get(1));

		list.add("3");
		assertEquals(list.size(), 3);
		assertEquals("1", list.get(0));
		assertEquals("2", list.get(1));
		assertEquals("3", list.get(2));

		list.add("4");
		assertEquals(list.size(), 4);
		assertEquals("1", list.get(0));
		assertEquals("2", list.get(1));
		assertEquals("3", list.get(2));
		assertEquals("4", list.get(3));

		// remove first
		list.remove("1");
		assertEquals(list.size(), 3);
		assertEquals("2", list.get(0));
		assertEquals("3", list.get(1));
		assertEquals("4", list.get(2));

		// remove middle
		list.remove("3");
		assertEquals(list.size(), 2);
		assertEquals("2", list.get(0));
		assertEquals("4", list.get(1));

		// remove last
		list.remove("4");
		assertEquals(list.size(), 1);
		assertEquals("2", list.get(0));

		// remove remaining one
		list.remove("2");
		assertTrue(list.isEmpty());
	}

	public final void testIterator() {
		GUID parent = new GUID();
		GUID collectionRole = new GUID();
		GUID nextRole = new GUID();

		GenericLinkedList<GUID, Object, GUID, String> list = new GenericLinkedList<GUID, Object, GUID, String>(
				store, parent, collectionRole, nextRole);
		assertTrue(list.isEmpty());

		list.add("1");
		Iterator<String> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("1", iterator.next());
		assertFalse(iterator.hasNext());

		list.add("2");
		iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("1", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("2", iterator.next());
		assertFalse(iterator.hasNext());

		list.add("3");
		iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("1", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("2", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("3", iterator.next());
		assertFalse(iterator.hasNext());
	}

	/*
	 * // add same object again try { list.add(newOperation); fail(); } catch
	 * (Exception e) { assertEquals(list.size(), 6);
	 * assertEquals(list.get(5).getId(), newOperation.getId()); } }
	 * 
	 * public final void testListAddAtIndex() { PDOperation newOperation1 = new
	 * PDOperation(store); PDOperation newOperation2 = new PDOperation(store);
	 * PDOperation newOperation3 = new PDOperation(store); PDOperation
	 * newOperation4 = new PDOperation(store);
	 * 
	 * // Add to the front of the linked list try { list.add(0, newOperation1);
	 * assertEquals(list.size(), 6); assertEquals(list.get(0).getId(),
	 * newOperation1.getId()); } catch (Exception e) { fail(); }
	 * 
	 * // Add to the middle of the linked list try { list.add(2, newOperation2);
	 * assertEquals(list.size(), 7); assertEquals(list.get(2).getId(),
	 * newOperation2.getId()); } catch (Exception e) { fail(); }
	 * 
	 * // Add to the end of the linked list try { list.add(list.size(),
	 * newOperation3); assertEquals(list.size(), 8);
	 * assertEquals(list.get(7).getId(), newOperation3.getId()); } catch
	 * (Exception e) { fail(); }
	 * 
	 * // Should throw an exception try { list.add(4, newOperation1); // Add the
	 * same element twice fail(); } catch (Exception e) { } // TODO: use the
	 * specific exception
	 * 
	 * try { list.add(-1, newOperation4); fail(); } catch (Exception e) { }
	 * 
	 * try { list.add(20, newOperation4); fail(); } catch (Exception e) { }
	 * 
	 * try { list.add(3, null); fail(); } catch (Exception e) { }
	 * 
	 * }
	 * 
	 * public final void testSort() { // Test that the list is sorted and the
	 * get method work assertEquals(list.get(0).getId(), op1.getId());
	 * assertEquals(list.get(1).getId(), op2.getId());
	 * assertEquals(list.get(2).getId(), op3.getId());
	 * assertEquals(list.get(3).getId(), op4.getId());
	 * assertEquals(list.get(4).getId(), op5.getId()); }
	 * 
	 * public final void testRemoveByIndex() { assertFalse(list.isEmpty());
	 * 
	 * // Remove a middle element list.remove(1); assertEquals(list.size(), 4);
	 * assertEquals(list.get(0).getId(), op1.getId());
	 * assertEquals(list.get(1).getId(), op3.getId());
	 * assertEquals(list.get(2).getId(), op4.getId());
	 * assertEquals(list.get(3).getId(), op5.getId());
	 * 
	 * // Remove the first element list.remove(0); assertEquals(list.size(), 3);
	 * assertEquals(list.get(0).getId(), op3.getId());
	 * assertEquals(list.get(1).getId(), op4.getId());
	 * assertEquals(list.get(2).getId(), op5.getId());
	 * 
	 * // Remove the last element list.remove(2); assertEquals(list.size(), 2);
	 * assertEquals(list.get(0).getId(), op3.getId());
	 * assertEquals(list.get(1).getId(), op4.getId());
	 * 
	 * // Remove all remaining elements list.remove(0); list.remove(0);
	 * assertEquals(list.size(), 0); assertTrue(list.isEmpty());
	 * 
	 * // Remove invalid indexes // Throws exception // list.remove(-1); //
	 * list.remove(10); }
	 * 
	 * public final void testRemoveByElement() { assertFalse(list.isEmpty());
	 * 
	 * // Remove a middle element list.remove(op2); assertEquals(list.size(),
	 * 4); assertEquals(list.get(0).getId(), op1.getId());
	 * assertEquals(list.get(1).getId(), op3.getId());
	 * assertEquals(list.get(2).getId(), op4.getId());
	 * assertEquals(list.get(3).getId(), op5.getId());
	 * 
	 * // Remove the first element list.remove(op1); assertEquals(list.size(),
	 * 3); assertEquals(list.get(0).getId(), op3.getId());
	 * assertEquals(list.get(1).getId(), op4.getId());
	 * assertEquals(list.get(2).getId(), op5.getId());
	 * 
	 * // Remove the last element list.remove(op5); assertEquals(list.size(),
	 * 2); assertEquals(list.get(0).getId(), op3.getId());
	 * assertEquals(list.get(1).getId(), op4.getId());
	 * 
	 * // Remove all remaining elements list.remove(op3); list.remove(op4);
	 * assertEquals(list.size(), 0); assertTrue(list.isEmpty());
	 * 
	 * // Remove invalid elements // Throws exception // list.remove(null); //
	 * TODO what happens if you try removing something thats not in the // list?
	 * }
	 * 
	 * public final void testContains() { assertTrue(list.contains(op4));
	 * 
	 * list.remove(op4); assertFalse(list.contains(op4)); }
	 * 
	 * public final void testGet() { assertEquals(list.get(0).getId(),
	 * op1.getId());
	 * 
	 * try { list.get(10); fail(); } catch (Exception e) { } }
	 */
}
