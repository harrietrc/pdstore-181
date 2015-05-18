package pdstore.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "Role" in memory.
 * @author PDGen
 */
public interface PDRoleI{

	/**
	 * Returns the instance connected to this instance through the role user.
	 * @return the connected instance
	 */
	public PDInstance getUser();

	/**
	 * Returns the instance(s) connected to this instance through the role user.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getUsers();

   /**
	 * Connects this instance to the given instance using role "user".
	 * If the given instance is null, nothing happens.
	 * @param user the instance to connect
	 */
	public void addUser(GUID user);
	/**
	 * Connects this instance to the given instance using role "user".
	 * If the given instance is null, nothing happens.
	 * @param user the instance to connect
	 */
	public void addUser(PDInstance user);

	/**
	 * Connects this instance to the given instance using role "user".
	 * If the given collection of instances is null, nothing happens.
	 * @param user the Collection of instances to connect
	 */
	public void addUsers(Collection<PDInstance> users);

	/**
	 * Removes the link from this instance through role "user".
	 */
	public void removeUser();

	/**
	 * Removes the link from this instance through role "user" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeUser(PDInstance user);

	/**
	 * Removes the links from this instance through role "user" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeUsers(Collection<PDInstance> users);

	/**
	 * Connects this instance to the given instance using role "user".
	 * If there is already an instance connected to this instance through role "user", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param user the instance to connect
	 */
	public void setUser(GUID user) ;
	/**
	 * Connects this instance to the given instance using role "user".
	 * If there is already an instance connected to this instance through role "user", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param user the instance to connect
	 */
	public void setUser(PDInstance user) 
;	/**
	 * Returns the instance connected to this instance through the role max mult.
	 * @return the connected instance
	 */
	public Long getMaxMult();

	/**
	 * Returns the instance(s) connected to this instance through the role max mult.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getMaxMults();

   /**
	 * Connects this instance to the given instance using role "max mult".
	 * If the given instance is null, nothing happens.
	 * @param maxMult the instance to connect
	 */
	public void addMaxMult(Long maxMult);
	/**
	 * Connects this instance to the given instances using role "max mult".
	 * If the given collection of instances is null, nothing happens.
	 * @param maxMult the Collection of instances to connect
	 */
	public void addMaxMults(Collection<Long> maxMults);

	/**
	 * Removes the link from this instance through role "max mult".
	 */
	public void removeMaxMult();

	/**
	 * Removes the link from this instance through role "max mult" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeMaxMult(Long maxMult);

	/**
	 * Connects this instance to the given instance using role "max mult".
	 * If there is already an instance connected to this instance through role "max mult", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param maxMult the instance to connect
	 */
	public void setMaxMult(Long maxMult) ;
	/**
	 * Returns the instance connected to this instance through the role min mult.
	 * @return the connected instance
	 */
	public Long getMinMult();

	/**
	 * Returns the instance(s) connected to this instance through the role min mult.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getMinMults();

   /**
	 * Connects this instance to the given instance using role "min mult".
	 * If the given instance is null, nothing happens.
	 * @param minMult the instance to connect
	 */
	public void addMinMult(Long minMult);
	/**
	 * Connects this instance to the given instances using role "min mult".
	 * If the given collection of instances is null, nothing happens.
	 * @param minMult the Collection of instances to connect
	 */
	public void addMinMults(Collection<Long> minMults);

	/**
	 * Removes the link from this instance through role "min mult".
	 */
	public void removeMinMult();

	/**
	 * Removes the link from this instance through role "min mult" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeMinMult(Long minMult);

	/**
	 * Connects this instance to the given instance using role "min mult".
	 * If there is already an instance connected to this instance through role "min mult", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param minMult the instance to connect
	 */
	public void setMinMult(Long minMult) ;
	/**
	 * Returns the instance connected to this instance through the role partner.
	 * @return the connected instance
	 */
	public PDRole getPartner();

	/**
	 * Returns the instance(s) connected to this instance through the role partner.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getPartners();

   /**
	 * Connects this instance to the given instance using role "partner".
	 * If the given instance is null, nothing happens.
	 * @param partner the instance to connect
	 */
	public void addPartner(GUID partner);
	/**
	 * Connects this instance to the given instance using role "partner".
	 * If the given instance is null, nothing happens.
	 * @param partner the instance to connect
	 */
	public void addPartner(PDRole partner);

	/**
	 * Connects this instance to the given instance using role "partner".
	 * If the given collection of instances is null, nothing happens.
	 * @param partner the Collection of instances to connect
	 */
	public void addPartners(Collection<PDRole> partners);

	/**
	 * Removes the link from this instance through role "partner".
	 */
	public void removePartner();

	/**
	 * Removes the link from this instance through role "partner" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePartner(PDRole partner);

	/**
	 * Removes the links from this instance through role "partner" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removePartners(Collection<PDRole> partners);

	/**
	 * Connects this instance to the given instance using role "partner".
	 * If there is already an instance connected to this instance through role "partner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param partner the instance to connect
	 */
	public void setPartner(GUID partner) ;
	/**
	 * Connects this instance to the given instance using role "partner".
	 * If there is already an instance connected to this instance through role "partner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param partner the instance to connect
	 */
	public void setPartner(PDRole partner) 
;	/**
	 * Returns the instance connected to this instance through the role owner.
	 * @return the connected instance
	 */
	public PDType getOwner();

	/**
	 * Returns the instance(s) connected to this instance through the role owner.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getOwners();

   /**
	 * Connects this instance to the given instance using role "owner".
	 * If the given instance is null, nothing happens.
	 * @param owner the instance to connect
	 */
	public void addOwner(GUID owner);
	/**
	 * Connects this instance to the given instance using role "owner".
	 * If the given instance is null, nothing happens.
	 * @param owner the instance to connect
	 */
	public void addOwner(PDType owner);

	/**
	 * Connects this instance to the given instance using role "owner".
	 * If the given collection of instances is null, nothing happens.
	 * @param owner the Collection of instances to connect
	 */
	public void addOwners(Collection<PDType> owners);

	/**
	 * Removes the link from this instance through role "owner".
	 */
	public void removeOwner();

	/**
	 * Removes the link from this instance through role "owner" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOwner(PDType owner);

	/**
	 * Removes the links from this instance through role "owner" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOwners(Collection<PDType> owners);

	/**
	 * Connects this instance to the given instance using role "owner".
	 * If there is already an instance connected to this instance through role "owner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param owner the instance to connect
	 */
	public void setOwner(GUID owner) ;
	/**
	 * Connects this instance to the given instance using role "owner".
	 * If there is already an instance connected to this instance through role "owner", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param owner the instance to connect
	 */
	public void setOwner(PDType owner) 
;}
