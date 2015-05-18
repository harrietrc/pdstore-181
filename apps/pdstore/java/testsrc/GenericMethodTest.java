package pdstore.java.testsrc;

public class GenericMethodTest {
	public static < E > void printArray(E[] inputArray) {
		// Display array elements              
		for (E element : inputArray) {        
			System.out.printf( "%s ", element );
		}
	}

	public static void main (String args[]) {
		Double[] doubleArray = { 1.1, 2.2, 3.3, 4.4 };
		
		printArray( doubleArray );
	} 
}
