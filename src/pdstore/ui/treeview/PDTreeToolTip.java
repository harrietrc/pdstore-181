package pdstore.ui.treeview;

import java.awt.Point;

import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.tree.TreeNode;

public class PDTreeToolTip extends JToolTip {
	private static final long serialVersionUID = 5752654103079748810L;
	
	// Settings
	public int maxLines; // The maximum number of lines to display in the tooltip
	
	private Popup container; // The container for the tooltip, that allows it to be shown or hidden
	
	// Tooltip state
	private TreeNode hoveredNode;
	private String text;
	public boolean isVisible; // TODO getter/setter?
	
	public PDTreeToolTip() {
		super();
		this.maxLines = 10;
		this.text = "HEY";
		isVisible = false;

		setTipText(text);
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
