package pdstore.sparql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.IteratorBasedCollection;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.ListenerDispatcher;
import pdstore.notify.PDListener;
import pdstore.notify.PDListenerAdapter;

/**
 * A listener that will use a SPARQL Query to construct its results.
 * See the constructor for how to construct the SPARQL query.
 * The query is executed in transactionCommitted() based on the
 * first triple provided in matchedTriples.
 * 
 * This listener is currently used as a ViewListener, and
 * therefore is usually registered with 
 * GenericPDStore.getViewDispatcher
 * 
 * @author gweb017
 * 
 */
public class SparqlViewListener extends PDListenerAdapter<GUID, Object, GUID> {

	public static final Variable ROLE2VAR2 = new Variable("pred");
	public static final Variable INSTANCE2VAR2 = new Variable("obj");
	public static final Variable INSTANCE1VAR2 = new Variable("subj");
	public Variable instance1var;
	public Variable instance2var;
	public Variable role2var;

	Query query;
	private PDChange<GUID, Object, GUID> changeTemplate;
	private boolean instance1IsSet;
	private boolean instance2IsSet;
	private boolean role2IsSet;

	/**
	 * A constructor that allows additionally to customize the variables used to
	 * represent the matched tuples.
	 * 
	 * @param instance1var
	 * @param instance2var
	 * @param role2var
	 * @param query
	 * @param changeTemplate
	 */
	public SparqlViewListener(Variable instance1var, Variable instance2var,
			Variable role2var, Query query,
			PDChange<GUID, Object, GUID> changeTemplate) {
		super();
		this.instance1var = instance1var;
		this.instance2var = instance2var;
		this.role2var = role2var;
		this.query = query;
		this.changeTemplate = changeTemplate;

		// Analyze the change template for the later processing:
		Object instance1 = changeTemplate.getInstance1();
		instance1IsSet = instance1 != null && ! (instance1 instanceof Variable);
		Object instance2 = changeTemplate.getInstance2();
		instance2IsSet = instance2 != null && ! (instance2 instanceof Variable);
		Object role2 = changeTemplate.getRole2();
		role2IsSet = role2 != null && ! (role2 instanceof Variable);;

		// Three known positions seem largely pointless and are not supported by
		// addMySelf()
		Debug.assertTrue(!(instance1IsSet && instance2IsSet && role2IsSet),
				"not intended to be used with three known positions");

	}

	/**
	 * @param query
	 *            The Sparql operation executed if the listener is called. This
	 *            Sparql query should contain the variables "subj", "pred",
	 *            "obj", and in these variables it gets the information on which
	 *            change the listener is called (the matched change from the
	 *            transactionCommitted method)
	 * @param change
	 *            the change template that this listener is reacting to.
	 */
	public SparqlViewListener(Query query, PDChange<GUID, Object, GUID> change) {
		this(INSTANCE1VAR2, INSTANCE2VAR2, ROLE2VAR2, query, change);

	}

	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges,
			PDCoreI<GUID, Object, GUID> core) {
		Debug.println("SparqlViewListener", "PDStore");
		//Debug.assertTrue(core==query.store, "listener called on wrong store");
		for (PDChange<GUID, Object, GUID> change : matchedChanges) {
			Assignment<GUID, Object, GUID> variableAssignment = new Assignment<GUID, Object, GUID>();

			// Enforce consistency with the changeTemplate provided:
			// The variables are preset:
			Object instance1 = changeTemplate.getInstance1();
			if (instance1IsSet) {
				variableAssignment.put(instance1var, instance1);
			} else if (change.getInstance1() != null) {
				variableAssignment.put(instance1var, change.getInstance1());

			}

			Object instance2 = changeTemplate.getInstance2();
			if (instance2IsSet) {
				variableAssignment.put(instance2var, instance2);
			} else if (change.getInstance2() != null) {
				variableAssignment.put(instance2var, change.getInstance2());

			}

			Object role2 = changeTemplate.getRole2();
			if (role2IsSet) {
				variableAssignment.put(role2var, role2);
			} else if (change.getRole2() != null) {
				variableAssignment.put(role2var, change.getRole2());
			}
			/*
			 * TODO: support transactions as variables
			 * if(changeTemplate.getTransaction()()!=null)
			 * variableAssignment.put(this.transaction,
			 * changeTemplate.getTransaction());
			 * 
			 * TODO: change this to a lazy process, using iterator concatenation
			 */

			Iterator<Assignment<GUID, Object, GUID>> resultIterator = query
					.execute(change.getTransaction(), variableAssignment);
			for (Assignment<GUID, Object, GUID> row = resultIterator.next(); row!=null; row = resultIterator.next()) {
				transaction.add(new PDChange<GUID, Object, GUID>(change
						.getChangeType(), change.getTransaction(), row
						.get(instance1var), (GUID) row.get(role2var), row
						.get(instance2var)));
			}
		}

	}

	/**
	 * Adding ViewListeners is a bit more tricky than adding other Listeners.
	 * ViewListeners have to be added several times. This is because
	 * ViewListeners need to be matched against getChange parameters, which are
	 * again change templates and can contain null values or variables. This is
	 * a not completely trivial case of unification.
	 * 
	 * Other listeners are matched only against individual changes.
	 * 
	 * The following example illustrates the additional problem in matching
	 * change templates against other change templates:
	 * 
	 * Assume a ViewListener with a changeTemplate that has role2=studiesAt,
	 * instance2= UniAuckland, instance1=null. which computes all current
	 * students as instance1.
	 * 
	 * This ViewListener is, as usual, entered under that change template. A
	 * getChanges request with the same change template will correctly get all
	 * students.
	 * 
	 * However, assume now a getChanges request with a changeTemplate: null,
	 * null, null, null, UniAuckland. This will not search the index with
	 * role2=studiesAt. Hence it would not find the listener.
	 * 
	 * Unfortunately it is not generally efficient enough to just change the
	 * matching procedure in the dispatcher. At first glance it seems enough to
	 * proceed as follows: if role = null, search not only the index for
	 * role=null, but all other indices for role=constantX. However, if there
	 * are 10000 roles entered, this is not practical anymore.
	 * 
	 * Hence the listener must be added a second time in this example, for
	 * change template null, null, null, null, UniAuckland.
	 * 
	 * Fortunately this can be limited to a second adding for the following
	 * reasons: - Views with three known positions need not be supported. If
	 * there are two known positions, then only the first one requires this
	 * treatment. First means here the order in which the positions are entered
	 * into the Listener Dispatcher index.
	 * 
	 * 
	 * @param dispatcher
	 */
	public void addMyself(ListenerDispatcher<GUID, Object, GUID> dispatcher) {
		// The usual add command that is compatible with general listeners.
		dispatcher.addListener(this, changeTemplate);

		// analyze template structure further:
		// The following code is a bit repetitive, but cannot be easily
		// generalized
		// since it deals with different fields of PDChange.
		if (instance1IsSet && instance2IsSet && role2IsSet) {

			PDChange<GUID, Object, GUID> generalizedChange = new PDChange<GUID, Object, GUID>(
					changeTemplate);
			generalizedChange.setInstance1(null);
			dispatcher.addListener(this, generalizedChange);

			generalizedChange = new PDChange<GUID, Object, GUID>(changeTemplate);
			generalizedChange.setInstance2(null);
			dispatcher.addListener(this, generalizedChange);

			generalizedChange = new PDChange<GUID, Object, GUID>(changeTemplate);
			generalizedChange.setRole2(null);
			dispatcher.addListener(this, generalizedChange);

			generalizedChange = new PDChange<GUID, Object, GUID>(changeTemplate);
			generalizedChange.setInstance1(null);
			generalizedChange.setInstance2(null);
			dispatcher.addListener(this, generalizedChange);

			generalizedChange = new PDChange<GUID, Object, GUID>(changeTemplate);
			generalizedChange.setInstance1(null);
			generalizedChange.setRole2(null);
			dispatcher.addListener(this, generalizedChange);

			generalizedChange = new PDChange<GUID, Object, GUID>(changeTemplate);
			generalizedChange.setInstance2(null);
			generalizedChange.setRole2(null);
			dispatcher.addListener(this, generalizedChange);

		} else {

			if (instance2IsSet && instance1IsSet) {
				// instance1 needs to be added a second time
				PDChange<GUID, Object, GUID> generalizedChange = new PDChange<GUID, Object, GUID>(
						changeTemplate);
				generalizedChange.setInstance1(null);
				dispatcher.addListener(this, generalizedChange);

				generalizedChange = new PDChange<GUID, Object, GUID>(
						changeTemplate);
				generalizedChange.setInstance2(null);

				dispatcher.addListener(this, generalizedChange);

				generalizedChange = new PDChange<GUID, Object, GUID>(
						changeTemplate);
				generalizedChange.setInstance1(null);
				generalizedChange.setInstance2(null);
				dispatcher.addListener(this, generalizedChange);

			}
			if (role2IsSet && (instance1IsSet || instance2IsSet)) {
				// role2 needs to be added a second time
				PDChange<GUID, Object, GUID> generalizedChange = new PDChange<GUID, Object, GUID>(
						changeTemplate);
				generalizedChange.setRole2(null);
				dispatcher.addListener(this, generalizedChange);

				generalizedChange = new PDChange<GUID, Object, GUID>(
						changeTemplate);
				generalizedChange.setInstance2(null);

				dispatcher.addListener(this, generalizedChange);

				generalizedChange = new PDChange<GUID, Object, GUID>(
						changeTemplate);
				generalizedChange.setInstance1(null);

				dispatcher.addListener(this, generalizedChange);

				generalizedChange = new PDChange<GUID, Object, GUID>(
						changeTemplate);
				generalizedChange.setInstance1(null);
				generalizedChange.setInstance2(null);
				dispatcher.addListener(this, generalizedChange);

			}
		}
	}

}
