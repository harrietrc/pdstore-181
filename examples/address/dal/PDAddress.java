package address.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Address" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("address.dal.PDAddress");
 * @author PDGen
 */
public class PDAddress implements PDInstance,PDAddressI{

	public static final GUID typeId = new GUID("5c61caa043b111e08850842b2b9af4fd"); 

	public static final GUID roleHouseNoId = new GUID("9e7190b243b111e08de5842b2b9af4fd");
	public static final GUID roleStreetNameId = new GUID("5c61caa143b111e08850842b2b9af4fd");
	public static final GUID roleCustomerId = new GUID("9e7190b043b111e08df5842b2b9af4fd");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDAddress.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDAddress:" + name;
		else
			return "PDAddress:" + id;
	}

	/**
	 * Creates an PDAddress object representing a PDStore instance of type Address.
	 * @param store the store the instance should be in
	 */
	public PDAddress(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDAddress object representing the instance with the given ID. 
	 * The ID must be of an instance of type Address.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDAddress(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Address with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDAddress load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDAddress)instance;
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
	 * Returns the instance connected to this instance through the role houseNo.
	 * @return the connected instance
	 */
	public Long getHouseNo(){
	 	return (Long)store.getInstance(this.id, roleHouseNoId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role houseNo.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getHouseNos(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleHouseNoId);
	 }

   /**
	 * Connects this instance to the given instance using role "houseNo".
	 * If the given instance is null, nothing happens.
	 * @param houseNo the instance to connect
	 */
	public void addHouseNo(Long houseNo){
		if (houseNo != null) {
			store.addLink(this.id, roleHouseNoId, houseNo);
		}
	}
	/**
	 * Connects this instance to the given instances using role "houseNo".
	 * If the given collection of instances is null, nothing happens.
	 * @param houseNo the Collection of instances to connect
	 */
	public void addHouseNos(Collection<Long> houseNos){
		if (houseNos == null)
			return;
		for (Long instance : houseNos)
			addHouseNo(instance);
	}

	/**
	 * Removes the link from this instance through role "houseNo".
	 */
	public void removeHouseNo(){
		store.removeLink(this.id, roleHouseNoId,
			store.getInstance(this.id, roleHouseNoId));
	}

	/**
	 * Removes the link from this instance through role "houseNo" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHouseNo(Long houseNo){
		if (houseNo == null)
			return;
		store.removeLink(this.id, roleHouseNoId, houseNo);
	}

	/**
	 * Connects this instance to the given instance using role "houseNo".
	 * If there is already an instance connected to this instance through role "houseNo", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param houseNo the instance to connect
	 */
	public void setHouseNo(Long houseNo) {
		store.setLink(this.id,  roleHouseNoId, houseNo);
	}
	/**
	 * Returns the instance connected to this instance through the role streetName.
	 * @return the connected instance
	 */
	public String getStreetName(){
	 	return (String)store.getInstance(this.id, roleStreetNameId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role streetName.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getStreetNames(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleStreetNameId);
	 }

   /**
	 * Connects this instance to the given instance using role "streetName".
	 * If the given instance is null, nothing happens.
	 * @param streetName the instance to connect
	 */
	public void addStreetName(String streetName){
		if (streetName != null) {
			store.addLink(this.id, roleStreetNameId, streetName);
		}
	}
	/**
	 * Connects this instance to the given instances using role "streetName".
	 * If the given collection of instances is null, nothing happens.
	 * @param streetName the Collection of instances to connect
	 */
	public void addStreetNames(Collection<String> streetNames){
		if (streetNames == null)
			return;
		for (String instance : streetNames)
			addStreetName(instance);
	}

	/**
	 * Removes the link from this instance through role "streetName".
	 */
	public void removeStreetName(){
		store.removeLink(this.id, roleStreetNameId,
			store.getInstance(this.id, roleStreetNameId));
	}

	/**
	 * Removes the link from this instance through role "streetName" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeStreetName(String streetName){
		if (streetName == null)
			return;
		store.removeLink(this.id, roleStreetNameId, streetName);
	}

	/**
	 * Connects this instance to the given instance using role "streetName".
	 * If there is already an instance connected to this instance through role "streetName", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param streetName the instance to connect
	 */
	public void setStreetName(String streetName) {
		store.setLink(this.id,  roleStreetNameId, streetName);
	}
	/**
	 * Returns the instance connected to this instance through the role customer.
	 * @return the connected instance
	 */
	public PDCustomer getCustomer(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleCustomerId);
	 	return (PDCustomer) store.load(PDCustomer.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role customer.
	 * @return the connected instance(s)
	 */
	 public Collection<PDCustomer> getCustomers(){
		return (Collection<PDCustomer>)(Object)store.getAndLoadInstances(this.id, roleCustomerId, PDCustomer.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "customer".
	 * If the given instance is null, nothing happens.
	 * @param customer the instance to connect
	 */
	public void addCustomer(GUID customer){
		if (customer != null) {
			store.addLink(this.id, roleCustomerId, customer);
		}
	}
	/**
	 * Connects this instance to the given instance using role "customer".
	 * If the given instance is null, nothing happens.
	 * @param customer the instance to connect
	 */
	public void addCustomer(PDCustomer customer){
		if (customer != null)
			addCustomer(customer.getId());
	}

	/**
	 * Connects this instance to the given instance using role "customer".
	 * If the given collection of instances is null, nothing happens.
	 * @param customer the Collection of instances to connect
	 */
	public void addCustomers(Collection<PDCustomer> customers){
		if (customers == null)
			return;
		for (PDCustomer instance : customers)
			addCustomer(instance);
	}

	/**
	 * Removes the link from this instance through role "customer".
	 */
	public void removeCustomer(){
		store.removeLink(this.id, roleCustomerId,
			store.getInstance(this.id, roleCustomerId));
	}

	/**
	 * Removes the link from this instance through role "customer" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeCustomer(PDCustomer customer){
		if (customer == null)
			return;
		store.removeLink(this.id, roleCustomerId, customer.getId());
	}

	/**
	 * Removes the links from this instance through role "customer" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeCustomers(Collection<PDCustomer> customers){
		if (customers == null)
			return;
		for (PDCustomer instance : customers)
			store.removeLink(this.id, roleCustomerId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "customer".
	 * If there is already an instance connected to this instance through role "customer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param customer the instance to connect
	 */
	public void setCustomer(GUID customer) {
		store.setLink(this.id,  roleCustomerId, customer);
	}
	/**
	 * Connects this instance to the given instance using role "customer".
	 * If there is already an instance connected to this instance through role "customer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param customer the instance to connect
	 */
	public void setCustomer(PDCustomer customer) 
{		setCustomer(customer.getId());
	}
}
