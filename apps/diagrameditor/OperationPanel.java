package diagrameditor;


import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import diagrameditor.ops.AddImage;
import diagrameditor.ops.AddText;
import diagrameditor.ops.ChangeColor;
import diagrameditor.ops.CopyShape;
import diagrameditor.ops.MoveShape;
import diagrameditor.ops.NewShape;
import diagrameditor.ops.ResizeShape;

/**
 * Class to set up the operation panel section of the diagram editor.
 */
public class OperationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public DiagramEditor editor;
	public JComboBox selectBox;

	private NewShape newShape;
	private CopyShape copyShape;
	private MoveShape moveShape;
	private ResizeShape resizeShape;
	private ChangeColor changeColour;
	private AddText text;
	private AddImage image;
	
	/**
	 * Constructor
	 * @param editor, diagram editor
	 */
	public OperationPanel(DiagramEditor editor) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.editor = editor;
		selectBox = editor.selectBox;
		
		//create actions
		newShape = new NewShape(editor);
		copyShape = new CopyShape(editor);
		moveShape = new MoveShape(editor);
		resizeShape = new ResizeShape(editor);
		changeColour = new ChangeColor(editor);
		text = new AddText(editor);
		image = new AddImage(editor);
		
		this.add(selectBox, BorderLayout.NORTH);
		this.add(newShape.getUI());
		this.add(text.getUI());
		this.add(image.getUI());
		this.add(copyShape.getUI());
		this.add(moveShape.getUI());
		this.add(resizeShape.getUI());
		this.add(changeColour.getUI());

	}
	
	/**
	 * Method to close the panels which can be extended
	 */
	public void closePanels() {
		moveShape.closePanel();
		resizeShape.closePanel();
	}
}
