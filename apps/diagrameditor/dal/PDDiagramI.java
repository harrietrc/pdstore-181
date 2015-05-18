package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Diagram" in memory.
 * @author PDGen
 */
public interface PDDiagramI{

	/**
	 * Returns the instance connected to this instance through the role previous.
	 * @return the connected instance
	 */
	public PDDiagram getPrevious();

	/**
	 * Returns the instance(s) connected to this instance through the role previous.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagram> getPreviouss();

   /**
	 * Connects this instance to the given instance using role "previous".
	 * If the given instance is null, nothing happens.
	 * @param previous the instance to connect
	 */
	public void addPrevious(GUID previous);
	/**
	 * Connects this instance to the given instance using role "previous".
	 * If the given instance is null, nothing happens.
	 * @param previous the instance to connect
	 */
	public void addPrevious(PDDiagram previous);

	/**
	 * Connects this instance to the given instance using role "previous".
	 * If the given collection of instances is null, nothing happens.
	 * @param previous the Collection of instances to connect
	 */
	public void addPreviouss(Collection<PDDiagram> previouss);

	/**
	 * Removes the link from this instance through role "previous".
	 */
	public void removePrevious();

	/**
	 * Removes the link from this instance through role "previous" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrevious(PDDiagram previous);

	/**
	 * Removes the links from this instance through role "previous" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removePreviouss(Collection<PDDiagram> previouss);

	/**
	 * Connects this instance to the given instance using role "previous".
	 * If there is already an instance connected to this instance through role "previous", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param previous the instance to connect
	 */
	public void setPrevious(GUID previous) ;
	/**
	 * Connects this instance to the given instance using role "previous".
	 * If there is already an instance connected to this instance through role "previous", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param previous the instance to connect
	 */
	public void setPrevious(PDDiagram previous) 
;	/**
	 * Returns the instance connected to this instance through the role next.
	 * @return the connected instance
	 */
	public PDDiagram getNext();

	/**
	 * Returns the instance(s) connected to this instance through the role next.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagram> getNexts();

   /**
	 * Connects this instance to the given instance using role "next".
	 * If the given instance is null, nothing happens.
	 * @param next the instance to connect
	 */
	public void addNext(GUID next);
	/**
	 * Connects this instance to the given instance using role "next".
	 * If the given instance is null, nothing happens.
	 * @param next the instance to connect
	 */
	public void addNext(PDDiagram next);

	/**
	 * Connects this instance to the given instance using role "next".
	 * If the given collection of instances is null, nothing happens.
	 * @param next the Collection of instances to connect
	 */
	public void addNexts(Collection<PDDiagram> nexts);

	/**
	 * Removes the link from this instance through role "next".
	 */
	public void removeNext();

	/**
	 * Removes the link from this instance through role "next" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNext(PDDiagram next);

	/**
	 * Removes the links from this instance through role "next" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNexts(Collection<PDDiagram> nexts);

	/**
	 * Connects this instance to the given instance using role "next".
	 * If there is already an instance connected to this instance through role "next", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param next the instance to connect
	 */
	public void setNext(GUID next) ;
	/**
	 * Connects this instance to the given instance using role "next".
	 * If there is already an instance connected to this instance through role "next", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param next the instance to connect
	 */
	public void setNext(PDDiagram next) 
;	/**
	 * Returns the instance connected to this instance through the role Shape.
	 * @return the connected instance
	 */
	public PDShape getShape();

	/**
	 * Returns the instance(s) connected to this instance through the role Shape.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getShapes();

   /**
	 * Connects this instance to the given instance using role "Shape".
	 * If the given instance is null, nothing happens.
	 * @param shape the instance to connect
	 */
	public void addShape(GUID shape);
	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If the given instance is null, nothing happens.
	 * @param shape the instance to connect
	 */
	public void addShape(PDShape shape);

	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If the given collection of instances is null, nothing happens.
	 * @param shape the Collection of instances to connect
	 */
	public void addShapes(Collection<PDShape> shapes);

	/**
	 * Removes the link from this instance through role "Shape".
	 */
	public void removeShape();

	/**
	 * Removes the link from this instance through role "Shape" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShape(PDShape shape);

	/**
	 * Removes the links from this instance through role "Shape" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeShapes(Collection<PDShape> shapes);

	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If there is already an instance connected to this instance through role "Shape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shape the instance to connect
	 */
	public void setShape(GUID shape) ;
	/**
	 * Connects this instance to the given instance using role "Shape".
	 * If there is already an instance connected to this instance through role "Shape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shape the instance to connect
	 */
	public void setShape(PDShape shape) 
;	/**
	 * Returns the instance connected to this instance through the role Operation Application.
	 * @return the connected instance
	 */
	public PDOperationApplication getOperationApplication();

	/**
	 * Returns the instance(s) connected to this instance through the role Operation Application.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperationApplication> getOperationApplications();

   /**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given instance is null, nothing happens.
	 * @param operationApplication the instance to connect
	 */
	public void addOperationApplication(GUID operationApplication);
	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given instance is null, nothing happens.
	 * @param operationApplication the instance to connect
	 */
	public void addOperationApplication(PDOperationApplication operationApplication);

	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If the given collection of instances is null, nothing happens.
	 * @param operationApplication the Collection of instances to connect
	 */
	public void addOperationApplications(Collection<PDOperationApplication> operationApplications);

	/**
	 * Removes the link from this instance through role "Operation Application".
	 */
	public void removeOperationApplication();

	/**
	 * Removes the link from this instance through role "Operation Application" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperationApplication(PDOperationApplication operationApplication);

	/**
	 * Removes the links from this instance through role "Operation Application" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOperationApplications(Collection<PDOperationApplication> operationApplications);

	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If there is already an instance connected to this instance through role "Operation Application", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationApplication the instance to connect
	 */
	public void setOperationApplication(GUID operationApplication) ;
	/**
	 * Connects this instance to the given instance using role "Operation Application".
	 * If there is already an instance connected to this instance through role "Operation Application", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationApplication the instance to connect
	 */
	public void setOperationApplication(PDOperationApplication operationApplication) 
;	/**
	 * Returns the instance connected to this instance through the role DiagramSet.
	 * @return the connected instance
	 */
	public PDDiagramSet getDiagramSet();

	/**
	 * Returns the instance(s) connected to this instance through the role DiagramSet.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagramSet> getDiagramSets();

   /**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If the given instance is null, nothing happens.
	 * @param diagramSet the instance to connect
	 */
	public void addDiagramSet(GUID diagramSet);
	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If the given instance is null, nothing happens.
	 * @param diagramSet the instance to connect
	 */
	public void addDiagramSet(PDDiagramSet diagramSet);

	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If the given collection of instances is null, nothing happens.
	 * @param diagramSet the Collection of instances to connect
	 */
	public void addDiagramSets(Collection<PDDiagramSet> diagramSets);

	/**
	 * Removes the link from this instance through role "DiagramSet".
	 */
	public void removeDiagramSet();

	/**
	 * Removes the link from this instance through role "DiagramSet" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDiagramSet(PDDiagramSet diagramSet);

	/**
	 * Removes the links from this instance through role "DiagramSet" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeDiagramSets(Collection<PDDiagramSet> diagramSets);

	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If there is already an instance connected to this instance through role "DiagramSet", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagramSet the instance to connect
	 */
	public void setDiagramSet(GUID diagramSet) ;
	/**
	 * Connects this instance to the given instance using role "DiagramSet".
	 * If there is already an instance connected to this instance through role "DiagramSet", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagramSet the instance to connect
	 */
	public void setDiagramSet(PDDiagramSet diagramSet) 
;}
