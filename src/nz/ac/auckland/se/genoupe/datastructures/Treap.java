package nz.ac.auckland.se.genoupe.datastructures;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Stack;

import nz.ac.auckland.se.genoupe.tools.Debug;

/**
 * An implentation of an Comparable<K> dictionary data structure, based on the
 * randomized search trees (treaps) by Seidel and Aragon. <blockquote> R. Seidel
 * and C. R. Aragon. Randomized Binary Search Trees. <em>Algorithmica</em>,
 * 16(4/5):464-497, 1996. </blockquote>
 * 
 * This class implements an Comparable<K> dictionary that maps keys to values.
 * Any non-null object can be used as a value, but the keys must be non-null
 * objects that implement the <code>Ordered</code> interface. The methods are
 * similar to the ones in <code>java.util.Hashtable</code>. In addition,
 * efficient methods for finding the minimum and maximum keys and their values
 * are included. The enumeration returns the keys in sorted order.
 * <p>
 * 
 * Most methods run in <math>O(log n)</math> randomized time, where
 * <math>n</math> is the number of keys in the treap; the exceptions are
 * <code>clone()</code> and <code>toString()</code> that run in time
 * proportional to the size of their output.
 * 
 * @author Stefan.Nilsson@hut.fi
 * @see Ordered
 * @version 1.0, 22 April 1997
 */
public class Treap<K extends Comparable<K>, V, P extends Comparable<P>>
		implements Cloneable {
	private Tree<K, V, P> tree;
	private int size;

	/*
	 * If during an <code>insert()</code> or <code>delete()</code> it is found
	 * that the key is present in the tree, <code>keyFound</code> will be
	 * <code>true</code> and <code>prevValue</code> will contain the previous
	 * value assosiacted with the key before the update.
	 */
	private boolean keyFound;
	private Object prevValue;

	/**
	 * Constructs a new empty treap.
	 */
	public Treap() {
		tree = null;
		size = 0;
	}

	/**
	 * Maps the key to the specified value in this treap. Neither the key, nor
	 * the value can be <code>null</code>.
	 * 
	 * @return the previous value to which the key was mapped, or
	 *         <code>null</code> if the key did not have a previous mapping in
	 *         the treap.
	 */
	public synchronized Object put(K key, V value, P priority) {
		Debug.assertTrue(key != null, "Key must not be null!");
		Debug.assertTrue(priority != null, "Priority must not be null!");

		Tree<K, V, P> node = new Tree<K, V, P>();
		node.key = key;
		node.value = value;
		node.priority = priority;
		keyFound = false;
		tree = insert(node, tree);
		if (keyFound)
			return prevValue;
		else {
			size++;
			return null;
		}
	}

	/**
	 * Gets the object associated with the specified key in the treap.
	 * 
	 * @return the value to which the key is mapped in the treap, or
	 *         <code>null</code> if the key is not mapped to any value in this
	 *         treap.
	 */
	public synchronized Object get(Comparable<K> key) {
		Tree<K, V, P> t = tree;
		int comp;

		while (t != null && (comp = key.compareTo(t.key)) != 0)
			if (comp < 0)
				t = t.left;
			else
				t = t.right;
		return t != null ? t.value : null;
	}

	/**
	 * Returns the minimum key of the treap.
	 * 
	 * @exception NoSuchElementException
	 *                if the Treap<K,V,P> is empty.
	 */
	public synchronized Comparable<K> getMinKey() {
		Tree<K, V, P> t = tree;

		if (t == null)
			throw new NoSuchElementException("Treap");
		while (t.left != null)
			t = t.left;
		return t.key;
	}

	/**
	 * Returns the maximum key of the treap.
	 * 
	 * @exception NoSuchElementException
	 *                if the Treap<K,V,P> is empty.
	 */
	public synchronized Comparable<K> getMaxKey() {
		Tree<K, V, P> t = tree;

		if (t == null)
			throw new NoSuchElementException("Treap");
		while (t.right != null)
			t = t.right;
		return t.key;
	}

	/**
	 * Returns the value to which the minimum key of the Treap<K,V,P> is mapped.
	 * 
	 * @exception NoSuchElementException
	 *                if the Treap<K,V,P> is empty.
	 */
	public synchronized Object getMinValue() {
		Tree<K, V, P> t = tree;

		if (t == null)
			throw new NoSuchElementException("Treap");
		while (t.left != null)
			t = t.left;
		return t.value;
	}

	/**
	 * Returns the value to which the maximum key of the Treap<K,V,P> is mapped.
	 * 
	 * @exception NoSuchElementException
	 *                if the Treap<K,V,P> is empty.
	 */
	public synchronized Object getMaxValue() {
		Tree<K, V, P> t = tree;

		if (t == null)
			throw new NoSuchElementException("Treap");
		while (t.right != null)
			t = t.right;
		return t.value;
	}

	/**
	 * Returns the key with the minimum priority.
	 * 
	 * @exception NoSuchElementException
	 *                if the Treap<K,V,P> is empty.
	 */
	public synchronized K getKeyWithMinPriority() {
		Tree<K, V, P> t = tree;

		if (t == null)
			throw new NoSuchElementException("Treap");

		return t.key;
	}

	/**
	 * Returns an Comparable<K> enumeration of the keys in this treap. The
	 * enumeration will list the keys in ascending order.
	 * 
	 * @see Enumeration
	 */
	public synchronized Enumeration<K> keys() {
		return new TreapEnumeration<K, V, P>(tree, true);
	}

	/**
	 * Returns an Comparable<K> enumeration of the keys in this treap. The
	 * enumeration will list the keys in ascending order if
	 * <code>ascending</code> is <code>true</code>, otherwise the keys will be
	 * listed in descending order.
	 * 
	 * @see Enumeration
	 */
	public synchronized Enumeration<K> keys(boolean ascending) {
		return new TreapEnumeration<K, V, P>(tree, ascending);
	}

	/**
	 * Returns <code>true</code> if this Treap<K,V,P> contains no mappings.
	 */
	public boolean isEmpty() {
		return tree == null;
	}

	/**
	 * Removes the key (and its corresponding value) from this treap. This
	 * method does nothing if the key is not in the treap.
	 * 
	 * @return the value to which the key had been mapped in this treap, or
	 *         <code>null</code> if the key did not have a mapping.
	 */
	public synchronized Object remove(Comparable<K> key) {
		keyFound = false;
		tree = delete(key, tree);
		if (keyFound) {
			size--;
			return prevValue;
		} else
			return null;
	}

	/**
	 * Removes tke minimum key (and its corresponding value) from this treap.
	 * This method does nothing if the Treap<K,V,P> is empty.
	 * 
	 * @return the value to which the minimum key had been mapped in this treap,
	 *         or <code>null</code> if the Treap<K,V,P> was empty.
	 */
	public synchronized Object removeMin() {
		Tree<K, V, P> t = tree, tprev;

		if (tree == null)
			return null;
		if (t.left == null)
			tree = t.right;
		else {
			do {
				tprev = t;
				t = t.left;
			} while (t.left != null);
			tprev.left = t.right;
		}
		size--;
		return t.value;
	}

	/**
	 * Removes tke maximum key (and its corresponding value) from this treap.
	 * This method does nothing if the Treap<K,V,P> is empty.
	 * 
	 * @return the value to which the maximum key had been mapped in this treap,
	 *         or <code>null</code> if the Treap<K,V,P> was empty.
	 */
	public synchronized Object removeMax() {
		Tree<K, V, P> t = tree, tprev;

		if (t == null)
			return null;
		if (t.right == null)
			tree = t.left;
		else {
			do {
				tprev = t;
				t = t.right;
			} while (t.right != null);
			tprev.right = t.left;
		}
		size--;
		return t.value;
	}

	/**
	 * Clear the Treap<K,V,P> so that it contains no mappings.
	 */
	public synchronized void clear() {
		tree = null;
		size = 0;
	}

	/**
	 * Returns the number of keys in this treap.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns a string representation of this treap. This routine is
	 * inefficient and primarily intended for debugging. To access the elements
	 * in the Treap<K,V,P> in sorted order use the <code>keys()</code> and
	 * <code>elements()</code methods.
	 * 
	 * @see Treap#keys
	 * @see Treap#elements
	 */
	public synchronized String toString() {
		StringBuffer strbuf = new StringBuffer();

		strbuf.append("{");
		if (tree != null)
			strbuf.append(tree.toString());
		if (strbuf.length() > 1)
			strbuf.setLength(strbuf.length() - 2); // remove last ", "
		strbuf.append("}");
		return strbuf.toString();
	}

	/**
	 * Creates a shallow copy of this treap. The keys and the values themselves
	 * are not cloned.
	 * 
	 * @return a clone of this treap.
	 */
	public synchronized Object clone() {
		try {
			Treap<K, V, P> treap = (Treap<K, V, P>) super.clone();
			treap.tree = (tree != null) ? (Tree<K, V, P>) tree.clone() : null;
			return treap;
		} catch (CloneNotSupportedException e) {
			// Cannot happen
			throw new InternalError(e.toString());
		}
	}

	/* Inserts a node into tree and returns the updated Treap<K,V,P> */
	private Tree<K, V, P> insert(Tree<K, V, P> node, Tree<K, V, P> tree) {
		if (tree == null)
			return node;
		int comp = node.key.compareTo(tree.key);
		if (comp < 0) {
			tree.left = insert(node, tree.left);
			if (tree.priority.compareTo(tree.left.priority) > 0)
				tree = tree.rotateRight();
		} else if (comp > 0) {
			tree.right = insert(node, tree.right);
			if (tree.priority.compareTo(tree.right.priority) > 0)
				tree = tree.rotateLeft();
		} else {
			keyFound = true;
			prevValue = tree.value;
			tree.value = node.value;
		}
		return tree;
	}

	/*
	 * Searches for a node with this key. If found, deletes this node and
	 * returns the updated treap.
	 */
	private Tree<K, V, P> delete(Comparable<K> key, Tree<K, V, P> t) {
		if (t == null)
			return null;
		int comp = key.compareTo(t.key);
		if (comp < 0)
			t.left = delete(key, t.left);
		else if (comp > 0)
			t.right = delete(key, t.right);
		else {
			keyFound = true;
			prevValue = t.value;
			t = t.deleteRoot();
		}
		return t;
	}

	/**
	 * Prints the Treap<K,V,P> on stderr, displaying the tree structure and the
	 * priority numbers.
	 */
	public synchronized void printDebug() {
		System.err.println("size: " + size);
		if (tree != null)
			tree.printDebug(0);
	}
}

/* An object of this class represents a node in a binary tree */
class Tree<K extends Comparable<K>, V, P extends Comparable<P>> implements
		Cloneable {
	K key;
	V value;
	P priority;
	Tree<K, V, P> left, right;

	/*
	 * Rotate this tree to the left
	 */
	final Tree<K, V, P> rotateLeft() {
		Tree<K, V, P> temp = right;
		right = right.left;
		temp.left = this;
		return temp;
	}

	/*
	 * Rotate this tree to the right
	 */
	final Tree<K, V, P> rotateRight() {
		Tree<K, V, P> temp = left;
		left = left.right;
		temp.right = this;
		return temp;
	}

	/*
	 * If one of the children is an empty tree, remove the root and put the
	 * other child in its place. If both children are nonempty, rotate the tree
	 * at the root so that the child with the smallest prio-number comes to the
	 * top, then delete the root from the other subtee.
	 */
	final Tree<K, V, P> deleteRoot() {
		Tree<K, V, P> temp;

		if (left == null)
			return right;
		if (right == null)
			return left;
		if (left.priority.compareTo(right.priority) < 0) {
			temp = rotateRight();
			temp.right = deleteRoot();
		} else {
			temp = rotateLeft();
			temp.left = deleteRoot();
		}
		return temp;
	}

	public String toString() {
		StringBuffer strbuf = new StringBuffer();

		if (left != null)
			strbuf.append(left.toString());
		strbuf.append(key + "=" + value + ", ");
		if (right != null)
			strbuf.append(right.toString());

		return strbuf.toString();
	}

	/*
	 * Creates a shallow copy of this tree. The keys and the values themselves
	 * are not cloned.
	 */
	protected Object clone() {
		try {
			Tree<K, V, P> tree = (Tree<K, V, P>) super.clone();

			tree.left = (left != null) ? (Tree<K, V, P>) left.clone() : null;
			tree.right = (right != null) ? (Tree<K, V, P>) right.clone() : null;
			return tree;
		} catch (CloneNotSupportedException e) {
			// Cannot happen
			throw new InternalError(e.toString());
		}
	}

	/*
	 * Print in sorted order, displaying the tree structure and the priority
	 * numbers.
	 */
	void printDebug(int level) {
		if (left != null)
			left.printDebug(level + 1);
		for (int i = 0; i < level; i++)
			System.err.print("  ");
		System.err.println(key + "=" + value + " (" + priority + ")");
		if (right != null)
			right.printDebug(level + 1);
	}
}

/**
 * A Treap<K,V,P> enumeration class that returns the keys or elements of a
 * Treap<K,V,P> in sorted order. This class should not be seen by the client.
 */
class TreapEnumeration<K extends Comparable<K>, V, P extends Comparable<P>>
		implements Enumeration<K> {
	private Stack<Tree<K, V, P>> stack;
	private boolean ascending;

	TreapEnumeration(Tree<K, V, P> t, boolean ascending) {
		stack = new Stack<Tree<K, V, P>>();

		this.ascending = ascending;
		if (ascending)
			for (; t != null; t = t.left)
				stack.push(t);
		else
			// descending
			for (; t != null; t = t.right)
				stack.push(t);
	}

	public boolean hasMoreElements() {
		return !stack.empty();
	}

	public K nextElement() {
		if (stack.empty())
			throw new NoSuchElementException("TreapEnumeration");

		Tree<K, V, P> node = stack.peek();
		if (ascending)
			if (node.right == null) {
				Tree<K, V, P> t = stack.pop();
				while (!stack.empty() && (stack.peek()).right == t)
					t = stack.pop();
			} else
				for (Tree<K, V, P> t = node.right; t != null; t = t.left)
					stack.push(t);
		else // descending
		if (node.left == null) {
			Tree<K, V, P> t = stack.pop();
			while (!stack.empty() && (stack.peek()).left == t)
				t = stack.pop();
		} else
			for (Tree<K, V, P> t = node.left; t != null; t = t.right)
				stack.push(t);

		return node.key;
	}
}