package diagrameditor.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;
import diagrameditor.CreateModel;
import diagrameditor.dal.PDDiagram;
import diagrameditor.dal.PDNewParam;
import diagrameditor.dal.PDShape;
/**
 * Class to create a new shape.
 * @author Remy & Simon
 *
 */
public class OpNewShape implements OperationI {
	final public static GUID opID = new GUID("b81a82c29b1111e1a6ca002215eb452f");

	public static void register(PDStore store) {
		store.setName(opID, "New Shape");
		store.setType(opID, PDStore.OPERATION_TYPEID);
		store.setLink(opID, PDStore.PARAMETER_TYPE_ROLEID, CreateModel.NEW_PARAM_TYPE_ID);
		store.setLink(opID, PDStore.OPERATION_IMPLEMENTATION_ROLEID, "diagrameditor.ops.OpNewShape");
	}

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {

		PDNewParam newParam;
		if (parameter instanceof GUID) {
			newParam = PDNewParam.load(store, (GUID) parameter);
		} else { // else already in format of PDNewParam
			newParam = (PDNewParam) parameter;
		}

		// default parameter for a new shape.
		long xPos = 50;
		long yPos = 50;
		long height = 50;
		long width = 50;
		String color = "BLACK";

		// Create and set the new shape.
		PDShape newShape = PDShape.load(store, newParam.getTargetShape().getId());
		newShape.setX(xPos);
		newShape.setY(yPos);
		newShape.setHeight(height);
		newShape.setWidth(width);
		if(newParam.getData() != null){
			newShape.setVisualizedData(newParam.getData());
		}
		
		//Add appropriate links to the database.
		store.addLink(newShape.getId(), PDStore.VISUALIZED_BY_ROLEID, newParam.getVisualizedBy());
		newShape.setColor(color);

		Object targetShapeList = newParam.getTargetShapeList();
		store.addLink(targetShapeList, PDDiagram.roleShapeId, newShape.getId());
		newParam.setTargetShape(newShape);


		return null;
	}
}
