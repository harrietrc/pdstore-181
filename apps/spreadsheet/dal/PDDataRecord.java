package spreadsheet.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "DataRecord" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("assignment.dal.PDDataRecord");
 * @author PDGen
 */
public class PDDataRecord implements PDInstance,PDDataRecordI{

	public static final GUID typeId = new GUID("2e2b6ab1a25611e19ed778e4009ed4de"); 

	public static final GUID roleLeaderId = new GUID("b6cd0a42a25b11e196b978e4009ed4de");
	public static final GUID roleParticipantNoId = new GUID("b6cd0a41a25b11e196b978e4009ed4de");
	public static final GUID roleTaskNoId = new GUID("b6cd0a40a25b11e196b978e4009ed4de");
	public static final GUID roleAgeId = new GUID("b6cac050a25b11e196b978e4009ed4de");
	public static final GUID roleIsInId = new GUID("2e2b6ab7a25611e19ec778e4009ed4de");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDDataRecord.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDDataRecord:" + name;
		else
			return "PDDataRecord:" + id;
	}

	/**
	 * Creates an PDDataRecord object representing a PDStore instance of type DataRecord.
	 * @param store the store the instance should be in
	 */
	public PDDataRecord(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDDataRecord object representing the instance with the given ID. 
	 * The ID must be of an instance of type DataRecord.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDDataRecord(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type DataRecord with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDDataRecord load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDDataRecord)instance;
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
	 * Returns the instance connected to this instance through the role leader.
	 * @return the connected instance
	 */
	public String getLeader() {
	 	return (String)store.getInstance(this.id, roleLeaderId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role leader.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getLeaders() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleLeaderId);
	 }

   /**
	 * Connects this instance to the given instance using role "leader".
	 * If the given instance is null, nothing happens.
	 * @param leader the instance to connect
	 */
	public void addLeader(String leader) {
		if (leader != null) {
			store.addLink(this.id, roleLeaderId, leader);
		}
	}
	/**
	 * Connects this instance to the given instances using role "leader".
	 * If the given collection of instances is null, nothing happens.
	 * @param leader the Collection of instances to connect
	 */
	public void addLeaders(Collection<String> leaders) {
		if (leaders == null)
			return;
		for (String instance : leaders)
			addLeader(instance);
	}

	/**
	 * Removes the link from this instance through role "leader".
	 */
	public void removeLeader() {
		store.removeLink(this.id, roleLeaderId,
			store.getInstance(this.id, roleLeaderId));
	}

	/**
	 * Removes the link from this instance through role "leader" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeLeader(String leader) {
		if (leader == null)
			return;
		store.removeLink(this.id, roleLeaderId, leader);
	}

	/**
	 * Connects this instance to the given instance using role "leader".
	 * If there is already an instance connected to this instance through role "leader", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param leader the instance to connect
	 */
	public void setLeader(String leader) {
		store.setLink(this.id,  roleLeaderId, leader);
	}
	/**
	 * Returns the instance connected to this instance through the role participantNo.
	 * @return the connected instance
	 */
	public Long getParticipantNo() {
	 	return (Long)store.getInstance(this.id, roleParticipantNoId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role participantNo.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getParticipantNos() {
		return (Collection<Long>)(Object)store.getInstances(this.id, roleParticipantNoId);
	 }

   /**
	 * Connects this instance to the given instance using role "participantNo".
	 * If the given instance is null, nothing happens.
	 * @param participantNo the instance to connect
	 */
	public void addParticipantNo(Long participantNo) {
		if (participantNo != null) {
			store.addLink(this.id, roleParticipantNoId, participantNo);
		}
	}
	/**
	 * Connects this instance to the given instances using role "participantNo".
	 * If the given collection of instances is null, nothing happens.
	 * @param participantNo the Collection of instances to connect
	 */
	public void addParticipantNos(Collection<Long> participantNos) {
		if (participantNos == null)
			return;
		for (Long instance : participantNos)
			addParticipantNo(instance);
	}

	/**
	 * Removes the link from this instance through role "participantNo".
	 */
	public void removeParticipantNo() {
		store.removeLink(this.id, roleParticipantNoId,
			store.getInstance(this.id, roleParticipantNoId));
	}

	/**
	 * Removes the link from this instance through role "participantNo" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeParticipantNo(Long participantNo) {
		if (participantNo == null)
			return;
		store.removeLink(this.id, roleParticipantNoId, participantNo);
	}

	/**
	 * Connects this instance to the given instance using role "participantNo".
	 * If there is already an instance connected to this instance through role "participantNo", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param participantNo the instance to connect
	 */
	public void setParticipantNo(Long participantNo) {
		store.setLink(this.id,  roleParticipantNoId, participantNo);
	}
	/**
	 * Returns the instance connected to this instance through the role taskNo.
	 * @return the connected instance
	 */
	public Long getTaskNo() {
	 	return (Long)store.getInstance(this.id, roleTaskNoId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role taskNo.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getTaskNos() {
		return (Collection<Long>)(Object)store.getInstances(this.id, roleTaskNoId);
	 }

   /**
	 * Connects this instance to the given instance using role "taskNo".
	 * If the given instance is null, nothing happens.
	 * @param taskNo the instance to connect
	 */
	public void addTaskNo(Long taskNo) {
		if (taskNo != null) {
			store.addLink(this.id, roleTaskNoId, taskNo);
		}
	}
	/**
	 * Connects this instance to the given instances using role "taskNo".
	 * If the given collection of instances is null, nothing happens.
	 * @param taskNo the Collection of instances to connect
	 */
	public void addTaskNos(Collection<Long> taskNos) {
		if (taskNos == null)
			return;
		for (Long instance : taskNos)
			addTaskNo(instance);
	}

	/**
	 * Removes the link from this instance through role "taskNo".
	 */
	public void removeTaskNo() {
		store.removeLink(this.id, roleTaskNoId,
			store.getInstance(this.id, roleTaskNoId));
	}

	/**
	 * Removes the link from this instance through role "taskNo" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTaskNo(Long taskNo) {
		if (taskNo == null)
			return;
		store.removeLink(this.id, roleTaskNoId, taskNo);
	}

	/**
	 * Connects this instance to the given instance using role "taskNo".
	 * If there is already an instance connected to this instance through role "taskNo", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param taskNo the instance to connect
	 */
	public void setTaskNo(Long taskNo) {
		store.setLink(this.id,  roleTaskNoId, taskNo);
	}
	/**
	 * Returns the instance connected to this instance through the role age.
	 * @return the connected instance
	 */
	public Long getAge() {
	 	return (Long)store.getInstance(this.id, roleAgeId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role age.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getAges() {
		return (Collection<Long>)(Object)store.getInstances(this.id, roleAgeId);
	 }

   /**
	 * Connects this instance to the given instance using role "age".
	 * If the given instance is null, nothing happens.
	 * @param age the instance to connect
	 */
	public void addAge(Long age) {
		if (age != null) {
			store.addLink(this.id, roleAgeId, age);
		}
	}
	/**
	 * Connects this instance to the given instances using role "age".
	 * If the given collection of instances is null, nothing happens.
	 * @param age the Collection of instances to connect
	 */
	public void addAges(Collection<Long> ages) {
		if (ages == null)
			return;
		for (Long instance : ages)
			addAge(instance);
	}

	/**
	 * Removes the link from this instance through role "age".
	 */
	public void removeAge() {
		store.removeLink(this.id, roleAgeId,
			store.getInstance(this.id, roleAgeId));
	}

	/**
	 * Removes the link from this instance through role "age" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeAge(Long age) {
		if (age == null)
			return;
		store.removeLink(this.id, roleAgeId, age);
	}

	/**
	 * Connects this instance to the given instance using role "age".
	 * If there is already an instance connected to this instance through role "age", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param age the instance to connect
	 */
	public void setAge(Long age) {
		store.setLink(this.id,  roleAgeId, age);
	}
	/**
	 * Returns the instance connected to this instance through the role is in.
	 * @return the connected instance
	 */
	public PDDataSet getIsIn() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleIsInId);
	 	return (PDDataSet) store.load(PDDataSet.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role is in.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDataSet> getIsIns() {
		return (Collection<PDDataSet>)(Object)store.getAndLoadInstances(this.id, roleIsInId, PDDataSet.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "is in".
	 * If the given instance is null, nothing happens.
	 * @param isIn the instance to connect
	 */
	public void addIsIn(GUID isIn) {
		if (isIn != null) {
			store.addLink(this.id, roleIsInId, isIn);
		}
	}
	/**
	 * Connects this instance to the given instance using role "is in".
	 * If the given instance is null, nothing happens.
	 * @param isIn the instance to connect
	 */
	public void addIsIn(PDDataSet isIn) {
		if (isIn != null)
			addIsIn(isIn.getId());
	}

	/**
	 * Connects this instance to the given instance using role "is in".
	 * If the given collection of instances is null, nothing happens.
	 * @param isIn the Collection of instances to connect
	 */
	public void addIsIns(Collection<PDDataSet> isIns) {
		if (isIns == null)
			return;
		for (PDDataSet instance : isIns)
			addIsIn(instance);
	}

	/**
	 * Removes the link from this instance through role "is in".
	 */
	public void removeIsIn() {
		store.removeLink(this.id, roleIsInId,
			store.getInstance(this.id, roleIsInId));
	}

	/**
	 * Removes the link from this instance through role "is in" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeIsIn(PDDataSet isIn) {
		if (isIn == null)
			return;
		store.removeLink(this.id, roleIsInId, isIn.getId());
	}

	/**
	 * Removes the links from this instance through role "is in" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeIsIns(Collection<PDDataSet> isIns) {
		if (isIns == null)
			return;
		for (PDDataSet instance : isIns)
			store.removeLink(this.id, roleIsInId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "is in".
	 * If there is already an instance connected to this instance through role "is in", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isIn the instance to connect
	 */
	public void setIsIn(GUID isIn) {
		store.setLink(this.id,  roleIsInId, isIn);
	}
	/**
	 * Connects this instance to the given instance using role "is in".
	 * If there is already an instance connected to this instance through role "is in", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isIn the instance to connect
	 */
	public void setIsIn(PDDataSet isIn) {
		setIsIn(isIn.getId());
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
