package diagrameditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import pdstore.GUID;
import pdstore.ui.PDStore;
import diagrameditor.dal.PDCopyParam;
import diagrameditor.dal.PDMoveParam;
import diagrameditor.dal.PDNewParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDShape;
import diagrameditor.filters.Filter;
import diagrameditor.ops.OpCopyShape;
import diagrameditor.ops.OpMoveShape;
import diagrameditor.ops.OpNewShape;

/**
 * Class to set up the draw panel section of the diagram editor, listens to
 * changes and acts accordingly.
 * 
 */
public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -6586020874129525506L;

	protected JList list;
	private DiagramEditor editor;
	public DefaultListModel listModel;
	private PDShape selectedShape = null;
	private Point startClickPoint = null;
	private Point startDragPoint = null;

	// variables for drag functionality
	private Shape shapeOutline = null;
	private long shapeWidth = 0;
	private long shapeHeight = 0;
	private long diffX = 0;
	private long diffY = 0;

	public boolean inDrag = false;

	/**
	 * Constructor
	 * 
	 * @param editor
	 *            , diagram editor
	 */
	public DrawPanel(DiagramEditor editor) {
		this.editor = editor;
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setModel(listModel);

		addMouseListener(this);
		addMouseMotionListener(this);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Apply all OperationApplications Loop through shape and draw all shapes
	 */
	public void paint(Graphics g) {
		PDStore store = DiagramEditor.mainStore;
		store.rollback();

		super.paint(g);

		// loop through operation list and apply all operational changes to the
		// database
		applyAllOps();

		//loop through the shapes using the operationList and paint 
		//only if its a new or copy shape. Replaces commented code above.
		Graphics2D graphics = (Graphics2D) g;
		OperationList opList = editor.getOperationList();
		for (PDOperationApplication op : opList) {
			if (op.getOperation().getName().equals("New Shape") || op.getOperation().getName().equals("Copy Shape")) {
				GUID id = (GUID) store.getInstance(op.getSuperParameter().getId(), CreateModel.TARGET_SHAPE_ROLEID);
				store.drawInstance(id, graphics);
			}

		}

		store.rollback();

		// Draw shape outlines if set (for dragging of shapes)
		if (shapeOutline != null) {
			Graphics2D g2d = (Graphics2D) g;
			float dash1[] = { 10.0f };
			BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
			g2d.setStroke(dashed);
			g2d.setColor(Color.GRAY);
			g2d.draw(shapeOutline);
		}

		Object selectedOperation = list.getSelectedValue();
		// Add operations to the history panel.
		Filter filter = editor.menuBar.selectedFilter;
		filter.display(editor.getOperationList(), listModel);

		// If nothing is selected in the history panel, scroll to the most
		// recent operation.
		if (selectedOperation == null) {
			int lastIndex = list.getModel().getSize() - 1;
			if (lastIndex >= 0) {
				list.ensureIndexIsVisible(lastIndex);
			}
		} else { // Else reselect the same operation in the history panel.
			list.setSelectedValue(selectedOperation, true);
		}

		refreshJComboBox(editor.getOperationList());
	}

	/**
	 * Used to check parameters.
	 * @param shape-the shape to check.
	 */
	public void checkParameter(PDShape shape) {
		System.out.println("\nChecking shape parameters\nid: " + shape.getId() + " \nColor: " + shape.getColor()
				+ " \nX: " + shape.getX() + " \nY: " + shape.getY() + " \nHeight: " + shape.getHeight() + " \nWidth: "
				+ shape.getWidth());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing if there are no shapes and drag mode is disabled
		if (editor.selectBox.getItemCount() == 0 || DiagramEditor.isInPresentationMode()) {
			return;
		}
		// apply all operations to get shapes into correct positions
		applyAllOps();

		// Find all shapes whose rectangular bounding box surrounds the mouse point

		// get a list of all shapes
		OperationList opList = editor.getOperationList();
		ArrayList<PDShape> shapeArray = new ArrayList<PDShape>();

		for (PDOperationApplication opApp : opList) {
			if (opApp.getOperation().getId().equals(OpCopyShape.opID)) {
				PDCopyParam cp = PDCopyParam.load(DiagramEditor.mainStore, opApp.getSuperParameter().getId());
				shapeArray.add(cp.getCopy());
			} else if (opApp.getOperation().getId().equals(OpNewShape.opID)) {
				PDNewParam np = PDNewParam.load(DiagramEditor.mainStore, opApp.getSuperParameter().getId());
				shapeArray.add(np.getTargetShape());
			}
		}

		for (PDShape shapeInst : shapeArray) {
			// shape x, y refer to top left corner of bounding square formed by
			// the given shape
			long sStartX = shapeInst.getX();
			long sEndX = shapeInst.getX() + shapeInst.getWidth();
			long sStartY = shapeInst.getY();
			long sEndY = shapeInst.getY() + shapeInst.getHeight();

			// check if shape bounding box contains the mouse point
			if (e.getX() >= sStartX && e.getX() <= sEndX && e.getY() >= sStartY && e.getY() <= sEndY) {
				selectedShape = shapeInst;
			}
		}

		// Only continue the drag if a shape was selected
		if (selectedShape != null) {
			//set the jcombobox selection to the currently selected shape.
			editor.selectBox.setSelectedItem(selectedShape.getId());

			// Set outline shape properties for drag & drop
			shapeWidth = selectedShape.getWidth();
			shapeHeight = selectedShape.getHeight();
			diffX = e.getX() - selectedShape.getX();
			diffY = e.getY() - selectedShape.getY();
			shapeOutline = new Rectangle2D.Double(e.getX() - diffX, e.getY() - diffY, shapeWidth, shapeHeight);

			startClickPoint = e.getPoint();
			startDragPoint = e.getPoint();
			inDrag = true;

			list.clearSelection();
		}

		repaint();
		// undo effects of operations
		DiagramEditor.mainStore.rollback();

		// Display the coordinates on the status bar.
		editor.statusBar.clearStatus();
		editor.statusBar.setCoordinates("" + e.getX(), "" + e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Display the coordinates on the status bar.
		editor.statusBar.setCoordinates(Integer.toString(e.getX()), Integer.toString(e.getY()));

		// Do nothing if no shape is dragged.
		if (!inDrag)
			return;

		startDragPoint = e.getPoint();

		//TODO: fix so it dynamically displays the outline correctly for other shapes or 
		//take out completely.
		shapeOutline = new Rectangle2D.Double(e.getX() - diffX, e.getY() - diffY, shapeWidth, shapeHeight);

		this.repaint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point releasePoint = e.getPoint();

		// Do nothing if no shape is dragged or shape did not change coordinates
		if (!inDrag || startClickPoint.equals(releasePoint)) {
			shapeOutline = null;
			selectedShape = null;
			repaint();
			return;
		}

		inDrag = false;

		// Store the Move operation.
		long dx = releasePoint.x - startClickPoint.x;
		long dy = releasePoint.y - startClickPoint.y;

		PDOperationApplication opApp = new PDOperationApplication(DiagramEditor.mainStore);
		opApp.addOperation(OpMoveShape.opID);

		PDMoveParam param = new PDMoveParam(DiagramEditor.mainStore);
		param.addTargetShape(selectedShape);
		param.setDx(dx);
		param.setDy(dy);

		// Add this new operationApplication to the list
		opApp.addSuperParameter(param);
		editor.getOperationList().add(opApp);
		DiagramEditor.mainStore.commit();

		// reset drag outline
		shapeOutline = null;
		selectedShape = null;
		repaint();

		// Display the coordinates on the status bar.
		editor.statusBar.setCoordinates(Integer.toString(e.getX()), Integer.toString(e.getY()));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	private void applyAllOps() {
		for (PDOperationApplication opApp : editor.getOperationList()) {
			GUID opID = opApp.getOperation().getId();
			GUID paramID = opApp.getSuperParameter().getId();

			DiagramEditor.mainStore.applyOperation(opID, paramID);
		}
	}

	private void refreshJComboBox(OperationList opList) {
		// store existing selection
		Object selectedItem = editor.selectBox.getSelectedItem();

		// reobtain the relevant shapes
		editor.selectBox.removeAllItems();
		for (PDOperationApplication opApp : opList) {
			if (opApp.getOperation().getId().equals(OpNewShape.opID)) {
				PDNewParam np = PDNewParam.load(DiagramEditor.mainStore, opApp.getSuperParameter().getId());
				editor.selectBox.addItem(np.getTargetShape().getId());
			} else if (opApp.getOperation().getId().equals(OpCopyShape.opID)) {
				PDCopyParam cp = PDCopyParam.load(DiagramEditor.mainStore, opApp.getSuperParameter().getId());
				editor.selectBox.addItem(cp.getCopy().getId());
			}
		}

		// select existing selection
		if (selectedItem != null) {
			editor.selectBox.setSelectedItem(selectedItem);
		}
	}

	public DrawPanel displayClone() throws CloneNotSupportedException {
		return new DrawPanel(editor);
	}
}