package com.game.demos.input;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.game.demos.artifacts.DemoLabel;
import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.graphics.obj.fonts.Glyph;
import com.game.engine.input.MouseKeyboard;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

@SuppressWarnings("javadoc")
public class FontTestPlane extends AbstractPlane {

	private final DemoLabel characterLabel = new DemoLabel("?", 250);
	private final DemoLabel keycodeLabel = new DemoLabel("Type something", 250);

	private final DemoLabel ctrlLabel = new DemoLabel("CTRL", 100);
	private final DemoLabel shiftLabel = new DemoLabel("SHIFT", 100);
	private final DemoLabel altLabel = new DemoLabel("ALT", 100);

	private boolean ctrlDown = false;
	private boolean shiftDown = false;
	private boolean altDown = false;

	public FontTestPlane() {
		super(500, 500);

		this.characterLabel.setSpeed(0);
		this.keycodeLabel.setSpeed(0);
		this.ctrlLabel.setSpeed(0);
		this.shiftLabel.setSpeed(0);
		this.altLabel.setSpeed(0);

		this.objects.add(characterLabel);
		this.objects.add(keycodeLabel);
		this.objects.add(ctrlLabel);
		this.objects.add(shiftLabel);
		this.objects.add(altLabel);
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);
		this.realign(driver);
		this.lookAtPlane(driver);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
		this.shiftDown = false;
		this.ctrlDown = false;
		this.altDown = false;

		MouseKeyboard input = (MouseKeyboard) driver.getInput();
		for (int i = 0; i < MouseKeyboard.NUM_KEYS; i++) {
			if (input.isKeyDown(i)) {
				boolean shiftDown = input.isKeyActive(KeyEvent.VK_SHIFT);
				characterLabel.setText("'" + Glyph.characterOf(i, shiftDown) + "'");
				keycodeLabel.setText("Code: " + i);
				realign(driver);
				break;
			}
		}

		if (driver.getInput().isKeyActive(KeyEvent.VK_SHIFT)) {
			this.shiftDown = true;
		}
		if (driver.getInput().isKeyActive(KeyEvent.VK_CONTROL)) {
			this.ctrlDown = true;
		}
		if (driver.getInput().isKeyActive(KeyEvent.VK_ALT)) {
			this.altDown = true;
		}

	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);

		if (this.shiftDown) {
			renderer.stage(new Rectangle(shiftLabel.width(), shiftLabel.height(), 0xffff0000)
					.asRequest(RenderLevel.VOID, shiftLabel.position.x.asInt(), shiftLabel.position.y.asInt()));
		}
		if (this.ctrlDown) {
			renderer.stage(new Rectangle(ctrlLabel.width(), ctrlLabel.height(), 0xffff0000)
					.asRequest(RenderLevel.VOID, ctrlLabel.position.x.asInt(), ctrlLabel.position.y.asInt()));
		}
		if (this.altDown) {
			renderer.stage(new Rectangle(altLabel.width(), altLabel.height(), 0xffff0000)
					.asRequest(RenderLevel.VOID, altLabel.position.x.asInt(), altLabel.position.y.asInt()));
		}
	}

	private void lookAtPlane(GameDriver driver) {
		AbstractCamera cam = driver.getDisplay().getRenderer().getCamera();
		if (cam.viewport.width() > cam.viewport.height()) {
			cam.setZoom((double) cam.viewport.height() / (this.height));
		} else {
			cam.setZoom((double) cam.viewport.width() / (this.width));
		}
		cam.lookAt(this.width / 2, this.height / 2);
	}

	private void realign(GameDriver driver) {
		// Vertical objects
		List<AbstractGameObject> vObjects = new ArrayList<>();
		vObjects.add(this.characterLabel);
		vObjects.add(this.keycodeLabel);
		int vPadding = 10;
		int totalHeight = vObjects.stream()
				.mapToInt(AbstractGameObject::height)
				.map(height -> height + vPadding)
				.sum();
		int yOffset = 0;
		for (int i = 0; i < vObjects.size(); i++) {
			AbstractGameObject obj = vObjects.get(i);
			int offsetX = (this.width / 2) - (obj.width() / 2);
			int offsetY = (this.height / 2) - (totalHeight / 2) + yOffset;
			yOffset += obj.height() + vPadding;
			obj.move(driver, offsetX, offsetY);
		}

		// Vertical objects
		List<AbstractGameObject> hObjects = new ArrayList<>();
		hObjects.add(this.ctrlLabel);
		hObjects.add(this.shiftLabel);
		hObjects.add(this.altLabel);
		int hPadding = 10;
		int totalWidth = hObjects.stream()
				.mapToInt(AbstractGameObject::width)
				.map(width -> width + hPadding)
				.sum();
		int xOffset = 0;
		for (int i = 0; i < hObjects.size(); i++) {
			AbstractGameObject obj = hObjects.get(i);
			int offsetX = (this.width / 2) - (totalWidth / 2) + xOffset;
			int offsetY = this.height - obj.height();
			xOffset += obj.width() + vPadding;
			obj.move(driver, offsetX, offsetY);
		}

	}
}
