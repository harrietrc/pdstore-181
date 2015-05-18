package pdstore.ui.graphview;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * A Bezier arch to be used in a spline.
 * 
 * @author Harriet Fell
 * @email fell@ccs.neu.edu
 * @date August 2004
 */
public class BezierArch {
	Point2D.Double P, Q, R, S;

	public BezierArch(Point2D.Double p, Point2D.Double q, Point2D.Double r,
			Point2D.Double s) {
		P = p;
		Q = q;
		R = r;
		S = s;
	}

	public void draw(Graphics2D g2) {
		if (ArchSize(P, Q, R, S) <= .5)
			g2.drawLine((int) P.getX(), (int) P.getY(), (int) S.getX(),
					(int) S.getY());
		else {
			Point2D.Double PQ, QR, RS, PQR, QRS, PQRS;
			PQ = new Point2D.Double((P.getX() + Q.getX()) / 2,
					(P.getY() + Q.getY()) / 2);
			QR = new Point2D.Double((Q.getX() + R.getX()) / 2,
					(Q.getY() + R.getY()) / 2);
			RS = new Point2D.Double((R.getX() + S.getX()) / 2,
					(R.getY() + S.getY()) / 2);

			PQR = new Point2D.Double((PQ.getX() + QR.getX()) / 2,
					(PQ.getY() + QR.getY()) / 2);
			QRS = new Point2D.Double((QR.getX() + RS.getX()) / 2,
					(QR.getY() + RS.getY()) / 2);

			PQRS = new Point2D.Double((PQR.getX() + QRS.getX()) / 2,
					(PQR.getY() + QRS.getY()) / 2);

			BezierArch B1 = new BezierArch(P, PQ, PQR, PQRS);
			B1.draw(g2);
			BezierArch B2 = new BezierArch(PQRS, QRS, RS, S);
			B2.draw(g2);
		}
	}

	double ArchSize(Point2D.Double P, Point2D.Double Q, Point2D.Double R,
			Point2D.Double S) {
		double xmin = P.getX(), ymin = P.getY();
		double xmax = P.getX(), ymax = P.getY();

		if (Q.getX() < xmin)
			xmin = Q.getX();
		else if (Q.getX() > xmax)
			xmax = Q.getX();
		if (Q.getY() < ymin)
			ymin = Q.getY();
		else if (Q.getY() > ymax)
			ymax = Q.getY();
		if (R.getX() < xmin)
			xmin = Q.getX();
		else if (R.getX() > xmax)
			xmax = R.getX();
		if (R.getY() < ymin)
			ymin = R.getY();
		else if (R.getY() > ymax)
			ymax = R.getY();
		if (S.getX() < xmin)
			xmin = S.getX();
		else if (S.getX() > xmax)
			xmax = S.getX();
		if (S.getY() < ymin)
			ymin = S.getY();
		else if (S.getY() > ymax)
			ymax = S.getY();
		if (xmax - xmin > ymax - ymin)
			return (xmax - xmin);
		else
			return (ymax - ymin);
	}
}