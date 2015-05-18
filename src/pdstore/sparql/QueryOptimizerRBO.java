package pdstore.sparql;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pdstore.GUID;
import pdstore.PDChange;

public class QueryOptimizerRBO extends QueryOptimizerBase {

	/**
	 * This function is computing a complexity of a triple for a position in the
	 * where clause. Currently based only on number of variables, in future
	 * based on statistics.
	 * 
	 * @param variableAssignment
	 * @return
	 */
	@Override
	public long costInWhereClause(Query q, Set<Variable> assignedVariables,
			PDChange<GUID, Object, GUID> currentWhere) {
		return +instanceCost(currentWhere.getInstance1(), assignedVariables)
				+ roleCost(currentWhere.getRole2(), assignedVariables)
				+ instanceCost(currentWhere.getInstance2(), assignedVariables);
	}

	private static int instanceCost(Object o,
			Collection<Variable> assignedVariables) {
		// null values are like variables, but don't bring benefit,
		// should be done as late as possible.
		if (o == null)
			return 100;

		if (o instanceof Variable) {
			// using an assigned variable seems to be a positive thing.
			// It ensures that previous assigmnents are put to good use as the
			// query processing goes on.
			// so we set the cost to the lowest possible:
			if (assignedVariables.contains(o))
				return 0;

			// an unassigned variable incurs cost, but brings hope to be
			// assigned,
			// so the cost is lower than for a null value:
			return 90;
		}

		// constants incur no cost, but not as good as using an assigned
		// variable:
		// might have low selectivity? Therefore some cost.
		// TODO think this through
		if (o instanceof GUID)
			return 10;
		return 0;
	}

	private static int roleCost(Object o, Collection<Variable> assignedVariables) {
		// null values are like variables, but don't bring benefit,
		// should be done as late as possible.
		if (o == null)
			return 100;

		if (o instanceof Variable) {
			// using an assigned variable seems to be a positive thing.
			// It ensures that previous assigmnents are put to good use as the
			// query processing goes on.
			// so we set the cost to the lowest possible:
			if (assignedVariables.contains(o))
				return 0;

			// an unassigned variable incurs cost, but brings hope to be
			// assigned,
			// so the cost is lower than for a null value:
			return 90;
		}

		// constants incur no cost, but not as good as using an assigned
		// variable:
		// might have low selectivity? Therefore some cost.
		// TODO think this through
		if (o instanceof GUID)
			return 10;
		return 0;
	}
}