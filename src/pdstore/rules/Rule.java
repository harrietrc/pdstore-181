package pdstore.rules;

import java.util.List;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.PDStoreI;
import pdstore.sparql.Query;

public class Rule {

	/**
	 * Precondition of the rule, in the form of a SPARQL query.
	 */
	Query pre;

	/**
	 * Postcondition of the rule, in the form of a list of changes to insert.
	 */
	List<PDChange<GUID, Object, GUID>> post;

	public Rule(Query pre, List<PDChange<GUID, Object, GUID>> post) {
		this.pre = pre;
		this.post = post;
	}

	public void apply(PDStoreI store) {
		// TODO Auto-generated method stub
	}
}
