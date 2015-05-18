package pdstore.sparql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.PDStoreException;
import pdstore.generic.ChangeTemplateKind;
import pdstore.generic.PDStoreI;
import pdstore.sparql.Query.OptimimzerType;
import pdstore.sparql.QueryOrderBy.Order;

/**
 * An instance of this class represents a SPARQL query and is used to construct
 * and execute a SPARQL query. Examples can be found in the test classes. To
 * construct a query, change templates are added to the where clause. The query
 * is executed with the execute() methods.
 * 
 * 
 * @author gweb017
 * 
 */
public class Query {

	/**
	 * The PDStore that the query is executed on.
	 */
	public PDStoreI<GUID, Object, GUID> store;

	// query specification
	List<Variable> select;
	List<PDChange<GUID, Object, GUID>> where;
	public final FilterExpression filterParameter;
	private FilterExpression filter;
	public List<Query> optionals;
	Assignment<GUID, Object, GUID> incomingAssignment;
	public boolean distinct;
	public long limit = -1;
	public long offset = 0;
	public List<Query> union = new ArrayList<Query>();
	public List<QueryOrderBy> orderby = new ArrayList<QueryOrderBy>();
	ConcurrentHashMap<Integer, QueryPatternIterator<GUID, Object, GUID>> lstQueryPatternItrs = new ConcurrentHashMap<Integer, QueryPatternIterator<GUID, Object, GUID>>();

	// query execution
	public CartesianIndexIterator<GUID, Object, GUID> cartesianIterator;

	// query evaluation plan
	ChangeTemplateKind[] readFromAssignment;
	ChangeTemplateKind[] writeToAssignment;
	FilterExpression[] filtersToEvaluate;
	QueryOptimizerBase queryOptimizer;
	public boolean isOptimized = false;
	public boolean canBuildStatistics = true;
	Set<Variable> queryAssignedVariablesSoFar = new HashSet<Variable>();

	public enum OptimimzerType {
		RBO, LSO, NoOpt
	};

	public static OptimimzerType currentOptimizerType = OptimimzerType.LSO;

	/**
	 * Can be used to turn off storing USES_ROLE_ROLEID by setting it to false.
	 * TODO: getChanges patterns with variable Roles should throw an
	 * Unsuppported Exception then.
	 */
	public static boolean isUsingVariableRoles = true;

	/**
	 * Indicates whether if the optional graph patters of the query can be
	 * reoptimized during execution. Please note that this operation could be
	 * costly
	 */
	public boolean canDynamicallyReoptimizeOptionals = false;

	/**
	 * Indicates whether statistics should be generated for every optional
	 * execution. Please note that this operation could be costly
	 */
	public boolean canBuildStatsForOptionals = false;

	/**
	 * The query.filter is decomposed into singleFilters, which are ANDed. TODO:
	 * check if this needs to be here; should probably be a local variable
	 * somewhere
	 */
	List<FilterExpression> singleFilters;

	/**
	 * 
	 * Standard semantics of query execution is:
	 * 
	 * 1. equijoin, natural join Result: Net of Links Possible further
	 * decomposition: 1.1. Cartesian product 1.2. Exclude tuples not joining
	 * 
	 * 2. step: apply filters (in relational algebra: selection)
	 * 
	 * 3. step projection means apply the select clause.
	 * 
	 * select ?name where ?person gender "male" ?person hasChild "Pat" ?person
	 * lastName ?name
	 * 
	 * 
	 * select ?person where ?person gender "male" ?person hasChild "Pat"
	 * 
	 * @param select
	 * @param where
	 * @param store
	 */
	public Query(List<Variable> select,
			List<PDChange<GUID, Object, GUID>> where, FilterExpression filter,
			List<Query> optionals, PDStoreI<GUID, Object, GUID> store) {
		Debug.assertTrue(store != null, "The store must not be null");
		if (select == null)
			this.select = new ArrayList<Variable>();
		else
			this.select = select;
		this.where = where;
		this.setFilter(filter);
		this.filterParameter = filter;
		this.optionals = optionals;
		this.store = store;
	}

	public Query(List<PDChange<GUID, Object, GUID>> where,
			PDStoreI<GUID, Object, GUID> store) {
		this(null, where, null, null, store);
	}

	// TODO : can we take out this into separate functionalities ?
	/**
	 * This constructor is called when we have sub queries to be executed. This
	 * constructor merges all queries to a single query and return a single
	 * query object.
	 * 
	 * @param canMerge
	 * @param queries
	 * 
	 */
	public Query(boolean canMerge, Query... queries) {
		this(null, null, null, null, queries[0].store);
		if (canMerge)
			merge(queries);
		else {
			for (Query q : queries)
				this.union.add(q);
		}
	}

	/**
	 * This is used merge all sub queries into the main query.
	 * 
	 * @param queries
	 * 
	 */
	public void merge(Query... queries) {

		for (int loopcnt = queries.length - 1; loopcnt >= 0; loopcnt--) {

			// unnecessary to add the sub-query select into main query

			if (this.where == null) {
				this.where = new ArrayList<PDChange<GUID, Object, GUID>>();
			}
			for (PDChange<GUID, Object, GUID> p : queries[loopcnt].where) {

				if (!this.where.contains(p)) {
					this.where.add(new PDChange<GUID, Object, GUID>(p));
				}
			}

			// adding sub-queries filters to main query
			if (queries[loopcnt].getFilter() != null) {
				if (this.getFilter() != null) {
					FilterExpression fe = new AndExpression(this.getFilter(),
							queries[loopcnt].getFilter());
					this.setFilter(fe);
				} else
					this.setFilter(queries[loopcnt].getFilter());
			}

			// adding sub-queries optionals to main query
			if (queries[loopcnt].optionals != null) {
				if (this.optionals != null) {
					this.optionals.addAll(queries[loopcnt].optionals);
				} else {
					this.optionals = queries[loopcnt].optionals;
				}
			}
		}
	}

	public Query(List<Variable> select,
			List<PDChange<GUID, Object, GUID>> where, FilterExpression filter,
			PDStoreI<GUID, Object, GUID> store) {
		this(select, where, filter, new ArrayList<Query>(), store);
	}

	public Query(List<Variable> select,
			List<PDChange<GUID, Object, GUID>> where,
			PDStoreI<GUID, Object, GUID> store) {
		this(select, where, null, store);
	}

	/**
	 * Creates a query object from a SPARQL query string.
	 * 
	 * @param queryString
	 */
	public Query(String queryString) {
		// TODO: not implemented yet
		throw new UnsupportedOperationException();
	}

	/**
	 * Gives the SPARQL query as a formatted String.
	 */
	@Override
	public String toString() {
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT" + " ");
		if (select.size() == 0)
			queryString.append("*");
		else {
			for (Variable variable : select) {
				queryString.append(variable.toString() + " ");
			}
		}
		queryString.append("\r\n");
		queryString.append("WHERE" + "\r\n");
		queryString.append("{" + "\r\n");
		for (PDChange<GUID, Object, GUID> change : where) {

			queryString.append(change.getChangeType() + " "
					+ change.getTransaction() + " "
					+ change.getInstance1().toString() + " "
					+ change.getRole2().toString() + " "
					+ change.getInstance2().toString() + " .\n");

		}
		if (optionals != null && optionals.size() != 0) {
			queryString.append("\r\n");
			queryString.append("OPTIONAL { ");
			for (Query optional : optionals) {
				for (PDChange<GUID, Object, GUID> change : optional.where) {
					queryString.append(change.getChangeType() + " "
							+ change.getTransaction().toString() + " "
							+ change.getInstance1().toString() + " "
							+ change.getRole2().toString() + " "
							+ change.getInstance2().toString() + " .\n");
				}
			}
			queryString.append("}" + "\r\n");
		}
		if (getFilter() != null) {
			queryString.append("\r\n");
			queryString.append("FILTER  " + getFilter().toString());
		}
		queryString.append("\r\n" + "}");
		return queryString.toString();
	}

	/**
	 * decompose the query filter into filters that are not AndExpressions.
	 */
	private List<FilterExpression> decomposeFilter(
			FilterExpression filterExpression) {

		List<FilterExpression> singleFilters = new CopyOnWriteArrayList<FilterExpression>();
		if (filterExpression == null)
			return singleFilters;
		if (!(filterExpression instanceof AndExpression)) {
			singleFilters.add(filterExpression);
			// Equal expression filter can be converted into assignment
			if (queryOptimizer.resolveFilter
					&& filterExpression instanceof EqualExpression) {
				/*
				 * // TODO: this code is quarantined since it seems insufficient
				 * // it does put any arg2 into an assignment. // but what if
				 * this is a variable? // then it cant go into an assignment
				 * 
				 * if (((EqualExpression) filterExpression).arg1 instanceof
				 * Variable) { this.incomingAssignment .put((Variable)
				 * ((EqualExpression) filterExpression).arg1, ((EqualExpression)
				 * filterExpression).arg2);
				 * singleFilters.remove(filterExpression);// since we re-write,
				 * // we dont want this // filter anymore } else {
				 * this.incomingAssignment .put((Variable) ((EqualExpression)
				 * filterExpression).arg2, ((EqualExpression)
				 * filterExpression).arg1);
				 * singleFilters.remove(filterExpression); }
				 */
			}
		} else {

			FilterExpression[] filters = ((AndExpression) filterExpression).args;
			for (int i = 0; i < filters.length; i++) {
				singleFilters.addAll(decomposeFilter(filters[i]));
			}
		}
		return singleFilters;
	}

	/**
	 * 
	 * @param incomingAssignment
	 */
	private void optimizeQuery(Map<Variable, Object> incomingAssignment) {

		Debug.println("Analyzing the where-clause...", "SPARQL");

		// initializeOptimizer()
		if (currentOptimizerType == null
				|| OptimimzerType.RBO == currentOptimizerType) {
			queryOptimizer = new QueryOptimizerRBO();
			currentOptimizerType = OptimimzerType.RBO;
		} else if (OptimimzerType.LSO == currentOptimizerType) {
			queryOptimizer = new QueryOptimizerLSO();
			queryOptimizer.resolveFilter = true;
			queryOptimizer.canUseLearningStats = true;
			queryOptimizer.canFormNaturalJoin = true;
			queryOptimizer.noSampling = true;
		} else {
			queryOptimizer = new QueryOptimizerRBO();
		}

		// init
		try {
			readFromAssignment = new ChangeTemplateKind[where.size()];
			writeToAssignment = new ChangeTemplateKind[where.size()];
			filtersToEvaluate = new FilterExpression[where.size()];
			this.incomingAssignment = new Assignment<GUID, Object, GUID>();
			singleFilters = decomposeFilter(getFilter());
		} catch (Exception ex) {
			throw new PDStoreException("Error initializing Query", ex);
		}

		if (incomingAssignment != null) {
			this.incomingAssignment.putAll(incomingAssignment);
		}
		queryOptimizer.optimizeQuery(this);
		if (!this.canDynamicallyReoptimizeOptionals && this.optionals != null) {
			for (int i = 0; i < this.optionals.size(); i++) {
				Query quo = this.optionals.get(i);
				quo.queryAssignedVariablesSoFar
						.addAll(this.queryAssignedVariablesSoFar);
				if (i != 0)
					quo.queryAssignedVariablesSoFar.addAll(this.optionals
							.get(i - 1).queryAssignedVariablesSoFar);
				quo.ensureIsOptimized(this.incomingAssignment);
			}
		}
		isOptimized = true;

		if (Debug.isDebugging("SPARQL")) {
			for (int i = where.size() - 1; i >= 0; i--) {
				Debug.println("  Tuple " + where.get(i) + " has reading kind "
						+ readFromAssignment[i] + ". has writing kind: "
						+ writeToAssignment[i], "SPARQL");
			}
		}
	}

	public Iterator<Assignment<GUID, Object, GUID>> execute(GUID transactionId) {
		return execute(transactionId, null);
	}

	@SuppressWarnings("unchecked")
	public Iterator<Assignment<GUID, Object, GUID>> execute(GUID transactionId,
			Assignment<GUID, Object, GUID> incomingAssignment) {
		Debug.println("Executing query...", "SPARQL");

		// UNIONs are treated specially...
		if (this.union.size() > 0) {
			Iterator<Assignment<GUID, Object, GUID>> resultIterator = applyUnion();
			distinct = false;
			if (orderby.size() > 0) {
				resultIterator = applyOrderBy(resultIterator, 0);
			}
			return new LimitIterator<Assignment<GUID, Object, GUID>>(
					resultIterator, limit, offset);
		}

		ensureIsOptimized(incomingAssignment);

		// Create an iterator containing only the initial assignment.
		List<Assignment<GUID, Object, GUID>> initialAssignment = new ArrayList<Assignment<GUID, Object, GUID>>(
				1);

		// initialAssignment.add(this.incomingAssignment);
		Assignment<GUID, Object, GUID> typedAssignment = new Assignment<GUID, Object, GUID>();
		if (incomingAssignment != null)
			typedAssignment.putAll(incomingAssignment);
		initialAssignment.add(typedAssignment);

		// Create and link iterators for all query patterns.
		// Note that at the moment the optimizer sorts the patterns in reverse
		// order of processing (probably artefact from old
		// CartesianIndexIterator).
		Iterator<Assignment<GUID, Object, GUID>> resultIterator = initialAssignment
				.iterator();
		for (int i = where.size() - 1; i >= 0; i--) {
			PDChange<GUID, Object, GUID> pattern = where.get(i);
			FilterExpression filter = this.filtersToEvaluate[i];
			QueryPatternIterator<GUID, Object, GUID> qpt = new QueryPatternIterator<GUID, Object, GUID>(
					(Iterator<Assignment<GUID, Object, GUID>>) (Object) resultIterator,
					pattern, filter, store);
			lstQueryPatternItrs.put(i, qpt);
			resultIterator = qpt;
		}

		// process OPTIONALs
		if (this.optionals != null && this.optionals.size() > 0) {
			OptionalsIterator<GUID, Object, GUID> optionalIterator = new OptionalsIterator<GUID, Object, GUID>(
					this, resultIterator);
			resultIterator = optionalIterator;
		}

		// Apply order by here. Since we handle distinct along with order by, we
		// reset the distinct to false here.
		if (orderby.size() > 0) {
			resultIterator = applyOrderBy(resultIterator, 0);
			distinct = false;
		}

		if (distinct)
			resultIterator = new DistinctIterator<Assignment<GUID, Object, GUID>>(
					resultIterator);

		return new LimitIterator<Assignment<GUID, Object, GUID>>(
				resultIterator, limit, offset);
	}

	/**
	 * Ensures Query is optimized.
	 * 
	 * @param incomingAssignment
	 */
	public void ensureIsOptimized(
			Assignment<GUID, Object, GUID> incomingAssignment) {
		// Optimize the query, if it has not been done already.
		// TODO: this is currently a single boolean flag, but it must be
		// dependent
		// on the variable set of the Assignment.
		// In fact the optimization result must be loaded from the assignment.
		// Easier: recommend that the assignment must be the same, otherwise
		// throw warning.
		// However, still needs a dispatcher to optimized queries.
		// So better method: create new queries, and optimize them.

		if (!isOptimized) {

			// TODO: see above. Also need to copy and store original old
			// assignment
			// so that comparison is correct.
			// originalOldAssignment = new Assignment<GUID, Object, GUID>();
			// originalOldAssignment.putAll(incomingAssignment);
			optimizeQuery(incomingAssignment);
		} else {
			// if the query is already optimized, then there is a probability
			// that for this execution, it has a different or additional
			// incoming assignments
			if (incomingAssignment != null) {
				// Assignment has to be deleted so that the old incoming
				// assignment
				// can have no effects on the query result. (Bugfix 2014-04-02)
				this.incomingAssignment = new Assignment<GUID, Object, GUID>();
				this.incomingAssignment.putAll(incomingAssignment);
			}
		}
	}

	private Iterator<Assignment<GUID, Object, GUID>> applyUnion() {
		HashSet<Assignment<GUID, Object, GUID>> finalUnionTuples = new HashSet<Assignment<GUID, Object, GUID>>();
		for (Query q : union) {
			// union by default has distinct
			q.distinct = true;
			Iterator<Assignment<GUID, Object, GUID>> assignmentIterator = q
					.execute(null);
			while (assignmentIterator.hasNext()) {
				Assignment<GUID, Object, GUID> result = new Assignment<GUID, Object, GUID>(
						assignmentIterator.next());

				if (finalUnionTuples.contains(result))
					continue;

				finalUnionTuples.add(result);
			}
		}
		return finalUnionTuples.iterator();
	}

	/**
	 * Sorts the contents of the iterator for the given iterator. Uses an
	 * insertion sort for its simplicity (Needs to be changes to merge sort in
	 * future). Level will be useful for composite sorts. This sort also uses to
	 * be distinct implementation.
	 * 
	 * @param resultIterator
	 * @param level
	 * @return
	 */
	private Iterator<Assignment<GUID, Object, GUID>> applyOrderBy(
			Iterator<Assignment<GUID, Object, GUID>> resultIterator, int level) {
		// used List instead of HashSet, since we need to access index locations
		// for sort
		List<Assignment<GUID, Object, GUID>> finalOrderedList = new ArrayList<Assignment<GUID, Object, GUID>>();
		QueryOrderBy currentOrderBy = orderby.get(level);
		Variable currentOrderByVariable = currentOrderBy.var;
		Order arrangingOrder = currentOrderBy.order;
		outsideloops: while (resultIterator.hasNext()) {
			Assignment<GUID, Object, GUID> currentValue = resultIterator.next();
			if (finalOrderedList.size() == 0) {
				finalOrderedList.add(new Assignment<GUID, Object, GUID>(
						currentValue));
				continue;
			}
			int candidateIndex = 0;
			// int currentIndex = 0;
			String currentVariable = (String) currentValue
					.get(currentOrderByVariable);

			for (Assignment<GUID, Object, GUID> result : finalOrderedList) {
				String compareVariable = (String) result
						.get(currentOrderByVariable);
				if (arrangingOrder == Order.asc) {
					if (currentVariable.compareTo(compareVariable) < 0) {
						break;
					} else if (currentVariable.compareTo(compareVariable) == 0) {
						if (distinct)
							continue outsideloops;
						else
							break;
					} else if (currentVariable.compareTo(compareVariable) > 0) {
						candidateIndex++;
					}
				} else if (arrangingOrder == Order.desc) {
					if (currentVariable.compareTo(compareVariable) < 0) {
						candidateIndex++;
					} else if (currentVariable.compareTo(compareVariable) == 0) {
						if (distinct)
							continue outsideloops;
						else
							break;
					} else if (currentVariable.compareTo(compareVariable) > 0) {
						break;
					}
				}
			}
			finalOrderedList.add(candidateIndex,
					new Assignment<GUID, Object, GUID>(currentValue));
		}
		if (orderby.size() > level + 1)
			applyOrderBy(finalOrderedList.iterator(), level + 1);

		return finalOrderedList.iterator();
	}

	public String getStatistics() {
		return cartesianIterator.getStatistics();
	}

	/*
	 * Creates statistics based on the query execution. after the query
	 * execution is completed, an average of new statistics (from query
	 * execution) and the old statistics (persisted in pdstore) for every role
	 * belonging to XRX, IRX, XRI patterns are calculated.
	 * 
	 * @param statStore
	 */
	public void buildQueryStatistics(PDStore statStore) {
		if (!canBuildStatistics)
			return;
		int whereSize = where.size() - 1;
		Set<Variable> assignedVariables = new HashSet<Variable>();
		// if (this.incomingAssignment != null
		// && this.incomingAssignment.size() > 0)
		// assignedVariables.addAll(this.incomingAssignment.keySet());
		for (int currentIndex = whereSize; currentIndex >= 0; currentIndex--) {
			PDChange<GUID, Object, GUID> pattern = where.get(currentIndex);
			ChangeTemplateKind templatekind = ChangeTemplateKind.getKind(
					pattern, assignedVariables);

			// No statistics for IXI,IXX,XXI
			if (templatekind == ChangeTemplateKind.IXI
					|| templatekind == ChangeTemplateKind.IXX
					|| templatekind == ChangeTemplateKind.XXI)
				continue;
			// No statistics for IRI
			if (templatekind == ChangeTemplateKind.IRI)
				continue;

			GUID roleId = pattern.getRole2();

			// calculate new selectivity
			boolean CurrrentIndexFilter = false;
			if (filtersToEvaluate[currentIndex] != null)
				CurrrentIndexFilter = true;
			long changesProcessed = getChangesProcessedCount(currentIndex,
					CurrrentIndexFilter);

			boolean PrevIndexFilter = false;
			long prevChangesProcessed = 1;
			// right most one, so no prev iteration
			if (currentIndex != whereSize) {
				if (filtersToEvaluate[currentIndex + 1] != null)
					PrevIndexFilter = true;
				prevChangesProcessed = getPrevChangesProcessedCount(
						currentIndex, PrevIndexFilter);
			}
			long newSelectivity = getNewSelectivity(changesProcessed,
					prevChangesProcessed);
			GUID roleSelectivityId = null;

			// do not process if it has 'X' in role position un resolved
			// if (!(pattern.getRole2() instanceof Variable)
			// || assignedVariables.contains(pattern.getRole2())) {
			// XRX
			if (ChangeTemplateKind.XRX == templatekind) {
				roleSelectivityId = PDStore.ROLE_XRX_SELECTIVITY_ROLEID;
			} else if (ChangeTemplateKind.IRX == templatekind) {
				// IRX
				roleSelectivityId = PDStore.ROLE_IRX_SELECTIVITY_ROLEID;
			} else if (ChangeTemplateKind.XRI == templatekind) {
				// XRI
				roleSelectivityId = PDStore.ROLE_XRI_SELECTIVITY_ROLEID;
			}

			if (roleSelectivityId != null) {
				estimateNewStats(statStore, roleId, newSelectivity,
						roleSelectivityId);
				if (filtersToEvaluate[currentIndex] != null)
					estimateFilterStats(statStore, roleId, currentIndex,
							prevChangesProcessed, templatekind,
							roleSelectivityId);
			}
			generateAssignedVariable(assignedVariables, pattern);
		}
		statStore.commit();
	}

	/*
	 * Number of records processed by a pattern is divided by the number of
	 * records processed by the previous pattern to get an average number of
	 * records processed by each pattern.
	 */
	long getNewSelectivity(long changesProcessed, long prevChangesProcessed) {
		return (changesProcessed == 0) ? 0
				: ((prevChangesProcessed == 0) ? changesProcessed
						: (changesProcessed / prevChangesProcessed));
	}

	long getPrevChangesProcessedCount(int currentIndex,
			boolean getChangesProcessedBeforeFilter) {
		long prevChangesProcessed = 1;
		if (currentIndex < where.size() - 1) {
			prevChangesProcessed = lstQueryPatternItrs.get(currentIndex + 1)
					.getIterations();
		}
		return prevChangesProcessed;
	}

	long getChangesProcessedCount(int currentIndex,
			boolean getChangesProcessedBeforeFilter) {

		// use the number of unfiltered changes if changes were filtered
		// TODO: can we have filter statistics as well ?
		QueryPatternIterator<GUID, Object, GUID> qpi = lstQueryPatternItrs
				.get(currentIndex);
		long changesProcessed;
		if (getChangesProcessedBeforeFilter)
			changesProcessed = qpi.getIterations();
		else
			changesProcessed = qpi.getIterationsAfterFilter();
		return changesProcessed;
	}

	void estimateFilterStats(PDStore statStore, GUID roleId, int currentIndex,
			long prevChangesProcessed, ChangeTemplateKind templatekind,
			GUID roleSelectivityId) {
		GUID filterTypeId = null;

		long currentChangeProcessed = getChangesProcessedCount(currentIndex,
				false);
		long newSelectivity = getNewSelectivity(currentChangeProcessed,
				prevChangesProcessed);
		if (this.filtersToEvaluate[currentIndex].getClass().getCanonicalName()
				.contains("equal"))
			filterTypeId = PDStore.EQUAL_SELECTIVITY_ROLEID;
		else if (this.filtersToEvaluate[currentIndex].getClass()
				.getCanonicalName().contains("less"))
			filterTypeId = PDStore.LESSTHAN_SELECTIVITY_ROLEID;
		else if (this.filtersToEvaluate[currentIndex].getClass()
				.getCanonicalName().contains("greater"))
			filterTypeId = PDStore.GREATERTHAN_SELECTIVITY_ROLEID;

		Object combId = null;

		// TODO: replace with a SPARQL query
		Object lookUpId = statStore.getInstance(roleId, filterTypeId);
		if (lookUpId != null) {
			combId = statStore.getInstance(lookUpId,
					PDStore.HAS_CHANGETEMPLATEKIND_ROLEID);
		}

		if (combId != null && combId == templatekind) {
			statStore.addLink(lookUpId, PDStore.HAS_VALUE_ROLEID,
					newSelectivity);
		} else {
			lookUpId = new GUID();
			statStore.addLink(roleId, filterTypeId, lookUpId);
			statStore.addLink(lookUpId, PDStore.HAS_CHANGETEMPLATEKIND_ROLEID,
					templatekind);
			statStore.addLink(lookUpId, PDStore.HAS_VALUE_ROLEID,
					newSelectivity);
		}

	}

	private void estimateNewStats(PDStore statStore, GUID roleId,
			long newSelectivity, GUID selectivityRoleId) {

		Long oldSelectivity = (Long) statStore.getInstance(null, roleId,
				selectivityRoleId);
		// Calculate mean here
		if (oldSelectivity != null) {
			newSelectivity = (newSelectivity + oldSelectivity) / 2;
			statStore.removeLink(roleId, selectivityRoleId, oldSelectivity);
		}
		statStore.addLink(roleId, selectivityRoleId, newSelectivity);
		// System.out.println(roleType + "----  RoleId -" + roleId
		// + " Selectivty-" + newSelectivity);
	}

	void generateAssignedVariable(Set<Variable> assignedVariables,
			PDChange<GUID, Object, GUID> pattern) {
		if (assignedVariables == null)
			assignedVariables = new HashSet<Variable>();

		Object instance1 = pattern.getInstance1();
		GUID role2 = pattern.getRole2();
		Object instance2 = pattern.getInstance2();
		if (instance1 instanceof Variable) {
			assignedVariables.add((Variable) instance1);
		}
		if (instance2 instanceof Variable) {
			assignedVariables.add((Variable) instance2);
		}
		if (role2 instanceof Variable) {
			assignedVariables.add((Variable) role2);
		}
	}

	public List<PDChange<GUID, Object, GUID>> getWhere() {
		return where;
	}

	public void setWhere(List<PDChange<GUID, Object, GUID>> where) {
		this.where = where;
	}

	public FilterExpression getFilter() {
		return filter;
	}

	public void setFilter(FilterExpression filter) {
		this.filter = filter;
	}

}