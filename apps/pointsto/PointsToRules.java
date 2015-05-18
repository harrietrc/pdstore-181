package pointsto;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.deduction.ListenerRule;
import pdstore.sparql.InsertionPseudoFilter;
import pdstore.sparql.Variable;

import java.util.*;

/**
 * Created by clut002 on 19-May-14.
 */
public class PointsToRules {
    public final static GUID STORE_LEFT = new GUID(
            "e034ddbfc84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID STORE_RIGHT = new GUID(
			"e034ddbdc84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID STORE_FIELD = new GUID(
			"e034ddbbc84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID LOAD_FIELD = new GUID(
			"e034ddc2c84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID LOAD_RIGHT = new GUID(
			"e034ddb9c84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID LOAD_LEFT = new GUID(
			"e034ddc4c84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID ASSIGN = new GUID(
			"e034ddb7c84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID ALLOC = new GUID(
			"e034ddc8c84011e39cc41cc1dec00ed3").getFirst();
    public final static GUID POINTS_TO = new GUID(
			"e034ddb5c84011e39cc41cc1dec00ed3").getFirst();
    public static ListenerRule loadStoreRule = null;
    public static Map<String,ListenerRule> rules = new HashMap<String, ListenerRule>();
    private static ListenerRule assignRule = null;
    private static ListenerRule allocRule = null;

    public static void addRules(PDStore store) {
        setRuleRoleNames(store);
        addRuleAlloc(store);
        addRuleAssign(store);
        addRuleLoadStore(store);
    }

    public static void addRuleLoadStore(PDStore store) {
		// rule for store/load pairs
		Variable hTarget = new Variable("hTarget");
		Variable field = new Variable("field");
		Variable store_op = new Variable("store_op");
		Variable load_op = new Variable("load_op");
		Variable hTmp = new Variable("hTmp");
		Variable vStoreTmp = new Variable("vStoreTmp");
		Variable vStoreTarget = new Variable("vStoreTarget");
		Variable vLoadTmp = new Variable("vLoadTmp");
		Variable vLoadTarget = new Variable("vLoadTarget");


		List<PDChange<GUID, Object, GUID>> condition21 = new ArrayList<PDChange<GUID, Object, GUID>>();

        // Note: the where tuples need to be added in the reverse order in which they are evaluated at the moment
        // TODO Query should evaluate where tuples in the order in which they are given (if no reordering optimization is used)

        // vP(v2, h2):
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, vStoreTarget, POINTS_TO, hTarget));

        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, store_op, STORE_RIGHT, vStoreTarget));
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, store_op, STORE_FIELD, field));
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, store_op, STORE_LEFT, vStoreTmp));

        // vP(v1, h1):
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, vStoreTmp, POINTS_TO, hTmp));

        // vP(v4, h1):
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, vLoadTmp, POINTS_TO, hTmp));

        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, load_op, LOAD_LEFT, vLoadTarget));
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, load_op, LOAD_FIELD, field));
		condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_RIGHT, vLoadTmp));

//		if(loadStoreRule!=null)
//			loadStoreRule.deleteFromDispatcher();
		loadStoreRule = new ListenerRule("storeLoad", condition21,
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null,
						vLoadTarget, POINTS_TO, hTarget), store);
		loadStoreRule.addTemplates(store.getListenerDispatcher());
		rules.put("storeLoad", loadStoreRule);
	}

    public static void addNewLoadStore(PDStore store) {
		// rule for store/load pairs
		Variable hTarget = new Variable("hTarget");
		Variable field = new Variable("field");
		Variable store_op = new Variable("store_op");
		Variable load_op = new Variable("load_op");
		Variable hTmp = new Variable("hTmp");
		Variable vStoreTmp = new Variable("vStoreTmp");
		Variable vStoreTarget = new Variable("vStoreTarget");
		Variable vLoadTmp = new Variable("vLoadTmp");
		Variable vLoadTarget = new Variable("vLoadTarget");

		// Adding rule optimized for trigger (load_op, LOAD_FIELD, field):

        // Note: the where tuples need to be added in the reverse order in which they are evaluated at the moment
		List<PDChange<GUID, Object, GUID>> condition21 = new ArrayList<PDChange<GUID, Object, GUID>>();

        // vP(v2, h2): is deleted!

        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, store_op, STORE_RIGHT, vStoreTarget));
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, store_op, STORE_FIELD, field));
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, store_op, STORE_LEFT, vStoreTmp));

         // vP(v1, h1):
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, vStoreTmp, POINTS_TO, hTmp));

        // vP(v4, h1):
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, vLoadTmp, POINTS_TO, hTmp));

        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, load_op, LOAD_FIELD, field));
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, load_op, LOAD_RIGHT, vLoadTmp));
        condition21.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_LEFT, vLoadTarget));


//		if(loadStoreRule!=null)
//			loadStoreRule.deleteFromDispatcher();
		loadStoreRule = new ListenerRule("storeLoad", condition21,
				new ArrayList<PDChange<GUID, Object, GUID>>(Arrays.asList(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                        null, load_op, LOAD_FIELD, field))),
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null,
						vLoadTarget, ASSIGN, vStoreTarget), store);
		loadStoreRule.addTemplates(store.getListenerDispatcher());
		rules.put("storeLoad1", loadStoreRule);

		// Adding rule optimized for trigger ( vLoadTmp, POINTS_TO, hTmp):

		// Note: the where tuples need to be added in the reverse order in which they are evaluated at the moment
		List<PDChange<GUID, Object, GUID>> condition211 = new ArrayList<PDChange<GUID, Object, GUID>>();


		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, store_op, STORE_RIGHT, vStoreTarget));

		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, store_op, STORE_FIELD, field));

		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, store_op, STORE_LEFT, vStoreTmp));

		// vP(v1, h1):
		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, vStoreTmp, POINTS_TO, hTmp));

		// vP(v4, h1):
		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_LEFT, vLoadTarget));
		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_FIELD, field));
		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_RIGHT, vLoadTmp));
		condition211.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, vLoadTmp, POINTS_TO, hTmp));

		//				if(loadStoreRule!=null)
		//					loadStoreRule.deleteFromDispatcher();
		loadStoreRule = new ListenerRule("storeLoad2", condition211,
				new ArrayList<PDChange<GUID, Object, GUID>>(Arrays.asList(
						new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null, vLoadTmp, POINTS_TO, hTmp))),
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null, vLoadTarget, ASSIGN, vStoreTarget), store);
		//loadStoreRule.addTemplates(store.getListenerDispatcher());
		rules.put("storeLoad2", loadStoreRule);

		// Adding rule optimized for trigger ( vStoreTmp, POINTS_TO, hTmp):

		// Note: the where tuples need to be added in the reverse order in which they are evaluated at the moment
		List<PDChange<GUID, Object, GUID>> condition2111 = new ArrayList<PDChange<GUID, Object, GUID>>();


		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_LEFT, vLoadTarget));

		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_FIELD, field));

		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, load_op, LOAD_RIGHT, vLoadTmp));
		// vP(v4, h1):
		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, vLoadTmp, POINTS_TO, hTmp));


		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, store_op, STORE_RIGHT, vStoreTarget));
		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, store_op, STORE_FIELD, field));
		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, store_op, STORE_LEFT, vStoreTmp));

		// vP(v1, h1):
		condition2111.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, vStoreTmp, POINTS_TO, hTmp));


		//				if(loadStoreRule!=null)
		//					loadStoreRule.deleteFromDispatcher();
		loadStoreRule = new ListenerRule("storeLoad2", condition2111,
				new ArrayList<PDChange<GUID, Object, GUID>>(Arrays.asList(
						new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null, vStoreTmp, POINTS_TO, hTmp))),
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null, vLoadTarget, ASSIGN, vStoreTarget), store);
		//loadStoreRule.addTemplates(store.getListenerDispatcher());
		rules.put("storeLoad3", loadStoreRule);

	}

    public static void addRuleAssign(PDStore store) {
		// rule for "assign"
		Variable vleft = new Variable("vleft");
		Variable vright = new Variable("vright");
		Variable h1 = new Variable("h");

        // Note: the where tuples need to be added in the reverse order in which they are evaluated at the moment
		List<PDChange<GUID, Object, GUID>> condition2 = new ArrayList<PDChange<GUID, Object, GUID>>();
		condition2.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, vright, POINTS_TO, h1));
        condition2.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
                null, vleft, ASSIGN, vright));

		assignRule = new ListenerRule("assign", condition2,
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null,
						vleft, POINTS_TO, h1), store);
		assignRule.addTemplates(store.getListenerDispatcher());
		rules.put("assign", assignRule);
	}

    public static void addRuleAlloc(PDStore store) {

		// rule for "allocate"
		Variable v = new Variable("v");
		Variable h = new Variable("h");

		List<PDChange<GUID, Object, GUID>> condition = new ArrayList<PDChange<GUID, Object, GUID>>();
		// TODO: Next should be either EFFECTIVELY_ADDED
		condition.add(new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED,
				null, v, ALLOC, h));
		allocRule = new ListenerRule("alloc", condition,
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_ADDED, null,
						v, POINTS_TO, h), store);
		allocRule.addTemplates(store.getListenerDispatcher());
		rules.put("alloc", allocRule);
	}

    public static void setRuleRoleNames(PDStore store) {
		// role names for rule parts
		store.setName(LOAD_FIELD, "loadField");
		store.setName(LOAD_LEFT, "loadLeft");
		store.setName(LOAD_RIGHT, "loadRight");

		store.setName(STORE_FIELD, "storeField");
		store.setName(STORE_LEFT, "storeLeft");
		store.setName(STORE_RIGHT, "storeRight");

		store.setName(ASSIGN, "assign");
		store.setName(ALLOC, "alloc");
		store.setName(POINTS_TO, "vP");
		store.commit();

		// reducing need to call RoleListeners, see ListenerRule.GOES_WITH_ROLE javadoc
		store.addLink(STORE_LEFT, ListenerRule.GOES_WITH_ROLE, STORE_FIELD);
		store.addLink(STORE_RIGHT, ListenerRule.GOES_WITH_ROLE, STORE_FIELD);

		store.addLink(LOAD_LEFT, ListenerRule.GOES_WITH_ROLE, LOAD_FIELD);
		store.addLink(LOAD_RIGHT, ListenerRule.GOES_WITH_ROLE, LOAD_FIELD);

		store.commit();
	}

    public static void printRuleCount(String role2name) {
        System.err.println();
        ListenerRule listenerRule = (ListenerRule)(rules.get(role2name));
        if(listenerRule==null) return;
        System.err.println("rule: "+listenerRule.name);
        InsertionPseudoFilter<GUID, Object, GUID> filterParameter = listenerRule.singleConsequence;
        System.err.println("vP so far:"+ filterParameter.countInserts);
    }
}
