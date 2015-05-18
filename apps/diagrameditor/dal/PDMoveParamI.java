package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "MoveParam" in memory.
 * @author PDGen
 */
public interface PDMoveParamI{

	/**
	 * Returns the instance connected to this instance through the role dy.
	 * @return the connected instance
	 */
	public Long getDy();

	/**
	 * Returns the instance(s) connected to this instance through the role dy.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getDys();

   /**
	 * Connects this instance to the given instance using role "dy".
	 * If the given instance is null, nothing happens.
	 * @param dy the instance to connect
	 */
	public void addDy(Long dy);
	/**
	 * Connects this instance to the given instances using role "dy".
	 * If the given collection of instances is null, nothing happens.
	 * @param dy the Collection of instances to connect
	 */
	public void addDys(Collection<Long> dys);

	/**
	 * Removes the link from this instance through role "dy".
	 */
	public void removeDy();

	/**
	 * Removes the link from this instance through role "dy" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDy(Long dy);

	/**
	 * Connects this instance to the given instance using role "dy".
	 * If there is already an instance connected to this instance through role "dy", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param dy the instance to connect
	 */
	public void setDy(Long dy) ;
	/**
	 * Returns the instance connected to this instance through the role dx.
	 * @return the connected instance
	 */
	public Long getDx();

	/**
	 * Returns the instance(s) connected to this instance through the role dx.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getDxs();

   /**
	 * Connects this instance to the given instance using role "dx".
	 * If the given instance is null, nothing happens.
	 * @param dx the instance to connect
	 */
	public void addDx(Long dx);
	/**
	 * Connects this instance to the given instances using role "dx".
	 * If the given collection of instances is null, nothing happens.
	 * @param dx the Collection of instances to connect
	 */
	public void addDxs(Collection<Long> dxs);

	/**
	 * Removes the link from this instance through role "dx".
	 */
	public void removeDx();

	/**
	 * Removes the link from this instance through role "dx" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDx(Long dx);

	/**
	 * Connects this instance to the given instance using role "dx".
	 * If there is already an instance connected to this instance through role "dx", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param dx the instance to connect
	 */
	public void setDx(Long dx) ;
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
