package pdstore.ui.graphview.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Graph" in memory.
 * @author PDGen
 */
public interface PDGraphI{

	/**
	 * Returns the instance connected to this instance through the role Java package.
	 * @return the connected instance
	 */
	public String getJavaPackage();

	/**
	 * Returns the instance(s) connected to this instance through the role Java package.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getJavaPackages();

   /**
	 * Connects this instance to the given instance using role "Java package".
	 * If the given instance is null, nothing happens.
	 * @param javaPackage the instance to connect
	 */
	public void addJavaPackage(String javaPackage);
	/**
	 * Connects this instance to the given instances using role "Java package".
	 * If the given collection of instances is null, nothing happens.
	 * @param javaPackage the Collection of instances to connect
	 */
	public void addJavaPackages(Collection<String> javaPackages);

	/**
	 * Removes the link from this instance through role "Java package".
	 */
	public void removeJavaPackage();

	/**
	 * Removes the link from this instance through role "Java package" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeJavaPackage(String javaPackage);

	/**
	 * Connects this instance to the given instance using role "Java package".
	 * If there is already an instance connected to this instance through role "Java package", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param javaPackage the instance to connect
	 */
	public void setJavaPackage(String javaPackage) ;
	/**
	 * Returns the instance connected to this instance through the role Node.
	 * @return the connected instance
	 */
	public PDNode getNode();

	/**
	 * Returns the instance(s) connected to this instance through the role Node.
	 * @return the connected instance(s)
	 */
	 public Collection<PDNode> getNodes();

   /**
	 * Connects this instance to the given instance using role "Node".
	 * If the given instance is null, nothing happens.
	 * @param node the instance to connect
	 */
	public void addNode(GUID node);
	/**
	 * Connects this instance to the given instance using role "Node".
	 * If the given instance is null, nothing happens.
	 * @param node the instance to connect
	 */
	public void addNode(PDNode node);

	/**
	 * Connects this instance to the given instance using role "Node".
	 * If the given collection of instances is null, nothing happens.
	 * @param node the Collection of instances to connect
	 */
	public void addNodes(Collection<PDNode> nodes);

	/**
	 * Removes the link from this instance through role "Node".
	 */
	public void removeNode();

	/**
	 * Removes the link from this instance through role "Node" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNode(PDNode node);

	/**
	 * Removes the links from this instance through role "Node" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeNodes(Collection<PDNode> nodes);

	/**
	 * Connects this instance to the given instance using role "Node".
	 * If there is already an instance connected to this instance through role "Node", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param node the instance to connect
	 */
	public void setNode(GUID node) ;
	/**
	 * Connects this instance to the given instance using role "Node".
	 * If there is already an instance connected to this instance through role "Node", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param node the instance to connect
	 */
	public void setNode(PDNode node) 
;}
