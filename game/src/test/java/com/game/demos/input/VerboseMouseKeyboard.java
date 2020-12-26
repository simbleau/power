package com.game.demos.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.game.engine.driver.GameDriver;
import com.game.engine.input.MouseKeyboard;
import com.game.engine.logger.PowerLogger;

@SuppressWarnings("javadoc")
public class VerboseMouseKeyboard extends MouseKeyboard {

	public VerboseMouseKeyboard(GameDriver driver) {
		super(driver);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		PowerLogger.LOGGER.info("Button " + e.getButton() + " was pressed.");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		PowerLogger.LOGGER.info("Button " + e.getButton() + " was released.");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		PowerLogger.LOGGER.info("Key " + e.getKeyCode() + " was pressed.");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		PowerLogger.LOGGER.info("Key " + e.getKeyCode() + " was released.");
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
		PowerLogger.LOGGER.info("Mouse wheel moved:  " + e.getWheelRotation());
	}

}
