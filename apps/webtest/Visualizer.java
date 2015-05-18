package webtest;

import java.util.Collection;
import java.util.HashSet;
import pdstore.GUID;
import pdstore.PDStore;

public class Visualizer {
	PDStore store;
	StringBuffer out = new StringBuffer();
	HashSet<GUID> drawnIDs = new HashSet<GUID>();
	
	public Visualizer(PDStore store) {
		this.store = store;
	}

	public void visualizeAction(GUID action) {
		// make sure not to visualize the action twice
		if (drawnIDs.contains(action))
			return;
		drawnIDs.add(action);

		// visualize action
		String actionname = store.getName(action);
		out.append(actionname + "[shape=box];" + "\n");

		visualizeFields(action);
		for (Object page : store.getInstances(action, store.getId("page"))) {
			String pageName = store.getName(page);
			out.append(actionname + "-> " + pageName);
			printprobabilityoftransition(action, (GUID)page, "page");
			visualizePage((GUID) page);
		}
	}
	
public void visualizeFields(GUID action)
{	Collection<Object> fields = store.getInstances(action,
		store.getId("field"));

	if (fields.size() > 0) {
		/*out.append(store.getName(action) + "[" + "\n");
		out.append("label=<\n");
		out.append("<TABLE >\n <TR><TD ><b>" + store.getName(action) + "</b></TD></TR>\n");
		out.append("<TR><TD>\n<TABLE>");*/
		
		out.append(store.getName(action) + "[");
		out.append("label=\""+store.getName(action)+"\n");

		for (Object field : fields) {
			
			String fieldName = store.getName(field);
			String fieldType = (String) store.getName(store.getInstance(field,
					store.getId("fieldType")));
			
			out.append(fieldName + " : " + fieldType
					+ "\n");
		}

		out.append("\"]\n");
	}	
}	
	
	
	
	void visualizePage(GUID page) {
		// make sure not to visualize the page twice
		if (drawnIDs.contains(page))
			return;
		drawnIDs.add(page);

		// visualize action
		String pagename = store.getName(page);
		out.append(pagename + "[shape=ellipse];" + "\n");
		
		visualizeFields(page);
		
		for (Object action : store.getInstances(page, store.getId("action"))) {
			String actionName = store.getName(action);
			out.append(pagename + "-> " + actionName);
			printprobabilityoftransition(page,(GUID) action, "action");
			visualizeAction((GUID) action);
		}
	}

	public void printprobabilityoftransition(GUID page, GUID action, String type)
	{
		//for page get transition
		for (Object transition  : store.getInstances(page, store.getId("transition")) )
		{
			String transitionaction = store.getName(store.getInstance(transition, store.getId(type)));
			String actionname = store.getName(action);
			String transactionname = store.getName(transition);
			Object a = store.getInstance(transition, store.getId("prob"));
			
			if(transitionaction==actionname)
			{	out.append("[label="+a + "];\n");
			}
		}
		
		//if transition has action
		//print probability
	}
	
	public static void main(String args[]) {
		String storeName = "shoutwiki";
		String startActionName = "StartAction";

		PDStore store = new PDStore(storeName);
		Visualizer v = new Visualizer(store);
		GUID startAction = store.getId(startActionName);
		v.out.append("digraph{");
		v.visualizeAction(startAction);
		v.out.append("}");
		System.out.println(v.out);
	}
}