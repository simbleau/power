package com.game.demos.font;

import com.game.demos.objects.DemoLabel;
import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractChunkedPlane;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.Chunk;
import com.game.engine.rendering.common.AbstractRenderer;

@SuppressWarnings("javadoc")
public class FontTestPlane extends AbstractChunkedPlane {

	static final int OBJ_SIZE = 3 * Chunk.SIZE / 5;
	
	static final AbstractGameObject TEST_OBJECT = new DemoLabel(OBJ_SIZE);
	
	public FontTestPlane() {
		super(500, 500);
		this.levelObjects.add(TEST_OBJECT);
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);
		this.lookAtPlane(driver);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);
	}

	private void lookAtPlane(GameDriver driver) {
		AbstractCamera cam = driver.getDisplay().settings.getCamera();
		if (cam.viewport.width() > cam.viewport.height()) {
			cam.setZoom((double) cam.viewport.height() / (this.height));
		} else {
			cam.setZoom((double) cam.viewport.width() / (this.width));
		}
		cam.lookAt(this.width / 2, this.height / 2);
	}
}
