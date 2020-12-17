package com.game.demos.chunker;

import com.game.engine.camera.StationaryCamera;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.logger.PowerLogger;

@SuppressWarnings("javadoc")
public class VerboseStationaryCamera extends StationaryCamera {

	public VerboseStationaryCamera(double x, double y, int width, int height, double zoom) {
		super(x, y, width, height, zoom);
	}

	@Override
	public void lookAt(double x, double y) {
		super.lookAt(x, y);
		printFineCameraStatistics();
	}

	@Override
	public void lookAt(AbstractGameObject obj) {
		super.lookAt(obj);
		printFineCameraStatistics();
	}

	private void printFineCameraStatistics() {
		StringBuilder sb = new StringBuilder();

		// Title
		sb.append("[]< " + this.getClass().getSimpleName());
		sb.append(System.lineSeparator());

		// Origin
		sb.append("\tOrigin: ");
		sb.append(this.viewport.x());
		sb.append(',');
		sb.append(this.viewport.y());
		sb.append(System.lineSeparator());

		// Viewport
		sb.append("\tViewport: ");
		sb.append(this.viewport.x() + (int) (this.viewport.width() / this.zoom));
		sb.append(',');
		sb.append(this.viewport.y() + (int) (this.viewport.height() / this.zoom));
		sb.append(System.lineSeparator());

		// Chunks
		sb.append("\tChunks: ");
		sb.append(this.viewport.closestChunkRow());
		sb.append(',');
		sb.append(this.viewport.closestChunkColumn());
		sb.append("->");
		sb.append(this.viewport.furthestChunkRow());
		sb.append(',');
		sb.append(this.viewport.furthestChunkColumn());

		// Log it
		PowerLogger.LOGGER.fine(sb.toString());
	}

}
