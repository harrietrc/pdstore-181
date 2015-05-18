package pdstore.ui.graphview.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Node" in memory.
 * @author PDGen
 */
public interface PDNodeI{

	/**
	 * Returns the instance connected to this instance through the role y.
	 * @return the connected instance
	 */
	public Double getY();

	/**
	 * Returns the instance(s) connected to this instance through the role y.
	 * @return the connected instance(s)
	 */
	 public Collection<Double> getYs();

   /**
	 * Connects this instance to the given instance using role "y".
	 * If the given instance is null, nothing happens.
	 * @param y the instance to connect
	 */
	public void addY(Double y);
	/**
	 * Connects this instance to the given instances using role "y".
	 * If the given collection of instances is null, nothing happens.
	 * @param y the Collection of instances to connect
	 */
	public void addYs(Collection<Double> ys);

	/**
	 * Removes the link from this instance through role "y".
	 */
	public void removeY();

	/**
	 * Removes the link from this instance through role "y" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeY(Double y);

	/**
	 * Connects this instance to the given instance using role "y".
	 * If there is already an instance connected to this instance through role "y", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param y the instance to connect
	 */
	public void setY(Double y) ;
	/**
	 * Returns the instance connected to this instance through the role x.
	 * @return the connected instance
	 */
	public Double getX();

	/**
	 * Returns the instance(s) connected to this instance through the role x.
	 * @return the connected instance(s)
	 */
	 public Collection<Double> getXs();

   /**
	 * Connects this instance to the given instance using role "x".
	 * If the given instance is null, nothing happens.
	 * @param x the instance to connect
	 */
	public void addX(Double x);
	/**
	 * Connects this instance to the given instances using role "x".
	 * If the given collection of instances is null, nothing happens.
	 * @param x the Collection of instances to connect
	 */
	public void addXs(Collection<Double> xs);

	/**
	 * Removes the link from this instance through role "x".
	 */
	public void removeX();

	/**
	 * Removes the link from this instance through role "x" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeX(Double x);

	/**
	 * Connects this instance to the given instance using role "x".
	 * If there is already an instance connected to this instance through role "x", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param x the instance to connect
	 */
	public void setX(Double x) ;
	/**
	 * Returns the instance connected to this instance through the role shown instance.
	 * @return the connected instance
	 */
	public Object getShownInstance();

	/**
	 * Returns the instance(s) connected to this instance through the role shown instance.
	 * @return the connected instance(s)
	 */
	 public Collection<Object> getShownInstances();

   /**
	 * Connects this instance to the given instance using role "shown instance".
	 * If the given instance is null, nothing happens.
	 * @param shownInstance the instance to connect
	 */
	public void addShownInstance(Object shownInstance);
	/**
	 * Connects this instance to the given instances using role "shown instance".
	 * If the given collection of instances is null, nothing happens.
	 * @param shownInstance the Collection of instances to connect
	 */
	public void addShownInstances(Collection<Object> shownInstances);

	/**
	 * Removes the link from this instance through role "shown instance".
	 */
	public void removeShownInstance();

	/**
	 * Removes the link from this instance through role "shown instance" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShownInstance(Object shownInstance);

	/**
	 * Connects this instance to the given instance using role "shown instance".
	 * If there is already an instance connected to this instance through role "shown instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shownInstance the instance to connect
	 */
	public void setShownInstance(Object shownInstance) ;
	/**
	 * Returns the instance connected to this instance through the role Graph.
	 * @return the connected instance
	 */
	public PDGraph getGraph();

	/**
	 * Returns the instance(s) connected to this instance through the role Graph.
	 * @return the connected instance(s)
	 */
	 public Collection<PDGraph> getGraphs();

   /**
	 * Connects this instance to the given instance using role "Graph".
	 * If the given instance is null, nothing happens.
	 * @param graph the instance to connect
	 */
	public void addGraph(GUID graph);
	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If the given instance is null, nothing happens.
	 * @param graph the instance to connect
	 */
	public void addGraph(PDGraph graph);

	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If the given collection of instances is null, nothing happens.
	 * @param graph the Collection of instances to connect
	 */
	public void addGraphs(Collection<PDGraph> graphs);

	/**
	 * Removes the link from this instance through role "Graph".
	 */
	public void removeGraph();

	/**
	 * Removes the link from this instance through role "Graph" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeGraph(PDGraph graph);

	/**
	 * Removes the links from this instance through role "Graph" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeGraphs(Collection<PDGraph> graphs);

	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If there is already an instance connected to this instance through role "Graph", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param graph the instance to connect
	 */
	public void setGraph(GUID graph) ;
	/**
	 * Connects this instance to the given instance using role "Graph".
	 * If there is already an instance connected to this instance through role "Graph", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param graph the instance to connect
	 */
	public void setGraph(PDGraph graph) 
;}
