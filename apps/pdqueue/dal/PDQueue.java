package pdqueue.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;

/**
 * Data access class to represent instances of type "Queue" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdqueue.dal.PDQueue");
 * @author PDGen
 */
public class PDQueue implements PDInstance {

	public static final GUID typeId = new GUID("0774e9a8540f11e0a4530024e80616c7"); 

	public static final GUID roleServiceTypeId = new GUID("27070a41100111e1aee00024e80616c7");
	public static final GUID roleItemId = new GUID("899ecc6cd76111e09b030024e80616c7");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDQueue.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDQueue:" + name;
		else
			return "PDQueue:" + id;
	}

	/**
	 * Creates an PDQueue object representing a PDStore instance of type Queue.
	 * @param store the store the instance should be in
	 */
	public PDQueue(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDQueue object representing the instance with the given ID. 
	 * The ID must be of an instance of type Queue.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDQueue(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Queue with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDQueue load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDQueue)instance;
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
	 * Returns the instance connected to this instance through the role ServiceType.
	 * @return the connected instance
	 */
	public String getServiceType() {
	 	return (String)store.getInstance(this.id, roleServiceTypeId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role ServiceType.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getServiceTypes() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleServiceTypeId);
	 }

   /**
	 * Connects this instance to the given instance using role "ServiceType".
	 * If the given instance is null, nothing happens.
	 * @param serviceType the instance to connect
	 */
	public void addServiceType(String serviceType) {
		if (serviceType != null) {
			store.addLink(this.id, roleServiceTypeId, serviceType);
		}
	}
	/**
	 * Connects this instance to the given instances using role "ServiceType".
	 * If the given collection of instances is null, nothing happens.
	 * @param serviceType the Collection of instances to connect
	 */
	public void addServiceTypes(Collection<String> serviceTypes) {
		if (serviceTypes == null)
			return;
		for (String instance : serviceTypes)
			addServiceType(instance);
	}

	/**
	 * Removes the link from this instance through role "ServiceType".
	 */
	public void removeServiceType() {
		store.removeLink(this.id, roleServiceTypeId,
			store.getInstance(this.id, roleServiceTypeId));
	}

	/**
	 * Removes the link from this instance through role "ServiceType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeServiceType(Object serviceType) {
		if (serviceType == null)
			return;
		store.removeLink(this.id, roleServiceTypeId, serviceType);
	}

	/**
	 * Connects this instance to the given instance using role "ServiceType".
	 * If there is already an instance connected to this instance through role "ServiceType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param serviceType the instance to connect
	 */
	public void setServiceType(String serviceType) {
		store.setLink(this.id,  roleServiceTypeId, serviceType);
	}
	/**
	 * Returns the instance connected to this instance through the role Item.
	 * @return the connected instance
	 */
	public PDItem getItem() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleItemId);
	 	return (PDItem) store.load(PDItem.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Item.
	 * @return the connected instance(s)
	 */
	 public Collection<PDItem> getItems() {
		return (Collection<PDItem>)(Object)store.getAndLoadInstances(this.id, roleItemId, PDItem.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Item".
	 * If the given instance is null, nothing happens.
	 * @param item the instance to connect
	 */
	public void addItem(GUID item) {
		if (item != null) {
			store.addLink(this.id, roleItemId, item);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Item".
	 * If the given instance is null, nothing happens.
	 * @param item the instance to connect
	 */
	public void addItem(PDItem item) {
		if (item != null)
			addItem(item.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Item".
	 * If the given collection of instances is null, nothing happens.
	 * @param item the Collection of instances to connect
	 */
	public void addItems(Collection<PDItem> items) {
		if (items == null)
			return;
		for (PDItem instance : items)
			addItem(instance);
	}

	/**
	 * Removes the link from this instance through role "Item".
	 */
	public void removeItem() {
		store.removeLink(this.id, roleItemId,
			store.getInstance(this.id, roleItemId));
	}

	/**
	 * Removes the link from this instance through role "Item" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeItem(Object item) {
		if (item == null)
			return;
		store.removeLink(this.id, roleItemId, item);
	}

	/**
	 * Removes the links from this instance through role "Item" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeItems(Collection<PDItem> items) {
		if (items == null)
			return;
		for (PDItem instance : items)
			store.removeLink(this.id, roleItemId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "Item".
	 * If there is already an instance connected to this instance through role "Item", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param item the instance to connect
	 */
	public void setItem(GUID item) {
		store.setLink(this.id,  roleItemId, item);
	}
	/**
	 * Connects this instance to the given instance using role "Item".
	 * If there is already an instance connected to this instance through role "Item", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param item the instance to connect
	 */
	public void setItem(PDItem item) {
		setItem(item.getId());
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
