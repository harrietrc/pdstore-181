package sparqlQuery.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;
import pdstore.dal.PDChange;

/**
 * Data access class to represent instances of type "SPARQLQuery" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("sparqlQuery.dal.PDSPARQLQuery");
 * @author PDGen
 */
public class PDSPARQLQuery implements PDInstance,PDSPARQLQueryI{

	public static final GUID typeId = new GUID("43fdb564e1bb11e1bf76020054554e01"); 

	public static final GUID roleSubqueryId = new GUID("58d680c6e27111e1ac55020054554e01");
	public static final GUID roleFilterId = new GUID("43fdb565e1bb11e1bf76020054554e01");
	public static final GUID roleWhereId = new GUID("43fdb560e1bb11e1bf76020054554e01");

	static {
		register();
	}

	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		PDStore.addDataClass(typeId, PDSPARQLQuery.class);
	}

	private PDStore store;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDSPARQLQuery:" + name;
		else
			return "PDSPARQLQuery:" + id;
	}

	/**
	 * Creates an PDSPARQLQuery object representing a PDStore instance of type SPARQLQuery.
	 * @param store the store the instance should be in
	 */
	public PDSPARQLQuery(PDStore store) {
		this(store, new GUID());
	}

	/**
	 * Creates an PDSPARQLQuery object representing the instance with the given ID. 
	 * The ID must be of an instance of type SPARQLQuery.
	 * @param store the store the instance should be in
	 * @param id GUID of the instance
	 */
	public PDSPARQLQuery(PDStore store, GUID id) {
		this.store = store;
		this.id = id;
		// set the has-type link for this instance
		store.setType(id, typeId);
	}

	/**
	 * Loads an object for the instance of PDStore type SPARQLQuery with the given ID.
	 * If an object for the instance is already available in the given PDStore object, it is returned.
	 * @param store store to load the instance into
	 * @param id GUID of the instance
	 */
	public static PDSPARQLQuery load(PDStore store, GUID id) {
		PDInstance instance = store.load(typeId, id);
		return (PDSPARQLQuery)instance;
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
	 * Returns the instance connected to this instance through the role subquery.
	 * @return the connected instance
	 */
	public PDSPARQLQuery getSubquery(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleSubqueryId);
	 	return (PDSPARQLQuery) store.load(PDSPARQLQuery.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role subquery.
	 * @return the connected instance(s)
	 */
	 public Collection<PDSPARQLQuery> getSubquerys(){
		return (Collection<PDSPARQLQuery>)(Object)store.getAndLoadInstances(this.id, roleSubqueryId, PDSPARQLQuery.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "subquery".
	 * If the given instance is null, nothing happens.
	 * @param subquery the instance to connect
	 */
	public void addSubquery(GUID subquery){
		if (subquery != null) {
			store.addLink(this.id, roleSubqueryId, subquery);
		}
	}
	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If the given instance is null, nothing happens.
	 * @param subquery the instance to connect
	 */
	public void addSubquery(PDSPARQLQuery subquery){
		if (subquery != null)
			addSubquery(subquery.getId());
	}

	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If the given collection of instances is null, nothing happens.
	 * @param subquery the Collection of instances to connect
	 */
	public void addSubquerys(Collection<PDSPARQLQuery> subquerys){
		if (subquerys == null)
			return;
		for (PDSPARQLQuery instance : subquerys)
			addSubquery(instance);
	}

	/**
	 * Removes the link from this instance through role "subquery".
	 */
	public void removeSubquery(){
		store.removeLink(this.id, roleSubqueryId,
			store.getInstance(this.id, roleSubqueryId));
	}

	/**
	 * Removes the link from this instance through role "subquery" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSubquery(PDSPARQLQuery subquery){
		if (subquery == null)
			return;
		store.removeLink(this.id, roleSubqueryId, subquery.getId());
	}

	/**
	 * Removes the links from this instance through role "subquery" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSubquerys(Collection<PDSPARQLQuery> subquerys){
		if (subquerys == null)
			return;
		for (PDSPARQLQuery instance : subquerys)
			store.removeLink(this.id, roleSubqueryId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If there is already an instance connected to this instance through role "subquery", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subquery the instance to connect
	 */
	public void setSubquery(GUID subquery) {
		store.setLink(this.id,  roleSubqueryId, subquery);
	}
	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If there is already an instance connected to this instance through role "subquery", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subquery the instance to connect
	 */
	public void setSubquery(PDSPARQLQuery subquery) 
{		setSubquery(subquery.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role filter.
	 * @return the connected instance
	 */
	public PDChange getFilter(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleFilterId);
	 	return (PDChange) store.load(PDChange.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role filter.
	 * @return the connected instance(s)
	 */
	 public Collection<PDChange> getFilters(){
		return (Collection<PDChange>)(Object)store.getAndLoadInstances(this.id, roleFilterId, PDChange.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "filter".
	 * If the given instance is null, nothing happens.
	 * @param filter the instance to connect
	 */
	public void addFilter(GUID filter){
		if (filter != null) {
			store.addLink(this.id, roleFilterId, filter);
		}
	}
	/**
	 * Connects this instance to the given instance using role "filter".
	 * If the given instance is null, nothing happens.
	 * @param filter the instance to connect
	 */
	public void addFilter(PDChange filter){
		if (filter != null)
			addFilter(filter.getId());
	}

	/**
	 * Connects this instance to the given instance using role "filter".
	 * If the given collection of instances is null, nothing happens.
	 * @param filter the Collection of instances to connect
	 */
	public void addFilters(Collection<PDChange> filters){
		if (filters == null)
			return;
		for (PDChange instance : filters)
			addFilter(instance);
	}

	/**
	 * Removes the link from this instance through role "filter".
	 */
	public void removeFilter(){
		store.removeLink(this.id, roleFilterId,
			store.getInstance(this.id, roleFilterId));
	}

	/**
	 * Removes the link from this instance through role "filter" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeFilter(PDChange filter){
		if (filter == null)
			return;
		store.removeLink(this.id, roleFilterId, filter.getId());
	}

	/**
	 * Removes the links from this instance through role "filter" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeFilters(Collection<PDChange> filters){
		if (filters == null)
			return;
		for (PDChange instance : filters)
			store.removeLink(this.id, roleFilterId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "filter".
	 * If there is already an instance connected to this instance through role "filter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param filter the instance to connect
	 */
	public void setFilter(GUID filter) {
		store.setLink(this.id,  roleFilterId, filter);
	}
	/**
	 * Connects this instance to the given instance using role "filter".
	 * If there is already an instance connected to this instance through role "filter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param filter the instance to connect
	 */
	public void setFilter(PDChange filter) 
{		setFilter(filter.getId());
	}
	/**
	 * Returns the instance connected to this instance through the role where.
	 * @return the connected instance
	 */
	public PDChange getWhere(){
		GUID instanceId = (GUID) store.getInstance(this.id, roleWhereId);
	 	return (PDChange) store.load(PDChange.typeId, instanceId);
	}

	/**
	 * Returns the instance(s) connected to this instance through the role where.
	 * @return the connected instance(s)
	 */
	 public Collection<PDChange> getWheres(){
		return (Collection<PDChange>)(Object)store.getAndLoadInstances(this.id, roleWhereId, PDChange.typeId);
	 }

   /**
	 * Connects this instance to the given instance using role "where".
	 * If the given instance is null, nothing happens.
	 * @param where the instance to connect
	 */
	public void addWhere(GUID where){
		if (where != null) {
			store.addLink(this.id, roleWhereId, where);
		}
	}
	/**
	 * Connects this instance to the given instance using role "where".
	 * If the given instance is null, nothing happens.
	 * @param where the instance to connect
	 */
	public void addWhere(PDChange where){
		if (where != null)
			addWhere(where.getId());
	}

	/**
	 * Connects this instance to the given instance using role "where".
	 * If the given collection of instances is null, nothing happens.
	 * @param where the Collection of instances to connect
	 */
	public void addWheres(Collection<PDChange> wheres){
		if (wheres == null)
			return;
		for (PDChange instance : wheres)
			addWhere(instance);
	}

	/**
	 * Removes the link from this instance through role "where".
	 */
	public void removeWhere(){
		store.removeLink(this.id, roleWhereId,
			store.getInstance(this.id, roleWhereId));
	}

	/**
	 * Removes the link from this instance through role "where" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeWhere(PDChange where){
		if (where == null)
			return;
		store.removeLink(this.id, roleWhereId, where.getId());
	}

	/**
	 * Removes the links from this instance through role "where" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeWheres(Collection<PDChange> wheres){
		if (wheres == null)
			return;
		for (PDChange instance : wheres)
			store.removeLink(this.id, roleWhereId, instance.getId());
	}

	/**
	 * Connects this instance to the given instance using role "where".
	 * If there is already an instance connected to this instance through role "where", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param where the instance to connect
	 */
	public void setWhere(GUID where) {
		store.setLink(this.id,  roleWhereId, where);
	}
	/**
	 * Connects this instance to the given instance using role "where".
	 * If there is already an instance connected to this instance through role "where", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param where the instance to connect
	 */
	public void setWhere(PDChange where) 
{		setWhere(where.getId());
	}
}
