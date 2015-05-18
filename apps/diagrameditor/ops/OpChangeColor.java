package diagrameditor.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;
import diagrameditor.CreateModel;
import diagrameditor.dal.PDColorParam;
import diagrameditor.dal.PDShape;
/**
 * Class to change shape color.
 * @author Remy & Simon
 *
 */
public class OpChangeColor implements OperationI {
	final public static GUID opID = new GUID("1e497841cb0211e195b178e4009ed4de");
	
	public static void register (PDStore store){
		store.setName(opID, "Change Color");
		store.setType(opID,  PDStore.OPERATION_TYPEID);
		store.setLink(opID, PDStore.PARAMETER_TYPE_ROLEID, CreateModel.COLOR_PARAM_TYPE_ID);
		store.setLink(opID, PDStore.OPERATION_IMPLEMENTATION_ROLEID,
				"diagrameditor.ops.OpChangeColor");
	}
	
	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {
		/*
		 * OpChangeColor uses a PDColorParam as it's parameter, which has getters for
		 * the target shape and a string of the color.
		 * PDColorParam is defined in diagrameditor.CreateModel.java
		 */
		
		PDColorParam colParam;
		if (parameter instanceof GUID){
			colParam = PDColorParam.load(store, (GUID) parameter);
		} else { //else already in format of PDColorParam
			colParam = (PDColorParam)parameter;	
		}
		
		PDShape ps = colParam.getTargetShape();
		String strCol = colParam.getNewColor();
		
		ps.setColor(strCol);
		return null;
	}

}
