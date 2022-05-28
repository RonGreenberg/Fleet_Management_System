package model.algorithms;
import model.statlib.Point;


import java.util.Collection;

public class Circle {

private static final double MULTIPLICATIVE_EPSILON = 1 + 1e-14;


	public final Point c;   // Center
	public final double r;  // Radius


	public Circle(Point c, double r) {
		this.c = c;
		this.r = r;
	}


	public boolean contains(Point p) {
		return c.distance(p) <= r * MULTIPLICATIVE_EPSILON;
	}


	public boolean contains(Collection<Point> ps) {
		for (Point p : ps) {
			if (!contains(p))
				return false;
		}
		return true;
	}


	public String toString() {
		return String.format("Circle(x=%g, y=%g, r=%g)", c.x, c.y, r);
	}


}
