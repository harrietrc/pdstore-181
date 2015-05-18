package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "LI tag" in memory.
 * @author PDGen
 */
public interface PDLITagI{

	/**
	 * Returns the instance connected to this instance through the role text.
	 * @return the connected instance
	 */
	public PDText getText();

	/**
	 * Returns the instance(s) connected to this instance through the role text.
	 * @return the connected instance(s)
	 */
	 public Collection<PDText> getTexts();

   /**
	 * Connects this instance to the given instance using role "text".
	 * If the given instance is null, nothing happens.
	 * @param text the instance to connect
	 */
	public void addText(GUID text);
	/**
	 * Connects this instance to the given instance using role "text".
	 * If the given instance is null, nothing happens.
	 * @param text the instance to connect
	 */
	public void addText(PDText text);

	/**
	 * Connects this instance to the given instance using role "text".
	 * If the given collection of instances is null, nothing happens.
	 * @param text the Collection of instances to connect
	 */
	public void addTexts(Collection<PDText> texts);

	/**
	 * Removes the link from this instance through role "text".
	 */
	public void removeText();

	/**
	 * Removes the link from this instance through role "text" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeText(PDText text);

	/**
	 * Removes the links from this instance through role "text" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTexts(Collection<PDText> texts);

	/**
	 * Connects this instance to the given instance using role "text".
	 * If there is already an instance connected to this instance through role "text", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param text the instance to connect
	 */
	public void setText(GUID text) ;
	/**
	 * Connects this instance to the given instance using role "text".
	 * If there is already an instance connected to this instance through role "text", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param text the instance to connect
	 */
	public void setText(PDText text) 
;}
