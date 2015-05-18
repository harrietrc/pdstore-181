package pdstore.ui.graphview.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Node" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("pdstore.ui.graphview.dal.PDNode");
 * @author PDGen
 */
public class PDNode implements PDInstance,PDNodeI{

	public static final GUID typeId = new GUID("e44c1e511d9211e1a04700235411d565"); 

	public static final GUID roleYId = new GUID("e44c1e551d9211e1a04700235411d565");
	public static final GUID roleXId = new GUID("e44c1e541d9211e1a04700235411d565");
	public static final GUID roleShownInstanceId = new GUID("e44c1e531d9211e1a04700235411d565");
	public static final GUID roleGraphId = new GUID("e44c1e521d9211e1a05700235411d565");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDNode.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDNode:" + name;
		else
			return "PDNode:" + id;
	}

	/**
	 * Creates an PDNode object representing a PDStore instance of type Node.
	 * @param store the store the instance should be in
	 */
	public PDNode(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDNode object representing the instance with the given ID. 
	 * The ID must be of an instance of type Node.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDNode(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type Node with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDNode load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDNode)instance;
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
	 * Returns the instance connected to this instance through the role y.
	 * @return the connected instance
	 */
	public Double getY(){
	 	return (Double)store.getInstance(this.id, roleYId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role y.
	 * @return the connected instance(s)
	 */
	 public Collection<Double> getYs(){
		return (Collection<Double>)(Object)store.getInstances(this.id, roleYId);
	 }

   /**
	 * Connects this instance to the given instance using role "y".
	 * If the given instance is null, nothing happens.
	 * @param y the instance to connect
	 */
	public void addY(Double y){
		if (y != null) {
			store.addLink(this.id, roleYId, y);
		}
	}
	/**
	 * Connects this instance to the given instances using role "y".
	 * If the given collection of instances is null, nothing happens.
	 * @param y the Collection of instances to connect
	 */
	public void addYs(Collection<Double> ys){
		if (ys == null)
			return;
		for (Double instance : ys)
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
	public void removeY(Double y){
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
	public void setY(Double y) {
		store.setLink(this.id,  roleYId, y);
	}
	/**
	 * Returns the instance connected to this instance through the role x.
	 * @return the connected instance
	 */
	public Double getX(){
	 	return (Double)store.getInstance(this.id, roleXId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role x.
	 * @return the connected instance(s)
	 */
	 public Collection<Double> getXs(){
		return (Collection<Double>)(Object)store.getInstances(this.id, roleXId);
	 }

   /**
	 * Connects this instance to the given instance using role "x".
	 * If the given instance is null, nothing happens.
	 * @param x the instance to connect
	 */
	public void addX(Double x){
		if (x != null) {
			store.addLink(this.id, roleXId, x);
		}
	}
	/**
	 * Connects this instance to the given instances using role "x".
	 * If the given collection of instances is null, nothing happens.
	 * @param x the Collection of instances to connect
	 */
	public void addXs(Collection<Double> xs){
		if (xs == null)
			return;
		for (Double instance : xs)
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
	public void removeX(Double x){
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
	public void setX(Double x) {
		store.setLink(this.id,  roleXId, x);
	}
	/**
	 * Returns the instance connected to this instance through the role shown instance.
	 * @return the connected instance
	 */
	public Object getShownInstance(){
	 	return (Object)store.getInstance(this.id, roleShownInstanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role shown instance.
	 * @return the connected instance(s)
	 */
	 public Collection<Object> getShownInstances(){
		return (Collection<Object>)(Object)store.getInstances(this.id, roleShownInstanceId);
	 }

   /**
	 * Connects this instance to the given instance using role "shown instance".
	 * If the given instance is null, nothing happens.
	 * @param shownInstance the instance to connect
	 */
	public void addShownInstance(Object shownInstance){
		if (shownInstance != null) {
			store.addLink(this.id, roleShownInstanceId, shownInstance);
		}
	}
	/**
	 * Connects this instance to the given instances using role "shown instance".
	 * If the given collection of instances is null, nothing happens.
	 * @param shownInstance the Collection of instances to connect
	 */
	public void addShownInstances(Collection<Object> shownInstances){
		if (shownInstances == null)
			return;
		for (Object instance : shownInstances)
			addShownInstance(instance);
	}

	/**
	 * Removes the link from this instance through role "shown instance".
	 */
	public void removeShownInstance(){
		store.removeLink(this.id, roleShownInstanceId,
			store.getInstance(this.id, roleShownInstanceId));
	}

	/**
	 * Removes the link from this instance through role "shown instance" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShownInstance(Object shownInstance){
		if (shownInstance == null)
			return;
		store.removeLink(this.id, roleShownInstanceId, shownInstance);
	}

	/**
	 * Connects this instance to the given instance using role "shown instance".
	 * If there is already an instance connected to this instance through role "shown instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shownInstance the instance to connect
	 */
	public void setShownInstance(Object shownInstance) {
		store.setLink(this.id,  roleShownInstanceId, shownInstance);
	}
	/**
	 * Returns the instance connected to this instance through the role Graph.
	 * @return the connected instance
	 */
	public PDGraph getGraph(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleGraphId);
	 	return (PDGraph) store.load(PDGraph.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role Graph.
	 * @return the connected instance(s)
	 */
	 public Collection<PDGraph> getGraphs(){
		return (Collection<PDGraph>)(Object)store.getAndLoadInstances(this.id, roleGraphId, PDGraph.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "Graph".
	 * If the given instance is null, nothing happens.
	 * @param graph the instance to connect
	 */
	public void addGraph(GUID graph){
		if (graph != null) {
			store.addLink(this.id, roleGraphId, graph);
		}
	}
	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If the given instance is null, nothing happens.
	 * @param graph the instance to connect
	 */
	public void addGraph(PDGraph graph){
		if (graph != null)
			addGraph(graph.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If the given collection of instances is null, nothing happens.
	 * @param graph the Collection of instances to connect
	 */
	public void addGraphs(Collection<PDGraph> graphs){
		if (graphs == null)
			return;
		for (PDGraph instance : graphs)
			addGraph(instance);
	}

	/**
	 * Removes the link from this instance through role "Graph".
	 */
	public void removeGraph(){
		store.removeLink(this.id, roleGraphId,
			store.getInstance(this.id, roleGraphId));
	}

	/**
	 * Removes the link from this instance through role "Graph" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeGraph(PDGraph graph){
		if (graph == null)
			return;
		store.removeLink(this.id, roleGraphId, graph.getId());
	}

	/**
	 * Removes the links from this instance through role "Graph" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeGraphs(Collection<PDGraph> graphs){
		if (graphs == null)
			return;
		for (PDGraph instance : graphs)
			store.removeLink(this.id, roleGraphId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If there is already an instance connected to this instance through role "Graph", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param graph the instance to connect
	 */
	public void setGraph(GUID graph) {
		store.setLink(this.id,  roleGraphId, graph);
	}
	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If there is already an instance connected to this instance through role "Graph", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param graph the instance to connect
	 */
	public void setGraph(PDGraph graph) 
{		setGraph(graph.getId());
	}
}
