package diagrameditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "ColorParam" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("diagrameditor.dal.PDColorParam");
 * @author PDGen
 */
public class PDColorParam implements PDInstance,PDColorParamI{

	public static final GUID typeId = new GUID("b81a82c39b1111e1a6ca002215eb452f"); 

	public static final GUID roleNewColorId = new GUID("b81a82c89b1111e1a6ca002215eb452f");
	public static final GUID roleTargetShapeId = new GUID("b81a82c79b1111e1a6ca002215eb452f");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDColorParam.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDColorParam:" + name;
		else
			return "PDColorParam:" + id;
	}

	/**
	 * Creates an PDColorParam object representing a PDStore instance of type ColorParam.
	 * @param store the store the instance should be in
	 */
	public PDColorParam(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDColorParam object representing the instance with the given ID. 
	 * The ID must be of an instance of type ColorParam.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDColorParam(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type ColorParam with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDColorParam load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDColorParam)instance;
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
	 * Returns the instance connected to this instance through the role newColor.
	 * @return the connected instance
	 */
	public String getNewColor(){
	 	return (String)store.getInstance(this.id, roleNewColorId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role newColor.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getNewColors(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleNewColorId);
	 }

   /**
	 * Connects this instance to the given instance using role "newColor".
	 * If the given instance is null, nothing happens.
	 * @param newColor the instance to connect
	 */
	public void addNewColor(String newColor){
		if (newColor != null) {
			store.addLink(this.id, roleNewColorId, newColor);
		}
	}
	/**
	 * Connects this instance to the given instances using role "newColor".
	 * If the given collection of instances is null, nothing happens.
	 * @param newColor the Collection of instances to connect
	 */
	public void addNewColors(Collection<String> newColors){
		if (newColors == null)
			return;
		for (String instance : newColors)
			addNewColor(instance);
	}

	/**
	 * Removes the link from this instance through role "newColor".
	 */
	public void removeNewColor(){
		store.removeLink(this.id, roleNewColorId,
			store.getInstance(this.id, roleNewColorId));
	}

	/**
	 * Removes the link from this instance through role "newColor" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNewColor(String newColor){
		if (newColor == null)
			return;
		store.removeLink(this.id, roleNewColorId, newColor);
	}

	/**
	 * Connects this instance to the given instance using role "newColor".
	 * If there is already an instance connected to this instance through role "newColor", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param newColor the instance to connect
	 */
	public void setNewColor(String newColor) {
		store.setLink(this.id,  roleNewColorId, newColor);
	}
	/**
	 * Returns the instance connected to this instance through the role targetShape.
	 * @return the connected instance
	 */
	public PDShape getTargetShape(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleTargetShapeId);
	 	return (PDShape) store.load(PDShape.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role targetShape.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getTargetShapes(){
		return (Collection<PDShape>)(Object)store.getAndLoadInstances(this.id, roleTargetShapeId, PDShape.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "targetShape".
	 * If the given instance is null, nothing happens.
	 * @param targetShape the instance to connect
	 */
	public void addTargetShape(GUID targetShape){
		if (targetShape != null) {
			store.addLink(this.id, roleTargetShapeId, targetShape);
		}
	}
	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If the given instance is null, nothing happens.
	 * @param targetShape the instance to connect
	 */
	public void addTargetShape(PDShape targetShape){
		if (targetShape != null)
			addTargetShape(targetShape.getId());
	}

	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If the given collection of instances is null, nothing happens.
	 * @param targetShape the Collection of instances to connect
	 */
	public void addTargetShapes(Collection<PDShape> targetShapes){
		if (targetShapes == null)
			return;
		for (PDShape instance : targetShapes)
			addTargetShape(instance);
	}

	/**
	 * Removes the link from this instance through role "targetShape".
	 */
	public void removeTargetShape(){
		store.removeLink(this.id, roleTargetShapeId,
			store.getInstance(this.id, roleTargetShapeId));
	}

	/**
	 * Removes the link from this instance through role "targetShape" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTargetShape(PDShape targetShape){
		if (targetShape == null)
			return;
		store.removeLink(this.id, roleTargetShapeId, targetShape.getId());
	}

	/**
	 * Removes the links from this instance through role "targetShape" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTargetShapes(Collection<PDShape> targetShapes){
		if (targetShapes == null)
			return;
		for (PDShape instance : targetShapes)
			store.removeLink(this.id, roleTargetShapeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If there is already an instance connected to this instance through role "targetShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param targetShape the instance to connect
	 */
	public void setTargetShape(GUID targetShape) {
		store.setLink(this.id,  roleTargetShapeId, targetShape);
	}
	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If there is already an instance connected to this instance through role "targetShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param targetShape the instance to connect
	 */
	public void setTargetShape(PDShape targetShape) 
{		setTargetShape(targetShape.getId());
	}
}
