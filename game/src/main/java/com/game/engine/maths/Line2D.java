package com.game.engine.maths;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Line2D {

	public Vector2D start;

	public Vector2D end;

	public Line2D(Vector2D start, Vector2D end) {
		this.start = start;
		this.end = end;
	}

	public Line2D() {
		this.start = new Vector2D();
		this.end = new Vector2D();
	}

	/**
	 * Checks the equivalence of an object to this 2D line. For a 2D line to be
	 * equal, the backing 2D vectors must be equal.
	 *
	 * @param o - the line to be tested against.
	 * @return true if they are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Line2D) {
			Line2D l = (Line2D) o;
			return this.start.equals(l.start) && this.end.equals(l.end);
		} else {
			return false;
		}
	}

	/**
	 * Represent this line in text as a path between two vectors, inheriting the
	 * format of vectors.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.start.toString());
		sb.append("->");
		sb.append(this.end.toString());
		return sb.toString();
	}

	/**
	 * Creates and returns a deep-copy of this 2D Vector object.
	 */
	@Override
	public Line2D clone() {
		Vector2D startClone = this.start.clone();
		Vector2D endClone = this.end.clone();
		return new Line2D(startClone, endClone);
	}

	/**
	 * Reduce this line to its implicit direction and magnitude. This yields a new
	 * vector equivalent to
	 * 
	 * <pre>
	 * line.end.minus(line.start)
	 * </pre>
	 * 
	 * @return a reduced vector of the line, containing only direction and magnitude
	 */
	public Vector2D reduce() {
		return this.end.clone().minus(this.start);
	}

	public double dx() {
		return this.end.x() - this.start.x();
	}

	public double dy() {
		return this.end.y() - this.start.y();
	}
}
