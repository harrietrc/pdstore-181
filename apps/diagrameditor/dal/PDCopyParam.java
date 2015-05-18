package diagrameditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "CopyParam" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("diagrameditor.dal.PDCopyParam");
 * @author PDGen
 */
public class PDCopyParam implements PDInstance,PDCopyParamI,PDNewParamI{

	public static final GUID typeId = new GUID("b81a82c59b1111e1a6ca002215eb452f"); 

	public static final GUID roleCopyId = new GUID("b81a82cc9b1111e1a6ca002215eb452f");
	public static final GUID roleDataId = new GUID("8da40203eb1211e1996178e4009ed4de");
	public static final GUID roleVisualizedById = new GUID("be8ef753eb1011e1860778e4009ed4de");
	public static final GUID roleTargetShapeListId = new GUID("8b076910c70811e1ae8378e4009ed4de");
	public static final GUID roleTargetShapeId = new GUID("b81a82c79b1111e1a6ca002215eb452f");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDCopyParam.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDCopyParam:" + name;
		else
			return "PDCopyParam:" + id;
	}

	/**
	 * Creates an PDCopyParam object representing a PDStore instance of type CopyParam.
	 * @param store the store the instance should be in
	 */
	public PDCopyParam(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDCopyParam object representing the instance with the given ID. 
	 * The ID must be of an instance of type CopyParam.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDCopyParam(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type CopyParam with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDCopyParam load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDCopyParam)instance;
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
	 * Returns the instance connected to this instance through the role copy.
	 * @return the connected instance
	 */
	public PDShape getCopy(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleCopyId);
	 	return (PDShape) store.load(PDShape.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role copy.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getCopys(){
		return (Collection<PDShape>)(Object)store.getAndLoadInstances(this.id, roleCopyId, PDShape.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "copy".
	 * If the given instance is null, nothing happens.
	 * @param copy the instance to connect
	 */
	public void addCopy(GUID copy){
		if (copy != null) {
			store.addLink(this.id, roleCopyId, copy);
		}
	}
	/**
	 * Connects this instance to the given instance using role "copy".
	 * If the given instance is null, nothing happens.
	 * @param copy the instance to connect
	 */
	public void addCopy(PDShape copy){
		if (copy != null)
			addCopy(copy.getId());
	}

	/**
	 * Connects this instance to the given instance using role "copy".
	 * If the given collection of instances is null, nothing happens.
	 * @param copy the Collection of instances to connect
	 */
	public void addCopys(Collection<PDShape> copys){
		if (copys == null)
			return;
		for (PDShape instance : copys)
			addCopy(instance);
	}

	/**
	 * Removes the link from this instance through role "copy".
	 */
	public void removeCopy(){
		store.removeLink(this.id, roleCopyId,
			store.getInstance(this.id, roleCopyId));
	}

	/**
	 * Removes the link from this instance through role "copy" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeCopy(PDShape copy){
		if (copy == null)
			return;
		store.removeLink(this.id, roleCopyId, copy.getId());
	}

	/**
	 * Removes the links from this instance through role "copy" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeCopys(Collection<PDShape> copys){
		if (copys == null)
			return;
		for (PDShape instance : copys)
			store.removeLink(this.id, roleCopyId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "copy".
	 * If there is already an instance connected to this instance through role "copy", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param copy the instance to connect
	 */
	public void setCopy(GUID copy) {
		store.setLink(this.id,  roleCopyId, copy);
	}
	/**
	 * Connects this instance to the given instance using role "copy".
	 * If there is already an instance connected to this instance through role "copy", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param copy the instance to connect
	 */
	public void setCopy(PDShape copy) 
{		setCopy(copy.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role data.
	 * @return the connected instance
	 */
	public String getData(){
	 	return (String)store.getInstance(this.id, roleDataId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role data.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getDatas(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleDataId);
	 }

   /**
	 * Connects this instance to the given instance using role "data".
	 * If the given instance is null, nothing happens.
	 * @param data the instance to connect
	 */
	public void addData(String data){
		if (data != null) {
			store.addLink(this.id, roleDataId, data);
		}
	}
	/**
	 * Connects this instance to the given instances using role "data".
	 * If the given collection of instances is null, nothing happens.
	 * @param data the Collection of instances to connect
	 */
	public void addDatas(Collection<String> datas){
		if (datas == null)
			return;
		for (String instance : datas)
			addData(instance);
	}

	/**
	 * Removes the link from this instance through role "data".
	 */
	public void removeData(){
		store.removeLink(this.id, roleDataId,
			store.getInstance(this.id, roleDataId));
	}

	/**
	 * Removes the link from this instance through role "data" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeData(String data){
		if (data == null)
			return;
		store.removeLink(this.id, roleDataId, data);
	}

	/**
	 * Connects this instance to the given instance using role "data".
	 * If there is already an instance connected to this instance through role "data", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param data the instance to connect
	 */
	public void setData(String data) {
		store.setLink(this.id,  roleDataId, data);
	}
	/**
	 * Returns the instance connected to this instance through the role visualized by.
	 * @return the connected instance
	 */
	public GUID getVisualizedBy(){
	 	return (GUID)store.getInstance(this.id, roleVisualizedById);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role visualized by.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getVisualizedBys(){
		return (Collection<GUID>)(Object)store.getInstances(this.id, roleVisualizedById);
	 }

   /**
	 * Connects this instance to the given instance using role "visualized by".
	 * If the given instance is null, nothing happens.
	 * @param visualizedBy the instance to connect
	 */
	public void addVisualizedBy(GUID visualizedBy){
		if (visualizedBy != null) {
			store.addLink(this.id, roleVisualizedById, visualizedBy);
		}
	}
	/**
	 * Connects this instance to the given instances using role "visualized by".
	 * If the given collection of instances is null, nothing happens.
	 * @param visualizedBy the Collection of instances to connect
	 */
	public void addVisualizedBys(Collection<GUID> visualizedBys){
		if (visualizedBys == null)
			return;
		for (GUID instance : visualizedBys)
			addVisualizedBy(instance);
	}

	/**
	 * Removes the link from this instance through role "visualized by".
	 */
	public void removeVisualizedBy(){
		store.removeLink(this.id, roleVisualizedById,
			store.getInstance(this.id, roleVisualizedById));
	}

	/**
	 * Removes the link from this instance through role "visualized by" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeVisualizedBy(GUID visualizedBy){
		if (visualizedBy == null)
			return;
		store.removeLink(this.id, roleVisualizedById, visualizedBy);
	}

	/**
	 * Connects this instance to the given instance using role "visualized by".
	 * If there is already an instance connected to this instance through role "visualized by", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizedBy the instance to connect
	 */
	public void setVisualizedBy(GUID visualizedBy) {
		store.setLink(this.id,  roleVisualizedById, visualizedBy);
	}
	/**
	 * Returns the instance connected to this instance through the role target shape list.
	 * @return the connected instance
	 */
	public Object getTargetShapeList(){
	 	return (Object)store.getInstance(this.id, roleTargetShapeListId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role target shape list.
	 * @return the connected instance(s)
	 */
	 public Collection<Object> getTargetShapeLists(){
		return (Collection<Object>)(Object)store.getInstances(this.id, roleTargetShapeListId);
	 }

   /**
	 * Connects this instance to the given instance using role "target shape list".
	 * If the given instance is null, nothing happens.
	 * @param targetShapeList the instance to connect
	 */
	public void addTargetShapeList(Object targetShapeList){
		if (targetShapeList != null) {
			store.addLink(this.id, roleTargetShapeListId, targetShapeList);
		}
	}
	/**
	 * Connects this instance to the given instances using role "target shape list".
	 * If the given collection of instances is null, nothing happens.
	 * @param targetShapeList the Collection of instances to connect
	 */
	public void addTargetShapeLists(Collection<Object> targetShapeLists){
		if (targetShapeLists == null)
			return;
		for (Object instance : targetShapeLists)
			addTargetShapeList(instance);
	}

	/**
	 * Removes the link from this instance through role "target shape list".
	 */
	public void removeTargetShapeList(){
		store.removeLink(this.id, roleTargetShapeListId,
			store.getInstance(this.id, roleTargetShapeListId));
	}

	/**
	 * Removes the link from this instance through role "target shape list" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTargetShapeList(Object targetShapeList){
		if (targetShapeList == null)
			return;
		store.removeLink(this.id, roleTargetShapeListId, targetShapeList);
	}

	/**
	 * Connects this instance to the given instance using role "target shape list".
	 * If there is already an instance connected to this instance through role "target shape list", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param targetShapeList the instance to connect
	 */
	public void setTargetShapeList(Object targetShapeList) {
		store.setLink(this.id,  roleTargetShapeListId, targetShapeList);
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
