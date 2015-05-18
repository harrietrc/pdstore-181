package pdstore.ui.graphview;

import pdstore.*;
import pdstore.dal.*;
import pdstore.ui.graphview.ops.AddRelation;

public class CreateModel {
	PDStore store = new PDStore("GraphView");

	private void createModel() {
		GUID transaction = store.begin();

		// Create model for PD data as a visual graph
		store.createModel(transaction, GraphView.GRAPHVIEW_MODELID,
				"Graph View");

		store.createType(transaction, GraphView.GRAPHVIEW_MODELID,
				GraphView.GRAPH_TYPEID, "Graph");
		store.createType(transaction, GraphView.GRAPHVIEW_MODELID,
				GraphView.NODE_TYPEID, "Node");

		// Roles for type Graph
		store.createRelation(transaction, GraphView.GRAPH_TYPEID, "Graph",
				"Node", GraphView.GRAPH_NODE_ROLEID, GraphView.NODE_TYPEID);
		store.createRelation(transaction, GraphView.GRAPH_TYPEID, "graph",
				"Java package", GraphView.GRAPH_PACKAGE_ROLEID, PDStore.STRING_TYPEID);

		// Roles for type Node
		store.createRelation(transaction, GraphView.NODE_TYPEID, null,
				"shown instance", GraphView.NODE_INSTANCE_ROLEID, PDStore.ANY_TYPEID);
		store.createRelation(transaction, GraphView.NODE_TYPEID, null, "x",
				GraphView.NODE_X_ROLEID, PDStore.DOUBLE_PRECISION_TYPEID);
		store.createRelation(transaction, GraphView.NODE_TYPEID, null, "y",
				GraphView.NODE_Y_ROLEID, PDStore.DOUBLE_PRECISION_TYPEID);
			
		store.commit(transaction);
	}

	public void createDALClasses() {
		PDGen.generateModel(store, "Graph View", "src",
				"pdstore.ui.graphview.dal");
		store.commit();
	}

	public static void main(String[] args) {
		CreateModel m = new CreateModel();
		m.createModel();
		m.createDALClasses();
	}
}
