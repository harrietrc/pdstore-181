package pdstore.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "Branch" in memory.
 * @author PDGen
 */
public interface PDBranchI{

	/**
	 * Returns the instance connected to this instance through the role transaction.
	 * @return the connected instance
	 */
	public PDTransaction getTransaction();

	/**
	 * Returns the instance(s) connected to this instance through the role transaction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getTransactions();

   /**
	 * Connects this instance to the given instance using role "transaction".
	 * If the given instance is null, nothing happens.
	 * @param transaction the instance to connect
	 */
	public void addTransaction(GUID transaction);
	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If the given instance is null, nothing happens.
	 * @param transaction the instance to connect
	 */
	public void addTransaction(PDTransaction transaction);

	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If the given collection of instances is null, nothing happens.
	 * @param transaction the Collection of instances to connect
	 */
	public void addTransactions(Collection<PDTransaction> transactions);

	/**
	 * Removes the link from this instance through role "transaction".
	 */
	public void removeTransaction();

	/**
	 * Removes the link from this instance through role "transaction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTransaction(PDTransaction transaction);

	/**
	 * Removes the links from this instance through role "transaction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTransactions(Collection<PDTransaction> transactions);

	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If there is already an instance connected to this instance through role "transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param transaction the instance to connect
	 */
	public void setTransaction(GUID transaction) ;
	/**
	 * Connects this instance to the given instance using role "transaction".
	 * If there is already an instance connected to this instance through role "transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param transaction the instance to connect
	 */
	public void setTransaction(PDTransaction transaction) 
;}
