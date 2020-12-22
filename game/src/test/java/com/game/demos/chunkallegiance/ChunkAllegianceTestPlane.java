package com.game.demos.chunkallegiance;

import com.game.demos.gfxtest.GFXTestPlane;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.Chunk;
import com.game.engine.graphics.obj.Label;
import com.game.engine.graphics.obj.Line;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.graphics.obj.fonts.Font;
import com.game.engine.graphics.obj.fonts.mock.MockFonts;
import com.game.engine.graphics.request.LabelRequest;
import com.game.engine.graphics.request.LineRequest;
import com.game.engine.graphics.request.RectangleRequest;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

@SuppressWarnings("javadoc")
public class ChunkAllegianceTestPlane extends GFXTestPlane {

	/**
	 * The color for overlays in this demo.
	 */
	private static final int OVERLAY_COLOR = 0xff00ff00;

	/**
	 * The font to display text in this demo
	 */
	private static final Font DEMO_FONT = MockFonts.FONT_1;

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);

		// Display attachments between objects and chunks
		for (AbstractGameObject obj : this.levelObjects) {
			int dx = (obj.chunkColumn() * Chunk.SIZE) + (Chunk.SIZE / 2) - (int) obj.x();
			int dy = (obj.chunkRow() * Chunk.SIZE) + (Chunk.SIZE / 2) - (int) obj.y();
			LineRequest allegianceLineReq = new LineRequest(
					new Line(dx, dy, OVERLAY_COLOR),
					RenderLevel.WORLD_OVERLAY,
					Integer.MAX_VALUE,
					(int) obj.x(), (int) obj.y());
			RectangleRequest boundingBoxReq = new RectangleRequest(
					new Rectangle(obj.width(), obj.height(), OVERLAY_COLOR),
					RenderLevel.WORLD_OVERLAY,
					Integer.MAX_VALUE,
					(int) obj.x(), (int) obj.y());
			RectangleRequest originPointBoxReq = new RectangleRequest(
					new Rectangle(20, 20, OVERLAY_COLOR),
					RenderLevel.WORLD_OVERLAY,
					Integer.MAX_VALUE,
					(int) obj.x() - 10, (int) obj.y() - 10);
			renderer.stage(allegianceLineReq);
			renderer.stage(boundingBoxReq);
			renderer.stage(originPointBoxReq);
		}

		// Display chunk co-ordinates
		for (Chunk c : this.chunker.viewableChunks()) {
			Label chunkLabel = new Label(DEMO_FONT, c.row + "," + c.column, OVERLAY_COLOR);
			LabelRequest chunkLabelRequest = new LabelRequest(
					chunkLabel,
					RenderLevel.WORLD_BACKGROUND,
					Integer.MAX_VALUE,
					c.x() + (Chunk.SIZE / 2) - (chunkLabel.getWidth() / 2),
					c.y() + (Chunk.SIZE / 2) - (chunkLabel.getHeight() / 2));
			renderer.stage(chunkLabelRequest);
		}
	}

}
