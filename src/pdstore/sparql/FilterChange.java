package pdstore.sparql;

import java.util.HashSet;
import java.util.Set;

import pdstore.PDChange;
import pdstore.generic.ChangeTemplateKind;
import pdstore.generic.PDStoreI;
import pdstore.generic.Pairable;

/**
 * This type of filter is directly referring to triple information in the database.
 * Its chief application is to test for nonexistence of triples. For that purpose
 * the attribute nonNegated should be false.
 * 
 * 
 * @author gweb017
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 */
public class FilterChange<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends PDChange<TransactionType, InstanceType, RoleType> implements
		FilterExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	PDStoreI<TransactionType, InstanceType, RoleType> store;

	private boolean notNegated = true;

	public boolean isNotNegated() {
		return notNegated;
	}

	/**
	 * A simple way to negate a FilterChange, by setting this attribute to
	 * false.
	 * 
	 * @param notNegated
	 */
	public void setNotNegated(boolean notNegated) {
		this.notNegated = notNegated;
	}

	/**
	 * 
	 * @param template  The change template used to search for matching changes. The search template can contain variables and null values.
	 *        Null values and variables behave differently for these templates. Variables have to be matched with variables in the 
	 *        where clause. Null values are left unmatched and have the semantics "any match". Null values are particularly important
	 *        for FilterChanges with notNegated set to false. They can be used for example to find all addresses who do not have any
	 *        phone number.
	 * 
	 * @param store  The FilterChange needs a store to query. This is different from other filters.
	 * 
	 * @param notNegated  Set this to false if the FilterChange should be negated.
	 */
	public FilterChange(
			PDChange<TransactionType, InstanceType, RoleType> template,
			PDStoreI<TransactionType, InstanceType, RoleType> store, boolean notNegated) {
		super(template);
		this.store = store;
		this.notNegated = notNegated;
	}

	@Override
	public boolean evaluate(Assignment assignment) {
		PDChange<TransactionType, InstanceType, RoleType> change = substituteVariables(
				assignment, ChangeTemplateKind.getKind(this));
		return (processChange(change)
		// logical operation that ensures that notNegated has the desired
		// effect:
		== isNotNegated());
	}

	protected boolean processChange(
			PDChange<TransactionType, InstanceType, RoleType> change) {
		return store.getChanges(change).iterator().hasNext();
	}

	@Override
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		if ((Object)this.getTransaction() instanceof Variable) {
			variables.add((Variable)(Object)this.getTransaction());
		}
		if (this.getInstance1() instanceof Variable) {
			variables.add((Variable) (this.getInstance1()));
		}
		if ((Object)this.getRole2() instanceof Variable) {
			variables.add((Variable)(Object)this.getRole2());
		}
		if (this.getInstance2() instanceof Variable) {
			variables.add((Variable) (this.getInstance2()));
		}
		return variables;
	}
}
