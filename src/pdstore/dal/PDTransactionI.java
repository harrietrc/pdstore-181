package pdstore.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "Transaction" in memory.
 * @author PDGen
 */
public interface PDTransactionI{

	/**
	 * Returns the instance connected to this instance through the role begin-transaction.
	 * @return the connected instance
	 */
	public PDTransaction getBegin_transaction();

	/**
	 * Returns the instance(s) connected to this instance through the role begin-transaction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getBegin_transactions();

   /**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If the given instance is null, nothing happens.
	 * @param begin_transaction the instance to connect
	 */
	public void addBegin_transaction(GUID begin_transaction);
	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If the given instance is null, nothing happens.
	 * @param begin_transaction the instance to connect
	 */
	public void addBegin_transaction(PDTransaction begin_transaction);

	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If the given collection of instances is null, nothing happens.
	 * @param begin_transaction the Collection of instances to connect
	 */
	public void addBegin_transactions(Collection<PDTransaction> begin_transactions);

	/**
	 * Removes the link from this instance through role "begin-transaction".
	 */
	public void removeBegin_transaction();

	/**
	 * Removes the link from this instance through role "begin-transaction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeBegin_transaction(PDTransaction begin_transaction);

	/**
	 * Removes the links from this instance through role "begin-transaction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeBegin_transactions(Collection<PDTransaction> begin_transactions);

	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If there is already an instance connected to this instance through role "begin-transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param begin_transaction the instance to connect
	 */
	public void setBegin_transaction(GUID begin_transaction) ;
	/**
	 * Connects this instance to the given instance using role "begin-transaction".
	 * If there is already an instance connected to this instance through role "begin-transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param begin_transaction the instance to connect
	 */
	public void setBegin_transaction(PDTransaction begin_transaction) 
;	/**
	 * Returns the instance connected to this instance through the role durable transaction.
	 * @return the connected instance
	 */
	public PDTransaction getDurableTransaction();

	/**
	 * Returns the instance(s) connected to this instance through the role durable transaction.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getDurableTransactions();

   /**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If the given instance is null, nothing happens.
	 * @param durableTransaction the instance to connect
	 */
	public void addDurableTransaction(GUID durableTransaction);
	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If the given instance is null, nothing happens.
	 * @param durableTransaction the instance to connect
	 */
	public void addDurableTransaction(PDTransaction durableTransaction);

	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If the given collection of instances is null, nothing happens.
	 * @param durableTransaction the Collection of instances to connect
	 */
	public void addDurableTransactions(Collection<PDTransaction> durableTransactions);

	/**
	 * Removes the link from this instance through role "durable transaction".
	 */
	public void removeDurableTransaction();

	/**
	 * Removes the link from this instance through role "durable transaction" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDurableTransaction(PDTransaction durableTransaction);

	/**
	 * Removes the links from this instance through role "durable transaction" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeDurableTransactions(Collection<PDTransaction> durableTransactions);

	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If there is already an instance connected to this instance through role "durable transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param durableTransaction the instance to connect
	 */
	public void setDurableTransaction(GUID durableTransaction) ;
	/**
	 * Connects this instance to the given instance using role "durable transaction".
	 * If there is already an instance connected to this instance through role "durable transaction", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param durableTransaction the instance to connect
	 */
	public void setDurableTransaction(PDTransaction durableTransaction) 
;	/**
	 * Returns the instance connected to this instance through the role isolation level.
	 * @return the connected instance
	 */
	public Long getIsolationLevel();

	/**
	 * Returns the instance(s) connected to this instance through the role isolation level.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getIsolationLevels();

   /**
	 * Connects this instance to the given instance using role "isolation level".
	 * If the given instance is null, nothing happens.
	 * @param isolationLevel the instance to connect
	 */
	public void addIsolationLevel(Long isolationLevel);
	/**
	 * Connects this instance to the given instances using role "isolation level".
	 * If the given collection of instances is null, nothing happens.
	 * @param isolationLevel the Collection of instances to connect
	 */
	public void addIsolationLevels(Collection<Long> isolationLevels);

	/**
	 * Removes the link from this instance through role "isolation level".
	 */
	public void removeIsolationLevel();

	/**
	 * Removes the link from this instance through role "isolation level" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeIsolationLevel(Long isolationLevel);

	/**
	 * Connects this instance to the given instance using role "isolation level".
	 * If there is already an instance connected to this instance through role "isolation level", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isolationLevel the instance to connect
	 */
	public void setIsolationLevel(Long isolationLevel) ;
	/**
	 * Returns the instance connected to this instance through the role branch.
	 * @return the connected instance
	 */
	public PDBranch getBranch();

	/**
	 * Returns the instance(s) connected to this instance through the role branch.
	 * @return the connected instance(s)
	 */
	 public Collection<PDBranch> getBranchs();

   /**
	 * Connects this instance to the given instance using role "branch".
	 * If the given instance is null, nothing happens.
	 * @param branch the instance to connect
	 */
	public void addBranch(GUID branch);
	/**
	 * Connects this instance to the given instance using role "branch".
	 * If the given instance is null, nothing happens.
	 * @param branch the instance to connect
	 */
	public void addBranch(PDBranch branch);

	/**
	 * Connects this instance to the given instance using role "branch".
	 * If the given collection of instances is null, nothing happens.
	 * @param branch the Collection of instances to connect
	 */
	public void addBranchs(Collection<PDBranch> branchs);

	/**
	 * Removes the link from this instance through role "branch".
	 */
	public void removeBranch();

	/**
	 * Removes the link from this instance through role "branch" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeBranch(PDBranch branch);

	/**
	 * Removes the links from this instance through role "branch" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeBranchs(Collection<PDBranch> branchs);

	/**
	 * Connects this instance to the given instance using role "branch".
	 * If there is already an instance connected to this instance through role "branch", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param branch the instance to connect
	 */
	public void setBranch(GUID branch) ;
	/**
	 * Connects this instance to the given instance using role "branch".
	 * If there is already an instance connected to this instance through role "branch", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param branch the instance to connect
	 */
	public void setBranch(PDBranch branch) 
;	/**
	 * Returns the instance connected to this instance through the role parent.
	 * @return the connected instance
	 */
	public PDTransaction getParent();

	/**
	 * Returns the instance(s) connected to this instance through the role parent.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getParents();

   /**
	 * Connects this instance to the given instance using role "parent".
	 * If the given instance is null, nothing happens.
	 * @param parent the instance to connect
	 */
	public void addParent(GUID parent);
	/**
	 * Connects this instance to the given instance using role "parent".
	 * If the given instance is null, nothing happens.
	 * @param parent the instance to connect
	 */
	public void addParent(PDTransaction parent);

	/**
	 * Connects this instance to the given instance using role "parent".
	 * If the given collection of instances is null, nothing happens.
	 * @param parent the Collection of instances to connect
	 */
	public void addParents(Collection<PDTransaction> parents);

	/**
	 * Removes the link from this instance through role "parent".
	 */
	public void removeParent();

	/**
	 * Removes the link from this instance through role "parent" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeParent(PDTransaction parent);

	/**
	 * Removes the links from this instance through role "parent" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeParents(Collection<PDTransaction> parents);

	/**
	 * Connects this instance to the given instance using role "parent".
	 * If there is already an instance connected to this instance through role "parent", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parent the instance to connect
	 */
	public void setParent(GUID parent) ;
	/**
	 * Connects this instance to the given instance using role "parent".
	 * If there is already an instance connected to this instance through role "parent", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param parent the instance to connect
	 */
	public void setParent(PDTransaction parent) 
;	/**
	 * Returns the instance connected to this instance through the role child.
	 * @return the connected instance
	 */
	public PDTransaction getChild();

	/**
	 * Returns the instance(s) connected to this instance through the role child.
	 * @return the connected instance(s)
	 */
	 public Collection<PDTransaction> getChilds();

   /**
	 * Connects this instance to the given instance using role "child".
	 * If the given instance is null, nothing happens.
	 * @param child the instance to connect
	 */
	public void addChild(GUID child);
	/**
	 * Connects this instance to the given instance using role "child".
	 * If the given instance is null, nothing happens.
	 * @param child the instance to connect
	 */
	public void addChild(PDTransaction child);

	/**
	 * Connects this instance to the given instance using role "child".
	 * If the given collection of instances is null, nothing happens.
	 * @param child the Collection of instances to connect
	 */
	public void addChilds(Collection<PDTransaction> childs);

	/**
	 * Removes the link from this instance through role "child".
	 */
	public void removeChild();

	/**
	 * Removes the link from this instance through role "child" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeChild(PDTransaction child);

	/**
	 * Removes the links from this instance through role "child" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeChilds(Collection<PDTransaction> childs);

	/**
	 * Connects this instance to the given instance using role "child".
	 * If there is already an instance connected to this instance through role "child", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param child the instance to connect
	 */
	public void setChild(GUID child) ;
	/**
	 * Connects this instance to the given instance using role "child".
	 * If there is already an instance connected to this instance through role "child", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param child the instance to connect
	 */
	public void setChild(PDTransaction child) 
;}
