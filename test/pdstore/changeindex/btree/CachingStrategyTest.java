package pdstore.changeindex.btree;

import junit.framework.TestCase;

public class CachingStrategyTest extends TestCase {

	public final void testLRU() {
		CachingStrategyI cs = new LRUCachingStrategy();
		cs.blockAccessed(1);
		assertEquals(1, cs.selectBlockToReplace());

		cs.blockAccessed(2);
		assertEquals(2, cs.selectBlockToReplace());

		cs.blockAccessed(1);
		cs.blockAccessed(2);
		cs.blockAccessed(3);
		assertEquals(1, cs.selectBlockToReplace());

		cs.blockAccessed(1);
		assertEquals(2, cs.selectBlockToReplace());
		assertEquals(3, cs.selectBlockToReplace());
		assertEquals(1, cs.selectBlockToReplace());
	}
}
