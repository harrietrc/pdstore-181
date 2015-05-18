package pdstore.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Widget" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.dal.PDWidget");
 * @author PDGen
 */
public class PDWidget implements PDInstance,PDWidgetI{

	public static final GUID typeId = new GUID("005422208cf711e1926b842b2b9af4fd"); 

	public static final GUID roleVisualizesId = new GUID("005422198cf711e1927b842b2b9af4fd");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDWidget.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDWidget:" + name;
		else
			return "PDWidget:" + id;
	}

	/**
	 * Creates an PDWidget object representing a PDStore instance of type Widget.
	 * @param store the store the instance should be in
	 */
	public PDWidget(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDWidget object representing the instance with the given ID. 
	 * The ID must be of an instance of type Widget.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDWidget(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Widget with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDWidget load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDWidget)instance;
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
	 * Returns the instance connected to this instance through the role visualizes.
	 * @return the connected instance
	 */
	public PDInstance getVisualizes(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleVisualizesId);
	 	return (PDInstance) store.load(instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role visualizes.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getVisualizess(){
		return (Collection<PDInstance>)(Object)store.getAndLoadInstances(this.id, roleVisualizesId);
	 }

   /**
	 * Connects this instance to the given instance using role "visualizes".
	 * If the given instance is null, nothing happens.
	 * @param visualizes the instance to connect
	 */
	public void addVisualizes(GUID visualizes){
		if (visualizes != null) {
			store.addLink(this.id, roleVisualizesId, visualizes);
		}
	}
	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If the given instance is null, nothing happens.
	 * @param visualizes the instance to connect
	 */
	public void addVisualizes(PDInstance visualizes){
		if (visualizes != null)
			addVisualizes(visualizes.getId());
	}

	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If the given collection of instances is null, nothing happens.
	 * @param visualizes the Collection of instances to connect
	 */
	public void addVisualizess(Collection<PDInstance> visualizess){
		if (visualizess == null)
			return;
		for (PDInstance instance : visualizess)
			addVisualizes(instance);
	}

	/**
	 * Removes the link from this instance through role "visualizes".
	 */
	public void removeVisualizes(){
		store.removeLink(this.id, roleVisualizesId,
			store.getInstance(this.id, roleVisualizesId));
	}

	/**
	 * Removes the link from this instance through role "visualizes" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeVisualizes(PDInstance visualizes){
		if (visualizes == null)
			return;
		store.removeLink(this.id, roleVisualizesId, visualizes.getId());
	}

	/**
	 * Removes the links from this instance through role "visualizes" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeVisualizess(Collection<PDInstance> visualizess){
		if (visualizess == null)
			return;
		for (PDInstance instance : visualizess)
			store.removeLink(this.id, roleVisualizesId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If there is already an instance connected to this instance through role "visualizes", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizes the instance to connect
	 */
	public void setVisualizes(GUID visualizes) {
		store.setLink(this.id,  roleVisualizesId, visualizes);
	}
	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If there is already an instance connected to this instance through role "visualizes", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizes the instance to connect
	 */
	public void setVisualizes(PDInstance visualizes) 
{		setVisualizes(visualizes.getId());
	}
}
