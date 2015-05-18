package diagrameditor.filters;

import java.util.List;

import javax.swing.DefaultListModel;

import pdstore.GUID;
import diagrameditor.CreateModel;
import diagrameditor.DiagramEditor;
import diagrameditor.OperationList;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.ops.OpCopyShape;
import diagrameditor.ops.OpNewShape;

/**
 * Class to create the filters which only shows the operations in the dependency
 * tree of the selected operation, implements Filter.
 * 
 */
public class DependencyFilter implements Filter {
	private GUID rootShapeID;

	/**
	 * Method to add the operations in the dependency tree of the selected
	 * operation to the listModel.
	 */
	@Override
	public void display(OperationList operations, DefaultListModel listModel) {
		// Clear list.
		listModel.removeAllElements();

		if (rootShapeID != null) {
			List<PDOperationApplication> ops = operations
					.getFamily(rootShapeID);

			for (PDOperationApplication op : ops) {
				listModel.addElement(op);
			}
		}
	}

	/**
	 * Method to change the selected operation
	 */
	@Override
	public void setSelectedOp(PDOperationApplication selectedOp) {
		// No operation selected in the history.
		if (selectedOp == null) {
			rootShapeID = null;
			return;
		}

		// TODO: move to OperationList

		// Find the root of the selected shape in the history.
		// This will be used to display the entire dependency tree.

		GUID supPar = selectedOp.getSuperParameter().getId();
		if (selectedOp.getOperation().getId().equals(OpNewShape.opID)) {
			rootShapeID = (GUID) DiagramEditor.mainStore.getInstance(supPar,
					CreateModel.TARGET_SHAPE_ROLEID);
			return;
		} else if (selectedOp.getOperation().getId().equals(OpCopyShape.opID)) {
			// If the operation is a Copy, the parent shape's ID is the
			// targetID.
			rootShapeID = (GUID) DiagramEditor.mainStore.getInstance(supPar,
					CreateModel.COPY_ROLEID);
		} else {
			rootShapeID = (GUID) DiagramEditor.mainStore.getInstance(supPar,
					CreateModel.TARGET_SHAPE_ROLEID);
		}

		// If the selected operation is a New, then it is the root shape of the
		// tree.
		// If its not a New, then find the root shape.
		while (selectedOp.getPrevious() != null) {
			selectedOp = selectedOp.getPrevious();
			GUID sPar = selectedOp.getSuperParameter().getId();
			GUID targetID = (GUID) DiagramEditor.mainStore.getInstance(sPar,
					CreateModel.TARGET_SHAPE_ROLEID);
			GUID copyID = (GUID) DiagramEditor.mainStore.getInstance(sPar,
					CreateModel.COPY_ROLEID);

			// If the operation is a New on the hierarchical path,
			// then it is the root shape of the tree.
			if (selectedOp.getOperation().getId().equals(OpNewShape.opID)) {
				if (targetID.equals(rootShapeID)) {
					rootShapeID = targetID;
					break;
				}
			}
			// If the operation is a Copy on the hierarchical path,
			// continue looking using the targetID
			else if (selectedOp.getOperation().getId().equals(OpCopyShape.opID)) {
				if (targetID.equals(rootShapeID)) {
					rootShapeID = copyID;
				}
			}
		}
	}
}
