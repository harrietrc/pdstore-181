package diagrameditor.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.ui.WidgetI;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDShape;

/**
 * Class to visualize the shape data into a circle.
 * @author Remy & Simon
 *
 */
public class Circle implements WidgetI {

	final public static GUID widgetID = new GUID("7c6315eac71311e181e678e4009ed4de");
	//Register with the main store.
	public static void register(PDStore store) {
		store.setName(widgetID, "Circle");
		store.setType(widgetID, PDStore.WIDGET_TYPEID);
		store.setLink(widgetID, PDStore.WIDGET_IMPLEMENTATION_ROLEID, "diagrameditor.widgets.Circle");
	}

	double width;
	double height;
	double xPos;
	double yPos;

	@Override
	public Dimension getPreferredSize(PDStore store, GUID transaction, GUID instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getMinSize(PDStore store, GUID transaction, GUID instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getMaxSize(PDStore store, GUID transaction, GUID instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * Draw the shapes as a circle.
	 */
	public void draw(PDStore store, GUID transaction, Object instance, Graphics2D graphics) {
		//Draw the circle
		PDShape shape = (PDShape) store.load((GUID) instance);

		//load the parameters for drawing.
		height = shape.getHeight();
		width = shape.getWidth();
		xPos = shape.getX();
		yPos = shape.getY();

		//Color parameter is a string (e.g, 'BLACK', 'RED' etc)
		Color color;
		
		//Get set color if existing shape or use default color if new shape.
		try {
			color = Color.decode(shape.getColor());
		} catch (Exception e) {
			color = Color.RED;//set default color.
		}
		graphics.setColor(color);

		graphics.fill(new Ellipse2D.Double(xPos, yPos, width, height));

		//Draw the label unless we're in presentation mode.
		if (!DiagramEditor.isInPresentationMode()) {
			String label = store.getLabel(transaction, instance);
			FontMetrics metrics = graphics.getFontMetrics();
			graphics.drawString(label, (int) xPos, (int) yPos);
		}

	}
}
