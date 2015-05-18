package pdstore.changeindex.btree;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import nz.ac.auckland.se.genoupe.tools.Debug;

import junit.framework.TestCase;
import pdstore.Blob;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;

public class BTreeIndexTest extends TestCase {

	public final void testAddGetBlob() {
		// create brand new index file
		String fileName = "pddata/BTreeIndexTest-testAddGetBlob-"
				+ System.nanoTime() + ".idx";
		BTreeIndex index = new BTreeIndex(fileName, null);

		Blob blob111 = new Blob(new byte[] { 1, 1, 1 });
		Blob blob999 = new Blob(new byte[] { 9, 9, 9 });
		GUID roleA = new GUID();

		PDChange<GUID, Object, GUID> change = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, new GUID(), blob111, new GUID(), blob999);
		index.add(change);

		Iterator<PDChange<GUID, Object, GUID>> result = index.get(change);
		assertTrue(result.hasNext());
		assertEquals(change, result.next());
	}

	public final void testAddGet0000_4K_1MB() {
		addGetCheck(0, 4 * 1024, 1024 * 1024, LRUCachingStrategy.class);
	}

	public final void testAddGet0001_4K_1MB() {
		addGetCheck(1, 4 * 1024, 1024 * 1024, LRUCachingStrategy.class);
	}

	public final void testAddGet0100_4K_1MB() {
		addGetCheck(100, 4 * 1024, 1024 * 1024, LRUCachingStrategy.class);
	}

	public final void testAddGet1000_4K_1MB() {
		addGetCheck(1000, 4 * 1024, 1024 * 1024, LRUCachingStrategy.class);
	}

	public final void testAddGet10000_4K_1MB() {
		addGetCheck(10000, 4 * 1024, 1024 * 1024, LRUCachingStrategy.class);
	}

	public final void testAddGet0100_4K_4K() {
		addGetCheck(100, 4 * 1024, 32 * 1024, LRUCachingStrategy.class);
	}

	public final void testAddGet1000_4K_32K() {
		addGetCheck(1000, 4 * 1024, 32 * 1024, LRUCachingStrategy.class);
	}

	public final void addGetCheck(int numChanges, int blockSize, int cacheSize,
			Class<? extends CachingStrategyI> cachingStrategy) {
		// create brand new index file
		String fileName = "pddata/BTreeIndexTest-testAddGet" + numChanges + "-"
				+ System.nanoTime() + ".idx";
		BTreeIndex index;

		// Note: the ugly try-catch block is necessary because of the use of
		// reflection
		try {
			index = new BTreeIndex(fileName, blockSize, cacheSize,
					cachingStrategy.newInstance(), null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// generate ordered list of unique changes
		List<PDChange<GUID, Object, GUID>> changes = new ArrayList<PDChange<GUID, Object, GUID>>(
				100);
		for (int i = 0; i < numChanges; i++) {
			PDChange<GUID, Object, GUID> change = new PDChange<GUID, Object, GUID>(
					ChangeType.LINK_ADDED, new GUID(), new GUID(), new GUID(),
					new GUID());
			changes.add(change);
		}
		Collections.sort(changes);

		// add changes to index in random order
		Random rand = new Random();
		List<PDChange<GUID, Object, GUID>> tempChanges = new ArrayList<PDChange<GUID, Object, GUID>>(
				changes);
		for (int i = 0; i < numChanges; i++) {
			int randomIndex = rand.nextInt(tempChanges.size());
			PDChange<GUID, Object, GUID> change = tempChanges
					.remove(randomIndex);
			index.add(change);
		}

		Iterator<PDChange<GUID, Object, GUID>> result;

		// the following tests work only if the tree is non-empty
		if (!changes.isEmpty()) {
			// get() the first change.
			PDChange<GUID, Object, GUID> firstChange = changes.get(0);
			result = index.get(firstChange);
			assertTrue(result.hasNext());
			assertEquals(firstChange, result.next());

			// get() the median change.
			PDChange<GUID, Object, GUID> medianChange = changes
					.get(numChanges / 2);
			result = index.get(medianChange);
			assertTrue(result.hasNext());
			assertEquals(medianChange, result.next());

			// get() the last change.
			PDChange<GUID, Object, GUID> lastChange = changes
					.get(numChanges - 1);
			result = index.get(lastChange);
			assertTrue(result.hasNext());
			assertEquals(lastChange, result.next());
			assertFalse(result.hasNext());

			// insert some elements twice to check in the following that they
			// are not added and hence retrieved twice
			index.add(changes.get(0));
			index.add(changes.get(changes.size() / 2));
			index.add(changes.get(changes.size() - 1));
		}

		// get() with nullChange should return all changes (nullChange is the
		// smallest possible element).
		PDChange<GUID, Object, GUID> nullChange = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, null, null, null, null);
		result = index.get(nullChange);
		for (int i = 0; i < numChanges; i++) {
			assertTrue(result.hasNext());
			assertEquals(changes.get(i), result.next());
		}
		assertFalse(result.hasNext());

		/*
		 * get() with laterThanAnyChange should not return any element
		 * (laterThanAnyChange has GUIDs that are larger than all GUIDs in the
		 * index so far).
		 */
		PDChange<GUID, Object, GUID> laterThanAnyChange = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_ADDED, new GUID(), new GUID(), new GUID(),
				new GUID());
		result = index.get(laterThanAnyChange);
		assertFalse(result.hasNext());

		// check if the index is persistent by reloading and querying it
		index.flush();

		// Note: the ugly try-catch block is necessary because of the use of
		// reflection
		try {
			index = new BTreeIndex(fileName, blockSize, cacheSize,
					cachingStrategy.newInstance(), null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		result = index.get(nullChange);
		for (int i = 0; i < numChanges; i++) {
			assertTrue(result.hasNext());
			assertEquals(changes.get(i), result.next());
		}
		assertFalse(result.hasNext());
	}

}
