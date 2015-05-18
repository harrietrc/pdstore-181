package pdstore.sparql;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.Pairable;

public class Assignment<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends HashMap<Variable, Object> {

	private static final long serialVersionUID = 9158949462119234139L;

	public Assignment() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param assignment
	 */
	public Assignment(Assignment assignment) {
		super(assignment);
	}

	public PDChange<TransactionType, InstanceType, RoleType> fillInValues(
			PDChange<TransactionType, InstanceType, RoleType> pattern) {

		// fill in transaction value if variable and in assignment
		TransactionType transaction = pattern.getTransaction();
		if (transaction instanceof Variable) {
			TransactionType value = (TransactionType) this
					.get((Variable) transaction);
			if (value != null)
				pattern.setTransaction(value);
		}

		// fill in instance1 value if variable and in assignment
		InstanceType instance1 = pattern.getInstance1();
		if (instance1 instanceof Variable) {
			InstanceType value = (InstanceType) this.get((Variable) instance1);
			if (value != null)
				pattern.setInstance1(value);
		}

		// fill in role2 value if variable and in assignment
		RoleType role2 = pattern.getRole2();
		if (role2 instanceof Variable) {
			RoleType value = (RoleType) this.get((Variable) role2);
			if (value != null)
				pattern.setRole2(value);
		}

		// fill in instance2 value if variable and in assignment
		InstanceType instance2 = pattern.getInstance2();
		if (instance2 instanceof Variable) {
			InstanceType value = (InstanceType) this.get((Variable) instance2);
			if (value != null)
				pattern.setInstance2(value);
		}

		return pattern;
	}

	public void addValues(
			PDChange<TransactionType, InstanceType, RoleType> pattern,
			PDChange<TransactionType, InstanceType, RoleType> change) {

		// if transaction is pattern variable then assign value from change
		if (pattern.getTransaction() instanceof Variable)
			this.put((Variable) pattern.getTransaction(),
					change.getTransaction());

		// if instance1 is pattern variable then assign value from change
		if (pattern.getInstance1() instanceof Variable)
			this.put((Variable) pattern.getInstance1(), change.getInstance1());

		// if role2 is pattern variable then assign value from change
		if (pattern.getRole2() instanceof Variable)
			this.put((Variable) pattern.getRole2(), change.getRole2());

		// if instance2 is pattern variable then assign value from change
		if (pattern.getInstance2() instanceof Variable)
			this.put((Variable) pattern.getInstance2(), change.getInstance2());
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		for (Variable v : this.keySet())
			s.append(v + "=" + PDStore.getDebugStore().getLabel(this.get(v)) + "\t");
		return s.toString();
	}
	
	public String toString(PDStore store) {
		StringBuffer s = new StringBuffer();
		for (Variable v : this.keySet())
			s.append(v + "=" + store.getNameOrValue(this.get(v)) + "\t");
		return s.toString();
	}

	public Object getVar(String arg) {
		Set<Variable> keys = keySet();
		for(Variable key: keys) {
			if (key.toString().equals(arg)) {
				return key;
			} 
		}
		return null;
	}

	public Object getValue(Object arg) {
		if (arg instanceof Variable && containsKey(arg)) {
			return get(arg);
		} else {
			return arg;
		}
	}
}
