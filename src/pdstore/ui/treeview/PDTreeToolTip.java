package pdstore.ui.treeview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.Timer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;

public class PDTreeToolTip extends JToolTip {
	private static final long serialVersionUID = 5752654103079748810L;
	
	private static final int INITIAL_MAX_DEPTH = 1; // Start off showing only the parent node and its first level children
	private static final int HARD_MAX_DEPTH = 4; // A hard cap on the maximum depth of traversal for the tree.
	// TODO This is arbitrary - it would be better to set it based on the size of the window.
	
	// Settings
	private int maxDepth; // The maximum number of lines to display in the tooltip
	// TODO If this is unlimited, infinite recursion will occur - not ideal and should be fixed (or enforced).
	
	private Popup container; // The container for the tooltip that allows it to be shown or hidden
	
	// Tooltip state
	private TreeNode hoveredNode; // The node that is being hovered over
	public boolean isVisible; // Whether the tooltip is currently visible 
	private StringBuilder tipTextBuilder; // Tooltip text - added to as subtree is traversed.
	
	private Timer hoverTimer; // Times how long the user hovers over a component and refreshes the tooltip 
							  // with more data at set intervals
	
	public PDTreeToolTip() {
		super();
		this.maxDepth = INITIAL_MAX_DEPTH; 
		isVisible = false;
		setTipText("<html></html>");
	}
	
	/**
	 * TODO extensive javadoc here
	 */
	private void generateTipText(TreeNode node, int currentDepth) {
		// Children may be null - expand the list of children so that the subtree can be traversed.
		if (node instanceof PDTreeNode) {
			try {
				((PDTreeNode) node).treeWillExpand(null);
			} catch (ExpandVetoException e) {
				e.printStackTrace();
			}
		}
		
		// Add a representation of this node to the tooltip
		String nodeText = getTextFromNode(node);
		for (int i=0; i< currentDepth; i++) // Add a suitable indent
			tipTextBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		tipTextBuilder.append("  " + nodeText);
		
		Enumeration children = node.children();
		
		// Recurse through the subtree getting representations of the nodes until the maximum depth is reached.
		if (children != null) {
			if (currentDepth < Math.min(maxDepth, HARD_MAX_DEPTH)) {
				tipTextBuilder.append("<br>");
				currentDepth++;
				while (children.hasMoreElements()) {
					generateTipText((TreeNode) children.nextElement(), currentDepth);
				}
			} else {
				tipTextBuilder.append( " (...)<br>"); // Supposed to signify hidden children
			}
		}
	}
	
	/**
	 * Returns the relevant information from a tree node as a string, taking into account node type.
	 * @return Text that should represent the node in the tooltip.
	 */
	private String getTextFromNode(TreeNode node) {
		String nodeText = null;
		
		if (node instanceof InstanceNode) {
			InstanceNode instNode = (InstanceNode) node;
			nodeText = instNode.toStringNoHtmlTags();
		} 
		else if (node instanceof RoleNode) {
			RoleNode compNode = (RoleNode) node;
			nodeText = compNode.toStringNoHtmlTags();
		} 

		return nodeText;
	}
	
	/**
	 * Sets the tooltip's text by generating it from the nodes. JToolTip allows formatting through HTML, so
	 * the text is marked up using HTML.
	 */
	private void setTipTextFromTree() {
		tipTextBuilder = new StringBuilder("<html>");
		generateTipText(hoveredNode, 0); // Recurses through the subtree, adding node information
		tipTextBuilder.append("</html>");
		
		String tipText = tipTextBuilder.toString();
		setTipText(tipText);

		container.show(); // Refresh the container size
	}
	
	/**
	 * Hides the tooltip and stops the timer that controls when new information is added. The detail of
	 * information to show is also reset.
	 */
	public void hide() {
		if (hoverTimer != null)
			hoverTimer.stop();
		
		maxDepth = INITIAL_MAX_DEPTH; // Reset the traversal depth
		
		if (container != null)
			container.hide();
		isVisible = false;
	}
	
	/**
	 * Show the tooltip after a delay. New information is added to the tooltip at regular intervals.
	 */
	public void show() {	
		// Timer that fires every 5 seconds, adding new information to the tooltip. An extra level of depth
		// from the subtree is added each time the timer fires.
		hoverTimer = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTipTextFromTree(); // Regenerate the tooltip text
				maxDepth++; // Allow traversal of the subtree to extend a level deeper
			}
		});
		hoverTimer.setInitialDelay(1000);
		hoverTimer.start();
		
		isVisible = true;
	}
	
	public void setToolTipContainer(Popup container) {
		this.container = container;
	}
	
	public void setHoveredNode(TreeNode node) {
		hoveredNode = node;
	}
}
