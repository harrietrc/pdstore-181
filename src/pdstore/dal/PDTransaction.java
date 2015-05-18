package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Transaction" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDTransaction");
 * @author PDGen
 */
public class PDTransaction implements PDInstance,PDTransactionI{

	public static final GUID typeId = new GUID("5921e0a179b811dfb27f002170295281"); 

	public static final GUID roleBegin_transactionId = new GUID("878e396d134d11e1b3051cc1dec00ed3");
	public static final GUID roleDurableTransactionId = new GUID("878e396d134d11e1b3151cc1dec00ed3");
	public static final GUID roleIsolationLevelId = new GUID("878e3970134d11e1b3151cc1dec00ed3");
	public static final GUID roleBranchId = new GUID("5921e0a479b811dfb27f002170295281");
	public static final GUID roleParentId = new GUID("5921e0a279b811dfb27f002170295281");
	public static final GUID roleChildId = new GUID("5921e0a279b811dfb26f002170295281");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDTransaction.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDTransaction:" + name;
		else
			return "PDTransaction:" + id;
	}

	/**
	 * Creates an PDTransaction object representing a PDStore instance of type Transaction.
	 * @param store the store the instance should be in
	 */
	public PDTransaction(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDTransaction object representing the instance with the given ID. 
	 * The ID must be of an instance of type Transaction.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDTransaction(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Transaction with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDTransaction load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDTransaction)instance;
	}

	/**
	 * Gets the PDStore this object is stored in.
	 */
	public PDStore getStore() {
		return store;
	}

	/**
	 * Gets the GUID of the instance represented by this object.
	 */
	public GUID getId() {
		return id;
	}

	/**
	 * Gets the GUID of the type of the instance represented by this object.
	 * This method isn't static so that it can be part of the PDInstance interface.
	 */
	public GUID getTypeId() {
		return typeId;
	}

	/**
Get pdWorkingCopy */
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
return null;
	}

	/**
	 * Gets a textual label for this instance, for use in UIs.
	 * @return a textual label for the instance
	 */
	public String getLabel() {
		return store.getLabel(id);
	}

	/**
	 * Gets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * @return name the instance name
	 */
	public String getName() {
		return store.getName(id);
	}

	/**
	 * Sets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance already has a name, the name will be overwritten.
	 * If the given name is null, an existing name will be removed.
	 * @return name the new instance name
	 */
	public void setName(String name) {
		store.setName(id, name);
	}

	/**
	 * Removes the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance does not have a name, nothing happens.
	 */
	public void removeName() {
		store.removeName(id);
	}

	/**
	 * Gets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * @return icon the instance icon
	 */
	public Blob getIcon() {
		return store.getIcon(id);
	}

	/**
	 * Sets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance already has an icon, the icon will be overwritten.
	 * If the given icon is null, an existing icon will be removed.
	 * @return icon the new instance icon
	 */
	public void setIcon(Blob icon) {
		store.setIcon(id, icon);
	}

	/**
	 * Removes the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance does not have an icon, nothing happens.
	 */
	public void removeIcon() {
		store.removeIcon(id);
	}
	/**
	 * Returns the instance connected to this instance through the role begin-transaction.
	 * @return the connected instance
	 */
	public PDTransaction getBegin_transaction(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleBegin_transactionId);
	 	return (PDTransaction) store.load(PDTransaction.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role begin-transaction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getBegin_transactions(){
		return (Collection<PDTransaction>)(Object)store.getAndLoadInstances(this.id, roleBegin_transactionId, PDTransaction.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If the given instance is null, nothing happens.
	 * @param begin_transaction the instance to connect
	 */
	public void addBegin_transaction(GUID begin_transaction){
		if (begin_transaction != null) {
			store.addLink(this.id, roleBegin_transactionId, begin_transaction);
		}
	}
	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If the given instance is null, nothing happens.
	 * @param begin_transaction the instance to connect
	 */
	public void addBegin_transaction(PDTransaction begin_transaction){
		if (begin_transaction != null)
			addBegin_transaction(begin_transaction.getId());
	}

	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If the given collection of instances is null, nothing happens.
	 * @param begin_transaction the Collection of instances to connect
	 */
	public void addBegin_transactions(Collection<PDTransaction> begin_transactions){
		if (begin_transactions == null)
			return;
		for (PDTransaction instance : begin_transactions)
			addBegin_transaction(instance);
	}

	/**
	 * Removes the link from this instance through role "begin-transaction".
	 */
	public void removeBegin_transaction(){
		store.removeLink(this.id, roleBegin_transactionId,
			store.getInstance(this.id, roleBegin_transactionId));
	}

	/**
	 * Removes the link from this instance through role "begin-transaction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeBegin_transaction(PDTransaction begin_transaction){
		if (begin_transaction == null)
			return;
		store.removeLink(this.id, roleBegin_transactionId, begin_transaction.getId());
	}

	/**
	 * Removes the links from this instance through role "begin-transaction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeBegin_transactions(Collection<PDTransaction> begin_transactions){
		if (begin_transactions == null)
			return;
		for (PDTransaction instance : begin_transactions)
			store.removeLink(this.id, roleBegin_transactionId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If there is already an instance connected to this instance through role "begin-transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param begin_transaction the instance to connect
	 */
	public void setBegin_transaction(GUID begin_transaction) {
		store.setLink(this.id,  roleBegin_transactionId, begin_transaction);
	}
	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If there is already an instance connected to this instance through role "begin-transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param begin_transaction the instance to connect
	 */
	public void setBegin_transaction(PDTransaction begin_transaction) 
{		setBegin_transaction(begin_transaction.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role durable transaction.
	 * @return the connected instance
	 */
	public PDTransaction getDurableTransaction(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleDurableTransactionId);
	 	return (PDTransaction) store.load(PDTransaction.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role durable transaction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getDurableTransactions(){
		return (Collection<PDTransaction>)(Object)store.getAndLoadInstances(this.id, roleDurableTransactionId, PDTransaction.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If the given instance is null, nothing happens.
	 * @param durableTransaction the instance to connect
	 */
	public void addDurableTransaction(GUID durableTransaction){
		if (durableTransaction != null) {
			store.addLink(this.id, roleDurableTransactionId, durableTransaction);
		}
	}
	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If the given instance is null, nothing happens.
	 * @param durableTransaction the instance to connect
	 */
	public void addDurableTransaction(PDTransaction durableTransaction){
		if (durableTransaction != null)
			addDurableTransaction(durableTransaction.getId());
	}

	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If the given collection of instances is null, nothing happens.
	 * @param durableTransaction the Collection of instances to connect
	 */
	public void addDurableTransactions(Collection<PDTransaction> durableTransactions){
		if (durableTransactions == null)
			return;
		for (PDTransaction instance : durableTransactions)
			addDurableTransaction(instance);
	}

	/**
	 * Removes the link from this instance through role "durable transaction".
	 */
	public void removeDurableTransaction(){
		store.removeLink(this.id, roleDurableTransactionId,
			store.getInstance(this.id, roleDurableTransactionId));
	}

	/**
	 * Removes the link from this instance through role "durable transaction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDurableTransaction(PDTransaction durableTransaction){
		if (durableTransaction == null)
			return;
		store.removeLink(this.id, roleDurableTransactionId, durableTransaction.getId());
	}

	/**
	 * Removes the links from this instance through role "durable transaction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeDurableTransactions(Collection<PDTransaction> durableTransactions){
		if (durableTransactions == null)
			return;
		for (PDTransaction instance : durableTransactions)
			store.removeLink(this.id, roleDurableTransactionId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If there is already an instance connected to this instance through role "durable transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param durableTransaction the instance to connect
	 */
	public void setDurableTransaction(GUID durableTransaction) {
		store.setLink(this.id,  roleDurableTransactionId, durableTransaction);
	}
	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If there is already an instance connected to this instance through role "durable transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param durableTransaction the instance to connect
	 */
	public void setDurableTransaction(PDTransaction durableTransaction) 
{		setDurableTransaction(durableTransaction.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role isolation level.
	 * @return the connected instance
	 */
	public Long getIsolationLevel(){
	 	return (Long)store.getInstance(this.id, roleIsolationLevelId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role isolation level.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getIsolationLevels(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleIsolationLevelId);
	 }

   /**
	 * Connects this instance to the given instance using role "isolation level".
	 * If the given instance is null, nothing happens.
	 * @param isolationLevel the instance to connect
	 */
	public void addIsolationLevel(Long isolationLevel){
		if (isolationLevel != null) {
			store.addLink(this.id, roleIsolationLevelId, isolationLevel);
		}
	}
	/**
	 * Connects this instance to the given instances using role "isolation level".
	 * If the given collection of instances is null, nothing happens.
	 * @param isolationLevel the Collection of instances to connect
	 */
	public void addIsolationLevels(Collection<Long> isolationLevels){
		if (isolationLevels == null)
			return;
		for (Long instance : isolationLevels)
			addIsolationLevel(instance);
	}

	/**
	 * Removes the link from this instance through role "isolation level".
	 */
	public void removeIsolationLevel(){
		store.removeLink(this.id, roleIsolationLevelId,
			store.getInstance(this.id, roleIsolationLevelId));
	}

	/**
	 * Removes the link from this instance through role "isolation level" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeIsolationLevel(Long isolationLevel){
		if (isolationLevel == null)
			return;
		store.removeLink(this.id, roleIsolationLevelId, isolationLevel);
	}

	/**
	 * Connects this instance to the given instance using role "isolation level".
	 * If there is already an instance connected to this instance through role "isolation level", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isolationLevel the instance to connect
	 */
	public void setIsolationLevel(Long isolationLevel) {
		store.setLink(this.id,  roleIsolationLevelId, isolationLevel);
	}
	/**
	 * Returns the instance connected to this instance through the role branch.
	 * @return the connected instance
	 */
	public PDBranch getBranch(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleBranchId);
	 	return (PDBranch) store.load(PDBranch.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role branch.
	 * @return the connected instance(s)
	 */
	 public Collection<PDBranch> getBranchs(){
		return (Collection<PDBranch>)(Object)store.getAndLoadInstances(this.id, roleBranchId, PDBranch.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "branch".
	 * If the given instance is null, nothing happens.
	 * @param branch the instance to connect
	 */
	public void addBranch(GUID branch){
		if (branch != null) {
			store.addLink(this.id, roleBranchId, branch);
		}
	}
	/**
	 * Connects this instance to the given instance using role "branch".
	 * If the given instance is null, nothing happens.
	 * @param branch the instance to connect
	 */
	public void addBranch(PDBranch branch){
		if (branch != null)
			addBranch(branch.getId());
	}

	/**
	 * Connects this instance to the given instance using role "branch".
	 * If the given collection of instances is null, nothing happens.
	 * @param branch the Collection of instances to connect
	 */
	public void addBranchs(Collection<PDBranch> branchs){
		if (branchs == null)
			return;
		for (PDBranch instance : branchs)
			addBranch(instance);
	}

	/**
	 * Removes the link from this instance through role "branch".
	 */
	public void removeBranch(){
		store.removeLink(this.id, roleBranchId,
			store.getInstance(this.id, roleBranchId));
	}

	/**
	 * Removes the link from this instance through role "branch" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeBranch(PDBranch branch){
		if (branch == null)
			return;
		store.removeLink(this.id, roleBranchId, branch.getId());
	}

	/**
	 * Removes the links from this instance through role "branch" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeBranchs(Collection<PDBranch> branchs){
		if (branchs == null)
			return;
		for (PDBranch instance : branchs)
			store.removeLink(this.id, roleBranchId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "branch".
	 * If there is already an instance connected to this instance through role "branch", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param branch the instance to connect
	 */
	public void setBranch(GUID branch) {
		store.setLink(this.id,  roleBranchId, branch);
	}
	/**
	 * Connects this instance to the given instance using role "branch".
	 * If there is already an instance connected to this instance through role "branch", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param branch the instance to connect
	 */
	public void setBranch(PDBranch branch) 
{		setBranch(branch.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role parent.
	 * @return the connected instance
	 */
	public PDTransaction getParent(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleParentId);
	 	return (PDTransaction) store.load(PDTransaction.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role parent.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getParents(){
		return (Collection<PDTransaction>)(Object)store.getAndLoadInstances(this.id, roleParentId, PDTransaction.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "parent".
	 * If the given instance is null, nothing happens.
	 * @param parent the instance to connect
	 */
	public void addParent(GUID parent){
		if (parent != null) {
			store.addLink(this.id, roleParentId, parent);
		}
	}
	/**
	 * Connects this instance to the given instance using role "parent".
	 * If the given instance is null, nothing happens.
	 * @param parent the instance to connect
	 */
	public void addParent(PDTransaction parent){
		if (parent != null)
			addParent(parent.getId());
	}

	/**
	 * Connects this instance to the given instance using role "parent".
	 * If the given collection of instances is null, nothing happens.
	 * @param parent the Collection of instances to connect
	 */
	public void addParents(Collection<PDTransaction> parents){
		if (parents == null)
			return;
		for (PDTransaction instance : parents)
			addParent(instance);
	}

	/**
	 * Removes the link from this instance through role "parent".
	 */
	public void removeParent(){
		store.removeLink(this.id, roleParentId,
			store.getInstance(this.id, roleParentId));
	}

	/**
	 * Removes the link from this instance through role "parent" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeParent(PDTransaction parent){
		if (parent == null)
			return;
		store.removeLink(this.id, roleParentId, parent.getId());
	}

	/**
	 * Removes the links from this instance through role "parent" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeParents(Collection<PDTransaction> parents){
		if (parents == null)
			return;
		for (PDTransaction instance : parents)
			store.removeLink(this.id, roleParentId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "parent".
	 * If there is already an instance connected to this instance through role "parent", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parent the instance to connect
	 */
	public void setParent(GUID parent) {
		store.setLink(this.id,  roleParentId, parent);
	}
	/**
	 * Connects this instance to the given instance using role "parent".
	 * If there is already an instance connected to this instance through role "parent", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parent the instance to connect
	 */
	public void setParent(PDTransaction parent) 
{		setParent(parent.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role child.
	 * @return the connected instance
	 */
	public PDTransaction getChild(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleChildId);
	 	return (PDTransaction) store.load(PDTransaction.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role child.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getChilds(){
		return (Collection<PDTransaction>)(Object)store.getAndLoadInstances(this.id, roleChildId, PDTransaction.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "child".
	 * If the given instance is null, nothing happens.
	 * @param child the instance to connect
	 */
	public void addChild(GUID child){
		if (child != null) {
			store.addLink(this.id, roleChildId, child);
		}
	}
	/**
	 * Connects this instance to the given instance using role "child".
	 * If the given instance is null, nothing happens.
	 * @param child the instance to connect
	 */
	public void addChild(PDTransaction child){
		if (child != null)
			addChild(child.getId());
	}

	/**
	 * Connects this instance to the given instance using role "child".
	 * If the given collection of instances is null, nothing happens.
	 * @param child the Collection of instances to connect
	 */
	public void addChilds(Collection<PDTransaction> childs){
		if (childs == null)
			return;
		for (PDTransaction instance : childs)
			addChild(instance);
	}

	/**
	 * Removes the link from this instance through role "child".
	 */
	public void removeChild(){
		store.removeLink(this.id, roleChildId,
			store.getInstance(this.id, roleChildId));
	}

	/**
	 * Removes the link from this instance through role "child" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeChild(PDTransaction child){
		if (child == null)
			return;
		store.removeLink(this.id, roleChildId, child.getId());
	}

	/**
	 * Removes the links from this instance through role "child" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeChilds(Collection<PDTransaction> childs){
		if (childs == null)
			return;
		for (PDTransaction instance : childs)
			store.removeLink(this.id, roleChildId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "child".
	 * If there is already an instance connected to this instance through role "child", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param child the instance to connect
	 */
	public void setChild(GUID child) {
		store.setLink(this.id,  roleChildId, child);
	}
	/**
	 * Connects this instance to the given instance using role "child".
	 * If there is already an instance connected to this instance through role "child", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param child the instance to connect
	 */
	public void setChild(PDTransaction child) 
{		setChild(child.getId());
	}
}
