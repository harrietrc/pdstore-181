package pdstore.deduction;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.PDStoreI;
import pdstore.notify.ListenerDispatcher;
import pdstore.notify.PDListener;
import pdstore.sparql.Assignment;
import pdstore.sparql.FilterExpression;
import pdstore.sparql.InsertionPseudoFilter;
import pdstore.sparql.Query;
import pdstore.sparql.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nz.ac.auckland.se.genoupe.tools.Debug;

/**
 * Representing a deductive rule for semi-naive execution.
 * The superclass is used as the precondition of the rule, in the form of a SPARQL query.
 *
 * @author gweb017
 */
public class ListenerRule extends Query {

    public String name = "unnamed rule";
    
	/**
	 *  A role used to optimize Rules for reified immutable records, i.e.
	 *  records where all fields are entered at the same time (not necesarily
	 *  in the same transaction)
	 *  It connects two roles of the reified record 
	 *  The instance2 should be a 1..1 field of the record.
	 *  The instance2 role should be inserted last.
	 *  This role is used to avoid adding superfluous listeners, i.e.
	 *  a listener for the role at instance2 suffices.
	 * 
	 */
	public final static GUID GOES_WITH_ROLE  = new GUID(
	        "96ca7370cb9611e3ae4a1cc1dec00ed3");

	/** 
	 * The where tuples that should become triggers for semi-naive evaluation.
	 */
	List<PDChange<GUID, Object, GUID>> trigger = null;
	
    /**
     * Postcondition of the rule, in the form of a list of changes to insert.
     * Currently unused, because this right hand side is realized as insertionpseudofilters,
     * but might make sense in the long term.
     */
    public List<PDChange<GUID, Object, GUID>> consequence;
    
    public InsertionPseudoFilter<GUID, Object, GUID> singleConsequence = null;
    
	private ListenerDispatcher<GUID, Object, GUID> dispatcher;

	private List<RuleListener> listeners = new ArrayList<RuleListener>();

    /**
     * Constructor for ListenerRule  derived from Superclass and fields.
     *
     * @param where
     * @param filter
     * @param optionals
     * @param store
     * @param consequence
     */
    @SuppressWarnings("unchecked")
	public ListenerRule(List<PDChange<GUID, Object, GUID>> where,
						List<PDChange<GUID, Object, GUID>> trigger,
                        FilterExpression filter,
                        List<Query> optionals,
                        PDStoreI<GUID, Object, GUID> store,
                        List<PDChange<GUID, Object, GUID>> consequence) {
        super(null, where, filter, optionals, store);
        
        if (trigger == null) 
        	this.trigger = where;
        else {
        	
            System.err.println("Rule Limited by trigger");
        	this.trigger = trigger;
         }       
        this.consequence = consequence;

        // TODO may be removed once the optimizer works properly
        this.currentOptimizerType = OptimimzerType.NoOpt;
    }

    public ListenerRule(List<PDChange<GUID, Object, GUID>> condition,
    		List<PDChange<GUID, Object, GUID>> trigger,
            PDChange<GUID, Object, GUID> consequence,
            PDStoreI<GUID, Object, GUID> store) {
    	this(condition, trigger, null, null, store,
    			new ArrayList<PDChange<GUID, Object, GUID>>(Arrays.asList(consequence))); 
    	if(consequence instanceof InsertionPseudoFilter<?, ?, ?>)
    		singleConsequence = (InsertionPseudoFilter<GUID, Object, GUID>)consequence;
    	else 
    		singleConsequence = new InsertionPseudoFilter<GUID, Object, GUID>(consequence, store);
    }

    public ListenerRule(String name,
                        List<PDChange<GUID, Object, GUID>> condition,
                        PDChange<GUID, Object, GUID> consequence,
                        PDStoreI<GUID, Object, GUID> store) {
        this(condition, null, consequence, store);
        this.name = name;
    }

    public ListenerRule(String name,
            List<PDChange<GUID, Object, GUID>> condition,
            List<PDChange<GUID, Object, GUID>> trigger,
            PDChange<GUID, Object, GUID> consequence,
            PDStoreI<GUID, Object, GUID> store) {
    	this(condition, trigger, consequence, store);
    	this.name = name;
    }

    /**
     * This method must be called with the DetachedListenerDispatcher as parameter, in order to make the rule active.
     *
     * @param dispatcher
     */
    public void addTemplates(ListenerDispatcher<GUID, Object, GUID> dispatcher) {
        this.dispatcher = dispatcher;
        for (PDChange<GUID, Object, GUID> template : trigger) {
           	PDStore pdstore = (PDStore)store;
    		Collection<Object> result1 = pdstore.getInstances(template.getRole2(), GOES_WITH_ROLE);
    		if(result1.isEmpty())
              dispatcher.add(new RuleListener(this, template));
        }
    }

    /**
     * This method must be called with the DetachedListenerDispatcher as parameter, in order to make the rule active.
     *
     * @param dispatcher
     */
    public void addTemplates(List<PDListener<GUID, Object, GUID>> dispatcher) {
    	for (PDChange<GUID, Object, GUID> template : trigger) {
            RuleListener ruleListener = new RuleListener(this, template);
            listeners.add(ruleListener);
			dispatcher.add(ruleListener);
        }
    }

    @Override
    public String toString() {
        return name;
    }

	public boolean deleteFromDispatcher() {
		boolean allRemoved = true;
        for (RuleListener l : listeners) {
        	allRemoved = dispatcher.removeListener(l,l.getPattern()) && allRemoved;
        }  
        return allRemoved;
	}
}
