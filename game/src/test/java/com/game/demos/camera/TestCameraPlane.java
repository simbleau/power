package com.game.demos.camera;

import java.awt.event.KeyEvent;

import com.game.demos.artifacts.DemoEllipse;
import com.game.engine.camera.AbstractCamera;
import com.game.engine.camera.DevCamera;
import com.game.engine.camera.StationaryCamera;
import com.game.engine.camera.TargetCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractChunkedPlane;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Label;
import com.game.engine.graphics.obj.fonts.Font;
import com.game.engine.graphics.obj.fonts.mock.MockFonts;
import com.game.engine.logger.PowerLogger;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

/**
 * A demo which shows switching between cameras.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestCameraPlane extends AbstractChunkedPlane {

	/**
	 * The width of the plane.
	 */
	private static final int PLANE_WIDTH = 5000;

	/**
	 * The height of the plane.
	 */
	private static final int PLANE_HEIGHT = 5000;

	/**
	 * All cameras to cycle through.
	 */
	private static final AbstractCamera CAMERAS[] = {
			new DevCamera(0, 0, 0, 0, 1), // 0
			new StationaryCamera(0, 0, 0, 0, 1), // 1
			new TargetCamera(0, 0, 0, 0, 1) // 2
	};

	/**
	 * Make a test object in our plane just for the target camera to follow.
	 */
	private static final AbstractGameObject TEST_OBJECT = new DemoEllipse(100);
	static {
		// The target camera will follow our test object
		TargetCamera tc = (TargetCamera) CAMERAS[2];
		tc.setTarget(TEST_OBJECT);
	}

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
	 * The label which shows the camera type used.
	 */
	private static final Label TYPE_LABEL = new Label(FONT, LABEL_COLOR);
	/**
	 * The label which shows the camera's origin.
	 */
	private static final Label ORIGIN_LABEL = new Label(FONT, LABEL_COLOR);
	/**
	 * The label which shows the camera's viewport.
	 */
	private static final Label VIEWPORT_LABEL = new Label(FONT, LABEL_COLOR);
	/**
	 * The label which shows the mouse's position.
	 */
	private static final Label LOCAL_MOUSE_LABEL = new Label(FONT, LABEL_COLOR);
	/**
	 * The label which shows the mouse's position on the plane.
	 */
	private static final Label PLANE_MOUSE_LABEL = new Label(FONT, LABEL_COLOR);

	private static int cameraIndex = 0;

	/**
	 * Initialize the plane.
	 */
	public TestCameraPlane() {
		super(PLANE_WIDTH, PLANE_HEIGHT);
		this.objects.add(TEST_OBJECT);
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);

		// Move the test object to the middle of the plane
		TEST_OBJECT.move(driver, this.width / 2, this.height / 2);

		// Start with the first camera
		switchCamera(driver, 0);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);

		// Cycle cameras if asked
		if (driver.getInput().isKeyUp(KeyEvent.VK_RIGHT)) {
			switchCamera(driver, Math.min(CAMERAS.length - 1, cameraIndex + 1));
		}
		if (driver.getInput().isKeyUp(KeyEvent.VK_LEFT)) {
			switchCamera(driver, Math.max(0, cameraIndex - 1));
		}

		// Update the labels
		this.updateOriginLabel();
		this.updateViewportLabel();
		this.updateLocalMouseLabel(driver);
		this.updatePlaneMouseLabel(driver);
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);

		// Stage each label as a UI element
		RenderRequest typeRequest = TYPE_LABEL.asRequest(RenderLevel.UI_OVERLAY, 0, PADDING * 0 + FONT.size * 0);
		RenderRequest originRequest = ORIGIN_LABEL.asRequest(RenderLevel.UI_OVERLAY, 0, PADDING * 1 + FONT.size * 1);
		RenderRequest viewportRequest = VIEWPORT_LABEL.asRequest(RenderLevel.UI_OVERLAY, 0,
				PADDING * 2 + FONT.size * 2);
		RenderRequest localMouseRequest = LOCAL_MOUSE_LABEL.asRequest(RenderLevel.UI_OVERLAY, 0,
				PADDING * 3 + FONT.size * 3);
		RenderRequest planeMouseRequest = PLANE_MOUSE_LABEL.asRequest(RenderLevel.UI_OVERLAY, 0,
				PADDING * 4 + FONT.size * 4);

		renderer.stage(typeRequest);
		renderer.stage(originRequest);
		renderer.stage(viewportRequest);
		renderer.stage(localMouseRequest);
		renderer.stage(planeMouseRequest);
	}

	/**
	 * Helper method to transfer cameras seamlessly on demand.
	 *
	 * @param driver         - the game driver
	 * @param newCameraIndex - the camera index to transfer to
	 */
	private void switchCamera(GameDriver driver, int newCameraIndex) {
		AbstractCamera nextCam = CAMERAS[newCameraIndex];

		// Log the event
		PowerLogger.LOGGER.info("Transferring to " + nextCam.getClass().getSimpleName());
		// Transfer camera
		driver.getDisplay().getRenderer().transferCamera(nextCam);
		cameraIndex = newCameraIndex;

		// Update camera type label
		this.updateTypeLabel();
	}

	/**
	 * Helper method to update the camera type label.
	 */
	private void updateTypeLabel() {
		String labelText = "[]< " + CAMERAS[cameraIndex].getClass().getSimpleName();
		TYPE_LABEL.setText(labelText);
	}

	/**
	 * Helper method to update the camera origin label.
	 */
	private void updateOriginLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append("Origin: ");
		sb.append(String.format("%.2f", CAMERAS[cameraIndex].viewport.x()));
		sb.append(',');
		sb.append(String.format("%.2f", CAMERAS[cameraIndex].viewport.y()));
		ORIGIN_LABEL.setText(sb.toString());
	}

	/**
	 * Helper method to update the camera viewport label.
	 */
	private void updateViewportLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append("Viewport: ");
		sb.append(String.format("%.2f", CAMERAS[cameraIndex].viewport.x() + CAMERAS[cameraIndex].viewport.width()));
		sb.append(',');
		sb.append(String.format("%.2f", CAMERAS[cameraIndex].viewport.x() + CAMERAS[cameraIndex].viewport.height()));
		VIEWPORT_LABEL.setText(sb.toString());
	}

	/**
	 * Helper method to update the local mouse position label.
	 */
	private void updateLocalMouseLabel(GameDriver driver) {
		StringBuilder sb = new StringBuilder();
		sb.append("Local Mouse: (");
		sb.append(driver.getInput().x());
		sb.append(',');
		sb.append(driver.getInput().y());
		sb.append(')');
		LOCAL_MOUSE_LABEL.setText(sb.toString());
	}

	/**
	 * Helper method to update the plane mouse position label.
	 */
	private void updatePlaneMouseLabel(GameDriver driver) {
		StringBuilder sb = new StringBuilder();
		sb.append("Plane Mouse: (");
		sb.append(driver.getInput().planeX());
		sb.append(',');
		sb.append(driver.getInput().planeY());
		sb.append(')');
		PLANE_MOUSE_LABEL.setText(sb.toString());
	}

}
