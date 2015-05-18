package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Text" in memory.
 * @author PDGen
 */
public interface PDTextI{

	/**
	 * Returns the instance connected to this instance through the role content.
	 * @return the connected instance
	 */
	public String getContent();

	/**
	 * Returns the instance(s) connected to this instance through the role content.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getContents();

   /**
	 * Connects this instance to the given instance using role "content".
	 * If the given instance is null, nothing happens.
	 * @param content the instance to connect
	 */
	public void addContent(String content);
	/**
	 * Connects this instance to the given instances using role "content".
	 * If the given collection of instances is null, nothing happens.
	 * @param content the Collection of instances to connect
	 */
	public void addContents(Collection<String> contents);

	/**
	 * Removes the link from this instance through role "content".
	 */
	public void removeContent();

	/**
	 * Removes the link from this instance through role "content" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeContent(String content);

	/**
	 * Connects this instance to the given instance using role "content".
	 * If there is already an instance connected to this instance through role "content", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param content the instance to connect
	 */
	public void setContent(String content) ;
}
