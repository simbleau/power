package com.game.engine.physics2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractComponent;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.physics2D.common.Collidable;
import com.game.engine.rendering.common.AbstractRenderer;

public class PhysicsComponent extends AbstractComponent {

	public final AbstractGameObject parent;

	public final List<Collidable> collidables;

	public final List<Consumer<Collision>> responders;

	public PhysicsComponent(AbstractGameObject parent) {
		this.parent = parent;
		this.collidables = new ArrayList<>();
		this.responders = new ArrayList<>();
	}

	@Override
	public void update(GameDriver driver) {
		// TODO : Limit the area we test collision against with a quad tree
		// For now, loop through all objects in the plane
		for (AbstractGameObject obj : driver.game.getPlane().objects()) {
			if (obj == this.parent) {
				// Skip if we're looking at ourself
				continue;
			}
			// Check if object has physics
			if (obj.hasPhysics()) {
				// Get physics of foreign object
				PhysicsComponent objPhysics = obj.getPhysics();
				// Test all of our collidables against their collidables
				for (Collidable objCollidable : objPhysics.collidables) {
					for (Collidable collidable : this.collidables) {
						if (collidable.collides(objCollidable)) {
							// Collision occurred
							// Get collision events
							Collision collision = collidable.getCollision(objCollidable);
							Collision collision2 = objCollidable.getCollision(collidable);

							// Handle collision for foreign object
							this.responders.forEach(responder -> responder.accept(collision));
							objPhysics.responders.forEach(responder -> responder.accept(collision2));
						}
					}
				}
			}
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// TODO display debug info
	}

}
