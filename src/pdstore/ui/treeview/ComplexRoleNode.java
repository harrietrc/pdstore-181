package pdstore.ui.treeview;

import java.util.Collection;
import java.util.List;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.Blob;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListenerAdapter;

public class ComplexRoleNode extends RoleNode {
	PDTreeModel pdTreeModel;
	public boolean isTypeRole = true;
	public PDListenerAdapter<GUID, Object, GUID> refreshListener;
	
	public ComplexRoleNode(InstanceNode parent, GUID role, PDTreeModel model) {
		super(role);
		this.treeView = model.treeView;
		this.parent = parent;
		this.role = role;
		this.roleName = getRoleName(role, model);
		this.pdTreeModel = model;
	    final TreeNode myThis = this;
	    final PDTreeModel myModel = model;
		refreshListener = new PDListenerAdapter<GUID, Object, GUID>() {
			@Override
			public void transactionCommitted(
					List<PDChange<GUID, Object, GUID>> transaction,
					List<PDChange<GUID, Object, GUID>> matchedChanges,
					PDCoreI<GUID, Object, GUID> core) {
				doExpandNode(null);
				Debug.println("role: "+roleName+"!","TreeRefreshListener");	
				myModel.reload(myThis);
			}
			
		};
		
		
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				null, null, parent.getValue(), role, null);
		model.store.getListenerDispatcher().addListener(refreshListener, changeTemplate);

	}

	
	private String  getRoleName(GUID role, PDTreeModel model) {
		String roleName;
		roleName =  model.store.getName(role);
		if(roleName == null) {
			roleName =  model.store.getName(role.getPartner());	
			if(roleName != null) 
				roleName = ">"+roleName;
			else
				roleName = role.toString();				
		}
		return roleName;
	}

	public InstanceNode getChildWithInstance(Object instance) {
		for (InstanceNode i : getInstances()) {
			if (i.getValue() == instance) {
				return i;
			}
		}
		return null;
	}

	/** Reloads the tree node based on its underlying role information 
	 * @param event TODO*/
	@Override
	public void doExpandNode(TreeExpansionEvent event) {
		for (InstanceNode node : getComplexNodes()) {
			remove(node);
			pdTreeModel.instanceNodeMap.remove(node.getValue(), node);
		}
		TreeNode t = new DefaultMutableTreeNode();
		
		GUID transaction = pdTreeModel.store.begin();
		// TODO: check whether alternative solution can be found
		if(!(getParent() instanceof InstanceNode)) return;
		Collection<Object> i = pdTreeModel.store.getInstances(transaction, 
				((InstanceNode)getParent()).getValue(), getRole());
		pdTreeModel.store.commit(transaction);
		for (Object instance : i) {
			InstanceNode node = new InstanceNode(this, instance, pdTreeModel, treeView);
			if(node.getName()==null && instance instanceof String){
				node.setName((String)instance);
			}
			transaction = pdTreeModel.store.begin();
			Blob icon = pdTreeModel.store.getIcon(transaction, node.getValue());
			if(icon!=null){
				pdTreeModel.store.setIcon(transaction, node.getValue(), icon);
			}
			pdTreeModel.store.commit(transaction);
			add(node);
		}
	}

	public void paste(PDTreeModel pdTreeModel,TreeNode node) {
		if (pdTreeModel.clipboardItem == null || node instanceof ComplexRoleNode){
			return;
		}
		GUID transaction = pdTreeModel.store.begin();	
		//Get the subject of the role to paste under
		Object instance1 = ((InstanceNode)getParent()).getValue();
		//Then link 'em
		pdTreeModel.store.addLink(transaction, instance1, getRole(), pdTreeModel.clipboardItem);
		pdTreeModel.store.commit(transaction);
	}

	public void copy(PDTreeModel pdTreeModel) {
		pdTreeModel.clipboardItem = getRole();
	}
	
}
