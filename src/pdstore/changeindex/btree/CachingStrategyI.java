package pdstore.changeindex.btree;

/**
 * Describes a cache page replacement algorithm that selects a block in memory
 * for replacement with a new block, in case of low memory.
 * 
 * @author christof
 * 
 */
public interface CachingStrategyI {

	/**
	 * Tells the caching strategy that the block with the given file block index
	 * has been accessed. Usually this makes it less likely that the block will
	 * be selected for replacement.
	 * 
	 * @param fileBlockIndex
	 */
	void blockAccessed(int fileBlockIndex);

	/**
	 * Selects a block in memory that can be replaced with another block.
	 * 
	 * @return the block file index of the block to be replaced
	 */
	int selectBlockToReplace();
}
