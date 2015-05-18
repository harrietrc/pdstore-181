package pdstore.ui.treeview;

import java.util.Collection;
import java.util.List;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.TreeNode;

import pdstore.Blob;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListenerAdapter;

public class PrimitiveRoleNode extends RoleNode {
	private Object value;
	private PDTreeModel model;

	public PrimitiveRoleNode(InstanceNode parent, GUID role, String roleName, Object value, final PDTreeModel myModel) {
		this.treeView = myModel.treeView;
		this.parent = parent;
		this.value = value;
		this.role = role;
		this.roleName = roleName;
		this.model = myModel;
		final TreeNode myThis = this;
		final PDTreeModel model = myModel;
		refreshListener = new PDListenerAdapter<GUID, Object, GUID>() {
			@Override
			public void transactionCommitted(
					List<PDChange<GUID, Object, GUID>> transaction,
					List<PDChange<GUID, Object, GUID>> matchedChanges,
					PDCoreI<GUID, Object, GUID> core) {
				doExpandNode(null);
				model.reload(myThis);
			}
			
		};
		
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				null, null, parent.getValue(), role, null);
		model.store.getListenerDispatcher().addListener(refreshListener, changeTemplate);
	}
	
	public GUID getRole() {
		return role;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void paste(PDTreeModel pdTreeModel, TreeNode node) {
		if (pdTreeModel.clipboardItem == null || node instanceof PrimitiveRoleNode){
			return;
		}
		GUID transaction = pdTreeModel.store.begin();	
		//Get the subject of the role to paste under
		Object instance1 = ((InstanceNode)getParent()).getValue();
		//Then link 'em
		pdTreeModel.store.addLink(transaction, instance1, getRole(), pdTreeModel.clipboardItem);
		pdTreeModel.store.commit(transaction);
	}

	@Override
	public void copy(PDTreeModel pdTreeModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doExpandNode(TreeExpansionEvent event) {
		for (InstanceNode node : getComplexNodes()) {
			remove(node);
			model.instanceNodeMap.remove(node.getValue(), node);
		}
		GUID transaction = model.store.begin();
		// TODO: check whether alternative solution can be found
		if(!(getParent() instanceof InstanceNode)) return;
		Collection<Object> i = model.store.getInstances(transaction, 
				((InstanceNode)getParent()).getValue(), getRole());
		model.store.commit(transaction);
		for (Object instance : i) {
			InstanceNode node = new InstanceNode(this, instance, model, treeView);
			
			//Getting the icon and setting it for the node
			transaction = model.store.begin();
			Blob icon = model.store.getIcon(transaction, node.getValue());
			if(icon!=null){
				model.store.setIcon(transaction, node.getValue(), icon);
			}
			model.store.commit(transaction);
			
			if(node.getName()!=null){
				node.setName(node.getName());
			}else{
				node.setName(instance.toString());
			}
			add(node);
		}
	}
}