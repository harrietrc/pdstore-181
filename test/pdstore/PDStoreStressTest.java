package pdstore;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

public class PDStoreStressTest extends PDStoreTest {

	final int MAX_RUNS = 80;

	PDStore getFreshStore() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "StressTest-" + System.nanoTime();
		return new PDStore(fileName);
	}

	public final void testReadManyLinksInSameTransaction() {
		GUID roleA = new GUID();
		final int MAX_ADDS = MAX_RUNS;
		ArrayList<GUID> guidArray = new ArrayList<GUID>(MAX_ADDS);
		for (int i = 1; i < MAX_ADDS; ++i) {
			guidArray
					.add(new GUID(
							("aebac123" + i + "ccccccccccccccccccccccbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
									.substring(0, 32)));
		}

		PDStore store = getFreshStore();
		GUID transaction = store.begin();
		int error = -1;
		for (int i = 1; i < MAX_ADDS - 2; ++i) {
			store.addLink(transaction, guidArray.get(i), roleA,
					guidArray.get(i + 1));
			Collection<Object> result = store.getInstances(transaction,
					guidArray.get(i), roleA);

			if (!result.contains(guidArray.get(i + 1)))
				error = i;
		}
		assertTrue(error == -1);
		store.commit(transaction);

	}

	public final void testReadManyLinksInSeveralTransactions() {
		GUID roleA = new GUID();
		final int MAX_ADDS = MAX_RUNS;
		ArrayList<GUID> guidArray = new ArrayList<GUID>(MAX_ADDS);
		for (int i = 1; i < MAX_ADDS; ++i) {
			guidArray
					.add(new GUID(
							("aebac123" + i + "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
									.substring(0, 32)));
		}

		PDStore store = getFreshStore();
		int error = -1;
		for (int i = 1; i < MAX_ADDS - 2; ++i) {
			GUID transaction = store.begin();
			store.addLink(transaction, guidArray.get(i), roleA,
					guidArray.get(i + 1));
			Collection<Object> result = store.getInstances(transaction,
					guidArray.get(i), roleA);

			if (!result.contains(guidArray.get(i + 1)))
				error = i;
			GUID commitTransaction = store.commit(transaction);
		}
		assertTrue(error == -1);

	}

}
