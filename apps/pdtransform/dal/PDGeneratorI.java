package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Generator" in memory.
 * @author PDGen
 */
public interface PDGeneratorI{

	/**
	 * Returns the instance connected to this instance through the role generator applications.
	 * @return the connected instance
	 */
	public PDGeneratorApplication getGeneratorApplications();

	/**
	 * Returns the instance(s) connected to this instance through the role generator applications.
	 * @return the connected instance(s)
	 */
	 public Collection<PDGeneratorApplication> getGeneratorApplicationss();

   /**
	 * Connects this instance to the given instance using role "generator applications".
	 * If the given instance is null, nothing happens.
	 * @param generatorApplications the instance to connect
	 */
	public void addGeneratorApplications(GUID generatorApplications);
	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If the given instance is null, nothing happens.
	 * @param generatorApplications the instance to connect
	 */
	public void addGeneratorApplications(PDGeneratorApplication generatorApplications);

	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If the given collection of instances is null, nothing happens.
	 * @param generatorApplications the Collection of instances to connect
	 */
	public void addGeneratorApplicationss(Collection<PDGeneratorApplication> generatorApplicationss);

	/**
	 * Removes the link from this instance through role "generator applications".
	 */
	public void removeGeneratorApplications();

	/**
	 * Removes the link from this instance through role "generator applications" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeGeneratorApplications(PDGeneratorApplication generatorApplications);

	/**
	 * Removes the links from this instance through role "generator applications" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeGeneratorApplicationss(Collection<PDGeneratorApplication> generatorApplicationss);

	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If there is already an instance connected to this instance through role "generator applications", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generatorApplications the instance to connect
	 */
	public void setGeneratorApplications(GUID generatorApplications) ;
	/**
	 * Connects this instance to the given instance using role "generator applications".
	 * If there is already an instance connected to this instance through role "generator applications", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generatorApplications the instance to connect
	 */
	public void setGeneratorApplications(PDGeneratorApplication generatorApplications) 
;	/**
	 * Returns the instance connected to this instance through the role map.
	 * @return the connected instance
	 */
	public PDMap getMap();

	/**
	 * Returns the instance(s) connected to this instance through the role map.
	 * @return the connected instance(s)
	 */
	 public Collection<PDMap> getMaps();

   /**
	 * Connects this instance to the given instance using role "map".
	 * If the given instance is null, nothing happens.
	 * @param map the instance to connect
	 */
	public void addMap(GUID map);
	/**
	 * Connects this instance to the given instance using role "map".
	 * If the given instance is null, nothing happens.
	 * @param map the instance to connect
	 */
	public void addMap(PDMap map);

	/**
	 * Connects this instance to the given instance using role "map".
	 * If the given collection of instances is null, nothing happens.
	 * @param map the Collection of instances to connect
	 */
	public void addMaps(Collection<PDMap> maps);

	/**
	 * Removes the link from this instance through role "map".
	 */
	public void removeMap();

	/**
	 * Removes the link from this instance through role "map" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeMap(PDMap map);

	/**
	 * Removes the links from this instance through role "map" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeMaps(Collection<PDMap> maps);

	/**
	 * Connects this instance to the given instance using role "map".
	 * If there is already an instance connected to this instance through role "map", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param map the instance to connect
	 */
	public void setMap(GUID map) ;
	/**
	 * Connects this instance to the given instance using role "map".
	 * If there is already an instance connected to this instance through role "map", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param map the instance to connect
	 */
	public void setMap(PDMap map) 
;	/**
	 * Returns the instance connected to this instance through the role order.
	 * @return the connected instance
	 */
	public PDOrder getOrder();

	/**
	 * Returns the instance(s) connected to this instance through the role order.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOrder> getOrders();

   /**
	 * Connects this instance to the given instance using role "order".
	 * If the given instance is null, nothing happens.
	 * @param order the instance to connect
	 */
	public void addOrder(GUID order);
	/**
	 * Connects this instance to the given instance using role "order".
	 * If the given instance is null, nothing happens.
	 * @param order the instance to connect
	 */
	public void addOrder(PDOrder order);

	/**
	 * Connects this instance to the given instance using role "order".
	 * If the given collection of instances is null, nothing happens.
	 * @param order the Collection of instances to connect
	 */
	public void addOrders(Collection<PDOrder> orders);

	/**
	 * Removes the link from this instance through role "order".
	 */
	public void removeOrder();

	/**
	 * Removes the link from this instance through role "order" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOrder(PDOrder order);

	/**
	 * Removes the links from this instance through role "order" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOrders(Collection<PDOrder> orders);

	/**
	 * Connects this instance to the given instance using role "order".
	 * If there is already an instance connected to this instance through role "order", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param order the instance to connect
	 */
	public void setOrder(GUID order) ;
	/**
	 * Connects this instance to the given instance using role "order".
	 * If there is already an instance connected to this instance through role "order", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param order the instance to connect
	 */
	public void setOrder(PDOrder order) 
;	/**
	 * Returns the instance connected to this instance through the role output type.
	 * @return the connected instance
	 */
	public PDType getOutputType();

	/**
	 * Returns the instance(s) connected to this instance through the role output type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getOutputTypes();

   /**
	 * Connects this instance to the given instance using role "output type".
	 * If the given instance is null, nothing happens.
	 * @param outputType the instance to connect
	 */
	public void addOutputType(GUID outputType);
	/**
	 * Connects this instance to the given instance using role "output type".
	 * If the given instance is null, nothing happens.
	 * @param outputType the instance to connect
	 */
	public void addOutputType(PDType outputType);

	/**
	 * Connects this instance to the given instance using role "output type".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputType the Collection of instances to connect
	 */
	public void addOutputTypes(Collection<PDType> outputTypes);

	/**
	 * Removes the link from this instance through role "output type".
	 */
	public void removeOutputType();

	/**
	 * Removes the link from this instance through role "output type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputType(PDType outputType);

	/**
	 * Removes the links from this instance through role "output type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOutputTypes(Collection<PDType> outputTypes);

	/**
	 * Connects this instance to the given instance using role "output type".
	 * If there is already an instance connected to this instance through role "output type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputType the instance to connect
	 */
	public void setOutputType(GUID outputType) ;
	/**
	 * Connects this instance to the given instance using role "output type".
	 * If there is already an instance connected to this instance through role "output type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputType the instance to connect
	 */
	public void setOutputType(PDType outputType) 
;	/**
	 * Returns the instance connected to this instance through the role output template.
	 * @return the connected instance
	 */
	public GUID getOutputTemplate();

	/**
	 * Returns the instance(s) connected to this instance through the role output template.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getOutputTemplates();

   /**
	 * Connects this instance to the given instance using role "output template".
	 * If the given instance is null, nothing happens.
	 * @param outputTemplate the instance to connect
	 */
	public void addOutputTemplate(GUID outputTemplate);
	/**
	 * Connects this instance to the given instances using role "output template".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputTemplate the Collection of instances to connect
	 */
	public void addOutputTemplates(Collection<GUID> outputTemplates);

	/**
	 * Removes the link from this instance through role "output template".
	 */
	public void removeOutputTemplate();

	/**
	 * Removes the link from this instance through role "output template" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputTemplate(GUID outputTemplate);

	/**
	 * Connects this instance to the given instance using role "output template".
	 * If there is already an instance connected to this instance through role "output template", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputTemplate the instance to connect
	 */
	public void setOutputTemplate(GUID outputTemplate) ;
	/**
	 * Returns the instance connected to this instance through the role input type.
	 * @return the connected instance
	 */
	public PDType getInputType();

	/**
	 * Returns the instance(s) connected to this instance through the role input type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getInputTypes();

   /**
	 * Connects this instance to the given instance using role "input type".
	 * If the given instance is null, nothing happens.
	 * @param inputType the instance to connect
	 */
	public void addInputType(GUID inputType);
	/**
	 * Connects this instance to the given instance using role "input type".
	 * If the given instance is null, nothing happens.
	 * @param inputType the instance to connect
	 */
	public void addInputType(PDType inputType);

	/**
	 * Connects this instance to the given instance using role "input type".
	 * If the given collection of instances is null, nothing happens.
	 * @param inputType the Collection of instances to connect
	 */
	public void addInputTypes(Collection<PDType> inputTypes);

	/**
	 * Removes the link from this instance through role "input type".
	 */
	public void removeInputType();

	/**
	 * Removes the link from this instance through role "input type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInputType(PDType inputType);

	/**
	 * Removes the links from this instance through role "input type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInputTypes(Collection<PDType> inputTypes);

	/**
	 * Connects this instance to the given instance using role "input type".
	 * If there is already an instance connected to this instance through role "input type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputType the instance to connect
	 */
	public void setInputType(GUID inputType) ;
	/**
	 * Connects this instance to the given instance using role "input type".
	 * If there is already an instance connected to this instance through role "input type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputType the instance to connect
	 */
	public void setInputType(PDType inputType) 
;}
