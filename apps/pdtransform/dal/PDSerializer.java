package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Serializer" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDSerializer");
 * @author PDGen
 */
public class PDSerializer implements PDInstance,PDSerializerI{

	public static final GUID typeId = new GUID("5f1e85348e0ede11980f9a097666e103"); 

	public static final GUID roleInstructionId = new GUID("611e85348e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDSerializer.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDSerializer:" + name;
		else
			return "PDSerializer:" + id;
	}

	/**
	 * Creates an PDSerializer object representing a PDStore instance of type Serializer.
	 * @param store the store the instance should be in
	 */
	public PDSerializer(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDSerializer object representing the instance with the given ID. 
	 * The ID must be of an instance of type Serializer.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDSerializer(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Serializer with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDSerializer load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDSerializer)instance;
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
	 * Returns the instance connected to this instance through the role instruction.
	 * @return the connected instance
	 */
	public PDPrintInstruction getInstruction(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleInstructionId);
	 	return (PDPrintInstruction) store.load(PDPrintInstruction.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role instruction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDPrintInstruction> getInstructions(){
		return (Collection<PDPrintInstruction>)(Object)store.getAndLoadInstances(this.id, roleInstructionId, PDPrintInstruction.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "instruction".
	 * If the given instance is null, nothing happens.
	 * @param instruction the instance to connect
	 */
	public void addInstruction(GUID instruction){
		if (instruction != null) {
			store.addLink(this.id, roleInstructionId, instruction);
		}
	}
	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If the given instance is null, nothing happens.
	 * @param instruction the instance to connect
	 */
	public void addInstruction(PDPrintInstruction instruction){
		if (instruction != null)
			addInstruction(instruction.getId());
	}

	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If the given collection of instances is null, nothing happens.
	 * @param instruction the Collection of instances to connect
	 */
	public void addInstructions(Collection<PDPrintInstruction> instructions){
		if (instructions == null)
			return;
		for (PDPrintInstruction instance : instructions)
			addInstruction(instance);
	}

	/**
	 * Removes the link from this instance through role "instruction".
	 */
	public void removeInstruction(){
		store.removeLink(this.id, roleInstructionId,
			store.getInstance(this.id, roleInstructionId));
	}

	/**
	 * Removes the link from this instance through role "instruction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInstruction(PDPrintInstruction instruction){
		if (instruction == null)
			return;
		store.removeLink(this.id, roleInstructionId, instruction.getId());
	}

	/**
	 * Removes the links from this instance through role "instruction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInstructions(Collection<PDPrintInstruction> instructions){
		if (instructions == null)
			return;
		for (PDPrintInstruction instance : instructions)
			store.removeLink(this.id, roleInstructionId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If there is already an instance connected to this instance through role "instruction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instruction the instance to connect
	 */
	public void setInstruction(GUID instruction) {
		store.setLink(this.id,  roleInstructionId, instruction);
	}
	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If there is already an instance connected to this instance through role "instruction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instruction the instance to connect
	 */
	public void setInstruction(PDPrintInstruction instruction) 
{		setInstruction(instruction.getId());
	}
}
