package diagrameditor.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;
import diagrameditor.dal.PDResizeParam;
import diagrameditor.dal.PDShape;

/**
 * Class to resize shape color.
 * @author Remy & Simon
 *
 */
public class OpResizeShape implements OperationI{

	final public static GUID opID = new GUID("0148c470aac911e1a00478e4009ed4de");
	
	public static void register (PDStore store){
		store.setName(opID, "Resize Shape");
		store.setType(opID, PDStore.OPERATION_TYPEID );
		store.setLink(opID, PDStore.OPERATION_IMPLEMENTATION_ROLEID,
				"diagrameditor.ops.OpResizeShape");
	}
	
	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {

		PDResizeParam resizeParam;
		if (parameter instanceof GUID){
			resizeParam = PDResizeParam.load(store, (GUID) parameter);
		} else { 
			resizeParam = (PDResizeParam)parameter;	
		}
		
		//Set size to the new dimensions given.
		PDShape ps = resizeParam.getTargetShape();
		ps.setWidth(resizeParam.getWidth());
		ps.setHeight(resizeParam.getHeight());
		
		return null;
	}

}
