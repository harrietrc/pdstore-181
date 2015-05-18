package pdstore.ui.graphview;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.dal.PDGen;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListener;
import pdstore.notify.PDListenerAdapter;
import pdstore.ui.graphview.dal.PDGraph;
import pdstore.ui.graphview.dal.PDNode;
import pdstore.ui.widgets.EmptyCircle;
import pdstore.ui.widgets.FilledCircle;
import diagrameditor.StatusBar;

public class GraphEditor extends JFrame {

	GraphView graphView;

	/**
	 * Maps a type ID to the ID of the PD widget that is used to visualize the
	 * instances of the type.
	 */
	public Map<GUID, GUID> visualizedBy = new ConcurrentHashMap<GUID, GUID>();

	public GraphEditor() {
		this(new PDStore("GraphView"), null);
	}

	public GraphEditor(PDStore store, GUID graphID) {
		super();

		init(store);

		/*
		 * If no graph ID was given, then use the first one that can be found.
		 */
		if (graphID == null) {
			graphID = (GUID) store.getInstance(PDGraph.typeId,
					PDStore.HAS_TYPE_ROLEID.getPartner());
		}

		/*
		 * If a graph was found, load it. If no graph could be found at all,
		 * create a new one.
		 */
		PDGraph graph = null;
		if (graphID == null) {
			graph = new PDGraph(store);
			store.commit();
		} else {
			graph = PDGraph.load(store, graphID);
		}
		graphView = new GraphView(graph);

		setTitle("Graph Editor - " + graph.getLabel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setMinimumSize(new Dimension(100, 10));
		setLayout(new BorderLayout());

		// setting up the menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu graphMenu = new JMenu("Graph");
		menuBar.add(graphMenu);

		JMenuItem menuItem = new JMenuItem("Open from file...");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ask for file
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter(
						"PDStore database file", "pds");
				fileChooser.addChoosableFileFilter(filter);
				int returnVal = fileChooser.showOpenDialog(graphView);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;

				String fileName = fileChooser.getSelectedFile().getName();
				PDStore store = new PDStore(fileName);
				init(store);
				selectGraph(store);
			}
		});
		graphMenu.add(menuItem);

		menuItem = new JMenuItem("Open from network...");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldURL = "";
				String newURL = JOptionPane.showInputDialog(null,
						"Host URL of the remote PDStore server:", oldURL);

				if (newURL == null || newURL.equals(""))
					return;

				PDStore store = new PDStore(PDStore.connectToServer(newURL));
				init(store);
				selectGraph(store);
			}
		});
		graphMenu.add(menuItem);

		menuItem = new JMenuItem("Select...");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectGraph(graphView.graph.getStore());
			}
		});
		graphMenu.add(menuItem);

		menuItem = new JMenuItem("Rename...");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldName = graphView.getGraph().getName();
				String newName = JOptionPane.showInputDialog(null,
						"New name for \"" + graphView.getGraph().getLabel()
								+ "\":", oldName);

				if (newName.equals(oldName))
					return;

				graphView.getGraph().setName(newName);
				graphView.getGraph().getStore().commit();
				graphView.repaint();

				setTitle("Graph Editor - " + graphView.getGraph().getLabel());
			}
		});
		graphMenu.add(menuItem);

		menuItem = new JMenuItem("Generate Java classes...");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldName = graphView.getGraph().getJavaPackage();
				String newName = JOptionPane
						.showInputDialog(null,
								"Java package name for the generated classes:",
								oldName);

				if (newName == null || newName.equals(""))
					return;

				graphView.getGraph().setJavaPackage(newName);
				PDGen.generateForGraph(graphView.getGraph().getStore(),
						graphView.getGraph(), newName);
				graphView.getGraph().getStore().commit();
				graphView.repaint();

				setTitle("Graph Editor - " + graphView.getGraph().getLabel());
			}
		});
		graphMenu.add(menuItem);

		StatusBar statusBar = new StatusBar();

		// setting up the overall layout
		Container contentPane = this.getContentPane();
		contentPane.add(graphView, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	public void init(PDStore store) {

		// make sure PDNode is registered as a DAL class
		PDNode.register();

		// set up default view for all instances
		EmptyCircle.register(store);
		FilledCircle.register(store);
		store.commit();

		visualizedBy.put(PDStore.TYPE_TYPEID, FilledCircle.widgetID);

		store.getViewDispatcher().addListener(
				new PDListenerAdapter<GUID, Object, GUID>() {
					public void transactionCommitted(
							List<PDChange<GUID, Object, GUID>> transaction,
							List<PDChange<GUID, Object, GUID>> matchedChanges,
							PDCoreI<GUID, Object, GUID> core) {
						PDChange<GUID, Object, GUID> changeTemplate = matchedChanges
								.get(0);

						// this view is only for queries with role
						// VISUALIZED_BY_ROLEID
						if (!changeTemplate.getRole2().equals(
								PDStore.VISUALIZED_BY_ROLEID))
							return;

						// the instance to query the visualization for must be
						// given
						Object instance1 = changeTemplate.getInstance1();
						if (instance1 == null)
							return;

						// get instance type to make visualization type
						// dependent
						GUID type = graphView.graph.getStore().getType(
								instance1);
						GUID widget = visualizedBy.get(type);

						if (widget != null) {
							transaction.add(new PDChange<GUID, Object, GUID>(
									ChangeType.LINK_ADDED, changeTemplate
											.getTransaction(), instance1,
									PDStore.VISUALIZED_BY_ROLEID, widget));
						} else {
							// add a link for the default visualization
							transaction.add(new PDChange<GUID, Object, GUID>(
									ChangeType.LINK_ADDED, changeTemplate
											.getTransaction(), changeTemplate
											.getInstance1(),
									PDStore.VISUALIZED_BY_ROLEID,
									EmptyCircle.WIDGET_ID));
						}
					}
				},
				new PDChange<GUID, Object, GUID>(ChangeType.LINK_EFFECTIVE,
						null, null, PDStore.VISUALIZED_BY_ROLEID, null));

		// add repaint listener
		List<PDListener<GUID, Object, GUID>> listeners = store
				.getDetachedListenerList();
		listeners.add(new PDListenerAdapter<GUID, Object, GUID>() {
			public void transactionCommitted(
					List<PDChange<GUID, Object, GUID>> transaction,
					List<PDChange<GUID, Object, GUID>> matchedChanges,
					PDCoreI<GUID, Object, GUID> core) {
				repaint();
			}
		});
	}

	void selectGraph(PDStore store) {
		Collection<Object> graphs = store.getInstances(PDGraph.typeId,
				PDStore.HAS_TYPE_ROLEID.getPartner());

		ArrayList<Object> graphLabels = new ArrayList<Object>();
		ArrayList<GUID> graphInstances = new ArrayList<GUID>();
		if (graphs.isEmpty()) {
			PDGraph newGraph = new PDGraph(store);
			graphLabels.add("Create new graph...");
			graphInstances.add(newGraph.getId());
		} else {
			for (Object graphID : graphs) {
				graphLabels.add(store.getLabel(graphID));
				graphInstances.add((GUID) graphID);
			}
		}

		Object[] choices = graphLabels.toArray();
		String selectedLabel = (String) JOptionPane.showInputDialog(null,
				"Please select a graph to open:", "Open Graph",
				JOptionPane.PLAIN_MESSAGE, null, choices, null);

		if (selectedLabel == null || selectedLabel.length() < 0)
			return;

		int selection = graphLabels.indexOf(selectedLabel);
		PDGraph graph = PDGraph.load(store, graphInstances.get(selection));
		graphView.setGraph(graph);

		setTitle("Graph Editor - " + graph.getLabel());
		store.commit();
	}

	public static void main(String[] args) {
		GraphEditor graphEditor = new GraphEditor();
	}
}
