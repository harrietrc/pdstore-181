package pdstore.ui.treeview;

import javax.swing.tree.TreeNode;

import pdstore.Blob;

import nz.ac.auckland.se.genoupe.tools.DynamicNode;

public abstract class PDTreeNode extends DynamicNode implements TreeNode {

	PDTreeView treeView;

	public PDTreeNode() {
		super();
	}

	public PDTreeNode(Object userObject) {
		super(userObject);
	}

	public PDTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public abstract void paste(PDTreeModel pdTreeModel, TreeNode node);

	public abstract void contextMenu(PDTreeView pdTreeView, int x, int y);

	public abstract void copy(PDTreeModel pdTreeModel);
	
	public abstract void setIcon(Blob image);

}