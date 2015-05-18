package diagrameditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import pdstore.GUID;
import pdstore.PDChange;
import pdstore.dal.PDInstance;
import pdstore.generic.GenericLinkedList;
import pdstore.ui.PDStore;
import diagrameditor.dal.PDDiagram;
import diagrameditor.dal.PDDiagramSet;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.ops.OpChangeColor;
import diagrameditor.ops.OpCopyShape;
import diagrameditor.ops.OpMoveShape;
import diagrameditor.ops.OpNewShape;
import diagrameditor.ops.OpResizeShape;
import diagrameditor.widgets.Circle;
import diagrameditor.widgets.Image;
import diagrameditor.widgets.Rectangle;
import diagrameditor.widgets.Text;

/**
 * Class to set up and run the history-based diagram editor prototype.
 * 
 */
public class DiagramEditor extends JFrame {
	private static final long serialVersionUID = -8330250391234385835L;

	enum Mode {
		NETWORK, REPLICATION, DEFAULT
	}

	public static final Mode MODE = Mode.REPLICATION;
	public static final boolean NETWORK_ACCESS = false;

	// Variables
	public static PDStore mainStore;

	private static boolean inPresentationMode = false;

	private PDDiagram diagram;
	private PDDiagramSet diagramSet;

	private OperationList operationList;
	private DiagramList diagramList;
	private GenericLinkedList<GUID, Object, GUID, GUID> shapeList;

	protected String username;

	// main widgets
	public JComboBox selectBox;
	public StatusBar statusBar;
	protected Menu menuBar;
	protected DrawPanel drawPanel;
	protected HistoryPanel historyPanel;
	public OperationPanel operationPanel;
	protected DiagramPanel diagramPanel;

	public PDDiagram getDiagram() {
		return diagram;
	}

	public void setDiagram(PDDiagram diagram) {
		this.diagram = diagram;
	}

	public PDDiagramSet getDiagramSet() {
		return diagramSet;
	}

	public OperationList getOperationList() {
		return operationList;
	}

	public void setOperationList(OperationList operationList) {
		this.operationList = operationList;
	}

	public DiagramList getDiagramList() {
		return diagramList;
	}

	public void setDiagramList(DiagramList list) {
		diagramList = list;
	}

	public GenericLinkedList<GUID, Object, GUID, GUID> getShapeList() {
		return shapeList;
	}

	public void setShapeList(GenericLinkedList<GUID, Object, GUID, GUID> shapeList) {
		this.shapeList = shapeList;
	}

	public void startPresentationMode() {
		inPresentationMode = true;
	}

	public void endPresentationMode() {
		inPresentationMode = false;
	}

	public static boolean isInPresentationMode() {
		return inPresentationMode;
	}

	/**
	 * Constructor
	 * 
	 * @param username-name of user
	 * @param workingCopy-cache of PDStore
	 * @param diagramSetID
	 * @throws HeadlessException
	 */
	public DiagramEditor(String username, GUID diagramSetID) throws HeadlessException {
		super("Diagram Editor _ " + username);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 700);
		this.setMinimumSize(new Dimension(100, 10));
		this.setLayout(new BorderLayout());

		this.selectBox = new JComboBox();
		this.diagramSet = PDDiagramSet.load(mainStore, diagramSetID);
		this.username = username;

		// instantiate & populate diagramList
		diagramList = new DiagramList(mainStore, PDDiagramSet.load(mainStore, diagramSetID),
				PDDiagramSet.roleDiagramId, PDDiagram.roleNextId);

		// find and register all operations
		init(mainStore);

		// Create bars.
		this.menuBar = new Menu(this);
		this.statusBar = new StatusBar();

		// Create the different panels.
		this.diagramPanel = new DiagramPanel(this);
		this.drawPanel = new DrawPanel(this);
		this.historyPanel = new HistoryPanel(this);
		this.operationPanel = new OperationPanel(this);

		// Load up first diagram in the set
		//System.out.println("DE:cons_ diagrams (" + diagramList.size() + ")");
		loadDiagram(diagramList.get(0).getId());

		// Create the HistoryPanel-OperationPanel splitpane.
		JSplitPane histOpeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.historyPanel, this.operationPanel);
		histOpeSplitPane.setOneTouchExpandable(true);
		histOpeSplitPane.setDividerLocation(200);
		Dimension miniSize = new Dimension(175, 150);
		this.historyPanel.setMinimumSize(miniSize);
		this.operationPanel.setMinimumSize(new Dimension(175, 270));
		this.operationPanel.setMaximumSize(new Dimension(175, 270));
		this.operationPanel.setPreferredSize(new Dimension(175, 270));

		// Splitpanel between slideListBox and histOpeSplitPane.
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.diagramPanel, histOpeSplitPane);
		splitPane2.setOneTouchExpandable(true);
		splitPane2.setDividerLocation(100);

		// Create a new splitpane with DrawPanel.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane2, this.drawPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(178);

		// Add them to the DiagramEditor.
		Container contentPane = this.getContentPane();
		contentPane.add(this.menuBar, BorderLayout.NORTH);
		contentPane.add(splitPane, BorderLayout.CENTER);
		contentPane.add(this.statusBar, BorderLayout.SOUTH);

		this.setVisible(true);

	}

	/**
	 * Method to load a history from the database
	 * 
	 * @param diagramID-ID of the history
	 */
	public void loadDiagram(GUID diagramID) {
		diagram = PDDiagram.load(mainStore, diagramID);

		setOperationList(new OperationList(mainStore, diagram, PDDiagram.roleOperationApplicationId,
				PDOperationApplication.roleNextId));
		setShapeList(new GenericLinkedList<GUID, Object, GUID, GUID>(mainStore, diagram, PDDiagram.roleShapeId,
				PDOperationApplication.roleNextId));

		repaint();
	}

	/**
	 * Takes a given PDOperationApplication, obtains the related Operation and
	 * SuperParameter and applies this operation, storing the result to the
	 * PDStore
	 * 
	 * @param pdOperationApplication
	 *            The pdOperationApplication to be applied
	 */
	public void applyOpApp(PDOperationApplication pdOpApp) {
		try {
			// Obtain PDOperation and PDOperationApplication's SuperParameter
			GUID pdOp = pdOpApp.getOperation().getId();
			PDInstance pdParam = (PDInstance) pdOpApp.getSuperParameter();

			mainStore.applyOperation(pdOp, pdParam);
		} catch (Throwable e) {
			System.err.println(e);
		}
		return;
	}

	/**
	 * Main method which is executed when we run the diagram editor
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {

		// register DAL classes and operations
		try {
			Class.forName("diagrameditor.dal.PDDiagramSet");
			Class.forName("diagrameditor.dal.PDDiagram");
			Class.forName("diagrameditor.dal.PDOperationApplication");
			Class.forName("diagrameditor.dal.PDOperation");
			Class.forName("diagrameditor.dal.PDShape");

			Class.forName("diagrameditor.dal.PDColorParam");
			Class.forName("diagrameditor.dal.PDMoveParam");
			Class.forName("diagrameditor.dal.PDResizeParam");
			Class.forName("diagrameditor.dal.PDNewParam");
			Class.forName("diagrameditor.dal.PDCopyParam");
			Class.forName("diagrameditor.dal.PDDeleteParam");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// TODO: connect to store
		if (NETWORK_ACCESS) {
			mainStore = new PDStore("DiagramEditor");
		} else {
			mainStore = new PDStore("DiagramEditor");
		}

		// choose initial diagramSet to show
		GUID diagramSetID = DiagramEditor.getInitialDiagramSetId(mainStore);

		DiagramEditor editor1 = new DiagramEditor("Bob", diagramSetID);
		//DiagramEditor editor2 = new DiagramEditor("Ann", diagramSetID);
	}

	public void loadNewStore(PDStore store) {
		System.out.println("DiagEditor:load_ repo ID\t" + store.getRepository());
		init(store);
		GUID diagramSetID = getInitialDiagramSetId(store);
		setDiagramList(new DiagramList(store, PDDiagramSet.load(store, diagramSetID), PDDiagramSet.roleDiagramId,
				PDDiagram.roleNextId));
		loadDiagram(diagramList.get(0).getId());
		diagramPanel.refreshModel();
	}

	/**
	 * This static method should be called upon initialisation and registers all
	 * current detected operation implementations to the pdStore.
	 */
	private void init(PDStore store) {
		// TODO: Replace hard-coded registration below with dynamic
		// detection/registration
		OpNewShape.register(store);
		OpChangeColor.register(store);
		OpCopyShape.register(store);
		OpMoveShape.register(store);
		OpResizeShape.register(store);

		Circle.register(store);
		Rectangle.register(store);
		Text.register(store);
		Image.register(store);
		store.commit();

		mainStore = store;

		// set up change listener for repainting
		RepaintListener listener1 = new RepaintListener(this);
		PDChange<GUID, Object, GUID> changeTemplate = new PDChange<GUID, Object, GUID>(null, null, null,
				PDDiagram.roleOperationApplicationId, null);
		//store.getListenerDispatcher().addListener(listener1, changeTemplate);
		store.getDetachedListenerList().add(listener1);

	}

	private static GUID getInitialDiagramSetId(PDStore store) {
		Collection<Object> diagramSet = store.getAllInstancesOfType(CreateModel.DIAGRAMSET_TYPE_ID);
		GUID diagramSetID;
		if (diagramSet.isEmpty()) {
			PDDiagramSet newDiag = new PDDiagramSet(store);
			newDiag.addDiagram(new GUID());
			diagramSetID = newDiag.getId();

			store.commit();
		} else {
			// in general there should only be one diagramSet
			diagramSetID = (GUID) diagramSet.iterator().next();
		}

		return diagramSetID;
	}

}
