package com.game.engine.maths;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

public class Vector2D implements Cloneable {

	private Matrix2D matrix;

	private Vector2D(Matrix2D m) {
		this.matrix = m;
	}

	public Vector2D(double dx, double dy) {
		this.matrix = Matrix2D.create(dx, dy);
	}

	@Override
	public String toString() {
		NumberFormat formatter = new DecimalFormat("000.0");
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(formatter.format(this.matrix.x()));
		sb.append(", ");
		sb.append(formatter.format(this.matrix.y()));
		sb.append(')');
		return sb.toString();
	}

	@Override
	public Vector2D clone() {
		Matrix2D m = this.matrix.clone();
		return new Vector2D(m);
	}

	public double x() {
		return this.matrix.x();
	}

	public double y() {
		return this.matrix.y();
	}

	public void add(Vector2D v) {
		this.matrix.translateEquals(v.x(), v.y());
	}

	public void sub(Vector2D v) {
		this.matrix.translateEquals(-v.x(), -v.y());
	}

	public void scale(double s) {
		this.matrix.scaleEquals(s);
	}

	public void component(Vector2D v) {
		this.matrix.scaleEquals(v.x(), v.y());
	}

	public double dot(Vector2D v) {
		return this.matrix.dot(v.matrix);
	}

	public double cross(Vector2D v) {
		return this.matrix.cross(v.matrix);
	}

	public double magnitude() {
		return Math.sqrt(this.x() * this.x() + this.y() * this.y());
	}

	public void normalize() {
		this.scale(1d / this.magnitude());
	}

	public Vector2D unit() {
		Vector2D clone = this.clone();
		clone.normalize();
		return clone;
	}
	
	public boolean isParallel(Vector2D v) {
		return this.dot(v) == 1;
	}
	
	public boolean isPerpendicular(Vector2D v) {
		return this.dot(v) == 0;
	}

}
