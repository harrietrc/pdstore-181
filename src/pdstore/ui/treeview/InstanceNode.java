package pdstore.ui.treeview;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.TreeNode;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.Blob;
import pdstore.GUID;
import pdstore.PDStore;
import book.ExampleTreeViewer;

/**
 * A tree node that represents a complex type. Displays relationships as child
 * nodes.
 * */
public class InstanceNode extends PDTreeNode implements TreeNode {

	private String instanceName;
	private GUID instanceType;
	private String typeName;
	private TreeNode parent;
	private boolean isCyclical;
	private JCheckBoxMenuItem toggle = new JCheckBoxMenuItem("Hide Unused Roles");
	private PDTreeModel model;

	public InstanceNode(TreeNode parent, Object value, PDTreeModel model, PDTreeView treeView) {
		super(value, true);
		this.parent = parent;
		this.model = model;
		GUID transaction = treeView.store.begin();
		this.instanceName = treeView.store.getName(transaction, value);
		instanceType = treeView.store.getType(transaction, value);
		this.typeName = treeView.store.getName(transaction, instanceType);
		treeView.store.commit(transaction);
		toggle.setState(true);// Check box menu item is checked initially
		this.treeView = treeView;
		// Check if this instance is already present in one of the ancestor
		// nodes
		// in order to prevent infinite recursion
		isCyclical = true;
		for (TreeNode currentParent = parent; currentParent != null; currentParent = currentParent
				.getParent()) {
			if (currentParent instanceof InstanceNode
					&& value.equals(((InstanceNode) currentParent).getValue())) {
				isCyclical = false;
				break;
			}
		}

		model.instanceNodeMap.put(value, this);
		
		// register checkbox menu item to handle check and uncheck
		final PDTreeView view = treeView;
		ItemListener listener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (toggle.isSelected()) {
					view.getPDTreeModel().setShowUnusedRoles(false);
				} else {
					view.getPDTreeModel().setShowUnusedRoles(true);
				}
				view.doRefresh();
			}
		};
		toggle.addItemListener(listener);
	}

	public List<ComplexRoleNode> getComplexRoleNodes() {
		ArrayList<ComplexRoleNode> list = new ArrayList<ComplexRoleNode>();
		List<PDTreeNode> nodes = Collections.list(children());

		for (Object node : nodes) {
			if (node instanceof ComplexRoleNode) {
				list.add((ComplexRoleNode) node);
			}
		}
		return list;
	}

	public void removePrimitiveRoleNode(PrimitiveRoleNode node) {
		node.setParent(this);
		node.removeFromParent();
	}

	public List<PrimitiveRoleNode> getPrimitiveRoleNodes() {
		ArrayList<PrimitiveRoleNode> list = new ArrayList<PrimitiveRoleNode>();
		List<PDTreeNode> nodes = Collections.list(children());

		for (Object node : nodes) {
			if (node instanceof PrimitiveRoleNode) {
				list.add((PrimitiveRoleNode) node);
			}
		}
		return list;
	}

	public boolean isCyclical() {
		return this.isCyclical;
	}

	@Override
	public boolean getAllowsChildren() {
		// The following definition is not used anymore
		// since it did not work and would be anyway too patronizing.
		// isCycle is the function used by PDTreeRenderer
		// return canLoadChildren;
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {// IMPORTANT(Defines the order of the nodes)
		if (childIndex < getPrimitiveRoleNodes().size()) {
			return getPrimitiveRoleNodes().get(childIndex);
		}
		return getComplexRoleNodes().get(
				childIndex - getPrimitiveRoleNodes().size());
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		// TODO: old cycle detection is deactivated,
		// turn that into a pure information, not a mandatory leaf.
		// return primitiveRoleNodes.isEmpty() && complexRoleNodes.isEmpty();
		//return false;
		
		//Avoiding a false postive scenario where the
		//user assumes a node has children
		if(this.parent instanceof PrimitiveRoleNode){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String text = "<html><font color='#00308d'>"+(instanceName != null ? instanceName: "")+"</font>" +   
		"<font color='#b48484'>:"+typeName+"</font></html>"; 
		return text;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getName() {
		return instanceName;
	}

	public void setName(String name) {
		instanceName = name;
	}

	public Object getValue() {
		return this.getUserObject();
	}

	public void setValue(Object val) {
		this.setUserObject(val);
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	private void clean() {
		for (RoleNode node : getComplexRoleNodes()) {
			node.removeFromParent();
			model.roleNodeMap.remove(node.getRole(), node);
		}
		for (PrimitiveRoleNode node : getPrimitiveRoleNodes()) {
			removePrimitiveRoleNode(node);
			model.roleNodeMap.remove(node.getRole(), node);
		}
		GUID transaction = model.treeView.store.begin();
		setName(model.treeView.store.getName(transaction, getValue()));
		model.treeView.store.commit(transaction);
	}

	@Override
	public void doExpandNode(TreeExpansionEvent event) {
		
		//Initially handling toggling
		if (toggle.isSelected()) {
			model.setShowUnusedRoles(false);
		} else {
			model.setShowUnusedRoles(true);
		}
		
		String nodeName = getName();
		GUID transaction;
		clean();
		if (getParent() instanceof PrimitiveRoleNode)
			return;
		transaction = model.treeView.store.begin();
		GUID type = model.treeView.store.getType(transaction, getValue());
		// Recursively add child nodes based on accessible roles
		Collection<GUID> accessibleRoles = model.treeView.store
				.getAccessibleRoles(transaction, type);
		HashSet<GUID> set = new HashSet<GUID>();
		set.addAll(accessibleRoles);
		model.treeView.store.commit(transaction);
		for (GUID role : accessibleRoles) {
			transaction = model.treeView.store.begin();
			GUID childType = model.treeView.store.getOwnerType(transaction,
					role);
			String roleName = model.treeView.store.getName(transaction, role);
			model.treeView.store.commit(transaction);
			Object isPrimitive = model.treeView.store.getInstance(transaction,
					childType, PDStore.ISPRIMITIVE_ROLEID);
			if (isPrimitive == null) {
				Debug.println("nullrole: " + roleName,
						ExampleTreeViewer.EXAMPLE_TREE_VIEWER);
				// TODO : the Treeview still sometimes creates infinite
				// recursions.
				// In the pdtreeView.store.examples.book dataset the following
				// line cuts
				// the recursion
				// however this is not well understood and just a workaround.
				// This line creates faulty behavior in
				// pdtreeView.store.java.JavaASTTreeViewer.
				// return;
			}
			if (isPrimitive != null && (Boolean) isPrimitive) {
				addPrimitiveRoleNode(role, roleName);
			} else {
				if (!model.isShowUnusedRoles()) { // check whether role is used
					Collection<Object> instances = model.treeView.store
							.getInstances(transaction, getValue(), role);
					if (instances.isEmpty()){
						continue;
					}
				}
				// Otherwise, add a role node, which in turn will recursively
				// populate children
				ComplexRoleNode node = new ComplexRoleNode(this, role, model);
				
				//Getting the icon and setting it for the node
				Blob icon = model.store.getIcon(transaction, node.getRole());
				if(icon!=null){
					model.store.setIcon(transaction, node.getRole(), icon);
				}

				//Avoiding a false postive scenario where the
				//user assumes a node has children
				Collection<Object> instances = model.treeView.store
						.getInstances(transaction, getValue(), role);
				if (instances.isEmpty()){
					node.isLeaf = true;
				}
				
				//Checks the visibility status of the role to decide if it should
				//be rendered
				if(!treeView.hiddenRoles.contains(node.getRole())){
					add(node);
					model.roleNodeMap.put(role, node);
				}
			}
		}

		// Add roles that are not defined in the type, but used on this objects.
		transaction = model.treeView.store.begin();
		Collection<Object> usedRoles = treeView.store.getInstances(transaction,
				getValue(), PDStore.USES_ROLE_ROLEID);
		model.treeView.store.commit(transaction);
		for (Object roleObject : usedRoles) {
			GUID role = (GUID) roleObject;
			if (set.contains(role))
				continue;
			transaction = model.treeView.store.begin();
			model.treeView.store.commit(transaction);
			if (!model.isShowUnusedRoles()) { // check whether role is used
				Collection<Object> instances = model.treeView.store
						.getInstances(transaction, getValue(), role);
				if (instances.isEmpty()){
					continue;
				}
			}
			// Otherwise, add a role node, which in turn will recursively
			// populate children
			ComplexRoleNode node = new ComplexRoleNode(this, role, model);
			node.isTypeRole = false;
			
			//Getting the icon and setting it for the node
			Blob icon = model.store.getIcon(transaction, node.getRole());
			if(icon!=null){
				model.store.setIcon(transaction, node.getRole(), icon);
			}
			
			//Avoiding a false postive scenario where the
			//user assumes a node has children
			Collection<Object> instances = model.treeView.store
					.getInstances(transaction, getValue(), role);
			if (instances.isEmpty()){
				node.isLeaf = true;
			}
			
			//Checks the visibility status of the role to decide if it should
			//be rendered
			if(!treeView.hiddenRoles.contains(node.getRole())){
				add(node);
				model.roleNodeMap.put(role, node);
			}
		}
		this.setName(nodeName);
	}

	private void addPrimitiveRoleNode(GUID role, String roleName) {
		GUID transaction;
		transaction = model.treeView.store.begin();
		// If primitive type role, then add special node
		Object child = model.treeView.store.getInstance(transaction,
				getValue(), role);
		// Note this will only get single instance
		// @TODO revisit the assumption that primitive-type relationships are
		// one-to-one
		PrimitiveRoleNode node = new PrimitiveRoleNode(this, role, roleName,
				child, model);
		
		//Getting the icon and setting it for the node
		Blob icon = model.store.getIcon(transaction, node.getRole());
		if(icon!=null){
			model.store.setIcon(transaction, node.getRole(), icon);
		}
		model.treeView.store.commit(transaction);
		
		//Avoiding a false postive scenario where the
		//user assumes a node has children
		if(!model.isShowUnusedRoles() && child!=null) { // check whether role is used
			add(node);
			model.roleNodeMap.put(node.getRole(), node);
		}else if(model.isShowUnusedRoles()){
			if(child==null){
				node.isLeaf = true;
			}
			add(node);
			model.roleNodeMap.put(node.getRole(), node);
		}
	}

	/**
	 * This method involves pasting the role as well as its content
	 */
	public void paste(PDTreeModel pdTreeModel, TreeNode node) {
		if (pdTreeModel.clipboardItem == null || node instanceof InstanceNode) {
			return;
		}
		GUID transaction = pdTreeModel.treeView.store.begin();
		//Initially linking the primary role node to the instance node
		pdTreeModel.treeView.store.ensureLink(transaction,getValue(),((RoleNode)node).getRole(),pdTreeModel.clipboardItem);
		//Followed by....
		//Linking each instance node child to the role node
		int numChild = node.getChildCount();
		for(int i=0; i < numChild; i++){
			InstanceNode child = ((InstanceNode)(node.getChildAt(i)));
			pdTreeModel.treeView.store.ensureLink(transaction, (GUID)getValue(), ((RoleNode)node).getRole(), (GUID)child.getValue());
		}
		pdTreeModel.treeView.store.commit(transaction);
	}

	/**
	 * This method involves pasting the role only
	 */
	public void pasteRole(PDTreeModel pdTreeModel, TreeNode node) {
		if (pdTreeModel.clipboardItem == null || node instanceof InstanceNode) {
			return;
		}
		GUID transaction = pdTreeModel.treeView.store.begin();
		//Linking the role node to the instance node
		pdTreeModel.treeView.store.ensureLink(transaction,getValue(),((RoleNode)node).getRole(),pdTreeModel.clipboardItem);
		pdTreeModel.treeView.store.commit(transaction);
	}

	public void copy(PDTreeModel pdTreeModel) {
		pdTreeModel.clipboardItem = getValue();
	}

	public void contextMenu(PDTreeView pdTreeView, int x, int y) {

		//This is the paste option which has a 
		//sub-menu presenting the user with a 
		//choice of pasting either just the 
		//role or pasting the role as well 
		//as its content
		AbstractAction pasteRole = new AbstractAction("Paste role") {
			@Override
			public void actionPerformed(ActionEvent e) {
				pasteRole(treeView.getPDTreeModel(),treeView.clipBoard);
			}
		};
		AbstractAction pasteRoleContent = new AbstractAction("Paste role and content") {
			@Override
			public void actionPerformed(ActionEvent e) {
				paste(treeView.getPDTreeModel(),treeView.clipBoard);
			}
		};

		//Adding items to the pop-up menu
		pdTreeView.popup.removeAll();
		pdTreeView.popup.add(pdTreeView.rename);
		pdTreeView.popup.add(pdTreeView.copy);
		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(pasteRole);
		pdTreeView.popup.add(pasteRoleContent);
		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(pdTreeView.remove);
		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(pdTreeView.findNext);
		pdTreeView.popup.add(pdTreeView.findPrev);
		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(pdTreeView.icon);
		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(pdTreeView.refresh);
		pdTreeView.popup.add(toggle);
		pdTreeView.popup.addSeparator();

		// Modify is only supported on primitive instances
		if (parent instanceof PrimitiveRoleNode) {
			AbstractAction modify = new AbstractAction("Modify...") {
				@Override
				public void actionPerformed(ActionEvent e) {
					doModify();
				}
			};
			modify.putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getAWTKeyStroke(KeyEvent.VK_F2, 0));
			pdTreeView.popup.add(modify);
		}

		// Adds a new role node with user's choice of name and type
		if(!(parent instanceof PrimitiveRoleNode)){
			AbstractAction newRole = new AbstractAction("Add new role...") {
				@Override
				public void actionPerformed(ActionEvent e) {
					addNewRole();
				}
			};
			newRole.putValue(Action.ACCELERATOR_KEY,
					KeyStroke.getAWTKeyStroke(KeyEvent.VK_F2, 0));

			pdTreeView.popup.add(newRole);
		}
		
		// This option renders roles if there were any hidden that is
		final InstanceNode iNode = this;
		AbstractAction displayHidden = new AbstractAction("Show hidden roles") {
			@Override
			public void actionPerformed(ActionEvent e) {
				treeView.hiddenRoles.clear();
				model.refresh(iNode);
			}
		};
		displayHidden.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getAWTKeyStroke(KeyEvent.VK_F2, 0));

		pdTreeView.popup.addSeparator();
		pdTreeView.popup.add(displayHidden);
		
		//Enable the "Show hidden roles" if there are any hidden roles in the HashSet
		displayHidden.setEnabled(treeView.hiddenRoles.size() > 0);

		// Restrict paste based on type of clipboard item (must equal to the
		// expected type)
		//Responsible for enabling pasting; set as true to enable
		pasteRole.setEnabled(false);
		pasteRoleContent.setEnabled(false);
		Object clipboardItem = pdTreeView.getPDTreeModel().getClipboardItem();
		if (clipboardItem != null) {
			GUID transaction = pdTreeView.store.begin();
			GUID objectType = treeView.store.getType(transaction,getValue());
			String clipboardItemName = pdTreeView.store.getName(
					transaction, clipboardItem);
			GUID clipboardItemType = pdTreeView.store.getType(
					transaction, clipboardItem);
			String clipboardItemTypeName = pdTreeView.store.getName(
					transaction, clipboardItemType);
			pdTreeView.store.commit(transaction);
			//Responsible for enabling pasting, comment out to ignore type check
			pasteRole.setEnabled(objectType.equals(clipboardItemType));
			pasteRoleContent.setEnabled(objectType.equals(clipboardItemType));
			pasteRole.putValue(Action.NAME, "Paste role: "
					+ clipboardItemTypeName + " '" + clipboardItemName + "'");
			pasteRoleContent.putValue(Action.NAME, "Paste role and content: "
					+ clipboardItemTypeName + " '" + clipboardItemName + "'");
		}

		// remove doesn't apply to the top-most instance node (which has
		// PDRootNode as parent
		pdTreeView.popup.show(pdTreeView, x, y);
	}

	public void doModify() {
		TreeNode selected = this.treeView.currentlySelectedNode();
		if (selected != null && selected instanceof InstanceNode) {
			final InstanceNode selectedNode = (InstanceNode) selected;
			
			//Checking the type name 
			GUID transaction = treeView.store.begin();
			GUID type = treeView.store.getType(transaction, selectedNode.getValue());
			String typeName = treeView.store.getName(transaction, type);
			treeView.store.commit(transaction);
			
			if(typeName.equals("Boolean")){//Scenario where type is Boolean: Launch Boolean widget
				booleanWidget(selectedNode);
			}else if(typeName.equals("Integer")){//Scenario where type is Integer: Launch Integer widget
				integerWidget(selectedNode);
			}else if(typeName.equals("Double")){//Scenario where type is Double: Launch Double widget
				doubleWidget(selectedNode);
			}
			else{
				Object oldValue = selectedNode.getName();
				// @TODO parse this value to appropriate type
				String newValue = JOptionPane.showInputDialog(treeView,
						"Enter new value", oldValue);
				if (newValue != null && !newValue.isEmpty()
						&& !newValue.equals(oldValue)) {
					transaction = treeView.store.begin();// Beginning transaction
					treeView.store.setName(transaction, selectedNode.getValue(), newValue);// Sets the name associated with the GUID
					treeView.store.commit(transaction);// Committing the transaction
					treeView.doRefresh();
					selectedNode.setValue(newValue);
				}
			}
		}
	}

	/**
	 * This method is used for naming a new node; The user is directed
	 * to this method right after a new InstanceNode is added under
	 * a RoleNode to provide a name for the new node.
	 * @param InstanceNode node
	 */
	public void nameInstance(InstanceNode node) {
		if (node != null) {
			String newValue = JOptionPane.showInputDialog(treeView,
					"Enter value for new instance","New "+node.getTypeName());
			if (newValue != null && !newValue.isEmpty()) {
				model.rename(node, newValue);
			}else{
				model.remove(node);
			}
		}
	}

	/**
	 * This method presents the user
	 * with an integer widget which
	 * allows them to enter only integer
	 * values
	 */
	public void integerWidget(InstanceNode node){
		InstanceNode selectedNode = node;
		int previousValue = Integer.parseInt(node.getName());
		String response = JOptionPane.showInputDialog(treeView, "Please enter the integer value", previousValue);
		int value;
		
		try{
			value = Integer.parseInt(response);
			GUID transaction = treeView.store.begin();
			treeView.store.setName(transaction, selectedNode.getValue(), response);
			treeView.store.commit(transaction);
			model.refresh((RoleNode)selectedNode.getParent());
			selectedNode.setValue(response);
		}catch(Exception e){
			//JOptionPane.showMessageDialog(treeView, "This is not a valid input");
		}
	}

	/**
	 * This method presents the user
	 * with an double widget which
	 * allows them to enter only double
	 * values
	 */
	public void doubleWidget(InstanceNode node){
		InstanceNode selectedNode = node;
		double previousValue = Double.parseDouble(node.getName());
		String response = JOptionPane.showInputDialog(treeView, "Please enter the double value", previousValue);
		double value;
		
		try{
			value = Double.parseDouble(response);
			GUID transaction = treeView.store.begin();
			treeView.store.setName(transaction, selectedNode.getValue(), response);
			treeView.store.commit(transaction);
			model.refresh((RoleNode)selectedNode.getParent());
			selectedNode.setValue(response);
		}catch(Exception e){
			//JOptionPane.showMessageDialog(treeView, "This is not a valid input");
		}
	}
	
	/**
	 * This method presents the user with a boolean widget
	 * which enables them to pick only selective values,
	 * namely, true or false. This constraint ensures that
	 * inappropriate values are not entered.
	 * @param node
	 */
	public void booleanWidget(InstanceNode node){
		
		//Creating the prompt window and adding components to it
		final InstanceNode selectedNode = node;
		final JFrame frame = new JFrame("Choose boolean value");
		frame.setLayout(new GridLayout(2,2));
		frame.setSize(300, 100);
		frame.setLocationRelativeTo(treeView);
		final JRadioButton trueRadio = new JRadioButton("true");
		final JRadioButton falseRadio = new JRadioButton("false");
		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");
		final ButtonGroup group = new ButtonGroup();
		group.add(trueRadio);
		group.add(falseRadio);
		frame.add(trueRadio);
		frame.add(falseRadio);
		frame.add(okButton);
		frame.add(cancelButton);
		if((selectedNode.getName()).equals("false")){
			falseRadio.setSelected(true);
		}else{
			trueRadio.setSelected(true);
		}
		frame.setVisible(true);

		//Linking an action to the associated button
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(trueRadio.isSelected()){
					GUID transaction = treeView.store.begin();// Beginning transaction
					treeView.store.setName(transaction, selectedNode.getValue(), "true");// Sets the name associated with the GUID
					treeView.store.commit(transaction);// Committing the transaction
					model.refresh((RoleNode)selectedNode.getParent());
					selectedNode.setValue("true");
				}else if(falseRadio.isSelected()){
					GUID transaction = treeView.store.begin();// Beginning transaction
					treeView.store.setName(transaction, selectedNode.getValue(), "false");// Sets the name associated with the GUID
					treeView.store.commit(transaction);// Committing the transaction
					model.refresh((RoleNode)selectedNode.getParent());
					selectedNode.setValue("false");
				}
				frame.dispose();
			}
		});
		
		//Linking an action to the associated button
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
	}
	
	/**
	 * This method presents the user with a widget
	 * that allows them to enter a name and pick
	 * a type for the role node from a list of 
	 * avialable types 
	 */
	private void addNewRole() {
		final InstanceNode selectedNode = (InstanceNode)treeView.currentlySelectedNode();

		//Retrieving the types and storing them
		GUID transaction = treeView.store.begin();
		Collection<Object>newRoles = treeView.store.getAllInstancesOfType(transaction, PDStore.TYPE_TYPEID);
		String[] list = new String[newRoles.size()];
		int i = 0;
		int objectIndex = 0;
		for(Object role: newRoles){
			list[i] = treeView.store.getName(transaction, role);
			if(list[i].equals("Object")){
				objectIndex = i;
			}
			i++;
		}
		treeView.store.commit(transaction);

		//Creating a combo box and loading the string names of GUID's
		final JComboBox<String> options = new JComboBox<String>(list);
		options.setSelectedIndex(objectIndex);//Default option if user does not choose an option
		options.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		//Creating the prompt window and adding components to it
		JLabel promptFirst = new JLabel("Please enter the role name");
		final JTextField textFirst = new JTextField();
		JLabel promptSecond = new JLabel("Please select the role type");
		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");
        final JFrame frame = new JFrame("Create a new role");
        frame.setLocationRelativeTo(treeView);
        frame.setLayout(new GridLayout(3,2));
        frame.add(promptFirst);
        frame.add(textFirst);
        frame.add(promptSecond);
        frame.add(options);
        frame.add(okButton);
        frame.add(cancelButton);
        frame.setSize(370, 140);
        frame.setVisible(true);

        //Linking an action to the associated button
        okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newRoleName = textFirst.getText();
				if (newRoleName != null && !newRoleName.isEmpty()) {
					GUID newRoleID = new GUID();
	        		GUID transaction = treeView.store.begin();// Beginning transaction
	        		GUID SelectedType = treeView.store.getId(transaction, (String)options.getSelectedItem());//Getting GUID of selected name
	        		treeView.store.setName(transaction, newRoleID, newRoleName);// Sets the name associated with the GUID
	        		treeView.store.setType(transaction, newRoleID, SelectedType);
	        		treeView.store.addLink(transaction, selectedNode.getValue(), PDStore.USES_ROLE_ROLEID, newRoleID);
	        		GUID type = treeView.store.getType(transaction,selectedNode.getValue());
	        		treeView.store.createRelation(transaction, type, newRoleID, SelectedType);
	        		treeView.store.commit(transaction);// Committing the transaction
	        		treeView.doRefresh();
				}
				frame.dispose();
			}
        });
        
        //Linking an action to the associated button
        cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
        });
	}
	
	public void setIcon(Blob image){
		GUID transaction = treeView.store.begin();
		treeView.store.setIcon(transaction,getValue(), image);
		treeView.store.commit(transaction);
		model.refresh((RoleNode)getParent());
		treeView.setCellRenderer(treeView.render);
	}
	
}