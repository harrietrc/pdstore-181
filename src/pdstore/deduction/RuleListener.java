package pdstore.deduction;

import nz.ac.auckland.se.genoupe.tools.Debug;
import nz.ac.auckland.se.genoupe.tools.L;
import nz.ac.auckland.se.genoupe.tools.Stopwatch;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListenerAdapter;
import pdstore.sparql.*;

import java.util.Iterator;
import java.util.List;

/**
 *   Creates a listener that ensures incremental semi-naive Rule evaluation, if a change matching "pattern"
 *   is inserted. See also ListenerRule for the general explanation of incremental semi-naive.
 *     
 * 
 *   @author Christof, Gerald
 *
 */
public class RuleListener extends
        PDListenerAdapter<GUID, Object, GUID> {

    Query query;
    PDStore store;
    private PDChange<GUID, Object, GUID> pattern;
    ListenerRule rule;
	private boolean querySize2orMore;

    public RuleListener(ListenerRule rule, PDChange<GUID, Object, GUID> pattern) {
        super();
        this.rule = rule;
        this.pattern =  pattern;
        store = (PDStore) rule.store;

        getMatchingTemplates().add(pattern);

        // create a deep clone of the condition query, to modify for this listener
        query = new Query(true, rule);
        
        querySize2orMore = query.getWhere().size()>1;
		if(querySize2orMore)  {
          Debug.assertTrue(query.getWhere().remove(pattern), "pattern not found");
        } else {
        	// case size== 1 must be treated differently, pseudoInsertionListener is triggered directly
        }
        Debug.println("-------\nListener created: " + pattern + "\n query:" + this.query, this.getClass().getName()+"basic");
    }

    @Override
    public void transactionCommitted(
            List<PDChange<GUID, Object, GUID>> transaction,
            List<PDChange<GUID, Object, GUID>> matchedChanges,
            PDCoreI<GUID, Object, GUID> core) {
        //Debug.println("invoked! ", this.getClass().getName());
    	
    	Stopwatch w = new Stopwatch();
    	w.start();

        if (matchedChanges == null) return;

        for (PDChange<GUID, Object, GUID> change2 : matchedChanges) {
            PDChange<GUID, Object, GUID> change = change2.getNormalizedChange();
            if (!change.matches(pattern))
                continue;
            
               enactRule(change);
 
        }
        w.stop();
        if(w.seconds() > 60.0) {
         System.err.println("Time: "+ w.seconds()+" Change: "+ matchedChanges);
        }
    }
    
    public void enactRule(PDChange<GUID, Object, GUID> change) {
        Debug.println(L.s("invoked " , rule , " for change: " , change), this.getClass().getName());
        Object instance1 = pattern.getInstance1();
        Object instance2 = pattern.getInstance2();
        Object role2 = pattern.getRole2();

        // Add variables defined in the pattern to assignment
        Assignment<GUID, Object, GUID> variableAssignment = new Assignment<GUID, Object, GUID>();
        if ((instance1 instanceof Variable)) {
            Debug.println(L.s("  put: " , (Variable) instance1 , " = " , store.getLabel(change.getInstance1())), this.getClass().getName());
            variableAssignment.put((Variable) instance1, change.getInstance1());
        }

        if ((instance2 instanceof Variable)) {
            Debug.println(L.s("  put: " , (Variable) instance2 , " = " , store.getLabel(change.getInstance2())), this.getClass().getName());
            variableAssignment.put((Variable) instance2, change.getInstance2());
        }

        if ((role2 instanceof Variable)) {
            Debug.println(L.s("  put: " , (Variable) role2 , " = " , store.getLabel(change.getRole2())), this.getClass().getName());
            variableAssignment.put((Variable) role2, change.getRole2());
        }
        
        if(querySize2orMore) {
        	GUID t = store.begin();
        	Iterator<Assignment<GUID, Object, GUID>> result = query.execute(t, variableAssignment);
        	while (result.hasNext()) {
        		Assignment<GUID, Object, GUID> resultAssignment = result.next();
				InsertionPseudoFilter<GUID, Object, GUID> filterParameter = rule.singleConsequence;
				GUID transaction = change.getTransaction();
				Debug.warningAssertTrue(transaction!=null, "Transaction null");
				if(PDStore.isNotConcurrentNotPersistent)
			    	filterParameter.setInsertTransaction(transaction);
        		filterParameter.evaluate(resultAssignment);
        		Debug.println(L.s("  result: ", resultAssignment), this.getClass().getName());
        	}
           	store.commit(t);
 		} else {
			@SuppressWarnings("unchecked")
			InsertionPseudoFilter<GUID, Object, GUID> filterParameter = rule.singleConsequence;
			filterParameter.evaluate(variableAssignment);
		}
    }

	public PDChange<GUID, Object, GUID> getPattern() {
		return pattern;
	}

}
