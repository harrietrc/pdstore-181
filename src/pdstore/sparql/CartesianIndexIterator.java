package pdstore.sparql;

import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.FilterIterator;
import pdstore.PDChange;
import pdstore.generic.ChangeTemplateKind;
import pdstore.generic.PDStoreI;
import pdstore.generic.Pairable;

public class CartesianIndexIterator<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends
		CartesianPowerIterator<PDChange<TransactionType, InstanceType, RoleType>> {
	PDStoreI<TransactionType, InstanceType, RoleType> store;
	TransactionType transactionId;

	private Assignment<TransactionType, InstanceType, RoleType> variableAssignment;
	ChangeTemplateKind[] readFromAssignment;
	FilterExpression[] filtersToEvaluate;

	int[] changesProcessedBeforeFilters;

	public Assignment<TransactionType, InstanceType, RoleType> getVariableAssignment() {
		return variableAssignment;
	}

	public void setVariableAssignment(
			Assignment<TransactionType, InstanceType, RoleType> variableAssignment) {
		this.variableAssignment = variableAssignment;
	}

	public CartesianIndexIterator(
			PDStoreI<TransactionType, InstanceType, RoleType> store,
			TransactionType transactionId,
			List<PDChange<TransactionType, InstanceType, RoleType>> where,
			FilterExpression filter,
			Assignment<TransactionType, InstanceType, RoleType> incomingAssignment,
			ChangeTemplateKind[] readFromAssignment,
			FilterExpression[] filtersToEvaluate) {
		this.store = store;
		this.transactionId = transactionId;
		this.where = where;
		this.exponent = where.size();
		if (incomingAssignment == null)
			setVariableAssignment(new Assignment<TransactionType, InstanceType, RoleType>());
		else
			setVariableAssignment(incomingAssignment);
		this.readFromAssignment = readFromAssignment;
		this.filtersToEvaluate = filtersToEvaluate;

		changesProcessedBeforeFilters = new int[where.size()];

		init();
	}

	@Override
	public Iterator<PDChange<TransactionType, InstanceType, RoleType>> getInputIterator(
			int i) {

		// get the pattern to create the iterator for
		PDChange<TransactionType, InstanceType, RoleType> currentWhereElement = where
				.get(i);

		// if no transaction is given for the pattern, then use the transaction
		// of the query in the pattern
		TransactionType transaction = currentWhereElement.getTransaction();
		if (transaction == null || transaction instanceof Variable) {
			transaction = this.transactionId;
		}

		// clone current pattern to tempChange and fill in values for the
		// assigned variables
		PDChange<TransactionType, InstanceType, RoleType> tempChange;
		ChangeTemplateKind changeTemplateKind = readFromAssignment[i];
		tempChange = currentWhereElement.substituteVariables(
				getVariableAssignment(), changeTemplateKind);

		// use the pattern (with assigned variable values inserted) to get
		// matching changes from the DB
		Iterator<PDChange<TransactionType, InstanceType, RoleType>> changeIterator = store
				.getChanges(tempChange).iterator();

		if (filtersToEvaluate[i] == null)
			return changeIterator;

		final int filterIndex = i;
		FilterIterator<PDChange<TransactionType, InstanceType, RoleType>> filterIterator = new FilterIterator<PDChange<TransactionType, InstanceType, RoleType>>(
				changeIterator) {
			public boolean filterCondition(
					PDChange<TransactionType, InstanceType, RoleType> change) {

				// update statistics
				changesProcessedBeforeFilters[filterIndex]++;

				assignVariable(change, filterIndex);
				return filtersToEvaluate[filterIndex]
						.evaluate(getVariableAssignment());
			}
		};
		return filterIterator;

	}

	PDChange<TransactionType, InstanceType, RoleType> nextInProduct(int i) {
		PDChange<TransactionType, InstanceType, RoleType> next = product.get(i)
				.next();
		assignVariable(next, i);
		return next;
	}

	public void assignVariable(
			PDChange<TransactionType, InstanceType, RoleType> pdchange, int i) {
		// if (i == 0)
		// return;

		ChangeTemplateKind templateKind = ChangeTemplateKind.getKind(where
				.get(i));

		switch (templateKind) {
		case XXX:
		case XXI:
		case XRI:
		case XRX:
			// assign first variable
			getVariableAssignment().put(
					(Variable) (where.get(i).getInstance1()),
					pdchange.getInstance1());
			break;
		default:
			break;

		}
		switch (templateKind) {
		case XXX:
		case IXX:
		case IXI:
		case XXI:
			// assign role2
			getVariableAssignment().put((Variable) (where.get(i).getRole2()),
					pdchange.getRole2());
			break;
		default:
			break;

		}

		switch (templateKind) {

		case XXX:
		case IXX:
		case XRX:
		case IRX:
			// assign instance2
			getVariableAssignment().put(
					(Variable) (where.get(i).getInstance2()),
					pdchange.getInstance2());
			break;
		default:
			break;
		}
		// Adding transaction variables in variables assignment to make them
		// used in projection
		if (where.get(i).getTransaction() instanceof Variable)
			getVariableAssignment().put(
					(Variable) (where.get(i).getTransaction()),
					pdchange.getTransaction());
	}
}
