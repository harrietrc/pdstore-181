package address.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Customer" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("address.dal.PDCustomer");
 * @author PDGen
 */
public class PDCustomer implements PDInstance,PDCustomerI{

	public static final GUID typeId = new GUID("9e70a65043b111e08de5842b2b9af4fd"); 

	public static final GUID roleAddressId = new GUID("9e7190b043b111e08de5842b2b9af4fd");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDCustomer.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDCustomer:" + name;
		else
			return "PDCustomer:" + id;
	}

	/**
	 * Creates an PDCustomer object representing a PDStore instance of type Customer.
	 * @param store the store the instance should be in
	 */
	public PDCustomer(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDCustomer object representing the instance with the given ID. 
	 * The ID must be of an instance of type Customer.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDCustomer(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Customer with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDCustomer load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDCustomer)instance;
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
	 * Returns the instance connected to this instance through the role address.
	 * @return the connected instance
	 */
	public PDAddress getAddress(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleAddressId);
	 	return (PDAddress) store.load(PDAddress.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role address.
	 * @return the connected instance(s)
	 */
	 public Collection<PDAddress> getAddresss(){
		return (Collection<PDAddress>)(Object)store.getAndLoadInstances(this.id, roleAddressId, PDAddress.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "address".
	 * If the given instance is null, nothing happens.
	 * @param address the instance to connect
	 */
	public void addAddress(GUID address){
		if (address != null) {
			store.addLink(this.id, roleAddressId, address);
		}
	}
	/**
	 * Connects this instance to the given instance using role "address".
	 * If the given instance is null, nothing happens.
	 * @param address the instance to connect
	 */
	public void addAddress(PDAddress address){
		if (address != null)
			addAddress(address.getId());
	}

	/**
	 * Connects this instance to the given instance using role "address".
	 * If the given collection of instances is null, nothing happens.
	 * @param address the Collection of instances to connect
	 */
	public void addAddresss(Collection<PDAddress> addresss){
		if (addresss == null)
			return;
		for (PDAddress instance : addresss)
			addAddress(instance);
	}

	/**
	 * Removes the link from this instance through role "address".
	 */
	public void removeAddress(){
		store.removeLink(this.id, roleAddressId,
			store.getInstance(this.id, roleAddressId));
	}

	/**
	 * Removes the link from this instance through role "address" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeAddress(PDAddress address){
		if (address == null)
			return;
		store.removeLink(this.id, roleAddressId, address.getId());
	}

	/**
	 * Removes the links from this instance through role "address" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeAddresss(Collection<PDAddress> addresss){
		if (addresss == null)
			return;
		for (PDAddress instance : addresss)
			store.removeLink(this.id, roleAddressId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "address".
	 * If there is already an instance connected to this instance through role "address", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param address the instance to connect
	 */
	public void setAddress(GUID address) {
		store.setLink(this.id,  roleAddressId, address);
	}
	/**
	 * Connects this instance to the given instance using role "address".
	 * If there is already an instance connected to this instance through role "address", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param address the instance to connect
	 */
	public void setAddress(PDAddress address) 
{		setAddress(address.getId());
	}
}
