package com.game.demos.gfxtest;

import java.util.Random;

import com.game.demos.artifacts.DemoEllipse;
import com.game.demos.artifacts.DemoImage;
import com.game.demos.artifacts.DemoLabel;
import com.game.demos.artifacts.DemoLine;
import com.game.demos.artifacts.DemoRectangle;
import com.game.demos.artifacts.DemoUpdatingImage;
import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractChunkedPlane;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.Chunk;
import com.game.engine.rendering.common.AbstractRenderer;

@SuppressWarnings("javadoc")
public class GFXTestPlane extends AbstractChunkedPlane {

	static Random rng = new Random();

	private static final int SIZE = 3 * Chunk.SIZE / 5;

	private static final AbstractGameObject OBJECTS[] = { new DemoUpdatingImage(SIZE), new DemoImage(SIZE),
			new DemoEllipse(SIZE), new DemoRectangle(SIZE), new DemoLine(SIZE), new DemoLabel(SIZE) };

	public GFXTestPlane() {
		super(Chunk.SIZE * OBJECTS.length, Chunk.SIZE * OBJECTS.length);

		// Add graphic objects on the map
		for (AbstractGameObject obj : OBJECTS) {
			this.levelObjects.add(obj);
		}
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);

		AbstractCamera cam = driver.getDisplay().settings.getCamera();
		if (cam.viewport.width() > cam.viewport.height()) {
			cam.setZoom((double) cam.viewport.height() / (this.height + 100));
		} else {
			cam.setZoom((double) cam.viewport.width() / (this.width + 100));
		}
		cam.lookAt(this.width / 2, this.height / 2);

		// Move all objects into diagonal position
		for (int i = 0; i < OBJECTS.length; i++) {
			AbstractGameObject obj = OBJECTS[i];
			int offsetX = (Chunk.SIZE / 2) - (obj.width() / 2);
			int offsetY = (Chunk.SIZE / 2) - (obj.height() / 2);
			obj.move(driver, (Chunk.SIZE * i) + offsetX, (Chunk.SIZE * i) + offsetY);
		}
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);
	}

}
