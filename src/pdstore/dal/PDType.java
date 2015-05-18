package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Type" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDType");
 * @author PDGen
 */
public class PDType implements PDInstance,PDTypeI{

	public static final GUID typeId = new GUID("518a986c4062db11afc0b95b08f50e2f"); 

	public static final GUID roleApplicableOperationId = new GUID("dbe74a3089cb11e197541cc1dec00ed3");
	public static final GUID roleHasInstanceId = new GUID("ee32adf0f68b11df861e1cc1dec00ed3");
	public static final GUID roleModelId = new GUID("54134d264e6fdd11a5cba737f860105f");
	public static final GUID roleOwnedRoleId = new GUID("648a986c4062db11afc0b95b08f50e2f");
	public static final GUID roleSubtypeId = new GUID("a093c5b1b6aa11e183cf842b2b9af4fd");
	public static final GUID roleSupertypeId = new GUID("a093c5b1b6aa11e183df842b2b9af4fd");
	public static final GUID roleIsPrimitiveId = new GUID("5d8a986c4062db11afc0b95b08f50e2f");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDType.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDType:" + name;
		else
			return "PDType:" + id;
	}

	/**
	 * Creates an PDType object representing a PDStore instance of type Type.
	 * @param store the store the instance should be in
	 */
	public PDType(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDType object representing the instance with the given ID. 
	 * The ID must be of an instance of type Type.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDType(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Type with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDType load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDType)instance;
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
	 * Returns the instance connected to this instance through the role applicable operation.
	 * @return the connected instance
	 */
	public PDOperation getApplicableOperation(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleApplicableOperationId);
	 	return (PDOperation) store.load(PDOperation.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role applicable operation.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperation> getApplicableOperations(){
		return (Collection<PDOperation>)(Object)store.getAndLoadInstances(this.id, roleApplicableOperationId, PDOperation.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If the given instance is null, nothing happens.
	 * @param applicableOperation the instance to connect
	 */
	public void addApplicableOperation(GUID applicableOperation){
		if (applicableOperation != null) {
			store.addLink(this.id, roleApplicableOperationId, applicableOperation);
		}
	}
	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If the given instance is null, nothing happens.
	 * @param applicableOperation the instance to connect
	 */
	public void addApplicableOperation(PDOperation applicableOperation){
		if (applicableOperation != null)
			addApplicableOperation(applicableOperation.getId());
	}

	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If the given collection of instances is null, nothing happens.
	 * @param applicableOperation the Collection of instances to connect
	 */
	public void addApplicableOperations(Collection<PDOperation> applicableOperations){
		if (applicableOperations == null)
			return;
		for (PDOperation instance : applicableOperations)
			addApplicableOperation(instance);
	}

	/**
	 * Removes the link from this instance through role "applicable operation".
	 */
	public void removeApplicableOperation(){
		store.removeLink(this.id, roleApplicableOperationId,
			store.getInstance(this.id, roleApplicableOperationId));
	}

	/**
	 * Removes the link from this instance through role "applicable operation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeApplicableOperation(PDOperation applicableOperation){
		if (applicableOperation == null)
			return;
		store.removeLink(this.id, roleApplicableOperationId, applicableOperation.getId());
	}

	/**
	 * Removes the links from this instance through role "applicable operation" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeApplicableOperations(Collection<PDOperation> applicableOperations){
		if (applicableOperations == null)
			return;
		for (PDOperation instance : applicableOperations)
			store.removeLink(this.id, roleApplicableOperationId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If there is already an instance connected to this instance through role "applicable operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param applicableOperation the instance to connect
	 */
	public void setApplicableOperation(GUID applicableOperation) {
		store.setLink(this.id,  roleApplicableOperationId, applicableOperation);
	}
	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If there is already an instance connected to this instance through role "applicable operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param applicableOperation the instance to connect
	 */
	public void setApplicableOperation(PDOperation applicableOperation) 
{		setApplicableOperation(applicableOperation.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role has instance.
	 * @return the connected instance
	 */
	public PDInstance getHasInstance(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleHasInstanceId);
	 	return (PDInstance) store.load(instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role has instance.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getHasInstances(){
		return (Collection<PDInstance>)(Object)store.getAndLoadInstances(this.id, roleHasInstanceId);
	 }

   /**
	 * Connects this instance to the given instance using role "has instance".
	 * If the given instance is null, nothing happens.
	 * @param hasInstance the instance to connect
	 */
	public void addHasInstance(GUID hasInstance){
		if (hasInstance != null) {
			store.addLink(this.id, roleHasInstanceId, hasInstance);
		}
	}
	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If the given instance is null, nothing happens.
	 * @param hasInstance the instance to connect
	 */
	public void addHasInstance(PDInstance hasInstance){
		if (hasInstance != null)
			addHasInstance(hasInstance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If the given collection of instances is null, nothing happens.
	 * @param hasInstance the Collection of instances to connect
	 */
	public void addHasInstances(Collection<PDInstance> hasInstances){
		if (hasInstances == null)
			return;
		for (PDInstance instance : hasInstances)
			addHasInstance(instance);
	}

	/**
	 * Removes the link from this instance through role "has instance".
	 */
	public void removeHasInstance(){
		store.removeLink(this.id, roleHasInstanceId,
			store.getInstance(this.id, roleHasInstanceId));
	}

	/**
	 * Removes the link from this instance through role "has instance" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHasInstance(PDInstance hasInstance){
		if (hasInstance == null)
			return;
		store.removeLink(this.id, roleHasInstanceId, hasInstance.getId());
	}

	/**
	 * Removes the links from this instance through role "has instance" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeHasInstances(Collection<PDInstance> hasInstances){
		if (hasInstances == null)
			return;
		for (PDInstance instance : hasInstances)
			store.removeLink(this.id, roleHasInstanceId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If there is already an instance connected to this instance through role "has instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param hasInstance the instance to connect
	 */
	public void setHasInstance(GUID hasInstance) {
		store.setLink(this.id,  roleHasInstanceId, hasInstance);
	}
	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If there is already an instance connected to this instance through role "has instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param hasInstance the instance to connect
	 */
	public void setHasInstance(PDInstance hasInstance) 
{		setHasInstance(hasInstance.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role model.
	 * @return the connected instance
	 */
	public PDModel getModel(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleModelId);
	 	return (PDModel) store.load(PDModel.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role model.
	 * @return the connected instance(s)
	 */
	 public Collection<PDModel> getModels(){
		return (Collection<PDModel>)(Object)store.getAndLoadInstances(this.id, roleModelId, PDModel.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "model".
	 * If the given instance is null, nothing happens.
	 * @param model the instance to connect
	 */
	public void addModel(GUID model){
		if (model != null) {
			store.addLink(this.id, roleModelId, model);
		}
	}
	/**
	 * Connects this instance to the given instance using role "model".
	 * If the given instance is null, nothing happens.
	 * @param model the instance to connect
	 */
	public void addModel(PDModel model){
		if (model != null)
			addModel(model.getId());
	}

	/**
	 * Connects this instance to the given instance using role "model".
	 * If the given collection of instances is null, nothing happens.
	 * @param model the Collection of instances to connect
	 */
	public void addModels(Collection<PDModel> models){
		if (models == null)
			return;
		for (PDModel instance : models)
			addModel(instance);
	}

	/**
	 * Removes the link from this instance through role "model".
	 */
	public void removeModel(){
		store.removeLink(this.id, roleModelId,
			store.getInstance(this.id, roleModelId));
	}

	/**
	 * Removes the link from this instance through role "model" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeModel(PDModel model){
		if (model == null)
			return;
		store.removeLink(this.id, roleModelId, model.getId());
	}

	/**
	 * Removes the links from this instance through role "model" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeModels(Collection<PDModel> models){
		if (models == null)
			return;
		for (PDModel instance : models)
			store.removeLink(this.id, roleModelId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "model".
	 * If there is already an instance connected to this instance through role "model", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param model the instance to connect
	 */
	public void setModel(GUID model) {
		store.setLink(this.id,  roleModelId, model);
	}
	/**
	 * Connects this instance to the given instance using role "model".
	 * If there is already an instance connected to this instance through role "model", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param model the instance to connect
	 */
	public void setModel(PDModel model) 
{		setModel(model.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role owned role.
	 * @return the connected instance
	 */
	public PDRole getOwnedRole(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOwnedRoleId);
	 	return (PDRole) store.load(PDRole.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role owned role.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getOwnedRoles(){
		return (Collection<PDRole>)(Object)store.getAndLoadInstances(this.id, roleOwnedRoleId, PDRole.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "owned role".
	 * If the given instance is null, nothing happens.
	 * @param ownedRole the instance to connect
	 */
	public void addOwnedRole(GUID ownedRole){
		if (ownedRole != null) {
			store.addLink(this.id, roleOwnedRoleId, ownedRole);
		}
	}
	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If the given instance is null, nothing happens.
	 * @param ownedRole the instance to connect
	 */
	public void addOwnedRole(PDRole ownedRole){
		if (ownedRole != null)
			addOwnedRole(ownedRole.getId());
	}

	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If the given collection of instances is null, nothing happens.
	 * @param ownedRole the Collection of instances to connect
	 */
	public void addOwnedRoles(Collection<PDRole> ownedRoles){
		if (ownedRoles == null)
			return;
		for (PDRole instance : ownedRoles)
			addOwnedRole(instance);
	}

	/**
	 * Removes the link from this instance through role "owned role".
	 */
	public void removeOwnedRole(){
		store.removeLink(this.id, roleOwnedRoleId,
			store.getInstance(this.id, roleOwnedRoleId));
	}

	/**
	 * Removes the link from this instance through role "owned role" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOwnedRole(PDRole ownedRole){
		if (ownedRole == null)
			return;
		store.removeLink(this.id, roleOwnedRoleId, ownedRole.getId());
	}

	/**
	 * Removes the links from this instance through role "owned role" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOwnedRoles(Collection<PDRole> ownedRoles){
		if (ownedRoles == null)
			return;
		for (PDRole instance : ownedRoles)
			store.removeLink(this.id, roleOwnedRoleId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If there is already an instance connected to this instance through role "owned role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param ownedRole the instance to connect
	 */
	public void setOwnedRole(GUID ownedRole) {
		store.setLink(this.id,  roleOwnedRoleId, ownedRole);
	}
	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If there is already an instance connected to this instance through role "owned role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param ownedRole the instance to connect
	 */
	public void setOwnedRole(PDRole ownedRole) 
{		setOwnedRole(ownedRole.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role subtype.
	 * @return the connected instance
	 */
	public PDType getSubtype(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleSubtypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role subtype.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getSubtypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleSubtypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "subtype".
	 * If the given instance is null, nothing happens.
	 * @param subtype the instance to connect
	 */
	public void addSubtype(GUID subtype){
		if (subtype != null) {
			store.addLink(this.id, roleSubtypeId, subtype);
		}
	}
	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If the given instance is null, nothing happens.
	 * @param subtype the instance to connect
	 */
	public void addSubtype(PDType subtype){
		if (subtype != null)
			addSubtype(subtype.getId());
	}

	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If the given collection of instances is null, nothing happens.
	 * @param subtype the Collection of instances to connect
	 */
	public void addSubtypes(Collection<PDType> subtypes){
		if (subtypes == null)
			return;
		for (PDType instance : subtypes)
			addSubtype(instance);
	}

	/**
	 * Removes the link from this instance through role "subtype".
	 */
	public void removeSubtype(){
		store.removeLink(this.id, roleSubtypeId,
			store.getInstance(this.id, roleSubtypeId));
	}

	/**
	 * Removes the link from this instance through role "subtype" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSubtype(PDType subtype){
		if (subtype == null)
			return;
		store.removeLink(this.id, roleSubtypeId, subtype.getId());
	}

	/**
	 * Removes the links from this instance through role "subtype" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSubtypes(Collection<PDType> subtypes){
		if (subtypes == null)
			return;
		for (PDType instance : subtypes)
			store.removeLink(this.id, roleSubtypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If there is already an instance connected to this instance through role "subtype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subtype the instance to connect
	 */
	public void setSubtype(GUID subtype) {
		store.setLink(this.id,  roleSubtypeId, subtype);
	}
	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If there is already an instance connected to this instance through role "subtype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subtype the instance to connect
	 */
	public void setSubtype(PDType subtype) 
{		setSubtype(subtype.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role supertype.
	 * @return the connected instance
	 */
	public PDType getSupertype(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleSupertypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role supertype.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getSupertypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleSupertypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "supertype".
	 * If the given instance is null, nothing happens.
	 * @param supertype the instance to connect
	 */
	public void addSupertype(GUID supertype){
		if (supertype != null) {
			store.addLink(this.id, roleSupertypeId, supertype);
		}
	}
	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If the given instance is null, nothing happens.
	 * @param supertype the instance to connect
	 */
	public void addSupertype(PDType supertype){
		if (supertype != null)
			addSupertype(supertype.getId());
	}

	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If the given collection of instances is null, nothing happens.
	 * @param supertype the Collection of instances to connect
	 */
	public void addSupertypes(Collection<PDType> supertypes){
		if (supertypes == null)
			return;
		for (PDType instance : supertypes)
			addSupertype(instance);
	}

	/**
	 * Removes the link from this instance through role "supertype".
	 */
	public void removeSupertype(){
		store.removeLink(this.id, roleSupertypeId,
			store.getInstance(this.id, roleSupertypeId));
	}

	/**
	 * Removes the link from this instance through role "supertype" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSupertype(PDType supertype){
		if (supertype == null)
			return;
		store.removeLink(this.id, roleSupertypeId, supertype.getId());
	}

	/**
	 * Removes the links from this instance through role "supertype" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSupertypes(Collection<PDType> supertypes){
		if (supertypes == null)
			return;
		for (PDType instance : supertypes)
			store.removeLink(this.id, roleSupertypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If there is already an instance connected to this instance through role "supertype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param supertype the instance to connect
	 */
	public void setSupertype(GUID supertype) {
		store.setLink(this.id,  roleSupertypeId, supertype);
	}
	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If there is already an instance connected to this instance through role "supertype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param supertype the instance to connect
	 */
	public void setSupertype(PDType supertype) 
{		setSupertype(supertype.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role is primitive.
	 * @return the connected instance
	 */
	public Boolean getIsPrimitive(){
	 	return (Boolean)store.getInstance(this.id, roleIsPrimitiveId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role is primitive.
	 * @return the connected instance(s)
	 */
	 public Collection<Boolean> getIsPrimitives(){
		return (Collection<Boolean>)(Object)store.getInstances(this.id, roleIsPrimitiveId);
	 }

   /**
	 * Connects this instance to the given instance using role "is primitive".
	 * If the given instance is null, nothing happens.
	 * @param isPrimitive the instance to connect
	 */
	public void addIsPrimitive(Boolean isPrimitive){
		if (isPrimitive != null) {
			store.addLink(this.id, roleIsPrimitiveId, isPrimitive);
		}
	}
	/**
	 * Connects this instance to the given instances using role "is primitive".
	 * If the given collection of instances is null, nothing happens.
	 * @param isPrimitive the Collection of instances to connect
	 */
	public void addIsPrimitives(Collection<Boolean> isPrimitives){
		if (isPrimitives == null)
			return;
		for (Boolean instance : isPrimitives)
			addIsPrimitive(instance);
	}

	/**
	 * Removes the link from this instance through role "is primitive".
	 */
	public void removeIsPrimitive(){
		store.removeLink(this.id, roleIsPrimitiveId,
			store.getInstance(this.id, roleIsPrimitiveId));
	}

	/**
	 * Removes the link from this instance through role "is primitive" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeIsPrimitive(Boolean isPrimitive){
		if (isPrimitive == null)
			return;
		store.removeLink(this.id, roleIsPrimitiveId, isPrimitive);
	}

	/**
	 * Connects this instance to the given instance using role "is primitive".
	 * If there is already an instance connected to this instance through role "is primitive", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isPrimitive the instance to connect
	 */
	public void setIsPrimitive(Boolean isPrimitive) {
		store.setLink(this.id,  roleIsPrimitiveId, isPrimitive);
	}
	public Collection<PDRole> getAccessibleRoles() throws PDStoreException {
		Set<PDRole> result = new HashSet<PDRole>();
		for (PDRole role1 : getOwnedRoles())
			result.add(role1.getPartner());
		return result;
	}
}
