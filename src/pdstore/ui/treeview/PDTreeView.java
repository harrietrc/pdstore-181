package pdstore.ui.treeview;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nz.ac.auckland.se.genoupe.tools.Debug;

import pdstore.Blob;
import pdstore.GUID;
import pdstore.PDStore;
import pdstore.dal.PDInstance;

public class PDTreeView extends JTree implements TreeExpansionListener,
		TreeWillExpandListener {
	private static final long serialVersionUID = -458975406734331705L;

	public PDStore store;
	JPopupMenu popup;
	public AbstractAction add, remove, newRole, copy,
			findPrev, paste, refresh, rename, findNext, icon;
	public HashSet<GUID> hiddenRoles = new HashSet<GUID>();

	public TreeNode clipBoard;
	public PDTreeRenderer render;

	/**
	 * Creates an editable tree view of instances and roles. The tree view uses
	 * metadata (types and roles) to enable type-safe editing of the data it
	 * shows.
	 * 
	 * @param store
	 *            the store to take the data for the tree view from
	 * @param rootInstances
	 *            the instances that are shown as roots in the tree view.
	 *            Several roots means several trees are shown.
	 */
	public PDTreeView(PDStore store, Object... rootInstances) {
		Debug.assertTrue(rootInstances != null && store != null,
				"The PDTreeView arguments must not be null.");
		this.store = store;
		render = new PDTreeRenderer(store);
		/*
		 * Internally the tree view works only with GUIDs. So if a root instance
		 * is given as DAL object (subclass of PDInstance), then use its GUID
		 * instead.
		 */
		for (int i = 0; i < rootInstances.length; i++) {
			if (rootInstances[i] instanceof PDInstance)
				rootInstances[i] = ((PDInstance) rootInstances[i]).getId();
		}
		PDTreeModel pdTreeModel = new PDTreeModel(rootInstances, this);
		setModel(pdTreeModel);

		popup = new JPopupMenu("Operations");
		// Register popup-menu actions
		rename = new AbstractAction("Rename...") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRename();
			}
		};
		rename.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_F2, 0));
		remove = new AbstractAction("Remove") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRemove();
			}
		};
		remove.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_DELETE, 0));
		copy = new AbstractAction("Copy") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCopy();
			}
		};
		copy.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		paste = new AbstractAction("Paste") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPaste();
			}
		};
		paste.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));

		refresh = new AbstractAction("Refresh") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRefresh();
			}
		};
		refresh.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_F5, 0));
		findNext = new AbstractAction("Find next instance") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doFind(true);
			}
		};
		findNext.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		findPrev = new AbstractAction("Find previous instance") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doFind(false);
			}
		};
		icon = new AbstractAction("Change Icon") {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeIcon();
			}
		};
		findPrev.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK
						| InputEvent.SHIFT_MASK));
		
		setCellRenderer(render);
		addTreeExpansionListener(this);
		addTreeWillExpandListener(this);

		// register mouse listener to handle right-click popup menu interaction
		final PDTreeView pdTreeView = this;
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					setSelectionRow(getRowForLocation(e.getX(), e.getY()));
					TreeNode selected = currentlySelectedNode();
					if (selected != null) {
						((PDTreeNode) selected).contextMenu(pdTreeView,
								e.getX(), e.getY());
					}
				}
			}
		});
	}

	PDTreeModel getPDTreeModel() {
		return (PDTreeModel) getModel();
	}

	public void doRename() {
		TreeNode selected = currentlySelectedNode();
		if (selected != null && selected instanceof InstanceNode) {
			InstanceNode selectedNode = (InstanceNode) selected;
			String oldName = selectedNode.getName();
			String newName = JOptionPane.showInputDialog(PDTreeView.this,
					"Enter new name", oldName);
			if (newName != null && !newName.isEmpty()
					&& !newName.equals(oldName)) {
				getPDTreeModel().rename(selectedNode, newName);
			}
		}
	}

	private void doRemove() {
		TreeNode selected = currentlySelectedNode();
		if (selected != null && selected instanceof InstanceNode) {
			getPDTreeModel().remove((InstanceNode) selected);
		}
	}

	private void doCopy() {
		TreeNode selected = currentlySelectedNode();
		clipBoard = selected;
		if (selected == null || !(selected instanceof PDTreeNode))
			return;

		((PDTreeNode) selected).copy(getPDTreeModel());
	}

	private void doPaste() {
		TreeNode selected = currentlySelectedNode();
		if (selected == null || !(selected instanceof PDTreeNode))
			return;

		((PDTreeNode) selected).paste(getPDTreeModel(), clipBoard);
		doRefresh();
	}

	public void doRefresh() {
		TreeNode selected = currentlySelectedNode();
		// TODO: remove ask-what-kind
		if (selected != null && selected instanceof InstanceNode) {
			getPDTreeModel().refresh((InstanceNode) selected);
		} else if (selected != null && selected instanceof ComplexRoleNode) {
			getPDTreeModel().refresh((RoleNode) selected);
		} else if (selected != null && selected instanceof PrimitiveRoleNode) {
			getPDTreeModel().refresh((PrimitiveRoleNode) selected);
		}
	}
	
	public void refresh(InstanceNode node){
		getPDTreeModel().refresh(node);
	}

	private void doFind(boolean forward) {
		TreePath matchPath = getSelectionPath();
		if (matchPath != null) {
			Object pathEnd = matchPath.getLastPathComponent();
			if (pathEnd instanceof InstanceNode) {
				InstanceNode next = getPDTreeModel().find((InstanceNode) pathEnd,
						forward);
				if (next != null) {
					matchPath = new TreePath(getPDTreeModel().getPathToRoot(
							next));
					setSelectionPath(matchPath);
				} else {
					JOptionPane.showMessageDialog(this,
							"No instance found after this node");
				}
			}
		}
	}
	
	/*
	 * Sets the icon for a node
	 */
	public void changeIcon(){
		JFileChooser choose = new JFileChooser("icons");
		int response = choose.showOpenDialog(this);
		if(response == choose.APPROVE_OPTION){
			File selected = choose.getSelectedFile();
			FileInputStream fileInputStream = null;
			byte[] bFile = new byte[(int) selected.length()];

			try {
				fileInputStream = new FileInputStream(selected);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				fileInputStream.read(bFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Blob image = new Blob(bFile);
			image.setFileName(selected.getName());
			currentlySelectedNode().setIcon(image);
		}
	}

	public PDTreeNode currentlySelectedNode() {
		return getLastNode(getSelectionPath());
	}

	private PDTreeNode getLastNode(TreePath path) {
		if (path != null) {
			Object selected = path.getLastPathComponent();
			if (selected instanceof PDTreeNode) {
				return (PDTreeNode) selected;
			}
		}
		return null;
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {
		// TODO Auto-generated method stub
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {
		PDTreeNode node = getLastNode(event.getPath());
		if (node instanceof TreeWillExpandListener && !(node.getParent() instanceof PrimitiveRoleNode)) {
			((TreeWillExpandListener) node).treeWillExpand(event);
		}
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		// TODO Auto-generated method stub
	}

}
