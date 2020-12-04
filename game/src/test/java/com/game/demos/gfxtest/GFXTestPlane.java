package com.game.demos.gfxtest;

import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Ellipse;
import com.game.engine.graphics.obj.Image;
import com.game.engine.graphics.obj.Line;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

@SuppressWarnings("javadoc")
public class GFXTestPlane extends AbstractPlane {

	public GFXTestPlane() {
		super(1400, 1200);
	}

	static Random rand = new Random();

	static final int MIN_OBJ_COUNT = 10;
	static final int MAX_OBJ_COUNT = 20;
	static final int MAX_OBJ_SIZE = 200;

	@Override
	public void init(GameDriver driver) {
		super.init(driver);

		// Add random graphic objects
		for (int i = 0; i < MIN_OBJ_COUNT + rand.nextInt(MAX_OBJ_COUNT - MIN_OBJ_COUNT); i++) {
			this.levelObjects.add(new TestGraphicObject(this, driver, i));
		}

	}

	@Override
	public void update(GameDriver driver) {
		// TODO Auto-generated method stub
		super.update(driver);
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// TODO Auto-generated method stub
		super.stage(driver, renderer);
	}

}

@SuppressWarnings("javadoc")
class TestGraphicObject extends AbstractGameObject {

	/**
	 * Rng
	 */
	static Random rng = new Random(); 
	
	/**
	 * Size of the graphic test object.
	 */
	int size;

	/**
	 * Type of the graphic test object.
	 * 0 - Image 
	 * 1 - Line 
	 * 2 - Rectangle 
	 * 3 - Ellipse
	 */
	int type;

	/**
	 * The plane this exists on.
	 */
	AbstractPlane parent;

	/**
	 * The drawable graphic object.
	 */
	Drawable drawable;

	public TestGraphicObject(AbstractPlane plane, GameDriver driver, int type) {
		this.parent = plane;
		this.type = type;
		this.size = Math.max(10, GFXTestPlane.rand.nextInt(GFXTestPlane.MAX_OBJ_SIZE));

		// Put in a random location
		this.x = GFXTestPlane.rand.nextInt(parent.width - this.size);
		this.y = GFXTestPlane.rand.nextInt(parent.height - this.size);
		
		switch (this.type % 4) {
		case 0:
			Image img = driver.cache.fetch("C:\\Users\\Spencer Imbleau\\Pictures\\Designs\\headshot_512px.png");
			float sx = (float) this.size / img.getWidth();
			float sy = (float) this.size / img.getHeight();
			this.drawable = img.resize(sx, sy);
			break;
		case 1:
			this.drawable = new Line(this.size, this.size, 0xffff0000);
			break;
		case 2:
			this.drawable = new Rectangle(this.size, this.size, 0xffff0000);
			break;
		case 3:
			this.drawable = new Ellipse(size, size, 0xffff0000);
			break;
		default:
			System.err.println("Unexpected graphic type: " + this.type);
			System.exit(1);
		}
	}

	@Override
	public void update(GameDriver driver) {
		double dx = 5 * rng.nextDouble() * ((rng.nextBoolean()) ? 1 : -1);
		double dy = 5 * rng.nextDouble() * ((rng.nextBoolean()) ? 1 : -1);
		this.x = Math.max(0, Math.min(this.x + dx, this.parent.width - this.size));
		this.y = Math.max(0, Math.min(this.y + dy, this.parent.height - this.size));
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		RenderRequest request = this.drawable.asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x, (int) this.y);
		renderer.stage(request);
	}

}
