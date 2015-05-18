package pdstore.ui.graphview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import nz.ac.auckland.se.genoupe.tools.Debug;
import pdstore.ChangeType;
import pdstore.GUID;
import pdstore.IsolationLevel;
import pdstore.PDChange;
import pdstore.PDStore;
import pdstore.PersistenceLevel;
import pdstore.dal.PDInstance;
import pdstore.sparql.Assignment;
import pdstore.sparql.Query;
import pdstore.sparql.Variable;
import pdstore.ui.graphview.dal.PDGraph;
import pdstore.ui.graphview.dal.PDNode;

public class GraphView extends JPanel implements MouseListener,
		MouseMotionListener {

	static {
		Debug.addDebugTopic("GraphView");
	}

	static {
		PDGraph.register();
		PDNode.register();
	}

	/**
	 * auto-generated class id for java serialization
	 */
	private static final long serialVersionUID = 4204606997608235219L;

	// GUIDs for the used model, types and their roles
	public final static GUID GRAPHVIEW_MODELID = new GUID(
			"e44bd0301d9211e1a04700235411d565");
	public final static GUID GRAPH_TYPEID = new GUID(
			"e44c1e501d9211e1a04700235411d565");
	public final static GUID NODE_TYPEID = new GUID(
			"e44c1e511d9211e1a04700235411d565");
	public final static GUID GRAPH_NODE_ROLEID = new GUID(
			"e44c1e521d9211e1a04700235411d565");
	public final static GUID NODE_INSTANCE_ROLEID = new GUID(
			"e44c1e531d9211e1a04700235411d565");
	public final static GUID NODE_X_ROLEID = new GUID(
			"e44c1e541d9211e1a04700235411d565");
	public final static GUID NODE_Y_ROLEID = new GUID(
			"e44c1e551d9211e1a04700235411d565");
	public final static GUID GRAPH_PACKAGE_ROLEID = new GUID(
			"e44c1e561d9211e1a04700235411d565");
	public final static GUID DUMMYe44c1e571d9211e1a04700235411d565 = new GUID(
			"e44c1e571d9211e1a04700235411d565");

	/**
	 * The PDStore we are using
	 */
	PDStore store;

	/**
	 * The graph that is being viewed/edited
	 */
	PDGraph graph;

	Random random = new Random();

	public PDGraph getGraph() {
		return graph;
	}

	public void setGraph(PDGraph graph) {
		this.graph = graph;
		this.store = graph.getStore();
		repaint();
	}

	/**
	 * The node that is currently selected, or null if no selection.
	 */
	List<PDNode> selectedNodes = new ArrayList<PDNode>();

	/**
	 * The edge that is currently selected, or null if no selection. The edge is
	 * represented as a PDChange since the edges are only visualizing links.
	 */
	PDChange<GUID, Object, GUID> selectedEdge;

	/**
	 * Number of pixels that a user can click away from the middle of an edge
	 * (on both sides) and still select it.
	 */
	static double EDGE_SELECTION_TOLERANCE = 8;

	/**
	 * During a drag operation, this contains the starting point of the drag.
	 */
	Point startDragPoint;

	/**
	 * Map from instance Objects to their node representations. Used in paint().
	 * This is also used to make sure edges are only drawn once, because a node
	 * is put into nodes after its edges have been drawn, and the edges of a
	 * node are only drawn if the target node is not already in nodes (i.e.
	 * edges are only drawn from one node but not the other).
	 */
	Map<Object, PDNode> nodes = new Hashtable<Object, PDNode>();

	/**
	 * Edges drawn in paint() are registered here. It is used in
	 * edgeNearLocation() to iterate through the current edges and determine if
	 * an edge was selected. It could be extended later on to find extra
	 * visualization info about edges (i.e. using some value type other than
	 * Boolean).
	 */
	Map<PDChange<GUID, Object, GUID>, Boolean> edges = new Hashtable<PDChange<GUID, Object, GUID>, Boolean>();

	/**
	 * The last mouse position where a mouse key was pressed, so that it can be
	 * used to display a context menu there.
	 */
	Point mousePosition;

	final GraphView graphView = this;

	/**
	 * The context menus (or list of default menu items for dynamic menus) for
	 * different right-click locations.
	 */
	List<JMenuItem> nodeMenuItems = new ArrayList<JMenuItem>();
	List<JMenuItem> edgeMenuItems = new ArrayList<JMenuItem>();
	JPopupMenu canvasMenu = new JPopupMenu();

	public GraphView(PDStore store, Object... instances) {
		this.store = store;

		/*
		 * Internally the view works only with GUIDs. So if a root instance is
		 * given as DAL object (subclass of PDInstance), then use its GUID
		 * instead.
		 */
		for (int i = 0; i < instances.length; i++) {
			if (instances[i] instanceof PDInstance)
				instances[i] = ((PDInstance) instances[i]).getId();
		}

		// create a new graph and nodes for the given instances
		graph = new PDGraph(store);
		for (Object instance : instances) {
			PDNode node = new PDNode(store);
			// TODO The instance should be of type Object. Also primitive
			// instances should be possible.
			node.addShownInstance((GUID) instance);
			node.addX(new Double(random.nextInt(200)));
			node.addY(new Double(random.nextInt(200)));
			graph.addNode(node);
		}
		store.commit();

		initUI();
	}

	public GraphView(final PDGraph graph) {
		initUI();
		setGraph(graph);
	}

	void initUI() {
		JMenuItem addNewInstanceMenuItem = new JMenuItem("Add new instance...");
		addNewInstanceMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// query all the type names from the DB
				// TODO this should be less verbose
				Variable typeVar = new Variable("type");
				Variable typeNameVar = new Variable("type name");

				List<Variable> select = new ArrayList<Variable>();
				select.add(typeNameVar);

				List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, typeVar,
						PDStore.HAS_TYPE_ROLEID, PDStore.TYPE_TYPEID));
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, typeVar,
						PDStore.NAME_ROLEID, typeNameVar));

				Query query = new Query(select, where, null, null, store);
				Iterator<Assignment<GUID, Object, GUID>> resultSet = query
						.execute(store.getCurrentTransaction());

				List<Object> typeNames = new ArrayList<Object>();
				while (resultSet.hasNext()) {
					Assignment<GUID, Object, GUID> result = resultSet.next();
					typeNames.add(result.get(typeNameVar));
				}

				Object[] choices = typeNames.toArray();
				String typeName = (String) JOptionPane.showInputDialog(null,
						"Please select a type for the new instance:",
						"Add New Instance", JOptionPane.PLAIN_MESSAGE, null,
						choices, null);

				if ((typeName != null) && (typeName.length() > 0)) {
					// create new instance of the given type
					GUID newInstanceID = new GUID();
					GUID typeID = store.getId(typeName);
					store.setType(newInstanceID, typeID);

					// add new node
					PDNode newNode = new PDNode(store);
					newNode.addShownInstance(newInstanceID);
					newNode.addX(mousePosition.getX());
					newNode.addY(mousePosition.getY());
					graph.addNode(newNode);
					store.commit();

					graphView.repaint();
				}
			}
		});
		canvasMenu.add(addNewInstanceMenuItem);

		JMenuItem addExistingInstanceMenu = new JMenuItem(
				"Add existing instance...");
		addExistingInstanceMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// query all the instance names from the DB
				// TODO this should be less verbose
				Variable instanceVar = new Variable("instance");
				Variable instanceNameVar = new Variable("instance name");
				Variable instanceTypeVar = new Variable("instance type");
				Variable instanceTypeNameVar = new Variable(
						"instance type name");

				List<Variable> select = new ArrayList<Variable>();
				select.add(instanceNameVar);

				List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, instanceVar,
						PDStore.NAME_ROLEID, instanceNameVar));
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, instanceVar,
						PDStore.HAS_TYPE_ROLEID, instanceTypeVar));
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, instanceTypeVar,
						PDStore.NAME_ROLEID, instanceTypeNameVar));

				Query query = new Query(select, where, null, null, store);
				Iterator<Assignment<GUID, Object, GUID>> resultSet = query
						.execute(store.getCurrentTransaction());

				List<String> instanceNames = new ArrayList<String>();
				while (resultSet.hasNext()) {
					Assignment<GUID, Object, GUID> result = resultSet.next();
					String name = (String) result.get(instanceNameVar) + " : "
							+ (String) result.get(instanceTypeNameVar);
					instanceNames.add(name);
				}

				// TODO there must be a better way to convert a list to a typed
				// array
				String[] choices = new String[instanceNames.size()];
				int i = 0;
				for (String name : instanceNames) {
					choices[i] = name;
					i++;
				}

				java.util.Arrays.sort(choices, String.CASE_INSENSITIVE_ORDER);

				String instanceName = (String) JOptionPane.showInputDialog(
						null, "Please select the instance to add:",
						"Add Existing Instance", JOptionPane.PLAIN_MESSAGE,
						null, choices, null);

				if ((instanceName != null) && (instanceName.length() > 0)) {
					// remove type name from string
					instanceName = instanceName.substring(0,
							instanceName.indexOf(" : "));

					// look up ID of remaining instance name
					GUID instanceID = store.getId(instanceName);

					// add new node
					PDNode newNode = new PDNode(store);
					newNode.addShownInstance(instanceID);
					newNode.addX(mousePosition.getX());
					newNode.addY(mousePosition.getY());
					graph.addNode(newNode);
					store.commit();

					graphView.repaint();
				}
			}
		});
		canvasMenu.add(addExistingInstanceMenu);

		JMenuItem addLinkMenuItem = new JMenuItem("Add link...");
		addLinkMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// we need two selected nodes to create a link between the
				// corresponding instances
				if (selectedNodes.size() < 2)
					return;

				// get the two selected nodes
				Iterator<PDNode> iterator = selectedNodes.iterator();
				PDNode node1 = iterator.next();
				PDNode node2 = iterator.next();

				// get their instances and types
				Object instance1 = node1.getShownInstance();
				GUID type1 = store.getType(instance1);

				Object instance2 = node2.getShownInstance();
				GUID type2 = store.getType(instance2);

				// find the roles that might connect them
				// TODO this should be less verbose
				Variable role1Var = new Variable("role1");
				Variable role2Var = new Variable("role2");
				Variable role2NameVar = new Variable("role2 name");

				List<Variable> select = new ArrayList<Variable>();
				select.add(role2NameVar);

				List<PDChange<GUID, Object, GUID>> where = new ArrayList<PDChange<GUID, Object, GUID>>();
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, type1,
						PDStore.OWNED_ROLE_ROLEID, role1Var));
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, role1Var,
						PDStore.PARTNER_ROLEID, role2Var));
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, type2,
						PDStore.OWNED_ROLE_ROLEID, role2Var));
				where.add(new PDChange<GUID, Object, GUID>(
						ChangeType.LINK_EFFECTIVE, null, role2Var,
						PDStore.NAME_ROLEID, role2NameVar));

				Query query = new Query(select, where, null, null, store);
				Iterator<Assignment<GUID, Object, GUID>> resultSet = query
						.execute(store.getCurrentTransaction());

				List<Object> roleNames = new ArrayList<Object>();
				while (resultSet.hasNext()) {
					Assignment<GUID, Object, GUID> result = resultSet.next();
					roleNames.add(result.get(role2NameVar));
				}

				Object[] choices = roleNames.toArray();
				String roleName = (String) JOptionPane.showInputDialog(
						null,
						"Please select a role to connect the instances "
								+ store.getLabel(instance1) + " and "
								+ store.getLabel(instance2) + ":", "Add Link",
						JOptionPane.PLAIN_MESSAGE, null, choices, null);

				if ((roleName != null) && (roleName.length() > 0)) {
					// add the link
					GUID roleID = store.getId(roleName);
					store.addLink(instance1, roleID, instance2);
					store.commit();

					graphView.repaint();
				}

			}
		});
		nodeMenuItems.add(addLinkMenuItem);

		JMenuItem removeNodeMenuItem = new JMenuItem("Remove node(s)...");
		removeNodeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nodeLabels = "";
				for (PDNode node : selectedNodes) {
					nodeLabels += store.getLabel(node.getShownInstance())
							+ "\n";
				}

				int answer = JOptionPane.showConfirmDialog(null,
						"Would you like to remove the nodes for the following instances?"
								+ nodeLabels, "Remove Node(s)",
						JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					graphView.graph.removeNodes(selectedNodes);
					store.commit();
					selectedNodes.clear();
					graphView.repaint();
				}
			}
		});
		nodeMenuItems.add(removeNodeMenuItem);

		JMenuItem renameInstanceMenuItem = new JMenuItem("Rename instance...");
		renameInstanceMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// choose only one of the selected instances -- no power
				// renaming yet
				Object instance = ((PDNode) selectedNodes.toArray()[0])
						.getShownInstance();
				String instanceName = store.getName(instance);

				JTextField textField = new JTextField(40);
				if (instanceName != null) {
					textField.setText(instanceName);
					textField.setSelectionStart(0);
					textField.setSelectionEnd(instanceName.length());
				}

				String newName = JOptionPane.showInputDialog(null,
						"New name for " + store.getLabel(instance) + "\":",
						instanceName);

				if (!newName.equals(instanceName)) {
					store.setName(instance, newName);
					store.commit();
					graphView.repaint();
				}
			}
		});
		nodeMenuItems.add(renameInstanceMenuItem);

		JMenuItem removeLinkMenuItem = new JMenuItem("Remove link...");
		removeLinkMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(
						null,
						"Would you like to remove the link \n"
								+ store.toString(selectedEdge) + " ?",
						"Remove Node", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					store.removeLink(selectedEdge.getInstance1(),
							selectedEdge.getRole2(),
							selectedEdge.getInstance2());
					store.commit();
					graphView.repaint();
				}
			}
		});
		edgeMenuItems.add(removeLinkMenuItem);

		// install the mouse listeners to handle the mouse operations such as
		// dragging
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * This method paints the GraphView. It is called through repaint() whenever
	 * something changes and the GUI needs repainting.
	 */
	public void paint(Graphics g) {
		Debug.println("Entering paint().", "paint");

		super.paint(g);
		Graphics2D graphics = (Graphics2D) g;

		// This starts a new transaction, ensuring that we get up-to-date
		// information.
		store.rollback();

		// no nodes or edges have been drawn yet in this call to paint, so clear
		// nodes and edges maps.
		nodes.clear();
		edges.clear();

		/*
		 * Get the changes between the instances of the nodes - they will be
		 * drawn as edges. For each such change, draw the link between the node.
		 * Drawing the edges before drawing the nodes on top of them makes sure
		 * the edges connect precisely to the nodes (painter's algorithm).
		 */
		for (PDNode node1 : graph.getNodes()) {

			// if the node does not have an instance, remove it
			Object instance1 = node1.getShownInstance();
			if (instance1 == null) {
				graph.removeNode(node1);
				break;
			}

			// make sure the node has coordinates
			if (node1.getX() == null)
				node1.addX(new Double(random.nextInt(200)));
			if (node1.getY() == null)
				node1.addY(new Double(random.nextInt(200)));

			Point location1 = new Point(node1.getX().intValue(), node1.getY()
					.intValue());
			GUID type1 = store.getType(node1.getShownInstance());

			// add the node to the map so it is found when drawing edges
			nodes.put(instance1, node1);

			Debug.println("Searching edges for " + store.getLabel(instance1),
					"edges");

			// enable anti-aliasing
			RenderingHints rh = new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHints(rh);

			// set drawing parameters
			graphics.setStroke(new BasicStroke(2.5f));

			// go through the accessible roles of the node...
			for (GUID role2 : store.getAccessibleRoles(type1)) {

				Debug.println("  Traversing " + store.getLabel(role2), "edges");

				// go through the changes from instance1 using the role...
				for (Object instance2 : store.getInstances(instance1, role2)) {

					Debug.println(
							"    Testing edge to " + store.getLabel(instance2),
							"edges");

					// find appropriate nodes to connect, or continue loop if
					// there are no nodes
					PDNode node2;
					if (nodes.containsKey(instance2)) {
						// look up the node
						node2 = nodes.get(instance2);

						// register the edge
						PDChange<GUID, Object, GUID> edge = new PDChange<GUID, Object, GUID>(
								null, null, instance1, role2, instance2);
						edges.put(edge, true);

						// make sure the node has coordinates
						if (node2.getX() == null)
							node2.addX(new Double(random.nextInt(200)));
						if (node2.getY() == null)
							node2.addY(new Double(random.nextInt(200)));

						Point location2 = new Point(node2.getX().intValue(),
								(int) node2.getY().intValue());

						// draw the edge
						graphics.drawLine(location1.x, location1.y,
								location2.x, location2.y);

						// draw labels for roles
						GUID role1 = role2.getPartner();
						String label1 = store.getName(role1);
						if (label1 != null) {
							double x = location1.x + 0.25
									* (location2.x - location1.x);
							double y = location1.y + 0.25
									* (location2.y - location1.y);
							FontMetrics metrics = graphics.getFontMetrics();
							int height = metrics.getHeight();
							int width = metrics.stringWidth(label1);
							graphics.drawString(label1, (int) (x - width / 2),
									(int) (y - 0.3 * height));
						}

						String label2 = store.getName(role2);
						if (label2 != null) {
							double x = location1.x + 0.75
									* (location2.x - location1.x);
							double y = location1.y + 0.75
									* (location2.y - location1.y);
							FontMetrics metrics = graphics.getFontMetrics();
							int height = metrics.getHeight();
							int width = metrics.stringWidth(label2);
							graphics.drawString(label2, (int) (x - width / 2),
									(int) (y - 0.3 * height));
						}

					} else if (type1.equals(PDStore.TYPE_TYPEID)) {
						// ensure there should be an edge
						GUID type2 = store.getType(instance2);
						if (!type2.equals(PDStore.ROLE_TYPEID))
							continue;

						GUID partnerRole = ((GUID) instance2).getPartner();
						GUID ownerType = store.getOwnerType(partnerRole);
						if (!nodes.containsKey(ownerType))
							continue;

						// look up the node
						node2 = nodes.get(ownerType);

						// register the edge
						PDChange<GUID, Object, GUID> edge = new PDChange<GUID, Object, GUID>(
								null, null, instance1, partnerRole, ownerType);
						edges.put(edge, true);

						// make sure the node has coordinates
						if (node2.getX() == null)
							node2.addX(new Double(random.nextInt(200)));
						if (node2.getY() == null)
							node2.addY(new Double(random.nextInt(200)));

						Point location2 = new Point(node2.getX().intValue(),
								(int) node2.getY().intValue());

						// draw the edge
						graphics.drawLine(location1.x, location1.y,
								location2.x, location2.y);

						// draw labels for roles
						String label1 = store.getName(instance2);
						if (label1 != null) {
							double x = location1.x + 0.25
									* (location2.x - location1.x);
							double y = location1.y + 0.25
									* (location2.y - location1.y);
							FontMetrics metrics = graphics.getFontMetrics();
							int height = metrics.getHeight();
							int width = metrics.stringWidth(label1);
							graphics.drawString(label1, (int) (x - width / 2),
									(int) (y - 0.3 * height));
						}

						String label2 = store.getName(partnerRole);
						if (label2 != null) {
							double x = location1.x + 0.75
									* (location2.x - location1.x);
							double y = location1.y + 0.75
									* (location2.y - location1.y);
							FontMetrics metrics = graphics.getFontMetrics();
							int height = metrics.getHeight();
							int width = metrics.stringWidth(label2);
							graphics.drawString(label2, (int) (x - width / 2),
									(int) (y - 0.3 * height));
						}
					}
				}
			}
		}

		// now draw all the nodes on top of the edges
		for (PDNode node : graph.getNodes()) {

			Point location = new Point(node.getX().intValue(), (int) node
					.getY().intValue());

			// if the node is selected, draw highlight in the background first
			if (selectedNodes.contains(node)) {
				double radius = 12;
				graphics.setPaint(Color.BLUE);
				RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(
						location.x - 1.5 * radius, location.y - 1.5 * radius,
						3 * radius, 3 * radius, 10, 10);
				graphics.draw(roundedRectangle);
			}

			// draw the instance with its associated widget
			// for each widget a local graphics context is created that has its
			// origin at the center of the widget
			Graphics2D widgetGraphics = (Graphics2D) g.create();
			widgetGraphics.translate(location.getX(), location.getY());
			new pdstore.ui.PDStore(store).drawInstance(node.getShownInstance(),
					widgetGraphics);
		}

		// TODO this is just a workaround to avoid unnecessary writes (each
		// transaction registers itself at the moment); in the future this
		// should be replaced with a notion of read-only transactions
		store.rollback();

		Debug.println("Exiting paint().", "paint");
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * This method is called whenever a mouse button is pressed.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// see if there is a node or edge near the mouse and make it the
		// selected node/edge
		PDNode newSelectedNode = nodeAtLocation(e.getPoint());

		// If the Control key is not pressed (single selection) and this is not
		// a right-click, then clear the previous selection.
		// If the Control key is pressed (multiple selection) or this is a
		// right-click (function invocation), then the
		// previously selected nodes remain selected.
		if (!e.isControlDown()
				&& (e.getModifiers() & MouseEvent.BUTTON3_MASK) == 0)
			selectedNodes.clear();

		// add the new selected node to the list of selected nodes
		if (newSelectedNode != null
				&& !(selectedNodes.size() > 0 && selectedNodes.get(
						selectedNodes.size() - 1).equals(newSelectedNode)))
			selectedNodes.add(newSelectedNode);

		// see if there is an edge near the mouse and make it the
		// selected edge
		selectedEdge = edgeNearLocation(e.getPoint());

		Debug.println("Newly selected node " + newSelectedNode, "selection");
		Debug.println("Selected edge " + selectedEdge, "selection");

		// in case we are dragging, set the press position as start point for
		// the next drag event
		startDragPoint = e.getPoint();

		repaint();
	}

	/**
	 * Returns a node that is at the given location, or null.
	 * 
	 * @param location
	 * @return a node at the given location
	 */
	PDNode nodeAtLocation(Point location) {
		// TODO get nearest node, not just first one
		for (PDNode node : graph.getNodes()) {
			if (location.x >= node.getX() - 15
					&& location.y >= node.getY() - 15
					&& location.x <= node.getX() + 15
					&& location.y <= node.getY() + 15) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Identifies an edge that is at the given location, or null.
	 * 
	 * @param location
	 * @return a PDChange that is represented by the edge at the given location
	 */
	PDChange<GUID, Object, GUID> edgeNearLocation(Point location) {
		// find the edge closest to the given location
		PDChange<GUID, Object, GUID> closestEdge = null;
		double closestDistance = Double.MAX_VALUE;

		for (PDChange<GUID, Object, GUID> edge : edges.keySet()) {
			PDNode n1 = nodes.get(edge.getInstance1());
			PDNode n2 = nodes.get(edge.getInstance2());
			Point2D.Double p1 = new Point2D.Double(n1.getX(), n1.getY());
			Point2D.Double p2 = new Point2D.Double(n2.getX(), n2.getY());

			double distance = distanceToLineSegment(p1, p2, location);

			Debug.println("Edge " + edge + " has distance " + distance,
					"selection");

			if (distance < closestDistance) {
				closestEdge = edge;
				closestDistance = distance;
			}
		}

		if (closestDistance <= EDGE_SELECTION_TOLERANCE)
			return closestEdge;

		return null;
	}

	private double distanceToLineSegment(Point2D p1, Point2D p2, Point2D p3) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();

		// if the line segment is just a point, return the distance between the
		// points
		if (dx == 0 && dy == 0)
			return p1.distance(p3);

		double u = ((p3.getX() - p1.getX()) * dx + (p3.getY() - p1.getY()) * dy)
				/ (dx * dx + dy * dy);

		Point2D closestPoint;
		if (u < 0)
			closestPoint = p1;
		else if (u > 1)
			closestPoint = p2;
		else
			closestPoint = new Point2D.Double(p1.getX() + u * dx, p1.getY() + u
					* dy);

		return closestPoint.distance(p3);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// If no node is being dragged, then there is nothing to do.
		if (selectedNodes.size() == 0)
			return;

		// calculate dragging vector for this dragging event
		double dx = e.getX() - startDragPoint.x;
		double dy = e.getY() - startDragPoint.y;

		// set the starting point for the next dragging event
		startDragPoint.setLocation(e.getPoint());

		GUID transactionID = store.begin(IsolationLevel.SNAPSHOT,
				PersistenceLevel.INDEX_ONLY);
		store.setTransactionId(transactionID);

		// move all the selected nodes by the dragging vector
		for (PDNode node : selectedNodes) {
			node.setX(node.getX() + dx);
			node.setY(node.getY() + dy);
		}
		store.commit();

		// TODO check whether the many repaint()'s are actually necessary or if
		// a listener does that already
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePosition = e.getPoint();

		/*
		 * If it was NOT the right button that was released (since
		 * isPopupTrigger() doesn't work properly we also check the right button
		 * explicitly), then just update selected node positions and return.
		 */
		if (!e.isPopupTrigger() && e.getButton() != MouseEvent.BUTTON3) {
			// make sure there are actually selected nodes
			if (selectedNodes.isEmpty())
				return;

			/*
			 * The selected nodes were dragged, so update their position
			 * persistently here (dragging does so only in the index, not
			 * persistently).
			 */
			for (PDNode node : selectedNodes) {
				/*
				 * Need to remove & add here to make sure the changes are
				 * written because set wouldn't change anything if the values
				 * stay the same. TODO maybe make set aware of the persistence
				 * level, so that it can be effective if it recognizes that the
				 * link has not been set adequately?
				 */
				double x = node.getX();
				double y = node.getY();
				node.removeX(x);
				node.removeY(y);
				node.addX(x);
				node.addY(y);
			}
			store.commit();
			return;
		}

		/*
		 * From here on we can be sure that a context menu has been activated.
		 * Depending on where was clicked, a different menu is shown.
		 */
		if (selectedNodes.size() > 0) {
			generateNodeMenu().show(e.getComponent(), e.getX(), e.getY());
		} else if (selectedEdge != null)
			generateEdgeMenu().show(e.getComponent(), e.getX(), e.getY());
		else
			canvasMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	JPopupMenu generateNodeMenu() {
		JPopupMenu menu = new JPopupMenu();

		// add the default items
		for (JMenuItem defaultItem : nodeMenuItems)
			menu.add(defaultItem);

		/*
		 * add those operations to the menu that can use the selected instance
		 * as superparameter
		 */
		final PDNode selectedNode = selectedNodes.get(selectedNodes.size() - 1);
		final Object shownInstance = selectedNode.getShownInstance();
		for (GUID opID : store.getApplicableOperations(shownInstance)) {
			final GUID operationID = opID;
			JMenuItem menuItem = new JMenuItem(store.getName(operationID));
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					store.applyOperation(operationID, shownInstance);
					store.commit();
				}
			});
			menu.add(menuItem);
		}

		// add context-specific extra functions
		final GUID typeID = store.getType(shownInstance);

		// binary functions
		if (selectedNodes.size() == 2) {
			final PDNode selectedNode2 = selectedNodes
					.get(selectedNodes.size() - 2);
			final Object shownInstance2 = selectedNode2.getShownInstance();
			final GUID typeID2 = store.getType(shownInstance2);

			// function for adding a new relation between two selected type
			// nodes
			if (typeID.equals(PDStore.TYPE_TYPEID)
					&& typeID2.equals(PDStore.TYPE_TYPEID)) {

				JMenuItem menuItem = new JMenuItem("Add relation");
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						GUID role1Id = new GUID();
						GUID role2Id = role1Id.getPartner();
						store.createRelation((GUID) shownInstance, null, null,
								role2Id, (GUID) shownInstance2);
						store.commit();

						// TODO should be done through PDStore listener
						repaint();
					}
				});
				menu.add(menuItem);
			}
		}

		return menu;
	}

	JPopupMenu generateEdgeMenu() {
		JPopupMenu menu = new JPopupMenu();

		// add the default items
		for (JMenuItem defaultItem : edgeMenuItems)
			menu.add(defaultItem);

		final PDChange<GUID, Object, GUID> edge = selectedEdge;
		GUID type1 = store.getType(edge.getInstance1());
		GUID type2 = store.getType(edge.getInstance2());

		// add context-specific extra functions
		if (type1.equals(PDStore.TYPE_TYPEID)
				&& type2.equals(PDStore.TYPE_TYPEID)) {

			final String type1Label = store.getLabel(edge.getInstance1());
			JMenuItem menuItem = new JMenuItem("Rename role of " + type1Label
					+ "...");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GUID role1 = edge.getRole2().getPartner();
					String oldName = store.getName(role1);
					String newName = JOptionPane.showInputDialog(null,
							"Name for role of " + type1Label + ":", oldName);

					if (newName == null || newName.equals(""))
						return;

					store.setName(role1, newName);
					store.commit();

					// TODO should be done through PDStore listener
					repaint();
				}
			});
			menu.add(menuItem);

			final String type2Label = store.getLabel(edge.getInstance2());
			menuItem = new JMenuItem("Rename role of " + type2Label + "...");
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GUID role2 = edge.getRole2();
					String oldName = store.getName(role2);
					String newName = JOptionPane.showInputDialog(null,
							"Name for role of " + type2Label + ":", oldName);

					if (newName == null || newName.equals(""))
						return;

					store.setName(role2, newName);
					store.commit();

					// TODO should be done through PDStore listener
					repaint();
				}
			});
			menu.add(menuItem);
		}

		return menu;
	}
}
