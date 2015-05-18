package diagrameditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Operation" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("diagrameditor.dal.PDOperation");
 * @author PDGen
 */
public class PDOperation implements PDInstance,PDOperationI{

	public static final GUID typeId = new GUID("920645120d6411e0b45a1cc1dec00ed3"); 

	public static final GUID roleShapeTypeId = new GUID("7420c754980111e1b62d1c6f65cd0117");
	public static final GUID roleImplementationId = new GUID("7420c753980111e1b62d1c6f65cd0117");
	public static final GUID roleTypeId = new GUID("ee32adf0f68b11df860e1cc1dec00ed3");
	public static final GUID roleOperationApplicationId = new GUID("7420c752980111e1b63d1c6f65cd0117");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDOperation.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDOperation:" + name;
		else
			return "PDOperation:" + id;
	}

	/**
	 * Creates an PDOperation object representing a PDStore instance of type Operation.
	 * @param store the store the instance should be in
	 */
	public PDOperation(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDOperation object representing the instance with the given ID. 
	 * The ID must be of an instance of type Operation.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDOperation(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Operation with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDOperation load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDOperation)instance;
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
	 * Returns the instance connected to this instance through the role shapeType.
	 * @return the connected instance
	 */
	public PDType getShapeType(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleShapeTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role shapeType.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getShapeTypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleShapeTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(GUID shapeType){
		if (shapeType != null) {
			store.addLink(this.id, roleShapeTypeId, shapeType);
		}
	}
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(PDType shapeType){
		if (shapeType != null)
			addShapeType(shapeType.getId());
	}

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given collection of instances is null, nothing happens.
	 * @param shapeType the Collection of instances to connect
	 */
	public void addShapeTypes(Collection<PDType> shapeTypes){
		if (shapeTypes == null)
			return;
		for (PDType instance : shapeTypes)
			addShapeType(instance);
	}

	/**
	 * Removes the link from this instance through role "shapeType".
	 */
	public void removeShapeType(){
		store.removeLink(this.id, roleShapeTypeId,
			store.getInstance(this.id, roleShapeTypeId));
	}

	/**
	 * Removes the link from this instance through role "shapeType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShapeType(PDType shapeType){
		if (shapeType == null)
			return;
		store.removeLink(this.id, roleShapeTypeId, shapeType.getId());
	}

	/**
	 * Removes the links from this instance through role "shapeType" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeShapeTypes(Collection<PDType> shapeTypes){
		if (shapeTypes == null)
			return;
		for (PDType instance : shapeTypes)
			store.removeLink(this.id, roleShapeTypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(GUID shapeType) {
		store.setLink(this.id,  roleShapeTypeId, shapeType);
	}
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(PDType shapeType) 
{		setShapeType(shapeType.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role implementation.
	 * @return the connected instance
	 */
	public String getImplementation(){
	 	return (String)store.getInstance(this.id, roleImplementationId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role implementation.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getImplementations(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleImplementationId);
	 }

   /**
	 * Connects this instance to the given instance using role "implementation".
	 * If the given instance is null, nothing happens.
	 * @param implementation the instance to connect
	 */
	public void addImplementation(String implementation){
		if (implementation != null) {
			store.addLink(this.id, roleImplementationId, implementation);
		}
	}
	/**
	 * Connects this instance to the given instances using role "implementation".
	 * If the given collection of instances is null, nothing happens.
	 * @param implementation the Collection of instances to connect
	 */
	public void addImplementations(Collection<String> implementations){
		if (implementations == null)
			return;
		for (String instance : implementations)
			addImplementation(instance);
	}

	/**
	 * Removes the link from this instance through role "implementation".
	 */
	public void removeImplementation(){
		store.removeLink(this.id, roleImplementationId,
			store.getInstance(this.id, roleImplementationId));
	}

	/**
	 * Removes the link from this instance through role "implementation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeImplementation(String implementation){
		if (implementation == null)
			return;
		store.removeLink(this.id, roleImplementationId, implementation);
	}

	/**
	 * Connects this instance to the given instance using role "implementation".
	 * If there is already an instance connected to this instance through role "implementation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param implementation the instance to connect
	 */
	public void setImplementation(String implementation) {
		store.setLink(this.id,  roleImplementationId, implementation);
	}
	/**
	 * Returns the instance connected to this instance through the role type.
	 * @return the connected instance
	 */
	public PDType getType(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getTypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(GUID type){
		if (type != null) {
			store.addLink(this.id, roleTypeId, type);
		}
	}
	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(PDType type){
		if (type != null)
			addType(type.getId());
	}

	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given collection of instances is null, nothing happens.
	 * @param type the Collection of instances to connect
	 */
	public void addTypes(Collection<PDType> types){
		if (types == null)
			return;
		for (PDType instance : types)
			addType(instance);
	}

	/**
	 * Removes the link from this instance through role "type".
	 */
	public void removeType(){
		store.removeLink(this.id, roleTypeId,
			store.getInstance(this.id, roleTypeId));
	}

	/**
	 * Removes the link from this instance through role "type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeType(PDType type){
		if (type == null)
			return;
		store.removeLink(this.id, roleTypeId, type.getId());
	}

	/**
	 * Removes the links from this instance through role "type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTypes(Collection<PDType> types){
		if (types == null)
			return;
		for (PDType instance : types)
			store.removeLink(this.id, roleTypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "type".
	 * If there is already an instance connected to this instance through role "type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param type the instance to connect
	 */
	public void setType(GUID type) {
		store.setLink(this.id,  roleTypeId, type);
	}
	/**
	 * Connects this instance to the given instance using role "type".
	 * If there is already an instance connected to this instance through role "type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param type the instance to connect
	 */
	public void setType(PDType type) 
{		setType(type.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role Operation Application.
	 * @return the connected instance
	 */
	public PDOperationApplication getOperationApplication(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOperationApplicationId);
	 	return (PDOperationApplication) store.load(PDOperationApplication.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Operation Application.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperationApplication> getOperationApplications(){
		return (Collection<PDOperationApplication>)(Object)store.getAndLoadInstances(this.id, roleOperationApplicationId, PDOperationApplication.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given instance is null, nothing happens.
	 * @param operationApplication the instance to connect
	 */
	public void addOperationApplication(GUID operationApplication){
		if (operationApplication != null) {
			store.addLink(this.id, roleOperationApplicationId, operationApplication);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given instance is null, nothing happens.
	 * @param operationApplication the instance to connect
	 */
	public void addOperationApplication(PDOperationApplication operationApplication){
		if (operationApplication != null)
			addOperationApplication(operationApplication.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given collection of instances is null, nothing happens.
	 * @param operationApplication the Collection of instances to connect
	 */
	public void addOperationApplications(Collection<PDOperationApplication> operationApplications){
		if (operationApplications == null)
			return;
		for (PDOperationApplication instance : operationApplications)
			addOperationApplication(instance);
	}

	/**
	 * Removes the link from this instance through role "Operation Application".
	 */
	public void removeOperationApplication(){
		store.removeLink(this.id, roleOperationApplicationId,
			store.getInstance(this.id, roleOperationApplicationId));
	}

	/**
	 * Removes the link from this instance through role "Operation Application" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperationApplication(PDOperationApplication operationApplication){
		if (operationApplication == null)
			return;
		store.removeLink(this.id, roleOperationApplicationId, operationApplication.getId());
	}

	/**
	 * Removes the links from this instance through role "Operation Application" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOperationApplications(Collection<PDOperationApplication> operationApplications){
		if (operationApplications == null)
			return;
		for (PDOperationApplication instance : operationApplications)
			store.removeLink(this.id, roleOperationApplicationId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If there is already an instance connected to this instance through role "Operation Application", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationApplication the instance to connect
	 */
	public void setOperationApplication(GUID operationApplication) {
		store.setLink(this.id,  roleOperationApplicationId, operationApplication);
	}
	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If there is already an instance connected to this instance through role "Operation Application", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationApplication the instance to connect
	 */
	public void setOperationApplication(PDOperationApplication operationApplication) 
{		setOperationApplication(operationApplication.getId());
	}
}
