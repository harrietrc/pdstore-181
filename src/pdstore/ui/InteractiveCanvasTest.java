package pdstore.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class InteractiveCanvasTest extends JFrame {

	InteractiveCanvas canvas;
	JScrollPane scrollPane;

	InteractiveCanvasTest() {
		canvas = new InteractiveCanvas(new PDStore("InteractiveCanvasTest"),
				null);
		scrollPane = new JScrollPane(canvas);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new InteractiveCanvasTest();
	}
}
