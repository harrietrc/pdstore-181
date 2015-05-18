package diagrameditor.ops;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pdstore.PDStore;
import pdstore.dal.PDInstance;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDNewParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDShape;
import diagrameditor.widgets.Text;

/**
 * Class to handle GUI and create superparameters for texts.
 * @author Remy & Simon
 *
 */
public class AddText extends EditorOperation {

	private boolean panelOpen = false;
	private JButton textButton;
	private JTextField textField;
	private JButton applyButton;
	private JButton cancelButton;
	private JPanel inputContainer;

	public AddText(DiagramEditor editor) {
		super(editor);
	}

	protected void generateOperationPanel() {
		textButton = new JButton("Add Text");
		textButton.setMargin(new Insets(0, 50, 0, 50));
		textButton.addActionListener(this);
		operationPanel = new JPanel(new BorderLayout());

		// Create the text fields.
		JPanel inputPanel = new JPanel(new GridLayout(1, 4));
		textField = new JTextField();
		textField.setColumns(5);
		inputPanel.add(textField);

		// Create the Apply and Cancel buttons.
		JPanel buttonPanel = new JPanel();
		applyButton = new JButton("Apply");
		applyButton.setMargin(new Insets(0, 11, 0, 11));
		applyButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.setMargin(new Insets(0, 11, 0, 11));
		cancelButton.addActionListener(this);

		buttonPanel.add(applyButton);
		buttonPanel.add(cancelButton);

		// Padding to be used in the layout.
		JPanel paddingPanel1 = new JPanel();
		JPanel paddingPanel2 = new JPanel();

		// Create the collapsing\expanding panel.
		inputContainer = new JPanel(new BorderLayout());
		inputContainer.add(inputPanel, BorderLayout.NORTH);
		inputContainer.add(paddingPanel1, BorderLayout.CENTER);
		inputContainer.add(buttonPanel, BorderLayout.SOUTH);
		inputContainer.setVisible(false);

		operationPanel.add(textButton, BorderLayout.NORTH);
		operationPanel.add(paddingPanel2, BorderLayout.CENTER);
		operationPanel.add(inputContainer, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent e) {

		// Collapse or expand the move panel.
		if (e.getSource().equals(textButton)) {
			boolean temp = !panelOpen;
			editor.operationPanel.closePanels();
			panelOpen = temp;
			inputContainer.setVisible(panelOpen);
		}
		// Clear all text fields. 
		else if (e.getSource().equals(cancelButton)) {
			textField.setText("");
			editor.statusBar.clearStatus();
			closePanel();
		} else if (e.getSource().equals(applyButton)) {
			String text;
			text = textField.getText();
			//System.out.println("adding text: "+text);
			textField.setText("");

			PDStore store = DiagramEditor.mainStore;
			// Create the parameter object and add to the operation list
			PDOperationApplication opApp = new PDOperationApplication(store);
			opApp.addOperation(OpNewShape.opID);

			PDNewParam param = new PDNewParam(store);
			PDInstance parent = (PDInstance) editor.getShapeList().getParentInstance();
			param.addTargetShapeList(parent.getId());
			param.addVisualizedBy(Text.widgetID);
			param.addData(text);

			// Create the new shape
			PDShape newShape = new PDShape(store);
			param.addTargetShape(newShape.getId());
			opApp.addSuperParameter(param);
			editor.getOperationList().add(opApp);
			store.commit();
		}

	}

	public void closePanel() {
		panelOpen = false;
		inputContainer.setVisible(panelOpen);
	}

}
