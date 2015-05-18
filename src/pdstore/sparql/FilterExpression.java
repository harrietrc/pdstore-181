package pdstore.sparql;

import java.util.Set;

public interface FilterExpression {
	boolean evaluate(Assignment assignment);

	String toString();

	Set<Variable> getVariables();
}
