package texteditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Cut" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("texteditor.dal.PDCut");
 * @author PDGen
 */
public class PDCut implements PDInstance {

	public static final GUID typeId = new GUID("3c08266554d111e09c2a001e6805726d"); 

	public static final GUID roleWordId = new GUID("3c08266f54d111e09c2a001e6805726d");
	public static final GUID roleTypeId = new GUID("ee32adf0f68b11df860e1cc1dec00ed3");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDCut.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDCut:" + name;
		else
			return "PDCut:" + id;
	}

	/**
	 * Creates an PDCut object representing a PDStore instance of type Cut.
	 * @param store the store the instance should be in
	 */
	public PDCut(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDCut object representing the instance with the given ID. 
	 * The ID must be of an instance of type Cut.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDCut(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Cut with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDCut load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDCut)instance;
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
	 * Returns the instance connected to this instance through the role word.
	 * @return the connected instance
	 */
	public PDWord getWord() {
		GUID instanceId = (GUID) store.getInstance(this.id, roleWordId);
	 	return (PDWord) store.load(PDWord.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role word.
	 * @return the connected instance(s)
	 */
	 public Collection<PDWord> getWords() {
		return (Collection<PDWord>)(Object)store.getAndLoadInstances(this.id, roleWordId, PDWord.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "word".
	 * If the given instance is null, nothing happens.
	 * @param word the instance to connect
	 */
	public void addWord(GUID word) {
		if (word != null) {
			store.addLink(this.id, roleWordId, word);
		}
	}
	/**
	 * Connects this instance to the given instance using role "word".
	 * If the given instance is null, nothing happens.
	 * @param word the instance to connect
	 */
	public void addWord(PDWord word) {
		if (word != null)
			addWord(word.getId());
	}

	/**
	 * Connects this instance to the given instance using role "word".
	 * If the given collection of instances is null, nothing happens.
	 * @param word the Collection of instances to connect
	 */
	public void addWords(Collection<PDWord> words) {
		if (words == null)
			return;
		for (PDWord instance : words)
			addWord(instance);
	}

	/**
	 * Removes the link from this instance through role "word".
	 */
	public void removeWord() {
		store.removeLink(this.id, roleWordId,
			store.getInstance(this.id, roleWordId));
	}

	/**
	 * Removes the link from this instance through role "word" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeWord(Object word) {
		if (word == null)
			return;
		store.removeLink(this.id, roleWordId, word);
	}

	/**
	 * Removes the links from this instance through role "word" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeWords(Collection<PDWord> words) {
		if (words == null)
			return;
		for (PDWord instance : words)
			store.removeLink(this.id, roleWordId, instance);
	}

	/**
	 * Connects this instance to the given instance using role "word".
	 * If there is already an instance connected to this instance through role "word", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param word the instance to connect
	 */
	public void setWord(GUID word) {
		store.setLink(this.id,  roleWordId, word);
	}
	/**
	 * Connects this instance to the given instance using role "word".
	 * If there is already an instance connected to this instance through role "word", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param word the instance to connect
	 */
	public void setWord(PDWord word) {
		setWord(word.getId());
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

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
