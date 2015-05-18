package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Serializer Application" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDSerializerApplication");
 * @author PDGen
 */
public class PDSerializerApplication implements PDInstance,PDSerializerApplicationI{

	public static final GUID typeId = new GUID("a3ba85348e0ede11980f9a097666e103"); 

	public static final GUID roleSerializerId = new GUID("abba85348e0ede11980f9a097666e103");
	public static final GUID roleOutputId = new GUID("a9ba85348e0ede11980f9a097666e103");
	public static final GUID roleInputTypeId = new GUID("a7ba85348e0ede11980f9a097666e103");
	public static final GUID roleInputId = new GUID("a5ba85348e0ede11980f9a097666e103");
	public static final GUID roleOrderId = new GUID("bdba85348e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDSerializerApplication.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDSerializerApplication:" + name;
		else
			return "PDSerializerApplication:" + id;
	}

	/**
	 * Creates an PDSerializerApplication object representing a PDStore instance of type Serializer Application.
	 * @param store the store the instance should be in
	 */
	public PDSerializerApplication(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDSerializerApplication object representing the instance with the given ID. 
	 * The ID must be of an instance of type Serializer Application.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDSerializerApplication(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Serializer Application with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDSerializerApplication load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDSerializerApplication)instance;
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
	 * Returns the instance connected to this instance through the role serializer.
	 * @return the connected instance
	 */
	public PDSerializer getSerializer(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleSerializerId);
	 	return (PDSerializer) store.load(PDSerializer.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role serializer.
	 * @return the connected instance(s)
	 */
	 public Collection<PDSerializer> getSerializers(){
		return (Collection<PDSerializer>)(Object)store.getAndLoadInstances(this.id, roleSerializerId, PDSerializer.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given instance is null, nothing happens.
	 * @param serializer the instance to connect
	 */
	public void addSerializer(GUID serializer){
		if (serializer != null) {
			store.addLink(this.id, roleSerializerId, serializer);
		}
	}
	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given instance is null, nothing happens.
	 * @param serializer the instance to connect
	 */
	public void addSerializer(PDSerializer serializer){
		if (serializer != null)
			addSerializer(serializer.getId());
	}

	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given collection of instances is null, nothing happens.
	 * @param serializer the Collection of instances to connect
	 */
	public void addSerializers(Collection<PDSerializer> serializers){
		if (serializers == null)
			return;
		for (PDSerializer instance : serializers)
			addSerializer(instance);
	}

	/**
	 * Removes the link from this instance through role "serializer".
	 */
	public void removeSerializer(){
		store.removeLink(this.id, roleSerializerId,
			store.getInstance(this.id, roleSerializerId));
	}

	/**
	 * Removes the link from this instance through role "serializer" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSerializer(PDSerializer serializer){
		if (serializer == null)
			return;
		store.removeLink(this.id, roleSerializerId, serializer.getId());
	}

	/**
	 * Removes the links from this instance through role "serializer" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSerializers(Collection<PDSerializer> serializers){
		if (serializers == null)
			return;
		for (PDSerializer instance : serializers)
			store.removeLink(this.id, roleSerializerId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If there is already an instance connected to this instance through role "serializer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param serializer the instance to connect
	 */
	public void setSerializer(GUID serializer) {
		store.setLink(this.id,  roleSerializerId, serializer);
	}
	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If there is already an instance connected to this instance through role "serializer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param serializer the instance to connect
	 */
	public void setSerializer(PDSerializer serializer) 
{		setSerializer(serializer.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role output.
	 * @return the connected instance
	 */
	public String getOutput(){
	 	return (String)store.getInstance(this.id, roleOutputId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role output.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getOutputs(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleOutputId);
	 }

   /**
	 * Connects this instance to the given instance using role "output".
	 * If the given instance is null, nothing happens.
	 * @param output the instance to connect
	 */
	public void addOutput(String output){
		if (output != null) {
			store.addLink(this.id, roleOutputId, output);
		}
	}
	/**
	 * Connects this instance to the given instances using role "output".
	 * If the given collection of instances is null, nothing happens.
	 * @param output the Collection of instances to connect
	 */
	public void addOutputs(Collection<String> outputs){
		if (outputs == null)
			return;
		for (String instance : outputs)
			addOutput(instance);
	}

	/**
	 * Removes the link from this instance through role "output".
	 */
	public void removeOutput(){
		store.removeLink(this.id, roleOutputId,
			store.getInstance(this.id, roleOutputId));
	}

	/**
	 * Removes the link from this instance through role "output" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutput(String output){
		if (output == null)
			return;
		store.removeLink(this.id, roleOutputId, output);
	}

	/**
	 * Connects this instance to the given instance using role "output".
	 * If there is already an instance connected to this instance through role "output", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param output the instance to connect
	 */
	public void setOutput(String output) {
		store.setLink(this.id,  roleOutputId, output);
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
	/**
	 * Returns the instance connected to this instance through the role input.
	 * @return the connected instance
	 */
	public GUID getInput(){
	 	return (GUID)store.getInstance(this.id, roleInputId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role input.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getInputs(){
		return (Collection<GUID>)(Object)store.getInstances(this.id, roleInputId);
	 }

   /**
	 * Connects this instance to the given instance using role "input".
	 * If the given instance is null, nothing happens.
	 * @param input the instance to connect
	 */
	public void addInput(GUID input){
		if (input != null) {
			store.addLink(this.id, roleInputId, input);
		}
	}
	/**
	 * Connects this instance to the given instances using role "input".
	 * If the given collection of instances is null, nothing happens.
	 * @param input the Collection of instances to connect
	 */
	public void addInputs(Collection<GUID> inputs){
		if (inputs == null)
			return;
		for (GUID instance : inputs)
			addInput(instance);
	}

	/**
	 * Removes the link from this instance through role "input".
	 */
	public void removeInput(){
		store.removeLink(this.id, roleInputId,
			store.getInstance(this.id, roleInputId));
	}

	/**
	 * Removes the link from this instance through role "input" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInput(GUID input){
		if (input == null)
			return;
		store.removeLink(this.id, roleInputId, input);
	}

	/**
	 * Connects this instance to the given instance using role "input".
	 * If there is already an instance connected to this instance through role "input", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param input the instance to connect
	 */
	public void setInput(GUID input) {
		store.setLink(this.id,  roleInputId, input);
	}
	/**
	 * Returns the instance connected to this instance through the role order.
	 * @return the connected instance
	 */
	public PDOrder getOrder(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOrderId);
	 	return (PDOrder) store.load(PDOrder.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role order.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOrder> getOrders(){
		return (Collection<PDOrder>)(Object)store.getAndLoadInstances(this.id, roleOrderId, PDOrder.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "order".
	 * If the given instance is null, nothing happens.
	 * @param order the instance to connect
	 */
	public void addOrder(GUID order){
		if (order != null) {
			store.addLink(this.id, roleOrderId, order);
		}
	}
	/**
	 * Connects this instance to the given instance using role "order".
	 * If the given instance is null, nothing happens.
	 * @param order the instance to connect
	 */
	public void addOrder(PDOrder order){
		if (order != null)
			addOrder(order.getId());
	}

	/**
	 * Connects this instance to the given instance using role "order".
	 * If the given collection of instances is null, nothing happens.
	 * @param order the Collection of instances to connect
	 */
	public void addOrders(Collection<PDOrder> orders){
		if (orders == null)
			return;
		for (PDOrder instance : orders)
			addOrder(instance);
	}

	/**
	 * Removes the link from this instance through role "order".
	 */
	public void removeOrder(){
		store.removeLink(this.id, roleOrderId,
			store.getInstance(this.id, roleOrderId));
	}

	/**
	 * Removes the link from this instance through role "order" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOrder(PDOrder order){
		if (order == null)
			return;
		store.removeLink(this.id, roleOrderId, order.getId());
	}

	/**
	 * Removes the links from this instance through role "order" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOrders(Collection<PDOrder> orders){
		if (orders == null)
			return;
		for (PDOrder instance : orders)
			store.removeLink(this.id, roleOrderId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "order".
	 * If there is already an instance connected to this instance through role "order", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param order the instance to connect
	 */
	public void setOrder(GUID order) {
		store.setLink(this.id,  roleOrderId, order);
	}
	/**
	 * Connects this instance to the given instance using role "order".
	 * If there is already an instance connected to this instance through role "order", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param order the instance to connect
	 */
	public void setOrder(PDOrder order) 
{		setOrder(order.getId());
	}
}
