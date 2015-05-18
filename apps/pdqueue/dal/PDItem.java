package pdqueue.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.PDInstance;
import pdstore.dal.PDWorkingCopy;

/**
 * Data access class to represent instances of type "Item" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdqueue.dal.PDItem");
 * @author PDGen
 */
public class PDItem implements PDInstance {

	public static final GUID typeId = new GUID("b3bba7cbd80911e0b63d0024e80616c7"); 

	public static final GUID roleSenderId = new GUID("7e2e063014bf11e18cea0024e80616c7");
	public static final GUID roleTimeId = new GUID("899ecc6ed76111e09b030024e80616c7");
	public static final GUID roleReceiverId = new GUID("899ecc6fd76111e09b030024e80616c7");
	public static final GUID roleStateId = new GUID("b3bba7d2d80911e0b63d0024e80616c7");
	public static final GUID roleMessageId = new GUID("899ecc70d76111e09b030024e80616c7");
	public static final GUID roleQueueId = new GUID("899ecc6cd76111e09b130024e80616c7");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDItem.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDItem:" + name;
		else
			return "PDItem:" + id;
	}

	/**
	 * Creates an PDItem object representing a PDStore instance of type Item.
	 * @param store the store the instance should be in
	 */
	public PDItem(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDItem object representing the instance with the given ID. 
	 * The ID must be of an instance of type Item.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDItem(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Item with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDItem load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDItem)instance;
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
	 * Returns the instance connected to this instance through the role Sender.
	 * @return the connected instance
	 */
	public String getSender() {
	 	return (String)store.getInstance(this.id, roleSenderId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Sender.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getSenders() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleSenderId);
	 }

   /**
	 * Connects this instance to the given instance using role "Sender".
	 * If the given instance is null, nothing happens.
	 * @param sender the instance to connect
	 */
	public void addSender(String sender) {
		if (sender != null) {
			store.addLink(this.id, roleSenderId, sender);
		}
	}
	/**
	 * Connects this instance to the given instances using role "Sender".
	 * If the given collection of instances is null, nothing happens.
	 * @param sender the Collection of instances to connect
	 */
	public void addSenders(Collection<String> senders) {
		if (senders == null)
			return;
		for (String instance : senders)
			addSender(instance);
	}

	/**
	 * Removes the link from this instance through role "Sender".
	 */
	public void removeSender() {
		store.removeLink(this.id, roleSenderId,
			store.getInstance(this.id, roleSenderId));
	}

	/**
	 * Removes the link from this instance through role "Sender" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSender(Object sender) {
		if (sender == null)
			return;
		store.removeLink(this.id, roleSenderId, sender);
	}

	/**
	 * Connects this instance to the given instance using role "Sender".
	 * If there is already an instance connected to this instance through role "Sender", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param sender the instance to connect
	 */
	public void setSender(String sender) {
		store.setLink(this.id,  roleSenderId, sender);
	}
	/**
	 * Returns the instance connected to this instance through the role Time.
	 * @return the connected instance
	 */
	public Date getTime() {
	 	return (Date)store.getInstance(this.id, roleTimeId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Time.
	 * @return the connected instance(s)
	 */
	 public Collection<Date> getTimes() {
		return (Collection<Date>)(Object)store.getInstances(this.id, roleTimeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Time".
	 * If the given instance is null, nothing happens.
	 * @param time the instance to connect
	 */
	public void addTime(Date time) {
		if (time != null) {
			store.addLink(this.id, roleTimeId, time);
		}
	}
	/**
	 * Connects this instance to the given instances using role "Time".
	 * If the given collection of instances is null, nothing happens.
	 * @param time the Collection of instances to connect
	 */
	public void addTimes(Collection<Date> times) {
		if (times == null)
			return;
		for (Date instance : times)
			addTime(instance);
	}

	/**
	 * Removes the link from this instance through role "Time".
	 */
	public void removeTime() {
		store.removeLink(this.id, roleTimeId,
			store.getInstance(this.id, roleTimeId));
	}

	/**
	 * Removes the link from this instance through role "Time" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTime(Object time) {
		if (time == null)
			return;
		store.removeLink(this.id, roleTimeId, time);
	}

	/**
	 * Connects this instance to the given instance using role "Time".
	 * If there is already an instance connected to this instance through role "Time", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param time the instance to connect
	 */
	public void setTime(Date time) {
		store.setLink(this.id,  roleTimeId, time);
	}
	/**
	 * Returns the instance connected to this instance through the role Receiver.
	 * @return the connected instance
	 */
	public String getReceiver() {
	 	return (String)store.getInstance(this.id, roleReceiverId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Receiver.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getReceivers() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleReceiverId);
	 }

   /**
	 * Connects this instance to the given instance using role "Receiver".
	 * If the given instance is null, nothing happens.
	 * @param receiver the instance to connect
	 */
	public void addReceiver(String receiver) {
		if (receiver != null) {
			store.addLink(this.id, roleReceiverId, receiver);
		}
	}
	/**
	 * Connects this instance to the given instances using role "Receiver".
	 * If the given collection of instances is null, nothing happens.
	 * @param receiver the Collection of instances to connect
	 */
	public void addReceivers(Collection<String> receivers) {
		if (receivers == null)
			return;
		for (String instance : receivers)
			addReceiver(instance);
	}

	/**
	 * Removes the link from this instance through role "Receiver".
	 */
	public void removeReceiver() {
		store.removeLink(this.id, roleReceiverId,
			store.getInstance(this.id, roleReceiverId));
	}

	/**
	 * Removes the link from this instance through role "Receiver" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeReceiver(Object receiver) {
		if (receiver == null)
			return;
		store.removeLink(this.id, roleReceiverId, receiver);
	}

	/**
	 * Connects this instance to the given instance using role "Receiver".
	 * If there is already an instance connected to this instance through role "Receiver", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param receiver the instance to connect
	 */
	public void setReceiver(String receiver) {
		store.setLink(this.id,  roleReceiverId, receiver);
	}
	/**
	 * Returns the instance connected to this instance through the role State.
	 * @return the connected instance
	 */
	public String getState() {
	 	return (String)store.getInstance(this.id, roleStateId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role State.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getStates() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleStateId);
	 }

   /**
	 * Connects this instance to the given instance using role "State".
	 * If the given instance is null, nothing happens.
	 * @param state the instance to connect
	 */
	public void addState(String state) {
		if (state != null) {
			store.addLink(this.id, roleStateId, state);
		}
	}
	/**
	 * Connects this instance to the given instances using role "State".
	 * If the given collection of instances is null, nothing happens.
	 * @param state the Collection of instances to connect
	 */
	public void addStates(Collection<String> states) {
		if (states == null)
			return;
		for (String instance : states)
			addState(instance);
	}

	/**
	 * Removes the link from this instance through role "State".
	 */
	public void removeState() {
		store.removeLink(this.id, roleStateId,
			store.getInstance(this.id, roleStateId));
	}

	/**
	 * Removes the link from this instance through role "State" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeState(Object state) {
		if (state == null)
			return;
		store.removeLink(this.id, roleStateId, state);
	}

	/**
	 * Connects this instance to the given instance using role "State".
	 * If there is already an instance connected to this instance through role "State", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param state the instance to connect
	 */
	public void setState(String state) {
		store.setLink(this.id,  roleStateId, state);
	}
	/**
	 * Returns the instance connected to this instance through the role Message.
	 * @return the connected instance
	 */
	public String getMessage() {
	 	return (String)store.getInstance(this.id, roleMessageId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Message.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getMessages() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleMessageId);
	 }

   /**
	 * Connects this instance to the given instance using role "Message".
	 * If the given instance is null, nothing happens.
	 * @param message the instance to connect
	 */
	public void addMessage(String message) {
		if (message != null) {
			store.addLink(this.id, roleMessageId, message);
		}
	}
	/**
	 * Connects this instance to the given instances using role "Message".
	 * If the given collection of instances is null, nothing happens.
	 * @param message the Collection of instances to connect
	 */
	public void addMessages(Collection<String> messages) {
		if (messages == null)
			return;
		for (String instance : messages)
			addMessage(instance);
	}

	/**
	 * Removes the link from this instance through role "Message".
	 */
	public void removeMessage() {
		store.removeLink(this.id, roleMessageId,
			store.getInstance(this.id, roleMessageId));
	}

	/**
	 * Removes the link from this instance through role "Message" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeMessage(Object message) {
		if (message == null)
			return;
		store.removeLink(this.id, roleMessageId, message);
	}

	/**
	 * Connects this instance to the given instance using role "Message".
	 * If there is already an instance connected to this instance through role "Message", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param message the instance to connect
	 */
	public void setMessage(String message) {
		store.setLink(this.id,  roleMessageId, message);
	}
	/**
	 * Returns the instance connected to this instance through the role Queue.
	 * @return the connected instance
	 */
	public PDQueue getQueue() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleQueueId);
	 	return (PDQueue) store.load(PDQueue.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Queue.
	 * @return the connected instance(s)
	 */
	 public Collection<PDQueue> getQueues() {
		return (Collection<PDQueue>)(Object)store.getAndLoadInstances(this.id, roleQueueId, PDQueue.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Queue".
	 * If the given instance is null, nothing happens.
	 * @param queue the instance to connect
	 */
	public void addQueue(GUID queue) {
		if (queue != null) {
			store.addLink(this.id, roleQueueId, queue);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Queue".
	 * If the given instance is null, nothing happens.
	 * @param queue the instance to connect
	 */
	public void addQueue(PDQueue queue) {
		if (queue != null)
			addQueue(queue.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Queue".
	 * If the given collection of instances is null, nothing happens.
	 * @param queue the Collection of instances to connect
	 */
	public void addQueues(Collection<PDQueue> queues) {
		if (queues == null)
			return;
		for (PDQueue instance : queues)
			addQueue(instance);
	}

	/**
	 * Removes the link from this instance through role "Queue".
	 */
	public void removeQueue() {
		store.removeLink(this.id, roleQueueId,
			store.getInstance(this.id, roleQueueId));
	}

	/**
	 * Removes the link from this instance through role "Queue" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeQueue(Object queue) {
		if (queue == null)
			return;
		store.removeLink(this.id, roleQueueId, queue);
	}

	/**
	 * Removes the links from this instance through role "Queue" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeQueues(Collection<PDQueue> queues) {
		if (queues == null)
			return;
		for (PDQueue instance : queues)
			store.removeLink(this.id, roleQueueId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "Queue".
	 * If there is already an instance connected to this instance through role "Queue", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param queue the instance to connect
	 */
	public void setQueue(GUID queue) {
		store.setLink(this.id,  roleQueueId, queue);
	}
	/**
	 * Connects this instance to the given instance using role "Queue".
	 * If there is already an instance connected to this instance through role "Queue", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param queue the instance to connect
	 */
	public void setQueue(PDQueue queue) {
		setQueue(queue.getId());
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
