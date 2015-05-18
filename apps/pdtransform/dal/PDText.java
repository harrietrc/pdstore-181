package pdtransform.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Text" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdtransform.dal.PDText");
 * @author PDGen
 */
public class PDText implements PDInstance,PDTextI{

	public static final GUID typeId = new GUID("caba85348e0ede11980f9a097666e103"); 

	public static final GUID roleContentId = new GUID("c8ba85348e0ede11980f9a097666e103");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDText.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDText:" + name;
		else
			return "PDText:" + id;
	}

	/**
	 * Creates an PDText object representing a PDStore instance of type Text.
	 * @param store the store the instance should be in
	 */
	public PDText(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDText object representing the instance with the given ID. 
	 * The ID must be of an instance of type Text.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDText(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Text with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDText load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDText)instance;
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
	 * Returns the instance connected to this instance through the role content.
	 * @return the connected instance
	 */
	public String getContent(){
	 	return (String)store.getInstance(this.id, roleContentId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role content.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getContents(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleContentId);
	 }

   /**
	 * Connects this instance to the given instance using role "content".
	 * If the given instance is null, nothing happens.
	 * @param content the instance to connect
	 */
	public void addContent(String content){
		if (content != null) {
			store.addLink(this.id, roleContentId, content);
		}
	}
	/**
	 * Connects this instance to the given instances using role "content".
	 * If the given collection of instances is null, nothing happens.
	 * @param content the Collection of instances to connect
	 */
	public void addContents(Collection<String> contents){
		if (contents == null)
			return;
		for (String instance : contents)
			addContent(instance);
	}

	/**
	 * Removes the link from this instance through role "content".
	 */
	public void removeContent(){
		store.removeLink(this.id, roleContentId,
			store.getInstance(this.id, roleContentId));
	}

	/**
	 * Removes the link from this instance through role "content" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeContent(String content){
		if (content == null)
			return;
		store.removeLink(this.id, roleContentId, content);
	}

	/**
	 * Connects this instance to the given instance using role "content".
	 * If there is already an instance connected to this instance through role "content", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param content the instance to connect
	 */
	public void setContent(String content) {
		store.setLink(this.id,  roleContentId, content);
	}
}
