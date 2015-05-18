package diagrameditor.ops;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import pdstore.PDStore;
import pdstore.dal.PDInstance;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDNewParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDShape;
import diagrameditor.widgets.Image;

/**
 * Class to handle GUI and create superparameters for images.
 * @author Remy & Simon
 *
 */
public class AddImage extends EditorOperation {

	private JButton imageButton;
	private JFileChooser chooser;

	public AddImage(DiagramEditor editor) {
		super(editor);
	}

	protected void generateOperationPanel() {
		imageButton = new JButton("Add Image");
		imageButton.setMargin(new Insets(0, 50, 0, 50));
		imageButton.addActionListener(this);
		operationPanel = new JPanel(new BorderLayout());
		operationPanel.add(imageButton, BorderLayout.NORTH);

	}

	public void actionPerformed(ActionEvent e) {

		String imagePath = null;
		//Open a file chooser to display possible images to open.
		chooser = new JFileChooser();
		//Open from the image folder from the current directory.
		chooser.setCurrentDirectory(new java.io.File("./images"));
		chooser.setDialogTitle("Select an image");
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(imageButton) == JFileChooser.APPROVE_OPTION) {
			imagePath = chooser.getSelectedFile().toString();
		} else {
			System.out.println("No Selection ");
			return;
		}

		PDStore store = DiagramEditor.mainStore;
		// Create the parameter object and add to the operation list
		PDOperationApplication opApp = new PDOperationApplication(store);
		opApp.addOperation(OpNewShape.opID);

		PDNewParam param = new PDNewParam(store);
		PDInstance parent = (PDInstance) editor.getShapeList().getParentInstance();
		param.addTargetShapeList(parent.getId());
		param.addVisualizedBy(Image.widgetID);
		param.addData(imagePath);

		// Create the new shape
		PDShape newShape = new PDShape(store);
		param.addTargetShape(newShape.getId());
		opApp.addSuperParameter(param);
		editor.getOperationList().add(opApp);
		store.commit();

	}

}
