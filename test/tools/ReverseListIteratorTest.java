package tools;

import java.util.Arrays;

import junit.framework.TestCase;
import nz.ac.auckland.se.genoupe.tools.ReverseListIterator;

public class ReverseListIteratorTest extends TestCase {

	public void testEmptyList() {
		String[] input1 = { };

		ReverseListIterator<String> concatIterator = new ReverseListIterator<String>(
				Arrays.asList(input1));

		assertFalse(concatIterator.hasNext());
	}
	
	public void test1() {
		String[] input1 = { "1" };

		ReverseListIterator<String> concatIterator = new ReverseListIterator<String>(
				Arrays.asList(input1));

		assertTrue(concatIterator.hasNext());
		assertEquals("1", concatIterator.next());
		assertFalse(concatIterator.hasNext());
	}
	
	public void test2() {
		String[] input1 = { "1", "2", "3" };

		ReverseListIterator<String> concatIterator = new ReverseListIterator<String>(
				Arrays.asList(input1));

		assertTrue(concatIterator.hasNext());
		assertEquals("3", concatIterator.next());
		assertTrue(concatIterator.hasNext());
		assertEquals("2", concatIterator.next());
		assertTrue(concatIterator.hasNext());
		assertEquals("1", concatIterator.next());
		assertFalse(concatIterator.hasNext());
	}
}
