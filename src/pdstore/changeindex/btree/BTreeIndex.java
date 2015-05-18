package pdstore.changeindex.btree;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;

/**
 * Stub for a cached B-tree index. The idea is to simply arrange full changes
 * similar to ChangeLogStore, but as a B-tree instead of a list. That is, there
 * are no key/value pairs but just serialized changes (keys), because (almost)
 * any part of a change could be useful as part of the key.
 * 
 * @author Christof
 * 
 */
public class BTreeIndex {
	/**
	 * File cache for all B-tree nodes.
	 */
	FileCache cache;

	/**
	 * Log file reference for loading Blobs (which are only stored in the log).
	 */
	String blobFileName;

	public BTreeIndex(String fileName, String blobFileName) {
		cache = new FileCache(fileName);
		this.blobFileName = blobFileName;
	}

	public BTreeIndex(String fileName, int blockSize, int cacheSizeInBytes,
			CachingStrategyI cachingStrategy, String blobFileName) {
		cache = new FileCache(fileName, blockSize, cacheSizeInBytes,
				cachingStrategy);
		this.blobFileName = blobFileName;
	}

	/**
	 * Searches the first change in the tree that matches the given pattern, and
	 * then returns it and all following changes in an iterator.
	 * 
	 * This should usually be only called with patterns that have only
	 * trailing null values. (see PDChange.compareTo() for lexicographical order)
	 * I.e. if instance1 is null, then everything later, namely instance2,
	 * transaction, changeType should be null.
	 * 
	 * Otherwise the index gets more and more inefficient.
	 * 
	 * BTreeIndexStore only makes the right kind of calls.
	 * 
	 * 
	 * @param pattern
	 * @return
	 */
	public Iterator<PDChange<GUID, Object, GUID>> get(
			PDChange<GUID, Object, GUID> pattern) {

		// clone the pattern to make this method non-destructive
		pattern = new PDChange<GUID, Object, GUID>(pattern);
		pattern.normalize();

		// TODO: move into PDChange
		/*
		 * In the B-tree index only the ChangeTypes LINK_ADDED and LINK_REMOVED
		 * appear. Therefore, all other ChangeTypes need to be adjusted so that
		 * the right changes in the index are captured by the search.
		 */
		if (pattern.getChangeType() == ChangeType.LINK_EFFECTIVE)
			// both LINK_ADDED and LINK_REMOVED are important for LINK_EFFECTIVE
			pattern.setChangeType(null);
		else if (pattern.getChangeType() == ChangeType.LINK_NOW_ADDED)
			pattern.setChangeType(ChangeType.LINK_ADDED);
		else if (pattern.getChangeType() == ChangeType.LINK_NOW_REMOVED)
			pattern.setChangeType(ChangeType.LINK_REMOVED);

		return new BTreeRangeIterator(cache, pattern, blobFileName);
	}

	/**
	 * Adds the given change to the tree.
	 * 
	 * @param change
	 */
	public void add(PDChange<GUID, Object, GUID> change) {
		change.normalize();
		ChangeAndPointer capToAdd = add(change, cache.rootBlockIndex);

		/*
		 * If an element to add is returned, then the old root was split and a
		 * new root needs to be created, with the element in it (which is the
		 * median of the old root that was split).
		 */
		if (capToAdd != null) {
			BTreeNode newRoot = BTreeNode.create(cache, cache.rootBlockIndex);
			newRoot.addChangeAndPointer(capToAdd);
			newRoot.writeToCache();

			// update pointer to the new root
			cache.rootBlockIndex = newRoot.fileBlockIndex;
		}
	}

	/**
	 * Adds the given change to the block with the given index, or to one of its
	 * children if the block is not a leaf. In case of lack of space, blocks are
	 * split. Splitting of blocks may propagate up the tree and eventually lead
	 * to a new root being inserted.
	 * 
	 * @param change
	 * @param fileBlockIndex
	 */
	ChangeAndPointer add(PDChange<GUID, Object, GUID> change, int fileBlockIndex) {
		BTreeNode node = BTreeNode.load(cache, fileBlockIndex, blobFileName);

		// Find the right child node to insert the change, and also check if the
		// change is already in the tree
		int l = 0;
		int r = node.changesAndPointers.size();
		while (l != r) {
			int medianIndex = (l + r) / 2;
			int order = change.compareTo(node.changesAndPointers
					.get(medianIndex).change);
			if (order < 0)
				r = medianIndex;
			else if (order > 0)
				l = medianIndex + 1;
			else
				// If the change is already inserted, then nothing to do.
				return null;
		}

		// the childBlockIndex is the one before the element we found
		int childBlockIndex;
		if (l == 0)
			childBlockIndex = node.leftChildNodeIndex;
		else
			childBlockIndex = node.changesAndPointers.get(l - 1).childBlockIndex;

		// A ChangeAndPointer that needs to be added to this block, to be
		// determined below.
		ChangeAndPointer capToAdd;

		if (childBlockIndex != 0) {
			// This block is not a leaf yet; go down further.
			capToAdd = add(change, childBlockIndex);

			// If we have not noted down any change to be added to this block,
			// then there was enough space in the child nodes,
			// nothing to do.
			if (capToAdd == null)
				return null;
		} else {
			// It is a leaf; we proceed and try to add the change.
			capToAdd = new ChangeAndPointer(change, 0);
		}
        // capToAdd is now non-null.
		// Change has now to be added to the current node.

		// If there is enough space in the block, add the change, write the
		// block to cache and return.
		if (node.hasSpaceFor(capToAdd.change)) {
			node.addChangeAndPointer(capToAdd);
			node.writeToCache();
			return null;
		}

		/*
		 * Being here means that there is still a change that needs to be added
		 * to this block, but the block will be overfull. We need to split the
		 * block and add the median change to the parent (which may in turn be
		 * split recursively).
		 */
		node.addChangeAndPointer(capToAdd);
		List<ChangeAndPointer> caps = new ArrayList<ChangeAndPointer>(
				node.changesAndPointers);

		// get median element
		int medianIndex = caps.size() / 2;
		ChangeAndPointer median = caps.get(medianIndex);

		// truncate old node so that it contains changes only before the median
		// (left sibling)
		node.replaceChangesAndPointers(caps.subList(0, medianIndex));

		// create new sibling node with changes after the median (right sibling)
		BTreeNode rightSibling = BTreeNode
				.create(cache, median.childBlockIndex);
		rightSibling.replaceChangesAndPointers(caps.subList(medianIndex + 1,
				caps.size()));

		median.childBlockIndex = rightSibling.fileBlockIndex;

		// make sure node modifications are written back into the binary cache
		node.writeToCache();
		rightSibling.writeToCache();

		// Returning the median means that it gets noted down for addition to
		// the parent of this block (see variable capToAdd above).
		return median;
	}

	/**
	 * Write all dirty nodes (i.e. modified cache blocks) back to the index
	 * file.
	 */
	public void flush() {
		cache.flush();
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 1; i < cache.getFileSizeInBlocks(); i++) {
			BTreeNode node = BTreeNode.load(cache, i, blobFileName);
			s.append(node.toString() + " ");
		}
		return s.toString();
	}

	public String toStringTree() {
		return toStringTree(cache.rootBlockIndex, 0);
	}

	public String toStringTree(int fileBlockIndex, int tabs) {
		StringBuffer s = new StringBuffer();
		BTreeNode node = BTreeNode.load(cache, fileBlockIndex, blobFileName);

		if (node.leftChildNodeIndex != 0)
			s.append(toStringTree(node.leftChildNodeIndex, tabs + 1));

		for (ChangeAndPointer cap : node.changesAndPointers) {
			for (int i = 0; i < tabs; i++)
				s.append("\t");
			s.append(cap.change.toRawString() + "\n");
			if (cap.childBlockIndex != 0)
				s.append(toStringTree(cap.childBlockIndex, tabs + 1));
		}
		return s.toString();
	}
}
