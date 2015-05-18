package diagrameditor.filters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import pdstore.GUID;
import diagrameditor.CreateModel;
import diagrameditor.DiagramEditor;
import diagrameditor.OperationList;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.ops.OpChangeColor;
import diagrameditor.ops.OpCopyShape;
import diagrameditor.ops.OpMoveShape;
import diagrameditor.ops.OpNewShape;
import diagrameditor.ops.OpResizeShape;

/**
 * Class to create the filter which only shows operations which affect the
 * selected operations shape.
 */
public class ShapeFilter implements Filter {

	private boolean colour, move, resize; // test if these variables have been
											// set for the selected shape
	private GUID selectedShapeID;

	/**
	 * Method to add the operations which affect the shape of the selected
	 * operation to the listModel.
	 */
	@Override
	public void display(OperationList operations, DefaultListModel listModel) {

		// Clear list.
		listModel.removeAllElements();

		if (selectedShapeID != null) {
			GUID inheritedShapeID = selectedShapeID;
			List<PDOperationApplication> dep = operations
					.getFamily(selectedShapeID);

			colour = false;
			move = false;
			resize = false;

			/**
			 * Get all the copy and new operations from the list of dependencies
			 * and put them in a separate list, named tree.
			 */
			List<PDOperationApplication> tree = new ArrayList<PDOperationApplication>();
			for (PDOperationApplication op : dep) {
				if (op.getOperation().getId().equals(OpNewShape.opID)
						|| op.getOperation().getId().equals(OpCopyShape.opID)) {
					tree.add(op);
				}
			}

			/**
			 * Loop through all the operations starting from the
			 * back/end/bottom. If they are included in the tree list add them
			 * to the model. If they reference the selected shape add them to
			 * the model. If they are a generalised operation inherited by the
			 * shape and still affect the properties of the shape add them to
			 * the model.
			 */
			for (int i = operations.size() - 1; i >= 0; i--) {
				// Variables
				PDOperationApplication op = operations.get(i);
				GUID superParam = op.getSuperParameter().getId();
				GUID opShapeID = (GUID) DiagramEditor.mainStore.getInstance(
						superParam, CreateModel.TARGET_SHAPE_ROLEID);

				if (tree.contains(op)) {
					// If the tree list contains the operation add it to the
					// model
					listModel.add(0, op);
					/**
					 * Check if the operation is a copy operation and has the
					 * inherited shape id as its shapeID, if so then determine
					 * the shape which was targeted in the copy operation. Check
					 * to see if colour, move or resize was set in relation to
					 * that targeted shape and has been inherited by the shape
					 * we filtered on
					 */
					if (op.getOperation().equals(OpCopyShape.opID)
							&& opShapeID.equals(inheritedShapeID)) {
						inheritedShapeID = (GUID) DiagramEditor.mainStore
								.getInstance(superParam,
										CreateModel.COPY_ROLEID);
					}

				} else if (opShapeID.equals(selectedShapeID)
						&& !listModel.contains(op)) {
					// If the operation references the selected shape add it to
					// the model
					listModel.add(0, op);
					setParameter(op);
				}
				/**
				 * Check to operation to see if it is the most recent operation
				 * to set one of the shape parameters, if it is add it to the
				 * model.
				 */
				if (opShapeID.equals(inheritedShapeID)) {
					if (setParameter(op) && !listModel.contains(op)) {
						listModel.add(0, op);
					}
				}
			}
		} else {
			return;
		}
	}

	/**
	 * Method to determine if the given operation is the most recent operation
	 * to set a parameter of the shape, i.e. if this operation determines the
	 * shape colour, size or position.
	 * 
	 * @param op
	 *            , operation
	 * @return true if the operation is the first to set a parameter of the
	 *         shape
	 */
	private boolean setParameter(PDOperationApplication op) {
		GUID command = op.getOperation().getId();
		if (command.equals(OpChangeColor.opID) && colour == false) {
			colour = true;
			return true;
		} else if (command.equals(OpMoveShape.opID) && move == false) {
			move = true;
			return true;
		} else if (command.equals(OpResizeShape.opID) && resize == false) {
			resize = true;
			return true;
		}
		return false;
	}

	@Override
	public void setSelectedOp(PDOperationApplication selectedOp) {
		// No operation selected in the history.
		if (selectedOp == null) {
			selectedShapeID = null;
			return;
		}

		// Get the selected shape's ID.
		GUID targetID = (GUID) DiagramEditor.mainStore.getInstance(selectedOp
				.getSuperParameter().getId(), CreateModel.TARGET_SHAPE_ROLEID);
		selectedShapeID = targetID;
	}

}
