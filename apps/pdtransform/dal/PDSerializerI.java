package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Serializer" in memory.
 * @author PDGen
 */
public interface PDSerializerI{

	/**
	 * Returns the instance connected to this instance through the role instruction.
	 * @return the connected instance
	 */
	public PDPrintInstruction getInstruction();

	/**
	 * Returns the instance(s) connected to this instance through the role instruction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDPrintInstruction> getInstructions();

   /**
	 * Connects this instance to the given instance using role "instruction".
	 * If the given instance is null, nothing happens.
	 * @param instruction the instance to connect
	 */
	public void addInstruction(GUID instruction);
	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If the given instance is null, nothing happens.
	 * @param instruction the instance to connect
	 */
	public void addInstruction(PDPrintInstruction instruction);

	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If the given collection of instances is null, nothing happens.
	 * @param instruction the Collection of instances to connect
	 */
	public void addInstructions(Collection<PDPrintInstruction> instructions);

	/**
	 * Removes the link from this instance through role "instruction".
	 */
	public void removeInstruction();

	/**
	 * Removes the link from this instance through role "instruction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInstruction(PDPrintInstruction instruction);

	/**
	 * Removes the links from this instance through role "instruction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInstructions(Collection<PDPrintInstruction> instructions);

	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If there is already an instance connected to this instance through role "instruction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instruction the instance to connect
	 */
	public void setInstruction(GUID instruction) ;
	/**
	 * Connects this instance to the given instance using role "instruction".
	 * If there is already an instance connected to this instance through role "instruction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instruction the instance to connect
	 */
	public void setInstruction(PDPrintInstruction instruction) 
;}
