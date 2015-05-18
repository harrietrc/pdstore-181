package pdstore.ui;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pdstore.GUID;
import pdstore.PDChange;

public class InteractiveCanvas extends JComponent implements ComponentListener,
		MouseListener {

	private static final long serialVersionUID = 3555179798142695605L;

	PDStore store;

	/**
	 * Changes that should be visualized. TODO find better way of specifying
	 * these, e.g. with query? If yes, how to ensure the result is a set of changes?
	 */
	Collection<PDChange<GUID, Object, GUID>> changes;

	/**
	 * A buffer that contains the change object represented on each canvas
	 * pixel.
	 */
	PDChange<GUID, Object, GUID>[][] changeBuffer;

	public InteractiveCanvas(PDStore store,
			Collection<PDChange<GUID, Object, GUID>> changes) {

		this.store = store;
		this.changes = changes;

		addComponentListener(this);
	}

	public void paintComponent(Graphics g) {
		// paint background
		super.paintComponent(g);
		
		//TODO reevaluate query of the changes to be drawn

		// draw the widget of the instance2 of every change
		for (PDChange<GUID, Object, GUID> change : changes) {
			// TODO draw change.instance2 on the canvas and also on the change buffer. How?
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		changeBuffer = new PDChange[getWidth()][getHeight()];
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		PDChange<GUID, Object, GUID> clickedChange = changeBuffer[arg0.getX()][arg0
				.getY()];

		Object oldValue = clickedChange.getInstance2();
		String roleLabel = store.getLabel(clickedChange.getRole2());

		if (oldValue instanceof String) {
			String newString = JOptionPane.showInputDialog(null, roleLabel,
					oldValue);

			if (newString == null || newString.equals(""))
				return;
			
			store.setLink(clickedChange.getInstance1(), clickedChange.getRole2(), newString);
			store.commit();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
