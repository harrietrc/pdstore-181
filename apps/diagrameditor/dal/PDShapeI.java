package diagrameditor.dal;

import java.util.Collection;
import pdstore.GUID;
import pdstore.dal.*;

/**
 *Interface for the Data access class of type "Shape" in memory.
 * @author PDGen
 */
public interface PDShapeI{

	/**
	 * Returns the instance connected to this instance through the role visualized data.
	 * @return the connected instance
	 */
	public String getVisualizedData();

	/**
	 * Returns the instance(s) connected to this instance through the role visualized data.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getVisualizedDatas();

   /**
	 * Connects this instance to the given instance using role "visualized data".
	 * If the given instance is null, nothing happens.
	 * @param visualizedData the instance to connect
	 */
	public void addVisualizedData(String visualizedData);
	/**
	 * Connects this instance to the given instances using role "visualized data".
	 * If the given collection of instances is null, nothing happens.
	 * @param visualizedData the Collection of instances to connect
	 */
	public void addVisualizedDatas(Collection<String> visualizedDatas);

	/**
	 * Removes the link from this instance through role "visualized data".
	 */
	public void removeVisualizedData();

	/**
	 * Removes the link from this instance through role "visualized data" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeVisualizedData(String visualizedData);

	/**
	 * Connects this instance to the given instance using role "visualized data".
	 * If there is already an instance connected to this instance through role "visualized data", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param visualizedData the instance to connect
	 */
	public void setVisualizedData(String visualizedData) ;
	/**
	 * Returns the instance connected to this instance through the role originalShape.
	 * @return the connected instance
	 */
	public PDShape getOriginalShape();

	/**
	 * Returns the instance(s) connected to this instance through the role originalShape.
	 * @return the connected instance(s)
	 */
	 public Collection<PDShape> getOriginalShapes();

   /**
	 * Connects this instance to the given instance using role "originalShape".
	 * If the given instance is null, nothing happens.
	 * @param originalShape the instance to connect
	 */
	public void addOriginalShape(GUID originalShape);
	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If the given instance is null, nothing happens.
	 * @param originalShape the instance to connect
	 */
	public void addOriginalShape(PDShape originalShape);

	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If the given collection of instances is null, nothing happens.
	 * @param originalShape the Collection of instances to connect
	 */
	public void addOriginalShapes(Collection<PDShape> originalShapes);

	/**
	 * Removes the link from this instance through role "originalShape".
	 */
	public void removeOriginalShape();

	/**
	 * Removes the link from this instance through role "originalShape" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeOriginalShape(PDShape originalShape);

	/**
	 * Removes the links from this instance through role "originalShape" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeOriginalShapes(Collection<PDShape> originalShapes);

	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If there is already an instance connected to this instance through role "originalShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param originalShape the instance to connect
	 */
	public void setOriginalShape(GUID originalShape) ;
	/**
	 * Connects this instance to the given instance using role "originalShape".
	 * If there is already an instance connected to this instance through role "originalShape", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param originalShape the instance to connect
	 */
	public void setOriginalShape(PDShape originalShape) 
;	/**
	 * Returns the instance connected to this instance through the role shapeType.
	 * @return the connected instance
	 */
	public PDType getShapeType();

	/**
	 * Returns the instance(s) connected to this instance through the role shapeType.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getShapeTypes();

   /**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(GUID shapeType);
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given instance is null, nothing happens.
	 * @param shapeType the instance to connect
	 */
	public void addShapeType(PDType shapeType);

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If the given collection of instances is null, nothing happens.
	 * @param shapeType the Collection of instances to connect
	 */
	public void addShapeTypes(Collection<PDType> shapeTypes);

	/**
	 * Removes the link from this instance through role "shapeType".
	 */
	public void removeShapeType();

	/**
	 * Removes the link from this instance through role "shapeType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeShapeType(PDType shapeType);

	/**
	 * Removes the links from this instance through role "shapeType" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeShapeTypes(Collection<PDType> shapeTypes);

	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(GUID shapeType) ;
	/**
	 * Connects this instance to the given instance using role "shapeType".
	 * If there is already an instance connected to this instance through role "shapeType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param shapeType the instance to connect
	 */
	public void setShapeType(PDType shapeType) 
;	/**
	 * Returns the instance connected to this instance through the role color.
	 * @return the connected instance
	 */
	public String getColor();

	/**
	 * Returns the instance(s) connected to this instance through the role color.
	 * @return the connected instance(s)
	 */
	 public Collection<String> getColors();

   /**
	 * Connects this instance to the given instance using role "color".
	 * If the given instance is null, nothing happens.
	 * @param color the instance to connect
	 */
	public void addColor(String color);
	/**
	 * Connects this instance to the given instances using role "color".
	 * If the given collection of instances is null, nothing happens.
	 * @param color the Collection of instances to connect
	 */
	public void addColors(Collection<String> colors);

	/**
	 * Removes the link from this instance through role "color".
	 */
	public void removeColor();

	/**
	 * Removes the link from this instance through role "color" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeColor(String color);

	/**
	 * Connects this instance to the given instance using role "color".
	 * If there is already an instance connected to this instance through role "color", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param color the instance to connect
	 */
	public void setColor(String color) ;
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
	 * Returns the instance connected to this instance through the role y.
	 * @return the connected instance
	 */
	public Long getY();

	/**
	 * Returns the instance(s) connected to this instance through the role y.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getYs();

   /**
	 * Connects this instance to the given instance using role "y".
	 * If the given instance is null, nothing happens.
	 * @param y the instance to connect
	 */
	public void addY(Long y);
	/**
	 * Connects this instance to the given instances using role "y".
	 * If the given collection of instances is null, nothing happens.
	 * @param y the Collection of instances to connect
	 */
	public void addYs(Collection<Long> ys);

	/**
	 * Removes the link from this instance through role "y".
	 */
	public void removeY();

	/**
	 * Removes the link from this instance through role "y" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeY(Long y);

	/**
	 * Connects this instance to the given instance using role "y".
	 * If there is already an instance connected to this instance through role "y", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param y the instance to connect
	 */
	public void setY(Long y) ;
	/**
	 * Returns the instance connected to this instance through the role x.
	 * @return the connected instance
	 */
	public Long getX();

	/**
	 * Returns the instance(s) connected to this instance through the role x.
	 * @return the connected instance(s)
	 */
	 public Collection<Long> getXs();

   /**
	 * Connects this instance to the given instance using role "x".
	 * If the given instance is null, nothing happens.
	 * @param x the instance to connect
	 */
	public void addX(Long x);
	/**
	 * Connects this instance to the given instances using role "x".
	 * If the given collection of instances is null, nothing happens.
	 * @param x the Collection of instances to connect
	 */
	public void addXs(Collection<Long> xs);

	/**
	 * Removes the link from this instance through role "x".
	 */
	public void removeX();

	/**
	 * Removes the link from this instance through role "x" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeX(Long x);

	/**
	 * Connects this instance to the given instance using role "x".
	 * If there is already an instance connected to this instance through role "x", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param x the instance to connect
	 */
	public void setX(Long x) ;
	/**
	 * Returns the instance connected to this instance through the role type.
	 * @return the connected instance
	 */
	public PDType getType();

	/**
	 * Returns the instance(s) connected to this instance through the role type.
	 * @return the connected instance(s)
	 */
	 public Collection<PDType> getTypes();

   /**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(GUID type);
	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given instance is null, nothing happens.
	 * @param type the instance to connect
	 */
	public void addType(PDType type);

	/**
	 * Connects this instance to the given instance using role "type".
	 * If the given collection of instances is null, nothing happens.
	 * @param type the Collection of instances to connect
	 */
	public void addTypes(Collection<PDType> types);

	/**
	 * Removes the link from this instance through role "type".
	 */
	public void removeType();

	/**
	 * Removes the link from this instance through role "type" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeType(PDType type);

	/**
	 * Removes the links from this instance through role "type" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeTypes(Collection<PDType> types);

	/**
	 * Connects this instance to the given instance using role "type".
	 * If there is already an instance connected to this instance through role "type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param type the instance to connect
	 */
	public void setType(GUID type) ;
	/**
	 * Connects this instance to the given instance using role "type".
	 * If there is already an instance connected to this instance through role "type", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param type the instance to connect
	 */
	public void setType(PDType type) 
;	/**
	 * Returns the instance connected to this instance through the role Diagram.
	 * @return the connected instance
	 */
	public PDDiagram getDiagram();

	/**
	 * Returns the instance(s) connected to this instance through the role Diagram.
	 * @return the connected instance(s)
	 */
	 public Collection<PDDiagram> getDiagrams();

   /**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given instance is null, nothing happens.
	 * @param diagram the instance to connect
	 */
	public void addDiagram(GUID diagram);
	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given instance is null, nothing happens.
	 * @param diagram the instance to connect
	 */
	public void addDiagram(PDDiagram diagram);

	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If the given collection of instances is null, nothing happens.
	 * @param diagram the Collection of instances to connect
	 */
	public void addDiagrams(Collection<PDDiagram> diagrams);

	/**
	 * Removes the link from this instance through role "Diagram".
	 */
	public void removeDiagram();

	/**
	 * Removes the link from this instance through role "Diagram" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 */
	public void removeDiagram(PDDiagram diagram);

	/**
	 * Removes the links from this instance through role "Diagram" to the instances
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 */
	public void removeDiagrams(Collection<PDDiagram> diagrams);

	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If there is already an instance connected to this instance through role "Diagram", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagram the instance to connect
	 */
	public void setDiagram(GUID diagram) ;
	/**
	 * Connects this instance to the given instance using role "Diagram".
	 * If there is already an instance connected to this instance through role "Diagram", the link will be overwritten.
	 * If the given instance is null, an existing link is removed.
	 * @param diagram the instance to connect
	 */
	public void setDiagram(PDDiagram diagram) 
;}
