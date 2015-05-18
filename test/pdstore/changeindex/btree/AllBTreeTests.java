package pdstore.changeindex.btree;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This runs the most common PDStore tests. Some notable exceptions:
 * GUIDLongRunningTest PDStoreStressTests, (takes some seconds) and
 * PDSToreServerTest (requires running server)
 * 
 * @author clut002
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BTreeNodeTest.class, CachingStrategyTest.class,
		BTreeIndexTest.class })
public class AllBTreeTests {
}
