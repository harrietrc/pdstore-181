package pdstore.scripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;

import pdstore.PDStore;

public class Shell extends Interpreter {
	
	public static void main(String[] args) {
		// argument: the name of the PDStore
		String storeName = "default";
		if (args.length > 0)
			storeName = args[0];
		PDStore store = new PDStore(storeName);
		
		Shell s = new Shell(store);
		s.shell();
	}
	
	public Shell(PDStore store) {
		super(store);
	}

	void shell() {
		try {
			do {
				System.out.print("> ");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(System.in));
				String input = reader.readLine();

				parseString(input);
			} while (true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	List<Object> parseString(String input) {
		try {
			CharStream chars = new ANTLRStringStream(input);
			PDLLexer lex = new PDLLexer(chars);
			TokenStream tokens = new CommonTokenStream(lex);

			PDLParser parser = new PDLParser(tokens);
			parser.interpreter = this;
			return parser.pdlScript();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
