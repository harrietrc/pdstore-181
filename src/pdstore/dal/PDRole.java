package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Role" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDRole");
 * @author PDGen
 */
public class PDRole implements PDInstance,PDRoleI{

	public static final GUID typeId = new GUID("528a986c4062db11afc0b95b08f50e2f"); 

	public static final GUID roleUserId = new GUID("cdc181d1319311e1b11c1cc1dec00ed3");
	public static final GUID roleMaxMultId = new GUID("698a986c4062db11afc0b95b08f50e2f");
	public static final GUID roleMinMultId = new GUID("678a986c4062db11afc0b95b08f50e2f");
	public static final GUID rolePartnerId = new GUID("6d8a986c4062db11afc0b95b08f50e2f");
	public static final GUID roleOwnerId = new GUID("648a986c4062db11afd0b95b08f50e2f");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDRole.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDRole:" + name;
		else
			return "PDRole:" + id;
	}

	/**
	 * Creates an PDRole object representing a PDStore instance of type Role.
	 * @param store the store the instance should be in
	 */
	public PDRole(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDRole object representing the instance with the given ID. 
	 * The ID must be of an instance of type Role.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDRole(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Role with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDRole load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDRole)instance;
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
	 * Returns the instance connected to this instance through the role user.
	 * @return the connected instance
	 */
	public PDInstance getUser(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleUserId);
	 	return (PDInstance) store.load(instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role user.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getUsers(){
		return (Collection<PDInstance>)(Object)store.getAndLoadInstances(this.id, roleUserId);
	 }

   /**
	 * Connects this instance to the given instance using role "user".
	 * If the given instance is null, nothing happens.
	 * @param user the instance to connect
	 */
	public void addUser(GUID user){
		if (user != null) {
			store.addLink(this.id, roleUserId, user);
		}
	}
	/**
	 * Connects this instance to the given instance using role "user".
	 * If the given instance is null, nothing happens.
	 * @param user the instance to connect
	 */
	public void addUser(PDInstance user){
		if (user != null)
			addUser(user.getId());
	}

	/**
	 * Connects this instance to the given instance using role "user".
	 * If the given collection of instances is null, nothing happens.
	 * @param user the Collection of instances to connect
	 */
	public void addUsers(Collection<PDInstance> users){
		if (users == null)
			return;
		for (PDInstance instance : users)
			addUser(instance);
	}

	/**
	 * Removes the link from this instance through role "user".
	 */
	public void removeUser(){
		store.removeLink(this.id, roleUserId,
			store.getInstance(this.id, roleUserId));
	}

	/**
	 * Removes the link from this instance through role "user" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeUser(PDInstance user){
		if (user == null)
			return;
		store.removeLink(this.id, roleUserId, user.getId());
	}

	/**
	 * Removes the links from this instance through role "user" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeUsers(Collection<PDInstance> users){
		if (users == null)
			return;
		for (PDInstance instance : users)
			store.removeLink(this.id, roleUserId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "user".
	 * If there is already an instance connected to this instance through role "user", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param user the instance to connect
	 */
	public void setUser(GUID user) {
		store.setLink(this.id,  roleUserId, user);
	}
	/**
	 * Connects this instance to the given instance using role "user".
	 * If there is already an instance connected to this instance through role "user", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param user the instance to connect
	 */
	public void setUser(PDInstance user) 
{		setUser(user.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role max mult.
	 * @return the connected instance
	 */
	public Long getMaxMult(){
	 	return (Long)store.getInstance(this.id, roleMaxMultId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role max mult.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getMaxMults(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleMaxMultId);
	 }

   /**
	 * Connects this instance to the given instance using role "max mult".
	 * If the given instance is null, nothing happens.
	 * @param maxMult the instance to connect
	 */
	public void addMaxMult(Long maxMult){
		if (maxMult != null) {
			store.addLink(this.id, roleMaxMultId, maxMult);
		}
	}
	/**
	 * Connects this instance to the given instances using role "max mult".
	 * If the given collection of instances is null, nothing happens.
	 * @param maxMult the Collection of instances to connect
	 */
	public void addMaxMults(Collection<Long> maxMults){
		if (maxMults == null)
			return;
		for (Long instance : maxMults)
			addMaxMult(instance);
	}

	/**
	 * Removes the link from this instance through role "max mult".
	 */
	public void removeMaxMult(){
		store.removeLink(this.id, roleMaxMultId,
			store.getInstance(this.id, roleMaxMultId));
	}

	/**
	 * Removes the link from this instance through role "max mult" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeMaxMult(Long maxMult){
		if (maxMult == null)
			return;
		store.removeLink(this.id, roleMaxMultId, maxMult);
	}

	/**
	 * Connects this instance to the given instance using role "max mult".
	 * If there is already an instance connected to this instance through role "max mult", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param maxMult the instance to connect
	 */
	public void setMaxMult(Long maxMult) {
		store.setLink(this.id,  roleMaxMultId, maxMult);
	}
	/**
	 * Returns the instance connected to this instance through the role min mult.
	 * @return the connected instance
	 */
	public Long getMinMult(){
	 	return (Long)store.getInstance(this.id, roleMinMultId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role min mult.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getMinMults(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleMinMultId);
	 }

   /**
	 * Connects this instance to the given instance using role "min mult".
	 * If the given instance is null, nothing happens.
	 * @param minMult the instance to connect
	 */
	public void addMinMult(Long minMult){
		if (minMult != null) {
			store.addLink(this.id, roleMinMultId, minMult);
		}
	}
	/**
	 * Connects this instance to the given instances using role "min mult".
	 * If the given collection of instances is null, nothing happens.
	 * @param minMult the Collection of instances to connect
	 */
	public void addMinMults(Collection<Long> minMults){
		if (minMults == null)
			return;
		for (Long instance : minMults)
			addMinMult(instance);
	}

	/**
	 * Removes the link from this instance through role "min mult".
	 */
	public void removeMinMult(){
		store.removeLink(this.id, roleMinMultId,
			store.getInstance(this.id, roleMinMultId));
	}

	/**
	 * Removes the link from this instance through role "min mult" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeMinMult(Long minMult){
		if (minMult == null)
			return;
		store.removeLink(this.id, roleMinMultId, minMult);
	}

	/**
	 * Connects this instance to the given instance using role "min mult".
	 * If there is already an instance connected to this instance through role "min mult", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param minMult the instance to connect
	 */
	public void setMinMult(Long minMult) {
		store.setLink(this.id,  roleMinMultId, minMult);
	}
	/**
	 * Returns the instance connected to this instance through the role partner.
	 * @return the connected instance
	 */
	public PDRole getPartner(){
		GUID instanceId = (GUID) store.getInstance(this.id, rolePartnerId);
	 	return (PDRole) store.load(PDRole.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role partner.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getPartners(){
		return (Collection<PDRole>)(Object)store.getAndLoadInstances(this.id, rolePartnerId, PDRole.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "partner".
	 * If the given instance is null, nothing happens.
	 * @param partner the instance to connect
	 */
	public void addPartner(GUID partner){
		if (partner != null) {
			store.addLink(this.id, rolePartnerId, partner);
		}
	}
	/**
	 * Connects this instance to the given instance using role "partner".
	 * If the given instance is null, nothing happens.
	 * @param partner the instance to connect
	 */
	public void addPartner(PDRole partner){
		if (partner != null)
			addPartner(partner.getId());
	}

	/**
	 * Connects this instance to the given instance using role "partner".
	 * If the given collection of instances is null, nothing happens.
	 * @param partner the Collection of instances to connect
	 */
	public void addPartners(Collection<PDRole> partners){
		if (partners == null)
			return;
		for (PDRole instance : partners)
			addPartner(instance);
	}

	/**
	 * Removes the link from this instance through role "partner".
	 */
	public void removePartner(){
		store.removeLink(this.id, rolePartnerId,
			store.getInstance(this.id, rolePartnerId));
	}

	/**
	 * Removes the link from this instance through role "partner" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePartner(PDRole partner){
		if (partner == null)
			return;
		store.removeLink(this.id, rolePartnerId, partner.getId());
	}

	/**
	 * Removes the links from this instance through role "partner" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removePartners(Collection<PDRole> partners){
		if (partners == null)
			return;
		for (PDRole instance : partners)
			store.removeLink(this.id, rolePartnerId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "partner".
	 * If there is already an instance connected to this instance through role "partner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param partner the instance to connect
	 */
	public void setPartner(GUID partner) {
		store.setLink(this.id,  rolePartnerId, partner);
	}
	/**
	 * Connects this instance to the given instance using role "partner".
	 * If there is already an instance connected to this instance through role "partner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param partner the instance to connect
	 */
	public void setPartner(PDRole partner) 
{		setPartner(partner.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role owner.
	 * @return the connected instance
	 */
	public PDType getOwner(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOwnerId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role owner.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getOwners(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleOwnerId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "owner".
	 * If the given instance is null, nothing happens.
	 * @param owner the instance to connect
	 */
	public void addOwner(GUID owner){
		if (owner != null) {
			store.addLink(this.id, roleOwnerId, owner);
		}
	}
	/**
	 * Connects this instance to the given instance using role "owner".
	 * If the given instance is null, nothing happens.
	 * @param owner the instance to connect
	 */
	public void addOwner(PDType owner){
		if (owner != null)
			addOwner(owner.getId());
	}

	/**
	 * Connects this instance to the given instance using role "owner".
	 * If the given collection of instances is null, nothing happens.
	 * @param owner the Collection of instances to connect
	 */
	public void addOwners(Collection<PDType> owners){
		if (owners == null)
			return;
		for (PDType instance : owners)
			addOwner(instance);
	}

	/**
	 * Removes the link from this instance through role "owner".
	 */
	public void removeOwner(){
		store.removeLink(this.id, roleOwnerId,
			store.getInstance(this.id, roleOwnerId));
	}

	/**
	 * Removes the link from this instance through role "owner" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOwner(PDType owner){
		if (owner == null)
			return;
		store.removeLink(this.id, roleOwnerId, owner.getId());
	}

	/**
	 * Removes the links from this instance through role "owner" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOwners(Collection<PDType> owners){
		if (owners == null)
			return;
		for (PDType instance : owners)
			store.removeLink(this.id, roleOwnerId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "owner".
	 * If there is already an instance connected to this instance through role "owner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param owner the instance to connect
	 */
	public void setOwner(GUID owner) {
		store.setLink(this.id,  roleOwnerId, owner);
	}
	/**
	 * Connects this instance to the given instance using role "owner".
	 * If there is already an instance connected to this instance through role "owner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param owner the instance to connect
	 */
	public void setOwner(PDType owner) 
{		setOwner(owner.getId());
	}
}
