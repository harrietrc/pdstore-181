package pdstore.changeindex.btree;

import pdstore.GUID;
import pdstore.PDChange;

/**
 * Class for representing a change object and a pointer to a child node, as they
 * are found in B-tree nodes to reference child nodes. The pointer
 * childBlockIndex is the file block index of the block that contains changes
 * greater than the given change (and smaller or equal than the change in the
 * next ChangeAndPointer object in the B-tree node).
 * 
 * @author Christof
 * 
 */
class ChangeAndPointer implements Comparable<ChangeAndPointer> {
	PDChange<GUID, Object, GUID> change;
	int childBlockIndex;

	public ChangeAndPointer(PDChange<GUID, Object, GUID> change,
			int childNodeIndex) {
		this.change = change;
		this.childBlockIndex = childNodeIndex;
	}

	@Override
	public int compareTo(ChangeAndPointer o) {
		return change.compareTo(o.change);
	}

	@Override
	public String toString() {
		return change.getTransaction().toString().substring(0,2) + "/" + childBlockIndex;
	}
}