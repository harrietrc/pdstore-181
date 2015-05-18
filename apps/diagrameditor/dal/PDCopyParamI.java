package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "CopyParam" in memory.
 * @author PDGen
 */
public interface PDCopyParamI{

	/**
	 * Returns the instance connected to this instance through the role copy.
	 * @return the connected instance
	 */
	public PDShape getCopy();

	/**
	 * Returns the instance(s) connected to this instance through the role copy.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getCopys();

   /**
	 * Connects this instance to the given instance using role "copy".
	 * If the given instance is null, nothing happens.
	 * @param copy the instance to connect
	 */
	public void addCopy(GUID copy);
	/**
	 * Connects this instance to the given instance using role "copy".
	 * If the given instance is null, nothing happens.
	 * @param copy the instance to connect
	 */
	public void addCopy(PDShape copy);

	/**
	 * Connects this instance to the given instance using role "copy".
	 * If the given collection of instances is null, nothing happens.
	 * @param copy the Collection of instances to connect
	 */
	public void addCopys(Collection<PDShape> copys);

	/**
	 * Removes the link from this instance through role "copy".
	 */
	public void removeCopy();

	/**
	 * Removes the link from this instance through role "copy" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeCopy(PDShape copy);

	/**
	 * Removes the links from this instance through role "copy" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeCopys(Collection<PDShape> copys);

	/**
	 * Connects this instance to the given instance using role "copy".
	 * If there is already an instance connected to this instance through role "copy", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param copy the instance to connect
	 */
	public void setCopy(GUID copy) ;
	/**
	 * Connects this instance to the given instance using role "copy".
	 * If there is already an instance connected to this instance through role "copy", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param copy the instance to connect
	 */
	public void setCopy(PDShape copy) 
;}
