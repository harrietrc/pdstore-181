package nz.ac.auckland.se.genoupe.tools;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;

/**
 * Implements the basic natural logic of a dynamically expanding treeNode: The
 * elements of a node are loaded the first time it is expanded. This behavior is
 * a two-state machine and this class implements the correct state changes so
 * that this correct behavior is inherited by the subclasses. The state is
 * represented by the boolean field wasExpandedBefore.
 * 
 * Subclasses should implement the doExpandNode method Does currently not
 * implement any logic for collapsing because this is quite tricky and probably
 * heuristics have to be used.
 * 
 * @author gweb017
 * 
 */
public abstract class DynamicNode extends DefaultMutableTreeNode implements
		TreeWillExpandListener {

	private boolean wasExpandedBefore = false;

	public boolean wasExpandedBefore() {
		return wasExpandedBefore;
	}

	public DynamicNode() {
		super();
	}

	public DynamicNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public DynamicNode(Object userObject) {
		super(userObject);
	}

	public void setWasExpandedBefore(boolean wasExpandedBefore) {
		this.wasExpandedBefore = wasExpandedBefore;
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeWillExpand(TreeExpansionEvent arg0)
			throws ExpandVetoException {
		if (!wasExpandedBefore) {
			doExpandNode(arg0);
			wasExpandedBefore = true;
		}
	}

	/**
	 * Subclasses should overwrite this method and perform in its implementation
	 * all necessary steps before the first expansion. This will typically be
	 * the dynamically loading of the child nodes.
	 * 
	 * @param event
	 */
	public abstract void doExpandNode(TreeExpansionEvent event);

	/**
	 * Adds the given child node to this node.
	 * 
	 * Note that it does not remove the child from any node it might have been a
	 * child of before; this has to be done manually.
	 * 
	 * This method overrides the API add method, which is broken as it does not
	 * allow to add new nodes without a parent.
	 */
	@Override
	public void add(MutableTreeNode newChild) {
		newChild.setParent(null);
		insert(newChild, getChildCount());
	}
}
