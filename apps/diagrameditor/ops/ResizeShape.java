package diagrameditor.ops;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pdstore.GUID;
import pdstore.PDStore;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDResizeParam;

/**
 * Class to handle GUI and create superparameters for resizing shapes.
 * @author Remy & Simon
 *
 */
public class ResizeShape extends EditorOperation {
	private boolean panelOpen = false;
	
	public JPanel inputContainer;
	private JTextField widthField;
	private JTextField heightField;
	private JButton resizeButton;	
	private JButton applyButton;
	private JButton cancelButton;
	
	public ResizeShape(DiagramEditor newEditor) {
		super(newEditor);
	}

	public void closePanel() {
		panelOpen = false;
		inputContainer.setVisible(panelOpen);
	}
	
	@Override
	protected void generateOperationPanel() {
		resizeButton = new JButton("Resize");
		resizeButton.setMargin(new Insets(0, 50, 0, 50));
		resizeButton.addActionListener(this);
		
		// resizebuttonPanel contains all components.
		operationPanel = new JPanel(new BorderLayout());

		// Create the text fields.
		JPanel inputPanel = new JPanel(new GridLayout(1, 4));
		widthField = new JTextField();
		widthField.setColumns(5);
		heightField = new JTextField();
		heightField.setColumns(5);
		JLabel widthLabel = new JLabel("Width");
		JLabel heightLabel = new JLabel("Height");

		inputPanel.add(widthLabel);
		inputPanel.add(widthField);
		inputPanel.add(heightLabel);
		inputPanel.add(heightField);

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

		inputContainer = new JPanel(new BorderLayout());
		inputContainer.add(inputPanel, BorderLayout.NORTH);
		inputContainer.add(paddingPanel1, BorderLayout.CENTER);
		inputContainer.add(buttonPanel, BorderLayout.SOUTH);
		inputContainer.setVisible(false);

		operationPanel.add(resizeButton, BorderLayout.NORTH);
		operationPanel.add(paddingPanel2, BorderLayout.CENTER);
		operationPanel.add(inputContainer, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Collapse or expand the move panel.
		if (e.getSource().equals(resizeButton)) {
			boolean temp = !panelOpen;
			editor.operationPanel.closePanels();
			panelOpen = temp;
			inputContainer.setVisible(panelOpen);
		} 
		// Clear all text fields. 
		else if (e.getSource().equals(cancelButton)) {
			heightField.setText("");
			widthField.setText("");
			editor.statusBar.clearStatus();
			closePanel();
		}
		// Attempt to apply the user's changes.
		else if (e.getSource().equals(applyButton)) {
			// No shape selected, do nothing.
			if (editor.selectBox.getSelectedItem() == null) {
				editor.statusBar.setError("Select a shape to perform the operation");
			} else {
				long height = 0;
				long width = 0;
				try {
					height = Long.parseLong(heightField.getText());
					width = Long.parseLong(widthField.getText());
				} catch (NumberFormatException e2) {
					editor.statusBar.setError("Please input Width and Height coordinates in right format!");
					return;
				}
				//Create superParam and add it to the operations list.
				PDStore store = editor.mainStore;
				PDOperationApplication opApp = new PDOperationApplication(store);
				opApp.addOperation(OpResizeShape.opID);

				PDResizeParam param = new PDResizeParam(store);
				param.addTargetShape((GUID)editor.selectBox.getSelectedItem());
				param.addHeight(height);
				param.addWidth(width);

				opApp.addSuperParameter(param);
				editor.getOperationList().add(opApp);
				store.commit();
				
				editor.statusBar.clearStatus();
			}
		}
		
	}

}
