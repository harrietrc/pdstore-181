package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Change" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDChange");
 * @author PDGen
 */
public class PDChange implements PDInstance,PDChangeI{

	public static final GUID typeId = new GUID("005422218cf711e1926b842b2b9af4fd"); 

	public static final GUID roleInstance2Id = new GUID("0054221b8cf711e1926b842b2b9af4fd");
	public static final GUID roleRole2Id = new GUID("0054221c8cf711e1926b842b2b9af4fd");
	public static final GUID roleInstance1Id = new GUID("0054221d8cf711e1926b842b2b9af4fd");
	public static final GUID roleChangeTypeId = new GUID("0054221e8cf711e1926b842b2b9af4fd");
	public static final GUID roleTransactionId = new GUID("0054221f8cf711e1926b842b2b9af4fd");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDChange.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDChange:" + name;
		else
			return "PDChange:" + id;
	}

	/**
	 * Creates an PDChange object representing a PDStore instance of type Change.
	 * @param store the store the instance should be in
	 */
	public PDChange(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDChange object representing the instance with the given ID. 
	 * The ID must be of an instance of type Change.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDChange(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Change with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDChange load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDChange)instance;
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
	 * Returns the instance connected to this instance through the role instance2.
	 * @return the connected instance
	 */
	public PDInstance getInstance2(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleInstance2Id);
	 	return (PDInstance) store.load(instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role instance2.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getInstance2s(){
		return (Collection<PDInstance>)(Object)store.getAndLoadInstances(this.id, roleInstance2Id);
	 }

   /**
	 * Connects this instance to the given instance using role "instance2".
	 * If the given instance is null, nothing happens.
	 * @param instance2 the instance to connect
	 */
	public void addInstance2(GUID instance2){
		if (instance2 != null) {
			store.addLink(this.id, roleInstance2Id, instance2);
		}
	}
	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If the given instance is null, nothing happens.
	 * @param instance2 the instance to connect
	 */
	public void addInstance2(PDInstance instance2){
		if (instance2 != null)
			addInstance2(instance2.getId());
	}

	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If the given collection of instances is null, nothing happens.
	 * @param instance2 the Collection of instances to connect
	 */
	public void addInstance2s(Collection<PDInstance> instance2s){
		if (instance2s == null)
			return;
		for (PDInstance instance : instance2s)
			addInstance2(instance);
	}

	/**
	 * Removes the link from this instance through role "instance2".
	 */
	public void removeInstance2(){
		store.removeLink(this.id, roleInstance2Id,
			store.getInstance(this.id, roleInstance2Id));
	}

	/**
	 * Removes the link from this instance through role "instance2" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInstance2(PDInstance instance2){
		if (instance2 == null)
			return;
		store.removeLink(this.id, roleInstance2Id, instance2.getId());
	}

	/**
	 * Removes the links from this instance through role "instance2" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInstance2s(Collection<PDInstance> instance2s){
		if (instance2s == null)
			return;
		for (PDInstance instance : instance2s)
			store.removeLink(this.id, roleInstance2Id, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If there is already an instance connected to this instance through role "instance2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance2 the instance to connect
	 */
	public void setInstance2(GUID instance2) {
		store.setLink(this.id,  roleInstance2Id, instance2);
	}
	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If there is already an instance connected to this instance through role "instance2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance2 the instance to connect
	 */
	public void setInstance2(PDInstance instance2) 
{		setInstance2(instance2.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role role2.
	 * @return the connected instance
	 */
	public PDRole getRole2(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleRole2Id);
	 	return (PDRole) store.load(PDRole.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role role2.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getRole2s(){
		return (Collection<PDRole>)(Object)store.getAndLoadInstances(this.id, roleRole2Id, PDRole.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "role2".
	 * If the given instance is null, nothing happens.
	 * @param role2 the instance to connect
	 */
	public void addRole2(GUID role2){
		if (role2 != null) {
			store.addLink(this.id, roleRole2Id, role2);
		}
	}
	/**
	 * Connects this instance to the given instance using role "role2".
	 * If the given instance is null, nothing happens.
	 * @param role2 the instance to connect
	 */
	public void addRole2(PDRole role2){
		if (role2 != null)
			addRole2(role2.getId());
	}

	/**
	 * Connects this instance to the given instance using role "role2".
	 * If the given collection of instances is null, nothing happens.
	 * @param role2 the Collection of instances to connect
	 */
	public void addRole2s(Collection<PDRole> role2s){
		if (role2s == null)
			return;
		for (PDRole instance : role2s)
			addRole2(instance);
	}

	/**
	 * Removes the link from this instance through role "role2".
	 */
	public void removeRole2(){
		store.removeLink(this.id, roleRole2Id,
			store.getInstance(this.id, roleRole2Id));
	}

	/**
	 * Removes the link from this instance through role "role2" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeRole2(PDRole role2){
		if (role2 == null)
			return;
		store.removeLink(this.id, roleRole2Id, role2.getId());
	}

	/**
	 * Removes the links from this instance through role "role2" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeRole2s(Collection<PDRole> role2s){
		if (role2s == null)
			return;
		for (PDRole instance : role2s)
			store.removeLink(this.id, roleRole2Id, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "role2".
	 * If there is already an instance connected to this instance through role "role2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param role2 the instance to connect
	 */
	public void setRole2(GUID role2) {
		store.setLink(this.id,  roleRole2Id, role2);
	}
	/**
	 * Connects this instance to the given instance using role "role2".
	 * If there is already an instance connected to this instance through role "role2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param role2 the instance to connect
	 */
	public void setRole2(PDRole role2) 
{		setRole2(role2.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role instance1.
	 * @return the connected instance
	 */
	public PDInstance getInstance1(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleInstance1Id);
	 	return (PDInstance) store.load(instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role instance1.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getInstance1s(){
		return (Collection<PDInstance>)(Object)store.getAndLoadInstances(this.id, roleInstance1Id);
	 }

   /**
	 * Connects this instance to the given instance using role "instance1".
	 * If the given instance is null, nothing happens.
	 * @param instance1 the instance to connect
	 */
	public void addInstance1(GUID instance1){
		if (instance1 != null) {
			store.addLink(this.id, roleInstance1Id, instance1);
		}
	}
	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If the given instance is null, nothing happens.
	 * @param instance1 the instance to connect
	 */
	public void addInstance1(PDInstance instance1){
		if (instance1 != null)
			addInstance1(instance1.getId());
	}

	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If the given collection of instances is null, nothing happens.
	 * @param instance1 the Collection of instances to connect
	 */
	public void addInstance1s(Collection<PDInstance> instance1s){
		if (instance1s == null)
			return;
		for (PDInstance instance : instance1s)
			addInstance1(instance);
	}

	/**
	 * Removes the link from this instance through role "instance1".
	 */
	public void removeInstance1(){
		store.removeLink(this.id, roleInstance1Id,
			store.getInstance(this.id, roleInstance1Id));
	}

	/**
	 * Removes the link from this instance through role "instance1" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInstance1(PDInstance instance1){
		if (instance1 == null)
			return;
		store.removeLink(this.id, roleInstance1Id, instance1.getId());
	}

	/**
	 * Removes the links from this instance through role "instance1" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInstance1s(Collection<PDInstance> instance1s){
		if (instance1s == null)
			return;
		for (PDInstance instance : instance1s)
			store.removeLink(this.id, roleInstance1Id, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If there is already an instance connected to this instance through role "instance1", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance1 the instance to connect
	 */
	public void setInstance1(GUID instance1) {
		store.setLink(this.id,  roleInstance1Id, instance1);
	}
	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If there is already an instance connected to this instance through role "instance1", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance1 the instance to connect
	 */
	public void setInstance1(PDInstance instance1) 
{		setInstance1(instance1.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role change type.
	 * @return the connected instance
	 */
	public String getChangeType(){
	 	return (String)store.getInstance(this.id, roleChangeTypeId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role change type.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getChangeTypes(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleChangeTypeId);
	 }

   /**
	 * Connects this instance to the given instance using role "change type".
	 * If the given instance is null, nothing happens.
	 * @param changeType the instance to connect
	 */
	public void addChangeType(String changeType){
		if (changeType != null) {
			store.addLink(this.id, roleChangeTypeId, changeType);
		}
	}
	/**
	 * Connects this instance to the given instances using role "change type".
	 * If the given collection of instances is null, nothing happens.
	 * @param changeType the Collection of instances to connect
	 */
	public void addChangeTypes(Collection<String> changeTypes){
		if (changeTypes == null)
			return;
		for (String instance : changeTypes)
			addChangeType(instance);
	}

	/**
	 * Removes the link from this instance through role "change type".
	 */
	public void removeChangeType(){
		store.removeLink(this.id, roleChangeTypeId,
			store.getInstance(this.id, roleChangeTypeId));
	}

	/**
	 * Removes the link from this instance through role "change type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeChangeType(String changeType){
		if (changeType == null)
			return;
		store.removeLink(this.id, roleChangeTypeId, changeType);
	}

	/**
	 * Connects this instance to the given instance using role "change type".
	 * If there is already an instance connected to this instance through role "change type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param changeType the instance to connect
	 */
	public void setChangeType(String changeType) {
		store.setLink(this.id,  roleChangeTypeId, changeType);
	}
	/**
	 * Returns the instance connected to this instance through the role transaction.
	 * @return the connected instance
	 */
	public PDTransaction getTransaction(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleTransactionId);
	 	return (PDTransaction) store.load(PDTransaction.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role transaction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getTransactions(){
		return (Collection<PDTransaction>)(Object)store.getAndLoadInstances(this.id, roleTransactionId, PDTransaction.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "transaction".
	 * If the given instance is null, nothing happens.
	 * @param transaction the instance to connect
	 */
	public void addTransaction(GUID transaction){
		if (transaction != null) {
			store.addLink(this.id, roleTransactionId, transaction);
		}
	}
	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If the given instance is null, nothing happens.
	 * @param transaction the instance to connect
	 */
	public void addTransaction(PDTransaction transaction){
		if (transaction != null)
			addTransaction(transaction.getId());
	}

	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If the given collection of instances is null, nothing happens.
	 * @param transaction the Collection of instances to connect
	 */
	public void addTransactions(Collection<PDTransaction> transactions){
		if (transactions == null)
			return;
		for (PDTransaction instance : transactions)
			addTransaction(instance);
	}

	/**
	 * Removes the link from this instance through role "transaction".
	 */
	public void removeTransaction(){
		store.removeLink(this.id, roleTransactionId,
			store.getInstance(this.id, roleTransactionId));
	}

	/**
	 * Removes the link from this instance through role "transaction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTransaction(PDTransaction transaction){
		if (transaction == null)
			return;
		store.removeLink(this.id, roleTransactionId, transaction.getId());
	}

	/**
	 * Removes the links from this instance through role "transaction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTransactions(Collection<PDTransaction> transactions){
		if (transactions == null)
			return;
		for (PDTransaction instance : transactions)
			store.removeLink(this.id, roleTransactionId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If there is already an instance connected to this instance through role "transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param transaction the instance to connect
	 */
	public void setTransaction(GUID transaction) {
		store.setLink(this.id,  roleTransactionId, transaction);
	}
	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If there is already an instance connected to this instance through role "transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param transaction the instance to connect
	 */
	public void setTransaction(PDTransaction transaction) 
{		setTransaction(transaction.getId());
	}
}
