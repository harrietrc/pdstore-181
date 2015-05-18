package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Map" in memory.
 * @author PDGen
 */
public interface PDMapI{

	/**
	 * Returns the instance connected to this instance through the role output role.
	 * @return the connected instance
	 */
	public PDRole getOutputRole();

	/**
	 * Returns the instance(s) connected to this instance through the role output role.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getOutputRoles();

   /**
	 * Connects this instance to the given instance using role "output role".
	 * If the given instance is null, nothing happens.
	 * @param outputRole the instance to connect
	 */
	public void addOutputRole(GUID outputRole);
	/**
	 * Connects this instance to the given instance using role "output role".
	 * If the given instance is null, nothing happens.
	 * @param outputRole the instance to connect
	 */
	public void addOutputRole(PDRole outputRole);

	/**
	 * Connects this instance to the given instance using role "output role".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputRole the Collection of instances to connect
	 */
	public void addOutputRoles(Collection<PDRole> outputRoles);

	/**
	 * Removes the link from this instance through role "output role".
	 */
	public void removeOutputRole();

	/**
	 * Removes the link from this instance through role "output role" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputRole(PDRole outputRole);

	/**
	 * Removes the links from this instance through role "output role" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOutputRoles(Collection<PDRole> outputRoles);

	/**
	 * Connects this instance to the given instance using role "output role".
	 * If there is already an instance connected to this instance through role "output role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputRole the instance to connect
	 */
	public void setOutputRole(GUID outputRole) ;
	/**
	 * Connects this instance to the given instance using role "output role".
	 * If there is already an instance connected to this instance through role "output role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputRole the instance to connect
	 */
	public void setOutputRole(PDRole outputRole) 
;	/**
	 * Returns the instance connected to this instance through the role output instance.
	 * @return the connected instance
	 */
	public GUID getOutputInstance();

	/**
	 * Returns the instance(s) connected to this instance through the role output instance.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getOutputInstances();

   /**
	 * Connects this instance to the given instance using role "output instance".
	 * If the given instance is null, nothing happens.
	 * @param outputInstance the instance to connect
	 */
	public void addOutputInstance(GUID outputInstance);
	/**
	 * Connects this instance to the given instances using role "output instance".
	 * If the given collection of instances is null, nothing happens.
	 * @param outputInstance the Collection of instances to connect
	 */
	public void addOutputInstances(Collection<GUID> outputInstances);

	/**
	 * Removes the link from this instance through role "output instance".
	 */
	public void removeOutputInstance();

	/**
	 * Removes the link from this instance through role "output instance" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOutputInstance(GUID outputInstance);

	/**
	 * Connects this instance to the given instance using role "output instance".
	 * If there is already an instance connected to this instance through role "output instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param outputInstance the instance to connect
	 */
	public void setOutputInstance(GUID outputInstance) ;
	/**
	 * Returns the instance connected to this instance through the role input role.
	 * @return the connected instance
	 */
	public PDRole getInputRole();

	/**
	 * Returns the instance(s) connected to this instance through the role input role.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getInputRoles();

   /**
	 * Connects this instance to the given instance using role "input role".
	 * If the given instance is null, nothing happens.
	 * @param inputRole the instance to connect
	 */
	public void addInputRole(GUID inputRole);
	/**
	 * Connects this instance to the given instance using role "input role".
	 * If the given instance is null, nothing happens.
	 * @param inputRole the instance to connect
	 */
	public void addInputRole(PDRole inputRole);

	/**
	 * Connects this instance to the given instance using role "input role".
	 * If the given collection of instances is null, nothing happens.
	 * @param inputRole the Collection of instances to connect
	 */
	public void addInputRoles(Collection<PDRole> inputRoles);

	/**
	 * Removes the link from this instance through role "input role".
	 */
	public void removeInputRole();

	/**
	 * Removes the link from this instance through role "input role" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeInputRole(PDRole inputRole);

	/**
	 * Removes the links from this instance through role "input role" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeInputRoles(Collection<PDRole> inputRoles);

	/**
	 * Connects this instance to the given instance using role "input role".
	 * If there is already an instance connected to this instance through role "input role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputRole the instance to connect
	 */
	public void setInputRole(GUID inputRole) ;
	/**
	 * Connects this instance to the given instance using role "input role".
	 * If there is already an instance connected to this instance through role "input role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param inputRole the instance to connect
	 */
	public void setInputRole(PDRole inputRole) 
;	/**
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
