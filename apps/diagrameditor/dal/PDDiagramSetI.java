package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "DiagramSet" in memory.
 * @author PDGen
 */
public interface PDDiagramSetI{

	/**
	 * Returns the instance connected to this instance through the role Diagram.
	 * @return the connected instance
	 */
	public PDDiagram getDiagram();

	/**
	 * Returns the instance(s) connected to this instance through the role Diagram.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagram> getDiagrams();

   /**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given instance is null, nothing happens.
	 * @param diagram the instance to connect
	 */
	public void addDiagram(GUID diagram);
	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given instance is null, nothing happens.
	 * @param diagram the instance to connect
	 */
	public void addDiagram(PDDiagram diagram);

	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given collection of instances is null, nothing happens.
	 * @param diagram the Collection of instances to connect
	 */
	public void addDiagrams(Collection<PDDiagram> diagrams);

	/**
	 * Removes the link from this instance through role "Diagram".
	 */
	public void removeDiagram();

	/**
	 * Removes the link from this instance through role "Diagram" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDiagram(PDDiagram diagram);

	/**
	 * Removes the links from this instance through role "Diagram" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeDiagrams(Collection<PDDiagram> diagrams);

	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If there is already an instance connected to this instance through role "Diagram", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagram the instance to connect
	 */
	public void setDiagram(GUID diagram) ;
	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If there is already an instance connected to this instance through role "Diagram", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagram the instance to connect
	 */
	public void setDiagram(PDDiagram diagram) 
;}
