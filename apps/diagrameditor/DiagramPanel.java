package diagrameditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pdstore.GUID;
import diagrameditor.dal.PDDiagram;

/**
 * Class to set up the history panel section of the diagram editor, listen to
 * the history buttons and perform the required actions caused by pressing these
 * buttons.
 * 
 */
public class DiagramPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String upString = "Move up";
	private static final String downString = "Move down";
	final String prefix = "Slide ";
	private DiagramEditor editor;

	public JList list;
	// History panel buttons
	private JButton upButton;
	private JButton downButton;
	private JButton deleteButton;
	private JButton newButton;

	public DefaultListModel listModel;

	/**
	 * Constructor
	 * 
	 * @param editor
	 *            , diagram editor
	 */
	public DiagramPanel(DiagramEditor editor) {
		super(new BorderLayout());
		this.editor = editor;
		setUpLayout();
	}

	/**
	 * Method to set up the layout of the history panel
	 */
	private void setUpLayout() {
		this.setMinimumSize(new Dimension(this.getPreferredSize().width, 100));
		listModel = new DefaultListModel();
		this.list = new JList(listModel);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		refreshModel();
		this.list.setSelectedIndex(0);

		// set selected action (load the selected diagram)
		this.list.addListSelectionListener(new ListSelectionListener() {
			// Load the relevant operations when a different diagram is selected
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false && list.getSelectedIndex() != -1) {

					GUID diagramId = getDiagramAt(list.getSelectedIndex()).getId();
					editor.loadDiagram(diagramId);
				}
			}
		});

		// Create the Up Arrow button.
		JScrollPane listScrollPane = new JScrollPane(list);
		ImageIcon icon = createImageIcon("up");
		if (icon != null) {
			this.upButton = new JButton(icon);
			this.upButton.setSize(5, 5);
			this.upButton.setMargin(new Insets(0, 0, 0, 0));
		} else {
			this.upButton = new JButton("UP");
		}
		this.upButton.setToolTipText("Move the currently selected diagram higher.");
		this.upButton.addActionListener(upButton(editor, list));
		this.upButton.setActionCommand(upString);

		// Create the Down Arrow button.
		icon = createImageIcon("down");
		if (icon != null) {
			this.downButton = new JButton(icon);
			this.downButton.setMargin(new Insets(0, 0, 0, 0));
		} else {
			this.downButton = new JButton("Down");
		}
		this.downButton.setToolTipText("Move the currently selected diagram lower.");
		this.downButton.addActionListener(downButton(editor, list));
		this.downButton.setActionCommand(downString);

		// Create the Delete button.
		icon = createImageIcon("delete");
		if (icon != null) {
			this.deleteButton = new JButton(icon);
			this.deleteButton.setMargin(new Insets(0, 0, 0, 0));
		} else {
			this.deleteButton = new JButton("Delete");
		}
		this.deleteButton.setToolTipText("Delete the selected diagram.");
		this.deleteButton.addActionListener(deleteButton(editor, list));
		this.deleteButton.setActionCommand("Delete");

		//Add a new slide button.
		icon = createImageIcon("new");
		if (icon != null) {
			this.newButton = new JButton(icon);
			this.newButton.setMargin(new Insets(0, 0, 0, 0));
		} else {
			this.newButton = new JButton("New");
		}
		this.newButton.setToolTipText("Add a new diagram.");
		this.newButton.addActionListener(newButton(editor, list));
		this.newButton.setActionCommand("New");

		// Add buttons to the layout.
		JPanel upDownPanel = new JPanel(new GridLayout(1, 3));
		upDownPanel.add(newButton);
		upDownPanel.add(upButton);
		upDownPanel.add(downButton);
		upDownPanel.add(deleteButton);

		JPanel buttonPane = new JPanel();
		buttonPane.add(upDownPanel);

		this.add(buttonPane, BorderLayout.NORTH);
		this.add(listScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Method to create an image
	 * 
	 * @param imageName
	 * @return an ImageIcon of the image
	 */
	private static ImageIcon createImageIcon(String imageName) {
		String imgLocation = imageName + ".jpg";
		java.net.URL imageURL = DiagramPanel.class.getResource(imgLocation);
		if (imageURL == null) {
			System.err.println("Resource not found: " + imgLocation);
			return null;
		} else {
			return new ImageIcon(imageURL);
		}
	}

	/**
	 * Method to listen to the delete button action events and delete the
	 * selected operation from the history list.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the delete button
	 */
	public ActionListener deleteButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int currentIndex = list.getSelectedIndex();
				PDDiagram curDiag = getDiagramAt(currentIndex);

				// No operation has been selected
				if (curDiag == null) {
					String message = "Please select one diagram to delete";
					editor.statusBar.setError(message);
				} else if (list.getModel().getSize() == 1) {
					String message = "You cannot delete a diagram when there is one left";
					editor.statusBar.setError(message);
				} else {
					editor.statusBar.clearStatus();

					// remove the selected diagram
					//testOpList(editor.getDiagramList());
					//System.out.println("DiagPanel:delete_ - trying to remove: " + curDiag.getId());
					editor.getDiagramList().remove(curDiag);
					//testOpList(editor.getDiagramList());

					// push change to the database
					DiagramEditor.mainStore.commit();

					refreshModel();
					revalidate();

					list.setSelectedIndex(0);
				}
			}
		};
		return listener;
	}

	//method for testing. Prints the contents of an operationsList.
	public void testOpList(DiagramList diagList) {
		System.out.println("Current diagramList: ");
		for (int i = 0; i < diagList.size(); i++) {
			System.out.println(prefix + diagList.get(i).getId());
		}
		System.out.println("");
	}

	/**
	 * Method to listen to the down button action events and move the selected
	 * operation down in the history list.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the down button
	 */
	public ActionListener downButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int listSize = list.getModel().getSize();
				int currentIndex = list.getSelectedIndex();
				PDDiagram curDiag = getDiagramAt(currentIndex);

				if (curDiag == null) {
					String message = "Please select one diagram to move down";
					editor.statusBar.setError(message);

				} else if (currentIndex == listSize - 1) {
					// Cannot move the last diagram downwards.
					String message = "Bottom diagram! Can not move down again!";
					editor.statusBar.setError(message);

				} else {
					editor.statusBar.clearStatus();

					// Switch the position of the two operations
					editor.getDiagramList().move(curDiag, true);

					refreshModel();
					revalidate();

					// highlight the existing selected diagram
					list.setSelectedIndex(currentIndex + 1);
				}
			}
		};
		return listener;
	}

	/**
	 * Method to listen to the up button action events and move the selected
	 * operation up in the history list.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the up button
	 */
	public ActionListener upButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int currentIndex = list.getSelectedIndex();
				PDDiagram curDiag = getDiagramAt(currentIndex);

				// No diagram has been selected to move
				if (curDiag == null) {
					String message = "Please select one diagram to move up";
					editor.statusBar.setError(message);
					// Cannot move the top diagram upwards.
				} else if (currentIndex == 0) {
					String message = "Top diagram! Can not move up again!";
					editor.statusBar.setError(message);
					// move the selected diagram
				} else {
					//System.out.println("DiagPanel:up_ moved up");
					editor.statusBar.clearStatus();

					// Switch the position of the two operations
					editor.getDiagramList().move(curDiag, false);

					refreshModel();
					revalidate();

					// highlight the existing selected diagram
					list.setSelectedIndex(currentIndex - 1);
				}
			}
		};
		return listener;
	}

	/**
	 * Method to listen to the new button action events and create a new slide.
	 * 
	 * @param editor
	 *            , the diagram editor
	 * @param list
	 *            , the history list
	 * @return an action listener for the new button
	 */
	public ActionListener newButton(final DiagramEditor editor, final JList list) {
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PDDiagram newDiag = new PDDiagram(DiagramEditor.mainStore);
				DiagramEditor.mainStore.addLink(editor.getDiagramSet().getId(), CreateModel.DIAGRAM_TYPE_ID,
						newDiag.getId());
				editor.getDiagramList().add(newDiag);

				DiagramEditor.mainStore.commit();
				refreshModel();
				revalidate();

				//set selected as the new diagram made
				int lastIndex = list.getModel().getSize() - 1;
				if (lastIndex >= 0)
					list.setSelectedIndex(lastIndex);

			}

		};
		return listener;
	}

	private PDDiagram getDiagramAt(int index) {
		String itemLabel = (String) listModel.getElementAt(index);
		PDDiagram diag = PDDiagram.load(DiagramEditor.mainStore,
				new GUID(itemLabel.substring(prefix.length(), itemLabel.length())));

		return diag;
	}

	public void refreshModel() {
		listModel.clear();
		for (PDDiagram diag : editor.getDiagramList()) {
			listModel.addElement(prefix + diag.getId());
		}
	}
}
