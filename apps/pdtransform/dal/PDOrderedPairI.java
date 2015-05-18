package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Ordered Pair" in memory.
 * @author PDGen
 */
public interface PDOrderedPairI{

	/**
	 * Returns the instance connected to this instance through the role next.
	 * @return the connected instance
	 */
	public GUID getNext();

	/**
	 * Returns the instance(s) connected to this instance through the role next.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getNexts();

   /**
	 * Connects this instance to the given instance using role "next".
	 * If the given instance is null, nothing happens.
	 * @param next the instance to connect
	 */
	public void addNext(GUID next);
	/**
	 * Connects this instance to the given instances using role "next".
	 * If the given collection of instances is null, nothing happens.
	 * @param next the Collection of instances to connect
	 */
	public void addNexts(Collection<GUID> nexts);

	/**
	 * Removes the link from this instance through role "next".
	 */
	public void removeNext();

	/**
	 * Removes the link from this instance through role "next" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNext(GUID next);

	/**
	 * Connects this instance to the given instance using role "next".
	 * If there is already an instance connected to this instance through role "next", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param next the instance to connect
	 */
	public void setNext(GUID next) ;
	/**
	 * Returns the instance connected to this instance through the role prev.
	 * @return the connected instance
	 */
	public GUID getPrev();

	/**
	 * Returns the instance(s) connected to this instance through the role prev.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getPrevs();

   /**
	 * Connects this instance to the given instance using role "prev".
	 * If the given instance is null, nothing happens.
	 * @param prev the instance to connect
	 */
	public void addPrev(GUID prev);
	/**
	 * Connects this instance to the given instances using role "prev".
	 * If the given collection of instances is null, nothing happens.
	 * @param prev the Collection of instances to connect
	 */
	public void addPrevs(Collection<GUID> prevs);

	/**
	 * Removes the link from this instance through role "prev".
	 */
	public void removePrev();

	/**
	 * Removes the link from this instance through role "prev" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removePrev(GUID prev);

	/**
	 * Connects this instance to the given instance using role "prev".
	 * If there is already an instance connected to this instance through role "prev", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param prev the instance to connect
	 */
	public void setPrev(GUID prev) ;
}
