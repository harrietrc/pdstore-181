package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Print Instruction" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDPrintInstruction");
 * @author PDGen
 */
public class PDPrintInstruction implements PDInstance,PDPrintInstructionI{

	public static final GUID typeId = new GUID("621e85348e0ede11980f9a097666e103"); 

	public static final GUID rolePrintAfterId = new GUID("681e85348e0ede11980f9a097666e103");
	public static final GUID rolePrintBeforeId = new GUID("661e85348e0ede11980f9a097666e103");
	public static final GUID roleTypeId = new GUID("641e85348e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDPrintInstruction.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDPrintInstruction:" + name;
		else
			return "PDPrintInstruction:" + id;
	}

	/**
	 * Creates an PDPrintInstruction object representing a PDStore instance of type Print Instruction.
	 * @param store the store the instance should be in
	 */
	public PDPrintInstruction(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDPrintInstruction object representing the instance with the given ID. 
	 * The ID must be of an instance of type Print Instruction.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDPrintInstruction(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Print Instruction with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDPrintInstruction load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDPrintInstruction)instance;
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
	 * Returns the instance connected to this instance through the role print after.
	 * @return the connected instance
	 */
	public String getPrintAfter(){
	 	return (String)store.getInstance(this.id, rolePrintAfterId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role print after.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getPrintAfters(){
		return (Collection<String>)(Object)store.getInstances(this.id, rolePrintAfterId);
	 }

   /**
	 * Connects this instance to the given instance using role "print after".
	 * If the given instance is null, nothing happens.
	 * @param printAfter the instance to connect
	 */
	public void addPrintAfter(String printAfter){
		if (printAfter != null) {
			store.addLink(this.id, rolePrintAfterId, printAfter);
		}
	}
	/**
	 * Connects this instance to the given instances using role "print after".
	 * If the given collection of instances is null, nothing happens.
	 * @param printAfter the Collection of instances to connect
	 */
	public void addPrintAfters(Collection<String> printAfters){
		if (printAfters == null)
			return;
		for (String instance : printAfters)
			addPrintAfter(instance);
	}

	/**
	 * Removes the link from this instance through role "print after".
	 */
	public void removePrintAfter(){
		store.removeLink(this.id, rolePrintAfterId,
			store.getInstance(this.id, rolePrintAfterId));
	}

	/**
	 * Removes the link from this instance through role "print after" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrintAfter(String printAfter){
		if (printAfter == null)
			return;
		store.removeLink(this.id, rolePrintAfterId, printAfter);
	}

	/**
	 * Connects this instance to the given instance using role "print after".
	 * If there is already an instance connected to this instance through role "print after", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param printAfter the instance to connect
	 */
	public void setPrintAfter(String printAfter) {
		store.setLink(this.id,  rolePrintAfterId, printAfter);
	}
	/**
	 * Returns the instance connected to this instance through the role print before.
	 * @return the connected instance
	 */
	public String getPrintBefore(){
	 	return (String)store.getInstance(this.id, rolePrintBeforeId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role print before.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getPrintBefores(){
		return (Collection<String>)(Object)store.getInstances(this.id, rolePrintBeforeId);
	 }

   /**
	 * Connects this instance to the given instance using role "print before".
	 * If the given instance is null, nothing happens.
	 * @param printBefore the instance to connect
	 */
	public void addPrintBefore(String printBefore){
		if (printBefore != null) {
			store.addLink(this.id, rolePrintBeforeId, printBefore);
		}
	}
	/**
	 * Connects this instance to the given instances using role "print before".
	 * If the given collection of instances is null, nothing happens.
	 * @param printBefore the Collection of instances to connect
	 */
	public void addPrintBefores(Collection<String> printBefores){
		if (printBefores == null)
			return;
		for (String instance : printBefores)
			addPrintBefore(instance);
	}

	/**
	 * Removes the link from this instance through role "print before".
	 */
	public void removePrintBefore(){
		store.removeLink(this.id, rolePrintBeforeId,
			store.getInstance(this.id, rolePrintBeforeId));
	}

	/**
	 * Removes the link from this instance through role "print before" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrintBefore(String printBefore){
		if (printBefore == null)
			return;
		store.removeLink(this.id, rolePrintBeforeId, printBefore);
	}

	/**
	 * Connects this instance to the given instance using role "print before".
	 * If there is already an instance connected to this instance through role "print before", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param printBefore the instance to connect
	 */
	public void setPrintBefore(String printBefore) {
		store.setLink(this.id,  rolePrintBeforeId, printBefore);
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
}
