package diagrameditor.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.ui.WidgetI;
import diagrameditor.dal.PDShape;

/**
 * Class to use data and visualize an image.
 * @author Remy & Simon
 *
 */
public class Image implements WidgetI {

	final public static GUID widgetID = new GUID("9b4ad3c3eb0d11e191b978e4009ed4de");
	private double xPos;
	private double yPos;
	private double width;
	private double height;
	Dimension size;
	//Register with main store.
	public static void register(PDStore store) {
		store.setName(widgetID, "Image");
		store.setType(widgetID, PDStore.WIDGET_TYPEID);
		store.setLink(widgetID, PDStore.WIDGET_IMPLEMENTATION_ROLEID, "diagrameditor.widgets.Image");
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

	/**
	 * Load and display an image.
	 */
	public void draw(PDStore store, GUID transaction, Object instance, Graphics2D graphics) {

		//Load the shape.
		PDShape shape = (PDShape) store.load((GUID) instance);
		//Load the parameters for drawing.
		xPos = shape.getX();
		yPos = shape.getY();
		width = shape.getWidth();
		height = shape.getHeight();
		size = new Dimension();
		size.width = (int) width;
		size.height = (int) height;
		Color color;

		try {
			color = Color.decode(shape.getColor());
		} catch (Exception e) {
			color = new Color(Color.TRANSLUCENT);//set default color.
		}

		//Load the image
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(shape.getVisualizedData()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedImage scaledImage = scaleImage(image, (int) width, (int) height, color);

		graphics.drawImage(scaledImage, null, (int) xPos, (int) yPos);

	}
	
	/**
	 * Takes and image and resizes it to the dimensions given.
	 * @param img The image to be used.
	 * @param width Desired width
	 * @param height Desired height
	 * @param background Background color to set.
	 * @return
	 */
	public BufferedImage scaleImage(BufferedImage img, int width, int height, Color background) {
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();

		// Un-hash the following to enforce scaling to aspect ratio
		//	    if (imgWidth*height < imgHeight*width) {
		//	        width = imgWidth*height/imgHeight;
		//	    } else {
		//	        height = imgHeight*width/imgWidth;
		//	    }
		
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		try {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setBackground(background);
			g.clearRect(0, 0, width, height);
			g.drawImage(img, 0, 0, width, height, null);
		} finally {
			g.dispose();
		}
		return newImage;
	}

}
