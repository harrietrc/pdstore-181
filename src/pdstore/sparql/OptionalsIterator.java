package pdstore.sparql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.StatefulIterator;
import pdstore.GUID;
import pdstore.generic.Pairable;

public class OptionalsIterator<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends
		StatefulIterator<Assignment<TransactionType, InstanceType, RoleType>> {

	Query query;

	Iterator<Assignment<TransactionType, InstanceType, RoleType>> mainIterator;
	Assignment<TransactionType, InstanceType, RoleType> mainAssignment = null;

	List<Iterator<Assignment<TransactionType, InstanceType, RoleType>>> optionalIterators;
	List<Assignment<TransactionType, InstanceType, RoleType>> optionalAssignments;

	public OptionalsIterator(
			Query query,
			Iterator<Assignment<TransactionType, InstanceType, RoleType>> mainIterator) {
		this.query = query;
		this.mainIterator = mainIterator;

		optionalIterators = new ArrayList<Iterator<Assignment<TransactionType, InstanceType, RoleType>>>(
				query.optionals.size());
		optionalAssignments = new ArrayList<Assignment<TransactionType, InstanceType, RoleType>>(
				query.optionals.size());

		for (int i = 0; i < query.optionals.size(); i++) {
			optionalIterators.add(null);
			optionalAssignments.add(null);
		}
	}

	@Override
	protected Assignment<TransactionType, InstanceType, RoleType> computeNext() {
		/*
		 * Initialization phase: First make sure we have elements in all
		 * involved iterators, as far as possible, from left to right, i.e. from
		 * the main iterator to the optionals. At the same time, calculate the
		 * result assignment by aggregating the current main and optional
		 * assignments.
		 */
		Assignment<TransactionType, InstanceType, RoleType> result = new Assignment<TransactionType, InstanceType, RoleType>();

		// make sure main iterator has element
		if (mainAssignment == null) {
			if (!mainIterator.hasNext())
				return null;
			mainAssignment = mainIterator.next();
		}
		// add assignment to result
		result.putAll(mainAssignment);

		// make sure optional iterators have elements, as far as possible
		for (int i = query.optionals.size()-1; i >=0 ; i--) {
			if (optionalAssignments.get(i) == null) {
				// this means the iterator has run out; we need a new one
				optionalIterators
						.set(i,
								(Iterator<Assignment<TransactionType, InstanceType, RoleType>>) (Object) query.optionals
										.get(i)
										.execute(
												(GUID) null,
												(Assignment<GUID, Object, GUID>) result));
				if (optionalIterators.get(i).hasNext())
					optionalAssignments.set(i, optionalIterators.get(i).next());
			}
			// add assignment to result, if available
			if (optionalAssignments.get(i) != null)
				result.putAll(optionalAssignments.get(i));
		}

		/*
		 * Incrementing phase: Finally increment one of the iterators to get
		 * ready for the next result element. Look for an iterator that can be
		 * incremented further starting from the right. Return once one of the
		 * iterators has been incremented, as we want only one increment at a
		 * time to go through all combinations.
		 */
		for (int i = 0; i < query.optionals.size(); i++) {
			if (optionalIterators.get(i).hasNext()) {
				optionalAssignments.set(i, optionalIterators.get(i).next());
				return result;
			}

			// Here we know that the iterator ran out. It will be reinitialized
			// in the next call if we set it to null here (see initialization
			// phase).
			optionalAssignments.set(i, null);
		}

		// Being here means we need to increment the main iterator (done in the
		// initialization phase).
		mainAssignment = null;
		return result;
	}
}
