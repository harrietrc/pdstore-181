package pdstore.applications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gsel521
 * 
 */
// TODO : move it to some other apt folder
public class LoadTest {

	static Long totalCount = (long) 1000000000;

	public static void main(String[] args) {

		// loadhashMapTester();
		// loadTreeMapTester();
		// loadhashTableTester();
		// loadConcurrenthashMapTester();
		// loadArrayTester();
		// loadarraylistTester();
	}

	private static void loadArrayTester() {

		// loop to add dummy data to check its capability
		long start = System.nanoTime();
		Long arr[] = new Long[5000000];
		for (int cnt = 0; cnt < 5000000; cnt++) {
			arr[cnt] = new Long(cnt);
			if (cnt % 100 == 0)
				System.out.println(cnt);
		}
		Long stop = System.nanoTime();
		Long interval = stop - start;
		System.out.println("Total Time take ----> " + interval / 1000000000
				+ " seconds");
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			System.out.println(cnt);
		}
	}

	private static void loadhashMapTester() {

		Map<Long, Long> dstr = new HashMap<Long, Long>();
		// loop to add dummy data to check its capability
		long start = System.nanoTime();
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			dstr.put(cnt, cnt);
			if (cnt % 100 == 0)
				System.out.println(cnt);
		}
		Long stop = System.nanoTime();
		Long interval = stop - start;
		System.out.println("Total Time take ----> " + interval / 1000000000
				+ " seconds");
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			System.out.println(cnt);
		}
	}

	private static void loadTreeMapTester() {

		Map<Long, Long> dstr = new TreeMap<Long, Long>();
		// loop to add dummy data to check its capability
		long start = System.nanoTime();
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			dstr.put(cnt, cnt);
			if (cnt % 100 == 0)
				System.out.println(cnt);
		}
		Long stop = System.nanoTime();
		Long interval = stop - start;
		System.out.println("Total Time take ----> " + interval / 1000000000
				+ " seconds");
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			System.out.println(cnt);
		}
	}

	private static void loadhashTableTester() {

		Map<Long, Long> dstr = new Hashtable<Long, Long>();
		// loop to add dummy data to check its capability
		long start = System.nanoTime();
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			dstr.put(cnt, cnt);
			if (cnt % 100 == 0)
				System.out.println(cnt);
		}
		Long stop = System.nanoTime();
		Long interval = stop - start;
		System.out.println("Total Time take ----> " + interval / 1000000000
				+ " seconds");
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			System.out.println(cnt);
		}
	}

	private static void loadConcurrenthashMapTester() {

		Map<Long, Long> dstr = new ConcurrentHashMap<Long, Long>();
		// loop to add dummy data to check its capability
		long start = System.nanoTime();
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			dstr.put(cnt, cnt);
			if (cnt % 100 == 0)
				System.out.println(cnt);
		}
		Long stop = System.nanoTime();
		Long interval = stop - start;
		System.out.println("Total Time take ----> " + interval / 1000000000
				+ " seconds");
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			System.out.println(cnt);
		}
	}

	private static void loadarraylistTester() {

		List<Long> lst = new ArrayList<Long>();
		// loop to add dummy data to check its capability
		long start = System.nanoTime();
		for (Long cnt = (long) 0; cnt <= totalCount; cnt++) {
			lst.add(cnt);
			System.out.println(cnt);
		}
		Long stop = System.nanoTime();
		Long interval = stop - start;
		System.out.println("Total Time take ----> " + interval / 1000000000
				+ " seconds");
	}
}
