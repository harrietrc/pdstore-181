package texteditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Copy" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("texteditor.dal.PDCopy");
 * @author PDGen
 */
public class PDCopy implements PDInstance {

	public static final GUID typeId = new GUID("3c08266654d111e09c2a001e6805726d"); 

	public static final GUID roleToAfterId = new GUID("3c08267354d111e09c2a001e6805726d");
	public static final GUID roleNewWordId = new GUID("3c08267254d111e09c2a001e6805726d");
	public static final GUID roleTypeId = new GUID("ee32adf0f68b11df860e1cc1dec00ed3");
	public static final GUID roleOriginalWordId = new GUID("3c08267154d111e09c2a001e6805726d");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDCopy.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDCopy:" + name;
		else
			return "PDCopy:" + id;
	}

	/**
	 * Creates an PDCopy object representing a PDStore instance of type Copy.
	 * @param store the store the instance should be in
	 */
	public PDCopy(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDCopy object representing the instance with the given ID. 
	 * The ID must be of an instance of type Copy.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDCopy(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Copy with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDCopy load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDCopy)instance;
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
	 * Returns the instance connected to this instance through the role toAfter.
	 * @return the connected instance
	 */
	public PDWord getToAfter() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleToAfterId);
	 	return (PDWord) store.load(PDWord.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role toAfter.
	 * @return the connected instance(s)
	 */
	 public Collection<PDWord> getToAfters() {
		return (Collection<PDWord>)(Object)store.getAndLoadInstances(this.id, roleToAfterId, PDWord.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "toAfter".
	 * If the given instance is null, nothing happens.
	 * @param toAfter the instance to connect
	 */
	public void addToAfter(GUID toAfter) {
		if (toAfter != null) {
			store.addLink(this.id, roleToAfterId, toAfter);
		}
	}
	/**
	 * Connects this instance to the given instance using role "toAfter".
	 * If the given instance is null, nothing happens.
	 * @param toAfter the instance to connect
	 */
	public void addToAfter(PDWord toAfter) {
		if (toAfter != null)
			addToAfter(toAfter.getId());
	}

	/**
	 * Connects this instance to the given instance using role "toAfter".
	 * If the given collection of instances is null, nothing happens.
	 * @param toAfter the Collection of instances to connect
	 */
	public void addToAfters(Collection<PDWord> toAfters) {
		if (toAfters == null)
			return;
		for (PDWord instance : toAfters)
			addToAfter(instance);
	}

	/**
	 * Removes the link from this instance through role "toAfter".
	 */
	public void removeToAfter() {
		store.removeLink(this.id, roleToAfterId,
			store.getInstance(this.id, roleToAfterId));
	}

	/**
	 * Removes the link from this instance through role "toAfter" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeToAfter(Object toAfter) {
		if (toAfter == null)
			return;
		store.removeLink(this.id, roleToAfterId, toAfter);
	}

	/**
	 * Removes the links from this instance through role "toAfter" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeToAfters(Collection<PDWord> toAfters) {
		if (toAfters == null)
			return;
		for (PDWord instance : toAfters)
			store.removeLink(this.id, roleToAfterId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "toAfter".
	 * If there is already an instance connected to this instance through role "toAfter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param toAfter the instance to connect
	 */
	public void setToAfter(GUID toAfter) {
		store.setLink(this.id,  roleToAfterId, toAfter);
	}
	/**
	 * Connects this instance to the given instance using role "toAfter".
	 * If there is already an instance connected to this instance through role "toAfter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param toAfter the instance to connect
	 */
	public void setToAfter(PDWord toAfter) {
		setToAfter(toAfter.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role newWord.
	 * @return the connected instance
	 */
	public PDWord getNewWord() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleNewWordId);
	 	return (PDWord) store.load(PDWord.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role newWord.
	 * @return the connected instance(s)
	 */
	 public Collection<PDWord> getNewWords() {
		return (Collection<PDWord>)(Object)store.getAndLoadInstances(this.id, roleNewWordId, PDWord.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "newWord".
	 * If the given instance is null, nothing happens.
	 * @param newWord the instance to connect
	 */
	public void addNewWord(GUID newWord) {
		if (newWord != null) {
			store.addLink(this.id, roleNewWordId, newWord);
		}
	}
	/**
	 * Connects this instance to the given instance using role "newWord".
	 * If the given instance is null, nothing happens.
	 * @param newWord the instance to connect
	 */
	public void addNewWord(PDWord newWord) {
		if (newWord != null)
			addNewWord(newWord.getId());
	}

	/**
	 * Connects this instance to the given instance using role "newWord".
	 * If the given collection of instances is null, nothing happens.
	 * @param newWord the Collection of instances to connect
	 */
	public void addNewWords(Collection<PDWord> newWords) {
		if (newWords == null)
			return;
		for (PDWord instance : newWords)
			addNewWord(instance);
	}

	/**
	 * Removes the link from this instance through role "newWord".
	 */
	public void removeNewWord() {
		store.removeLink(this.id, roleNewWordId,
			store.getInstance(this.id, roleNewWordId));
	}

	/**
	 * Removes the link from this instance through role "newWord" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNewWord(Object newWord) {
		if (newWord == null)
			return;
		store.removeLink(this.id, roleNewWordId, newWord);
	}

	/**
	 * Removes the links from this instance through role "newWord" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNewWords(Collection<PDWord> newWords) {
		if (newWords == null)
			return;
		for (PDWord instance : newWords)
			store.removeLink(this.id, roleNewWordId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "newWord".
	 * If there is already an instance connected to this instance through role "newWord", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param newWord the instance to connect
	 */
	public void setNewWord(GUID newWord) {
		store.setLink(this.id,  roleNewWordId, newWord);
	}
	/**
	 * Connects this instance to the given instance using role "newWord".
	 * If there is already an instance connected to this instance through role "newWord", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param newWord the instance to connect
	 */
	public void setNewWord(PDWord newWord) {
		setNewWord(newWord.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role type.
	 * @return the connected instance
	 */
	public PDType getType() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getTypes() {
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(GUID type) {
		if (type != null) {
			store.addLink(this.id, roleTypeId, type);
		}
	}
	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(PDType type) {
		if (type != null)
			addType(type.getId());
	}

	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given collection of instances is null, nothing happens.
	 * @param type the Collection of instances to connect
	 */
	public void addTypes(Collection<PDType> types) {
		if (types == null)
			return;
		for (PDType instance : types)
			addType(instance);
	}

	/**
	 * Removes the link from this instance through role "type".
	 */
	public void removeType() {
		store.removeLink(this.id, roleTypeId,
			store.getInstance(this.id, roleTypeId));
	}

	/**
	 * Removes the link from this instance through role "type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeType(Object type) {
		if (type == null)
			return;
		store.removeLink(this.id, roleTypeId, type);
	}

	/**
	 * Removes the links from this instance through role "type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTypes(Collection<PDType> types) {
		if (types == null)
			return;
		for (PDType instance : types)
			store.removeLink(this.id, roleTypeId, instance);
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
	public void setType(PDType type) {
		setType(type.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role originalWord.
	 * @return the connected instance
	 */
	public PDWord getOriginalWord() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleOriginalWordId);
	 	return (PDWord) store.load(PDWord.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role originalWord.
	 * @return the connected instance(s)
	 */
	 public Collection<PDWord> getOriginalWords() {
		return (Collection<PDWord>)(Object)store.getAndLoadInstances(this.id, roleOriginalWordId, PDWord.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "originalWord".
	 * If the given instance is null, nothing happens.
	 * @param originalWord the instance to connect
	 */
	public void addOriginalWord(GUID originalWord) {
		if (originalWord != null) {
			store.addLink(this.id, roleOriginalWordId, originalWord);
		}
	}
	/**
	 * Connects this instance to the given instance using role "originalWord".
	 * If the given instance is null, nothing happens.
	 * @param originalWord the instance to connect
	 */
	public void addOriginalWord(PDWord originalWord) {
		if (originalWord != null)
			addOriginalWord(originalWord.getId());
	}

	/**
	 * Connects this instance to the given instance using role "originalWord".
	 * If the given collection of instances is null, nothing happens.
	 * @param originalWord the Collection of instances to connect
	 */
	public void addOriginalWords(Collection<PDWord> originalWords) {
		if (originalWords == null)
			return;
		for (PDWord instance : originalWords)
			addOriginalWord(instance);
	}

	/**
	 * Removes the link from this instance through role "originalWord".
	 */
	public void removeOriginalWord() {
		store.removeLink(this.id, roleOriginalWordId,
			store.getInstance(this.id, roleOriginalWordId));
	}

	/**
	 * Removes the link from this instance through role "originalWord" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOriginalWord(Object originalWord) {
		if (originalWord == null)
			return;
		store.removeLink(this.id, roleOriginalWordId, originalWord);
	}

	/**
	 * Removes the links from this instance through role "originalWord" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOriginalWords(Collection<PDWord> originalWords) {
		if (originalWords == null)
			return;
		for (PDWord instance : originalWords)
			store.removeLink(this.id, roleOriginalWordId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "originalWord".
	 * If there is already an instance connected to this instance through role "originalWord", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param originalWord the instance to connect
	 */
	public void setOriginalWord(GUID originalWord) {
		store.setLink(this.id,  roleOriginalWordId, originalWord);
	}
	/**
	 * Connects this instance to the given instance using role "originalWord".
	 * If there is already an instance connected to this instance through role "originalWord", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param originalWord the instance to connect
	 */
	public void setOriginalWord(PDWord originalWord) {
		setOriginalWord(originalWord.getId());
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
