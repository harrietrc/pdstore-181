package diagrameditor.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.ui.WidgetI;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDShape;

/**
 * Class to visualize the shape data into a rectangle.
 * @author Remy & Simon
 *
 */
public class Rectangle implements WidgetI {

	final public static GUID widgetID = new GUID("a81ee761c95b11e1b4ac78e4009ed4de");
	//Register with the main store.
	public static void register(PDStore store) {
		store.setName(widgetID, "Rectangle");
		store.setType(widgetID, PDStore.WIDGET_TYPEID);
		store.setLink(widgetID, PDStore.WIDGET_IMPLEMENTATION_ROLEID, "diagrameditor.widgets.Rectangle");
	}

	// Default rectangle sizes.
	double height = 50;
	double width = 50;
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
	 * Draw the shape as a rectangle.
	 */
	public void draw(PDStore store, GUID transaction, Object instance, Graphics2D graphics) {
		// draw the circle
		PDShape shape = (PDShape) store.load((GUID) instance);

		//load the parameters for drawing.
		height = shape.getHeight();
		width = shape.getWidth();
		xPos = shape.getX();
		yPos = shape.getY();

		// color parameter is a string (e.g, 'BLACK', 'RED' etc)
		Color color;

		try {
			color = Color.decode(shape.getColor());
		} catch (Exception e) {
			color = Color.BLUE;//set default color.
		}
		graphics.setColor(color);

		graphics.fill(new Rectangle2D.Double(xPos, yPos, width, height));

		// Draw the label unless we're in presentation mode
		if (!DiagramEditor.isInPresentationMode()) {
			String label = store.getLabel(transaction, instance);
			FontMetrics metrics = graphics.getFontMetrics();
			graphics.drawString(label, (int) xPos, (int) yPos);
		}
	}

}
