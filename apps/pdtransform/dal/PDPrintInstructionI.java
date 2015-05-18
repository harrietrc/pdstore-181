package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Print Instruction" in memory.
 * @author PDGen
 */
public interface PDPrintInstructionI{

	/**
	 * Returns the instance connected to this instance through the role print after.
	 * @return the connected instance
	 */
	public String getPrintAfter();

	/**
	 * Returns the instance(s) connected to this instance through the role print after.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getPrintAfters();

   /**
	 * Connects this instance to the given instance using role "print after".
	 * If the given instance is null, nothing happens.
	 * @param printAfter the instance to connect
	 */
	public void addPrintAfter(String printAfter);
	/**
	 * Connects this instance to the given instances using role "print after".
	 * If the given collection of instances is null, nothing happens.
	 * @param printAfter the Collection of instances to connect
	 */
	public void addPrintAfters(Collection<String> printAfters);

	/**
	 * Removes the link from this instance through role "print after".
	 */
	public void removePrintAfter();

	/**
	 * Removes the link from this instance through role "print after" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrintAfter(String printAfter);

	/**
	 * Connects this instance to the given instance using role "print after".
	 * If there is already an instance connected to this instance through role "print after", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param printAfter the instance to connect
	 */
	public void setPrintAfter(String printAfter) ;
	/**
	 * Returns the instance connected to this instance through the role print before.
	 * @return the connected instance
	 */
	public String getPrintBefore();

	/**
	 * Returns the instance(s) connected to this instance through the role print before.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getPrintBefores();

   /**
	 * Connects this instance to the given instance using role "print before".
	 * If the given instance is null, nothing happens.
	 * @param printBefore the instance to connect
	 */
	public void addPrintBefore(String printBefore);
	/**
	 * Connects this instance to the given instances using role "print before".
	 * If the given collection of instances is null, nothing happens.
	 * @param printBefore the Collection of instances to connect
	 */
	public void addPrintBefores(Collection<String> printBefores);

	/**
	 * Removes the link from this instance through role "print before".
	 */
	public void removePrintBefore();

	/**
	 * Removes the link from this instance through role "print before" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrintBefore(String printBefore);

	/**
	 * Connects this instance to the given instance using role "print before".
	 * If there is already an instance connected to this instance through role "print before", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param printBefore the instance to connect
	 */
	public void setPrintBefore(String printBefore) ;
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
;}
