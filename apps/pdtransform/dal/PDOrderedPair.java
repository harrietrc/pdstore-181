package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Ordered Pair" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDOrderedPair");
 * @author PDGen
 */
public class PDOrderedPair implements PDInstance,PDOrderedPairI{

	public static final GUID typeId = new GUID("b3ba85348e0ede11980f9a097666e103"); 

	public static final GUID roleNextId = new GUID("bbba85348e0ede11980f9a097666e103");
	public static final GUID rolePrevId = new GUID("b9ba85348e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDOrderedPair.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDOrderedPair:" + name;
		else
			return "PDOrderedPair:" + id;
	}

	/**
	 * Creates an PDOrderedPair object representing a PDStore instance of type Ordered Pair.
	 * @param store the store the instance should be in
	 */
	public PDOrderedPair(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDOrderedPair object representing the instance with the given ID. 
	 * The ID must be of an instance of type Ordered Pair.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDOrderedPair(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Ordered Pair with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDOrderedPair load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDOrderedPair)instance;
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
	 * Returns the instance connected to this instance through the role next.
	 * @return the connected instance
	 */
	public GUID getNext(){
	 	return (GUID)store.getInstance(this.id, roleNextId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role next.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getNexts(){
		return (Collection<GUID>)(Object)store.getInstances(this.id, roleNextId);
	 }

   /**
	 * Connects this instance to the given instance using role "next".
	 * If the given instance is null, nothing happens.
	 * @param next the instance to connect
	 */
	public void addNext(GUID next){
		if (next != null) {
			store.addLink(this.id, roleNextId, next);
		}
	}
	/**
	 * Connects this instance to the given instances using role "next".
	 * If the given collection of instances is null, nothing happens.
	 * @param next the Collection of instances to connect
	 */
	public void addNexts(Collection<GUID> nexts){
		if (nexts == null)
			return;
		for (GUID instance : nexts)
			addNext(instance);
	}

	/**
	 * Removes the link from this instance through role "next".
	 */
	public void removeNext(){
		store.removeLink(this.id, roleNextId,
			store.getInstance(this.id, roleNextId));
	}

	/**
	 * Removes the link from this instance through role "next" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNext(GUID next){
		if (next == null)
			return;
		store.removeLink(this.id, roleNextId, next);
	}

	/**
	 * Connects this instance to the given instance using role "next".
	 * If there is already an instance connected to this instance through role "next", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param next the instance to connect
	 */
	public void setNext(GUID next) {
		store.setLink(this.id,  roleNextId, next);
	}
	/**
	 * Returns the instance connected to this instance through the role prev.
	 * @return the connected instance
	 */
	public GUID getPrev(){
	 	return (GUID)store.getInstance(this.id, rolePrevId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role prev.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getPrevs(){
		return (Collection<GUID>)(Object)store.getInstances(this.id, rolePrevId);
	 }

   /**
	 * Connects this instance to the given instance using role "prev".
	 * If the given instance is null, nothing happens.
	 * @param prev the instance to connect
	 */
	public void addPrev(GUID prev){
		if (prev != null) {
			store.addLink(this.id, rolePrevId, prev);
		}
	}
	/**
	 * Connects this instance to the given instances using role "prev".
	 * If the given collection of instances is null, nothing happens.
	 * @param prev the Collection of instances to connect
	 */
	public void addPrevs(Collection<GUID> prevs){
		if (prevs == null)
			return;
		for (GUID instance : prevs)
			addPrev(instance);
	}

	/**
	 * Removes the link from this instance through role "prev".
	 */
	public void removePrev(){
		store.removeLink(this.id, rolePrevId,
			store.getInstance(this.id, rolePrevId));
	}

	/**
	 * Removes the link from this instance through role "prev" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrev(GUID prev){
		if (prev == null)
			return;
		store.removeLink(this.id, rolePrevId, prev);
	}

	/**
	 * Connects this instance to the given instance using role "prev".
	 * If there is already an instance connected to this instance through role "prev", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param prev the instance to connect
	 */
	public void setPrev(GUID prev) {
		store.setLink(this.id,  rolePrevId, prev);
	}
}
