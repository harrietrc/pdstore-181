package address.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Customer" in memory.
 * @author PDGen
 */
public interface PDCustomerI{

	/**
	 * Returns the instance connected to this instance through the role address.
	 * @return the connected instance
	 */
	public PDAddress getAddress();

	/**
	 * Returns the instance(s) connected to this instance through the role address.
	 * @return the connected instance(s)
	 */
	 public Collection<PDAddress> getAddresss();

   /**
	 * Connects this instance to the given instance using role "address".
	 * If the given instance is null, nothing happens.
	 * @param address the instance to connect
	 */
	public void addAddress(GUID address);
	/**
	 * Connects this instance to the given instance using role "address".
	 * If the given instance is null, nothing happens.
	 * @param address the instance to connect
	 */
	public void addAddress(PDAddress address);

	/**
	 * Connects this instance to the given instance using role "address".
	 * If the given collection of instances is null, nothing happens.
	 * @param address the Collection of instances to connect
	 */
	public void addAddresss(Collection<PDAddress> addresss);

	/**
	 * Removes the link from this instance through role "address".
	 */
	public void removeAddress();

	/**
	 * Removes the link from this instance through role "address" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeAddress(PDAddress address);

	/**
	 * Removes the links from this instance through role "address" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeAddresss(Collection<PDAddress> addresss);

	/**
	 * Connects this instance to the given instance using role "address".
	 * If there is already an instance connected to this instance through role "address", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param address the instance to connect
	 */
	public void setAddress(GUID address) ;
	/**
	 * Connects this instance to the given instance using role "address".
	 * If there is already an instance connected to this instance through role "address", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param address the instance to connect
	 */
	public void setAddress(PDAddress address) 
;}
