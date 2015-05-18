package pdstore.changeindex.btree;

import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Stack;

import nz.ac.auckland.se.genoupe.tools.StatefulIterator;
import pdstore.GUID;
import pdstore.PDChange;

/**
 * An iterator that iterates in-order through the changes in the given B-tree,
 * starting with the first change that matches the given change pattern. It is
 * based on the StatefulIterator (i.e. computeNext() instead of hasNext() and
 * next()).
 * 
 * @author Christof
 * 
 */
public class BTreeRangeIterator extends
		StatefulIterator<PDChange<GUID, Object, GUID>> {

	/**
	 * File cache for all B-tree nodes.
	 */
	FileCache cache;

	/**
	 * The change pattern to search the tree for.
	 */
	PDChange<GUID, Object, GUID> changePattern;

	/**
	 * Stack to keep track of the depth-first search. Note: we cannot simply use
	 * the call stack because this is an iterator; it has to return something
	 * for every call of next() / computeNext().
	 */
	Stack<MatchingRangeInNode> stack = new Stack<MatchingRangeInNode>();

	String blobFileName;

	/**
	 * Describes what changes of a particular node match the changePattern and
	 * which of its child nodes may contain matching changes.
	 * 
	 * @author Christof
	 * 
	 */
	class MatchingRangeInNode {
		int childBlockIndex;
		ChangeAndPointer nextCaP;
		Iterator<ChangeAndPointer> remainingMatchingCaPs;

		public MatchingRangeInNode(int childBlockIndex,
				ChangeAndPointer nextCaP,
				Iterator<ChangeAndPointer> remainingMatchingCaPs) {
			this.childBlockIndex = childBlockIndex;
			this.nextCaP = nextCaP;
			this.remainingMatchingCaPs = remainingMatchingCaPs;
		}
	}

	public BTreeRangeIterator(FileCache cache,
			PDChange<GUID, Object, GUID> changePattern, String blobFileName) {
		this.cache = cache;
		this.changePattern = changePattern;
		this.blobFileName = blobFileName;

		MatchingRangeInNode rangeInNode = findMatchingRangeInNode(cache.rootBlockIndex);
		stack.push(rangeInNode);
	}

	MatchingRangeInNode findMatchingRangeInNode(int fileBlockIndex) {
		BTreeNode node = BTreeNode.load(cache, fileBlockIndex, blobFileName);

		int childBlockIndex = node.leftChildNodeIndex;
		Iterator<ChangeAndPointer> iterator = node.changesAndPointers
				.iterator();
		// TODO try to use binary search here
		while (iterator.hasNext()) {
			ChangeAndPointer cap = iterator.next();
			if (changePattern.compareTo(cap.change) <= 0)
				return new MatchingRangeInNode(childBlockIndex, cap, iterator);
			childBlockIndex = cap.childBlockIndex;
		}
		// the changePattern is greater than all the changes in this node
		return new MatchingRangeInNode(childBlockIndex, null, null);
	}

	protected PDChange<GUID, Object, GUID> computeNext() {
		while (!stack.isEmpty()) {
			MatchingRangeInNode rangeInNode = stack.peek();

			// First make sure we are at the deepest unprocessed node (go down).
			// If it is a leaf or we have already visited the node below,
			// indicated by a childBlockIndex that is set to 0.
			while (rangeInNode.childBlockIndex != 0) {
				MatchingRangeInNode rangeInChildNode = findMatchingRangeInNode(rangeInNode.childBlockIndex);

				// mark the pointer as already traversed
				rangeInNode.childBlockIndex = 0;

				// go down to the child node
				stack.push(rangeInChildNode);
				rangeInNode = rangeInChildNode;
			}

			// Now try to return the nextMatchingChange of the deepest
			// unprocessed node. If it has already been returned, it is set to
			// null.
			if (rangeInNode.nextCaP != null
					&& rangeInNode.nextCaP.change != null) {
				PDChange<GUID, Object, GUID> nextMatchingChange = rangeInNode.nextCaP.change;
				rangeInNode.nextCaP.change = null;
				return nextMatchingChange;
			}

			// If there is no change, look for the next pointer to a child node.
			// Mark it as traversed by setting it to 0. Continue to actually
			// traverse it.
			if (rangeInNode.nextCaP != null
					&& rangeInNode.nextCaP.childBlockIndex != 0) {
				rangeInNode.childBlockIndex = rangeInNode.nextCaP.childBlockIndex;
				rangeInNode.nextCaP.childBlockIndex = 0;
				continue;
			}

			// Nothing left but to check the remainingMatchingCaPs.
			if (rangeInNode.remainingMatchingCaPs != null
					&& rangeInNode.remainingMatchingCaPs.hasNext()) {
				rangeInNode.nextCaP = rangeInNode.remainingMatchingCaPs.next();
				continue;
			}

			// If we are here this means this node has been completely
			// processed. Go up to the parent node (go up) and repeat the
			// procedure.
			stack.pop();
		}
		return null;
	}
}
