package texteditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Operation" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("texteditor.dal.PDOperation");
 * @author PDGen
 */
public class PDOperation implements PDInstance {

	public static final GUID typeId = new GUID("3c08266154d111e09c2a001e6805726d"); 

	public static final GUID roleHistoryId = new GUID("3c08266754d111e09c3a001e6805726d");
	public static final GUID roleTimeStampId = new GUID("3c08266854d111e09c2a001e6805726d");
	public static final GUID roleSuperParameterId = new GUID("3c08266a54d111e09c2a001e6805726d");
	public static final GUID roleCommandId = new GUID("3c08266954d111e09c2a001e6805726d");
	public static final GUID roleUserId = new GUID("913dc29d5a6711e0b82c001e6805726d");

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
	 * Returns the instance connected to this instance through the role History.
	 * @return the connected instance
	 */
	public PDHistory getHistory() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleHistoryId);
	 	return (PDHistory) store.load(PDHistory.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role History.
	 * @return the connected instance(s)
	 */
	 public Collection<PDHistory> getHistorys() {
		return (Collection<PDHistory>)(Object)store.getAndLoadInstances(this.id, roleHistoryId, PDHistory.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "History".
	 * If the given instance is null, nothing happens.
	 * @param history the instance to connect
	 */
	public void addHistory(GUID history) {
		if (history != null) {
			store.addLink(this.id, roleHistoryId, history);
		}
	}
	/**
	 * Connects this instance to the given instance using role "History".
	 * If the given instance is null, nothing happens.
	 * @param history the instance to connect
	 */
	public void addHistory(PDHistory history) {
		if (history != null)
			addHistory(history.getId());
	}

	/**
	 * Connects this instance to the given instance using role "History".
	 * If the given collection of instances is null, nothing happens.
	 * @param history the Collection of instances to connect
	 */
	public void addHistorys(Collection<PDHistory> historys) {
		if (historys == null)
			return;
		for (PDHistory instance : historys)
			addHistory(instance);
	}

	/**
	 * Removes the link from this instance through role "History".
	 */
	public void removeHistory() {
		store.removeLink(this.id, roleHistoryId,
			store.getInstance(this.id, roleHistoryId));
	}

	/**
	 * Removes the link from this instance through role "History" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHistory(Object history) {
		if (history == null)
			return;
		store.removeLink(this.id, roleHistoryId, history);
	}

	/**
	 * Removes the links from this instance through role "History" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeHistorys(Collection<PDHistory> historys) {
		if (historys == null)
			return;
		for (PDHistory instance : historys)
			store.removeLink(this.id, roleHistoryId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "History".
	 * If there is already an instance connected to this instance through role "History", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param history the instance to connect
	 */
	public void setHistory(GUID history) {
		store.setLink(this.id,  roleHistoryId, history);
	}
	/**
	 * Connects this instance to the given instance using role "History".
	 * If there is already an instance connected to this instance through role "History", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param history the instance to connect
	 */
	public void setHistory(PDHistory history) {
		setHistory(history.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role timeStamp.
	 * @return the connected instance
	 */
	public Long getTimeStamp() {
	 	return (Long)store.getInstance(this.id, roleTimeStampId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role timeStamp.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getTimeStamps() {
		return (Collection<Long>)(Object)store.getInstances(this.id, roleTimeStampId);
	 }

   /**
	 * Connects this instance to the given instance using role "timeStamp".
	 * If the given instance is null, nothing happens.
	 * @param timeStamp the instance to connect
	 */
	public void addTimeStamp(Long timeStamp) {
		if (timeStamp != null) {
			store.addLink(this.id, roleTimeStampId, timeStamp);
		}
	}
	/**
	 * Connects this instance to the given instances using role "timeStamp".
	 * If the given collection of instances is null, nothing happens.
	 * @param timeStamp the Collection of instances to connect
	 */
	public void addTimeStamps(Collection<Long> timeStamps) {
		if (timeStamps == null)
			return;
		for (Long instance : timeStamps)
			addTimeStamp(instance);
	}

	/**
	 * Removes the link from this instance through role "timeStamp".
	 */
	public void removeTimeStamp() {
		store.removeLink(this.id, roleTimeStampId,
			store.getInstance(this.id, roleTimeStampId));
	}

	/**
	 * Removes the link from this instance through role "timeStamp" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTimeStamp(Object timeStamp) {
		if (timeStamp == null)
			return;
		store.removeLink(this.id, roleTimeStampId, timeStamp);
	}

	/**
	 * Connects this instance to the given instance using role "timeStamp".
	 * If there is already an instance connected to this instance through role "timeStamp", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param timeStamp the instance to connect
	 */
	public void setTimeStamp(Long timeStamp) {
		store.setLink(this.id,  roleTimeStampId, timeStamp);
	}
	/**
	 * Returns the instance connected to this instance through the role superParameter.
	 * @return the connected instance
	 */
	public PDInstance getSuperParameter() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleSuperParameterId);
	 	return (PDInstance) store.load(instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role superParameter.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getSuperParameters() {
		return (Collection<PDInstance>)(Object)store.getAndLoadInstances(this.id, roleSuperParameterId);
	 }

   /**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given instance is null, nothing happens.
	 * @param superParameter the instance to connect
	 */
	public void addSuperParameter(GUID superParameter) {
		if (superParameter != null) {
			store.addLink(this.id, roleSuperParameterId, superParameter);
		}
	}
	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given instance is null, nothing happens.
	 * @param superParameter the instance to connect
	 */
	public void addSuperParameter(PDInstance superParameter) {
		if (superParameter != null)
			addSuperParameter(superParameter.getId());
	}

	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given collection of instances is null, nothing happens.
	 * @param superParameter the Collection of instances to connect
	 */
	public void addSuperParameters(Collection<PDInstance> superParameters) {
		if (superParameters == null)
			return;
		for (PDInstance instance : superParameters)
			addSuperParameter(instance);
	}

	/**
	 * Removes the link from this instance through role "superParameter".
	 */
	public void removeSuperParameter() {
		store.removeLink(this.id, roleSuperParameterId,
			store.getInstance(this.id, roleSuperParameterId));
	}

	/**
	 * Removes the link from this instance through role "superParameter" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSuperParameter(Object superParameter) {
		if (superParameter == null)
			return;
		store.removeLink(this.id, roleSuperParameterId, superParameter);
	}

	/**
	 * Removes the links from this instance through role "superParameter" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSuperParameters(Collection<PDInstance> superParameters) {
		if (superParameters == null)
			return;
		for (PDInstance instance : superParameters)
			store.removeLink(this.id, roleSuperParameterId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If there is already an instance connected to this instance through role "superParameter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param superParameter the instance to connect
	 */
	public void setSuperParameter(GUID superParameter) {
		store.setLink(this.id,  roleSuperParameterId, superParameter);
	}
	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If there is already an instance connected to this instance through role "superParameter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param superParameter the instance to connect
	 */
	public void setSuperParameter(PDInstance superParameter) {
		setSuperParameter(superParameter.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role command.
	 * @return the connected instance
	 */
	public String getCommand() {
	 	return (String)store.getInstance(this.id, roleCommandId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role command.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getCommands() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleCommandId);
	 }

   /**
	 * Connects this instance to the given instance using role "command".
	 * If the given instance is null, nothing happens.
	 * @param command the instance to connect
	 */
	public void addCommand(String command) {
		if (command != null) {
			store.addLink(this.id, roleCommandId, command);
		}
	}
	/**
	 * Connects this instance to the given instances using role "command".
	 * If the given collection of instances is null, nothing happens.
	 * @param command the Collection of instances to connect
	 */
	public void addCommands(Collection<String> commands) {
		if (commands == null)
			return;
		for (String instance : commands)
			addCommand(instance);
	}

	/**
	 * Removes the link from this instance through role "command".
	 */
	public void removeCommand() {
		store.removeLink(this.id, roleCommandId,
			store.getInstance(this.id, roleCommandId));
	}

	/**
	 * Removes the link from this instance through role "command" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeCommand(Object command) {
		if (command == null)
			return;
		store.removeLink(this.id, roleCommandId, command);
	}

	/**
	 * Connects this instance to the given instance using role "command".
	 * If there is already an instance connected to this instance through role "command", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param command the instance to connect
	 */
	public void setCommand(String command) {
		store.setLink(this.id,  roleCommandId, command);
	}
	/**
	 * Returns the instance connected to this instance through the role user.
	 * @return the connected instance
	 */
	public String getUser() {
	 	return (String)store.getInstance(this.id, roleUserId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role user.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getUsers() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleUserId);
	 }

   /**
	 * Connects this instance to the given instance using role "user".
	 * If the given instance is null, nothing happens.
	 * @param user the instance to connect
	 */
	public void addUser(String user) {
		if (user != null) {
			store.addLink(this.id, roleUserId, user);
		}
	}
	/**
	 * Connects this instance to the given instances using role "user".
	 * If the given collection of instances is null, nothing happens.
	 * @param user the Collection of instances to connect
	 */
	public void addUsers(Collection<String> users) {
		if (users == null)
			return;
		for (String instance : users)
			addUser(instance);
	}

	/**
	 * Removes the link from this instance through role "user".
	 */
	public void removeUser() {
		store.removeLink(this.id, roleUserId,
			store.getInstance(this.id, roleUserId));
	}

	/**
	 * Removes the link from this instance through role "user" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeUser(Object user) {
		if (user == null)
			return;
		store.removeLink(this.id, roleUserId, user);
	}

	/**
	 * Connects this instance to the given instance using role "user".
	 * If there is already an instance connected to this instance through role "user", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param user the instance to connect
	 */
	public void setUser(String user) {
		store.setLink(this.id,  roleUserId, user);
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
