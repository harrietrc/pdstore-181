package pdstore.sparql;

import java.util.HashSet;
import java.util.Set;

import pdstore.changelog.PrimitiveType;

public class LessThanExpression implements FilterExpression {
	public Object arg1, arg2;

	public LessThanExpression(Object arg1, Object arg2) {
		super();
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	@Override
	public boolean evaluate(Assignment assignment) {
		Object first = assignment.getValue(arg1);
		Object second = assignment.getValue(arg2);
		return PrimitiveType.compare(first, second) < 0;
	}

	@Override
	public String toString() {
		return (arg1.toString() + " < " + arg2.toString());
	}

	@Override
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		if (this.arg1 instanceof Variable) {
			variables.add((Variable) this.arg1);
		}
		if (this.arg2 instanceof Variable) {
			variables.add((Variable) this.arg2);
		}
		return variables;
	}
}
