package texteditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Word" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("texteditor.dal.PDWord");
 * @author PDGen
 */
public class PDWord implements PDInstance {

	public static final GUID typeId = new GUID("3c08266254d111e09c2a001e6805726d"); 

	public static final GUID roleTextId = new GUID("3c08266b54d111e09c2a001e6805726d");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDWord.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDWord:" + name;
		else
			return "PDWord:" + id;
	}

	/**
	 * Creates an PDWord object representing a PDStore instance of type Word.
	 * @param store the store the instance should be in
	 */
	public PDWord(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDWord object representing the instance with the given ID. 
	 * The ID must be of an instance of type Word.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDWord(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Word with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDWord load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDWord)instance;
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
	 * Returns the instance connected to this instance through the role text.
	 * @return the connected instance
	 */
	public String getText() {
	 	return (String)store.getInstance(this.id, roleTextId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role text.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getTexts() {
		return (Collection<String>)(Object)store.getInstances(this.id, roleTextId);
	 }

   /**
	 * Connects this instance to the given instance using role "text".
	 * If the given instance is null, nothing happens.
	 * @param text the instance to connect
	 */
	public void addText(String text) {
		if (text != null) {
			store.addLink(this.id, roleTextId, text);
		}
	}
	/**
	 * Connects this instance to the given instances using role "text".
	 * If the given collection of instances is null, nothing happens.
	 * @param text the Collection of instances to connect
	 */
	public void addTexts(Collection<String> texts) {
		if (texts == null)
			return;
		for (String instance : texts)
			addText(instance);
	}

	/**
	 * Removes the link from this instance through role "text".
	 */
	public void removeText() {
		store.removeLink(this.id, roleTextId,
			store.getInstance(this.id, roleTextId));
	}

	/**
	 * Removes the link from this instance through role "text" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeText(Object text) {
		if (text == null)
			return;
		store.removeLink(this.id, roleTextId, text);
	}

	/**
	 * Connects this instance to the given instance using role "text".
	 * If there is already an instance connected to this instance through role "text", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param text the instance to connect
	 */
	public void setText(String text) {
		store.setLink(this.id,  roleTextId, text);
	}

	@Override
	public PDWorkingCopy getPDWorkingCopy() {
		// TODO Auto-generated method stub
		return null;
	}
}
