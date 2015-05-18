package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Generator" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDGenerator");
 * @author PDGen
 */
public class PDGenerator implements PDInstance,PDGeneratorI{

	public static final GUID typeId = new GUID("fa5c1f278e0ede11980f9a097666e103"); 

	public static final GUID roleGeneratorApplicationsId = new GUID("f05c1f278e0ede11981f9a097666e103");
	public static final GUID roleMapId = new GUID("015d1f278e0ede11980f9a097666e103");
	public static final GUID roleOrderId = new GUID("b5ba85348e0ede11980f9a097666e103");
	public static final GUID roleOutputTypeId = new GUID("0b5d1f278e0ede11980f9a097666e103");
	public static final GUID roleOutputTemplateId = new GUID("ff5c1f278e0ede11980f9a097666e103");
	public static final GUID roleInputTypeId = new GUID("fd5c1f278e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDGenerator.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDGenerator:" + name;
		else
			return "PDGenerator:" + id;
	}

	/**
	 * Creates an PDGenerator object representing a PDStore instance of type Generator.
	 * @param store the store the instance should be in
	 */
	public PDGenerator(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDGenerator object representing the instance with the given ID. 
	 * The ID must be of an instance of type Generator.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDGenerator(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Generator with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDGenerator load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDGenerator)instance;
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
	 * Returns the instance connected to this instance through the role generator applications.
	 * @return the connected instance
	 */
	public PDGeneratorApplication getGeneratorApplications(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleGeneratorApplicationsId);
	 	return (PDGeneratorApplication) store.load(PDGeneratorApplication.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role generator applications.
	 * @return the connected instance(s)
	 */
	 public Collection<PDGeneratorApplication> getGeneratorApplicationss(){
		return (Collection<PDGeneratorApplication>)(Object)store.getAndLoadInstances(this.id, roleGeneratorApplicationsId, PDGeneratorApplication.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "generator applications".
	 * If the given instance is null, nothing happens.
	 * @param generatorApplications the instance to connect
	 */
	public void addGeneratorApplications(GUID generatorApplications){
		if (generatorApplications != null) {
			store.addLink(this.id, roleGeneratorApplicationsId, generatorApplications);
		}
	}
	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If the given instance is null, nothing happens.
	 * @param generatorApplications the instance to connect
	 */
	public void addGeneratorApplications(PDGeneratorApplication generatorApplications){
		if (generatorApplications != null)
			addGeneratorApplications(generatorApplications.getId());
	}

	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If the given collection of instances is null, nothing happens.
	 * @param generatorApplications the Collection of instances to connect
	 */
	public void addGeneratorApplicationss(Collection<PDGeneratorApplication> generatorApplicationss){
		if (generatorApplicationss == null)
			return;
		for (PDGeneratorApplication instance : generatorApplicationss)
			addGeneratorApplications(instance);
	}

	/**
	 * Removes the link from this instance through role "generator applications".
	 */
	public void removeGeneratorApplications(){
		store.removeLink(this.id, roleGeneratorApplicationsId,
			store.getInstance(this.id, roleGeneratorApplicationsId));
	}

	/**
	 * Removes the link from this instance through role "generator applications" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeGeneratorApplications(PDGeneratorApplication generatorApplications){
		if (generatorApplications == null)
			return;
		store.removeLink(this.id, roleGeneratorApplicationsId, generatorApplications.getId());
	}

	/**
	 * Removes the links from this instance through role "generator applications" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeGeneratorApplicationss(Collection<PDGeneratorApplication> generatorApplicationss){
		if (generatorApplicationss == null)
			return;
		for (PDGeneratorApplication instance : generatorApplicationss)
			store.removeLink(this.id, roleGeneratorApplicationsId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If there is already an instance connected to this instance through role "generator applications", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generatorApplications the instance to connect
	 */
	public void setGeneratorApplications(GUID generatorApplications) {
		store.setLink(this.id,  roleGeneratorApplicationsId, generatorApplications);
	}
	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If there is already an instance connected to this instance through role "generator applications", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generatorApplications the instance to connect
	 */
	public void setGeneratorApplications(PDGeneratorApplication generatorApplications) 
{		setGeneratorApplications(generatorApplications.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role map.
	 * @return the connected instance
	 */
	public PDMap getMap(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleMapId);
	 	return (PDMap) store.load(PDMap.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role map.
	 * @return the connected instance(s)
	 */
	 public Collection<PDMap> getMaps(){
		return (Collection<PDMap>)(Object)store.getAndLoadInstances(this.id, roleMapId, PDMap.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "map".
	 * If the given instance is null, nothing happens.
	 * @param map the instance to connect
	 */
	public void addMap(GUID map){
		if (map != null) {
			store.addLink(this.id, roleMapId, map);
		}
	}
	/**
	 * Connects this instance to the given instance using role "map".
	 * If the given instance is null, nothing happens.
	 * @param map the instance to connect
	 */
	public void addMap(PDMap map){
		if (map != null)
			addMap(map.getId());
	}

	/**
	 * Connects this instance to the given instance using role "map".
	 * If the given collection of instances is null, nothing happens.
	 * @param map the Collection of instances to connect
	 */
	public void addMaps(Collection<PDMap> maps){
		if (maps == null)
			return;
		for (PDMap instance : maps)
			addMap(instance);
	}

	/**
	 * Removes the link from this instance through role "map".
	 */
	public void removeMap(){
		store.removeLink(this.id, roleMapId,
			store.getInstance(this.id, roleMapId));
	}

	/**
	 * Removes the link from this instance through role "map" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeMap(PDMap map){
		if (map == null)
			return;
		store.removeLink(this.id, roleMapId, map.getId());
	}

	/**
	 * Removes the links from this instance through role "map" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeMaps(Collection<PDMap> maps){
		if (maps == null)
			return;
		for (PDMap instance : maps)
			store.removeLink(this.id, roleMapId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "map".
	 * If there is already an instance connected to this instance through role "map", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param map the instance to connect
	 */
	public void setMap(GUID map) {
		store.setLink(this.id,  roleMapId, map);
	}
	/**
	 * Connects this instance to the given instance using role "map".
	 * If there is already an instance connected to this instance through role "map", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param map the instance to connect
	 */
	public void setMap(PDMap map) 
{		setMap(map.getId());
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
	/**
	 * Returns the instance connected to this instance through the role output type.
	 * @return the connected instance
	 */
	public PDType getOutputType(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOutputTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role output type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getOutputTypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleOutputTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "output type".
	 * If the given instance is null, nothing happens.
	 * @param outputType the instance to connect
	 */
	public void addOutputType(GUID outputType){
		if (outputType != null) {
			store.addLink(this.id, roleOutputTypeId, outputType);
		}
	}
	/**
	 * Connects this instance to the given instance using role "output type".
	 * If the given instance is null, nothing happens.
	 * @param outputType the instance to connect
	 */
	public void addOutputType(PDType outputType){
		if (outputType != null)
			addOutputType(outputType.getId());
	}

	/**
	 * Connects this instance to the given instance using role "output type".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputType the Collection of instances to connect
	 */
	public void addOutputTypes(Collection<PDType> outputTypes){
		if (outputTypes == null)
			return;
		for (PDType instance : outputTypes)
			addOutputType(instance);
	}

	/**
	 * Removes the link from this instance through role "output type".
	 */
	public void removeOutputType(){
		store.removeLink(this.id, roleOutputTypeId,
			store.getInstance(this.id, roleOutputTypeId));
	}

	/**
	 * Removes the link from this instance through role "output type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputType(PDType outputType){
		if (outputType == null)
			return;
		store.removeLink(this.id, roleOutputTypeId, outputType.getId());
	}

	/**
	 * Removes the links from this instance through role "output type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOutputTypes(Collection<PDType> outputTypes){
		if (outputTypes == null)
			return;
		for (PDType instance : outputTypes)
			store.removeLink(this.id, roleOutputTypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "output type".
	 * If there is already an instance connected to this instance through role "output type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputType the instance to connect
	 */
	public void setOutputType(GUID outputType) {
		store.setLink(this.id,  roleOutputTypeId, outputType);
	}
	/**
	 * Connects this instance to the given instance using role "output type".
	 * If there is already an instance connected to this instance through role "output type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputType the instance to connect
	 */
	public void setOutputType(PDType outputType) 
{		setOutputType(outputType.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role output template.
	 * @return the connected instance
	 */
	public GUID getOutputTemplate(){
	 	return (GUID)store.getInstance(this.id, roleOutputTemplateId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role output template.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getOutputTemplates(){
		return (Collection<GUID>)(Object)store.getInstances(this.id, roleOutputTemplateId);
	 }

   /**
	 * Connects this instance to the given instance using role "output template".
	 * If the given instance is null, nothing happens.
	 * @param outputTemplate the instance to connect
	 */
	public void addOutputTemplate(GUID outputTemplate){
		if (outputTemplate != null) {
			store.addLink(this.id, roleOutputTemplateId, outputTemplate);
		}
	}
	/**
	 * Connects this instance to the given instances using role "output template".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputTemplate the Collection of instances to connect
	 */
	public void addOutputTemplates(Collection<GUID> outputTemplates){
		if (outputTemplates == null)
			return;
		for (GUID instance : outputTemplates)
			addOutputTemplate(instance);
	}

	/**
	 * Removes the link from this instance through role "output template".
	 */
	public void removeOutputTemplate(){
		store.removeLink(this.id, roleOutputTemplateId,
			store.getInstance(this.id, roleOutputTemplateId));
	}

	/**
	 * Removes the link from this instance through role "output template" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputTemplate(GUID outputTemplate){
		if (outputTemplate == null)
			return;
		store.removeLink(this.id, roleOutputTemplateId, outputTemplate);
	}

	/**
	 * Connects this instance to the given instance using role "output template".
	 * If there is already an instance connected to this instance through role "output template", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputTemplate the instance to connect
	 */
	public void setOutputTemplate(GUID outputTemplate) {
		store.setLink(this.id,  roleOutputTemplateId, outputTemplate);
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
