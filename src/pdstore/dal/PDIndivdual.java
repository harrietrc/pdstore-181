package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Indivdual" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDIndivdual");
 * @author PDGen
 */
public class PDIndivdual implements PDInstance {

	public static final GUID typeId = new GUID("ec5eec84c34111e199d1441ea1dc5b4f"); 

	public static final GUID roleHasNameId = new GUID("ec5eec85c34111e199d1441ea1dc5b4f");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDIndivdual.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDIndivdual:" + name;
		else
			return "PDIndivdual:" + id;
	}

	/**
	 * Creates an PDIndivdual object representing a PDStore instance of type Indivdual.
	 * @param store the store the instance should be in
	 */
	public PDIndivdual(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDIndivdual object representing the instance with the given ID. 
	 * The ID must be of an instance of type Indivdual.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDIndivdual(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Indivdual with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDIndivdual load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDIndivdual)instance;
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
	 * Returns the instance connected to this instance through the role hasName.
	 * @return the connected instance
	 */
	public String getHasName()
 {
	 	return (String)store.getInstance(this.id, roleHasNameId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role hasName.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getHasNames() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleHasNameId);
	 }

   /**
	 * Connects this instance to the given instance using role "hasName".
	 * If the given instance is null, nothing happens.
	 * @param hasName the instance to connect
	 */
	public void addHasName(String hasName) {
		if (hasName != null) {
			store.addLink(this.id, roleHasNameId, hasName);
		}
	}
	/**
	 * Connects this instance to the given instances using role "hasName".
	 * If the given collection of instances is null, nothing happens.
	 * @param hasName the Collection of instances to connect
	 */
	public void addHasNames(Collection<String> hasNames) {
		if (hasNames == null)
			return;
		for (String instance : hasNames)
			addHasName(instance);
	}

	/**
	 * Removes the link from this instance through role "hasName".
	 */
	public void removeHasName() {
		store.removeLink(this.id, roleHasNameId,
			store.getInstance(this.id, roleHasNameId));
	}

	/**
	 * Removes the link from this instance through role "hasName" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHasName(String hasName) {
		if (hasName == null)
			return;
		store.removeLink(this.id, roleHasNameId, hasName);
	}

	/**
	 * Connects this instance to the given instance using role "hasName".
	 * If there is already an instance connected to this instance through role "hasName", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param hasName the instance to connect
	 */
	public void setHasName(String hasName) {
		store.setLink(this.id,  roleHasNameId, hasName);
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
