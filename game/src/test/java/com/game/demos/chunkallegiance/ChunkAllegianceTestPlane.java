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
	private static final Font DEMO_FONT = MockFonts.FONT_256;

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);

		// Display attachments between objects and chunks
		for (Chunk c : this.chunker.viewableChunks()) {
			for (AbstractGameObject obj : c.objects()) {
				int dx = (obj.position.chunkColumn() * Chunk.SIZE) + (Chunk.SIZE / 2) - obj.position.x.asInt();
				int dy = (obj.position.chunkRow() * Chunk.SIZE) + (Chunk.SIZE / 2) - obj.position.y.asInt();
				LineRequest allegianceLineReq = new LineRequest(
						new Line(dx, dy, OVERLAY_COLOR),
						RenderLevel.WORLD_OVERLAY,
						Integer.MAX_VALUE,
						obj.position.x.asInt(), obj.position.y.asInt());
				RectangleRequest boundingBoxReq = new RectangleRequest(
						new Rectangle(obj.width(), obj.height(), OVERLAY_COLOR),
						RenderLevel.WORLD_OVERLAY,
						Integer.MAX_VALUE,
						obj.position.x.asInt(), obj.position.y.asInt());
				RectangleRequest originPointBoxReq = new RectangleRequest(
						new Rectangle(20, 20, OVERLAY_COLOR),
						RenderLevel.WORLD_OVERLAY,
						Integer.MAX_VALUE,
						obj.position.x.asInt() - 10, obj.position.y.asInt() - 10);
				renderer.stage(allegianceLineReq);
				renderer.stage(boundingBoxReq);
				renderer.stage(originPointBoxReq);
			}
		}

		// Display chunk co-ordinates
		for (Chunk c : this.chunker.viewableChunks()) {
			Label chunkLabel = new Label(DEMO_FONT, "r" + c.row + "c" + c.column, OVERLAY_COLOR);
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
