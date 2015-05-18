package pdstore.ui.treeview;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;

import pdstore.Blob;
import pdstore.GUID;
import pdstore.PDStore;
import pdstore.notify.PDListenerAdapter;

public abstract class RoleNode extends PDTreeNode {

	protected GUID role;
	protected String roleName;
	protected List<InstanceNode> instances = new ArrayList<InstanceNode>();
	public PDListenerAdapter<GUID, Object, GUID> refreshListener;
	public boolean isLeaf = false;
	public boolean visible = true;

	public RoleNode() {
		super();
	}

	public RoleNode(Object userObject) {
		super(userObject);
	}

	public RoleNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public GUID getRole() {
		return role;
	}

	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String name){
		roleName = name;
	}

	public void contextMenu(final PDTreeView pdTreeView, int x, int y) {
		pdTreeView.popup.removeAll();
		
		//Getting the subtypes
		boolean subTypesAvailable = true;
		GUID transaction = treeView.store.begin();
		GUID role = getRole();
		Object typeID = treeView.store.getOwnerType(transaction, role);//ID of Type
		GUID subTypeID = treeView.store.getId(transaction, "subtype");//ID of subtype folder
		Collection<Object>newRoles = treeView.store.getInstances(transaction, typeID, subTypeID);//Retrieving the subtypes
		if(newRoles.isEmpty()){//Checking for availability of subtypes
			subTypesAvailable = false;
		}
		treeView.store.commit(transaction);

		//Getting the name 
		transaction = pdTreeView.store.begin();
		String typeName;
		if(typeID==null) {
			// If the role has no owner type, the type is Object"
			typeName = "Object";
		} else {
			typeName = pdTreeView.store.getName(transaction, typeID);
		}
		pdTreeView.store.commit(transaction);

		//Adding to the right-click menu
		//If there aren't any subtypes then just have single option of adding the primary type
		if(!subTypesAvailable){
			pdTreeView.add = new AbstractAction("Add") {
				@Override
				public void actionPerformed(ActionEvent e) {
					doAdd(pdTreeView,null);
				}
			};
			pdTreeView.popup.add(pdTreeView.add);
			pdTreeView.add.putValue(Action.NAME, "Add new '" + typeName + "'...");
		}else{//Otherwise if subtypes are available, add them as options along with primary type
			JMenu menu = new JMenu("Add new");
			AbstractAction option = new AbstractAction("\'"+typeName+"\'...") {
				@Override
				public void actionPerformed(ActionEvent e) {
					doAdd(pdTreeView,null);
				}
			};
			menu.add(option);
			
			for(final Object object: newRoles){
				transaction = treeView.store.begin();
				String name = treeView.store.getName(transaction, object);
				treeView.store.commit(transaction);
				option = new AbstractAction("\'"+name+"\'...") {
					@Override
					public void actionPerformed(ActionEvent e) {
						doAdd(pdTreeView,object);
					}
				};
				menu.add(option);
			}
			pdTreeView.popup.add(menu);
		}

		AbstractAction rename = new AbstractAction("Rename...") {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRename(pdTreeView);
			}
		};

		pdTreeView.popup.add(pdTreeView.copy);
		pdTreeView.popup.add(pdTreeView.paste);
		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(pdTreeView.refresh);
		pdTreeView.popup.add(rename);
		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(pdTreeView.icon);

		if(this instanceof ComplexRoleNode){
			final RoleNode roleNode = this;
			AbstractAction visibility = new AbstractAction("Hide this role everywhere") {
				@Override
				public void actionPerformed(ActionEvent e) {
					pdTreeView.hiddenRoles.add(roleNode.getRole());
					pdTreeView.refresh((InstanceNode)(roleNode.getParent()));
				}
			};
			pdTreeView.popup.addSeparator();
			pdTreeView.popup.add(visibility);
		}

		// Restrict paste based on type of clipboard item (must equal to the
		// expected type)
		pdTreeView.paste.setEnabled(false);
		Object clipboardItem = pdTreeView.getPDTreeModel().getClipboardItem();
		if (clipboardItem != null) {
			transaction = pdTreeView.store.begin();
			String clipboardItemName = pdTreeView.store.getName(transaction,
					clipboardItem);
			if(clipboardItemName==null){
				clipboardItemName = clipboardItem.toString();
			}
			GUID clipboardItemType = pdTreeView.store.getType(transaction,
					clipboardItem);
			String clipboardItemTypeName = pdTreeView.store.getName(transaction,
					clipboardItemType);
			pdTreeView.store.commit(transaction);
			pdTreeView.paste.setEnabled(typeID.equals(clipboardItemType));
			pdTreeView.paste.putValue(Action.NAME, "Paste " + clipboardItemTypeName
					+ " '" + clipboardItemName + "'");
		}
		pdTreeView.popup.show(pdTreeView, x, y);
	}

	public List<InstanceNode> getInstances() {
		return instances;
	}

	public void setInstances(List<InstanceNode> instances) {
		this.instances = instances;
	}

	private void doAdd(PDTreeView pdTreeView, Object object) {
		TreeNode selected = pdTreeView.currentlySelectedNode();
		if (selected != null && selected instanceof RoleNode) {
			RoleNode selectedRoleNode = (RoleNode) selected;
			// Create a new instance and register it
			GUID transaction = pdTreeView.store.begin();
			// - determine the type of the owner of the role (i.e. the instance
			// to be created)
			GUID type = pdTreeView.store.getOwnerType(transaction,
					selectedRoleNode.getRole());
			pdTreeView.store.commit(transaction);
			pdTreeView.getPDTreeModel().add(selectedRoleNode, object);
		}
	}

	private void doRename(PDTreeView pdTreeView) {
		TreeNode selected = pdTreeView.currentlySelectedNode();
		if (selected != null && selected instanceof RoleNode) {
			RoleNode selectedNode = (RoleNode) selected;
			String oldName = selectedNode.getRoleName();
			String newName = JOptionPane.showInputDialog(pdTreeView,
					"Enter new name", oldName);
			if (newName != null && !newName.isEmpty()
					&& !newName.equals(oldName)) {
				pdTreeView.getPDTreeModel().rename(selectedNode, newName);
			}
		}
	}

	@Override
	public String toString() {
		final GUID role = getRole();
		GUID transaction = treeView.store.begin();
		GUID objectType = treeView.store.getOwnerType(transaction, role);
		String typeName;
		if(objectType == null) {
			// If the role has no owner type, the type is Object"
			typeName = "Object";
		} else {
			typeName = treeView.store.getName(transaction, objectType);
		}
		treeView.store.commit(transaction);
		return "<html><font color='#00308d'>"+roleName+"</font><font color='#b48484'>:"+typeName+"</font></html>";  
	}
	
	@Override
	public Enumeration<?> children() {
		return new Vector<InstanceNode>(getInstances()).elements();
	}
	
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	
	@Override
	public TreeNode getChildAt(int childIndex) {
		return getInstances().get(childIndex);
	}

	@Override
	public int getChildCount() {
		return getInstances().size();
	}
	
	@Override
	public int getIndex(TreeNode node) {
		return getInstances().indexOf(node);
	}
	 
	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	public void add(InstanceNode node) {
		getInstances().add(node);
		node.setParent(this);
	}
	
	public List<InstanceNode> getComplexNodes() {
		return new ArrayList<InstanceNode>(getInstances());
	}
	
	public void remove(InstanceNode node) {
		getInstances().remove(node);
		node.setParent(null);
	}
	
	public void setIcon(Blob image){
		GUID transaction = treeView.store.begin();
		treeView.store.setIcon(transaction,getRole(), image);
		treeView.store.commit(transaction);
		treeView.getPDTreeModel().refresh((InstanceNode)getParent());
		treeView.setCellRenderer(treeView.render);
	}

}