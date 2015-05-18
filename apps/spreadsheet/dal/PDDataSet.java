package spreadsheet.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "DataSet" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("assignment.dal.PDDataSet");
 * @author PDGen
 */
public class PDDataSet implements PDInstance,PDDataSetI{

	public static final GUID typeId = new GUID("2e2b6ab0a25611e19ed778e4009ed4de"); 

	public static final GUID roleRecordsId = new GUID("2e2b6ab7a25611e19ed778e4009ed4de");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDDataSet.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDDataSet:" + name;
		else
			return "PDDataSet:" + id;
	}

	/**
	 * Creates an PDDataSet object representing a PDStore instance of type DataSet.
	 * @param store the store the instance should be in
	 */
	public PDDataSet(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDDataSet object representing the instance with the given ID. 
	 * The ID must be of an instance of type DataSet.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDDataSet(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type DataSet with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDDataSet load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDDataSet)instance;
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
	 * Returns the instance connected to this instance through the role Records.
	 * @return the connected instance
	 */
	public PDDataRecord getRecords() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleRecordsId);
	 	return (PDDataRecord) store.load(PDDataRecord.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Records.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDataRecord> getRecordss() {
		return (Collection<PDDataRecord>)(Object)store.getAndLoadInstances(this.id, roleRecordsId, PDDataRecord.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Records".
	 * If the given instance is null, nothing happens.
	 * @param records the instance to connect
	 */
	public void addRecords(GUID records) {
		if (records != null) {
			store.addLink(this.id, roleRecordsId, records);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Records".
	 * If the given instance is null, nothing happens.
	 * @param records the instance to connect
	 */
	public void addRecords(PDDataRecord records) {
		if (records != null)
			addRecords(records.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Records".
	 * If the given collection of instances is null, nothing happens.
	 * @param records the Collection of instances to connect
	 */
	public void addRecordss(Collection<PDDataRecord> recordss) {
		if (recordss == null)
			return;
		for (PDDataRecord instance : recordss)
			addRecords(instance);
	}

	/**
	 * Removes the link from this instance through role "Records".
	 */
	public void removeRecords() {
		store.removeLink(this.id, roleRecordsId,
			store.getInstance(this.id, roleRecordsId));
	}

	/**
	 * Removes the link from this instance through role "Records" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeRecords(PDDataRecord records) {
		if (records == null)
			return;
		store.removeLink(this.id, roleRecordsId, records.getId());
	}

	/**
	 * Removes the links from this instance through role "Records" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeRecordss(Collection<PDDataRecord> recordss) {
		if (recordss == null)
			return;
		for (PDDataRecord instance : recordss)
			store.removeLink(this.id, roleRecordsId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Records".
	 * If there is already an instance connected to this instance through role "Records", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param records the instance to connect
	 */
	public void setRecords(GUID records) {
		store.setLink(this.id,  roleRecordsId, records);
	}
	/**
	 * Connects this instance to the given instance using role "Records".
	 * If there is already an instance connected to this instance through role "Records", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param records the instance to connect
	 */
	public void setRecords(PDDataRecord records) {
		setRecords(records.getId());
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
