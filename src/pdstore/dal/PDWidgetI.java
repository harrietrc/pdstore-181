package pdstore.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "Widget" in memory.
 * @author PDGen
 */
public interface PDWidgetI{

	/**
	 * Returns the instance connected to this instance through the role visualizes.
	 * @return the connected instance
	 */
	public PDInstance getVisualizes();

	/**
	 * Returns the instance(s) connected to this instance through the role visualizes.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getVisualizess();

   /**
	 * Connects this instance to the given instance using role "visualizes".
	 * If the given instance is null, nothing happens.
	 * @param visualizes the instance to connect
	 */
	public void addVisualizes(GUID visualizes);
	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If the given instance is null, nothing happens.
	 * @param visualizes the instance to connect
	 */
	public void addVisualizes(PDInstance visualizes);

	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If the given collection of instances is null, nothing happens.
	 * @param visualizes the Collection of instances to connect
	 */
	public void addVisualizess(Collection<PDInstance> visualizess);

	/**
	 * Removes the link from this instance through role "visualizes".
	 */
	public void removeVisualizes();

	/**
	 * Removes the link from this instance through role "visualizes" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeVisualizes(PDInstance visualizes);

	/**
	 * Removes the links from this instance through role "visualizes" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeVisualizess(Collection<PDInstance> visualizess);

	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If there is already an instance connected to this instance through role "visualizes", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizes the instance to connect
	 */
	public void setVisualizes(GUID visualizes) ;
	/**
	 * Connects this instance to the given instance using role "visualizes".
	 * If there is already an instance connected to this instance through role "visualizes", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizes the instance to connect
	 */
	public void setVisualizes(PDInstance visualizes) 
;}
