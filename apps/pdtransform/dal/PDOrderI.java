package pdtransform.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Order" in memory.
 * @author PDGen
 */
public interface PDOrderI{

	/**
	 * Returns the instance connected to this instance through the role ordered pairs.
	 * @return the connected instance
	 */
	public PDOrderedPair getOrderedPairs();

	/**
	 * Returns the instance(s) connected to this instance through the role ordered pairs.
	 * @return the connected instance(s)
	 */
	 public Collection<PDOrderedPair> getOrderedPairss();

   /**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If the given instance is null, nothing happens.
	 * @param orderedPairs the instance to connect
	 */
	public void addOrderedPairs(GUID orderedPairs);
	/**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If the given instance is null, nothing happens.
	 * @param orderedPairs the instance to connect
	 */
	public void addOrderedPairs(PDOrderedPair orderedPairs);

	/**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If the given collection of instances is null, nothing happens.
	 * @param orderedPairs the Collection of instances to connect
	 */
	public void addOrderedPairss(Collection<PDOrderedPair> orderedPairss);

	/**
	 * Removes the link from this instance through role "ordered pairs".
	 */
	public void removeOrderedPairs();

	/**
	 * Removes the link from this instance through role "ordered pairs" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOrderedPairs(PDOrderedPair orderedPairs);

	/**
	 * Removes the links from this instance through role "ordered pairs" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOrderedPairss(Collection<PDOrderedPair> orderedPairss);

	/**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If there is already an instance connected to this instance through role "ordered pairs", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param orderedPairs the instance to connect
	 */
	public void setOrderedPairs(GUID orderedPairs) ;
	/**
	 * Connects this instance to the given instance using role "ordered pairs".
	 * If there is already an instance connected to this instance through role "ordered pairs", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param orderedPairs the instance to connect
	 */
	public void setOrderedPairs(PDOrderedPair orderedPairs) 
;}
