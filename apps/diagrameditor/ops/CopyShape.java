package diagrameditor.ops;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.dal.PDInstance;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDCopyParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDShape;

/**
 * Class to handle GUI and create superparameters for copying shapes.
 *
 */
public class CopyShape extends EditorOperation {

	public CopyShape(DiagramEditor newEditor) {
		super(newEditor);
	}

	protected void generateOperationPanel() {
		JButton copy_button = new JButton("Copy");
		copy_button.setMargin(new Insets(0, 50, 0, 50));
		copy_button.addActionListener(this);

		JPanel paddingPanel1 = new JPanel();

		operationPanel = new JPanel(new BorderLayout());
		operationPanel.add(copy_button, BorderLayout.NORTH);
		operationPanel.add(paddingPanel1, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		PDStore store = DiagramEditor.mainStore;
		// Must select a shape in the ComboBox to perform the operation.
		Object selectedItem = editor.selectBox.getSelectedItem();
		if (selectedItem == null) {
			editor.statusBar.setError("Select a shape to perform the operation");
			return;
		}

		// Clear the status bar.
		editor.statusBar.clearStatus();

		// Create superParam and add it to the operations list.
		PDOperationApplication opApp = new PDOperationApplication(store);
		opApp.addOperation(OpCopyShape.opID);

		PDCopyParam param = new PDCopyParam(store);
		param.addTargetShape((GUID) editor.selectBox.getSelectedItem());
		PDInstance parent = (PDInstance) editor.getShapeList().getParentInstance();
		param.addTargetShapeList(parent.getId());

		// Create the shape GUID
		PDShape newShape = new PDShape(store);
		param.addCopy(newShape.getId());

		opApp.addSuperParameter(param);
		editor.getOperationList().add(opApp);
		store.commit();

	}

}
