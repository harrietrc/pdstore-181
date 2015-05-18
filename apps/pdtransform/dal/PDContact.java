package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Contact" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDContact");
 * @author PDGen
 */
public class PDContact implements PDInstance,PDContactI{

	public static final GUID typeId = new GUID("e95c1f278e0ede11980f9a097666e103"); 

	public static final GUID roleAddressId = new GUID("0f5d1f278e0ede11980f9a097666e103");
	public static final GUID roleContactsNameId = new GUID("0d5d1f278e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDContact.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDContact:" + name;
		else
			return "PDContact:" + id;
	}

	/**
	 * Creates an PDContact object representing a PDStore instance of type Contact.
	 * @param store the store the instance should be in
	 */
	public PDContact(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDContact object representing the instance with the given ID. 
	 * The ID must be of an instance of type Contact.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDContact(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Contact with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDContact load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDContact)instance;
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
	public String getAddress(){
	 	return (String)store.getInstance(this.id, roleAddressId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role address.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getAddresss(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleAddressId);
	 }

   /**
	 * Connects this instance to the given instance using role "address".
	 * If the given instance is null, nothing happens.
	 * @param address the instance to connect
	 */
	public void addAddress(String address){
		if (address != null) {
			store.addLink(this.id, roleAddressId, address);
		}
	}
	/**
	 * Connects this instance to the given instances using role "address".
	 * If the given collection of instances is null, nothing happens.
	 * @param address the Collection of instances to connect
	 */
	public void addAddresss(Collection<String> addresss){
		if (addresss == null)
			return;
		for (String instance : addresss)
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
	public void removeAddress(String address){
		if (address == null)
			return;
		store.removeLink(this.id, roleAddressId, address);
	}

	/**
	 * Connects this instance to the given instance using role "address".
	 * If there is already an instance connected to this instance through role "address", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param address the instance to connect
	 */
	public void setAddress(String address) {
		store.setLink(this.id,  roleAddressId, address);
	}
	/**
	 * Returns the instance connected to this instance through the role contacts name.
	 * @return the connected instance
	 */
	public String getContactsName(){
	 	return (String)store.getInstance(this.id, roleContactsNameId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role contacts name.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getContactsNames(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleContactsNameId);
	 }

   /**
	 * Connects this instance to the given instance using role "contacts name".
	 * If the given instance is null, nothing happens.
	 * @param contactsName the instance to connect
	 */
	public void addContactsName(String contactsName){
		if (contactsName != null) {
			store.addLink(this.id, roleContactsNameId, contactsName);
		}
	}
	/**
	 * Connects this instance to the given instances using role "contacts name".
	 * If the given collection of instances is null, nothing happens.
	 * @param contactsName the Collection of instances to connect
	 */
	public void addContactsNames(Collection<String> contactsNames){
		if (contactsNames == null)
			return;
		for (String instance : contactsNames)
			addContactsName(instance);
	}

	/**
	 * Removes the link from this instance through role "contacts name".
	 */
	public void removeContactsName(){
		store.removeLink(this.id, roleContactsNameId,
			store.getInstance(this.id, roleContactsNameId));
	}

	/**
	 * Removes the link from this instance through role "contacts name" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeContactsName(String contactsName){
		if (contactsName == null)
			return;
		store.removeLink(this.id, roleContactsNameId, contactsName);
	}

	/**
	 * Connects this instance to the given instance using role "contacts name".
	 * If there is already an instance connected to this instance through role "contacts name", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param contactsName the instance to connect
	 */
	public void setContactsName(String contactsName) {
		store.setLink(this.id,  roleContactsNameId, contactsName);
	}
}
