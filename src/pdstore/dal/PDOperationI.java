package pdstore.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "Operation" in memory.
 * @author PDGen
 */
public interface PDOperationI{

	/**
	 * Returns the instance connected to this instance through the role widget implementation.
	 * @return the connected instance
	 */
	public String getWidgetImplementation();

	/**
	 * Returns the instance(s) connected to this instance through the role widget implementation.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getWidgetImplementations();

   /**
	 * Connects this instance to the given instance using role "widget implementation".
	 * If the given instance is null, nothing happens.
	 * @param widgetImplementation the instance to connect
	 */
	public void addWidgetImplementation(String widgetImplementation);
	/**
	 * Connects this instance to the given instances using role "widget implementation".
	 * If the given collection of instances is null, nothing happens.
	 * @param widgetImplementation the Collection of instances to connect
	 */
	public void addWidgetImplementations(Collection<String> widgetImplementations);

	/**
	 * Removes the link from this instance through role "widget implementation".
	 */
	public void removeWidgetImplementation();

	/**
	 * Removes the link from this instance through role "widget implementation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeWidgetImplementation(String widgetImplementation);

	/**
	 * Connects this instance to the given instance using role "widget implementation".
	 * If there is already an instance connected to this instance through role "widget implementation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param widgetImplementation the instance to connect
	 */
	public void setWidgetImplementation(String widgetImplementation) ;
	/**
	 * Returns the instance connected to this instance through the role operation implementation.
	 * @return the connected instance
	 */
	public String getOperationImplementation();

	/**
	 * Returns the instance(s) connected to this instance through the role operation implementation.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getOperationImplementations();

   /**
	 * Connects this instance to the given instance using role "operation implementation".
	 * If the given instance is null, nothing happens.
	 * @param operationImplementation the instance to connect
	 */
	public void addOperationImplementation(String operationImplementation);
	/**
	 * Connects this instance to the given instances using role "operation implementation".
	 * If the given collection of instances is null, nothing happens.
	 * @param operationImplementation the Collection of instances to connect
	 */
	public void addOperationImplementations(Collection<String> operationImplementations);

	/**
	 * Removes the link from this instance through role "operation implementation".
	 */
	public void removeOperationImplementation();

	/**
	 * Removes the link from this instance through role "operation implementation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOperationImplementation(String operationImplementation);

	/**
	 * Connects this instance to the given instance using role "operation implementation".
	 * If there is already an instance connected to this instance through role "operation implementation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param operationImplementation the instance to connect
	 */
	public void setOperationImplementation(String operationImplementation) ;
	/**
	 * Returns the instance connected to this instance through the role parameter type.
	 * @return the connected instance
	 */
	public PDType getParameterType();

	/**
	 * Returns the instance(s) connected to this instance through the role parameter type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getParameterTypes();

   /**
	 * Connects this instance to the given instance using role "parameter type".
	 * If the given instance is null, nothing happens.
	 * @param parameterType the instance to connect
	 */
	public void addParameterType(GUID parameterType);
	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If the given instance is null, nothing happens.
	 * @param parameterType the instance to connect
	 */
	public void addParameterType(PDType parameterType);

	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If the given collection of instances is null, nothing happens.
	 * @param parameterType the Collection of instances to connect
	 */
	public void addParameterTypes(Collection<PDType> parameterTypes);

	/**
	 * Removes the link from this instance through role "parameter type".
	 */
	public void removeParameterType();

	/**
	 * Removes the link from this instance through role "parameter type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeParameterType(PDType parameterType);

	/**
	 * Removes the links from this instance through role "parameter type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeParameterTypes(Collection<PDType> parameterTypes);

	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If there is already an instance connected to this instance through role "parameter type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parameterType the instance to connect
	 */
	public void setParameterType(GUID parameterType) ;
	/**
	 * Connects this instance to the given instance using role "parameter type".
	 * If there is already an instance connected to this instance through role "parameter type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parameterType the instance to connect
	 */
	public void setParameterType(PDType parameterType) 
;}
