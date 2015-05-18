package diagrameditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import pdstore.ui.PDStore;

public class PresentationFrame extends JFrame {
	private static final long serialVersionUID = -5828696014539306130L;
	protected DrawPanel drawPanel;
	private int diagramIndex;
	private DiagramEditor editor;

	public PresentationFrame(PDStore store, DiagramEditor editor, DrawPanel model, int diagramIndex)
			throws HeadlessException {
		super("Diagram Editor Presentation View");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.diagramIndex = diagramIndex;
		this.editor = editor;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize);

		this.setLayout(new BorderLayout());

		// set rendering to presentation mode

		try {
			this.drawPanel = model.displayClone();
			this.add(drawPanel);

			// setup inputMap
			drawPanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "prev_slide");
			drawPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "prev_slide");
			drawPanel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "next_slide");
			drawPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "next_slide");
			drawPanel.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "esc");

			// setup actionMap
			drawPanel.getActionMap().put("prev_slide", new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					prevSlide();
				}
			});
			drawPanel.getActionMap().put("next_slide", new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					nextSlide();
				}
			});
			drawPanel.getActionMap().put("esc", new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					exitPresentationView();
				}
			});

			this.setVisible(true);
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}

	}

	protected void prevSlide() {
		if (diagramIndex <= 0) {
			diagramIndex = 0;
		} else {
			diagramIndex--;
		}

		editor.diagramPanel.list.setSelectedIndex(diagramIndex);
		try {
			drawPanel = editor.drawPanel.displayClone();
			this.setVisible(false);
			this.setVisible(true);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	protected void nextSlide() {
		if (diagramIndex >= (editor.diagramPanel.list.getModel().getSize() - 1)) {
			diagramIndex = editor.diagramPanel.list.getModel().getSize() - 1;
		} else {
			diagramIndex++;
		}

		editor.diagramPanel.list.setSelectedIndex(diagramIndex);
		try {
			drawPanel = editor.drawPanel.displayClone();
			this.setVisible(false);
			this.setVisible(true);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	protected void exitPresentationView() {
		editor.endPresentationMode();
		dispose();
	}

}
