package texteditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "History" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("texteditor.dal.PDHistory");
 * @author PDGen
 */
public class PDHistory implements PDInstance {

	public static final GUID typeId = new GUID("3c08266054d111e09c2a001e6805726d"); 

	public static final GUID roleOperationId = new GUID("3c08266754d111e09c2a001e6805726d");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDHistory.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDHistory:" + name;
		else
			return "PDHistory:" + id;
	}

	/**
	 * Creates an PDHistory object representing a PDStore instance of type History.
	 * @param store the store the instance should be in
	 */
	public PDHistory(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDHistory object representing the instance with the given ID. 
	 * The ID must be of an instance of type History.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDHistory(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type History with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDHistory load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDHistory)instance;
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
	 * Returns the instance connected to this instance through the role Operation.
	 * @return the connected instance
	 */
	public PDOperation getOperation() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleOperationId);
	 	return (PDOperation) store.load(PDOperation.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Operation.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperation> getOperations() {
		return (Collection<PDOperation>)(Object)store.getAndLoadInstances(this.id, roleOperationId, PDOperation.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 */
	public void addOperation(GUID operation) {
		if (operation != null) {
			store.addLink(this.id, roleOperationId, operation);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 */
	public void addOperation(PDOperation operation) {
		if (operation != null)
			addOperation(operation.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given collection of instances is null, nothing happens.
	 * @param operation the Collection of instances to connect
	 */
	public void addOperations(Collection<PDOperation> operations) {
		if (operations == null)
			return;
		for (PDOperation instance : operations)
			addOperation(instance);
	}

	/**
	 * Removes the link from this instance through role "Operation".
	 */
	public void removeOperation() {
		store.removeLink(this.id, roleOperationId,
			store.getInstance(this.id, roleOperationId));
	}

	/**
	 * Removes the link from this instance through role "Operation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperation(Object operation) {
		if (operation == null)
			return;
		store.removeLink(this.id, roleOperationId, operation);
	}

	/**
	 * Removes the links from this instance through role "Operation" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOperations(Collection<PDOperation> operations) {
		if (operations == null)
			return;
		for (PDOperation instance : operations)
			store.removeLink(this.id, roleOperationId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operation the instance to connect
	 */
	public void setOperation(GUID operation) {
		store.setLink(this.id,  roleOperationId, operation);
	}
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operation the instance to connect
	 */
	public void setOperation(PDOperation operation) {
		setOperation(operation.getId());
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
