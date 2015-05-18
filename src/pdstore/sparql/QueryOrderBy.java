package pdstore.sparql;

/**
 * Used in SPARQL query specifications to describe the order in which the
 * possible values of a particular SPARQL variable are returned.
 * 
 * @author Ganesh
 * 
 */
public class QueryOrderBy {
	Variable var;
	Order order;

	public enum Order {
		asc, desc
	};

	public QueryOrderBy(Variable var, Order order) {
		this.var = var;
		this.order = order;
	}
}
