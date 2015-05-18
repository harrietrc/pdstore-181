package pdstore.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "Change" in memory.
 * @author PDGen
 */
public interface PDChangeI{

	/**
	 * Returns the instance connected to this instance through the role instance2.
	 * @return the connected instance
	 */
	public PDInstance getInstance2();

	/**
	 * Returns the instance(s) connected to this instance through the role instance2.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getInstance2s();

   /**
	 * Connects this instance to the given instance using role "instance2".
	 * If the given instance is null, nothing happens.
	 * @param instance2 the instance to connect
	 */
	public void addInstance2(GUID instance2);
	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If the given instance is null, nothing happens.
	 * @param instance2 the instance to connect
	 */
	public void addInstance2(PDInstance instance2);

	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If the given collection of instances is null, nothing happens.
	 * @param instance2 the Collection of instances to connect
	 */
	public void addInstance2s(Collection<PDInstance> instance2s);

	/**
	 * Removes the link from this instance through role "instance2".
	 */
	public void removeInstance2();

	/**
	 * Removes the link from this instance through role "instance2" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInstance2(PDInstance instance2);

	/**
	 * Removes the links from this instance through role "instance2" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInstance2s(Collection<PDInstance> instance2s);

	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If there is already an instance connected to this instance through role "instance2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance2 the instance to connect
	 */
	public void setInstance2(GUID instance2) ;
	/**
	 * Connects this instance to the given instance using role "instance2".
	 * If there is already an instance connected to this instance through role "instance2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance2 the instance to connect
	 */
	public void setInstance2(PDInstance instance2) 
;	/**
	 * Returns the instance connected to this instance through the role role2.
	 * @return the connected instance
	 */
	public PDRole getRole2();

	/**
	 * Returns the instance(s) connected to this instance through the role role2.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getRole2s();

   /**
	 * Connects this instance to the given instance using role "role2".
	 * If the given instance is null, nothing happens.
	 * @param role2 the instance to connect
	 */
	public void addRole2(GUID role2);
	/**
	 * Connects this instance to the given instance using role "role2".
	 * If the given instance is null, nothing happens.
	 * @param role2 the instance to connect
	 */
	public void addRole2(PDRole role2);

	/**
	 * Connects this instance to the given instance using role "role2".
	 * If the given collection of instances is null, nothing happens.
	 * @param role2 the Collection of instances to connect
	 */
	public void addRole2s(Collection<PDRole> role2s);

	/**
	 * Removes the link from this instance through role "role2".
	 */
	public void removeRole2();

	/**
	 * Removes the link from this instance through role "role2" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeRole2(PDRole role2);

	/**
	 * Removes the links from this instance through role "role2" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeRole2s(Collection<PDRole> role2s);

	/**
	 * Connects this instance to the given instance using role "role2".
	 * If there is already an instance connected to this instance through role "role2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param role2 the instance to connect
	 */
	public void setRole2(GUID role2) ;
	/**
	 * Connects this instance to the given instance using role "role2".
	 * If there is already an instance connected to this instance through role "role2", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param role2 the instance to connect
	 */
	public void setRole2(PDRole role2) 
;	/**
	 * Returns the instance connected to this instance through the role instance1.
	 * @return the connected instance
	 */
	public PDInstance getInstance1();

	/**
	 * Returns the instance(s) connected to this instance through the role instance1.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getInstance1s();

   /**
	 * Connects this instance to the given instance using role "instance1".
	 * If the given instance is null, nothing happens.
	 * @param instance1 the instance to connect
	 */
	public void addInstance1(GUID instance1);
	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If the given instance is null, nothing happens.
	 * @param instance1 the instance to connect
	 */
	public void addInstance1(PDInstance instance1);

	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If the given collection of instances is null, nothing happens.
	 * @param instance1 the Collection of instances to connect
	 */
	public void addInstance1s(Collection<PDInstance> instance1s);

	/**
	 * Removes the link from this instance through role "instance1".
	 */
	public void removeInstance1();

	/**
	 * Removes the link from this instance through role "instance1" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInstance1(PDInstance instance1);

	/**
	 * Removes the links from this instance through role "instance1" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInstance1s(Collection<PDInstance> instance1s);

	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If there is already an instance connected to this instance through role "instance1", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance1 the instance to connect
	 */
	public void setInstance1(GUID instance1) ;
	/**
	 * Connects this instance to the given instance using role "instance1".
	 * If there is already an instance connected to this instance through role "instance1", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param instance1 the instance to connect
	 */
	public void setInstance1(PDInstance instance1) 
;	/**
	 * Returns the instance connected to this instance through the role change type.
	 * @return the connected instance
	 */
	public String getChangeType();

	/**
	 * Returns the instance(s) connected to this instance through the role change type.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getChangeTypes();

   /**
	 * Connects this instance to the given instance using role "change type".
	 * If the given instance is null, nothing happens.
	 * @param changeType the instance to connect
	 */
	public void addChangeType(String changeType);
	/**
	 * Connects this instance to the given instances using role "change type".
	 * If the given collection of instances is null, nothing happens.
	 * @param changeType the Collection of instances to connect
	 */
	public void addChangeTypes(Collection<String> changeTypes);

	/**
	 * Removes the link from this instance through role "change type".
	 */
	public void removeChangeType();

	/**
	 * Removes the link from this instance through role "change type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeChangeType(String changeType);

	/**
	 * Connects this instance to the given instance using role "change type".
	 * If there is already an instance connected to this instance through role "change type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param changeType the instance to connect
	 */
	public void setChangeType(String changeType) ;
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
