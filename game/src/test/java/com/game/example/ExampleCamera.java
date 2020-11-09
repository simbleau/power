package com.game.example;

import java.awt.event.KeyEvent;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.input.MouseKeyboard;

@SuppressWarnings("javadoc")
public class ExampleCamera extends AbstractCamera {

	public ExampleCamera() {
		super(0, 0, 0.5);
	}

	@Override
	public void update(GameDriver driver) {
		// do nothing
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_UP)) {
			this.y -= 20;
		}
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_DOWN)) {
			this.y += 20;
		}
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_LEFT)) {
			this.x -= 20;
		}
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_RIGHT)) {
			this.x += 20;
		}

		if (driver.getInput().isKeyDown(KeyEvent.VK_UP)) {
			this.zoom += 0.03;
		}
		if (driver.getInput().isKeyDown(KeyEvent.VK_DOWN)) {
			this.zoom -= 0.03;
		}
//
//		System.out.println("camx:" + x + ", camy:" + y + " | scale: " + this.zoom);
	}

	@Override
	public double x() {
		return this.x;
	}

	@Override
	public double y() {
		return this.y;
	}

}
