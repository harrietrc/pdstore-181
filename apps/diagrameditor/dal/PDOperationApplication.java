package diagrameditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "OperationApplication" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("diagrameditor.dal.PDOperationApplication");
 * @author PDGen
 */
public class PDOperationApplication implements PDInstance,PDOperationApplicationI{

	public static final GUID typeId = new GUID("7420c751980111e1b62d1c6f65cd0117"); 

	public static final GUID roleSuperParameterId = new GUID("920645160d6411e0b45a1cc1dec00ed3");
	public static final GUID rolePreviousId = new GUID("7e8d67ca4f4c11e0a5c4842b2b9af4fd");
	public static final GUID roleNextId = new GUID("7e8d67ca4f4c11e0a5d4842b2b9af4fd");
	public static final GUID roleOperationId = new GUID("7420c752980111e1b62d1c6f65cd0117");
	public static final GUID roleDiagramId = new GUID("920645130d6411e0b44a1cc1dec00ed3");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDOperationApplication.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDOperationApplication:" + name;
		else
			return "PDOperationApplication:" + id;
	}

	/**
	 * Creates an PDOperationApplication object representing a PDStore instance of type OperationApplication.
	 * @param store the store the instance should be in
	 */
	public PDOperationApplication(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDOperationApplication object representing the instance with the given ID. 
	 * The ID must be of an instance of type OperationApplication.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDOperationApplication(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type OperationApplication with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDOperationApplication load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDOperationApplication)instance;
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
	 * Returns the instance connected to this instance through the role superParameter.
	 * @return the connected instance
	 */
	public PDInstance getSuperParameter(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleSuperParameterId);
	 	return (PDInstance) store.load(instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role superParameter.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getSuperParameters(){
		return (Collection<PDInstance>)(Object)store.getAndLoadInstances(this.id, roleSuperParameterId);
	 }

   /**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given instance is null, nothing happens.
	 * @param superParameter the instance to connect
	 */
	public void addSuperParameter(GUID superParameter){
		if (superParameter != null) {
			store.addLink(this.id, roleSuperParameterId, superParameter);
		}
	}
	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given instance is null, nothing happens.
	 * @param superParameter the instance to connect
	 */
	public void addSuperParameter(PDInstance superParameter){
		if (superParameter != null)
			addSuperParameter(superParameter.getId());
	}

	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given collection of instances is null, nothing happens.
	 * @param superParameter the Collection of instances to connect
	 */
	public void addSuperParameters(Collection<PDInstance> superParameters){
		if (superParameters == null)
			return;
		for (PDInstance instance : superParameters)
			addSuperParameter(instance);
	}

	/**
	 * Removes the link from this instance through role "superParameter".
	 */
	public void removeSuperParameter(){
		store.removeLink(this.id, roleSuperParameterId,
			store.getInstance(this.id, roleSuperParameterId));
	}

	/**
	 * Removes the link from this instance through role "superParameter" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSuperParameter(PDInstance superParameter){
		if (superParameter == null)
			return;
		store.removeLink(this.id, roleSuperParameterId, superParameter.getId());
	}

	/**
	 * Removes the links from this instance through role "superParameter" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSuperParameters(Collection<PDInstance> superParameters){
		if (superParameters == null)
			return;
		for (PDInstance instance : superParameters)
			store.removeLink(this.id, roleSuperParameterId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If there is already an instance connected to this instance through role "superParameter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param superParameter the instance to connect
	 */
	public void setSuperParameter(GUID superParameter) {
		store.setLink(this.id,  roleSuperParameterId, superParameter);
	}
	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If there is already an instance connected to this instance through role "superParameter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param superParameter the instance to connect
	 */
	public void setSuperParameter(PDInstance superParameter) 
{		setSuperParameter(superParameter.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role previous.
	 * @return the connected instance
	 */
	public PDOperationApplication getPrevious(){
		GUID instanceId = (GUID) store.getInstance(this.id, rolePreviousId);
	 	return (PDOperationApplication) store.load(PDOperationApplication.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role previous.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperationApplication> getPreviouss(){
		return (Collection<PDOperationApplication>)(Object)store.getAndLoadInstances(this.id, rolePreviousId, PDOperationApplication.typeId);
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
	public void addPrevious(PDOperationApplication previous){
		if (previous != null)
			addPrevious(previous.getId());
	}

	/**
	 * Connects this instance to the given instance using role "previous".
	 * If the given collection of instances is null, nothing happens.
	 * @param previous the Collection of instances to connect
	 */
	public void addPreviouss(Collection<PDOperationApplication> previouss){
		if (previouss == null)
			return;
		for (PDOperationApplication instance : previouss)
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
	public void removePrevious(PDOperationApplication previous){
		if (previous == null)
			return;
		store.removeLink(this.id, rolePreviousId, previous.getId());
	}

	/**
	 * Removes the links from this instance through role "previous" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removePreviouss(Collection<PDOperationApplication> previouss){
		if (previouss == null)
			return;
		for (PDOperationApplication instance : previouss)
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
	public void setPrevious(PDOperationApplication previous) 
{		setPrevious(previous.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role next.
	 * @return the connected instance
	 */
	public PDOperationApplication getNext(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleNextId);
	 	return (PDOperationApplication) store.load(PDOperationApplication.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role next.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperationApplication> getNexts(){
		return (Collection<PDOperationApplication>)(Object)store.getAndLoadInstances(this.id, roleNextId, PDOperationApplication.typeId);
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
	public void addNext(PDOperationApplication next){
		if (next != null)
			addNext(next.getId());
	}

	/**
	 * Connects this instance to the given instance using role "next".
	 * If the given collection of instances is null, nothing happens.
	 * @param next the Collection of instances to connect
	 */
	public void addNexts(Collection<PDOperationApplication> nexts){
		if (nexts == null)
			return;
		for (PDOperationApplication instance : nexts)
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
	public void removeNext(PDOperationApplication next){
		if (next == null)
			return;
		store.removeLink(this.id, roleNextId, next.getId());
	}

	/**
	 * Removes the links from this instance through role "next" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNexts(Collection<PDOperationApplication> nexts){
		if (nexts == null)
			return;
		for (PDOperationApplication instance : nexts)
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
	public void setNext(PDOperationApplication next) 
{		setNext(next.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role Operation.
	 * @return the connected instance
	 */
	public PDOperation getOperation(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleOperationId);
	 	return (PDOperation) store.load(PDOperation.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Operation.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperation> getOperations(){
		return (Collection<PDOperation>)(Object)store.getAndLoadInstances(this.id, roleOperationId, PDOperation.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 */
	public void addOperation(GUID operation){
		if (operation != null) {
			store.addLink(this.id, roleOperationId, operation);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 */
	public void addOperation(PDOperation operation){
		if (operation != null)
			addOperation(operation.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given collection of instances is null, nothing happens.
	 * @param operation the Collection of instances to connect
	 */
	public void addOperations(Collection<PDOperation> operations){
		if (operations == null)
			return;
		for (PDOperation instance : operations)
			addOperation(instance);
	}

	/**
	 * Removes the link from this instance through role "Operation".
	 */
	public void removeOperation(){
		store.removeLink(this.id, roleOperationId,
			store.getInstance(this.id, roleOperationId));
	}

	/**
	 * Removes the link from this instance through role "Operation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperation(PDOperation operation){
		if (operation == null)
			return;
		store.removeLink(this.id, roleOperationId, operation.getId());
	}

	/**
	 * Removes the links from this instance through role "Operation" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOperations(Collection<PDOperation> operations){
		if (operations == null)
			return;
		for (PDOperation instance : operations)
			store.removeLink(this.id, roleOperationId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operation the instance to connect
	 */
	public void setOperation(GUID operation) {
		store.setLink(this.id,  roleOperationId, operation);
	}
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operation the instance to connect
	 */
	public void setOperation(PDOperation operation) 
{		setOperation(operation.getId());
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
