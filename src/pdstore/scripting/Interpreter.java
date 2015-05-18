package pdstore.scripting;

import java.util.Iterator;

import nz.ac.auckland.se.genoupe.tools.Debug;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;

import pdstore.GUID;
import pdstore.PDStore;
import pdstore.sparql.Assignment;

/**
 * A PDL (Parsimonious Data Language) interpreter that receives the name of a
 * PDL script file as input.
 * 
 * @author clut002
 * 
 */
public class Interpreter {

	static {
		Debug.addDebugTopic(Interpreter.class);
	}

	PDStore store;

	/**
	 * If set, then the last query result object is made available in
	 * lastResult.
	 */
	public boolean getQuery = false;

	/**
	 * The result object of the query that was executed in the script last. Note
	 * that this works only if getQuery is set to true.
	 */
	public Iterator<Assignment<GUID, Object, GUID>> lastResult;

	public static void main(String[] args) {
		// first and only argument: the name of the script to execute
		if (args.length == 0) {
			System.out.println("Interpreter for PDStore scripts");
			System.out.println("Usage: call with script file name as argument");
		}

		String scriptName = args[0];
		PDStore store = new PDStore("default");

		Interpreter i = new Interpreter(store);
		i.executeScript(scriptName);
	}

	public Interpreter(PDStore store) {
		this.store = store;
	}

	void executeScript(String fileName) {
		try {
			CharStream chars = new ANTLRFileStream(fileName);
			PDLLexer lex = new PDLLexer(chars);
			TokenStream tokens = new CommonTokenStream(lex);

			PDLParser parser = new PDLParser(tokens);
			parser.interpreter = this;
			parser.pdlScript();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executeScriptInFile(String fileName) {
		try {
			executeScriptStream(new ANTLRFileStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executePDLString(String script) {
		try {
			executeScriptStream(new ANTLRStringStream(script));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executeScriptStream(CharStream chars) {
		try {
			PDLLexer lex = new PDLLexer(chars);
			TokenStream tokens = new CommonTokenStream(lex);

			PDLParser parser = new PDLParser(tokens);
			parser.interpreter = this;
			parser.pdlScript();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
