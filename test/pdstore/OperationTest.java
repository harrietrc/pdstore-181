package pdstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

public class OperationTest implements OperationI {

	static PDStore store;

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "OperationTest-" + System.nanoTime();
		store = new PDStore(fileName);
	}

	@Test
	public final void testIdentityOperation() {
		GUID transaction = store.begin();
		GUID myOperation = new GUID();
		store.addLink(transaction, myOperation,
				PDStore.OPERATION_IMPLEMENTATION_ROLEID,
				"pdstore.OperationTest");
		assertEquals("Hello",
				store.applyOperation(transaction, myOperation, "Hello"));
		assertFalse("X".equals(store.applyOperation(transaction, myOperation,
				"Hi")));
	}

	@Override
	public Object apply(PDStore store, GUID transaction, Object parameter) {
		return parameter;
	}
}
