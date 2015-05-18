package pdstore.changeindex.btree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nz.ac.auckland.se.genoupe.tools.Debug;

import pdstore.GUID;
import pdstore.PDStoreException;

/**
 * A block cache for a given file. The two main methods are loadBlock() and
 * writeBlock(). At the moment blocks are only loaded but never removed. More
 * sophisticated caching strategies (least-frequently used etc) could be
 * implemented later.
 * 
 * Note that this class also contains B-tree specific functionality (in the
 * header), although most of it would be useful also for other PDStore-related
 * implementations.
 * 
 * @author Christof
 * 
 */
public class FileCache {

	/**
	 * Identification GUID at beginning of index file.
	 */
	public static final GUID VERSION_GUID = new GUID(
			"4b7be9002df711e2b8f4c4850827f10a");

	/**
	 * Size of one block in bytes.
	 */
	public final int BLOCK_SIZE;
	private static final int DEFAULT_BLOCK_SIZE = 4 * 1024;

	/**
	 * Size of the in-memory block cache, as number of bytes.
	 */
	public final int CACHE_SIZE_BYTES;
	private static final int DEFAULT_CACHE_SIZE = 128 * 1024 * 1024;

	/**
	 * Size of the in-memory block cache, as number of blocks. Computed from the
	 * cache size in bytes. Some bytes in the cache will be unused since the
	 * division will leave a remainder.
	 */
	public final int CACHE_SIZE_BLOCKS;

	/**
	 * In-memory block cache.
	 */
	ByteBuffer buffer;

	/**
	 * Maps on-disk block numbers to in-memory cache block numbers.
	 */
	private Map<Integer, Integer> cacheMap = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * Lists cache indices of all free cache blocks.
	 */
	List<Integer> freeList = new ArrayList<Integer>();

	/**
	 * Contains file block indices of all dirty cache blocks, i.e. cache blocks
	 * that have been modified and need to be written back to the file.
	 */
	Set<Integer> dirtySet = new HashSet<Integer>();

	/**
	 * File to cache.
	 */
	RandomAccessFile file;

	/**
	 * The file size in blocks.
	 */
	long fileSizeInBlocks;

	/**
	 * The ID of the repository represented in the cached file.
	 */
	GUID repositoryID;

	/**
	 * The index of the root block of the B-tree in the cached file.
	 */
	int rootBlockIndex;

	/**
	 * Buffer array for reading blocks from file.
	 */
	byte[] blockBuffer;

	/**
	 * Strategy that will be used to replace blocks in the cache if memory runs
	 * low.
	 */
	CachingStrategyI cachingStrategy;
	
	/**
	 * Returns the number of blocks in the cached file.
	 * 
	 * @return
	 */
	public int getFileSizeInBlocks() {
		return (int) fileSizeInBlocks;
	}

	public FileCache(String fileName) {
		this(fileName, DEFAULT_BLOCK_SIZE, DEFAULT_CACHE_SIZE,
				new LRUCachingStrategy());
	}

	public FileCache(String fileName, int blockSize, int cacheSizeInBytes,
			CachingStrategyI cachingStrategy) {

		BLOCK_SIZE = blockSize;
		CACHE_SIZE_BYTES = cacheSizeInBytes;
		CACHE_SIZE_BLOCKS = CACHE_SIZE_BYTES / BLOCK_SIZE;
		blockBuffer = new byte[BLOCK_SIZE];
		this.cachingStrategy = cachingStrategy;

		// init cache
		// TODO use allocateDirect() ?
		buffer = ByteBuffer.allocate(CACHE_SIZE_BYTES);
		for (int i = 0; i < CACHE_SIZE_BLOCKS; i++)
			freeList.add(i);

		// handle file
		try {
			File fileEntry = new File(fileName);

			if (!fileEntry.exists()) {

				// create the file if it doesn't exist
				if (fileEntry.createNewFile())
					this.file = new RandomAccessFile(fileEntry, "rw");
				else
					throw new PDStoreException("File creation failed!");

				// block 0 contains metadata
				file.write(VERSION_GUID.toByteArray());
				repositoryID = GUID.newBranchId();
				file.write(repositoryID.toByteArray());

				// for a new B-tree, block 1 is always the root
				rootBlockIndex = 1;
				file.writeInt(rootBlockIndex);
				file.setLength(BLOCK_SIZE);
				fileSizeInBlocks = 1;

				// add & load the root block
				addNewBlockToFile();

			} else {

				// load the file header if the file exists
				file = new RandomAccessFile(fileEntry, "rw");

				// check version GUID
				byte[] bytes = new byte[16];
				file.read(bytes);
				GUID versionID = new GUID(bytes);
				if (!versionID.equals(VERSION_GUID))
					throw new PDStoreException("File " + fileName
							+ " is not a PersistentChangeIndex file.");

				// read other metadata
				file.read(bytes);
				repositoryID = new GUID(bytes);
				rootBlockIndex = file.readInt();

				fileSizeInBlocks = (long) Math.ceil((double) file.length()
						/ BLOCK_SIZE);
			}
		} catch (IOException e) {
			throw new PDStoreException("Error opening file", e);
		}
	}

	/**
	 * Adds a new block to the end of the cached file and returns the index of
	 * that block. The new block is also loaded into the cache.
	 * 
	 * @return the index of the new block
	 */
	public int addNewBlockToFile() {
		// add a new block to the file
		fileSizeInBlocks++;

		// get last index
		int fileBlockIndex = (int) fileSizeInBlocks - 1;

		// load node into free cache block
		int cacheBlockIndex = getFreeCacheBlock();
		cacheMap.put(fileBlockIndex, cacheBlockIndex);
		cachingStrategy.blockAccessed(fileBlockIndex);
		return fileBlockIndex;
	}

	/**
	 * Ensures the block of the file with the given index is loaded into the
	 * cache.
	 * 
	 * @param fileBlockIndex
	 *            the index of the desired block in the file.
	 * @return the index of the block in the cache.
	 */
	public int loadBlock(int fileBlockIndex) {
		Integer cacheBlockIndex = cacheMap.get(fileBlockIndex);

		// if cache miss, then load block
		if (cacheBlockIndex == null) {

			// load node into free cache block
			cacheBlockIndex = getFreeCacheBlock();
			Debug.assertTrue(cacheBlockIndex < CACHE_SIZE_BLOCKS,
					"Trying to write past the end of the cache: block index "
							+ cacheBlockIndex + " of "
							+ (CACHE_SIZE_BLOCKS - 1));
			try {
				file.seek(fileBlockIndex * BLOCK_SIZE);
				file.read(blockBuffer, 0, BLOCK_SIZE);
				buffer.position(cacheBlockIndex * BLOCK_SIZE);
				buffer.put(blockBuffer);
			} catch (IOException e) {
				throw new PDStoreException("Error reading file", e);
			}
			Debug.assertTrue(cacheBlockIndex != null,
					"cacheBlockIndex of block " + fileBlockIndex
							+ " must not be null!");
			cacheMap.put(fileBlockIndex, cacheBlockIndex);
		}

		cachingStrategy.blockAccessed(fileBlockIndex);
		return cacheBlockIndex;
	}

	int getFreeCacheBlock() {
		// check for free cache block
		if (!freeList.isEmpty())
			return freeList.remove(0);

		int fileBlockIndex = cachingStrategy.selectBlockToReplace();
		Integer cacheBlockIndex = cacheMap.get(fileBlockIndex);
		Debug.assertTrue(cacheBlockIndex != null, "cacheBlockIndex of block "
				+ fileBlockIndex + " must not be null!");

		// write block back to file if dirty
		if (dirtySet.contains(fileBlockIndex))
			writeBlock(fileBlockIndex);

		cacheMap.remove(fileBlockIndex);
		return cacheBlockIndex;
	}

	/**
	 * Writes the block with the given file index back to the file.
	 * 
	 * @param cacheBlockIndex
	 *            file index of the block to write
	 */
	public void writeBlock(int fileBlockIndex) {
		Debug.println("swapping out:" + fileBlockIndex);
		Integer cacheBlockIndex = cacheMap.get(fileBlockIndex);

		// if block not in cache, then nothing to do
		if (cacheBlockIndex == null)
			return;

		// write block from cache to file
		buffer.position(cacheBlockIndex * BLOCK_SIZE);
		buffer.get(blockBuffer, 0, BLOCK_SIZE);
		try {
			file.seek(fileBlockIndex * BLOCK_SIZE);
			file.write(blockBuffer);
		} catch (IOException e) {
			throw new PDStoreException("Error writing file", e);
		}

		// Remove block from dirty list, as it has been written back now.
		// Note: cast is necessary to use remove(Object) and not remove(int).
		dirtySet.remove((Object) fileBlockIndex);
	}

	/**
	 * Writes all volatile header data.
	 */
	public void writeHeader() {
		try {
			file.seek(16);
			byte[] bytes = repositoryID.toByteArray();
			file.write(bytes);
			file.writeInt(rootBlockIndex);
		} catch (IOException e) {
			throw new PDStoreException("Error writing file", e);
		}
	}

	/**
	 * Writes the header and all dirty cache blocks back to the file.
	 */
	public void flush() {
		writeHeader();

		// Write all dirty blocks to the file.
		// Note: the cloning of the list is necessary because writeBlock() will
		// remove the dirty entry from the list (no modification allowed while
		// iterating).
		for (Integer fileBockIndex : new ArrayList<Integer>(dirtySet))
			writeBlock(fileBockIndex);

		// Force outstanding writes onto persistent storage.
		// Note: whether this is really necessary is unclear.
		try {
			file.getChannel().force(false);
		} catch (IOException e) {
			throw new PDStoreException("Error writing file", e);
		}
	}

	public void close() {
		flush();
		try {
			file.close();
		} catch (IOException e) {
			throw new PDStoreException("Error closing file", e);
		}
	}

	protected void finalize() throws Throwable {
		close();
	}

}
