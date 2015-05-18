package pdstore.ui.graphview;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * A spline that goes through its control points.
 * 
 * @author Harriet Fell
 * @email fell@ccs.neu.edu
 * @date August 2004
 */
public class Spline extends JComponent implements MouseMotionListener,
		MouseListener, ActionListener {
	
	Point2D.Double[] controlPoints;
	int numCP;
	boolean getControlPoints;

	// dot size
	int dSize;
	
	// Mouse Points
	int mmX, mmY; // last mouse moved point coordinates
	int mdX, mdY; // last mouse dragged point coordinates

	public Spline() {
		// Listen for mouse motion and clicks
		addMouseMotionListener(this);
		addMouseListener(this);

		// Set dot size
		dSize = 8;

		// Array to hold user input control points;
		controlPoints = new Point2D.Double[20];
		numCP = 0;

		getControlPoints = true;

	}

	BezierArch MakeArch(Point2D.Double[] controlPoints, int numCP, int p) {
		Point2D.Double P = new Point2D.Double(controlPoints[p].x,
				controlPoints[p].y);
		Point2D.Double S = new Point2D.Double(controlPoints[p + 1].x,
				controlPoints[p + 1].y);
		Point2D.Double Q = new Point2D.Double();
		Point2D.Double R = new Point2D.Double();
		if (p == 0)
			Q = P;
		else {
			Q.x = controlPoints[p].x
					+ (-controlPoints[p - 1].x + controlPoints[p + 1].x) / 6;
			Q.y = controlPoints[p].y
					+ (-controlPoints[p - 1].y + controlPoints[p + 1].y) / 6;
		}
		if (p == numCP - 2)
			R = S;
		else {
			R.x = controlPoints[p + 1].x
					- (-controlPoints[p].x + controlPoints[p + 2].x) / 6;
			R.y = controlPoints[p + 1].y
					- (-controlPoints[p].y + controlPoints[p + 2].y) / 6;
		}
		return new BezierArch(P, Q, R, S);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black);
		g2.setStroke(new BasicStroke(2));
		for (int cp = 0; cp < numCP - 1; cp++) {
			BezierArch B = MakeArch(controlPoints, numCP, cp);
			B.draw(g2);
		}

		// Show Control Points
		g2.setPaint(Color.blue);
		for (int cp = 0; cp < numCP; cp++) {
			Point2D.Double P = controlPoints[cp];
			g2.fillOval((int) P.getX() - dSize, (int) P.getY() - dSize,
					2 * dSize, 2 * dSize);
		}
	}

	private boolean close(int x, int y, Point2D p) {
		return ((x - p.getX()) * (x - p.getX()) + (y - p.getY())
				* (y - p.getY()) <= dSize * dSize);
	}

	public void mouseDragged(MouseEvent e) {
		mdX = e.getX();
		mdY = e.getY();
		for (int cp = 0; cp < numCP; cp++) {
			// System.out.println(mmX + "  " + mmY + "  " +
			// controlPoints[cp].toString());
			if (close(mmX, mmY, controlPoints[cp])) {
				controlPoints[cp].setLocation(mdX, mdY);
				mmX = mdX;
				mmY = mdY;
				repaint();
				break;
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		mmX = e.getX();
		mmY = e.getY();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		// System.out.println("clicked");
		int x, y;
		x = e.getX();
		y = e.getY();
		// System.out.println(numCP + "  " + getControlPoints);
		if (getControlPoints && numCP < 20) {
			controlPoints[numCP] = new Point2D.Double(x, y);
			numCP++;
			System.out.println(numCP + "  "
					+ controlPoints[numCP - 1].toString());
		}
		repaint();
	}

	public void actionPerformed(ActionEvent e) {
	}

	public static void main(String[] args) {
		JFrame f = new JFrame(
				"CatmullRom Spline - click to place points - move blue points");
		Container c = f.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new Spline(), BorderLayout.CENTER);

		f.setSize(600, 622);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setVisible(true);
	}
}