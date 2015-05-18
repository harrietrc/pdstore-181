package pdstore.sparql;

import java.util.HashSet;
import java.util.Set;

/**
 *  Superclass for binary filters, making reuse of some simple functions possible.
 * 
 * 
 * @author gweb017
 *
 */
public abstract class BinaryFilterOperator  implements FilterExpression{

	public Object arg1;
	public Object arg2;
	protected static String operatorSymbol = "=";

	@Override
	public String toString() {
		return (arg1.toString() + " " + getOperatorSymbol() + " " + arg2.toString());
	}

	public BinaryFilterOperator() {
		super();
	}

	public String getOperatorSymbol() {
		return operatorSymbol;
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

	@Override
	public boolean evaluate(Assignment assignment) {
		Object first = assignment.getValue(arg1);
		Object second = assignment.getValue(arg2);
		return binaryEvaluate(first, second);
	}

	protected abstract boolean binaryEvaluate(Object first, Object second);

}