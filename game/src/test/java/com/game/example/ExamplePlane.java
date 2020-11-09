package com.game.example;

import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Ellipse;
import com.game.engine.graphics.obj.Image;
import com.game.engine.graphics.obj.Line;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.graphics.request.ImageRequest;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

@SuppressWarnings("javadoc")
public class ExamplePlane extends AbstractPlane {

	static Random rand = new Random();

	static final int MIN_OBJ_COUNT = 10;
	static final int MAX_OBJ_COUNT = 20;
	static final int MAX_OBJ_SIZE = 200;

	@Override
	public void init(GameDriver driver) {
		// TODO Auto-generated method stub
		super.init(driver);

		for (int i = 0; i < MIN_OBJ_COUNT + rand.nextInt(MAX_OBJ_COUNT - MIN_OBJ_COUNT); i++) {
//			if (i % 2 == 0) {
//				// Make polygon
//				this.objects.add(new Poly(driver));
//			} else {
//				// Make circle
//			}
			this.levelObjects.add(new TestSquare(this, driver, i));
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

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 1400;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 1200;
	}

}

@SuppressWarnings("javadoc")
class TestSquare extends AbstractGameObject {

	int size;

	int i;
	AbstractPlane parent;

	Image img;

	public TestSquare(AbstractPlane plane, GameDriver driver, int i) {
		this.parent = plane;
		this.i = i;
		this.size = Math.max(10, ExamplePlane.rand.nextInt(ExamplePlane.MAX_OBJ_SIZE));
		this.x = ExamplePlane.rand.nextInt(parent.getWidth() - this.size);
		this.y = ExamplePlane.rand.nextInt(parent.getHeight() - this.size);

		Image img = driver.cache.fetch("C:\\Users\\Spencer Imbleau\\Pictures\\Designs\\headshot_512px.png");
		float sx = (float) this.size / img.getWidth();
		float sy = (float) this.size / img.getHeight();
		this.img = img.resize(sx, sy);
	}

	@Override
	public void update(GameDriver driver) {
		if (x + size < this.parent.getWidth()) {
			this.x++;
		}
		if (y + size < this.parent.getHeight()) {
			this.y++;
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		RenderRequest request;

		switch (this.i % 4) {
		case 0:
			request = new ImageRequest(img, RenderLevel.WORLD_BACKGROUND, (int) this.x, (int) this.y);
			break;
		case 1:
			request = new Line(this.size, this.size, 0xffff0000).asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x,
					(int) this.y);
			break;
		case 2:
			request = new Rectangle(this.size, this.size, 0xffff0000).asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x,
					(int) this.y);
			break;
		case 3:
			request = new Ellipse(size, size, 0xffff0000).asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x, (int) this.y);
			break;
		default:
			request = new Line(this.size, this.size, 0xff00ffff).asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x,
					(int) this.y);
			break;
		}

//		Ellipse ellipse = new Ellipse(size / 2, size / 2, 0xffff0000, true);
//		EllipseRequest request = new EllipseRequest(ellipse, (int) (size / 2), (int) (size / 2));
		renderer.stage(request);
	}

	@Override
	public int getWidth() {
		return this.size;
	}

	@Override
	public int getHeight() {
		return this.size;
	}

}
