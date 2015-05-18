package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "NewParam" in memory.
 * @author PDGen
 */
public interface PDNewParamI{

	/**
	 * Returns the instance connected to this instance through the role data.
	 * @return the connected instance
	 */
	public String getData();

	/**
	 * Returns the instance(s) connected to this instance through the role data.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getDatas();

   /**
	 * Connects this instance to the given instance using role "data".
	 * If the given instance is null, nothing happens.
	 * @param data the instance to connect
	 */
	public void addData(String data);
	/**
	 * Connects this instance to the given instances using role "data".
	 * If the given collection of instances is null, nothing happens.
	 * @param data the Collection of instances to connect
	 */
	public void addDatas(Collection<String> datas);

	/**
	 * Removes the link from this instance through role "data".
	 */
	public void removeData();

	/**
	 * Removes the link from this instance through role "data" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeData(String data);

	/**
	 * Connects this instance to the given instance using role "data".
	 * If there is already an instance connected to this instance through role "data", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param data the instance to connect
	 */
	public void setData(String data) ;
	/**
	 * Returns the instance connected to this instance through the role visualized by.
	 * @return the connected instance
	 */
	public GUID getVisualizedBy();

	/**
	 * Returns the instance(s) connected to this instance through the role visualized by.
	 * @return the connected instance(s)
	 */
	 public Collection<GUID> getVisualizedBys();

   /**
	 * Connects this instance to the given instance using role "visualized by".
	 * If the given instance is null, nothing happens.
	 * @param visualizedBy the instance to connect
	 */
	public void addVisualizedBy(GUID visualizedBy);
	/**
	 * Connects this instance to the given instances using role "visualized by".
	 * If the given collection of instances is null, nothing happens.
	 * @param visualizedBy the Collection of instances to connect
	 */
	public void addVisualizedBys(Collection<GUID> visualizedBys);

	/**
	 * Removes the link from this instance through role "visualized by".
	 */
	public void removeVisualizedBy();

	/**
	 * Removes the link from this instance through role "visualized by" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeVisualizedBy(GUID visualizedBy);

	/**
	 * Connects this instance to the given instance using role "visualized by".
	 * If there is already an instance connected to this instance through role "visualized by", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizedBy the instance to connect
	 */
	public void setVisualizedBy(GUID visualizedBy) ;
	/**
	 * Returns the instance connected to this instance through the role target shape list.
	 * @return the connected instance
	 */
	public Object getTargetShapeList();

	/**
	 * Returns the instance(s) connected to this instance through the role target shape list.
	 * @return the connected instance(s)
	 */
	 public Collection<Object> getTargetShapeLists();

   /**
	 * Connects this instance to the given instance using role "target shape list".
	 * If the given instance is null, nothing happens.
	 * @param targetShapeList the instance to connect
	 */
	public void addTargetShapeList(Object targetShapeList);
	/**
	 * Connects this instance to the given instances using role "target shape list".
	 * If the given collection of instances is null, nothing happens.
	 * @param targetShapeList the Collection of instances to connect
	 */
	public void addTargetShapeLists(Collection<Object> targetShapeLists);

	/**
	 * Removes the link from this instance through role "target shape list".
	 */
	public void removeTargetShapeList();

	/**
	 * Removes the link from this instance through role "target shape list" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeTargetShapeList(Object targetShapeList);

	/**
	 * Connects this instance to the given instance using role "target shape list".
	 * If there is already an instance connected to this instance through role "target shape list", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param targetShapeList the instance to connect
	 */
	public void setTargetShapeList(Object targetShapeList) ;
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
