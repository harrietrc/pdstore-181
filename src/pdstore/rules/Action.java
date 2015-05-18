package pdstore.rules;

import java.util.Map;

import pdstore.GUID;

public class Action extends GUID {

	String name;

	public Action(String name) {
		super();
		this.name = name;
	}

	public String toString() {
		return "!" + name;
	}

	public static Object getValue(Map<Action, Object> assignment, Object arg) {
		if (arg instanceof Action && assignment.containsKey(arg)) {
			return assignment.get(arg);

		} else {
			return arg;
		}
	}
}
