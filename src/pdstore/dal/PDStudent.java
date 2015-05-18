package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Student" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDStudent");
 * @author PDGen
 */
public class PDStudent implements PDInstance {

	public static final GUID typeId = new GUID("ed189f93c34111e199d1441ea1dc5b4f"); 

	public static final GUID roleHasAgeId = new GUID("ed189f94c34111e199d1441ea1dc5b4f");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDStudent.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDStudent:" + name;
		else
			return "PDStudent:" + id;
	}

	/**
	 * Creates an PDStudent object representing a PDStore instance of type Student.
	 * @param store the store the instance should be in
	 */
	public PDStudent(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDStudent object representing the instance with the given ID. 
	 * The ID must be of an instance of type Student.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDStudent(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Student with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDStudent load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDStudent)instance;
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
	 * Returns the instance connected to this instance through the role hasAge.
	 * @return the connected instance
	 */
	public Long getHasAge()
 {
	 	return (Long)store.getInstance(this.id, roleHasAgeId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role hasAge.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getHasAges() {
		return (Collection<Long>)(Object)store.getInstances(this.id, roleHasAgeId);
	 }

   /**
	 * Connects this instance to the given instance using role "hasAge".
	 * If the given instance is null, nothing happens.
	 * @param hasAge the instance to connect
	 */
	public void addHasAge(Long hasAge) {
		if (hasAge != null) {
			store.addLink(this.id, roleHasAgeId, hasAge);
		}
	}
	/**
	 * Connects this instance to the given instances using role "hasAge".
	 * If the given collection of instances is null, nothing happens.
	 * @param hasAge the Collection of instances to connect
	 */
	public void addHasAges(Collection<Long> hasAges) {
		if (hasAges == null)
			return;
		for (Long instance : hasAges)
			addHasAge(instance);
	}

	/**
	 * Removes the link from this instance through role "hasAge".
	 */
	public void removeHasAge() {
		store.removeLink(this.id, roleHasAgeId,
			store.getInstance(this.id, roleHasAgeId));
	}

	/**
	 * Removes the link from this instance through role "hasAge" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHasAge(Long hasAge) {
		if (hasAge == null)
			return;
		store.removeLink(this.id, roleHasAgeId, hasAge);
	}

	/**
	 * Connects this instance to the given instance using role "hasAge".
	 * If there is already an instance connected to this instance through role "hasAge", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param hasAge the instance to connect
	 */
	public void setHasAge(Long hasAge) {
		store.setLink(this.id,  roleHasAgeId, hasAge);
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
