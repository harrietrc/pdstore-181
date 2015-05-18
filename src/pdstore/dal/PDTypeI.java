package pdstore.dal;

import java.util.Collection;
import pdstore.GUID;

/**
 *Interface for the Data access class of type "Type" in memory.
 * @author PDGen
 */
public interface PDTypeI{

	/**
	 * Returns the instance connected to this instance through the role applicable operation.
	 * @return the connected instance
	 */
	public PDOperation getApplicableOperation();

	/**
	 * Returns the instance(s) connected to this instance through the role applicable operation.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOperation> getApplicableOperations();

   /**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If the given instance is null, nothing happens.
	 * @param applicableOperation the instance to connect
	 */
	public void addApplicableOperation(GUID applicableOperation);
	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If the given instance is null, nothing happens.
	 * @param applicableOperation the instance to connect
	 */
	public void addApplicableOperation(PDOperation applicableOperation);

	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If the given collection of instances is null, nothing happens.
	 * @param applicableOperation the Collection of instances to connect
	 */
	public void addApplicableOperations(Collection<PDOperation> applicableOperations);

	/**
	 * Removes the link from this instance through role "applicable operation".
	 */
	public void removeApplicableOperation();

	/**
	 * Removes the link from this instance through role "applicable operation" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeApplicableOperation(PDOperation applicableOperation);

	/**
	 * Removes the links from this instance through role "applicable operation" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeApplicableOperations(Collection<PDOperation> applicableOperations);

	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If there is already an instance connected to this instance through role "applicable operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param applicableOperation the instance to connect
	 */
	public void setApplicableOperation(GUID applicableOperation) ;
	/**
	 * Connects this instance to the given instance using role "applicable operation".
	 * If there is already an instance connected to this instance through role "applicable operation", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param applicableOperation the instance to connect
	 */
	public void setApplicableOperation(PDOperation applicableOperation) 
;	/**
	 * Returns the instance connected to this instance through the role has instance.
	 * @return the connected instance
	 */
	public PDInstance getHasInstance();

	/**
	 * Returns the instance(s) connected to this instance through the role has instance.
	 * @return the connected instance(s)
	 */
	 public Collection<PDInstance> getHasInstances();

   /**
	 * Connects this instance to the given instance using role "has instance".
	 * If the given instance is null, nothing happens.
	 * @param hasInstance the instance to connect
	 */
	public void addHasInstance(GUID hasInstance);
	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If the given instance is null, nothing happens.
	 * @param hasInstance the instance to connect
	 */
	public void addHasInstance(PDInstance hasInstance);

	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If the given collection of instances is null, nothing happens.
	 * @param hasInstance the Collection of instances to connect
	 */
	public void addHasInstances(Collection<PDInstance> hasInstances);

	/**
	 * Removes the link from this instance through role "has instance".
	 */
	public void removeHasInstance();

	/**
	 * Removes the link from this instance through role "has instance" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHasInstance(PDInstance hasInstance);

	/**
	 * Removes the links from this instance through role "has instance" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeHasInstances(Collection<PDInstance> hasInstances);

	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If there is already an instance connected to this instance through role "has instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param hasInstance the instance to connect
	 */
	public void setHasInstance(GUID hasInstance) ;
	/**
	 * Connects this instance to the given instance using role "has instance".
	 * If there is already an instance connected to this instance through role "has instance", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param hasInstance the instance to connect
	 */
	public void setHasInstance(PDInstance hasInstance) 
;	/**
	 * Returns the instance connected to this instance through the role model.
	 * @return the connected instance
	 */
	public PDModel getModel();

	/**
	 * Returns the instance(s) connected to this instance through the role model.
	 * @return the connected instance(s)
	 */
	 public Collection<PDModel> getModels();

   /**
	 * Connects this instance to the given instance using role "model".
	 * If the given instance is null, nothing happens.
	 * @param model the instance to connect
	 */
	public void addModel(GUID model);
	/**
	 * Connects this instance to the given instance using role "model".
	 * If the given instance is null, nothing happens.
	 * @param model the instance to connect
	 */
	public void addModel(PDModel model);

	/**
	 * Connects this instance to the given instance using role "model".
	 * If the given collection of instances is null, nothing happens.
	 * @param model the Collection of instances to connect
	 */
	public void addModels(Collection<PDModel> models);

	/**
	 * Removes the link from this instance through role "model".
	 */
	public void removeModel();

	/**
	 * Removes the link from this instance through role "model" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeModel(PDModel model);

	/**
	 * Removes the links from this instance through role "model" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeModels(Collection<PDModel> models);

	/**
	 * Connects this instance to the given instance using role "model".
	 * If there is already an instance connected to this instance through role "model", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param model the instance to connect
	 */
	public void setModel(GUID model) ;
	/**
	 * Connects this instance to the given instance using role "model".
	 * If there is already an instance connected to this instance through role "model", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param model the instance to connect
	 */
	public void setModel(PDModel model) 
;	/**
	 * Returns the instance connected to this instance through the role owned role.
	 * @return the connected instance
	 */
	public PDRole getOwnedRole();

	/**
	 * Returns the instance(s) connected to this instance through the role owned role.
	 * @return the connected instance(s)
	 */
	 public Collection<PDRole> getOwnedRoles();

   /**
	 * Connects this instance to the given instance using role "owned role".
	 * If the given instance is null, nothing happens.
	 * @param ownedRole the instance to connect
	 */
	public void addOwnedRole(GUID ownedRole);
	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If the given instance is null, nothing happens.
	 * @param ownedRole the instance to connect
	 */
	public void addOwnedRole(PDRole ownedRole);

	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If the given collection of instances is null, nothing happens.
	 * @param ownedRole the Collection of instances to connect
	 */
	public void addOwnedRoles(Collection<PDRole> ownedRoles);

	/**
	 * Removes the link from this instance through role "owned role".
	 */
	public void removeOwnedRole();

	/**
	 * Removes the link from this instance through role "owned role" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOwnedRole(PDRole ownedRole);

	/**
	 * Removes the links from this instance through role "owned role" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOwnedRoles(Collection<PDRole> ownedRoles);

	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If there is already an instance connected to this instance through role "owned role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param ownedRole the instance to connect
	 */
	public void setOwnedRole(GUID ownedRole) ;
	/**
	 * Connects this instance to the given instance using role "owned role".
	 * If there is already an instance connected to this instance through role "owned role", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param ownedRole the instance to connect
	 */
	public void setOwnedRole(PDRole ownedRole) 
;	/**
	 * Returns the instance connected to this instance through the role subtype.
	 * @return the connected instance
	 */
	public PDType getSubtype();

	/**
	 * Returns the instance(s) connected to this instance through the role subtype.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getSubtypes();

   /**
	 * Connects this instance to the given instance using role "subtype".
	 * If the given instance is null, nothing happens.
	 * @param subtype the instance to connect
	 */
	public void addSubtype(GUID subtype);
	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If the given instance is null, nothing happens.
	 * @param subtype the instance to connect
	 */
	public void addSubtype(PDType subtype);

	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If the given collection of instances is null, nothing happens.
	 * @param subtype the Collection of instances to connect
	 */
	public void addSubtypes(Collection<PDType> subtypes);

	/**
	 * Removes the link from this instance through role "subtype".
	 */
	public void removeSubtype();

	/**
	 * Removes the link from this instance through role "subtype" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSubtype(PDType subtype);

	/**
	 * Removes the links from this instance through role "subtype" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSubtypes(Collection<PDType> subtypes);

	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If there is already an instance connected to this instance through role "subtype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subtype the instance to connect
	 */
	public void setSubtype(GUID subtype) ;
	/**
	 * Connects this instance to the given instance using role "subtype".
	 * If there is already an instance connected to this instance through role "subtype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subtype the instance to connect
	 */
	public void setSubtype(PDType subtype) 
;	/**
	 * Returns the instance connected to this instance through the role supertype.
	 * @return the connected instance
	 */
	public PDType getSupertype();

	/**
	 * Returns the instance(s) connected to this instance through the role supertype.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getSupertypes();

   /**
	 * Connects this instance to the given instance using role "supertype".
	 * If the given instance is null, nothing happens.
	 * @param supertype the instance to connect
	 */
	public void addSupertype(GUID supertype);
	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If the given instance is null, nothing happens.
	 * @param supertype the instance to connect
	 */
	public void addSupertype(PDType supertype);

	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If the given collection of instances is null, nothing happens.
	 * @param supertype the Collection of instances to connect
	 */
	public void addSupertypes(Collection<PDType> supertypes);

	/**
	 * Removes the link from this instance through role "supertype".
	 */
	public void removeSupertype();

	/**
	 * Removes the link from this instance through role "supertype" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSupertype(PDType supertype);

	/**
	 * Removes the links from this instance through role "supertype" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSupertypes(Collection<PDType> supertypes);

	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If there is already an instance connected to this instance through role "supertype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param supertype the instance to connect
	 */
	public void setSupertype(GUID supertype) ;
	/**
	 * Connects this instance to the given instance using role "supertype".
	 * If there is already an instance connected to this instance through role "supertype", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param supertype the instance to connect
	 */
	public void setSupertype(PDType supertype) 
;	/**
	 * Returns the instance connected to this instance through the role is primitive.
	 * @return the connected instance
	 */
	public Boolean getIsPrimitive();

	/**
	 * Returns the instance(s) connected to this instance through the role is primitive.
	 * @return the connected instance(s)
	 */
	 public Collection<Boolean> getIsPrimitives();

   /**
	 * Connects this instance to the given instance using role "is primitive".
	 * If the given instance is null, nothing happens.
	 * @param isPrimitive the instance to connect
	 */
	public void addIsPrimitive(Boolean isPrimitive);
	/**
	 * Connects this instance to the given instances using role "is primitive".
	 * If the given collection of instances is null, nothing happens.
	 * @param isPrimitive the Collection of instances to connect
	 */
	public void addIsPrimitives(Collection<Boolean> isPrimitives);

	/**
	 * Removes the link from this instance through role "is primitive".
	 */
	public void removeIsPrimitive();

	/**
	 * Removes the link from this instance through role "is primitive" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeIsPrimitive(Boolean isPrimitive);

	/**
	 * Connects this instance to the given instance using role "is primitive".
	 * If there is already an instance connected to this instance through role "is primitive", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param isPrimitive the instance to connect
	 */
	public void setIsPrimitive(Boolean isPrimitive) ;
}
