package replica;

import java.util.*;

/**
 * A generic utility class for trees of arbitrary and variable branching factor and depth.
 * For ease of implementation, equality testing requires that order be equivalent
 * (although there is a known test for isomorphism [Kelly 1957])
 * This behaviour should not be relied on.
 * 
 * Note that there is presently no treatment for cycles (a theoretically correct solution would require the isomorphism check above).
 * At present, these will induce overflow.
 * 
 * @author Michael Fowler
 *
 * @param <T>
 */
public class GenTree<T> {
	
	private T value;
	private List<GenTree<T>> children = new ArrayList<GenTree<T>>();
	
	/**
	 * Constructs a new GenTree with the given initial value.
	 * @param value
	 */
	public GenTree(T value) {
		this.value = value;
	}
	
	public Collection<GenTree<T>> getChildren() {
		return children;
	}
	
	public void addChild(GenTree<T> child) {
		children.add(child);
	}
	
	public void removeChild(GenTree<T> child) {
		children.remove(child);
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		@SuppressWarnings("unchecked") // We early exit above
		GenTree<T> t = (GenTree<T>) o;
		
		boolean match = value.equals(t.value);
		match = match && (children.size() == t.children.size());
		if (!match) return false; // Secondary early abort (avoid NPE below)
		
		// Now recursively match children
		for (int i=0; i < children.size(); i++) {
			boolean submatch = getChild(i).equals(t.getChild(i));
			
			match = match && submatch;
			if (!match) break;
		}
		
		return match;
	}
	
	// Exposes ordering (and avoiding that is preferable)
	private GenTree<T> getChild(int i) {
		return children.get(i);
	}
}
