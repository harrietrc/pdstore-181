package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Map" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDMap");
 * @author PDGen
 */
public class PDMap implements PDInstance,PDMapI{

	public static final GUID typeId = new GUID("fb5c1f278e0ede11980f9a097666e103"); 

	public static final GUID roleOutputRoleId = new GUID("075d1f278e0ede11980f9a097666e103");
	public static final GUID roleOutputInstanceId = new GUID("055d1f278e0ede11980f9a097666e103");
	public static final GUID roleInputRoleId = new GUID("035d1f278e0ede11980f9a097666e103");
	public static final GUID roleInputTypeId = new GUID("095d1f278e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDMap.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDMap:" + name;
		else
			return "PDMap:" + id;
	}

	/**
	 * Creates an PDMap object representing a PDStore instance of type Map.
	 * @param store the store the instance should be in
	 */
	public PDMap(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDMap object representing the instance with the given ID. 
	 * The ID must be of an instance of type Map.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDMap(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Map with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDMap load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDMap)instance;
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
	 * Returns the instance connected to this instance through the role output role.
	 * @return the connected instance
	 */
	public PDRole getOutputRole(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOutputRoleId);
	 	return (PDRole) store.load(PDRole.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role output role.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getOutputRoles(){
		return (Collection<PDRole>)(Object)store.getAndLoadInstances(this.id, roleOutputRoleId, PDRole.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "output role".
	 * If the given instance is null, nothing happens.
	 * @param outputRole the instance to connect
	 */
	public void addOutputRole(GUID outputRole){
		if (outputRole != null) {
			store.addLink(this.id, roleOutputRoleId, outputRole);
		}
	}
	/**
	 * Connects this instance to the given instance using role "output role".
	 * If the given instance is null, nothing happens.
	 * @param outputRole the instance to connect
	 */
	public void addOutputRole(PDRole outputRole){
		if (outputRole != null)
			addOutputRole(outputRole.getId());
	}

	/**
	 * Connects this instance to the given instance using role "output role".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputRole the Collection of instances to connect
	 */
	public void addOutputRoles(Collection<PDRole> outputRoles){
		if (outputRoles == null)
			return;
		for (PDRole instance : outputRoles)
			addOutputRole(instance);
	}

	/**
	 * Removes the link from this instance through role "output role".
	 */
	public void removeOutputRole(){
		store.removeLink(this.id, roleOutputRoleId,
			store.getInstance(this.id, roleOutputRoleId));
	}

	/**
	 * Removes the link from this instance through role "output role" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputRole(PDRole outputRole){
		if (outputRole == null)
			return;
		store.removeLink(this.id, roleOutputRoleId, outputRole.getId());
	}

	/**
	 * Removes the links from this instance through role "output role" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOutputRoles(Collection<PDRole> outputRoles){
		if (outputRoles == null)
			return;
		for (PDRole instance : outputRoles)
			store.removeLink(this.id, roleOutputRoleId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "output role".
	 * If there is already an instance connected to this instance through role "output role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputRole the instance to connect
	 */
	public void setOutputRole(GUID outputRole) {
		store.setLink(this.id,  roleOutputRoleId, outputRole);
	}
	/**
	 * Connects this instance to the given instance using role "output role".
	 * If there is already an instance connected to this instance through role "output role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputRole the instance to connect
	 */
	public void setOutputRole(PDRole outputRole) 
{		setOutputRole(outputRole.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role output instance.
	 * @return the connected instance
	 */
	public GUID getOutputInstance(){
	 	return (GUID)store.getInstance(this.id, roleOutputInstanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role output instance.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getOutputInstances(){
		return (Collection<GUID>)(Object)store.getInstances(this.id, roleOutputInstanceId);
	 }

   /**
	 * Connects this instance to the given instance using role "output instance".
	 * If the given instance is null, nothing happens.
	 * @param outputInstance the instance to connect
	 */
	public void addOutputInstance(GUID outputInstance){
		if (outputInstance != null) {
			store.addLink(this.id, roleOutputInstanceId, outputInstance);
		}
	}
	/**
	 * Connects this instance to the given instances using role "output instance".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputInstance the Collection of instances to connect
	 */
	public void addOutputInstances(Collection<GUID> outputInstances){
		if (outputInstances == null)
			return;
		for (GUID instance : outputInstances)
			addOutputInstance(instance);
	}

	/**
	 * Removes the link from this instance through role "output instance".
	 */
	public void removeOutputInstance(){
		store.removeLink(this.id, roleOutputInstanceId,
			store.getInstance(this.id, roleOutputInstanceId));
	}

	/**
	 * Removes the link from this instance through role "output instance" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputInstance(GUID outputInstance){
		if (outputInstance == null)
			return;
		store.removeLink(this.id, roleOutputInstanceId, outputInstance);
	}

	/**
	 * Connects this instance to the given instance using role "output instance".
	 * If there is already an instance connected to this instance through role "output instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputInstance the instance to connect
	 */
	public void setOutputInstance(GUID outputInstance) {
		store.setLink(this.id,  roleOutputInstanceId, outputInstance);
	}
	/**
	 * Returns the instance connected to this instance through the role input role.
	 * @return the connected instance
	 */
	public PDRole getInputRole(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleInputRoleId);
	 	return (PDRole) store.load(PDRole.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role input role.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getInputRoles(){
		return (Collection<PDRole>)(Object)store.getAndLoadInstances(this.id, roleInputRoleId, PDRole.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "input role".
	 * If the given instance is null, nothing happens.
	 * @param inputRole the instance to connect
	 */
	public void addInputRole(GUID inputRole){
		if (inputRole != null) {
			store.addLink(this.id, roleInputRoleId, inputRole);
		}
	}
	/**
	 * Connects this instance to the given instance using role "input role".
	 * If the given instance is null, nothing happens.
	 * @param inputRole the instance to connect
	 */
	public void addInputRole(PDRole inputRole){
		if (inputRole != null)
			addInputRole(inputRole.getId());
	}

	/**
	 * Connects this instance to the given instance using role "input role".
	 * If the given collection of instances is null, nothing happens.
	 * @param inputRole the Collection of instances to connect
	 */
	public void addInputRoles(Collection<PDRole> inputRoles){
		if (inputRoles == null)
			return;
		for (PDRole instance : inputRoles)
			addInputRole(instance);
	}

	/**
	 * Removes the link from this instance through role "input role".
	 */
	public void removeInputRole(){
		store.removeLink(this.id, roleInputRoleId,
			store.getInstance(this.id, roleInputRoleId));
	}

	/**
	 * Removes the link from this instance through role "input role" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInputRole(PDRole inputRole){
		if (inputRole == null)
			return;
		store.removeLink(this.id, roleInputRoleId, inputRole.getId());
	}

	/**
	 * Removes the links from this instance through role "input role" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInputRoles(Collection<PDRole> inputRoles){
		if (inputRoles == null)
			return;
		for (PDRole instance : inputRoles)
			store.removeLink(this.id, roleInputRoleId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "input role".
	 * If there is already an instance connected to this instance through role "input role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputRole the instance to connect
	 */
	public void setInputRole(GUID inputRole) {
		store.setLink(this.id,  roleInputRoleId, inputRole);
	}
	/**
	 * Connects this instance to the given instance using role "input role".
	 * If there is already an instance connected to this instance through role "input role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputRole the instance to connect
	 */
	public void setInputRole(PDRole inputRole) 
{		setInputRole(inputRole.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role input type.
	 * @return the connected instance
	 */
	public PDType getInputType(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleInputTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role input type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getInputTypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleInputTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "input type".
	 * If the given instance is null, nothing happens.
	 * @param inputType the instance to connect
	 */
	public void addInputType(GUID inputType){
		if (inputType != null) {
			store.addLink(this.id, roleInputTypeId, inputType);
		}
	}
	/**
	 * Connects this instance to the given instance using role "input type".
	 * If the given instance is null, nothing happens.
	 * @param inputType the instance to connect
	 */
	public void addInputType(PDType inputType){
		if (inputType != null)
			addInputType(inputType.getId());
	}

	/**
	 * Connects this instance to the given instance using role "input type".
	 * If the given collection of instances is null, nothing happens.
	 * @param inputType the Collection of instances to connect
	 */
	public void addInputTypes(Collection<PDType> inputTypes){
		if (inputTypes == null)
			return;
		for (PDType instance : inputTypes)
			addInputType(instance);
	}

	/**
	 * Removes the link from this instance through role "input type".
	 */
	public void removeInputType(){
		store.removeLink(this.id, roleInputTypeId,
			store.getInstance(this.id, roleInputTypeId));
	}

	/**
	 * Removes the link from this instance through role "input type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInputType(PDType inputType){
		if (inputType == null)
			return;
		store.removeLink(this.id, roleInputTypeId, inputType.getId());
	}

	/**
	 * Removes the links from this instance through role "input type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInputTypes(Collection<PDType> inputTypes){
		if (inputTypes == null)
			return;
		for (PDType instance : inputTypes)
			store.removeLink(this.id, roleInputTypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "input type".
	 * If there is already an instance connected to this instance through role "input type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputType the instance to connect
	 */
	public void setInputType(GUID inputType) {
		store.setLink(this.id,  roleInputTypeId, inputType);
	}
	/**
	 * Connects this instance to the given instance using role "input type".
	 * If there is already an instance connected to this instance through role "input type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputType the instance to connect
	 */
	public void setInputType(PDType inputType) 
{		setInputType(inputType.getId());
	}
}
