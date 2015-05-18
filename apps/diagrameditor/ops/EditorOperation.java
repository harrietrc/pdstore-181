package diagrameditor.ops;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

import diagrameditor.DiagramEditor;
/**
 * DEPRECATED - operations implement the pdstore interface src.pdstore.OperationI
 */
public abstract class EditorOperation implements ActionListener{
	protected final DiagramEditor editor;
	protected JPanel operationPanel;
	
	public EditorOperation(DiagramEditor editor){
		this.editor = editor;
		generateOperationPanel();
	}
	
	public JComponent getUI(){
		return operationPanel;
	}
	
	protected abstract void generateOperationPanel();
	/*
	public abstract String stringRepresentation(PDSimpleSpatialInfo pds);
	public abstract void drawAction(String shapeID, PDSimpleSpatialInfo pds, ArrayList<String> objectsToDraw);
	public abstract void closePanel();
	public abstract void actionPerformed(ActionEvent e);
	public abstract String checkId(PDSimpleSpatialInfo pds);
	*/
}
