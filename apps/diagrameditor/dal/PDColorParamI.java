package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "ColorParam" in memory.
 * @author PDGen
 */
public interface PDColorParamI{

	/**
	 * Returns the instance connected to this instance through the role newColor.
	 * @return the connected instance
	 */
	public String getNewColor();

	/**
	 * Returns the instance(s) connected to this instance through the role newColor.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getNewColors();

   /**
	 * Connects this instance to the given instance using role "newColor".
	 * If the given instance is null, nothing happens.
	 * @param newColor the instance to connect
	 */
	public void addNewColor(String newColor);
	/**
	 * Connects this instance to the given instances using role "newColor".
	 * If the given collection of instances is null, nothing happens.
	 * @param newColor the Collection of instances to connect
	 */
	public void addNewColors(Collection<String> newColors);

	/**
	 * Removes the link from this instance through role "newColor".
	 */
	public void removeNewColor();

	/**
	 * Removes the link from this instance through role "newColor" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeNewColor(String newColor);

	/**
	 * Connects this instance to the given instance using role "newColor".
	 * If there is already an instance connected to this instance through role "newColor", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param newColor the instance to connect
	 */
	public void setNewColor(String newColor) ;
	/**
	 * Returns the instance connected to this instance through the role targetShape.
	 * @return the connected instance
	 */
	public PDShape getTargetShape();

	/**
	 * Returns the instance(s) connected to this instance through the role targetShape.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getTargetShapes();

   /**
	 * Connects this instance to the given instance using role "targetShape".
	 * If the given instance is null, nothing happens.
	 * @param targetShape the instance to connect
	 */
	public void addTargetShape(GUID targetShape);
	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If the given instance is null, nothing happens.
	 * @param targetShape the instance to connect
	 */
	public void addTargetShape(PDShape targetShape);

	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If the given collection of instances is null, nothing happens.
	 * @param targetShape the Collection of instances to connect
	 */
	public void addTargetShapes(Collection<PDShape> targetShapes);

	/**
	 * Removes the link from this instance through role "targetShape".
	 */
	public void removeTargetShape();

	/**
	 * Removes the link from this instance through role "targetShape" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTargetShape(PDShape targetShape);

	/**
	 * Removes the links from this instance through role "targetShape" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTargetShapes(Collection<PDShape> targetShapes);

	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If there is already an instance connected to this instance through role "targetShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param targetShape the instance to connect
	 */
	public void setTargetShape(GUID targetShape) ;
	/**
	 * Connects this instance to the given instance using role "targetShape".
	 * If there is already an instance connected to this instance through role "targetShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param targetShape the instance to connect
	 */
	public void setTargetShape(PDShape targetShape) 
;}
