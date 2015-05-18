package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Address Book" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDAddressBook");
 * @author PDGen
 */
public class PDAddressBook implements PDInstance,PDAddressBookI{

	public static final GUID typeId = new GUID("e85c1f278e0ede11980f9a097666e103"); 

	public static final GUID roleLabelId = new GUID("afba85348e0ede11980f9a097666e103");
	public static final GUID roleSubAddressBookId = new GUID("115d1f278e0ede11980f9a097666e103");
	public static final GUID roleContactId = new GUID("eb5c1f278e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDAddressBook.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDAddressBook:" + name;
		else
			return "PDAddressBook:" + id;
	}

	/**
	 * Creates an PDAddressBook object representing a PDStore instance of type Address Book.
	 * @param store the store the instance should be in
	 */
	public PDAddressBook(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDAddressBook object representing the instance with the given ID. 
	 * The ID must be of an instance of type Address Book.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDAddressBook(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Address Book with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDAddressBook load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDAddressBook)instance;
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
	 * Returns the instance connected to this instance through the role label.
	 * @return the connected instance
	 */
	public String getLabel(){
	 	return (String)store.getInstance(this.id, roleLabelId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role label.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getLabels(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleLabelId);
	 }

   /**
	 * Connects this instance to the given instance using role "label".
	 * If the given instance is null, nothing happens.
	 * @param label the instance to connect
	 */
	public void addLabel(String label){
		if (label != null) {
			store.addLink(this.id, roleLabelId, label);
		}
	}
	/**
	 * Connects this instance to the given instances using role "label".
	 * If the given collection of instances is null, nothing happens.
	 * @param label the Collection of instances to connect
	 */
	public void addLabels(Collection<String> labels){
		if (labels == null)
			return;
		for (String instance : labels)
			addLabel(instance);
	}

	/**
	 * Removes the link from this instance through role "label".
	 */
	public void removeLabel(){
		store.removeLink(this.id, roleLabelId,
			store.getInstance(this.id, roleLabelId));
	}

	/**
	 * Removes the link from this instance through role "label" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeLabel(String label){
		if (label == null)
			return;
		store.removeLink(this.id, roleLabelId, label);
	}

	/**
	 * Connects this instance to the given instance using role "label".
	 * If there is already an instance connected to this instance through role "label", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param label the instance to connect
	 */
	public void setLabel(String label) {
		store.setLink(this.id,  roleLabelId, label);
	}
	/**
	 * Returns the instance connected to this instance through the role sub address book.
	 * @return the connected instance
	 */
	public PDAddressBook getSubAddressBook(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleSubAddressBookId);
	 	return (PDAddressBook) store.load(PDAddressBook.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role sub address book.
	 * @return the connected instance(s)
	 */
	 public Collection<PDAddressBook> getSubAddressBooks(){
		return (Collection<PDAddressBook>)(Object)store.getAndLoadInstances(this.id, roleSubAddressBookId, PDAddressBook.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "sub address book".
	 * If the given instance is null, nothing happens.
	 * @param subAddressBook the instance to connect
	 */
	public void addSubAddressBook(GUID subAddressBook){
		if (subAddressBook != null) {
			store.addLink(this.id, roleSubAddressBookId, subAddressBook);
		}
	}
	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If the given instance is null, nothing happens.
	 * @param subAddressBook the instance to connect
	 */
	public void addSubAddressBook(PDAddressBook subAddressBook){
		if (subAddressBook != null)
			addSubAddressBook(subAddressBook.getId());
	}

	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If the given collection of instances is null, nothing happens.
	 * @param subAddressBook the Collection of instances to connect
	 */
	public void addSubAddressBooks(Collection<PDAddressBook> subAddressBooks){
		if (subAddressBooks == null)
			return;
		for (PDAddressBook instance : subAddressBooks)
			addSubAddressBook(instance);
	}

	/**
	 * Removes the link from this instance through role "sub address book".
	 */
	public void removeSubAddressBook(){
		store.removeLink(this.id, roleSubAddressBookId,
			store.getInstance(this.id, roleSubAddressBookId));
	}

	/**
	 * Removes the link from this instance through role "sub address book" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSubAddressBook(PDAddressBook subAddressBook){
		if (subAddressBook == null)
			return;
		store.removeLink(this.id, roleSubAddressBookId, subAddressBook.getId());
	}

	/**
	 * Removes the links from this instance through role "sub address book" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSubAddressBooks(Collection<PDAddressBook> subAddressBooks){
		if (subAddressBooks == null)
			return;
		for (PDAddressBook instance : subAddressBooks)
			store.removeLink(this.id, roleSubAddressBookId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If there is already an instance connected to this instance through role "sub address book", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subAddressBook the instance to connect
	 */
	public void setSubAddressBook(GUID subAddressBook) {
		store.setLink(this.id,  roleSubAddressBookId, subAddressBook);
	}
	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If there is already an instance connected to this instance through role "sub address book", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subAddressBook the instance to connect
	 */
	public void setSubAddressBook(PDAddressBook subAddressBook) 
{		setSubAddressBook(subAddressBook.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role contact.
	 * @return the connected instance
	 */
	public PDContact getContact(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleContactId);
	 	return (PDContact) store.load(PDContact.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role contact.
	 * @return the connected instance(s)
	 */
	 public Collection<PDContact> getContacts(){
		return (Collection<PDContact>)(Object)store.getAndLoadInstances(this.id, roleContactId, PDContact.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "contact".
	 * If the given instance is null, nothing happens.
	 * @param contact the instance to connect
	 */
	public void addContact(GUID contact){
		if (contact != null) {
			store.addLink(this.id, roleContactId, contact);
		}
	}
	/**
	 * Connects this instance to the given instance using role "contact".
	 * If the given instance is null, nothing happens.
	 * @param contact the instance to connect
	 */
	public void addContact(PDContact contact){
		if (contact != null)
			addContact(contact.getId());
	}

	/**
	 * Connects this instance to the given instance using role "contact".
	 * If the given collection of instances is null, nothing happens.
	 * @param contact the Collection of instances to connect
	 */
	public void addContacts(Collection<PDContact> contacts){
		if (contacts == null)
			return;
		for (PDContact instance : contacts)
			addContact(instance);
	}

	/**
	 * Removes the link from this instance through role "contact".
	 */
	public void removeContact(){
		store.removeLink(this.id, roleContactId,
			store.getInstance(this.id, roleContactId));
	}

	/**
	 * Removes the link from this instance through role "contact" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeContact(PDContact contact){
		if (contact == null)
			return;
		store.removeLink(this.id, roleContactId, contact.getId());
	}

	/**
	 * Removes the links from this instance through role "contact" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeContacts(Collection<PDContact> contacts){
		if (contacts == null)
			return;
		for (PDContact instance : contacts)
			store.removeLink(this.id, roleContactId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "contact".
	 * If there is already an instance connected to this instance through role "contact", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param contact the instance to connect
	 */
	public void setContact(GUID contact) {
		store.setLink(this.id,  roleContactId, contact);
	}
	/**
	 * Connects this instance to the given instance using role "contact".
	 * If there is already an instance connected to this instance through role "contact", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param contact the instance to connect
	 */
	public void setContact(PDContact contact) 
{		setContact(contact.getId());
	}
}
