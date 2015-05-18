package pdstore.sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.ChangeTemplateKind;
import pdstore.sparql.Query.OptimimzerType;

public class QueryOptimizerBase {

	int numOfProcessedIndex = 0;
	Set<Variable> assignedVariables = new HashSet<Variable>();
	Set<Object> EvaluatedConstants = new HashSet<Object>();
	boolean noSampling = true;
	boolean resolveFilter = false;
	boolean canFormNaturalJoin = false;
	boolean canUseLearningStats = false;
	List<FilterExpression> singleFiltersForopt;
	Map<PDChange<GUID, Object, GUID>, Long> IRX_XRI_C = new ConcurrentHashMap<PDChange<GUID, Object, GUID>, Long>();
	Map<GUID, Long> role_XRX = new ConcurrentHashMap<GUID, Long>();
	Map<GUID, Long> role_XRI = new ConcurrentHashMap<GUID, Long>();
	Map<GUID, Long> role_IRX = new ConcurrentHashMap<GUID, Long>();

	public Map<PDChange<GUID, Object, GUID>, Set<Variable>> pattern_rewrittenVariables = new ConcurrentHashMap<PDChange<GUID, Object, GUID>, Set<Variable>>();

	/**
	 * estimates the statistics for each tuple in where clause. This does not
	 * consider joined tuple selectivity.
	 * 
	 * @param q
	 * @param resolveFilter
	 * @return
	 */

	// TODO: 1) refine code 2) constants in query are also equal filters 3)IRI
	// gets the most preference

	/**
	 * Based on the result we do a pattern matching with where tuples and find
	 * values can be binded to the same variable.
	 * 
	 * @param currWhereTuple
	 * @param sampleResult
	 * @return
	 * @return
	 */
	void resolveVariable(PDChange<GUID, Object, GUID> currWhereTuple,
			PDChange<GUID, Object, GUID> sampleResult,
			Map<Variable, Object> assignedVariable) {

		if (currWhereTuple.getInstance1() instanceof Variable)
			assignedVariable.put((Variable) currWhereTuple.getInstance1(),
					sampleResult.getInstance1());

		if (currWhereTuple.getInstance2() instanceof Variable)
			assignedVariable.put((Variable) currWhereTuple.getInstance2(),
					sampleResult.getInstance2());

		if (currWhereTuple.getRole2() instanceof Variable)
			assignedVariable.put((Variable) currWhereTuple.getRole2(),
					sampleResult.getRole2());
	}

	/**
	 * From a given set of where tuples this method finds the minimum cost
	 * tuple. This makes use of selectivity estimated previously and also
	 * samples value from previous selection to estimate the current
	 * selectivity.
	 * 
	 * @param lstchanges
	 * @param q
	 * @param CurrentIndexToProcess
	 * @return
	 */
	int findMinCostChange(List<PDChange<GUID, Object, GUID>> lstchanges,
			Query q, int CurrentIndexToProcess) {

		int indexOfMinComplexity = CurrentIndexToProcess;
		long minComplexitySoFar = Long.MAX_VALUE;
		long currentChangeCost = 0;

		for (int index = CurrentIndexToProcess; index >= 0; index--) {

			// find element with min complexity
			currentChangeCost = costInWhereClause(q, assignedVariables,
					q.where.get(index));
			if (currentChangeCost < minComplexitySoFar) {
				minComplexitySoFar = currentChangeCost;
				indexOfMinComplexity = index;
			}
		}
		return indexOfMinComplexity;

	}

	public long costInWhereClause(Query q, Set<Variable> assignedVariables,
			PDChange<GUID, Object, GUID> currentWhere) {
		return 0;
	}

	// todo change the function name later;
	boolean isIRXorXRI_withIasConstant(PDChange<GUID, Object, GUID> w) {
		Object I1 = w.getInstance1();
		Object I2 = w.getInstance2();

		if (w.getRole2() instanceof Variable)
			return false;

		if (!(I1 instanceof Variable))
			return true;
		else if (!(I2 instanceof Variable))
			return true;

		return false;
	}

	// todo change the function name later;
	private boolean isConstantRDFTerm(Object I) {
		if (!(I instanceof Variable))
			return true;
		return false;
	}

	/**
	 * This method tries choose patterns to form natural join or prefers
	 * patterns with constants or prefers patterns on which filters can be
	 * applied
	 * 
	 * @param q
	 * @param lstpatterns
	 * @param totalSize
	 */
	List<PDChange<GUID, Object, GUID>> getPreferredPatterns(Query q,
			int totalSize) {
		Object I1;
		Object I2;
		Object R1;
		PDChange<GUID, Object, GUID> currentPattern;
		List<PDChange<GUID, Object, GUID>> lstpatterns = new ArrayList<PDChange<GUID, Object, GUID>>();
		// List<PDChange<GUID, Object, GUID>> lstIRIPatterns = new
		// ArrayList<PDChange<GUID, Object, GUID>>();
		Map<PDChange<GUID, Object, GUID>, Integer> patternPriority = new HashMap<PDChange<GUID, Object, GUID>, Integer>();
		int currentPatternPriority = 0;
		for (int i = 0; i <= totalSize; i++) {
			currentPatternPriority = 0;
			currentPattern = q.where.get(i);
			I1 = currentPattern.getInstance1();
			I2 = currentPattern.getInstance2();
			R1 = currentPattern.getRole2();

			// see if we have an IRI pattern somewhere, then give high priority
			if (ChangeTemplateKind.getKind(currentPattern, assignedVariables) == ChangeTemplateKind.IRI) {
				// very high priority
				currentPatternPriority = currentPatternPriority + 100;
			}

			if (isConstantRDFTerm(I1)) {
				currentPatternPriority = currentPatternPriority + 3;
			}
			if (isConstantRDFTerm(I2)) {
				currentPatternPriority = currentPatternPriority + 3;
			}
			// if (isConstantRDFTerm(R1)) {
			// currentPatternPriority = currentPatternPriority + 3;
			// }

			// Manipulating variables to predict if a filter expr can be applied
			// to this pattern or not
			// todo: this should be more than a boolean function.. because
			// multiple filters can be applied on the same pattern
			Set<Variable> tempVar = new HashSet<Variable>(assignedVariables);
			if (I1 instanceof Variable) {
				tempVar.add((Variable) I1);
			}
			if (I2 instanceof Variable) {
				tempVar.add((Variable) I2);
			}
			if (R1 instanceof Variable) {
				tempVar.add((Variable) R1);
			}
			if (hasEligibleFilterExpression(tempVar, q)) // has filter
															// applicable
			{
				currentPatternPriority = currentPatternPriority + 2;
			}

			if (assignedVariables != null && assignedVariables.size() > 0) {

				if (checkIfVariableExists(I1)) {
					currentPatternPriority++;
				} else if (checkIfVariableExists(I2)) {
					currentPatternPriority++;
				} else if (checkIfVariableExists(R1)) {
					currentPatternPriority++;
				}
			}
			patternPriority.put(currentPattern, currentPatternPriority);
		}
		// race between constants and assigned variables
		// TODO: this is important, analyze this later
		// lstInstanceConstantPatterns.removeAll(lstpatterns);
		// lstInstanceConstantPatterns.addAll(lstpatterns);

		// get the highest priority patterns
		Collection patternPrioritiesValues = patternPriority.values();

		while (true) {
			int maxvalue = (Integer) Collections.max(patternPrioritiesValues);
			getPatternsWithMaxPriority(lstpatterns, patternPriority,
					patternPrioritiesValues);

			patternPrioritiesValues.remove(maxvalue);

			lstpatterns = getNaturallyJoiningPatterns(lstpatterns);
			if (lstpatterns.size() > 0 || patternPrioritiesValues.size() == 0)
				break;
		}

		return lstpatterns;
	}

	private void getPatternsWithMaxPriority(
			List<PDChange<GUID, Object, GUID>> lstpatterns,
			Map<PDChange<GUID, Object, GUID>, Integer> patternPriority,
			Collection patternPriorities) {
		int maxValueInMap = (Integer) Collections.max(patternPriorities);
		for (Entry<PDChange<GUID, Object, GUID>, Integer> entry : patternPriority
				.entrySet()) {
			if (entry.getValue() == maxValueInMap) {
				lstpatterns.add(entry.getKey());
			}
		}
	}

	private List<PDChange<GUID, Object, GUID>> getNaturallyJoiningPatterns(
			List<PDChange<GUID, Object, GUID>> lstpatterns) {
		Object I1;
		Object I2;
		Object R1;
		List<PDChange<GUID, Object, GUID>> lstNaturallyJoinedpatterns1 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> lstNaturallyJoinedpatterns2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		List<PDChange<GUID, Object, GUID>> lstNaturallyJoinedpatterns3 = new ArrayList<PDChange<GUID, Object, GUID>>();

		if (assignedVariables.size() == 0 && EvaluatedConstants.size() == 0
				&& pattern_rewrittenVariables.size() == 0)
			return lstpatterns;

		for (PDChange<GUID, Object, GUID> currentPattern : lstpatterns) {

			I1 = currentPattern.getInstance1();
			I2 = currentPattern.getInstance2();
			R1 = currentPattern.getRole2();

			if (assignedVariables != null && assignedVariables.size() > 0) {

				if (checkIfVariableExists(I1)) {
					lstNaturallyJoinedpatterns1.add(currentPattern);
				} else if (checkIfVariableExists(I2)) {
					lstNaturallyJoinedpatterns1.add(currentPattern);
				} else if (checkIfVariableExists(R1)) {
					lstNaturallyJoinedpatterns1.add(currentPattern);
				}
			}
			if (EvaluatedConstants != null && EvaluatedConstants.size() > 0) {

				if (EvaluatedConstants.contains(I1)) {
					lstNaturallyJoinedpatterns2.add(currentPattern);
				} else if (EvaluatedConstants.contains(I2)) {
					lstNaturallyJoinedpatterns2.add(currentPattern);
				} else if (EvaluatedConstants.contains(R1)) {
					lstNaturallyJoinedpatterns2.add(currentPattern);
				}
			}

			if (pattern_rewrittenVariables.size() > 0) {

				Set<Variable> vars = pattern_rewrittenVariables
						.get(currentPattern);

				for (Variable V : vars) {
					if (assignedVariables.contains(V)) {
						lstNaturallyJoinedpatterns3.add(currentPattern);
						break;
					}
				}
			}

		}
		lstNaturallyJoinedpatterns2.addAll(lstNaturallyJoinedpatterns3);
		lstNaturallyJoinedpatterns1.addAll(lstNaturallyJoinedpatterns2);
		return lstNaturallyJoinedpatterns1;

	}

	private boolean checkIfVariableExists(Object I1) {
		return I1 instanceof Variable && assignedVariables.contains(I1);
	}

	/**
	 * Sets up the lists readFromAssignment and writeToAssignment, which
	 * describe for each where-tuple which variables in the tuple are set by
	 * reading from the existing assignment, and which variable values are
	 * written from the tuple to the assignment.
	 * 
	 * Example: (x,y,2), (x,z,2) read from assignment: nothing IRI write to
	 * assignment: x and z XXI read from assignment: x XRI write to assignment:
	 * y IXI
	 */
	void createReadWriteAssignment(Set<Variable> assignedVariables, int start,
			int end, Query q) {
		if (assignedVariables == null)
			assignedVariables = new HashSet<Variable>();

		for (int i = start; i >= end; i--) {
			Object instance1 = q.where.get(i).getInstance1();
			GUID role2 = q.where.get(i).getRole2();
			Object instance2 = q.where.get(i).getInstance2();
			switch (ChangeTemplateKind.getKind(q.where.get(i))) {

			case IRI:
				q.readFromAssignment[i] = ChangeTemplateKind.IRI;
				q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				break;

			case IRX:
				// case x is already written
				if (assignedVariables.contains(instance2)) {
					q.readFromAssignment[i] = ChangeTemplateKind.IRX;
					q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				} else {
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.IRI;
					q.writeToAssignment[i] = ChangeTemplateKind.IRX;
				}
				break;

			case XRI:
				if (assignedVariables.contains(instance1)) {
					q.readFromAssignment[i] = ChangeTemplateKind.XRI;
					q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				} else {
					assignedVariables.add((Variable) instance1);
					q.readFromAssignment[i] = ChangeTemplateKind.IRI;
					q.writeToAssignment[i] = ChangeTemplateKind.XRI;
				}
				break;

			case XRX:
				if (assignedVariables.contains(instance1)
						&& assignedVariables.contains(instance2)) {
					q.readFromAssignment[i] = ChangeTemplateKind.XRX;
					q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				} else if (assignedVariables.contains(instance1)) {
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.XRI;
					q.writeToAssignment[i] = ChangeTemplateKind.IRX;
				} else if (assignedVariables.contains(instance2)) {
					assignedVariables.add((Variable) instance1);
					q.readFromAssignment[i] = ChangeTemplateKind.IRX;
					q.writeToAssignment[i] = ChangeTemplateKind.XRI;
				} else {
					assignedVariables.add((Variable) instance1);
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.IRI;
					q.writeToAssignment[i] = ChangeTemplateKind.XRX;
				}
				break;

			case IXI:
				if (assignedVariables.contains(role2)) {
					q.readFromAssignment[i] = ChangeTemplateKind.IXI;
					q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				} else {
					assignedVariables.add((Variable) role2);
					q.readFromAssignment[i] = ChangeTemplateKind.IRI;
					q.writeToAssignment[i] = ChangeTemplateKind.IXI;
				}
				break;

			case XXI:
				if (assignedVariables.contains(role2)
						&& assignedVariables.contains(instance1)) {
					q.readFromAssignment[i] = ChangeTemplateKind.XXI;
					q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				} else if (assignedVariables.contains(instance1)) {
					assignedVariables.add((Variable) role2);
					q.readFromAssignment[i] = ChangeTemplateKind.XRI;
					q.writeToAssignment[i] = ChangeTemplateKind.IXI;
				} else if (assignedVariables.contains(role2)) {
					assignedVariables.add((Variable) instance1);
					q.readFromAssignment[i] = ChangeTemplateKind.IXI;
					q.writeToAssignment[i] = ChangeTemplateKind.XRI;
				} else {
					assignedVariables.add((Variable) instance1);
					assignedVariables.add((Variable) role2);
					q.readFromAssignment[i] = ChangeTemplateKind.IRI;
					q.writeToAssignment[i] = ChangeTemplateKind.XXI;
				}
				break;

			case IXX:
				if (assignedVariables.contains(role2)
						&& assignedVariables.contains(instance2)) {
					q.readFromAssignment[i] = ChangeTemplateKind.IXX;
					q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				} else if (assignedVariables.contains(instance2)) {
					assignedVariables.add((Variable) role2);
					q.readFromAssignment[i] = ChangeTemplateKind.IRX;
					q.writeToAssignment[i] = ChangeTemplateKind.IXI;
				} else if (assignedVariables.contains(role2)) {
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.IXI;
					q.writeToAssignment[i] = ChangeTemplateKind.IRX;
				} else {
					assignedVariables.add((Variable) instance2);
					assignedVariables.add((Variable) role2);
					q.readFromAssignment[i] = ChangeTemplateKind.IRI;
					q.writeToAssignment[i] = ChangeTemplateKind.IXX;
				}
				break;

			case XXX:
				if (assignedVariables.contains(role2)
						&& assignedVariables.contains(instance1)
						&& assignedVariables.contains(instance2)) {
					q.readFromAssignment[i] = ChangeTemplateKind.XXX;
					q.writeToAssignment[i] = ChangeTemplateKind.IRI;
				} else if (assignedVariables.contains(role2)
						&& assignedVariables.contains(instance2)) {
					assignedVariables.add((Variable) instance1);
					q.readFromAssignment[i] = ChangeTemplateKind.IXX;
					q.writeToAssignment[i] = ChangeTemplateKind.XRI;
				} else if (assignedVariables.contains(role2)
						&& assignedVariables.contains(instance1)) {
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.XXI;
					q.writeToAssignment[i] = ChangeTemplateKind.IRX;
				} else if (assignedVariables.contains(instance2)
						&& assignedVariables.contains(instance1)) {
					assignedVariables.add((Variable) role2);
					q.readFromAssignment[i] = ChangeTemplateKind.XRX;
					q.writeToAssignment[i] = ChangeTemplateKind.IXI;
				} else if (assignedVariables.contains(instance1)) {
					assignedVariables.add((Variable) role2);
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.XRI;
					q.writeToAssignment[i] = ChangeTemplateKind.IXX;
				} else if (assignedVariables.contains(instance2)) {
					assignedVariables.add((Variable) role2);
					assignedVariables.add((Variable) instance1);
					q.readFromAssignment[i] = ChangeTemplateKind.IRX;
					q.writeToAssignment[i] = ChangeTemplateKind.XXI;
				} else if (assignedVariables.contains(role2)) {
					assignedVariables.add((Variable) instance1);
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.IXI;
					q.writeToAssignment[i] = ChangeTemplateKind.XRX;
				} else {
					assignedVariables.add((Variable) instance1);
					assignedVariables.add((Variable) role2);
					assignedVariables.add((Variable) instance2);
					q.readFromAssignment[i] = ChangeTemplateKind.IRI;
					q.writeToAssignment[i] = ChangeTemplateKind.XXX;
				}
				break;

			default:
				break;
			}

			// Adding transaction variables in assigned variables to make them
			// used in filter
			if (q.where.get(i).getTransaction() instanceof Variable)
				assignedVariables.add((Variable) q.where.get(i)
						.getTransaction());
			// End

			// Find Filter Expressions that can be evaluated.
			// join them to one expression with AND operator.
			// store in filtersToEvaluate [i+1]
			// its i+1 since they should be evaluated at the start
			// of the next step.
			createFiltersToEvaluate(assignedVariables, i, q);
		}
	}

	void createFiltersToEvaluate(Set<Variable> assignedVariables, int i, Query q) {
		if (q.singleFilters == null)
			return;

		Iterator<FilterExpression> filterIterator = q.singleFilters.iterator();
		List<FilterExpression> filterForATuple = new ArrayList<FilterExpression>();

		// find the filters can be evaluated for where.get(i)
		// put them in an array so a single AndExpression can combine
		// all of them
		// then set filtersToEvaluate [i] as this AndExpression
		while (filterIterator.hasNext()) {
			FilterExpression singleFilter = filterIterator.next();
			if (matchFilterAndPDChange(singleFilter, assignedVariables)) {
				filterForATuple.add(singleFilter);
				// with the new CopyOnWriteArrayList, the remove happens on the
				// list, not the iterator.
				q.singleFilters.remove(singleFilter);
			}
		}

		FilterExpression[] filterForATupleArray;
		if (filterForATuple.iterator().hasNext()) {

			filterForATupleArray = new FilterExpression[filterForATuple.size()];
			for (int k = 0; k < filterForATuple.size(); k++) {
				filterForATupleArray[k] = filterForATuple.get(k);
			}

			AndExpression andExpression = new AndExpression(
					filterForATupleArray);
			q.filtersToEvaluate[i] = andExpression;
		}
	}

	boolean matchFilterAndPDChange(FilterExpression singleFilter,
			Set<Variable> assignedVariables) {

		return assignedVariables.containsAll(singleFilter.getVariables());
	}

	void swapWhereTuple(Query q, int indexOfMinCostChange, int totalSize) {
		// Swap min cost triple to the current position
		PDChange<GUID, Object, GUID> tempChange = q.where.get(totalSize);
		q.where.set(totalSize, q.where.get(indexOfMinCostChange));
		q.where.set(indexOfMinCostChange, tempChange);
	}

	boolean hasEligibleFilterExpression(Set<Variable> assignedVariables, Query q) {
		boolean hasFilter = false;

		Iterator<FilterExpression> filterIterator = singleFiltersForopt
				.iterator();
		while (filterIterator.hasNext()) {
			FilterExpression singleFilter = filterIterator.next();

			if (matchFilterAndPDChange(singleFilter, assignedVariables)) {
				singleFiltersForopt.remove(singleFilter);// because this filter
															// is used up by a
															// pattern, do not
															// use it again for
															// another pattern
				return true;
			}
		}
		return hasFilter;
	}

	/**
	 * In the case of multiple optionals graph pattern, the optionals are
	 * arranged so that they can be naturally joined whenver possible.
	 * 
	 * @param q
	 */
	public void reorderOptionals(Query q) {
		double totalvariable = 0;
		double resolvedVariable = 0;
		double probability = 0;
		Set<Variable> resolveOptional = new HashSet<Variable>();
		Set<Variable> resolveAll = new HashSet<Variable>();
		List<Set<Variable>> lstresovedOptionals = new ArrayList<Set<Variable>>();
		for (PDChange<GUID, Object, GUID> w : q.where) {
			if (w.getInstance1() instanceof Variable
					&& !resolveAll.contains(w.getInstance1()))
				resolveAll.add((Variable) w.getInstance1());

			if (w.getRole2() instanceof Variable
					&& !resolveAll.contains(w.getRole2()))
				resolveAll.add((Variable) w.getRole2());

			if (w.getInstance2() instanceof Variable
					&& !resolveAll.contains(w.getInstance2()))
				resolveAll.add((Variable) w.getInstance2());
		}

		for (int optcnt = q.optionals.size() - 1; optcnt >= 0; optcnt--) {
			double minProbabilitySoFar = 1;
			int indexOfMinComplexity = 0;
			resolveOptional.clear();
			for (int j = optcnt; j >= 0; j--) {
				resolveOptional.clear();
				Query queryOptional = q.optionals.get(j);
				for (PDChange<GUID, Object, GUID> w : queryOptional.where) {

					if (w.getInstance1() instanceof Variable) {
						totalvariable++;
						if (resolveAll.contains(w.getInstance1()))
							resolvedVariable++;
						else
							resolveOptional.add((Variable) w.getInstance1());
					}
					if (w.getRole2() instanceof Variable) {
						totalvariable++;
						if (resolveAll.contains(w.getRole2()))
							resolvedVariable++;
						else
							resolveOptional.add((Variable) w.getRole2());
					}
					if (w.getInstance2() instanceof Variable) {
						totalvariable++;
						if (resolveAll.contains(w.getInstance2()))
							resolvedVariable++;
						else
							resolveOptional.add((Variable) w.getInstance2());
					}
				}
				// calculate the resolving value
				if (totalvariable == 0)
					probability = 0;
				else if (resolvedVariable == 0)
					probability = 1;
				else
					probability = resolvedVariable / totalvariable;

				if (probability < minProbabilitySoFar) {
					minProbabilitySoFar = probability;
					indexOfMinComplexity = j;
				}
				lstresovedOptionals.add(resolveOptional);
			}
			resolveAll.addAll(lstresovedOptionals.get(indexOfMinComplexity));
			lstresovedOptionals.clear();
			Query tempOptional = q.optionals.get(optcnt);
			q.optionals.set(optcnt, q.optionals.get(indexOfMinComplexity));
			q.optionals.set(indexOfMinComplexity, tempOptional);
		}
	}

	public void optimizeQuery(Query q) {

		assignedVariables = new HashSet<Variable>();
		singleFiltersForopt = new ArrayList<FilterExpression>(q.singleFilters);

		// all variable assignments will be rewritten into query, so adding now
		// into assigned variables
		if (q.incomingAssignment != null && q.incomingAssignment.size() > 0)
			assignedVariables.addAll(q.incomingAssignment.keySet());
		assignedVariables.addAll(q.queryAssignedVariablesSoFar);

		// TODO: the following method does not do what it claims to do and
		// introdcued
		// serious error: changed the where clause and filled values of the
		// initial assignment.
		// This led to wrong results when changing the assignment.
		// re-write query with equal filter expressions
		// queryRewriting(q);

		// if there is only one triple in BGP, no need to optimize
		if (q.where.size() <= 1) {
			// single pattern in the basic graph pattern
			createReadWriteAssignment(assignedVariables, 0, 0, q);
			q.queryAssignedVariablesSoFar.addAll(assignedVariables);
			return;
		}
		int indexOfMinCostChange;
		List<PDChange<GUID, Object, GUID>> lstchanges = new ArrayList<PDChange<GUID, Object, GUID>>();
		int totalSize = q.where.size() - 1;
		while (totalSize >= 0) {
			// if no optimizer is choosen, only read write assignments are
			// created
			if (q.currentOptimizerType != OptimimzerType.NoOpt && totalSize > 0) {
				lstchanges = new ArrayList<PDChange<GUID, Object, GUID>>();
				indexOfMinCostChange = findMinCostChange(lstchanges, q,
						totalSize);
				swapWhereTuple(q, indexOfMinCostChange, totalSize);

			}
			createReadWriteAssignment(assignedVariables, totalSize, totalSize,
					q);
			totalSize--;
		}

		if (q.optionals != null && q.optionals.size() > 0)
		// TODO: reorder optional is execution strategy or optimization strategy
		// ?
		// q.currentOptimizerType != OptimimzerType.NoOpt)
		{
			reorderOptionals(q);
		}
		q.queryAssignedVariablesSoFar.addAll(assignedVariables);
	}

}