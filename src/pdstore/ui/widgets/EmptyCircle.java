package pdstore.ui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.ui.WidgetI;

public class EmptyCircle implements WidgetI {

	final public static GUID WIDGET_ID = new GUID(
			"e4745341cb2611e19809842b2b9af4fd");

	public static void register(PDStore store) {
		store.setName(WIDGET_ID, "Empty Circle");
		store.setType(WIDGET_ID, PDStore.WIDGET_TYPEID);
		store.setLink(WIDGET_ID, PDStore.WIDGET_IMPLEMENTATION_ROLEID,
				"pdstore.ui.widgets.EmptyCircle");
	}

	final static double radius = 12;

	@Override
	public Dimension getPreferredSize(PDStore store, GUID transaction,
			GUID instance) {
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
	public void draw(PDStore store, GUID transaction, Object instance,
			Graphics2D graphics) {
		// draw the circle
		graphics.setPaint(Color.WHITE);
		graphics.fill(new Ellipse2D.Double(-radius, -radius, 2 * radius,
				2 * radius));
		graphics.setPaint(Color.BLACK);
		graphics.draw(new Ellipse2D.Double(-radius, -radius, 2 * radius,
				2 * radius));

		// draw the label
		String label = store.getLabel(transaction, instance);
		FontMetrics metrics = graphics.getFontMetrics();
		int height = metrics.getHeight();
		int width = metrics.stringWidth(label);
		graphics.drawString(label, -width / 2, (int) -radius - 5);
	}
}
