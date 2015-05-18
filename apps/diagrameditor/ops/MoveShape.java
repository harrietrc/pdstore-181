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
import diagrameditor.dal.PDMoveParam;
import diagrameditor.dal.PDOperationApplication;

/**
 * Class to handle GUI and create superparameters for moving shapes.
 *
 */
public class MoveShape extends EditorOperation {

	private boolean panelOpen = false;

	private JPanel inputContainer;
	private JTextField xTextField;
	private JTextField yTextField;
	private JButton moveButton;
	private JButton applyButton;
	private JButton cancelButton;

	public MoveShape(DiagramEditor newEditor) {
		super(newEditor);
	}

	protected void generateOperationPanel() {
		moveButton = new JButton("Move");
		moveButton.setMargin(new Insets(0, 50, 0, 50));
		moveButton.addActionListener(this);

		// movebuttonPanel contains all components.
		operationPanel = new JPanel(new BorderLayout());

		// Create the text fields.
		JPanel inputPanel = new JPanel(new GridLayout(1, 4));
		JLabel xLabel = new JLabel("  X: ");
		JLabel yLabel = new JLabel("  Y: ");
		xTextField = new JTextField();
		xTextField.setColumns(5);
		yTextField = new JTextField();
		yTextField.setColumns(5);

		inputPanel.add(xLabel);
		inputPanel.add(xTextField);
		inputPanel.add(yLabel);
		inputPanel.add(yTextField);

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

		operationPanel.add(moveButton, BorderLayout.NORTH);
		operationPanel.add(paddingPanel2, BorderLayout.CENTER);
		operationPanel.add(inputContainer, BorderLayout.SOUTH);
	}

	public void closePanel() {
		panelOpen = false;
		inputContainer.setVisible(panelOpen);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Collapse or expand the move panel.
		if (e.getSource().equals(moveButton)) {
			boolean temp = !panelOpen;
			editor.operationPanel.closePanels();
			panelOpen = temp;
			inputContainer.setVisible(panelOpen);
		}
		// Clear all text fields. 
		else if (e.getSource().equals(cancelButton)) {
			xTextField.setText("");
			yTextField.setText("");
			editor.statusBar.clearStatus();
			closePanel();
		}
		// Attempt to apply the user's changes.
		else if (e.getSource().equals(applyButton)) {
			// No shape selected, do nothing.
			if (editor.selectBox.getSelectedItem() == null) {
				editor.statusBar.setError("Select a shape to perform the operation");
			} else {
				// Get the X and Y coordinate inputs.
				long X_Coordinate = 0;
				long Y_Coordinate = 0;
				try {
					X_Coordinate = Long.parseLong(xTextField.getText());
					Y_Coordinate = Long.parseLong(yTextField.getText());
				} catch (NumberFormatException e2) {
					editor.statusBar.setError("Please input X and Y coordinates in right format!");
					return;
				}

				//Create superParam and add it to the operations list.
				PDStore store = editor.mainStore;

				PDOperationApplication opApp = new PDOperationApplication(store);
				opApp.addOperation(OpMoveShape.opID);

				PDMoveParam param = new PDMoveParam(store);
				param.addTargetShape((GUID) editor.selectBox.getSelectedItem());
				//System.out.println("trying to move: " + editor.selectBox.getSelectedItem().toString());
				param.addDx(X_Coordinate);
				param.addDy(Y_Coordinate);

				opApp.addSuperParameter(param);
				editor.getOperationList().add(opApp);
				store.commit();

				// Display the coordinates on the status bar.
				editor.statusBar.setCoordinates(xTextField.getText(), yTextField.getText());
				editor.statusBar.clearStatus();
			}
		}

	}

}
