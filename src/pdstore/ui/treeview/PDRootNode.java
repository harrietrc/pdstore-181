package pdstore.ui.treeview;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class PDRootNode implements TreeNode {
	private String name;
	private List<InstanceNode> rootNodes = new ArrayList<InstanceNode>();
	
	public PDRootNode(String name) {
		this.name = name;
	}

	public void add(InstanceNode node) {
		rootNodes.add(node);
		node.setParent(this);
	}
	
	public void remove(InstanceNode node) {
		rootNodes.remove(node);
		node.setParent(null);
	}
	
	@Override
	public Enumeration<?> children() {
		return new Vector<InstanceNode>(rootNodes).elements();
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return rootNodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return rootNodes.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return rootNodes.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
