package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Contact" in memory.
 * @author PDGen
 */
public interface PDContactI{

	/**
	 * Returns the instance connected to this instance through the role address.
	 * @return the connected instance
	 */
	public String getAddress();

	/**
	 * Returns the instance(s) connected to this instance through the role address.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getAddresss();

   /**
	 * Connects this instance to the given instance using role "address".
	 * If the given instance is null, nothing happens.
	 * @param address the instance to connect
	 */
	public void addAddress(String address);
	/**
	 * Connects this instance to the given instances using role "address".
	 * If the given collection of instances is null, nothing happens.
	 * @param address the Collection of instances to connect
	 */
	public void addAddresss(Collection<String> addresss);

	/**
	 * Removes the link from this instance through role "address".
	 */
	public void removeAddress();

	/**
	 * Removes the link from this instance through role "address" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeAddress(String address);

	/**
	 * Connects this instance to the given instance using role "address".
	 * If there is already an instance connected to this instance through role "address", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param address the instance to connect
	 */
	public void setAddress(String address) ;
	/**
	 * Returns the instance connected to this instance through the role contacts name.
	 * @return the connected instance
	 */
	public String getContactsName();

	/**
	 * Returns the instance(s) connected to this instance through the role contacts name.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getContactsNames();

   /**
	 * Connects this instance to the given instance using role "contacts name".
	 * If the given instance is null, nothing happens.
	 * @param contactsName the instance to connect
	 */
	public void addContactsName(String contactsName);
	/**
	 * Connects this instance to the given instances using role "contacts name".
	 * If the given collection of instances is null, nothing happens.
	 * @param contactsName the Collection of instances to connect
	 */
	public void addContactsNames(Collection<String> contactsNames);

	/**
	 * Removes the link from this instance through role "contacts name".
	 */
	public void removeContactsName();

	/**
	 * Removes the link from this instance through role "contacts name" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeContactsName(String contactsName);

	/**
	 * Connects this instance to the given instance using role "contacts name".
	 * If there is already an instance connected to this instance through role "contacts name", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param contactsName the instance to connect
	 */
	public void setContactsName(String contactsName) ;
}
