package pdstore.ui.graphview.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;

public class AddRelation implements OperationI {
	public final static GUID OPERATION_ID = new GUID(
			"285b2ecdcb1d11e19515842b2b9af4fd");

	public final static GUID PARAM_TYPEID = new GUID(
			"285b2ecccb1d11e19515842b2b9af4fd");
	public final static GUID PARAM_DIAGRAM_ROLEID = new GUID(
			"285b2ecbcb1d11e19515842b2b9af4fd");
	public final static GUID PARAM_TYPE_ROLEID = new GUID(
			"285b2ecacb1d11e19515842b2b9af4fd");

	public static void register(PDStore store) {
		store.setName(OPERATION_ID, "Add Relation");
		store.addLink(OPERATION_ID, PDStore.OPERATION_IMPLEMENTATION_ROLEID,
				"pdstore.ui.graphview.ops.AddRelation");
		store.addLink(OPERATION_ID, PDStore.PARAMETER_TYPE_ROLEID, PARAM_TYPEID);
	}

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {
		System.out.println("Operation Test was invoked!");
		return null;
	}

}
