package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Address Book" in memory.
 * @author PDGen
 */
public interface PDAddressBookI{

	/**
	 * Returns the instance connected to this instance through the role label.
	 * @return the connected instance
	 */
	public String getLabel();

	/**
	 * Returns the instance(s) connected to this instance through the role label.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getLabels();

   /**
	 * Connects this instance to the given instance using role "label".
	 * If the given instance is null, nothing happens.
	 * @param label the instance to connect
	 */
	public void addLabel(String label);
	/**
	 * Connects this instance to the given instances using role "label".
	 * If the given collection of instances is null, nothing happens.
	 * @param label the Collection of instances to connect
	 */
	public void addLabels(Collection<String> labels);

	/**
	 * Removes the link from this instance through role "label".
	 */
	public void removeLabel();

	/**
	 * Removes the link from this instance through role "label" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeLabel(String label);

	/**
	 * Connects this instance to the given instance using role "label".
	 * If there is already an instance connected to this instance through role "label", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param label the instance to connect
	 */
	public void setLabel(String label) ;
	/**
	 * Returns the instance connected to this instance through the role sub address book.
	 * @return the connected instance
	 */
	public PDAddressBook getSubAddressBook();

	/**
	 * Returns the instance(s) connected to this instance through the role sub address book.
	 * @return the connected instance(s)
	 */
	 public Collection<PDAddressBook> getSubAddressBooks();

   /**
	 * Connects this instance to the given instance using role "sub address book".
	 * If the given instance is null, nothing happens.
	 * @param subAddressBook the instance to connect
	 */
	public void addSubAddressBook(GUID subAddressBook);
	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If the given instance is null, nothing happens.
	 * @param subAddressBook the instance to connect
	 */
	public void addSubAddressBook(PDAddressBook subAddressBook);

	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If the given collection of instances is null, nothing happens.
	 * @param subAddressBook the Collection of instances to connect
	 */
	public void addSubAddressBooks(Collection<PDAddressBook> subAddressBooks);

	/**
	 * Removes the link from this instance through role "sub address book".
	 */
	public void removeSubAddressBook();

	/**
	 * Removes the link from this instance through role "sub address book" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeSubAddressBook(PDAddressBook subAddressBook);

	/**
	 * Removes the links from this instance through role "sub address book" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeSubAddressBooks(Collection<PDAddressBook> subAddressBooks);

	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If there is already an instance connected to this instance through role "sub address book", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subAddressBook the instance to connect
	 */
	public void setSubAddressBook(GUID subAddressBook) ;
	/**
	 * Connects this instance to the given instance using role "sub address book".
	 * If there is already an instance connected to this instance through role "sub address book", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param subAddressBook the instance to connect
	 */
	public void setSubAddressBook(PDAddressBook subAddressBook) 
;	/**
	 * Returns the instance connected to this instance through the role contact.
	 * @return the connected instance
	 */
	public PDContact getContact();

	/**
	 * Returns the instance(s) connected to this instance through the role contact.
	 * @return the connected instance(s)
	 */
	 public Collection<PDContact> getContacts();

   /**
	 * Connects this instance to the given instance using role "contact".
	 * If the given instance is null, nothing happens.
	 * @param contact the instance to connect
	 */
	public void addContact(GUID contact);
	/**
	 * Connects this instance to the given instance using role "contact".
	 * If the given instance is null, nothing happens.
	 * @param contact the instance to connect
	 */
	public void addContact(PDContact contact);

	/**
	 * Connects this instance to the given instance using role "contact".
	 * If the given collection of instances is null, nothing happens.
	 * @param contact the Collection of instances to connect
	 */
	public void addContacts(Collection<PDContact> contacts);

	/**
	 * Removes the link from this instance through role "contact".
	 */
	public void removeContact();

	/**
	 * Removes the link from this instance through role "contact" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeContact(PDContact contact);

	/**
	 * Removes the links from this instance through role "contact" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeContacts(Collection<PDContact> contacts);

	/**
	 * Connects this instance to the given instance using role "contact".
	 * If there is already an instance connected to this instance through role "contact", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param contact the instance to connect
	 */
	public void setContact(GUID contact) ;
	/**
	 * Connects this instance to the given instance using role "contact".
	 * If there is already an instance connected to this instance through role "contact", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param contact the instance to connect
	 */
	public void setContact(PDContact contact) 
;}
