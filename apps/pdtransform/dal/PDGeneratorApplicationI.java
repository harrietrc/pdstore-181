package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Generator Application" in memory.
 * @author PDGen
 */
public interface PDGeneratorApplicationI{

	/**
	 * Returns the instance connected to this instance through the role generator.
	 * @return the connected instance
	 */
	public PDGenerator getGenerator();

	/**
	 * Returns the instance(s) connected to this instance through the role generator.
	 * @return the connected instance(s)
	 */
	 public Collection<PDGenerator> getGenerators();

   /**
	 * Connects this instance to the given instance using role "generator".
	 * If the given instance is null, nothing happens.
	 * @param generator the instance to connect
	 */
	public void addGenerator(GUID generator);
	/**
	 * Connects this instance to the given instance using role "generator".
	 * If the given instance is null, nothing happens.
	 * @param generator the instance to connect
	 */
	public void addGenerator(PDGenerator generator);

	/**
	 * Connects this instance to the given instance using role "generator".
	 * If the given collection of instances is null, nothing happens.
	 * @param generator the Collection of instances to connect
	 */
	public void addGenerators(Collection<PDGenerator> generators);

	/**
	 * Removes the link from this instance through role "generator".
	 */
	public void removeGenerator();

	/**
	 * Removes the link from this instance through role "generator" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeGenerator(PDGenerator generator);

	/**
	 * Removes the links from this instance through role "generator" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeGenerators(Collection<PDGenerator> generators);

	/**
	 * Connects this instance to the given instance using role "generator".
	 * If there is already an instance connected to this instance through role "generator", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generator the instance to connect
	 */
	public void setGenerator(GUID generator) ;
	/**
	 * Connects this instance to the given instance using role "generator".
	 * If there is already an instance connected to this instance through role "generator", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param generator the instance to connect
	 */
	public void setGenerator(PDGenerator generator) 
;	/**
	 * Returns the instance connected to this instance through the role output.
	 * @return the connected instance
	 */
	public GUID getOutput();

	/**
	 * Returns the instance(s) connected to this instance through the role output.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getOutputs();

   /**
	 * Connects this instance to the given instance using role "output".
	 * If the given instance is null, nothing happens.
	 * @param output the instance to connect
	 */
	public void addOutput(GUID output);
	/**
	 * Connects this instance to the given instances using role "output".
	 * If the given collection of instances is null, nothing happens.
	 * @param output the Collection of instances to connect
	 */
	public void addOutputs(Collection<GUID> outputs);

	/**
	 * Removes the link from this instance through role "output".
	 */
	public void removeOutput();

	/**
	 * Removes the link from this instance through role "output" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutput(GUID output);

	/**
	 * Connects this instance to the given instance using role "output".
	 * If there is already an instance connected to this instance through role "output", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param output the instance to connect
	 */
	public void setOutput(GUID output) ;
	/**
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
}
