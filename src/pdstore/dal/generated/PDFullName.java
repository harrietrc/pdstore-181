package pdstore.dal.generated;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "FullName" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.generated.PDFullName");
 * @author PDGen
 */
public class PDFullName implements PDInstance {

	public static final GUID typeId = new GUID("f7b81e92685211dfb501020054554e03"); 

	public static final GUID rolePersonId = new GUID("f7b81e92685211dfb511020054554e04");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDFullName.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDFullName:" + name;
		else
			return "PDFullName:" + id;
	}

	/**
	 * Creates an PDFullName object representing a PDStore instance of type FullName.
	 * @param store the store the instance should be in
	 */
	public PDFullName(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDFullName object representing the instance with the given ID. 
	 * The ID must be of an instance of type FullName.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDFullName(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type FullName with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDFullName load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDFullName)instance;
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
	 * Returns the instance connected to this instance through the role Person.
	 * @return the connected instance
	 */
	public PDPerson getPerson()
 {
		GUID instanceId = (GUID) store.getInstance(this.id, rolePersonId);
	 	return (PDPerson) store.load(PDPerson.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Person.
	 * @return the connected instance(s)
	 */
	 public Collection<PDPerson> getPersons() {
		return (Collection<PDPerson>)(Object)store.getAndLoadInstances(this.id, rolePersonId, PDPerson.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Person".
	 * If the given instance is null, nothing happens.
	 * @param person the instance to connect
	 */
	public void addPerson(GUID person) {
		if (person != null) {
			store.addLink(this.id, rolePersonId, person);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Person".
	 * If the given instance is null, nothing happens.
	 * @param person the instance to connect
	 */
	public void addPerson(PDPerson person) {
		if (person != null)
			addPerson(person.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Person".
	 * If the given collection of instances is null, nothing happens.
	 * @param person the Collection of instances to connect
	 */
	public void addPersons(Collection<PDPerson> persons) {
		if (persons == null)
			return;
		for (PDPerson instance : persons)
			addPerson(instance);
	}

	/**
	 * Removes the link from this instance through role "Person".
	 */
	public void removePerson() {
		store.removeLink(this.id, rolePersonId,
			store.getInstance(this.id, rolePersonId));
	}

	/**
	 * Removes the link from this instance through role "Person" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePerson(PDPerson person) {
		if (person == null)
			return;
		store.removeLink(this.id, rolePersonId, person.getId());
	}

	/**
	 * Removes the links from this instance through role "Person" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removePersons(Collection<PDPerson> persons) {
		if (persons == null)
			return;
		for (PDPerson instance : persons)
			store.removeLink(this.id, rolePersonId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Person".
	 * If there is already an instance connected to this instance through role "Person", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param person the instance to connect
	 */
	public void setPerson(GUID person) {
		store.setLink(this.id,  rolePersonId, person);
	}
	/**
	 * Connects this instance to the given instance using role "Person".
	 * If there is already an instance connected to this instance through role "Person", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param person the instance to connect
	 */
	public void setPerson(PDPerson person) {
		setPerson(person.getId());
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
