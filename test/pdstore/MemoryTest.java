package pdstore;

import junit.framework.TestCase;

public class MemoryTest extends TestCase {

	PDStore getFreshStore() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "MemoryTest-" + System.nanoTime();
		PDStore store = new PDStore(fileName);
		return store;
	}

	public void testIRI1c() {

		for (int i = 0; i < 100; i++) {
			System.err.println(i);
			PDStore store = getFreshStore();
		}
	}
}
