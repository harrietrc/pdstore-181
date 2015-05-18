package diagrameditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Shape" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("diagrameditor.dal.PDShape");
 * @author PDGen
 */
public class PDShape implements PDInstance,PDShapeI{

	public static final GUID typeId = new GUID("920645110d6411e0b45a1cc1dec00ed3"); 

	public static final GUID roleVisualizedDataId = new GUID("be8ef752eb1011e1860778e4009ed4de");
	public static final GUID roleOriginalShapeId = new GUID("ce35eb73aab811e1a08a78e4009ed4de");
	public static final GUID roleShapeTypeId = new GUID("7420c754980111e1b62d1c6f65cd0117");
	public static final GUID roleColorId = new GUID("9206451b0d6411e0b45a1cc1dec00ed3");
	public static final GUID roleWidthId = new GUID("9206451a0d6411e0b45a1cc1dec00ed3");
	public static final GUID roleHeightId = new GUID("920645190d6411e0b45a1cc1dec00ed3");
	public static final GUID roleYId = new GUID("920645180d6411e0b45a1cc1dec00ed3");
	public static final GUID roleXId = new GUID("920645170d6411e0b45a1cc1dec00ed3");
	public static final GUID roleTypeId = new GUID("ee32adf0f68b11df860e1cc1dec00ed3");
	public static final GUID roleDiagramId = new GUID("b81a82cd9b1111e1a6da002215eb452f");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDShape.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDShape:" + name;
		else
			return "PDShape:" + id;
	}

	/**
	 * Creates an PDShape object representing a PDStore instance of type Shape.
	 * @param store the store the instance should be in
	 */
	public PDShape(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDShape object representing the instance with the given ID. 
	 * The ID must be of an instance of type Shape.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDShape(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Shape with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDShape load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDShape)instance;
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
	 * Returns the instance connected to this instance through the role visualized data.
	 * @return the connected instance
	 */
	public String getVisualizedData(){
	 	return (String)store.getInstance(this.id, roleVisualizedDataId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role visualized data.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getVisualizedDatas(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleVisualizedDataId);
	 }

   /**
	 * Connects this instance to the given instance using role "visualized data".
	 * If the given instance is null, nothing happens.
	 * @param visualizedData the instance to connect
	 */
	public void addVisualizedData(String visualizedData){
		if (visualizedData != null) {
			store.addLink(this.id, roleVisualizedDataId, visualizedData);
		}
	}
	/**
	 * Connects this instance to the given instances using role "visualized data".
	 * If the given collection of instances is null, nothing happens.
	 * @param visualizedData the Collection of instances to connect
	 */
	public void addVisualizedDatas(Collection<String> visualizedDatas){
		if (visualizedDatas == null)
			return;
		for (String instance : visualizedDatas)
			addVisualizedData(instance);
	}

	/**
	 * Removes the link from this instance through role "visualized data".
	 */
	public void removeVisualizedData(){
		store.removeLink(this.id, roleVisualizedDataId,
			store.getInstance(this.id, roleVisualizedDataId));
	}

	/**
	 * Removes the link from this instance through role "visualized data" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeVisualizedData(String visualizedData){
		if (visualizedData == null)
			return;
		store.removeLink(this.id, roleVisualizedDataId, visualizedData);
	}

	/**
	 * Connects this instance to the given instance using role "visualized data".
	 * If there is already an instance connected to this instance through role "visualized data", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizedData the instance to connect
	 */
	public void setVisualizedData(String visualizedData) {
		store.setLink(this.id,  roleVisualizedDataId, visualizedData);
	}
	/**
	 * Returns the instance connected to this instance through the role originalShape.
	 * @return the connected instance
	 */
	public PDShape getOriginalShape(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOriginalShapeId);
	 	return (PDShape) store.load(PDShape.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role originalShape.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getOriginalShapes(){
		return (Collection<PDShape>)(Object)store.getAndLoadInstances(this.id, roleOriginalShapeId, PDShape.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "originalShape".
	 * If the given instance is null, nothing happens.
	 * @param originalShape the instance to connect
	 */
	public void addOriginalShape(GUID originalShape){
		if (originalShape != null) {
			store.addLink(this.id, roleOriginalShapeId, originalShape);
		}
	}
	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If the given instance is null, nothing happens.
	 * @param originalShape the instance to connect
	 */
	public void addOriginalShape(PDShape originalShape){
		if (originalShape != null)
			addOriginalShape(originalShape.getId());
	}

	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If the given collection of instances is null, nothing happens.
	 * @param originalShape the Collection of instances to connect
	 */
	public void addOriginalShapes(Collection<PDShape> originalShapes){
		if (originalShapes == null)
			return;
		for (PDShape instance : originalShapes)
			addOriginalShape(instance);
	}

	/**
	 * Removes the link from this instance through role "originalShape".
	 */
	public void removeOriginalShape(){
		store.removeLink(this.id, roleOriginalShapeId,
			store.getInstance(this.id, roleOriginalShapeId));
	}

	/**
	 * Removes the link from this instance through role "originalShape" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOriginalShape(PDShape originalShape){
		if (originalShape == null)
			return;
		store.removeLink(this.id, roleOriginalShapeId, originalShape.getId());
	}

	/**
	 * Removes the links from this instance through role "originalShape" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOriginalShapes(Collection<PDShape> originalShapes){
		if (originalShapes == null)
			return;
		for (PDShape instance : originalShapes)
			store.removeLink(this.id, roleOriginalShapeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If there is already an instance connected to this instance through role "originalShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param originalShape the instance to connect
	 */
	public void setOriginalShape(GUID originalShape) {
		store.setLink(this.id,  roleOriginalShapeId, originalShape);
	}
	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If there is already an instance connected to this instance through role "originalShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param originalShape the instance to connect
	 */
	public void setOriginalShape(PDShape originalShape) 
{		setOriginalShape(originalShape.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role shapeType.
	 * @return the connected instance
	 */
	public PDType getShapeType(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleShapeTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role shapeType.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getShapeTypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleShapeTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(GUID shapeType){
		if (shapeType != null) {
			store.addLink(this.id, roleShapeTypeId, shapeType);
		}
	}
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(PDType shapeType){
		if (shapeType != null)
			addShapeType(shapeType.getId());
	}

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given collection of instances is null, nothing happens.
	 * @param shapeType the Collection of instances to connect
	 */
	public void addShapeTypes(Collection<PDType> shapeTypes){
		if (shapeTypes == null)
			return;
		for (PDType instance : shapeTypes)
			addShapeType(instance);
	}

	/**
	 * Removes the link from this instance through role "shapeType".
	 */
	public void removeShapeType(){
		store.removeLink(this.id, roleShapeTypeId,
			store.getInstance(this.id, roleShapeTypeId));
	}

	/**
	 * Removes the link from this instance through role "shapeType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShapeType(PDType shapeType){
		if (shapeType == null)
			return;
		store.removeLink(this.id, roleShapeTypeId, shapeType.getId());
	}

	/**
	 * Removes the links from this instance through role "shapeType" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeShapeTypes(Collection<PDType> shapeTypes){
		if (shapeTypes == null)
			return;
		for (PDType instance : shapeTypes)
			store.removeLink(this.id, roleShapeTypeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(GUID shapeType) {
		store.setLink(this.id,  roleShapeTypeId, shapeType);
	}
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(PDType shapeType) 
{		setShapeType(shapeType.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role color.
	 * @return the connected instance
	 */
	public String getColor(){
	 	return (String)store.getInstance(this.id, roleColorId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role color.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getColors(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleColorId);
	 }

   /**
	 * Connects this instance to the given instance using role "color".
	 * If the given instance is null, nothing happens.
	 * @param color the instance to connect
	 */
	public void addColor(String color){
		if (color != null) {
			store.addLink(this.id, roleColorId, color);
		}
	}
	/**
	 * Connects this instance to the given instances using role "color".
	 * If the given collection of instances is null, nothing happens.
	 * @param color the Collection of instances to connect
	 */
	public void addColors(Collection<String> colors){
		if (colors == null)
			return;
		for (String instance : colors)
			addColor(instance);
	}

	/**
	 * Removes the link from this instance through role "color".
	 */
	public void removeColor(){
		store.removeLink(this.id, roleColorId,
			store.getInstance(this.id, roleColorId));
	}

	/**
	 * Removes the link from this instance through role "color" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeColor(String color){
		if (color == null)
			return;
		store.removeLink(this.id, roleColorId, color);
	}

	/**
	 * Connects this instance to the given instance using role "color".
	 * If there is already an instance connected to this instance through role "color", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param color the instance to connect
	 */
	public void setColor(String color) {
		store.setLink(this.id,  roleColorId, color);
	}
	/**
	 * Returns the instance connected to this instance through the role width.
	 * @return the connected instance
	 */
	public Long getWidth(){
	 	return (Long)store.getInstance(this.id, roleWidthId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role width.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getWidths(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleWidthId);
	 }

   /**
	 * Connects this instance to the given instance using role "width".
	 * If the given instance is null, nothing happens.
	 * @param width the instance to connect
	 */
	public void addWidth(Long width){
		if (width != null) {
			store.addLink(this.id, roleWidthId, width);
		}
	}
	/**
	 * Connects this instance to the given instances using role "width".
	 * If the given collection of instances is null, nothing happens.
	 * @param width the Collection of instances to connect
	 */
	public void addWidths(Collection<Long> widths){
		if (widths == null)
			return;
		for (Long instance : widths)
			addWidth(instance);
	}

	/**
	 * Removes the link from this instance through role "width".
	 */
	public void removeWidth(){
		store.removeLink(this.id, roleWidthId,
			store.getInstance(this.id, roleWidthId));
	}

	/**
	 * Removes the link from this instance through role "width" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeWidth(Long width){
		if (width == null)
			return;
		store.removeLink(this.id, roleWidthId, width);
	}

	/**
	 * Connects this instance to the given instance using role "width".
	 * If there is already an instance connected to this instance through role "width", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param width the instance to connect
	 */
	public void setWidth(Long width) {
		store.setLink(this.id,  roleWidthId, width);
	}
	/**
	 * Returns the instance connected to this instance through the role height.
	 * @return the connected instance
	 */
	public Long getHeight(){
	 	return (Long)store.getInstance(this.id, roleHeightId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role height.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getHeights(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleHeightId);
	 }

   /**
	 * Connects this instance to the given instance using role "height".
	 * If the given instance is null, nothing happens.
	 * @param height the instance to connect
	 */
	public void addHeight(Long height){
		if (height != null) {
			store.addLink(this.id, roleHeightId, height);
		}
	}
	/**
	 * Connects this instance to the given instances using role "height".
	 * If the given collection of instances is null, nothing happens.
	 * @param height the Collection of instances to connect
	 */
	public void addHeights(Collection<Long> heights){
		if (heights == null)
			return;
		for (Long instance : heights)
			addHeight(instance);
	}

	/**
	 * Removes the link from this instance through role "height".
	 */
	public void removeHeight(){
		store.removeLink(this.id, roleHeightId,
			store.getInstance(this.id, roleHeightId));
	}

	/**
	 * Removes the link from this instance through role "height" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHeight(Long height){
		if (height == null)
			return;
		store.removeLink(this.id, roleHeightId, height);
	}

	/**
	 * Connects this instance to the given instance using role "height".
	 * If there is already an instance connected to this instance through role "height", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param height the instance to connect
	 */
	public void setHeight(Long height) {
		store.setLink(this.id,  roleHeightId, height);
	}
	/**
	 * Returns the instance connected to this instance through the role y.
	 * @return the connected instance
	 */
	public Long getY(){
	 	return (Long)store.getInstance(this.id, roleYId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role y.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getYs(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleYId);
	 }

   /**
	 * Connects this instance to the given instance using role "y".
	 * If the given instance is null, nothing happens.
	 * @param y the instance to connect
	 */
	public void addY(Long y){
		if (y != null) {
			store.addLink(this.id, roleYId, y);
		}
	}
	/**
	 * Connects this instance to the given instances using role "y".
	 * If the given collection of instances is null, nothing happens.
	 * @param y the Collection of instances to connect
	 */
	public void addYs(Collection<Long> ys){
		if (ys == null)
			return;
		for (Long instance : ys)
			addY(instance);
	}

	/**
	 * Removes the link from this instance through role "y".
	 */
	public void removeY(){
		store.removeLink(this.id, roleYId,
			store.getInstance(this.id, roleYId));
	}

	/**
	 * Removes the link from this instance through role "y" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeY(Long y){
		if (y == null)
			return;
		store.removeLink(this.id, roleYId, y);
	}

	/**
	 * Connects this instance to the given instance using role "y".
	 * If there is already an instance connected to this instance through role "y", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param y the instance to connect
	 */
	public void setY(Long y) {
		store.setLink(this.id,  roleYId, y);
	}
	/**
	 * Returns the instance connected to this instance through the role x.
	 * @return the connected instance
	 */
	public Long getX(){
	 	return (Long)store.getInstance(this.id, roleXId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role x.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getXs(){
		return (Collection<Long>)(Object)store.getInstances(this.id, roleXId);
	 }

   /**
	 * Connects this instance to the given instance using role "x".
	 * If the given instance is null, nothing happens.
	 * @param x the instance to connect
	 */
	public void addX(Long x){
		if (x != null) {
			store.addLink(this.id, roleXId, x);
		}
	}
	/**
	 * Connects this instance to the given instances using role "x".
	 * If the given collection of instances is null, nothing happens.
	 * @param x the Collection of instances to connect
	 */
	public void addXs(Collection<Long> xs){
		if (xs == null)
			return;
		for (Long instance : xs)
			addX(instance);
	}

	/**
	 * Removes the link from this instance through role "x".
	 */
	public void removeX(){
		store.removeLink(this.id, roleXId,
			store.getInstance(this.id, roleXId));
	}

	/**
	 * Removes the link from this instance through role "x" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeX(Long x){
		if (x == null)
			return;
		store.removeLink(this.id, roleXId, x);
	}

	/**
	 * Connects this instance to the given instance using role "x".
	 * If there is already an instance connected to this instance through role "x", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param x the instance to connect
	 */
	public void setX(Long x) {
		store.setLink(this.id,  roleXId, x);
	}
	/**
	 * Returns the instance connected to this instance through the role type.
	 * @return the connected instance
	 */
	public PDType getType(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleTypeId);
	 	return (PDType) store.load(PDType.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getTypes(){
		return (Collection<PDType>)(Object)store.getAndLoadInstances(this.id, roleTypeId, PDType.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(GUID type){
		if (type != null) {
			store.addLink(this.id, roleTypeId, type);
		}
	}
	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(PDType type){
		if (type != null)
			addType(type.getId());
	}

	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given collection of instances is null, nothing happens.
	 * @param type the Collection of instances to connect
	 */
	public void addTypes(Collection<PDType> types){
		if (types == null)
			return;
		for (PDType instance : types)
			addType(instance);
	}

	/**
	 * Removes the link from this instance through role "type".
	 */
	public void removeType(){
		store.removeLink(this.id, roleTypeId,
			store.getInstance(this.id, roleTypeId));
	}

	/**
	 * Removes the link from this instance through role "type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeType(PDType type){
		if (type == null)
			return;
		store.removeLink(this.id, roleTypeId, type.getId());
	}

	/**
	 * Removes the links from this instance through role "type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTypes(Collection<PDType> types){
		if (types == null)
			return;
		for (PDType instance : types)
			store.removeLink(this.id, roleTypeId, instance.getId());
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
	public void setType(PDType type) 
{		setType(type.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role Diagram.
	 * @return the connected instance
	 */
	public PDDiagram getDiagram(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleDiagramId);
	 	return (PDDiagram) store.load(PDDiagram.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Diagram.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagram> getDiagrams(){
		return (Collection<PDDiagram>)(Object)store.getAndLoadInstances(this.id, roleDiagramId, PDDiagram.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given instance is null, nothing happens.
	 * @param diagram the instance to connect
	 */
	public void addDiagram(GUID diagram){
		if (diagram != null) {
			store.addLink(this.id, roleDiagramId, diagram);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given instance is null, nothing happens.
	 * @param diagram the instance to connect
	 */
	public void addDiagram(PDDiagram diagram){
		if (diagram != null)
			addDiagram(diagram.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given collection of instances is null, nothing happens.
	 * @param diagram the Collection of instances to connect
	 */
	public void addDiagrams(Collection<PDDiagram> diagrams){
		if (diagrams == null)
			return;
		for (PDDiagram instance : diagrams)
			addDiagram(instance);
	}

	/**
	 * Removes the link from this instance through role "Diagram".
	 */
	public void removeDiagram(){
		store.removeLink(this.id, roleDiagramId,
			store.getInstance(this.id, roleDiagramId));
	}

	/**
	 * Removes the link from this instance through role "Diagram" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDiagram(PDDiagram diagram){
		if (diagram == null)
			return;
		store.removeLink(this.id, roleDiagramId, diagram.getId());
	}

	/**
	 * Removes the links from this instance through role "Diagram" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeDiagrams(Collection<PDDiagram> diagrams){
		if (diagrams == null)
			return;
		for (PDDiagram instance : diagrams)
			store.removeLink(this.id, roleDiagramId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If there is already an instance connected to this instance through role "Diagram", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagram the instance to connect
	 */
	public void setDiagram(GUID diagram) {
		store.setLink(this.id,  roleDiagramId, diagram);
	}
	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If there is already an instance connected to this instance through role "Diagram", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagram the instance to connect
	 */
	public void setDiagram(PDDiagram diagram) 
{		setDiagram(diagram.getId());
	}
}
