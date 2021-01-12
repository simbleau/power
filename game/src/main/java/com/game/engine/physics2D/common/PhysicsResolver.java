package com.game.engine.physics2D.common;

import com.game.engine.maths.Line2D;
import com.game.engine.maths.Matrix2D;
import com.game.engine.maths.Vector2D;
import com.game.engine.physics2D.Ray;
import com.game.engine.physics2D.Raycast;
import com.game.engine.physics2D.primitives.AABB;
import com.game.engine.physics2D.primitives.Circle;
import com.game.engine.physics2D.primitives.OBB;

/**
 * TODO: Document
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
@SuppressWarnings("javadoc")
public class PhysicsResolver {

	/**
	 * Prevents instantiation.
	 */
	private PhysicsResolver() {
		// Do not instantiate
	}

	public static boolean pointInLine(Vector2D point, Line2D line) {
		// Calculate dx, dy
		double dy = line.dy();
		double dx = line.dx();
		// Avoid dividing by 0, so check for vertical lines
		if (dx == 0) {
			return point.equals(line.start);
		}
		// y = slope(x) + intercept
		double slope = dy / dx;
		double intercept = dy - (slope * dx);
		// Check if the point is on the line
		return (point.y() == slope * point.x() + intercept);
	}

	public static boolean pointInCircle(Vector2D point, Circle circle) {
		// A point is in the circle if it has a distance from the center less than the
		// radius of the circle
		Vector2D center = circle.getCenter();
		Vector2D distance = point.clone().minus(center);
		return distance.lengthSquared() <= circle.getRadius() * circle.getRadius();
	}

	public static boolean pointInAABB(Vector2D point, AABB aabb) {
		// Ensure the point is within the bounds of the AABB
		Vector2D min = aabb.getMin();
		Vector2D max = aabb.getMax();
		return point.x() >= min.x() && point.x() <= max.x() && point.y() >= min.y() && point.y() <= max.y();
	}

	public static boolean pointInOBB(Vector2D point, OBB obb) {
		// Rotate the point being tested to be the same orientation as the obb
		Vector2D rotatedPoint = point.clone().rotate(obb.getRigidBody().getRotation(),
				obb.getRigidBody().getCenter().x(), obb.getRigidBody().getCenter().y());

		// Test as if it were an AABB
		Vector2D min = obb.getMin();
		Vector2D max = obb.getMax();
		return rotatedPoint.x() >= min.x() && rotatedPoint.x() <= max.x() && rotatedPoint.y() >= min.y()
				&& rotatedPoint.y() <= max.y();
	}

	// TODO : Line vs Primitive tests
	public static boolean lineInCircle(Line2D line, Circle circle) {
		// Check if either endpoint of the line is in the circle.
		// This is a micro-optimization and could be deleted.
		if (pointInCircle(line.start, circle) || pointInCircle(line.end, circle)) {
			return true;
		}

		// ab = line.end - line.start
		Vector2D ab = line.reduce();

		// Project the center point of the circle onto our line to receive a derived
		// paramaterized t such that t is a percentage of the line
		Vector2D circleCenter = circle.getCenter();
		Vector2D centerToStart = circleCenter.clone().minus(line.start);
		// Parameterized position t
		double t = centerToStart.dotProduct(ab) / ab.dotProduct(ab);

		// t is a percentage of the line ab, therefore, check if we are out of the
		// bounds.
		// This is a micro-optimization and could be deleted.
		if (t < 0 || t > 1) {
			return false;
		}

		// Find the closest point to the line segment
		Vector2D closestPoint = ab.scale(t).plus(line.start);
		// The closest point on the line to the center of the circle will always give a
		// valid result if the line is in the cicle.
		return pointInCircle(closestPoint, circle);
	}

	public static boolean lineInAABB(Line2D line, AABB aabb) {
		// Check if either endpoint of the line is in the AABB.
		// This is a micro-optimization and could be deleted.
		if (pointInAABB(line.start, aabb) || pointInAABB(line.end, aabb)) {
			return true;
		}

		// Get the normal unit vector
		Vector2D unitVector = line.reduce().normalize();
		// Avoid division by 0 by invert scaling rather than division
		unitVector.invert();

		Vector2D min = aabb.getMin();
		min.minus(line.start).componentProduct(unitVector);
		Vector2D max = aabb.getMax();
		max.minus(line.start).componentProduct(unitVector);

		// Determine tmin, tmax
		double tmin = Math.max(Math.min(min.x(), max.x()), Math.min(min.y(), max.y()));
		double tmax = Math.min(Math.max(min.x(), max.x()), Math.max(min.y(), max.y()));
		// If tmax < 0 or tmin > tmax, there is no intersection.
		// This is a micro-optimization and could be deleted.
		if (tmax < 0 || tmin > tmax) {
			return false;
		}

		// Get the furthest t possible
		double t = (tmin < 0) ? tmax : tmin;
		return t > 0 && t * t < line.reduce().lengthSquared();
	}

	public static boolean lineInOBB(Line2D line, OBB obb) {
		// Rotate the points being tested to be the same orientation as the OBB
		Vector2D localStart = line.start.clone().rotate(obb.getRigidBody().getRotation(),
				obb.getRigidBody().getCenter().x(), obb.getRigidBody().getCenter().y());
		Vector2D localEnd = line.end.clone().rotate(obb.getRigidBody().getRotation(),
				obb.getRigidBody().getCenter().x(), obb.getRigidBody().getCenter().y());

		// Make a new line
		Line2D localLine = new Line2D(localStart, localEnd);
		return lineInAABB(localLine, obb);
	}

	// Raycasts
	public static Raycast raycastCircle(Ray ray, Circle circle) {
		Vector2D originToCircle = circle.getCenter().clone().minus(ray.getOrigin());
		double radiusSquared = circle.getRadius() * circle.getRadius();
		double originToCircleLengthSquared = originToCircle.lengthSquared();

		// Project the vector from the ray origin onto the direction of the ray
		double a = originToCircle.dotProduct(ray.getDirection());
		double bSquared = originToCircleLengthSquared - (a * a);
		// Cannot take sqrt of a negative, so it's false
		if (radiusSquared - bSquared < 0) {
			return null; // Return nothing - No raycast hit
		}

		double f = Math.sqrt(radiusSquared - bSquared);
		double t = 0;
		if (originToCircleLengthSquared < radiusSquared) {
			// Ray starts inside the circle
			t = a + f;
		} else {
			// Ray starts outside the circle
			t = a - f;
		}

		Vector2D point = ray.getDirection().clone().scale(t).plus(ray.getOrigin());
		Vector2D normal = point.clone().minus(circle.getCenter()).normalize();
		return new Raycast(point, normal, t);
	}

	public static Raycast raycastAABB(Ray ray, AABB aabb) {
		// Get the normal unit vector
		Vector2D unitVector = ray.getDirection().clone();
		// Avoid division by 0 by invert scaling rather than division
		unitVector.invert();

		Vector2D min = aabb.getMin();
		min.minus(ray.getOrigin()).componentProduct(unitVector);
		Vector2D max = aabb.getMax();
		max.minus(ray.getOrigin()).componentProduct(unitVector);

		// Determine tmin, tmax
		double tmin = Math.max(Math.min(min.x(), max.x()), Math.min(min.y(), max.y()));
		double tmax = Math.min(Math.max(min.x(), max.x()), Math.max(min.y(), max.y()));
		// If tmax < 0 or tmin > tmax, there is no intersection.
		// This is a micro-optimization and could be deleted.
		if (tmax < 0 || tmin > tmax) {
			return null;
		}

		// Get the furthest t possible
		double t = (tmin < 0) ? tmax : tmin;
		boolean hit = t > 0; // && t * t < ray.getMaximum() -- If this gets implemented in the future.
		if (hit) {
			Vector2D point = ray.getDirection().clone().scale(t).plus(ray.getOrigin());
			Vector2D normal = ray.getOrigin().clone().minus(point).normalize();
			return new Raycast(point, normal, t);
		} else {
			return null;
		}
	}

	public static Raycast raycastOBB(Ray ray, OBB obb) {
		// Get unit vectors of axes
		Vector2D xAxis = new Vector2D(1, 0);
		Vector2D yAxis = new Vector2D(0, 1);
		xAxis.rotate(-obb.getRigidBody().getRotation());
		yAxis.rotate(-obb.getRigidBody().getRotation());

		Vector2D p = obb.getRigidBody().getCenter().clone().minus(ray.getOrigin());
		// Project p onto every axis of the box
		Vector2D e = new Vector2D(xAxis.dotProduct(p), yAxis.dotProduct(p));

		// Check for parallelism to x, and that the origin of the ray is not inside
		if (-e.x() - obb.getHalfSize().x() > 0 || -e.x() + obb.getHalfSize().x() < 0) {
			return null;
		}
		// Check for parallelism to y, and that the origin of the ray is not inside
		if (-e.y() - obb.getHalfSize().y() > 0 || -e.y() + obb.getHalfSize().y() < 0) {
			return null;
		}

		// Project the direction of the ray onto each axis
		Vector2D f = new Vector2D(xAxis.dotProduct(ray.getDirection()), yAxis.dotProduct(ray.getDirection())).invert();
		double tMaxX = (e.x() + obb.getHalfSize().x()) * f.x(); // tmax for x
		double tMinX = (e.x() - obb.getHalfSize().x()) * f.x(); // tmin for x
		double tMaxY = (e.y() + obb.getHalfSize().y()) * f.y(); // tmax for y
		double tMinY = (e.y() - obb.getHalfSize().y()) * f.y(); // tmin for y
		
		double tMin = Math.max(Math.min(tMaxX, tMinX), Math.min(tMaxY, tMinY));
		double tMax = Math.min(Math.max(tMaxX, tMinX), Math.max(tMaxY, tMinY));
		
		// Get the furthest t possible
		double t = (tMin < 0) ? tMax : tMin;
		boolean hit = t > 0; // && t * t < ray.getMaximum() -- If this gets implemented in the future.
		if (hit) {
			Vector2D point = ray.getDirection().clone().scale(t).plus(ray.getOrigin());
			Vector2D normal = ray.getOrigin().clone().minus(point).normalize();
			return new Raycast(point, normal, t);
		} else {
			return null;
		}
	}
	
	
	public static boolean circleAndCircle(Circle circle1, Circle circle2) {
		Vector2D distanceBetweenCenters = circle1.getCenter().clone().minus(circle2.getCenter());
		return distanceBetweenCenters.lengthSquared() <= circle1.getRadius() * circle2.getRadius();
	}
	
	public static boolean circleAndAABB(Circle circle, AABB aabb) {
		Vector2D min = aabb.getMin();
		Vector2D max = aabb.getMax();
		Vector2D center = circle.getCenter();
		
		Vector2D closestPoint = new Vector2D();
		// Clamp x and y to the closest point of the AABB
		if (center.x() < min.x()) {
			closestPoint.getMatrix().setX(min.x());
		} else if (center.x() > max.x()) {
			closestPoint.getMatrix().setX(max.x());
		} else {
			closestPoint.getMatrix().setX(center.x());
		}
		if (center.y() < min.y()) {
			closestPoint.getMatrix().setY(min.y());
		} else if (center.y() > max.y()) {
			closestPoint.getMatrix().setY(max.y());
		} else {
			closestPoint.getMatrix().setY(center.y());
		}
		
		// Take the vector from the circle to the closest point on the box
		Vector2D circleToPoint = center.clone().minus(closestPoint);
		// Return whether the point is within the circle's radius
		return circleToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
	}
	
	public static boolean circleAndOBB(Circle circle, OBB obb) {
		Vector2D min = new Vector2D();
		Vector2D max = new Vector2D().plus(obb.getSize());
		
		// Create a circle in the box's local space
		Vector2D dist = circle.getCenter().clone().minus(obb.getRigidBody().getCenter());
		dist.rotate(-obb.getRigidBody().getRotation());
		Vector2D center = dist.clone().plus(obb.getHalfSize());
		
		//Now we are in Axis-aligned space. Continue as if Circle vs. AABB
		Vector2D closestPoint = new Vector2D();
		// Clamp x and y to the closest point of the AABB
		if (center.x() < min.x()) {
			closestPoint.getMatrix().setX(min.x());
		} else if (center.x() > max.x()) {
			closestPoint.getMatrix().setX(max.x());
		} else {
			closestPoint.getMatrix().setX(center.x());
		}
		if (center.y() < min.y()) {
			closestPoint.getMatrix().setY(min.y());
		} else if (center.y() > max.y()) {
			closestPoint.getMatrix().setY(max.y());
		} else {
			closestPoint.getMatrix().setY(center.y());
		}
		
		// Take the vector from the circle to the closest point on the box
		Vector2D circleToPoint = center.clone().minus(closestPoint);
		// Return whether the point is within the circle's radius
		return circleToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
	}
}
