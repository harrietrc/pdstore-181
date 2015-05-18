package pdstore.sparql;

import java.util.Iterator;

import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.StatefulIterator;
import pdstore.PDChange;
import pdstore.generic.PDStoreI;
import pdstore.generic.Pairable;

/**
 * Iterator that receives assignments and joins them using the given pattern and
 * filter, hence generating extended assignments.
 * 
 * A SPARQL query can be processed by creating and linking such iterators for
 * every pattern in the WHERE clause.
 * 
 * @author clut002
 * 
 */
public class QueryPatternIterator<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends
		StatefulIterator<Assignment<TransactionType, InstanceType, RoleType>> {

	/**
	 * Pattern to use when searching for matching changes.
	 */
	PDChange<TransactionType, InstanceType, RoleType> pattern;

	/**
	 * Filter to use when searching for matching changes.
	 */
	FilterExpression filter;

	/**
	 * Store to read all changes from.
	 */
	PDStoreI<TransactionType, InstanceType, RoleType> store;

	/**
	 * Input iterator with incoming assignments to use when searching for
	 * matching changes.
	 */
	Iterator<Assignment<TransactionType, InstanceType, RoleType>> inIterator;

	/**
	 * The current input assignment being used for searching matching changes.
	 */
	Assignment<TransactionType, InstanceType, RoleType> currentAssignment;

	/**
	 * The current pattern resulting from filling the values of the current
	 * assignment into the main pattern.
	 */
	PDChange<TransactionType, InstanceType, RoleType> currentPattern;

	/**
	 * An iterator with changes that match the current pattern. Can only be null
	 * just before this iterator terminates.
	 */
	private Iterator<PDChange<TransactionType, InstanceType, RoleType>> outIterator;

	private long iterations = 0;
	private long iterationsAfterFilter = 0;

	QueryPatternIterator(
			Iterator<Assignment<TransactionType, InstanceType, RoleType>> inIterator,
			PDChange<TransactionType, InstanceType, RoleType> pattern,
			FilterExpression filter,
			PDStoreI<TransactionType, InstanceType, RoleType> store) {
		this.inIterator = inIterator;
		this.pattern = pattern;
		this.filter = filter;
		this.store = store;

		outIterator = getIterator();
	}

	/**
	 * @return the iterations
	 */
	public long getIterations() {
		return iterations;
	}

	/**
	 * @param iterations
	 *            the iterations to set
	 */
	public void setIterations(long iterations) {
		this.iterations = iterations;
	}

	/**
	 * @return the iterationsAfterFilter
	 */
	public long getIterationsAfterFilter() {
		return iterationsAfterFilter;
	}

	/**
	 * @param iterationsAfterFilter
	 *            the iterationsAfterFilter to set
	 */
	public void setIterationsAfterFilter(long iterationsAfterFilter) {
		this.iterationsAfterFilter = iterationsAfterFilter;
	}

	/**
	 * Returns the next iterator of changes that match the pattern, using the
	 * next input assignment. If it returns null, then computeNext will return
	 * false.
	 * 
	 * @return
	 */
	private Iterator<PDChange<TransactionType, InstanceType, RoleType>> getIterator() {
		if (!inIterator.hasNext())
			return null;

		currentAssignment = inIterator.next();
		currentPattern = new PDChange<TransactionType, InstanceType, RoleType>(
				pattern);
		currentAssignment.fillInValues(currentPattern);

		return store.getChanges(currentPattern).iterator();
	}

	@Override
	protected Assignment<TransactionType, InstanceType, RoleType> computeNext() {
		setIterations(getIterations() + 1);

		while (true) {

			// Make sure we have an iterator with changes that match the
			// pattern.
			while (outIterator != null && !outIterator.hasNext())
				outIterator = getIterator();

			// If there is no iterator with matching changes, we are done.
			if (outIterator == null)
				return null;

			// Read matching changes until we have found a result.
			do {
				// Create new assignment with the values determined by the next
				// change.
				PDChange<TransactionType, InstanceType, RoleType> change = outIterator
						.next();

				// TODO:
				// Debug.assertTrue(change.getNormalizedChange().matches(currentPattern.getNormalizedChange()),
				// "");

				Assignment<TransactionType, InstanceType, RoleType> assignment = new Assignment<TransactionType, InstanceType, RoleType>(
						currentAssignment);
				assignment.addValues(currentPattern, change);

				// If there is no filter, then we have a result.
				if (filter == null)
					return assignment;

				// If there is a filter, we need to apply it.
				if (filter.evaluate(assignment)) {
					setIterationsAfterFilter(getIterationsAfterFilter() + 1);
					return assignment;
				}

			} while (outIterator.hasNext());
		}
	}
}
