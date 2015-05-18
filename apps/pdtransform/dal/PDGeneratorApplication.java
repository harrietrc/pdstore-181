package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Generator Application" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDGeneratorApplication");
 * @author PDGen
 */
public class PDGeneratorApplication implements PDInstance,PDGeneratorApplicationI{

	public static final GUID typeId = new GUID("e45c1f278e0ede11980f9a097666e103"); 

	public static final GUID roleGeneratorId = new GUID("f05c1f278e0ede11980f9a097666e103");
	public static final GUID roleOutputId = new GUID("ed5c1f278e0ede11980f9a097666e103");
	public static final GUID roleInputId = new GUID("e65c1f278e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDGeneratorApplication.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDGeneratorApplication:" + name;
		else
			return "PDGeneratorApplication:" + id;
	}

	/**
	 * Creates an PDGeneratorApplication object representing a PDStore instance of type Generator Application.
	 * @param store the store the instance should be in
	 */
	public PDGeneratorApplication(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDGeneratorApplication object representing the instance with the given ID. 
	 * The ID must be of an instance of type Generator Application.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDGeneratorApplication(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Generator Application with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDGeneratorApplication load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDGeneratorApplication)instance;
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
	 * Returns the instance connected to this instance through the role generator.
	 * @return the connected instance
	 */
	public PDGenerator getGenerator(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleGeneratorId);
	 	return (PDGenerator) store.load(PDGenerator.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role generator.
	 * @return the connected instance(s)
	 */
	 public Collection<PDGenerator> getGenerators(){
		return (Collection<PDGenerator>)(Object)store.getAndLoadInstances(this.id, roleGeneratorId, PDGenerator.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "generator".
	 * If the given instance is null, nothing happens.
	 * @param generator the instance to connect
	 */
	public void addGenerator(GUID generator){
		if (generator != null) {
			store.addLink(this.id, roleGeneratorId, generator);
		}
	}
	/**
	 * Connects this instance to the given instance using role "generator".
	 * If the given instance is null, nothing happens.
	 * @param generator the instance to connect
	 */
	public void addGenerator(PDGenerator generator){
		if (generator != null)
			addGenerator(generator.getId());
	}

	/**
	 * Connects this instance to the given instance using role "generator".
	 * If the given collection of instances is null, nothing happens.
	 * @param generator the Collection of instances to connect
	 */
	public void addGenerators(Collection<PDGenerator> generators){
		if (generators == null)
			return;
		for (PDGenerator instance : generators)
			addGenerator(instance);
	}

	/**
	 * Removes the link from this instance through role "generator".
	 */
	public void removeGenerator(){
		store.removeLink(this.id, roleGeneratorId,
			store.getInstance(this.id, roleGeneratorId));
	}

	/**
	 * Removes the link from this instance through role "generator" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeGenerator(PDGenerator generator){
		if (generator == null)
			return;
		store.removeLink(this.id, roleGeneratorId, generator.getId());
	}

	/**
	 * Removes the links from this instance through role "generator" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeGenerators(Collection<PDGenerator> generators){
		if (generators == null)
			return;
		for (PDGenerator instance : generators)
			store.removeLink(this.id, roleGeneratorId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "generator".
	 * If there is already an instance connected to this instance through role "generator", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generator the instance to connect
	 */
	public void setGenerator(GUID generator) {
		store.setLink(this.id,  roleGeneratorId, generator);
	}
	/**
	 * Connects this instance to the given instance using role "generator".
	 * If there is already an instance connected to this instance through role "generator", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generator the instance to connect
	 */
	public void setGenerator(PDGenerator generator) 
{		setGenerator(generator.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role output.
	 * @return the connected instance
	 */
	public GUID getOutput(){
	 	return (GUID)store.getInstance(this.id, roleOutputId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role output.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getOutputs(){
		return (Collection<GUID>)(Object)store.getInstances(this.id, roleOutputId);
	 }

   /**
	 * Connects this instance to the given instance using role "output".
	 * If the given instance is null, nothing happens.
	 * @param output the instance to connect
	 */
	public void addOutput(GUID output){
		if (output != null) {
			store.addLink(this.id, roleOutputId, output);
		}
	}
	/**
	 * Connects this instance to the given instances using role "output".
	 * If the given collection of instances is null, nothing happens.
	 * @param output the Collection of instances to connect
	 */
	public void addOutputs(Collection<GUID> outputs){
		if (outputs == null)
			return;
		for (GUID instance : outputs)
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
	public void removeOutput(GUID output){
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
	public void setOutput(GUID output) {
		store.setLink(this.id,  roleOutputId, output);
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
}
