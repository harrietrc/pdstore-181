package diagrameditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.MapIterator;
import pdstore.GUID;
import pdstore.PDStore;
import pdstore.generic.GenericLinkedList;
import diagrameditor.dal.PDCopyParam;
import diagrameditor.dal.PDDiagram;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.ops.OpCopyShape;
import diagrameditor.ops.OpNewShape;

/**
 * Extends GenericLinkedList and implements diagram editor specific methods.
 * 
 */
public class OperationList implements Iterable<PDOperationApplication> {

	private boolean debug = false;
	private static final long serialVersionUID = 1L;

	PDStore store;
	GenericLinkedList<GUID, Object, GUID, GUID> list;

	public OperationList(PDStore store, PDDiagram diagram, GUID collectionRole, GUID nextRole) {
		this.store = store;
		list = new GenericLinkedList<GUID, Object, GUID, GUID>(store, diagram.getId(), collectionRole, nextRole);
	}

	/**
	 * Method to move an operation up or down in the list. Will jump commutative
	 * blocks of operations.
	 * 
	 * @param op
	 * @param down
	 */
	public void move(PDOperationApplication op, boolean down) {
		int steps = 1;
		// get number of operations to jump, affect my commutative
		// neighbourhoods
		if (down) {
			steps = getDownCommutative(op).size();
		} else {
			steps = getUpCommutative(op).size();
		}
		// minimum step size if no adjacent commutative neighbourhood
		if (steps < 1) {
			steps = 1;
		}
		// remove selected operation
		int index = list.indexOf(op.getId());
		list.remove(index);

		// add selected operation into new position
		if (down) {
			list.add(index + steps, op.getId());
		} else {
			list.add(index - steps, op.getId());
		}

		store.commit();
	}

	/**
	 * Method to determine the operations which need to be deleted when deleting
	 * the selected operation (cascading delete)
	 * 
	 * @param operation
	 *            , the operation being deleted
	 * @return a list of operations which also need to be deleted when deleting
	 *         the selected operation
	 */
	public List<PDOperationApplication> getDeleteOperations(PDOperationApplication operationApp) {
		List<PDOperationApplication> dep = new ArrayList<PDOperationApplication>();
		dep.add(operationApp);
		List<GUID> opsToCheck = new ArrayList<GUID>();

		// list of operations which we check all other operations for dependency on
		GUID paramID = operationApp.getSuperParameter().getId();
		GUID shapeID;

		if (operationApp.getOperation().getId().equals(OpCopyShape.opID)) {
			PDCopyParam cp = PDCopyParam.load(store, paramID);
			shapeID = cp.getCopy().getId();
		} else {
			shapeID = (GUID) store.getInstance(paramID, CreateModel.TARGET_SHAPE_ROLEID);
		}
		opsToCheck.add(shapeID);

		// loop through all operations
		for (PDOperationApplication opApp : this) {
			GUID pId = opApp.getSuperParameter().getId();

			// get the target of the operation (if copy we want original src)
			GUID opId = (GUID) store.getInstance(pId, CreateModel.TARGET_SHAPE_ROLEID);

			// if operation is not currently in the dependent list and it's shape id links
			// to an operation on the check list add it to the list of dependent operations
			if (opsToCheck.contains(opId) && !dep.contains(opApp)) {
				dep.add(opApp);
				if (opApp.getOperation().getId().equals(OpCopyShape.opID)) {
					PDCopyParam cp = PDCopyParam.load(store, opApp.getSuperParameter().getId());
					opsToCheck.add(cp.getCopy().getId());
				}
			}
		}
		// return list of dependent operations
		return dep;
	}

	/**
	 * Method to return a list of PDOperations which have a dependency between
	 * the selected operation's shape and themselves
	 * 
	 * @param operationApp
	 *            , the operation to find the dependencies for
	 * @return a list of PDOperations which contain a dependency to the given
	 *         operation, including the given operation.
	 */
	public List<PDOperationApplication> getFamily(PDOperationApplication operationApp) {
		List<PDOperationApplication> dep = new ArrayList<PDOperationApplication>();

		// List of shapes that are part of the same dependency tree as the
		// selected operation.
		ArrayList<GUID> shapesToFilter = new ArrayList<GUID>();

		if (operationApp != null) {
			PDOperationApplication tempOp = (PDOperationApplication) operationApp;
			GUID tmpSuperPar = tempOp.getSuperParameter().getId();
			GUID tmpTargetID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.TARGET_SHAPE_ROLEID);

			GUID shapeID = null;
			// If the selected operation is a Copy, the parent shape's ID is the
			// targetID
			if (tempOp.getOperation().getId().equals(OpCopyShape.opID)) {
				shapeID = tmpTargetID;
			} else {
				shapeID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.COPY_ROLEID);
			}

			// If the selected operation is a New, then it is the root shape of
			// the tree.
			// If its not a New, then find the root shape.
			if (!tempOp.getOperation().getId().equals(OpNewShape.opID)) {

				while (tempOp.getPrevious() != null) {
					tempOp = tempOp.getPrevious();
					tmpSuperPar = tempOp.getSuperParameter().getId();
					tmpTargetID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar,
							CreateModel.TARGET_SHAPE_ROLEID);

					// If the operation is a New on the hierarchical path,
					// then it is the root shape of the tree.
					if (tempOp.getOperation().getId().equals(OpNewShape.opID)) {
						if (tmpTargetID.equals(shapeID)) {
							break;
						}
					} // If the operation is a Copy on the hierarchical path,
						// continue looking using the targetID
					else if (tempOp.getOperation().getId().equals(OpCopyShape.opID)) {
						if (tmpTargetID.equals(shapeID)) {
							GUID tmpCopyID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar,
									CreateModel.COPY_ROLEID);
							shapeID = tmpCopyID;
						}
					}
				}
			}

			tmpSuperPar = tempOp.getSuperParameter().getId();
			tmpTargetID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.TARGET_SHAPE_ROLEID);
			shapesToFilter.add(tmpTargetID);

			dep.add(tempOp);
			while (tempOp.getNext() != null) {
				tempOp = tempOp.getNext();
				tmpSuperPar = tempOp.getSuperParameter().getId();
				tmpTargetID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.TARGET_SHAPE_ROLEID);
				GUID tmpCopyID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.COPY_ROLEID);

				if (shapesToFilter.contains(tmpTargetID)) {
					dep.add(tempOp);
				} else if (tempOp.getOperation().getId().equals(OpCopyShape.opID) && shapesToFilter.contains(tmpCopyID)) {
					shapeID = tmpTargetID;
					shapesToFilter.add(shapeID);
					dep.add(tempOp);
				}
			}
		}
		// return list of dependent operations
		return dep;
	}

	/**
	 * Method to get the list of operations which part of the same dependency
	 * tree as the selected shape
	 * 
	 * @param selectedShapeID
	 *            , the selected shape ID
	 * @return a list of operations which are part of the shape's dependency
	 *         tree
	 */
	public List<PDOperationApplication> getFamily(GUID selectedShapeID) {
		List<PDOperationApplication> dep = new ArrayList<PDOperationApplication>();

		// List of shapes that are part of the same dependency tree as the
		// selected operation.
		ArrayList<GUID> shapesToFilter = new ArrayList<GUID>();

		if (list.size() != 0) {
			PDOperationApplication tempOp = null;
			GUID targetID = null;

			// find the operation which created the selected shape
			for (PDOperationApplication op : this) {
				GUID sPar = op.getSuperParameter().getId();
				targetID = (GUID) DiagramEditor.mainStore.getInstance(sPar, CreateModel.TARGET_SHAPE_ROLEID);
				if (targetID.equals(selectedShapeID)) {
					tempOp = op;
				}
			}

			if (tempOp == null) {
				return dep;
			}

			// If the selected operation is a New, then it is the root shape of
			// the tree.
			// If its not a New, then find the root shape.
			if (!tempOp.getOperation().getId().equals(OpNewShape.opID)) {

				while (tempOp.getPrevious() != null) {
					tempOp = tempOp.getPrevious();

					GUID tmpSPar = tempOp.getSuperParameter().getId();
					GUID tmpTarID = (GUID) DiagramEditor.mainStore
							.getInstance(tmpSPar, CreateModel.TARGET_SHAPE_ROLEID);

					// If the operation is a New on the hierarchical path,
					// then it is the root shape of the tree.
					if (tempOp.getOperation().getId().equals(OpNewShape.opID)) {
						if (tmpTarID.equals(targetID)) {
							break;
						}
					} // If the operation is a Copy on the hierarchical path,
						// continue looking using the targetID
					else if (tempOp.getOperation().getId().equals(OpCopyShape.opID)) {
						if (tmpTarID.equals(targetID)) {
							targetID = tmpTarID;
						}
					}
				}
			}

			GUID tmpSuperPar = tempOp.getSuperParameter().getId();
			GUID tmpTargetID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.TARGET_SHAPE_ROLEID);

			shapesToFilter.add(tmpTargetID);

			dep.add(tempOp);
			while (tempOp.getNext() != null) {
				tempOp = tempOp.getNext();
				tmpSuperPar = tempOp.getSuperParameter().getId();
				tmpTargetID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.TARGET_SHAPE_ROLEID);
				GUID tmpCopyID = (GUID) DiagramEditor.mainStore.getInstance(tmpSuperPar, CreateModel.COPY_ROLEID);

				if (shapesToFilter.contains(tmpTargetID)) {
					dep.add(tempOp);
				} else if (tempOp.getOperation().getId().equals(OpCopyShape.opID) && shapesToFilter.contains(tmpCopyID)) {
					targetID = tmpTargetID;
					shapesToFilter.add(targetID);
					dep.add(tempOp);
				}
			}
		}
		// return list of dependent operations
		return dep;
	}

	/**
	 * Method to return a list of PDOperations which depend on the given
	 * operation.
	 * 
	 * @param operationApp
	 * @return a list of operations which are dependent upon the given operation
	 */
	public List<PDOperationApplication> getDependents(PDOperationApplication operationApp) {
		// Given operation must be a new or copy operation to have other
		// operations depending on it.
		if (!operationApp.getOperation().getId().equals(OpNewShape.opID)
				&& !operationApp.getOperation().getId().equals(OpCopyShape.opID)) {
			return null;
		}
		List<PDOperationApplication> dep = new ArrayList<PDOperationApplication>();
		List<GUID> opsToCheck = new ArrayList<GUID>();

		GUID superParameterID = operationApp.getSuperParameter().getId();
		GUID targetID = (GUID) store.getInstance(superParameterID, CreateModel.TARGET_SHAPE_ROLEID);

		opsToCheck.add(targetID);

		for (PDOperationApplication opApp : this) {
			GUID opId = null;
			// get shape of operation, or if operation is a copy the target id
			if (opApp.getOperation().getId().equals(OpCopyShape.opID)) {
				opId = targetID;
			} else {
				opId = targetID;
			}
			// if operation is not currently in the dependent list and it's id
			// links
			// to an operation on the check list add it to the list of dependent
			// operations
			if (opsToCheck.contains(opId) && !dep.contains(opApp)) {
				dep.add(opApp);
				if (opApp.getOperation().getId().equals(OpCopyShape.opID)) {
					opsToCheck.add(targetID);
				}
			}
		}
		return dep;
	}

	/**
	 * Method to find the commutative neighbourhood above the selected
	 * operation. An operation is commutative with the given operation if the
	 * order of their execution does not affect the resultant diagram.
	 * 
	 * @param selected
	 *            , the selected PDOperation
	 * @return list containing the operations in the commutative neighbourhood.
	 */
	public List<PDOperationApplication> getUpCommutative(PDOperationApplication selected) {
		List<PDOperationApplication> commutative = new ArrayList<PDOperationApplication>();

		int position = list.indexOf(selected.getId());
		for (int i = position - 1; i >= 0; i--) {
			// find consecutive commutative operations above the selected
			// operation
			PDOperationApplication op = PDOperationApplication.load(store, list.get(i));
			if (isCommutative(op, selected)) {
				commutative.add(op);
			} else {
				return commutative;
			}
		}
		// return list of operations in the commutative neighbourhood
		return commutative;
	}

	/**
	 * Method to find the commutative neighbourhood below the selected
	 * operation. An operation is commutative with the given operation if the
	 * order of their execution does not affect the resultant diagram.
	 * 
	 * @param selected
	 *            , the selected PDOperation
	 * @return list containing the operations in the commutative neighbourhood.
	 */
	public List<PDOperationApplication> getDownCommutative(PDOperationApplication selected) {
		List<PDOperationApplication> commutative = new ArrayList<PDOperationApplication>();

		int position = list.indexOf(selected.getId());
		for (int i = position + 1; i < list.size(); i++) {
			// find consecutive commutative operations above the selected
			// operation
			PDOperationApplication op = PDOperationApplication.load(store, list.get(i));
			if (isCommutative(selected, op)) {
				commutative.add(op);
			} else {
				return commutative;
			}
		}
		// return list of operations in the commutative neighbourhood
		return commutative;
	}

	/**
	 * Method to return a list of PDOperations which are commutative with the
	 * selected operation. An operation is commutative with the given operation
	 * if the order of their execution does not affect the resultant diagram.
	 * 
	 * @param selected
	 *            , the selected operation
	 * @return a list of PDOperations which are commutative operations.
	 */
	public List<PDOperationApplication> commutative(PDOperationApplication selected) {
		// find above commutative neighbourhood
		List<PDOperationApplication> commutative = getUpCommutative(selected);
		// add below commutative neighbourhood
		commutative.addAll(getDownCommutative(selected));
		return commutative;
	}

	/**
	 * Method to determine if two operations are commutative with one another.
	 * An operation is commutative with the given operation if the order of
	 * their execution does not affect the resultant diagram.
	 * 
	 * @param op1
	 *            , the above operation
	 * @param op2
	 *            , the below operation
	 * @return
	 */
	public boolean isCommutative(PDOperationApplication op1, PDOperationApplication op2) {
		// Get operation information
		GUID op1SuperParameterID = op1.getSuperParameter().getId();
		GUID op1TargetID = (GUID) DiagramEditor.mainStore.getInstance(op1SuperParameterID,
				CreateModel.TARGET_SHAPE_ROLEID);

		GUID op2SuperParameterID = op1.getSuperParameter().getId();
		GUID op2TargetID = (GUID) DiagramEditor.mainStore.getInstance(op2SuperParameterID,
				CreateModel.TARGET_SHAPE_ROLEID);

		GUID op1CopyID = new GUID();
		GUID op2CopyID = new GUID();

		if (op1.getOperation().getId().equals(OpCopyShape.opID)) {
			op1CopyID = (GUID) DiagramEditor.mainStore.getInstance(op1SuperParameterID, CreateModel.COPY_ROLEID);
			op2CopyID = (GUID) DiagramEditor.mainStore.getInstance(op2SuperParameterID, CreateModel.COPY_ROLEID);
		}

		if (debug) {
			System.out.println(op1.getOperation().getImplementation() + " " + op1TargetID);
			System.out.println(op2.getOperation().getImplementation() + " " + op2TargetID);
			if (op1.getOperation().getId().equals(OpCopyShape.opID)) {
				System.out.println(op1TargetID);
			}
		}
		// test if they are the same operations
		if (op1.getOperation().getId().equals(op2.getOperation().getId()) && op1TargetID.equals(op2TargetID)) {
			return false;
		}
		// if operation 2 is dependent on operation 2 they are not commutative
		if (getDependents(op1) != null && getDependents(op1).contains(op2)) {
			return false;
		}
		// test if they are shape disjoint
		// if both operations are copies:
		if (op1.getOperation().getId().equals(OpCopyShape.opID) && op2.getOperation().getId().equals(OpCopyShape.opID)) {

			// if the lower copy is not dependent upon the upper copy
			if (!op1TargetID.equals(op2CopyID) && !op1CopyID.equals(op2CopyID)) {
				return true;
			} else {
				return false;
			}
			// if op1 is a copy
		} else if (op1.getOperation().getId().equals(OpCopyShape.opID)
				&& !op2.getOperation().getId().equals(OpCopyShape.opID)) {
			if (!op2TargetID.equals(op1TargetID) && !op2TargetID.equals(op1CopyID)) {
				return true;
			} else {
				return false;
			}
			// if op2 is a copy
		} else if (!op1.getOperation().getId().equals(OpCopyShape.opID)
				&& op2.getOperation().getId().equals(OpCopyShape.opID)) {
			if (!op1TargetID.equals(op2CopyID) && !op1TargetID.equals(op2TargetID)) {
				return true;
			} else {
				return false;
			}
			// neither operation is a copy operation
		} else {
			if (!op1TargetID.equals(op2TargetID)) {
				return true;
			}
		}

		// test if type disjoint and neither operation is a new operation
		if (!op1.getOperation().getId().equals(op2.getOperation().getId())
				&& !op1.getOperation().equals(OpNewShape.opID) && !op2.getOperation().equals(OpNewShape.opID)) {
			return true;
		}
		return false;
	}

	public Iterator<PDOperationApplication> iterator() {
		return new MapIterator<GUID, PDOperationApplication>(list.iterator()) {
			public PDOperationApplication map(GUID nextInBase) {
				return PDOperationApplication.load(store, nextInBase);
			}
		};
	}

	public void add(PDOperationApplication element) {
		list.add(element.getId());
	}

	public void remove(PDOperationApplication element) {
		list.remove(element.getId());
	}

	public int size() {
		return list.size();
	}

	public PDOperationApplication get(int i) {
		return PDOperationApplication.load(store, list.get(i));
	}
}
