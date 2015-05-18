package pdstore.changeindex.btree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;

public class BTreeIndexStressTest extends TestCase {

	public final void test1000_4K_128MB() {
		addGetCheck(1000, 4 * 1024, 128 * 1024 * 1024, LRUCachingStrategy.class);
	}

	public final void test10000_4K_128MB() {
		addGetCheck(10000, 4 * 1024, 128 * 1024 * 1024,
				LRUCachingStrategy.class);
	}

	public final void test100000_4K_128MB() {
		addGetCheck(100000, 4 * 1024, 128 * 1024 * 1024,
				LRUCachingStrategy.class);
	}

	public final void addGetCheck(int n, int blockSize, int cacheSize,
			Class<? extends CachingStrategyI> cachingStrategy) {
		// create brand new index file
		String fileName = "pddata/BTreeIndexStressTest-" + n + "-"
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

		// generate ordered list of n unique changes
		List<PDChange<GUID, Object, GUID>> changes = new ArrayList<PDChange<GUID, Object, GUID>>(
				100);
		for (int i = 0; i < n; i++) {
			PDChange<GUID, Object, GUID> change = new PDChange<GUID, Object, GUID>(
					ChangeType.LINK_ADDED, new GUID(), new GUID(), new GUID(),
					new GUID());
			changes.add(change);
		}
		Collections.sort(changes);

		// add the n changes to the index in pseudo-random order (with seed to
		// make test repeatable)
		Random rand = new Random(0);
		List<PDChange<GUID, Object, GUID>> tempChanges = new ArrayList<PDChange<GUID, Object, GUID>>(
				changes);
		for (int i = 0; i < n; i++) {
			int randomIndex = rand.nextInt(tempChanges.size());
			PDChange<GUID, Object, GUID> change = tempChanges
					.remove(randomIndex);
			index.add(change);
		}

		// run n pseudo-random queries
		for (int i = 0; i < n; i++) {
			Iterator<PDChange<GUID, Object, GUID>> result;
			PDChange<GUID, Object, GUID> change = changes.get(rand.nextInt(n));
			result = index.get(change);
			// get the first 10 changes in the result set
			for (int k = 0; k < 10; k++) {
				if (result.hasNext())
					result.next();
			}
		}
	}
}
