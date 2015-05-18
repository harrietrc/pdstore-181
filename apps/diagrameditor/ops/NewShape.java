package diagrameditor.ops;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import pdstore.PDStore;
import pdstore.dal.PDInstance;
import diagrameditor.DiagramEditor;
import diagrameditor.dal.PDNewParam;
import diagrameditor.dal.PDOperationApplication;
import diagrameditor.dal.PDShape;
import diagrameditor.widgets.Circle;
import diagrameditor.widgets.Rectangle;

/**
 * Class to handle GUI and create superparameters for images.
 *
 */
public class NewShape extends EditorOperation {

	private JButton circleButton;
	private JButton rectangleButton;

	public NewShape(DiagramEditor editor) {
		super(editor);
	}

	protected void generateOperationPanel() {
		circleButton = new JButton("New Circle");
		circleButton.setMargin(new Insets(0, 11, 0, 11));
		circleButton.addActionListener(this);

		rectangleButton = new JButton("New Rectangle");
		rectangleButton.setMargin(new Insets(0, 11, 0, 11));
		rectangleButton.addActionListener(this);

		JPanel shapesPanel = new JPanel(new BorderLayout());
		JPanel paddingPanel1 = new JPanel();
		JPanel paddingPanel2 = new JPanel();

		shapesPanel.add(circleButton, BorderLayout.NORTH);
		shapesPanel.add(paddingPanel1, BorderLayout.CENTER);
		shapesPanel.add(rectangleButton, BorderLayout.SOUTH);

		operationPanel = new JPanel(new BorderLayout());
		operationPanel.add(shapesPanel, BorderLayout.NORTH);
		operationPanel.add(paddingPanel2, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PDStore store = DiagramEditor.mainStore;

		// Create the parameter object and add to the operation list
		PDOperationApplication opApp = new PDOperationApplication(store);
		opApp.addOperation(OpNewShape.opID);

		PDNewParam param = new PDNewParam(store);
		PDInstance parent = (PDInstance) editor.getShapeList().getParentInstance();
		param.addTargetShapeList(parent.getId());

		if (e.getSource().equals(circleButton)) {
			param.addVisualizedBy(Circle.widgetID);
		} else if (e.getSource().equals(rectangleButton)) {
			param.addVisualizedBy(Rectangle.widgetID);
		}

		// Create the new shape
		PDShape newShape = new PDShape(store);
		param.addTargetShape(newShape.getId());

		opApp.addSuperParameter(param);
		editor.getOperationList().add(opApp);
		store.commit();

		editor.repaint();
	}

}
