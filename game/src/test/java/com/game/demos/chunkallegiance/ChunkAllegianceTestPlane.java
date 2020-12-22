package com.game.demos.chunkallegiance;

import com.game.demos.gfxtest.GFXTestPlane;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.Chunk;
import com.game.engine.graphics.obj.Line;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.graphics.request.LineRequest;
import com.game.engine.graphics.request.RectangleRequest;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

@SuppressWarnings("javadoc")
public class ChunkAllegianceTestPlane extends GFXTestPlane {

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);
		for (AbstractGameObject obj : this.levelObjects) {
			int dx = (obj.chunkRow() * Chunk.SIZE) + (Chunk.SIZE / 2) - (int) obj.x();
			int dy = (obj.chunkColumn() * Chunk.SIZE) + (Chunk.SIZE / 2) - (int) obj.y();
			LineRequest allegianceLineReq = new LineRequest(
					new Line(dx, dy, 0xff00ff00),
					RenderLevel.WORLD_OVERLAY,
					Integer.MAX_VALUE,
					(int) obj.x(), (int) obj.y());
			RectangleRequest boundingBoxReq = new RectangleRequest(
					new Rectangle(obj.width(), obj.height(), 0xff00ff00),
					RenderLevel.WORLD_OVERLAY,
					Integer.MAX_VALUE,
					(int) obj.x(), (int) obj.y());
			RectangleRequest originPointBoxReq = new RectangleRequest(
					new Rectangle(20, 20, 0xffff0000),
					RenderLevel.WORLD_OVERLAY,
					Integer.MAX_VALUE,
					(int) obj.x() - 10, (int) obj.y() -10);
			renderer.stage(allegianceLineReq);
			renderer.stage(boundingBoxReq);
			renderer.stage(originPointBoxReq);
		}
	}

}
