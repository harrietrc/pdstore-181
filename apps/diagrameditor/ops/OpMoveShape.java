package diagrameditor.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;
import diagrameditor.CreateModel;
import diagrameditor.dal.PDMoveParam;
import diagrameditor.dal.PDShape;

/**
 * Class to move shapes. 
 * @author Remy & Simon
 *
 */
public class OpMoveShape implements OperationI {
	final public static GUID opID = new GUID("e513c761cb0111e1991b78e4009ed4de");

	public static void register(PDStore store) {
		store.setName(opID, "Move Shape");
		store.setType(opID, PDStore.OPERATION_TYPEID);
		store.setLink(opID, PDStore.PARAMETER_TYPE_ROLEID, CreateModel.MOVE_PARAM_TYPE_ID);
		store.setLink(opID, PDStore.OPERATION_IMPLEMENTATION_ROLEID, "diagrameditor.ops.OpMoveShape");
	}

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {
		/*
		 * OpMoveShape uses a PDMoveParam as it's parameter, which has getters
		 * for the target shape and the dx, dy change in position. PDMoveParam
		 * is defined in diagrameditor.CreateModel.java
		 */

		PDMoveParam moveParam;

		if (parameter instanceof GUID) {
			moveParam = PDMoveParam.load(store, (GUID) parameter);
		} else {
			moveParam = (PDMoveParam) parameter;
		}
		//Update position.
		PDShape ps = moveParam.getTargetShape();
		ps.setX(ps.getX() + moveParam.getDx());
		ps.setY(ps.getY() + moveParam.getDy());

		return null;
	}

}
