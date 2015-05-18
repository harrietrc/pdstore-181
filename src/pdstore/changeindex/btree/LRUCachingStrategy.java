package pdstore.changeindex.btree;

import nz.ac.auckland.se.genoupe.datastructures.Treap;

/**
 * Least-Recently Used caching strategy. A treap is used to efficiently manage
 * file block indices and last-access times.
 * 
 * @author christof
 * 
 */
public class LRUCachingStrategy implements CachingStrategyI {

	Treap<Integer, Object, Long> treap = new Treap<Integer, Object, Long>();

	@Override
	public void blockAccessed(int fileBlockIndex) {
		// update timestamp of fileBlockIndex in the treap
		treap.remove(fileBlockIndex);
		long time = System.nanoTime();
		treap.put(fileBlockIndex, null, time);
	}

	@Override
	public int selectBlockToReplace() {
		// get oldest fileBlockIndex in cache (min timestamp) and remove it
		int fileBlockIndex = treap.getKeyWithMinPriority();
		treap.remove(fileBlockIndex);
		return fileBlockIndex;
	}

}
