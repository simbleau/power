package com.game.engine.rendering.cpu;

import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;

/**
 * A canvas handled by the {@link CPURenderer} for CPU rendering
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
public class CPUCanvas extends Canvas {

	/**
	 * The serial version.
	 *
	 * @see Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize a Canvas used by the renderer
	 *
	 * @param processor - the CPU rendering processor
	 */
	public CPUCanvas(CPUProcessor processor) {
		this.addComponentListener(new CanvasAdapter(this) {
			@Override
			public void componentResized(ComponentEvent e) {
				processor.resize(this.canvas.getWidth(), this.canvas.getHeight());
			}
		});
	}
}

/**
 * A {@link ComponentAdapter} which simply holds a reference to a
 * {@link CPUCanvas}
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
class CanvasAdapter extends ComponentAdapter {

	/**
	 * The CPU canvas reference
	 */
	CPUCanvas canvas;

	/**
	 * Initialize this adapter
	 *
	 * @param canvas - a CPU canvas to hold a reference to
	 */
	public CanvasAdapter(CPUCanvas canvas) {
		this.canvas = canvas;
	}

}
