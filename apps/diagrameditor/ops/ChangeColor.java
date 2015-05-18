package diagrameditor.ops;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import pdstore.GUID;
import pdstore.PDStore;
import diagrameditor.CreateModel;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDColorParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDShape;

/**
 * Class to handle GUI and create superparameters for changing shape colors.
 * @author Remy & Simon
 *
 */
public class ChangeColor extends EditorOperation {

	public ChangeColor(DiagramEditor newEditor) {
		super(newEditor);
	}

	@Override
	protected void generateOperationPanel() {
		JButton colorButton = new JButton("Color");
		colorButton.setMargin(new Insets(0, 50, 0, 50));
		colorButton.addActionListener(this);
		operationPanel = new JPanel(new BorderLayout());
		operationPanel.add(colorButton, BorderLayout.NORTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		PDStore store = editor.mainStore;
		//Clear the status bar. 
		editor.statusBar.clearStatus();
		
		// Must select a shape in the ComboBox to perform the operation.
		Object selectedItem = editor.selectBox.getSelectedItem(); 
		if (selectedItem == null) {
			editor.statusBar.setError("Select a shape to perform the operation");
			return; 
		}
		
		// Get the current color of the shape.
		PDShape targetShape = null;
		Collection<Object> allShapes = editor.mainStore.getAllInstancesOfType(CreateModel.SHAPE_TYPE_ID);
		Iterator iterator = allShapes.iterator();
		while(iterator.hasNext()){
			PDShape tempShape = (PDShape)editor.mainStore.load((GUID)iterator.next());
			if(tempShape.getId().toString().equals(selectedItem.toString())){
				targetShape = tempShape;
			}
		}
		
		Color currentColor = Color.getColor(targetShape.getColor());

		
		// Ask the user to select a color. 
		Color tempColor = JColorChooser.showDialog(editor, "Please choose a color",
		currentColor); 
		if (tempColor == null){
			return;
		}
		
		//Format the color: #RRGGBB 
		String newColor = Integer.toHexString(tempColor.getRGB()); newColor = "#" + newColor.substring(2,
		newColor.length());

		//Create superParam and add it to the operations list.
		PDOperationApplication opApp = new PDOperationApplication(store);
		opApp.addOperation(OpChangeColor.opID);

		PDColorParam param = new PDColorParam(store);
		param.addTargetShape((GUID)selectedItem);
		param.addNewColor(newColor);

		opApp.addSuperParameter(param);
		editor.getOperationList().add(opApp);
		store.commit();

		
	}

}
