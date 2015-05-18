package diagrameditor.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;
import diagrameditor.dal.PDCopyParam;
import diagrameditor.dal.PDDiagram;
import diagrameditor.dal.PDShape;

/**
 * Class to make a copy of a shape.
 * @author Remy & Simon
 *
 */
public class OpCopyShape implements OperationI {

	final public static GUID opID = new GUID("9e732ce1a9f711e1845678e4009ed4de");

	public static void register(PDStore store) {
		store.setName(opID, "Copy Shape");
		store.setType(opID, PDStore.OPERATION_TYPEID);
		store.setLink(opID, PDStore.OPERATION_IMPLEMENTATION_ROLEID, "diagrameditor.ops.OpCopyShape");
	}

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {

		// Get instance of the original shape.
		PDCopyParam copyParam;

		if (parameter instanceof GUID) {
			copyParam = PDCopyParam.load(store, (GUID) parameter);
		} else { // else already can be cast to a PDCopyParam
			copyParam = (PDCopyParam) parameter;
		}

		PDShape originalShape = copyParam.getTargetShape();

		// Create a new PDShape copy. Could be done through the OpNewShape.
		PDShape newShape = PDShape.load(store, copyParam.getCopy().getId());
		// Create offset so copy is not displayed over the original.
		long offset = 20;

		// Get the parameters from the original shape and set them to the newly
		// created shape.
		newShape.setType(originalShape.getType());
		newShape.setX(originalShape.getX() + offset);
		newShape.setY(originalShape.getY() + offset);
		newShape.setHeight(originalShape.getHeight());
		newShape.setWidth(originalShape.getWidth());
		newShape.setColor(originalShape.getColor());
		if (originalShape.getVisualizedData() != null){
			newShape.setVisualizedData(originalShape.getVisualizedData());			
		}

		// add the same visualized by link as the original shape.
		store.addLink(newShape.getId(), PDStore.VISUALIZED_BY_ROLEID,
				store.getInstance(originalShape.getId(), PDStore.VISUALIZED_BY_ROLEID));

		Object targetShapeList = copyParam.getTargetShapeList();
		store.addLink(targetShapeList, PDDiagram.roleShapeId, newShape.getId());

		copyParam.setTargetShape(newShape);

		// Reference the parent shape.
		newShape.setOriginalShape(originalShape);

		return null;
	}

}
