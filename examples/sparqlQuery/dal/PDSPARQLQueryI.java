package sparqlQuery.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "SPARQLQuery" in memory.
 * @author PDGen
 */
public interface PDSPARQLQueryI{

	/**
	 * Returns the instance connected to this instance through the role subquery.
	 * @return the connected instance
	 */
	public PDSPARQLQuery getSubquery();

	/**
	 * Returns the instance(s) connected to this instance through the role subquery.
	 * @return the connected instance(s)
	 */
	 public Collection<PDSPARQLQuery> getSubquerys();

   /**
	 * Connects this instance to the given instance using role "subquery".
	 * If the given instance is null, nothing happens.
	 * @param subquery the instance to connect
	 */
	public void addSubquery(GUID subquery);
	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If the given instance is null, nothing happens.
	 * @param subquery the instance to connect
	 */
	public void addSubquery(PDSPARQLQuery subquery);

	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If the given collection of instances is null, nothing happens.
	 * @param subquery the Collection of instances to connect
	 */
	public void addSubquerys(Collection<PDSPARQLQuery> subquerys);

	/**
	 * Removes the link from this instance through role "subquery".
	 */
	public void removeSubquery();

	/**
	 * Removes the link from this instance through role "subquery" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSubquery(PDSPARQLQuery subquery);

	/**
	 * Removes the links from this instance through role "subquery" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSubquerys(Collection<PDSPARQLQuery> subquerys);

	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If there is already an instance connected to this instance through role "subquery", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subquery the instance to connect
	 */
	public void setSubquery(GUID subquery) ;
	/**
	 * Connects this instance to the given instance using role "subquery".
	 * If there is already an instance connected to this instance through role "subquery", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subquery the instance to connect
	 */
	public void setSubquery(PDSPARQLQuery subquery) 
;	/**
	 * Returns the instance connected to this instance through the role filter.
	 * @return the connected instance
	 */
	public PDChange getFilter();

	/**
	 * Returns the instance(s) connected to this instance through the role filter.
	 * @return the connected instance(s)
	 */
	 public Collection<PDChange> getFilters();

   /**
	 * Connects this instance to the given instance using role "filter".
	 * If the given instance is null, nothing happens.
	 * @param filter the instance to connect
	 */
	public void addFilter(GUID filter);
	/**
	 * Connects this instance to the given instance using role "filter".
	 * If the given instance is null, nothing happens.
	 * @param filter the instance to connect
	 */
	public void addFilter(PDChange filter);

	/**
	 * Connects this instance to the given instance using role "filter".
	 * If the given collection of instances is null, nothing happens.
	 * @param filter the Collection of instances to connect
	 */
	public void addFilters(Collection<PDChange> filters);

	/**
	 * Removes the link from this instance through role "filter".
	 */
	public void removeFilter();

	/**
	 * Removes the link from this instance through role "filter" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeFilter(PDChange filter);

	/**
	 * Removes the links from this instance through role "filter" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeFilters(Collection<PDChange> filters);

	/**
	 * Connects this instance to the given instance using role "filter".
	 * If there is already an instance connected to this instance through role "filter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param filter the instance to connect
	 */
	public void setFilter(GUID filter) ;
	/**
	 * Connects this instance to the given instance using role "filter".
	 * If there is already an instance connected to this instance through role "filter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param filter the instance to connect
	 */
	public void setFilter(PDChange filter) 
;	/**
	 * Returns the instance connected to this instance through the role where.
	 * @return the connected instance
	 */
	public PDChange getWhere();

	/**
	 * Returns the instance(s) connected to this instance through the role where.
	 * @return the connected instance(s)
	 */
	 public Collection<PDChange> getWheres();

   /**
	 * Connects this instance to the given instance using role "where".
	 * If the given instance is null, nothing happens.
	 * @param where the instance to connect
	 */
	public void addWhere(GUID where);
	/**
	 * Connects this instance to the given instance using role "where".
	 * If the given instance is null, nothing happens.
	 * @param where the instance to connect
	 */
	public void addWhere(PDChange where);

	/**
	 * Connects this instance to the given instance using role "where".
	 * If the given collection of instances is null, nothing happens.
	 * @param where the Collection of instances to connect
	 */
	public void addWheres(Collection<PDChange> wheres);

	/**
	 * Removes the link from this instance through role "where".
	 */
	public void removeWhere();

	/**
	 * Removes the link from this instance through role "where" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeWhere(PDChange where);

	/**
	 * Removes the links from this instance through role "where" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeWheres(Collection<PDChange> wheres);

	/**
	 * Connects this instance to the given instance using role "where".
	 * If there is already an instance connected to this instance through role "where", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param where the instance to connect
	 */
	public void setWhere(GUID where) ;
	/**
	 * Connects this instance to the given instance using role "where".
	 * If there is already an instance connected to this instance through role "where", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param where the instance to connect
	 */
	public void setWhere(PDChange where) 
;}
