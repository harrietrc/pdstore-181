package diagrameditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pdstore.GUID;
import diagrameditor.dal.PDCopyParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.ops.OpCopyShape;
import diagrameditor.ops.OpNewShape;

/**
 * Class to set up the history panel section of the diagram editor, listen to
 * the history buttons and perform the required actions caused by pressing these
 * buttons.
 * 
 */
public class HistoryPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String upString = "Move up";
	private static final String downString = "Move down";
	private DiagramEditor editor;

	public JList list;
	// History panel buttons
	private JButton upButton;
	private JButton downButton;
	private JButton deleteButton;

	/**
	 * Constructor
	 * 
	 * @param editor
	 *            , diagram editor
	 */
	public HistoryPanel(DiagramEditor editor) {
		super(new BorderLayout());
		this.editor = editor;
		setUpLayout();
	}

	/**
	 * Method to set up the layout of the history panel
	 */
	private void setUpLayout() {
		this.setMinimumSize(new Dimension(this.getPreferredSize().width, 100));
		this.list = editor.drawPanel.list;
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setSelectedIndex(0);
		this.list.setDragEnabled(true);
		this.list.setCellRenderer(new HistoryPanelRenderer(editor));

		this.list.addListSelectionListener(new ListSelectionListener() {
			// Clear the x and y coordinates on the status bar when an operation
			// in the history panel is selected.
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
					editor.statusBar.clearStatus();
					editor.statusBar.clearCoordinates();
				}
			}
		});

		// Create the Up Arrow button.
		JScrollPane listScrollPane = new JScrollPane(list);
		ImageIcon icon = createImageIcon("up");
		if (icon != null) {
			this.upButton = new JButton(icon);
			this.upButton.setSize(5, 5);
			this.upButton.setMargin(new Insets(0, 0, 0, 0));
		} else {
			this.upButton = new JButton("UP");
		}
		this.upButton.setToolTipText("Move the currently selected operation higher.");
		this.upButton.addActionListener(upButton(editor, list));
		this.upButton.setActionCommand(upString);

		// Create the Down Arrow button.
		icon = createImageIcon("down");
		if (icon != null) {
			this.downButton = new JButton(icon);
			this.downButton.setMargin(new Insets(0, 0, 0, 0));
		} else {
			this.downButton = new JButton("Down");
		}
		this.downButton.setToolTipText("Move the currently selected operation lower.");
		this.downButton.addActionListener(downButton(editor, list));
		this.downButton.setActionCommand(downString);

		// Create the Delete button.
		icon = createImageIcon("delete");
		if (icon != null) {
			this.deleteButton = new JButton(icon);
			this.deleteButton.setMargin(new Insets(0, 0, 0, 0));
		} else {
			this.deleteButton = new JButton("Delete");
		}
		this.deleteButton.setToolTipText("Delete the selected operation.");
		this.deleteButton.addActionListener(deleteButton(editor, list));
		this.deleteButton.setActionCommand("Delete");

		// Add buttons to the layout.
		JPanel upDownPanel = new JPanel(new GridLayout(1, 3));
		upDownPanel.add(upButton);
		upDownPanel.add(downButton);
		upDownPanel.add(deleteButton);

		JPanel buttonPane = new JPanel();
		buttonPane.add(upDownPanel);

		this.add(buttonPane, BorderLayout.NORTH);
		this.add(listScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Method to create an image
	 * 
	 * @param imageName
	 * @return an ImageIcon of the image
	 */
	private static ImageIcon createImageIcon(String imageName) {
		String imgLocation = imageName + ".jpg";
		java.net.URL imageURL = HistoryPanel.class.getResource(imgLocation);
		if (imageURL == null) {
			System.err.println("Resource not found: " + imgLocation);
			return null;
		} else {
			return new ImageIcon(imageURL);
		}
	}

	/**
	 * Method to listen to the delete button action events and delete the
	 * selected operation from the history list.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the delete button
	 */
	public ActionListener deleteButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PDOperationApplication curOpApp = (PDOperationApplication) list.getSelectedValue();

				// No operation has been selected
				if (curOpApp == null) {
					String message = "Please select one operation to delete";
					editor.statusBar.setError(message);
				} else {
					editor.statusBar.clearStatus();

					// check if we need to do cascading delete
					if (curOpApp.getOperation().getId().equals(OpNewShape.opID)
							|| curOpApp.getOperation().getId().equals(OpCopyShape.opID)) {

						// get the list of all operations which need to be deleted
						//System.out.println("HP - deleting a new or copy op.");
						List<PDOperationApplication> removing = editor.getOperationList().getDeleteOperations(curOpApp);

						// remove operations
						for (int i = removing.size() - 1; i >= 0; i--) {
							PDOperationApplication op = removing.get(i);
							editor.getOperationList().remove(op);
						}
					} else {
						// remove the selected operation
						//testOpList(editor.getOperationList());
						editor.getOperationList().remove(curOpApp);
						//testOpList(editor.getOperationList());
					}
					// push change to the database
					DiagramEditor.mainStore.commit();
				}
			}
		};
		return listener;
	}

	//method for testing. Prints the contents of an operationsList.
	public void testOpList(OperationList opList) {
		System.out.println("Current operationList: ");
		for (int i = 0; i < opList.size(); i++) {
			System.out.println(opList.get(i).getOperation() + " " + opList.get(i).getId());
		}
		System.out.println("");
	}

	/**
	 * Method to listen to the down button action events and move the selected
	 * operation down in the history list.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the down button
	 */
	public ActionListener downButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PDOperationApplication currentOp = (PDOperationApplication) list.getSelectedValue();
				int listSize = list.getModel().getSize();
				int currentIndex = list.getSelectedIndex();

				if (currentOp == null) {
					String message = "Please select one operation to move down";
					editor.statusBar.setError(message);

				} else if (currentIndex == listSize - 1) {
					// Cannot move the last operation downwards.
					String message = "Bottom operation! Can not move down again!";
					editor.statusBar.setError(message);

				} else {
					editor.statusBar.clearStatus();
					PDOperationApplication nextOp = (PDOperationApplication) list.getModel().getElementAt(
							currentIndex + 1);

					GUID curSuperPara = currentOp.getSuperParameter().getId();
					GUID nextSuperPara = nextOp.getSuperParameter().getId();
					GUID curTargetID = (GUID) DiagramEditor.mainStore.getInstance(curSuperPara,
							CreateModel.TARGET_SHAPE_ROLEID);
					GUID nextTargetID = (GUID) DiagramEditor.mainStore.getInstance(nextSuperPara,
							CreateModel.TARGET_SHAPE_ROLEID);

					if (checkValidDown(currentOp, nextOp, editor)) {
						// Case where a Copy operation moves below an operation
						// which acts on it.
						// The other operation now links to the Copied shape's
						// parent.
						if (currentOp.getOperation().getId().equals(OpCopyShape.opID)) {
							PDCopyParam copyParam = PDCopyParam.load(DiagramEditor.mainStore, curSuperPara);
							if (nextTargetID.equals(copyParam.getCopy().getId())) {
								// set the next operation's target to be the target of the previous operation
								DiagramEditor.mainStore.setLink(nextSuperPara, CreateModel.TARGET_SHAPE_ROLEID,
										curTargetID);
							}
						}

						// Switch the position of the two operations
						editor.getOperationList().move(currentOp, true);
					}
				}
			}
		};
		return listener;
	}

	/**
	 * Method to listen to the up button action events and move the selected
	 * operation up in the history list.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the up button
	 */
	public ActionListener upButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				PDOperationApplication currentOp = (PDOperationApplication) list.getSelectedValue();
				int currentIndex = list.getSelectedIndex();

				// No operation has been selected to move
				if (currentOp == null) {
					String message = "Please select one operation to move up";
					editor.statusBar.setError(message);
					// Cannot move the top operation upwards.
				} else if (currentIndex == 0) {
					String message = "Top operation! Can not move up again!";
					editor.statusBar.setError(message);
					// move the selected operation
				} else {
					editor.statusBar.clearStatus();
					PDOperationApplication previousOp = (PDOperationApplication) list.getModel().getElementAt(
							currentIndex - 1);

					// get operation information
					GUID preSuperPara = previousOp.getSuperParameter().getId();
					GUID curSuperPara = currentOp.getSuperParameter().getId();
					GUID curTargetID = (GUID) DiagramEditor.mainStore.getInstance(curSuperPara,
							CreateModel.TARGET_SHAPE_ROLEID);

					// Can if it is a valid swap
					if (checkValidUp(currentOp, previousOp, editor)) {

						// Case where an operation on a copied shape moves above it's Copy operation.
						// The operation will now be linked to the copied shape's parent.
						if (previousOp.getOperation().getId().equals(OpCopyShape.opID)) {
							PDCopyParam copyParam = PDCopyParam.load(DiagramEditor.mainStore, preSuperPara);
							if (copyParam.getCopy().getId().equals(curTargetID)) {
								// set the current operation's target to be the target of the previous operation
								DiagramEditor.mainStore.setLink(curSuperPara, CreateModel.TARGET_SHAPE_ROLEID,
										copyParam.getTargetShape().getId());

							}
						}

						// Switch the position of the two operations
						editor.getOperationList().move(currentOp, false);
					}
				}
			}
		};
		return listener;
	}

	/**
	 * Method to listen to the new button action events and create a new slide.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the new button
	 */
	public ActionListener newButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("new slide to be created.");
				//GUID newSlideID = new GUID();
				//DiagramEditor.mainStore.addLink(instance1, role2, instance2);
			}

		};
		return listener;
	}

	/**
	 * Method to check if it is a valid move to move currentOp above previousOp
	 * 
	 * @param currentOp
	 *            , the operation to move up
	 * @param previousOp
	 *            , the operation it will be moved above
	 * @param editor
	 * @return true if the move is valid, false otherwise
	 */
	private boolean checkValidUp(PDOperationApplication currentOp, PDOperationApplication previousOp,
			DiagramEditor editor) {
		// variables
		GUID preSuperPara = previousOp.getSuperParameter().getId();
		GUID curSuperPara = currentOp.getSuperParameter().getId();
		GUID preTargetID = (GUID) DiagramEditor.mainStore.getInstance(preSuperPara, CreateModel.TARGET_SHAPE_ROLEID);
		GUID curTargetID = (GUID) DiagramEditor.mainStore.getInstance(curSuperPara, CreateModel.TARGET_SHAPE_ROLEID);

		// check if moving an operation above the new operation which created the shape it works on
		if (previousOp.getOperation().getId().equals(OpNewShape.opID) && preTargetID.equals(curTargetID)) {
			String message = "Cannot do operations before shape is created!";
			editor.statusBar.setError(message);
			return false;
		}

		// otherwise it is a valid move
		//System.out.println("HistPan:checkVUp_ valid Up move");
		return true;
	}

	/**
	 * Method to check if it is a valid move to move the currentOp below nextOp
	 * 
	 * @param currentOp
	 *            , the operation to move down
	 * @param nextOp
	 *            , the operation it will be moved after
	 * @param editor
	 * @return true if the move is valid, false otherwise
	 */
	private boolean checkValidDown(PDOperationApplication currentOp, PDOperationApplication nextOp, DiagramEditor editor) {
		// operation information
		GUID curSuperParameterID = currentOp.getSuperParameter().getId();
		GUID curTargetID = (GUID) DiagramEditor.mainStore.getInstance(curSuperParameterID,
				CreateModel.TARGET_SHAPE_ROLEID);

		GUID nextSuperParameterID = nextOp.getSuperParameter().getId();
		GUID nextTargetID = (GUID) DiagramEditor.mainStore.getInstance(nextSuperParameterID,
				CreateModel.TARGET_SHAPE_ROLEID);

		// check if moving a new operation below an operation which works on the
		// shape
		// it produces
		if (currentOp.getOperation().getId().equals(OpNewShape.opID) && curTargetID.equals(nextTargetID)) {
			String message = "Cannot do operations before shape is created!";
			editor.statusBar.setError(message);
			return false;
			// check if moving a copy operation below an operation which works
			// on the shape
			// it produced
		} else if (currentOp.getOperation().getId().equals(OpNewShape.opID) && curTargetID.equals(nextTargetID)) {
			String message = "Cannot do operations before shape is created!";
			editor.statusBar.setError(message);
			return false;
		}
		// otherwise it is a valid move
		return true;
	}

}
