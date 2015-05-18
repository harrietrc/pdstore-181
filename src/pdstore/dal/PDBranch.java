package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Branch" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDBranch");
 * @author PDGen
 */
public class PDBranch implements PDInstance,PDBranchI{

	public static final GUID typeId = new GUID("5921e0a079b811dfb27f002170295281"); 

	public static final GUID roleTransactionId = new GUID("5921e0a479b811dfb26f002170295281");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDBranch.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDBranch:" + name;
		else
			return "PDBranch:" + id;
	}

	/**
	 * Creates an PDBranch object representing a PDStore instance of type Branch.
	 * @param store the store the instance should be in
	 */
	public PDBranch(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDBranch object representing the instance with the given ID. 
	 * The ID must be of an instance of type Branch.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDBranch(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Branch with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDBranch load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDBranch)instance;
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
