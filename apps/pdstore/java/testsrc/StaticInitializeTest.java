package pdstore.java.testsrc;

public class StaticInitializeTest {
	static {
		System.out.println("Static");
	}
	{
		System.out.println("Non-static block");
	}

	public static void main(String[] args) {
		StaticInitializeTest t = new StaticInitializeTest();
		StaticInitializeTest t2;
		for (int i=0; i < 4; i++){
			t2 = new StaticInitializeTest();
		}
	}
}
