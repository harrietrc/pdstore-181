package pdstore;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import nz.ac.auckland.se.genoupe.tools.Stopwatch;

public class PDStoreStressTest2 extends PDStoreTest {

	final int MAX_RUNS = 800;
	

	PDStore getFreshStore() {
		// create a brand new store (including the metamodel) to prevent
		// interference with earlier test runs
		String fileName = "Stress2Test-" + System.nanoTime();
		return new PDStore(fileName);
	}


	public final void testReadManyLinksInSameTransaction() {
		GUID guid1 = new GUID();
		GUID roleA = new GUID();
		GUID roleB = new GUID();

		final int MAX_ADDS = MAX_RUNS;
		ArrayList<GUID> guidArray = new ArrayList<GUID>(MAX_ADDS);
		for (int i = 1; i < MAX_ADDS; ++i) {
			guidArray
					.add(new GUID(
							("debac123" + i + "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
									.substring(0, 32)));
		}

		PDStore store = getFreshStore();
		for (int i = 1; i < MAX_ADDS - 2; ++i) {
			GUID transaction = store.begin();
			store.addLink(transaction, guidArray.get(i), roleA,
					guidArray.get(i + 1));
			store.addLink(transaction, guid1, roleB, guidArray.get(i));
			store.commit(transaction);
		}

		GUID transaction = store.begin();
		int error = -1;
		for (int i = 1; i < MAX_RUNS - 2; ++i) {
			Collection<Object> result = store.getInstances(transaction,
					guidArray.get(i), roleA);

			if (!result.contains(guidArray.get(i + 1)))
				error = i;
		}
		assertTrue(error == -1);
		store.commit(transaction);
	}

	public final void testReadManyLinksInSeveralTransactions() {
		GUID guid1 = new GUID();
		GUID roleA = new GUID();
		GUID roleB = new GUID();

		final int MAX_ADDS = MAX_RUNS;
		ArrayList<GUID> guidArray = new ArrayList<GUID>(MAX_ADDS);
		for (int i = 1; i < MAX_ADDS; ++i) {
			guidArray
					.add(new GUID(
							("debac123" + i + "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
									.substring(0, 32)));
		}

		PDStore store = getFreshStore();
		for (int i = 1; i < MAX_ADDS - 2; ++i) {
			GUID transaction = store.begin();
			store.addLink(transaction, guidArray.get(i), roleA,
					guidArray.get(i + 1));
			store.addLink(transaction, guid1, roleB, guidArray.get(i));
			store.commit(transaction);
		}

		int error = -1;
		Stopwatch watchDemarcation = new Stopwatch();
		Stopwatch watchOperation = new Stopwatch();
		watchDemarcation.start();
		for (int i = 1; i < MAX_RUNS - 2; ++i) {
			GUID transaction = store.begin();
			watchDemarcation.stop();
			watchOperation.start();
			Collection<Object> result = store.getInstances(transaction,
					guidArray.get(i), roleA);

			if (!result.contains(guidArray.get(i + 1)))
				error = i;
			watchOperation.stop();
			watchDemarcation.start();
			store.commit(transaction);
		}
		watchDemarcation.stop();
		System.err.println("Trans: " + watchDemarcation.nanoSeconds()
				+ " OPs: " + watchOperation.nanoSeconds());

		assertTrue(error == -1);
	}
}
