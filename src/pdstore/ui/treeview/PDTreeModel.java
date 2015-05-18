package pdstore.ui.treeview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * TreeModel implementation for representing PDStore objects and links as tree
 * nodes. Allows support for editing and dynamic updates to view.
 * 
 * */
public class PDTreeModel extends DefaultTreeModel implements
		PDListener<GUID, Object, GUID> {
	private static final long serialVersionUID = 122201688579380771L;

	PDTreeView treeView;

	// Settings
	public boolean showUnusedRoles = false;

	public boolean isShowUnusedRoles() {
		return showUnusedRoles;
	}

	public void setShowUnusedRoles(boolean notShowUnused) {
		this.showUnusedRoles = notShowUnused;
	}

	public PDStore store;
	// private Object[] rootInstances;
	Object clipboardItem;

	// Store the mapping from each instance to all TreeNodes that represent it
	public Multimap<Object, InstanceNode> instanceNodeMap = LinkedHashMultimap.create();
	Multimap<Object, RoleNode> roleNodeMap = LinkedHashMultimap.create();
		
	public PDTreeModel(Object[] rootInstances, PDTreeView treeView) {
		super(new PDRootNode("<html><font color='#00308d'>PDStore</font></html>"));
		// this.rootInstances = rootInstances;
		this.treeView = treeView;
		this.store = treeView.store;
		// Set up the root instances by creating a tree node for each
		for (Object rootInstance : rootInstances) {
			PDRootNode rootNode = (PDRootNode) root;

			// Create a tree node
			InstanceNode node = new InstanceNode(rootNode, rootInstance, this,
					treeView);
			rootNode.add(node);
			// register this node with the underlying model (used for model
			// updates)
			instanceNodeMap.put(rootInstance, node);
			// recursively load children
			node.doExpandNode(null);
		}

		// Not utilised at the moment... Would be nice if it was!
		treeView.store.getListenerDispatcher().addListener(this,
				new PDChange<GUID, Object, GUID>(null, null, null, null, null));
	}

	/**
	 * Invoked whenever a change is made to the underlying PDStore model.
	 * Updates the tree structure to reflect the new model
	 */
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges,
			PDCoreI<GUID, Object, GUID> core) {
		// TODO not used at the moment. Updates to treeview are done
		// "manually"...
	}

	public void rename(InstanceNode node, String newName) {
		// For complex types, just "rename"
		GUID transaction = treeView.store.begin();
		treeView.store.setName(transaction, node.getValue(), newName);
		treeView.store.commit(transaction);
		// Update affected tree nodes
		reloadNodesPertainingTo(node.getValue());
	}
	
	public void rename(RoleNode node, String newName) {
		GUID transaction = treeView.store.begin();
		treeView.store.setName(transaction, node.getRole(), newName);
		treeView.store.commit(transaction);
		// Update affected tree node
		refresh((InstanceNode)node.getParent());
	}
	
	public void changeValue(PrimitiveRoleNode node, Object newValue) {
		// for primitive types, a bit tricky - link the name role to the new
		// string
		TreeNode parent = node.getParent();
		if (parent == null || !(parent instanceof InstanceNode)) {
			// this theoretically shouldn't be true!!
			return;
		}
		// Get the role for which this primitive is the object; then the subject
		// for that role
		Object parentInstance = ((InstanceNode) node.getParent()).getValue();
		GUID transaction = treeView.store.begin();
		// Unlink the primitive from the role and replace it with the new
		// primitive
		if (node.getValue() != null)
			treeView.store.removeLink(transaction, parentInstance, node.getRole(),
					node.getValue());
		treeView.store.addLink(transaction, parentInstance, node.getRole(), newValue);
		treeView.store.commit(transaction);
		// Update affected tree nodes
		reloadNodesPertainingTo(parentInstance);
	}

	public void remove(InstanceNode node) {
		TreeNode parent = node.getParent();
		// "remove" action doesn't apply to the top-most instance node (which
		// has PDRootNode as parent)
		if (parent == null || !(parent instanceof RoleNode))
			return;
		// Get the role for which this node is the object; then the subject for
		// that role
		RoleNode parentRoleNode = (RoleNode) parent;
		Object parentInstance = ((InstanceNode) parentRoleNode.getParent())
				.getValue();
		GUID parentRole = parentRoleNode.getRole();
		GUID transaction = treeView.store.begin();
		// unlink the object from the role
		treeView.store.removeLink(transaction, parentInstance, parentRole,
				node.getValue());
		treeView.store.commit(transaction);
		reloadNodesPertainingTo(node.getValue());
	}

	public void add(RoleNode node, Object object) {
		GUID role = node.getRole();
		GUID transaction = treeView.store.begin();
		InstanceNode parent = (InstanceNode) node.getParent();
		
		// Create a new instance and register it
		Object newInstance = new GUID();
		if(object==null){
			// - determine the type of the object of the role (i.e. the instance to
			// be created)
			GUID type = treeView.store.getOwnerType(transaction, role);
			treeView.store.setType(transaction, newInstance, type);
		}else{
			treeView.store.setType(transaction, newInstance, (GUID)object);
		}
		// then link it to the role
		Object instance1 = ((InstanceNode) node.getParent()).getValue();
		treeView.store.addLink(transaction, instance1, role, newInstance);
		treeView.store.commit(transaction);
		// Update affected tree nodes
		reloadNodesPertainingTo(instance1);
		
		//Setting the default name for the new instance node
		InstanceNode newNode = new InstanceNode(node.getParent(), newInstance, this, treeView);
		newNode.setParent((TreeNode)node);
		node.setParent(parent);

		if(newNode.getTypeName().equals("Boolean")){
			transaction = treeView.store.begin();
			treeView.store.setName(transaction, newNode.getValue(), "false");//Sets default value
			newNode.setName("false");
			treeView.store.commit(transaction);
			newNode.booleanWidget(newNode);
		}else if(newNode.getTypeName().equals("Integer")){
			transaction = treeView.store.begin();
			treeView.store.setName(transaction,newNode.getValue(),"0");
			newNode.setName("0");
			treeView.store.commit(transaction);
			newNode.integerWidget(newNode);
		}else if(newNode.getTypeName().equals("Double")){
			transaction = treeView.store.begin();
			treeView.store.setName(transaction,newNode.getValue(),"0.0");
			newNode.setName("0.0");
			treeView.store.commit(transaction);
			newNode.doubleWidget(newNode);
		}else{
			transaction = treeView.store.begin();
			if(object==null){
				treeView.store.setName(transaction, newNode.getValue(), "New "+newNode.getTypeName());
			}else{
				String name = treeView.store.getName(transaction, object);
				treeView.store.setName(transaction, newNode.getValue(), "New "+name);
			}
			treeView.store.commit(transaction);
			newNode.nameInstance(newNode);//New node would get a name upon prompt
		}
		reloadNodesPertainingTo(instance1);
	}

	public InstanceNode find(InstanceNode node, boolean forward) {
		// Basically a brute-force search for the next node
		TreeNode[] path = getPathToRoot(node);
		Object instance = node.getValue();
		// "bubble" the search up the ancestry chain, starting from immediate
		// parent
		for (int i = path.length - 2; i > 0; i--) {
			TreeNode parent = path[i - 1];
			TreeNode child = path[i];
			InstanceNode next = findChild(parent, parent.getIndex(child)
					+ (forward ? 1 : -1), forward, instance);
			if (next != null)
				return next;
		}
		return null;
	}

	private InstanceNode findChild(TreeNode root, int beginningChildIndex,
			boolean forward, Object match) {
		if (root instanceof InstanceNode) {
			InstanceNode node = (InstanceNode) root;
			if (node.getValue().equals(match))
				return node;
		}
		int childCount = root.getChildCount();
		for (int i = beginningChildIndex; (forward && i < childCount)
				|| (!forward && i >= 0); i += (forward ? 1 : -1)) {
			TreeNode childAt = root.getChildAt(i);
			InstanceNode matched = findChild(childAt,
					forward ? 0 : childAt.getChildCount() - 1, forward, match);
			if (matched != null)
				return matched;
		}
		return null;
	}

	public Object getClipboardItem() {
		return clipboardItem;
	}

	public void refresh(InstanceNode selected) {
		selected.doExpandNode(null);
		reload(selected);
	}

	public void refresh(RoleNode selected) {
		selected.doExpandNode(null);
		reload(selected);
	}

	public void refresh(PrimitiveRoleNode selected) {
		selected.doExpandNode(null);
		reload(selected);
	}

	private void reloadNodesPertainingTo(Object value) {
		Collection<InstanceNode> nodes = new ArrayList<InstanceNode>(
				instanceNodeMap.get(value));
		for (InstanceNode node : nodes) {
			node.doExpandNode(null);
			reload(node);
		}
	}

	private void reloadNodesPertainingTo(GUID complexRole) {
		Collection<RoleNode> nodes = new ArrayList<RoleNode>(
				roleNodeMap.get(complexRole));
		for (RoleNode node : nodes) {
			node.doExpandNode(null);
			reload(node);
		}
	}

	Set<PDChange<GUID, Object, GUID>> matchingTemplates = new HashSet<PDChange<GUID, Object, GUID>>();

	@Override
	public Set<PDChange<GUID, Object, GUID>> getMatchingTemplates() {
		// TODO Auto-generated method stub
		return matchingTemplates;
	}

}
