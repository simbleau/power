package com.game.demos.physics2D;

import java.util.logging.Level;

import com.game.demos.artifacts.DemoPhysicalCircle;
import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.game.Chunk;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Label;
import com.game.engine.graphics.obj.fonts.Font;
import com.game.engine.graphics.obj.fonts.mock.MockFonts;
import com.game.engine.input.MouseKeyboard;
import com.game.engine.logger.PowerLogger;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

/**
 * A demo which shows physics.
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public class PhysicsPlane extends AbstractPlane {

	/**
	 * The maximum size of objects in this physics plane.
	 */
	static final int OBJ_SIZE = 3 * Chunk.SIZE / 5;

	/**
	 * The amount of chunks this plane is wide.
	 */
	static final int CHUNKS_WIDE = 3;

	/**
	 * The amount of chunks this plane is tall.
	 */
	static final int CHUNKS_HIGH = 3;

	/**
	 * Preset objects which can be stamped onto the plane by the user.
	 */
	private static final AbstractGameObject STAMPS[] = { new DemoPhysicalCircle(OBJ_SIZE) // Object 0
	};

	/**
	 * Padding between UI labels.
	 */
	private static final int PADDING = 5;

	/**
	 * The font used for our labels.
	 */
	private static final Font FONT = MockFonts.FONT_256;
	/**
	 * The color for our labels.
	 */
	private static final int LABEL_COLOR = 0xff00ff00;

	/**
	 * The label which shows the amount of type of stamp the mouse is creating.
	 */
	private static final Label STAMP_LABEL = new Label(FONT, LABEL_COLOR);

	/**
	 * The label which shows the amount of objects in the plane.
	 */
	private static final Label OBJECTS_LABEL = new Label(FONT, LABEL_COLOR);

	/**
	 * The current stmap index.
	 */
	private static int stampIndex = 0;

	/**
	 * Initialize the plane.
	 */
	public PhysicsPlane() {
		super(CHUNKS_WIDE * Chunk.SIZE, CHUNKS_HIGH * Chunk.SIZE);
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);

		// Initialize all builder objects
		for (AbstractGameObject o : STAMPS) {
			o.init(driver);
		}

		this.updateStampLabel();
		this.updateObjectsLabel();

		this.lookAtPlane(driver);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);

		// Cycle stamp object if asked
		switch (driver.getInput().getScroll()) {
		case MouseKeyboard.SCROLL.UP:
			switchStamp(driver, Math.min(STAMPS.length - 1, stampIndex + 1));
			break;
		case MouseKeyboard.SCROLL.DOWN:
			switchStamp(driver, Math.max(0, stampIndex - 1));
			break;
		case MouseKeyboard.SCROLL.IDLE:
			break;
		default:
			PowerLogger.LOGGER.warning("This shouldn't happen.");
			break;
		}

		// Add object is asked
		if (driver.getInput().isButtonDown(MouseKeyboard.BUTTONS.LEFT_CLICK)) {
			int pX = driver.getInput().planeX();
			int pY = driver.getInput().planeY();
			if (pX >= 0 && pX < this.width && pY >= 0 && pY < this.height) {
				try {
					stampObject(driver, pX, pY);
				} catch (CloneNotSupportedException e) {
					PowerLogger.LOGGER.log(Level.WARNING, "Clone not supported", e);
				}
			} else {
				PowerLogger.LOGGER.info(
						"Could not add object to the plane at (" + pX + ", " + pY + ") because that is out of bounds.");
			}
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);

		// Stage each label as a UI element
		RenderRequest stampRequest = STAMP_LABEL.asRequest(RenderLevel.UI_OVERLAY, 0, PADDING * 0 + FONT.size * 0);
		RenderRequest objsRequest = OBJECTS_LABEL.asRequest(RenderLevel.UI_OVERLAY, 0, PADDING * 1 + FONT.size * 1);
		renderer.stage(stampRequest);
		renderer.stage(objsRequest);

		// TODO Show tooltips
	}

	/**
	 * Helper method to transfer the type of object stamped
	 *
	 * @param driver         - the game drivere of
	 * @param newObjectIndex - the object index to switch to
	 */
	private void switchStamp(GameDriver driver, int newObjectIndex) {
		AbstractGameObject nextObject = STAMPS[newObjectIndex];

		// Log the event
		PowerLogger.LOGGER.info("Switching stamp to " + nextObject.getClass().getSimpleName());
		// Transfer stamps
		stampIndex = newObjectIndex;

		// Update camera type label
		this.updateStampLabel();
	}

	/**
	 * TODO: Document
	 */
	@SuppressWarnings("javadoc")
	private void stampObject(GameDriver driver, int planeX, int planeY) throws CloneNotSupportedException {
		PowerLogger.LOGGER.info("Adding " + STAMPS[stampIndex].getClass().getSimpleName() + " to the plane at ("
				+ planeX + ", " + planeY + ").");

		AbstractGameObject obj = STAMPS[stampIndex].clone();
		obj.init(driver);
		obj.move(driver, planeX, planeY);
		obj.update(driver);
		this.addGameObject(obj);

		this.updateObjectsLabel();
	}

	/**
	 * Helper method to update the stamp label.
	 */
	private void updateStampLabel() {
		String labelText = "Stamp: " + STAMPS[stampIndex].getClass().getSimpleName();
		STAMP_LABEL.setText(labelText);
	}

	/**
	 * Helper method to update the object count label.
	 */
	private void updateObjectsLabel() {
		String labelText = "Object count: " + this.objects.size();
		OBJECTS_LABEL.setText(labelText);
	}

	/**
	 * Adjust the zoom to look at the plane as an entity.
	 *
	 * @param driver - the game driver
	 */
	private void lookAtPlane(GameDriver driver) {
		AbstractCamera cam = driver.getDisplay().getRenderer().getCamera();
		if (cam.viewport.width() > cam.viewport.height()) {
			cam.setZoom((double) cam.viewport.height() / (this.height));
		} else {
			cam.setZoom((double) cam.viewport.width() / (this.width));
		}
		cam.lookAt(this.width / 2, this.height / 2);
	}
}
