package diagrameditor.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.ui.WidgetI;
import diagrameditor.dal.PDShape;

/**
 * Class to visualize the shape data into text.
 * @author Remy & Simon
 *
 */
public class Text implements WidgetI {

	final public static GUID widgetID = new GUID("4ccf26a1ea6b11e1b62ea4badbcc8c8f");

	private double xPos;
	private double yPos;
	private Font font;
	//Register with the main store.
	public static void register(PDStore store) {
		store.setName(widgetID, "Text");
		store.setType(widgetID, PDStore.WIDGET_TYPEID);
		store.setLink(widgetID, PDStore.WIDGET_IMPLEMENTATION_ROLEID, "diagrameditor.widgets.Text");
	}

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
	 * Use data to draw text.
	 */
	public void draw(PDStore store, GUID transaction, Object instance, Graphics2D graphics) {
		//Load the shape.
		PDShape shape = (PDShape) store.load((GUID) instance);

		//Load the parameters for drawing.
		xPos = shape.getX();
		yPos = shape.getY();

		Color color;
		//Record original font.
		Font originalFont = graphics.getFont();

		try {
			color = Color.decode(shape.getColor());
		} catch (Exception e) {
			color = Color.BLACK;//set default color.
		}
		
		//Set font for the text.
		font = new Font("serif", Font.PLAIN, Integer.parseInt(shape.getHeight().toString()));

		graphics.setFont(font);
		graphics.setColor(color);
		graphics.drawString(shape.getVisualizedData(), (int) xPos, (int) yPos + shape.getHeight());

		//Reset Graphics font
		graphics.setFont(originalFont);
	}

}
