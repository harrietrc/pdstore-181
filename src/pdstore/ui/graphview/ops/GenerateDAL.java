package pdstore.ui.graphview.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;

public class GenerateDAL implements OperationI {
	public final static GUID OPERATION_ID = new GUID(
			"285b2ed1cb1d11e19515842b2b9af4fd");

	public static void register(PDStore store) {
		store.setName(OPERATION_ID, "Generate DAL");
		store.addLink(OPERATION_ID, PDStore.OPERATION_IMPLEMENTATION_ROLEID,
				"pdstore.ui.graphview.ops.GenerateDAL");
		store.addLink(OPERATION_ID, PDStore.PARAMETER_TYPE_ROLEID,
				PDStore.TYPE_TYPEID);
	}

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {
		System.out.println("Operation Test was invoked!");
		return null;
	}

}
