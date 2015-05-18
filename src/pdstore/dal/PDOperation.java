package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Operation" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDOperation");
 * @author PDGen
 */
public class PDOperation implements PDInstance,PDOperationI{

	public static final GUID typeId = new GUID("dbe74a2f89cb11e197441cc1dec00ed3"); 

	public static final GUID roleWidgetImplementationId = new GUID("0054221a8cf711e1926b842b2b9af4fd");
	public static final GUID roleOperationImplementationId = new GUID("dbe74a3189cb11e197441cc1dec00ed3");
	public static final GUID roleParameterTypeId = new GUID("dbe74a3089cb11e197441cc1dec00ed3");

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
	 * Returns the instance connected to this instance through the role widget implementation.
	 * @return the connected instance
	 */
	public String getWidgetImplementation(){
	 	return (String)store.getInstance(this.id, roleWidgetImplementationId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role widget implementation.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getWidgetImplementations(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleWidgetImplementationId);
	 }

   /**
	 * Connects this instance to the given instance using role "widget implementation".
	 * If the given instance is null, nothing happens.
	 * @param widgetImplementation the instance to connect
	 */
	public void addWidgetImplementation(String widgetImplementation){
		if (widgetImplementation != null) {
			store.addLink(this.id, roleWidgetImplementationId, widgetImplementation);
		}
	}
	/**
	 * Connects this instance to the given instances using role "widget implementation".
	 * If the given collection of instances is null, nothing happens.
	 * @param widgetImplementation the Collection of instances to connect
	 */
	public void addWidgetImplementations(Collection<String> widgetImplementations){
		if (widgetImplementations == null)
			return;
		for (String instance : widgetImplementations)
			addWidgetImplementation(instance);
	}

	/**
	 * Removes the link from this instance through role "widget implementation".
	 */
	public void removeWidgetImplementation(){
		store.removeLink(this.id, roleWidgetImplementationId,
			store.getInstance(this.id, roleWidgetImplementationId));
	}

	/**
	 * Removes the link from this instance through role "widget implementation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeWidgetImplementation(String widgetImplementation){
		if (widgetImplementation == null)
			return;
		store.removeLink(this.id, roleWidgetImplementationId, widgetImplementation);
	}

	/**
	 * Connects this instance to the given instance using role "widget implementation".
	 * If there is already an instance connected to this instance through role "widget implementation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param widgetImplementation the instance to connect
	 */
	public void setWidgetImplementation(String widgetImplementation) {
		store.setLink(this.id,  roleWidgetImplementationId, widgetImplementation);
	}
	/**
	 * Returns the instance connected to this instance through the role operation implementation.
	 * @return the connected instance
	 */
	public String getOperationImplementation(){
	 	return (String)store.getInstance(this.id, roleOperationImplementationId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role operation implementation.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getOperationImplementations(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleOperationImplementationId);
	 }

   /**
	 * Connects this instance to the given instance using role "operation implementation".
	 * If the given instance is null, nothing happens.
	 * @param operationImplementation the instance to connect
	 */
	public void addOperationImplementation(String operationImplementation){
		if (operationImplementation != null) {
			store.addLink(this.id, roleOperationImplementationId, operationImplementation);
		}
	}
	/**
	 * Connects this instance to the given instances using role "operation implementation".
	 * If the given collection of instances is null, nothing happens.
	 * @param operationImplementation the Collection of instances to connect
	 */
	public void addOperationImplementations(Collection<String> operationImplementations){
		if (operationImplementations == null)
			return;
		for (String instance : operationImplementations)
			addOperationImplementation(instance);
	}

	/**
	 * Removes the link from this instance through role "operation implementation".
	 */
	public void removeOperationImplementation(){
		store.removeLink(this.id, roleOperationImplementationId,
			store.getInstance(this.id, roleOperationImplementationId));
	}

	/**
	 * Removes the link from this instance through role "operation implementation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperationImplementation(String operationImplementation){
		if (operationImplementation == null)
			return;
		store.removeLink(this.id, roleOperationImplementationId, operationImplementation);
	}

	/**
	 * Connects this instance to the given instance using role "operation implementation".
	 * If there is already an instance connected to this instance through role "operation implementation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationImplementation the instance to connect
	 */
	public void setOperationImplementation(String operationImplementation) {
		store.setLink(this.id,  roleOperationImplementationId, operationImplementation);
	}
	/**
	 * Returns the instance connected to this instance through the role parameter type.
	 * @return the connected instance
	 */
	public PDType getParameterType(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleParameterTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role parameter type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getParameterTypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleParameterTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "parameter type".
	 * If the given instance is null, nothing happens.
	 * @param parameterType the instance to connect
	 */
	public void addParameterType(GUID parameterType){
		if (parameterType != null) {
			store.addLink(this.id, roleParameterTypeId, parameterType);
		}
	}
	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If the given instance is null, nothing happens.
	 * @param parameterType the instance to connect
	 */
	public void addParameterType(PDType parameterType){
		if (parameterType != null)
			addParameterType(parameterType.getId());
	}

	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If the given collection of instances is null, nothing happens.
	 * @param parameterType the Collection of instances to connect
	 */
	public void addParameterTypes(Collection<PDType> parameterTypes){
		if (parameterTypes == null)
			return;
		for (PDType instance : parameterTypes)
			addParameterType(instance);
	}

	/**
	 * Removes the link from this instance through role "parameter type".
	 */
	public void removeParameterType(){
		store.removeLink(this.id, roleParameterTypeId,
			store.getInstance(this.id, roleParameterTypeId));
	}

	/**
	 * Removes the link from this instance through role "parameter type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeParameterType(PDType parameterType){
		if (parameterType == null)
			return;
		store.removeLink(this.id, roleParameterTypeId, parameterType.getId());
	}

	/**
	 * Removes the links from this instance through role "parameter type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeParameterTypes(Collection<PDType> parameterTypes){
		if (parameterTypes == null)
			return;
		for (PDType instance : parameterTypes)
			store.removeLink(this.id, roleParameterTypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If there is already an instance connected to this instance through role "parameter type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parameterType the instance to connect
	 */
	public void setParameterType(GUID parameterType) {
		store.setLink(this.id,  roleParameterTypeId, parameterType);
	}
	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If there is already an instance connected to this instance through role "parameter type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parameterType the instance to connect
	 */
	public void setParameterType(PDType parameterType) 
{		setParameterType(parameterType.getId());
	}
}
