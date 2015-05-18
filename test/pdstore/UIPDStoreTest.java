package pdstore;

import junit.framework.TestCase;

public class UIPDStoreTest extends TestCase {

	PDStore store = new PDStore("PDGenTest");

	public final void testDAL() {
		GUID id1 = new GUID("11111111111111111111111111111111");
		GUID id2 = new GUID("22222222222222222222222222222222");
		GUID id3 = new GUID("33333333333333333333333333333333");

		pdstore.ui.PDStore uiStore = new pdstore.ui.PDStore(store);
		store.addLink(id1, id2, id3);
		store.commit();

		assertEquals(id3, store.getInstance(id1, id2));
		assertEquals(id3, uiStore.getInstance(id1, id2));
	}
}
