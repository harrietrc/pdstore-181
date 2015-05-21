package pdstore.ui.treeview;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;

public class PDTreeToolTip extends JToolTip {
	private static final long serialVersionUID = 5752654103079748810L;
	
	// Settings
	public int maxLines; // The maximum number of lines to display in the tooltip
	
	private Popup container; // The container for the tooltip that allows it to be shown or hidden
	
	// Tooltip state
	private TreeNode hoveredNode; // The node that is being hovered over
	public boolean isVisible; // Whether the tooltip is currently visible TODO getter/setter?
	
	// 
	private StringBuilder tipTextBuilder;
	private Set<Object> cyclicalNodeValues;
	
	public PDTreeToolTip() {
		super();
		this.maxLines = 10;
		isVisible = false;

		setTipText("HEY");
	}
	
	/**
	 * TODO extensive javadoc here
	 */
	private void generateTipText(TreeNode node) {
		
		boolean iterateChildren = true; // True if we should recurse over the node's children
		
		// 
		if (node instanceof InstanceNode) {
			InstanceNode instNode = (InstanceNode) node;
			Object nodeValue = instNode.getValue();
			if (cyclicalNodeValues.contains(nodeValue))
				iterateChildren = false;
			if (instNode.isCyclical()) {
				// We need to keep track of whether this node has been encountered in the tree before
				cyclicalNodeValues.add(nodeValue);
			} else {
				// Node has been encountered before; stop recursing, as recursion will be infinite.
				if (cyclicalNodeValues.contains(nodeValue))
					iterateChildren = false;
			}
		}
		
		if (iterateChildren)
			if (node instanceof PDTreeNode) {
				try {
					((PDTreeNode) node).treeWillExpand(null);
				} catch (ExpandVetoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Enumeration children = node.children();
			
			if (children != null) {
				while (children.hasMoreElements()) {
					String nodeText = getTextFromNode(node);
					tipTextBuilder.append(nodeText);
					generateTipText((TreeNode) children.nextElement());
				}
			}
	}
	
	/**
	 * Returns the relevant information from a tree node as a string, taking into account node type.
	 * @return Text that should represent the node in the tooltip.
	 */
	private String getTextFromNode(TreeNode node) {
		String nodeText;
		
		if (node instanceof InstanceNode) {
			InstanceNode instNode = (InstanceNode) node;
			nodeText = instNode.toString();
		} 
		else if (node instanceof PrimitiveRoleNode) {
			PrimitiveRoleNode primNode = (PrimitiveRoleNode) node;
			nodeText = primNode.toString();
		}
		else if (node instanceof ComplexRoleNode) {
			ComplexRoleNode compNode = (ComplexRoleNode) node;
			nodeText = compNode.toString();
		} 
		else if (node instanceof PDRootNode) {
			PDRootNode rootNode = (PDRootNode) node;
			nodeText = rootNode.toString();
		} else {
			nodeText = "other";
		}
		System.out.println(nodeText);
		return nodeText;
	}
	
	/**
	 * Sets the tooltip's text by generating it from the nodes.
	 */
	public void setTipTextFromTree() {
		tipTextBuilder = new StringBuilder();
		cyclicalNodeValues = new HashSet<Object>();
		
		generateTipText(hoveredNode);
		String tipText = tipTextBuilder.toString();
		setTipText(tipText);
	}
	
	public void hide() {
		if (container != null)
			container.hide();
		isVisible = false;
	}
	
	public void show() {
		if (container != null)
			container.show();
		isVisible = true;
	}
	
	public void setToolTipContainer(Popup container) {
		this.container = container;
	}
	
	public Popup getToolTipContainer() {
		return container;
	}
	
	public TreeNode getHoveredNode() {
		return hoveredNode;
	}
	
	public void setHoveredNode(TreeNode node) {
		hoveredNode = node;
	}
	
	// TODO Should this be able to access doExpandNode directly?
	

}
