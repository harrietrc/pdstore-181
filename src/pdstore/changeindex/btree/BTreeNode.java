package pdstore.changeindex.btree;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.GUID;
import pdstore.PDChange;

/**
 * Provides an OO access layer to the binary representation of a B-tree node in
 * a cached block.
 * 
 * @author Christof
 * 
 */
public class BTreeNode {

	/**
	 * Header size in bytes. There are 2 ints in the header: size and
	 * leftChildNodeIndex.
	 */
	final static int HEADER_SIZE = 2 * 4;

	/**
	 * The cache this node is stored in.
	 */
	FileCache cache;

	/**
	 * The index of the file block the node is contained in.
	 */
	int fileBlockIndex;

	/**
	 * The index of the cache block the node is contained in.
	 */
	int cacheBlockIndex;

	/**
	 * The size of this node in bytes.
	 */
	int size;

	/**
	 * The index of the cache block of the left child node, i.e. the one
	 * containing values smaller than all the values in the node.
	 */
	int leftChildNodeIndex;

	/**
	 * Ordered list of the changes (ascendingly by PDChange.compareTo()) and
	 * their pointers to child nodes of this node.
	 */
	List<ChangeAndPointer> changesAndPointers = new ArrayList<ChangeAndPointer>(
			256);

	public int getSize() {
		return size;
	}

	public int getLeftChildNodeIndex() {
		return leftChildNodeIndex;
	}

	/**
	 * Checks whether this node has enough space for the given change and a
	 * pointer.
	 * 
	 * @param change
	 *            the change to consider for the space check
	 * @return true iff there is enough space to accommodate the given change
	 *         and a pointer
	 */
	public boolean hasSpaceFor(PDChange<GUID, Object, GUID> change) {
		// if the node is too full, return false
		int newSize = (int) (size + change.getSize() + 4 /* pointer size */);
		return (newSize <= cache.BLOCK_SIZE);
	}

	/**
	 * Adds a change and a pointer to a child node to this node.
	 * 
	 * @param cap
	 *            object containing the change and pointer to add
	 */
	public void addChangeAndPointer(ChangeAndPointer cap) {
		// insert given change at the right place using binary search
		int l = 0;
		int r = changesAndPointers.size();
		while (l != r) {
			int medianIndex = (l + r) / 2;
			int order = cap.compareTo(changesAndPointers.get(medianIndex));
			if (order < 0)
				r = medianIndex;
			else
				l = medianIndex + 1;
		}
		changesAndPointers.add(l, cap);
		size = (int) (size + cap.change.getSize() + 4 /* pointer size */);
	}

	/**
	 * Replaces all the changes and pointers of this node with the given
	 * change-and-pointer pairs
	 * 
	 * @param caps
	 *            collection of change-and-pointer pairs to write into this node
	 */
	public void replaceChangesAndPointers(Collection<ChangeAndPointer> caps) {
		changesAndPointers.clear();
		size = HEADER_SIZE;
		for (ChangeAndPointer cap : caps) {
			changesAndPointers.add(cap);
			size = (int) (size + cap.change.getSize() + 4 /* pointer size */);
		}
	}

	private BTreeNode() {
	}

	/**
	 * Loads a BTreeNode object for an existing node.
	 * 
	 * @param cache
	 * @param fileBlockIndex
	 * @param blobFileName
	 *            name of the file where long blobs etc. are stored (typically
	 *            the log file)
	 * @return
	 */
	public static BTreeNode load(FileCache cache, int fileBlockIndex,
			String blobFileName) {
		BTreeNode node = new BTreeNode();
		node.cache = cache;
		node.fileBlockIndex = fileBlockIndex;
		node.cacheBlockIndex = cache.loadBlock(fileBlockIndex);

		// parse the byte array representation of this node
		// i.e. fill size, parentNodeIndex and leftChildNodeIndex
		ByteBuffer buffer = cache.buffer;
		buffer.position(node.cacheBlockIndex * cache.BLOCK_SIZE);
		int expectedSize = buffer.getInt();
		node.leftChildNodeIndex = buffer.getInt();

		Debug.assertTrue(expectedSize <= cache.BLOCK_SIZE,
				"Data corruption in B-tree node: node size > BLOCK_SIZE.");
		Debug.assertTrue(expectedSize >= 0,
				"Data corruption in B-tree node: node size < 0.");
		Debug.assertTrue(node.leftChildNodeIndex >= 0,
				"Data corruption in B-tree node: leftChildNodeIndex < 0.");

		// now parse the changes with their pointers
		node.size = HEADER_SIZE;
		while (node.size < expectedSize) {
			PDChange<GUID, Object, GUID> change = PDChange.read(buffer,
					blobFileName);
			int pointer = buffer.getInt();

			// size gets incremented in addChangeAndPointer
			node.addChangeAndPointer(new ChangeAndPointer(change, pointer));
		}
		return node;
	}

	/**
	 * Creates a new B-tree node in a fresh block.
	 * 
	 * @param cache
	 * @param fileBlockIndex
	 * @param cacheBlockIndex
	 * @param parentNodeIndex
	 */
	public static BTreeNode create(FileCache cache, int leftChildNodeIndex) {
		BTreeNode node = new BTreeNode();
		node.cache = cache;
		node.fileBlockIndex = cache.addNewBlockToFile();
		node.cacheBlockIndex = cache.loadBlock(node.fileBlockIndex);

		node.size = HEADER_SIZE;
		node.leftChildNodeIndex = leftChildNodeIndex;
		return node;
	}

	/**
	 * Write the node data back to its cache block.
	 */
	public void writeToCache() {
		Debug.assertTrue(cacheBlockIndex < cache.CACHE_SIZE_BLOCKS,
				"Trying to write past the end of the cache: block index "
						+ cacheBlockIndex + " of "
						+ (cache.CACHE_SIZE_BLOCKS - 1));
		Debug.assertTrue(size <= cache.BLOCK_SIZE,
				"Data corruption in B-tree node: node size > BLOCK_SIZE.");
		Debug.assertTrue(size >= 0,
				"Data corruption in B-tree node: node size < 0.");
		Debug.assertTrue(leftChildNodeIndex >= 0,
				"Data corruption in B-tree node: leftChildNodeIndex < 0.");

		ByteBuffer buffer = cache.buffer;
		buffer.position(cacheBlockIndex * cache.BLOCK_SIZE);

		// write node header
		buffer.putInt(size);
		buffer.putInt(leftChildNodeIndex);

		// write changes and pointers to child nodes
		for (ChangeAndPointer cap : changesAndPointers) {
			cap.change.write(buffer);
			buffer.putInt(cap.childBlockIndex);
		}

		cache.dirtySet.add(fileBlockIndex);
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("(#" + fileBlockIndex + ", ");
		s.append("<" + leftChildNodeIndex);
		for (ChangeAndPointer cap : changesAndPointers)
			s.append(", " + cap);
		s.append(")");
		return s.toString();
	}
}
