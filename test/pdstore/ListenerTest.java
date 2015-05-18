package pdstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

public class ListenerTest {

	static PDStore store;

	@BeforeClass
	public static void setUpClass() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "ListenerTest-" + System.nanoTime();
		store = new PDStore(fileName);

        // used for testRegisteredViewListener.
        PDChange<GUID, Object, GUID> changeTemplate1 = new PDChange<GUID, Object, GUID>(
                ChangeType.LINK_ADDED, null, null, PDStore.CompleteTestRole2,
                null);
        CompleteRoleViewListener listener1 = new CompleteRoleViewListener(
                PDStore.CompleteTestRole2, PDStore.Complete_TestInstance1);
        store.getViewDispatcher().addListener(listener1, changeTemplate1);
    }

	/**
	 * This method tests for a prepared Change template the ListenerDispatcher
	 * index structure.
	 * 
	 * @param role2Id
	 * @param instance1
	 * @param instance2
	 * @param changeTemplate
	 */
	private void applyTemplate(final GUID role2Id, GUID instance1,
			GUID instance2, PDChange<GUID, Object, GUID> changeTemplate) {

		// Step1: test adding the listener with addListener()
		TestListener listener = new TestListener();

		store.getListenerDispatcher().addListener(listener, changeTemplate);

		useRegisteredListener(role2Id, instance1, instance2, listener);

		// Step2: test adding the listener with add()
		TestListener listener2 = new TestListener();

		listener2.getMatchingTemplates().add(changeTemplate);
		store.getListenerDispatcher().add(listener2);

		useRegisteredListener(role2Id, instance1, instance2, listener2);
	}

	/**
	 * The generic test that is executed in many different configurations.
	 * 
	 * @param role2Id
	 * @param instance1
	 * @param instance2
	 * @param listener
	 */
	private void useRegisteredListener(final GUID role2Id, GUID instance1,
			GUID instance2, TestListener listener) {
		GUID transaction = store.begin();
		store.addLink(transaction, instance1, role2Id, instance2);
		transaction = store.commit(transaction);

		listener.waitUntilFinished();
		assertTrue(listener.lastTransaction != null);
		assertEquals(transaction, listener.lastTransaction.get(0)
				.getTransaction());

		listener.lastTransaction = null;
		GUID transaction2 = store.begin();
		// add an arbitrary change that should not activate the listener.
		store.addLink(transaction2, new GUID(), new GUID(), new GUID());
		transaction2 = store.commit(transaction2);
		// Showing that the listener has not been activated for an arbitrary
		// change
		assertEquals(null, listener.lastTransaction);
	}

	@Test
	public final void testRoleListener() {
		final GUID role2Id = new GUID();
		GUID instance1 = new GUID();
		GUID instance2 = new GUID();
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				null, null, null, role2Id, null);
		applyTemplate(role2Id, instance1, instance2, changeTemplate);
	}

	@Test
	public final void testTripleListener() {
		final GUID role2Id = new GUID();
		GUID instance1 = new GUID();
		GUID instance2 = new GUID();

		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				null, null, instance1, role2Id, instance2);
		applyTemplate(role2Id, instance1, instance2, changeTemplate);
	}

	@Test
	public final void testInstance1RoleListener() {
		final GUID role2Id = new GUID();
		GUID instance1 = new GUID();
		GUID instance2 = new GUID();
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				null, null, instance1, role2Id, null);

		applyTemplate(role2Id, instance1, instance2, changeTemplate);
	}

	@Test
	public final void testInstance2RoleListener() {
		final GUID role2Id = new GUID();
		GUID instance1 = new GUID();
		GUID instance2 = new GUID();

		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				null, null, null, role2Id, instance2);
		applyTemplate(role2Id, instance1, instance2, changeTemplate);
	}

	@Test
	public final void testChangeTypeListener() {
		final GUID role2Id = new GUID();
		GUID instance1 = new GUID();
		GUID instance2 = new GUID();

		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(
				ChangeType.LINK_REMOVED, null, null, role2Id, instance2);
		TestListener listener = new TestListener();
		store.getListenerDispatcher().addListener(listener, changeTemplate);

		GUID transaction = store.begin();
		store.addLink(transaction, instance1, role2Id, instance2);
		store.commit(transaction);
		assertEquals(null, listener.lastTransaction);

		transaction = store.begin();
		store.removeLink(transaction, instance1, role2Id, instance2);
		transaction = store.commit(transaction);
		listener.waitUntilFinished();
		assertTrue(listener.lastTransaction != null);
	}

	@Test
	public final void testListener() {
		// local listener
		GUID transaction = store.begin();
		GUID roleA = new GUID();
		// TestRoleListener t = new TestRoleListener(roleA, false);
		ConsoleWritingListener t = new ConsoleWritingListener(roleA, false);
		store.getDetachedListenerList().add(t);
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		store.addLink(transaction, guid1, roleA, guid2);
		assertTrue(t.callCount == 0);
		store.commit(transaction);

		// The following is necessary to give asynchronous listener
		// time to finish
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(t.callCount > 0);
	}

	// only for embedded stores, not remote ones

	@Test
	public void testInterceptor() {
		GUID guid1 = new GUID();
		GUID guid2 = new GUID();
		GUID roleA = new GUID();

		GUID transaction = store.begin();
		TestRoleListener t = new TestRoleListener(roleA, true);
		store.getInterceptorList().add(t);
		store.addLink(transaction, guid1, roleA, guid2);
		Collection<Object> result = store.getInstances(transaction, guid1,
				roleA);
		assertTrue(result.contains(guid2));
		assertTrue(t.callCount == 0);
		store.commit(transaction);
		assertTrue(t.callCount > 0);
		transaction = store.begin();
		result = store.getInstances(transaction, guid1, roleA);

		// The following must hold true:
		// for that, the interceptors have to delete the cache
		assertTrue(!result.contains(guid2));
	}
}
