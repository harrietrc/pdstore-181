package diagrameditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import pdstore.GUID;
import pdstore.PDStoreException;
import pdstore.ui.PDStore;
import diagrameditor.dal.PDDiagram;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.filters.DependencyFilter;
import diagrameditor.filters.Filter;
import diagrameditor.filters.NoFilter;
import diagrameditor.filters.ShapeFilter;

/**
 * Class to set up the menu bar for the diagram editor and listen to actions on
 * it
 */
public class Menu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 3554253198683513656L;

	private DiagramEditor editor;
	private JMenu fileMenu, filtersMenu, presentationMenu;
	private JMenuItem newItem, openItem, networkOpenItem, startItem;
	private JRadioButtonMenuItem dependency, none, shape;
	private HistoryManager openDialog, saveDialog;

	protected Filter selectedFilter, noFilter, dependencyFilter, shapeFilter;

	protected String selectedShape;
	protected PDOperationApplication selectedOp;

	/**
	 * Constructor
	 * 
	 * @param editor
	 *            , the diagram editor
	 */
	public Menu(DiagramEditor editor) {
		this.editor = editor;

		// Create "File" menu.
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newItem = new JMenuItem("New", KeyEvent.VK_N);
		newItem.addActionListener(this);
		fileMenu.add(newItem);

		openItem = new JMenuItem("Open", KeyEvent.VK_O);
		openItem.addActionListener(this);
		fileMenu.add(openItem);

		networkOpenItem = new JMenuItem("Load from network", KeyEvent.VK_L);
		networkOpenItem.addActionListener(this);
		fileMenu.add(networkOpenItem);

		// Create "Filters" menu.
		filtersMenu = new JMenu("Filters");
		filtersMenu.setMnemonic(KeyEvent.VK_T);
		ButtonGroup filtersGroup = new ButtonGroup();

		dependency = new JRadioButtonMenuItem("Filter by dependency tree");
		dependency.setMnemonic(KeyEvent.VK_D);
		dependency.addActionListener(this);
		filtersGroup.add(dependency);
		filtersMenu.add(dependency);

		shape = new JRadioButtonMenuItem("Filter by shape");
		shape.setMnemonic(KeyEvent.VK_S);
		shape.addActionListener(this);
		filtersGroup.add(shape);
		filtersMenu.add(shape);

		filtersMenu.addSeparator();
		none = new JRadioButtonMenuItem("None");
		none.setSelected(true);
		none.setMnemonic(KeyEvent.VK_N);
		none.addActionListener(this);
		filtersGroup.add(none);
		filtersMenu.add(none);

		// Create "Presentation" menu.
		presentationMenu = new JMenu("Presentation");
		presentationMenu.setMnemonic(KeyEvent.VK_P);
		ButtonGroup presentationGroup = new ButtonGroup();

		startItem = new JMenuItem("Start presentation", KeyEvent.VK_S);
		startItem.addActionListener(this);
		presentationMenu.add(startItem);

		add(fileMenu);
		add(filtersMenu);
		add(presentationMenu);

		createFilters();
	}

	/**
	 * Method to initialise the filters
	 */
	private void createFilters() {
		noFilter = new NoFilter();
		shapeFilter = new ShapeFilter();
		dependencyFilter = new DependencyFilter();
		selectedFilter = noFilter;
	}

	/**
	 * Method to listen to actions on the menu
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//create a new diagramSet
		if (e.getSource().equals(newItem)) {
			PDDiagram diagram = PDDiagram.load(DiagramEditor.mainStore, new GUID());
			diagram.setName("");
			DiagramEditor.mainStore.commit();
			selectedFilter = noFilter;
			none.setSelected(true);
			editor.loadDiagram(diagram.getId());
			//open a existing diagramSet
		} else if (e.getSource().equals(openItem)) {
			//System.out.println("Menu:action_ openItem");
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("./pddata"));
			FileFilter filter = new FileNameExtensionFilter("PDStore database file", "pds");
			fileChooser.addChoosableFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(editor.drawPanel);
			if (returnVal != JFileChooser.APPROVE_OPTION)
				return;

			String fileName = fileChooser.getSelectedFile().getName();
			fileName = fileName.substring(0, fileName.length() - 4);
			PDStore store = new PDStore(fileName);
			editor.loadNewStore(store);

			//open an existing diagramSet over the network
		} else if (e.getSource().equals(networkOpenItem)) {
			String oldURL = "";
			String newURL = JOptionPane.showInputDialog(null, "Host URL of the remote PDStore server:", oldURL);

			if (newURL == null || newURL.equals(""))
				return;

			try {
				PDStore store = new PDStore(PDStore.connectToServer(newURL));
				editor.loadNewStore(store);
			} catch (PDStoreException e1) {
				String message = "Connection error. Check the URL and try again.";
				editor.statusBar.setError(message);
			}

			//starts the current DiagramSet presentation, focus on the selected diagram
		} else if (e.getSource().equals(startItem)) {
			editor.startPresentationMode();
			PresentationFrame pf = new PresentationFrame(DiagramEditor.mainStore, editor, editor.drawPanel,
					editor.diagramPanel.list.getSelectedIndex());
			//selected a filter	
		} else if (e.getSource().equals(none)) {
			selectedFilter = noFilter;
		} else if (e.getSource().equals(dependency)) {
			selectedFilter = dependencyFilter;
			selectedFilter.setSelectedOp((PDOperationApplication) editor.historyPanel.list.getSelectedValue());
		} else if (e.getSource().equals(shape)) {
			selectedFilter = shapeFilter;
			selectedFilter.setSelectedOp((PDOperationApplication) editor.historyPanel.list.getSelectedValue());
		}
	}
}
