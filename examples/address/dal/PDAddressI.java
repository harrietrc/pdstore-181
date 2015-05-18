package address.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Address" in memory.
 * @author PDGen
 */
public interface PDAddressI{

	/**
	 * Returns the instance connected to this instance through the role houseNo.
	 * @return the connected instance
	 */
	public Long getHouseNo();

	/**
	 * Returns the instance(s) connected to this instance through the role houseNo.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getHouseNos();

   /**
	 * Connects this instance to the given instance using role "houseNo".
	 * If the given instance is null, nothing happens.
	 * @param houseNo the instance to connect
	 */
	public void addHouseNo(Long houseNo);
	/**
	 * Connects this instance to the given instances using role "houseNo".
	 * If the given collection of instances is null, nothing happens.
	 * @param houseNo the Collection of instances to connect
	 */
	public void addHouseNos(Collection<Long> houseNos);

	/**
	 * Removes the link from this instance through role "houseNo".
	 */
	public void removeHouseNo();

	/**
	 * Removes the link from this instance through role "houseNo" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHouseNo(Long houseNo);

	/**
	 * Connects this instance to the given instance using role "houseNo".
	 * If there is already an instance connected to this instance through role "houseNo", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param houseNo the instance to connect
	 */
	public void setHouseNo(Long houseNo) ;
	/**
	 * Returns the instance connected to this instance through the role streetName.
	 * @return the connected instance
	 */
	public String getStreetName();

	/**
	 * Returns the instance(s) connected to this instance through the role streetName.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getStreetNames();

   /**
	 * Connects this instance to the given instance using role "streetName".
	 * If the given instance is null, nothing happens.
	 * @param streetName the instance to connect
	 */
	public void addStreetName(String streetName);
	/**
	 * Connects this instance to the given instances using role "streetName".
	 * If the given collection of instances is null, nothing happens.
	 * @param streetName the Collection of instances to connect
	 */
	public void addStreetNames(Collection<String> streetNames);

	/**
	 * Removes the link from this instance through role "streetName".
	 */
	public void removeStreetName();

	/**
	 * Removes the link from this instance through role "streetName" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeStreetName(String streetName);

	/**
	 * Connects this instance to the given instance using role "streetName".
	 * If there is already an instance connected to this instance through role "streetName", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param streetName the instance to connect
	 */
	public void setStreetName(String streetName) ;
	/**
	 * Returns the instance connected to this instance through the role customer.
	 * @return the connected instance
	 */
	public PDCustomer getCustomer();

	/**
	 * Returns the instance(s) connected to this instance through the role customer.
	 * @return the connected instance(s)
	 */
	 public Collection<PDCustomer> getCustomers();

   /**
	 * Connects this instance to the given instance using role "customer".
	 * If the given instance is null, nothing happens.
	 * @param customer the instance to connect
	 */
	public void addCustomer(GUID customer);
	/**
	 * Connects this instance to the given instance using role "customer".
	 * If the given instance is null, nothing happens.
	 * @param customer the instance to connect
	 */
	public void addCustomer(PDCustomer customer);

	/**
	 * Connects this instance to the given instance using role "customer".
	 * If the given collection of instances is null, nothing happens.
	 * @param customer the Collection of instances to connect
	 */
	public void addCustomers(Collection<PDCustomer> customers);

	/**
	 * Removes the link from this instance through role "customer".
	 */
	public void removeCustomer();

	/**
	 * Removes the link from this instance through role "customer" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeCustomer(PDCustomer customer);

	/**
	 * Removes the links from this instance through role "customer" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeCustomers(Collection<PDCustomer> customers);

	/**
	 * Connects this instance to the given instance using role "customer".
	 * If there is already an instance connected to this instance through role "customer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param customer the instance to connect
	 */
	public void setCustomer(GUID customer) ;
	/**
	 * Connects this instance to the given instance using role "customer".
	 * If there is already an instance connected to this instance through role "customer", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param customer the instance to connect
	 */
	public void setCustomer(PDCustomer customer) 
;}
