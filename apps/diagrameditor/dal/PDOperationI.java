package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Operation" in memory.
 * @author PDGen
 */
public interface PDOperationI{

	/**
	 * Returns the instance connected to this instance through the role shapeType.
	 * @return the connected instance
	 */
	public PDType getShapeType();

	/**
	 * Returns the instance(s) connected to this instance through the role shapeType.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getShapeTypes();

   /**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(GUID shapeType);
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(PDType shapeType);

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given collection of instances is null, nothing happens.
	 * @param shapeType the Collection of instances to connect
	 */
	public void addShapeTypes(Collection<PDType> shapeTypes);

	/**
	 * Removes the link from this instance through role "shapeType".
	 */
	public void removeShapeType();

	/**
	 * Removes the link from this instance through role "shapeType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShapeType(PDType shapeType);

	/**
	 * Removes the links from this instance through role "shapeType" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeShapeTypes(Collection<PDType> shapeTypes);

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(GUID shapeType) ;
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(PDType shapeType) 
;	/**
	 * Returns the instance connected to this instance through the role implementation.
	 * @return the connected instance
	 */
	public String getImplementation();

	/**
	 * Returns the instance(s) connected to this instance through the role implementation.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getImplementations();

   /**
	 * Connects this instance to the given instance using role "implementation".
	 * If the given instance is null, nothing happens.
	 * @param implementation the instance to connect
	 */
	public void addImplementation(String implementation);
	/**
	 * Connects this instance to the given instances using role "implementation".
	 * If the given collection of instances is null, nothing happens.
	 * @param implementation the Collection of instances to connect
	 */
	public void addImplementations(Collection<String> implementations);

	/**
	 * Removes the link from this instance through role "implementation".
	 */
	public void removeImplementation();

	/**
	 * Removes the link from this instance through role "implementation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeImplementation(String implementation);

	/**
	 * Connects this instance to the given instance using role "implementation".
	 * If there is already an instance connected to this instance through role "implementation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param implementation the instance to connect
	 */
	public void setImplementation(String implementation) ;
	/**
	 * Returns the instance connected to this instance through the role type.
	 * @return the connected instance
	 */
	public PDType getType();

	/**
	 * Returns the instance(s) connected to this instance through the role type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getTypes();

   /**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(GUID type);
	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(PDType type);

	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given collection of instances is null, nothing happens.
	 * @param type the Collection of instances to connect
	 */
	public void addTypes(Collection<PDType> types);

	/**
	 * Removes the link from this instance through role "type".
	 */
	public void removeType();

	/**
	 * Removes the link from this instance through role "type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeType(PDType type);

	/**
	 * Removes the links from this instance through role "type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTypes(Collection<PDType> types);

	/**
	 * Connects this instance to the given instance using role "type".
	 * If there is already an instance connected to this instance through role "type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param type the instance to connect
	 */
	public void setType(GUID type) ;
	/**
	 * Connects this instance to the given instance using role "type".
	 * If there is already an instance connected to this instance through role "type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param type the instance to connect
	 */
	public void setType(PDType type) 
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
;}
