package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Serializer Application" in memory.
 * @author PDGen
 */
public interface PDSerializerApplicationI{

	/**
	 * Returns the instance connected to this instance through the role serializer.
	 * @return the connected instance
	 */
	public PDSerializer getSerializer();

	/**
	 * Returns the instance(s) connected to this instance through the role serializer.
	 * @return the connected instance(s)
	 */
	 public Collection<PDSerializer> getSerializers();

   /**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given instance is null, nothing happens.
	 * @param serializer the instance to connect
	 */
	public void addSerializer(GUID serializer);
	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given instance is null, nothing happens.
	 * @param serializer the instance to connect
	 */
	public void addSerializer(PDSerializer serializer);

	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If the given collection of instances is null, nothing happens.
	 * @param serializer the Collection of instances to connect
	 */
	public void addSerializers(Collection<PDSerializer> serializers);

	/**
	 * Removes the link from this instance through role "serializer".
	 */
	public void removeSerializer();

	/**
	 * Removes the link from this instance through role "serializer" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSerializer(PDSerializer serializer);

	/**
	 * Removes the links from this instance through role "serializer" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSerializers(Collection<PDSerializer> serializers);

	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If there is already an instance connected to this instance through role "serializer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param serializer the instance to connect
	 */
	public void setSerializer(GUID serializer) ;
	/**
	 * Connects this instance to the given instance using role "serializer".
	 * If there is already an instance connected to this instance through role "serializer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param serializer the instance to connect
	 */
	public void setSerializer(PDSerializer serializer) 
;	/**
	 * Returns the instance connected to this instance through the role output.
	 * @return the connected instance
	 */
	public String getOutput();

	/**
	 * Returns the instance(s) connected to this instance through the role output.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getOutputs();

   /**
	 * Connects this instance to the given instance using role "output".
	 * If the given instance is null, nothing happens.
	 * @param output the instance to connect
	 */
	public void addOutput(String output);
	/**
	 * Connects this instance to the given instances using role "output".
	 * If the given collection of instances is null, nothing happens.
	 * @param output the Collection of instances to connect
	 */
	public void addOutputs(Collection<String> outputs);

	/**
	 * Removes the link from this instance through role "output".
	 */
	public void removeOutput();

	/**
	 * Removes the link from this instance through role "output" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutput(String output);

	/**
	 * Connects this instance to the given instance using role "output".
	 * If there is already an instance connected to this instance through role "output", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param output the instance to connect
	 */
	public void setOutput(String output) ;
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
;	/**
	 * Returns the instance connected to this instance through the role input.
	 * @return the connected instance
	 */
	public GUID getInput();

	/**
	 * Returns the instance(s) connected to this instance through the role input.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getInputs();

   /**
	 * Connects this instance to the given instance using role "input".
	 * If the given instance is null, nothing happens.
	 * @param input the instance to connect
	 */
	public void addInput(GUID input);
	/**
	 * Connects this instance to the given instances using role "input".
	 * If the given collection of instances is null, nothing happens.
	 * @param input the Collection of instances to connect
	 */
	public void addInputs(Collection<GUID> inputs);

	/**
	 * Removes the link from this instance through role "input".
	 */
	public void removeInput();

	/**
	 * Removes the link from this instance through role "input" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInput(GUID input);

	/**
	 * Connects this instance to the given instance using role "input".
	 * If there is already an instance connected to this instance through role "input", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param input the instance to connect
	 */
	public void setInput(GUID input) ;
	/**
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
;}
