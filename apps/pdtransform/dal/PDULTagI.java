package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "UL tag" in memory.
 * @author PDGen
 */
public interface PDULTagI{

	/**
	 * Returns the instance connected to this instance through the role child.
	 * @return the connected instance
	 */
	public PDLITag getChild();

	/**
	 * Returns the instance(s) connected to this instance through the role child.
	 * @return the connected instance(s)
	 */
	 public Collection<PDLITag> getChilds();

   /**
	 * Connects this instance to the given instance using role "child".
	 * If the given instance is null, nothing happens.
	 * @param child the instance to connect
	 */
	public void addChild(GUID child);
	/**
	 * Connects this instance to the given instance using role "child".
	 * If the given instance is null, nothing happens.
	 * @param child the instance to connect
	 */
	public void addChild(PDLITag child);

	/**
	 * Connects this instance to the given instance using role "child".
	 * If the given collection of instances is null, nothing happens.
	 * @param child the Collection of instances to connect
	 */
	public void addChilds(Collection<PDLITag> childs);

	/**
	 * Removes the link from this instance through role "child".
	 */
	public void removeChild();

	/**
	 * Removes the link from this instance through role "child" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeChild(PDLITag child);

	/**
	 * Removes the links from this instance through role "child" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeChilds(Collection<PDLITag> childs);

	/**
	 * Connects this instance to the given instance using role "child".
	 * If there is already an instance connected to this instance through role "child", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param child the instance to connect
	 */
	public void setChild(GUID child) ;
	/**
	 * Connects this instance to the given instance using role "child".
	 * If there is already an instance connected to this instance through role "child", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param child the instance to connect
	 */
	public void setChild(PDLITag child) 
;}
