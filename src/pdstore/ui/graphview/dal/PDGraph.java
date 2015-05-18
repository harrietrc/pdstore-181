package pdstore.ui.graphview.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Graph" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.ui.graphview.dal.PDGraph");
 * @author PDGen
 */
public class PDGraph implements PDInstance,PDGraphI{

	public static final GUID typeId = new GUID("e44c1e501d9211e1a04700235411d565"); 

	public static final GUID roleJavaPackageId = new GUID("e44c1e561d9211e1a04700235411d565");
	public static final GUID roleNodeId = new GUID("e44c1e521d9211e1a04700235411d565");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDGraph.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDGraph:" + name;
		else
			return "PDGraph:" + id;
	}

	/**
	 * Creates an PDGraph object representing a PDStore instance of type Graph.
	 * @param store the store the instance should be in
	 */
	public PDGraph(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDGraph object representing the instance with the given ID. 
	 * The ID must be of an instance of type Graph.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDGraph(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Graph with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDGraph load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDGraph)instance;
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
	 * Returns the instance connected to this instance through the role Java package.
	 * @return the connected instance
	 */
	public String getJavaPackage(){
	 	return (String)store.getInstance(this.id, roleJavaPackageId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Java package.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getJavaPackages(){
		return (Collection<String>)(Object)store.getInstances(this.id, roleJavaPackageId);
	 }

   /**
	 * Connects this instance to the given instance using role "Java package".
	 * If the given instance is null, nothing happens.
	 * @param javaPackage the instance to connect
	 */
	public void addJavaPackage(String javaPackage){
		if (javaPackage != null) {
			store.addLink(this.id, roleJavaPackageId, javaPackage);
		}
	}
	/**
	 * Connects this instance to the given instances using role "Java package".
	 * If the given collection of instances is null, nothing happens.
	 * @param javaPackage the Collection of instances to connect
	 */
	public void addJavaPackages(Collection<String> javaPackages){
		if (javaPackages == null)
			return;
		for (String instance : javaPackages)
			addJavaPackage(instance);
	}

	/**
	 * Removes the link from this instance through role "Java package".
	 */
	public void removeJavaPackage(){
		store.removeLink(this.id, roleJavaPackageId,
			store.getInstance(this.id, roleJavaPackageId));
	}

	/**
	 * Removes the link from this instance through role "Java package" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeJavaPackage(String javaPackage){
		if (javaPackage == null)
			return;
		store.removeLink(this.id, roleJavaPackageId, javaPackage);
	}

	/**
	 * Connects this instance to the given instance using role "Java package".
	 * If there is already an instance connected to this instance through role "Java package", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param javaPackage the instance to connect
	 */
	public void setJavaPackage(String javaPackage) {
		store.setLink(this.id,  roleJavaPackageId, javaPackage);
	}
	/**
	 * Returns the instance connected to this instance through the role Node.
	 * @return the connected instance
	 */
	public PDNode getNode(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleNodeId);
	 	return (PDNode) store.load(PDNode.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Node.
	 * @return the connected instance(s)
	 */
	 public Collection<PDNode> getNodes(){
		return (Collection<PDNode>)(Object)store.getAndLoadInstances(this.id, roleNodeId, PDNode.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Node".
	 * If the given instance is null, nothing happens.
	 * @param node the instance to connect
	 */
	public void addNode(GUID node){
		if (node != null) {
			store.addLink(this.id, roleNodeId, node);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Node".
	 * If the given instance is null, nothing happens.
	 * @param node the instance to connect
	 */
	public void addNode(PDNode node){
		if (node != null)
			addNode(node.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Node".
	 * If the given collection of instances is null, nothing happens.
	 * @param node the Collection of instances to connect
	 */
	public void addNodes(Collection<PDNode> nodes){
		if (nodes == null)
			return;
		for (PDNode instance : nodes)
			addNode(instance);
	}

	/**
	 * Removes the link from this instance through role "Node".
	 */
	public void removeNode(){
		store.removeLink(this.id, roleNodeId,
			store.getInstance(this.id, roleNodeId));
	}

	/**
	 * Removes the link from this instance through role "Node" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNode(PDNode node){
		if (node == null)
			return;
		store.removeLink(this.id, roleNodeId, node.getId());
	}

	/**
	 * Removes the links from this instance through role "Node" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNodes(Collection<PDNode> nodes){
		if (nodes == null)
			return;
		for (PDNode instance : nodes)
			store.removeLink(this.id, roleNodeId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Node".
	 * If there is already an instance connected to this instance through role "Node", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param node the instance to connect
	 */
	public void setNode(GUID node) {
		store.setLink(this.id,  roleNodeId, node);
	}
	/**
	 * Connects this instance to the given instance using role "Node".
	 * If there is already an instance connected to this instance through role "Node", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param node the instance to connect
	 */
	public void setNode(PDNode node) 
{		setNode(node.getId());
	}
}
