package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "OperationApplication" in memory.
 * @author PDGen
 */
public interface PDOperationApplicationI{

	/**
	 * Returns the instance connected to this instance through the role superParameter.
	 * @return the connected instance
	 */
	public PDInstance getSuperParameter();

	/**
	 * Returns the instance(s) connected to this instance through the role superParameter.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getSuperParameters();

   /**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given instance is null, nothing happens.
	 * @param superParameter the instance to connect
	 */
	public void addSuperParameter(GUID superParameter);
	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given instance is null, nothing happens.
	 * @param superParameter the instance to connect
	 */
	public void addSuperParameter(PDInstance superParameter);

	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If the given collection of instances is null, nothing happens.
	 * @param superParameter the Collection of instances to connect
	 */
	public void addSuperParameters(Collection<PDInstance> superParameters);

	/**
	 * Removes the link from this instance through role "superParameter".
	 */
	public void removeSuperParameter();

	/**
	 * Removes the link from this instance through role "superParameter" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSuperParameter(PDInstance superParameter);

	/**
	 * Removes the links from this instance through role "superParameter" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSuperParameters(Collection<PDInstance> superParameters);

	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If there is already an instance connected to this instance through role "superParameter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param superParameter the instance to connect
	 */
	public void setSuperParameter(GUID superParameter) ;
	/**
	 * Connects this instance to the given instance using role "superParameter".
	 * If there is already an instance connected to this instance through role "superParameter", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param superParameter the instance to connect
	 */
	public void setSuperParameter(PDInstance superParameter) 
;	/**
	 * Returns the instance connected to this instance through the role previous.
	 * @return the connected instance
	 */
	public PDOperationApplication getPrevious();

	/**
	 * Returns the instance(s) connected to this instance through the role previous.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperationApplication> getPreviouss();

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
	public void addPrevious(PDOperationApplication previous);

	/**
	 * Connects this instance to the given instance using role "previous".
	 * If the given collection of instances is null, nothing happens.
	 * @param previous the Collection of instances to connect
	 */
	public void addPreviouss(Collection<PDOperationApplication> previouss);

	/**
	 * Removes the link from this instance through role "previous".
	 */
	public void removePrevious();

	/**
	 * Removes the link from this instance through role "previous" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrevious(PDOperationApplication previous);

	/**
	 * Removes the links from this instance through role "previous" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removePreviouss(Collection<PDOperationApplication> previouss);

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
	public void setPrevious(PDOperationApplication previous) 
;	/**
	 * Returns the instance connected to this instance through the role next.
	 * @return the connected instance
	 */
	public PDOperationApplication getNext();

	/**
	 * Returns the instance(s) connected to this instance through the role next.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperationApplication> getNexts();

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
	public void addNext(PDOperationApplication next);

	/**
	 * Connects this instance to the given instance using role "next".
	 * If the given collection of instances is null, nothing happens.
	 * @param next the Collection of instances to connect
	 */
	public void addNexts(Collection<PDOperationApplication> nexts);

	/**
	 * Removes the link from this instance through role "next".
	 */
	public void removeNext();

	/**
	 * Removes the link from this instance through role "next" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNext(PDOperationApplication next);

	/**
	 * Removes the links from this instance through role "next" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNexts(Collection<PDOperationApplication> nexts);

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
	public void setNext(PDOperationApplication next) 
;	/**
	 * Returns the instance connected to this instance through the role Operation.
	 * @return the connected instance
	 */
	public PDOperation getOperation();

	/**
	 * Returns the instance(s) connected to this instance through the role Operation.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperation> getOperations();

   /**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 */
	public void addOperation(GUID operation);
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given instance is null, nothing happens.
	 * @param operation the instance to connect
	 */
	public void addOperation(PDOperation operation);

	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If the given collection of instances is null, nothing happens.
	 * @param operation the Collection of instances to connect
	 */
	public void addOperations(Collection<PDOperation> operations);

	/**
	 * Removes the link from this instance through role "Operation".
	 */
	public void removeOperation();

	/**
	 * Removes the link from this instance through role "Operation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperation(PDOperation operation);

	/**
	 * Removes the links from this instance through role "Operation" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOperations(Collection<PDOperation> operations);

	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operation the instance to connect
	 */
	public void setOperation(GUID operation) ;
	/**
	 * Connects this instance to the given instance using role "Operation".
	 * If there is already an instance connected to this instance through role "Operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operation the instance to connect
	 */
	public void setOperation(PDOperation operation) 
;	/**
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
