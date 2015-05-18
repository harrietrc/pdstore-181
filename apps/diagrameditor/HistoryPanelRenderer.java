package diagrameditor;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pdstore.GUID;
import diagrameditor.dal.PDCopyParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDShape;
import diagrameditor.ops.OpCopyShape;

/**
 * Class used to convert the operations from the format they were stored in the
 * database in into the format seen in the diagram editor and to change their
 * background colours to indicate commutative operations
 * 
 */
public class HistoryPanelRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = -623360668768283832L;

	private Color[] rowColors = new Color[5];
	private DiagramEditor editor;

	/**
	 * Constructor method
	 */
	public HistoryPanelRenderer(DiagramEditor editor) {
		setOpaque(true);
		this.editor = editor;
		setUp();
	}

	/**
	 * Method to format the operations text and set their background colours.
	 */
	public Component getListCellRendererComponent(final JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		PDOperationApplication opApp = (PDOperationApplication) value;

		GUID paramID = opApp.getSuperParameter().getId();
		GUID shapeID = null;

		// print out target shapeID (copy operation uses 'copy' instead)
		if (!opApp.getOperation().getName().equals("Copy Shape")) {
			shapeID = (GUID) DiagramEditor.mainStore.getInstance(paramID, CreateModel.TARGET_SHAPE_ROLEID);
		} else {
			PDCopyParam copyParam = PDCopyParam.load(DiagramEditor.mainStore, paramID);
			shapeID = copyParam.getCopy().getId();
		}

		setText(opApp.getOperation().getName() + " " + shapeID);

		// System.out.println(opApp.getOperation().getName() + " " + shapeID);

		// change background of operations to highlight commutative operations
		PDOperationApplication selected = getSelected(list);
		PDOperationApplication op = (PDOperationApplication) editor.drawPanel.listModel.get(index);
		List<PDOperationApplication> dependent = new ArrayList<PDOperationApplication>();
		if (selected != null) {
			// Change to getFamily if want to view dependencies in both
			// directions.
			dependent = editor.getOperationList().getDependents(selected);
			List<PDOperationApplication> commutative = editor.getOperationList().commutative(selected);

			// Set background colours
			if (isSelected) {
				// selected operation
				setBackground(rowColors[0]);
			} else if (dependent == null || !dependent.contains(op)) {
				if (commutative != null && commutative.contains(op)) {
					// operation is commutative but no dependent
					setBackground(rowColors[3]);
				} else {
					// operation not commutative and not dependent
					setBackground(list.getBackground());
				}
			} else if (dependent.contains(op)) {
				if (commutative != null && commutative.contains(op)) {
					// operation is dependent but not commutative
					// setBackground(rowColors[1]);
					setBackground(list.getBackground());
				} else {
					// operation is dependent and commutative
					// setBackground(rowColors[2]);
					setBackground(list.getBackground());
				}
			}
		} else {
			setBackground(list.getBackground());
		}
		// listener to check if a new operation has been selected
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// System.out.println("history panel-mouse pressed");
				list.repaint();
			}
		});
		return this;
	}

	/**
	 * Method to get which operation is currently selected.
	 * 
	 * @param list
	 *            , the JList containing the history
	 * @return the PDOperation or null if no operation is currently selected
	 */
	private PDOperationApplication getSelected(JList list) {
		int index = list.getSelectedIndex();

		if (index == -1) {
			return null;
		} else {
			PDOperationApplication opApp = (PDOperationApplication) editor.drawPanel.listModel.elementAt(index);
			GUID tarId;

			// target ID is always target_shape unless it is a copy op
			if (opApp.getOperation().getId().equals(OpCopyShape.opID)) {
				PDCopyParam cp = PDCopyParam.load(DiagramEditor.mainStore, opApp.getSuperParameter().getId());
				tarId = cp.getCopy().getId();
			} else {
				GUID tarObj = (GUID) DiagramEditor.mainStore.getInstance(opApp.getSuperParameter().getId(),
						CreateModel.TARGET_SHAPE_ROLEID);

				PDShape shape = PDShape.load(DiagramEditor.mainStore, tarObj);
				tarId = shape.getId();
			}

			// get target shape ID and update the JComboBox
			editor.selectBox.getModel().setSelectedItem(tarId);

			return opApp;
		}
	}

	/**
	 * Method to set up the background colours and listen for changes in the
	 * selected item in the list in order to repaint backgrounds.
	 */
	private void setUp() {
		// selected colour
		rowColors[0] = UIManager.getColor("ComboBox.selectionBackground");
		// is dependent and commutative
		rowColors[1] = new Color((float) 0.576470, (float) 0.576470, (float) 1.0);
		// is dependent but not commutative
		rowColors[2] = new Color((float) 0.576470, (float) 0.796078, (float) 0.901960);
		// not dependent but is commutative
		rowColors[3] = new Color((float) 0.903921, (float) 0.788235, (float) 0.788235, (float) 0.5);
		// not dependent or commutative
		rowColors[4] = Color.white;
	}

}
