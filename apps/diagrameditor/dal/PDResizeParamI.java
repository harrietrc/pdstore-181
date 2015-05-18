package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "ResizeParam" in memory.
 * @author PDGen
 */
public interface PDResizeParamI{

	/**
	 * Returns the instance connected to this instance through the role height.
	 * @return the connected instance
	 */
	public Long getHeight();

	/**
	 * Returns the instance(s) connected to this instance through the role height.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getHeights();

   /**
	 * Connects this instance to the given instance using role "height".
	 * If the given instance is null, nothing happens.
	 * @param height the instance to connect
	 */
	public void addHeight(Long height);
	/**
	 * Connects this instance to the given instances using role "height".
	 * If the given collection of instances is null, nothing happens.
	 * @param height the Collection of instances to connect
	 */
	public void addHeights(Collection<Long> heights);

	/**
	 * Removes the link from this instance through role "height".
	 */
	public void removeHeight();

	/**
	 * Removes the link from this instance through role "height" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeHeight(Long height);

	/**
	 * Connects this instance to the given instance using role "height".
	 * If there is already an instance connected to this instance through role "height", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param height the instance to connect
	 */
	public void setHeight(Long height) ;
	/**
	 * Returns the instance connected to this instance through the role width.
	 * @return the connected instance
	 */
	public Long getWidth();

	/**
	 * Returns the instance(s) connected to this instance through the role width.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getWidths();

   /**
	 * Connects this instance to the given instance using role "width".
	 * If the given instance is null, nothing happens.
	 * @param width the instance to connect
	 */
	public void addWidth(Long width);
	/**
	 * Connects this instance to the given instances using role "width".
	 * If the given collection of instances is null, nothing happens.
	 * @param width the Collection of instances to connect
	 */
	public void addWidths(Collection<Long> widths);

	/**
	 * Removes the link from this instance through role "width".
	 */
	public void removeWidth();

	/**
	 * Removes the link from this instance through role "width" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeWidth(Long width);

	/**
	 * Connects this instance to the given instance using role "width".
	 * If there is already an instance connected to this instance through role "width", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param width the instance to connect
	 */
	public void setWidth(Long width) ;
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
