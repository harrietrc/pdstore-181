package pdstore.sparql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.StatefulIterator;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.Pairable;

/**
 * This iterator will add the optional data to each query result, as far as
 * optional data is available.
 * 
 * @author Ganesh, Christof
 * 
 * @param <TransactionType>
 * @param <InstanceType>
 * @param <RoleType>
 */

public class TuplesIterator<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends
		StatefulIterator<Assignment<TransactionType, InstanceType, RoleType>> {

	Query query;

	CartesianIndexIterator<TransactionType, InstanceType, RoleType> whereIterator;
	List<PDChange<TransactionType, InstanceType, RoleType>> currentWhere;

	int reIterateIndexCount = 0;
	private final Assignment<TransactionType, InstanceType, RoleType> variableAssignment = new Assignment<TransactionType, InstanceType, RoleType>();
	List<Iterator<Assignment<TransactionType, InstanceType, RoleType>>> lstResultIterator = new ArrayList<Iterator<Assignment<TransactionType, InstanceType, RoleType>>>();

	public TuplesIterator(
			CartesianIndexIterator<TransactionType, InstanceType, RoleType> whereIterator,
			Query query) {
		this.whereIterator = whereIterator;
		this.query = query;
		init();
	}

	private void init() {
		if (this.query.optionals == null)
			return;

		lstResultIterator.clear();
		variableAssignment.clear();
		for (int i = 0; i < query.optionals.size(); i++) {
			lstResultIterator.add(null);
		}
	}

	/**
	 * this method does validation as if an current where tuple contains further
	 * more optional's or it loads the new where tuple.
	 */
	@Override
	protected Assignment<TransactionType, InstanceType, RoleType> computeNext() {
		Assignment<TransactionType, InstanceType, RoleType> resultElement = null;
		if (this.query.optionals == null) {
			if (!whereIterator.hasNext())
				return null;
			processWhere();
			return new Assignment<TransactionType, InstanceType, RoleType>(
					variableAssignment);
		}
		// Check to continue below only if we dont have any optional records
		if (!furtherOptionalsExist(lstResultIterator)) {
			// build statistcs for optionals here
			// TODO: do we need this. this could be some over head ?
			// start
			// for (int optcount = 0; optcount < optionalSize; optcount++)
			// lstOptionals.get(optcount).buildQueryStatistics((PDStore)
			// lstOptionals.get(optcount).store);
			// end
			if (!whereIterator.hasNext())
				return null;
			processWhere();
			if (loadoptional()) {
				resultElement = iterateOptionals(query.optionals.size(), true,
						resultElement);
			} else
				resultElement = new Assignment<TransactionType, InstanceType, RoleType>(
						variableAssignment);
		} else {
			resultElement = iterateOptionals(query.optionals.size(), false,
					resultElement);
		}
		return resultElement;
	}

	@SuppressWarnings("unchecked")
	private void processWhere() {
		init();
		currentWhere = whereIterator.next();
		// Get assigned variables to resolve optional query
		variableAssignment.putAll(whereIterator.getVariableAssignment());
	}

	/**
	 * This method checks whether for a given where tuple any further optional
	 * are found. If no optional is found it returns false.
	 * 
	 * @param lstResultIerator2
	 * @return
	 */
	private boolean furtherOptionalsExist(
			List<Iterator<Assignment<TransactionType, InstanceType, RoleType>>> lstResultIerator2) {
		for (int i = 0; i < lstResultIerator2.size(); i++) {
			if (lstResultIerator2.get(i) != null
					&& lstResultIerator2.get(i).hasNext())
				return true;
		}
		return false;
	}

	/**
	 * This method is called only once whenever a new where tuple is processed.
	 * This load the optional data for the where collection and for preceding
	 * optional if they are linked
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean loadoptional() {
		boolean haveOptional = false;

		for (int i = query.optionals.size() - 1; i >= 0; i--) {

			// since its a new query, we do optimization for this again
			// TODO: use DP table for finer optimization
			Query q = query.optionals.get(i);
			lstResultIterator
					.set(i,
							(Iterator<Assignment<TransactionType, InstanceType, RoleType>>) (Object) q
									.execute(null, new Assignment(
											variableAssignment)));

			// resolve the second optional with variables from the first
			if (lstResultIterator.get(i).hasNext()) {
				// Get assigned variables to resolve optional query
				variableAssignment
						.putAll(((StatefulIterator<Assignment<TransactionType, InstanceType, RoleType>>) lstResultIterator
								.get(i)).getNext());
				haveOptional = true;
			}
		}
		return haveOptional;
	}

	/**
	 * This method loops through the retrieved optional data. For the first time
	 * it computes from n-th index to 0-th index of optionals. For consecutive
	 * calls it loops only in 0-th index. If no data is found in 0-th index then
	 * reloadOptionalIterator is called to load the previous optional data. When
	 * a next optional is found for a index, again the control is returned to
	 * iterateOptionals.
	 * 
	 * @param indexCount
	 * @param isFirst
	 * @param resultElement
	 * @return
	 */
	private Assignment<TransactionType, InstanceType, RoleType> iterateOptionals(
			int indexCount, boolean isFirst,
			Assignment<TransactionType, InstanceType, RoleType> resultElement) {
		// will iterate from least significant index
		for (int i = 0; i <= indexCount - 1; i++) {
			if (lstResultIterator.get(i).hasNext()) {
				Assignment<TransactionType, InstanceType, RoleType> next = ((StatefulIterator<Assignment<TransactionType, InstanceType, RoleType>>) lstResultIterator
						.get(i)).getCurrent();
				// Get assigned variables to resolve optional query
				variableAssignment.putAll(next);
				if (!isFirst && i == 0)
					break;// we got the least significant index change
			} else {
				// TODO: add this later if needed
				// if (query.canBuildStatsForOptionals)
				// getOriginalQuery(i).buildQueryStatistics(
				// (PDStore) query.store);

				if (!isFirst) {
					if (ReloadOptionalIterator()) {
						iterateOptionals(reIterateIndexCount, true,
								resultElement);
					}
					break;
				}
			}
		}
		return new Assignment<TransactionType, InstanceType, RoleType>(
				variableAssignment);
	}

	/**
	 * This functions loads the optionals for the current index with the
	 * variables from previous index variables assignment.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean ReloadOptionalIterator() {
		boolean returnvalue = false;

		// iteration starts from 1 as we call this function only we dont have
		// data for the 0th index
		for (int i = 1; i < query.optionals.size(); i++) {
			if (lstResultIterator.get(i).hasNext()) {
				for (int loopcnt = i; loopcnt > 0; loopcnt--) {
					// Get assigned variables to resolve optional query
					variableAssignment
							.putAll(((StatefulIterator<Assignment<TransactionType, InstanceType, RoleType>>) lstResultIterator
									.get(loopcnt)).getCurrent());
					Query q = query.optionals.get(loopcnt - 1);
					lstResultIterator
							.set(loopcnt - 1,
									(Iterator<Assignment<TransactionType, InstanceType, RoleType>>) (Object) q
											.execute(
													null,
													((StatefulIterator<Assignment<GUID, Object, GUID>>) (Object) lstResultIterator
															.get(loopcnt))
															.getCurrent()));
				}

				// the index at which next value was found
				reIterateIndexCount = i + 1;
				returnvalue = true;
				break;
			}
		}
		return returnvalue;
	}
}
