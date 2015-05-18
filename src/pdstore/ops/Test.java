package pdstore.ops;

import pdstore.GUID;
import pdstore.OperationI;
import pdstore.PDStore;

public class Test implements OperationI {
	public final static GUID OPERATIONID = new GUID(
			"2dabaf40c8a911e1b21300224300a31a");

	public static void register(PDStore store) {
		store.setName(OPERATIONID, "Test operation");
		store.addLink(OPERATIONID, PDStore.OPERATION_IMPLEMENTATION_ROLEID,
				"pdstore.ops.Test");
		store.addLink(OPERATIONID, PDStore.PARAMETER_TYPE_ROLEID,
				PDStore.TYPE_TYPEID);
	}

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {
		System.out.println("Operation Test was invoked!");
		return null;
	}

}
