package diagrameditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Diagram" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("diagrameditor.dal.PDDiagram");
 * @author PDGen
 */
public class PDDiagram implements PDInstance,PDDiagramI{

	public static final GUID typeId = new GUID("920645100d6411e0b45a1cc1dec00ed3"); 

	public static final GUID rolePreviousId = new GUID("85d14343edab11e1aee50026b97da292");
	public static final GUID roleNextId = new GUID("85d14343edab11e1aef50026b97da292");
	public static final GUID roleShapeId = new GUID("b81a82cd9b1111e1a6ca002215eb452f");
	public static final GUID roleOperationApplicationId = new GUID("920645130d6411e0b45a1cc1dec00ed3");
	public static final GUID roleDiagramSetId = new GUID("f119cf12eda611e1b6d60026b97da292");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDDiagram.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDDiagram:" + name;
		else
			return "PDDiagram:" + id;
	}

	/**
	 * Creates an PDDiagram object representing a PDStore instance of type Diagram.
	 * @param store the store the instance should be in
	 */
	public PDDiagram(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDDiagram object representing the instance with the given ID. 
	 * The ID must be of an instance of type Diagram.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDDiagram(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Diagram with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDDiagram load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDDiagram)instance;
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
	 * Returns the instance connected to this instance through the role previous.
	 * @return the connected instance
	 */
	public PDDiagram getPrevious(){
		GUID instanceId = (GUID) store.getInstance(this.id, rolePreviousId);
	 	return (PDDiagram) store.load(PDDiagram.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role previous.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagram> getPreviouss(){
		return (Collection<PDDiagram>)(Object)store.getAndLoadInstances(this.id, rolePreviousId, PDDiagram.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "previous".
	 * If the given instance is null, nothing happens.
	 * @param previous the instance to connect
	 */
	public void addPrevious(GUID previous){
		if (previous != null) {
			store.addLink(this.id, rolePreviousId, previous);
		}
	}
	/**
	 * Connects this instance to the given instance using role "previous".
	 * If the given instance is null, nothing happens.
	 * @param previous the instance to connect
	 */
	public void addPrevious(PDDiagram previous){
		if (previous != null)
			addPrevious(previous.getId());
	}

	/**
	 * Connects this instance to the given instance using role "previous".
	 * If the given collection of instances is null, nothing happens.
	 * @param previous the Collection of instances to connect
	 */
	public void addPreviouss(Collection<PDDiagram> previouss){
		if (previouss == null)
			return;
		for (PDDiagram instance : previouss)
			addPrevious(instance);
	}

	/**
	 * Removes the link from this instance through role "previous".
	 */
	public void removePrevious(){
		store.removeLink(this.id, rolePreviousId,
			store.getInstance(this.id, rolePreviousId));
	}

	/**
	 * Removes the link from this instance through role "previous" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrevious(PDDiagram previous){
		if (previous == null)
			return;
		store.removeLink(this.id, rolePreviousId, previous.getId());
	}

	/**
	 * Removes the links from this instance through role "previous" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removePreviouss(Collection<PDDiagram> previouss){
		if (previouss == null)
			return;
		for (PDDiagram instance : previouss)
			store.removeLink(this.id, rolePreviousId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "previous".
	 * If there is already an instance connected to this instance through role "previous", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param previous the instance to connect
	 */
	public void setPrevious(GUID previous) {
		store.setLink(this.id,  rolePreviousId, previous);
	}
	/**
	 * Connects this instance to the given instance using role "previous".
	 * If there is already an instance connected to this instance through role "previous", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param previous the instance to connect
	 */
	public void setPrevious(PDDiagram previous) 
{		setPrevious(previous.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role next.
	 * @return the connected instance
	 */
	public PDDiagram getNext(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleNextId);
	 	return (PDDiagram) store.load(PDDiagram.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role next.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagram> getNexts(){
		return (Collection<PDDiagram>)(Object)store.getAndLoadInstances(this.id, roleNextId, PDDiagram.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "next".
	 * If the given instance is null, nothing happens.
	 * @param next the instance to connect
	 */
	public void addNext(GUID next){
		if (next != null) {
			store.addLink(this.id, roleNextId, next);
		}
	}
	/**
	 * Connects this instance to the given instance using role "next".
	 * If the given instance is null, nothing happens.
	 * @param next the instance to connect
	 */
	public void addNext(PDDiagram next){
		if (next != null)
			addNext(next.getId());
	}

	/**
	 * Connects this instance to the given instance using role "next".
	 * If the given collection of instances is null, nothing happens.
	 * @param next the Collection of instances to connect
	 */
	public void addNexts(Collection<PDDiagram> nexts){
		if (nexts == null)
			return;
		for (PDDiagram instance : nexts)
			addNext(instance);
	}

	/**
	 * Removes the link from this instance through role "next".
	 */
	public void removeNext(){
		store.removeLink(this.id, roleNextId,
			store.getInstance(this.id, roleNextId));
	}

	/**
	 * Removes the link from this instance through role "next" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNext(PDDiagram next){
		if (next == null)
			return;
		store.removeLink(this.id, roleNextId, next.getId());
	}

	/**
	 * Removes the links from this instance through role "next" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNexts(Collection<PDDiagram> nexts){
		if (nexts == null)
			return;
		for (PDDiagram instance : nexts)
			store.removeLink(this.id, roleNextId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "next".
	 * If there is already an instance connected to this instance through role "next", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param next the instance to connect
	 */
	public void setNext(GUID next) {
		store.setLink(this.id,  roleNextId, next);
	}
	/**
	 * Connects this instance to the given instance using role "next".
	 * If there is already an instance connected to this instance through role "next", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param next the instance to connect
	 */
	public void setNext(PDDiagram next) 
{		setNext(next.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role Shape.
	 * @return the connected instance
	 */
	public PDShape getShape(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleShapeId);
	 	return (PDShape) store.load(PDShape.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Shape.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getShapes(){
		return (Collection<PDShape>)(Object)store.getAndLoadInstances(this.id, roleShapeId, PDShape.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Shape".
	 * If the given instance is null, nothing happens.
	 * @param shape the instance to connect
	 */
	public void addShape(GUID shape){
		if (shape != null) {
			store.addLink(this.id, roleShapeId, shape);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If the given instance is null, nothing happens.
	 * @param shape the instance to connect
	 */
	public void addShape(PDShape shape){
		if (shape != null)
			addShape(shape.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If the given collection of instances is null, nothing happens.
	 * @param shape the Collection of instances to connect
	 */
	public void addShapes(Collection<PDShape> shapes){
		if (shapes == null)
			return;
		for (PDShape instance : shapes)
			addShape(instance);
	}

	/**
	 * Removes the link from this instance through role "Shape".
	 */
	public void removeShape(){
		store.removeLink(this.id, roleShapeId,
			store.getInstance(this.id, roleShapeId));
	}

	/**
	 * Removes the link from this instance through role "Shape" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShape(PDShape shape){
		if (shape == null)
			return;
		store.removeLink(this.id, roleShapeId, shape.getId());
	}

	/**
	 * Removes the links from this instance through role "Shape" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeShapes(Collection<PDShape> shapes){
		if (shapes == null)
			return;
		for (PDShape instance : shapes)
			store.removeLink(this.id, roleShapeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If there is already an instance connected to this instance through role "Shape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shape the instance to connect
	 */
	public void setShape(GUID shape) {
		store.setLink(this.id,  roleShapeId, shape);
	}
	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If there is already an instance connected to this instance through role "Shape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shape the instance to connect
	 */
	public void setShape(PDShape shape) 
{		setShape(shape.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role Operation Application.
	 * @return the connected instance
	 */
	public PDOperationApplication getOperationApplication(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOperationApplicationId);
	 	return (PDOperationApplication) store.load(PDOperationApplication.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Operation Application.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperationApplication> getOperationApplications(){
		return (Collection<PDOperationApplication>)(Object)store.getAndLoadInstances(this.id, roleOperationApplicationId, PDOperationApplication.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given instance is null, nothing happens.
	 * @param operationApplication the instance to connect
	 */
	public void addOperationApplication(GUID operationApplication){
		if (operationApplication != null) {
			store.addLink(this.id, roleOperationApplicationId, operationApplication);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given instance is null, nothing happens.
	 * @param operationApplication the instance to connect
	 */
	public void addOperationApplication(PDOperationApplication operationApplication){
		if (operationApplication != null)
			addOperationApplication(operationApplication.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given collection of instances is null, nothing happens.
	 * @param operationApplication the Collection of instances to connect
	 */
	public void addOperationApplications(Collection<PDOperationApplication> operationApplications){
		if (operationApplications == null)
			return;
		for (PDOperationApplication instance : operationApplications)
			addOperationApplication(instance);
	}

	/**
	 * Removes the link from this instance through role "Operation Application".
	 */
	public void removeOperationApplication(){
		store.removeLink(this.id, roleOperationApplicationId,
			store.getInstance(this.id, roleOperationApplicationId));
	}

	/**
	 * Removes the link from this instance through role "Operation Application" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperationApplication(PDOperationApplication operationApplication){
		if (operationApplication == null)
			return;
		store.removeLink(this.id, roleOperationApplicationId, operationApplication.getId());
	}

	/**
	 * Removes the links from this instance through role "Operation Application" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOperationApplications(Collection<PDOperationApplication> operationApplications){
		if (operationApplications == null)
			return;
		for (PDOperationApplication instance : operationApplications)
			store.removeLink(this.id, roleOperationApplicationId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If there is already an instance connected to this instance through role "Operation Application", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationApplication the instance to connect
	 */
	public void setOperationApplication(GUID operationApplication) {
		store.setLink(this.id,  roleOperationApplicationId, operationApplication);
	}
	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If there is already an instance connected to this instance through role "Operation Application", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationApplication the instance to connect
	 */
	public void setOperationApplication(PDOperationApplication operationApplication) 
{		setOperationApplication(operationApplication.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role DiagramSet.
	 * @return the connected instance
	 */
	public PDDiagramSet getDiagramSet(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleDiagramSetId);
	 	return (PDDiagramSet) store.load(PDDiagramSet.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role DiagramSet.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagramSet> getDiagramSets(){
		return (Collection<PDDiagramSet>)(Object)store.getAndLoadInstances(this.id, roleDiagramSetId, PDDiagramSet.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If the given instance is null, nothing happens.
	 * @param diagramSet the instance to connect
	 */
	public void addDiagramSet(GUID diagramSet){
		if (diagramSet != null) {
			store.addLink(this.id, roleDiagramSetId, diagramSet);
		}
	}
	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If the given instance is null, nothing happens.
	 * @param diagramSet the instance to connect
	 */
	public void addDiagramSet(PDDiagramSet diagramSet){
		if (diagramSet != null)
			addDiagramSet(diagramSet.getId());
	}

	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If the given collection of instances is null, nothing happens.
	 * @param diagramSet the Collection of instances to connect
	 */
	public void addDiagramSets(Collection<PDDiagramSet> diagramSets){
		if (diagramSets == null)
			return;
		for (PDDiagramSet instance : diagramSets)
			addDiagramSet(instance);
	}

	/**
	 * Removes the link from this instance through role "DiagramSet".
	 */
	public void removeDiagramSet(){
		store.removeLink(this.id, roleDiagramSetId,
			store.getInstance(this.id, roleDiagramSetId));
	}

	/**
	 * Removes the link from this instance through role "DiagramSet" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDiagramSet(PDDiagramSet diagramSet){
		if (diagramSet == null)
			return;
		store.removeLink(this.id, roleDiagramSetId, diagramSet.getId());
	}

	/**
	 * Removes the links from this instance through role "DiagramSet" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeDiagramSets(Collection<PDDiagramSet> diagramSets){
		if (diagramSets == null)
			return;
		for (PDDiagramSet instance : diagramSets)
			store.removeLink(this.id, roleDiagramSetId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If there is already an instance connected to this instance through role "DiagramSet", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagramSet the instance to connect
	 */
	public void setDiagramSet(GUID diagramSet) {
		store.setLink(this.id,  roleDiagramSetId, diagramSet);
	}
	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If there is already an instance connected to this instance through role "DiagramSet", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagramSet the instance to connect
	 */
	public void setDiagramSet(PDDiagramSet diagramSet) 
{		setDiagramSet(diagramSet.getId());
	}
}
