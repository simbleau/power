package com.game.engine.rendering.common;

/**
 * The levels rendered in order. Render sorting is dictated by enum ordinal,
 * therefore the topmost enum constants here are the most forward facing
 * elements rendered rendered first, and the bottommost constants are rendered
 * last.
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public enum RenderLevel {

	/**
	 * Elements by plugins and extensions which overlay all UI components.
	 */
	UI_PLUGIN,

	/**
	 * Elements meant to be game-given guides and hints which overlay the UI.
	 */
	UI_OVERLAY,

	/**
	 * Elements belonging strictly to the user interface.
	 */
	UI,

	/**
	 * Elements by plugins and extensions which overlay all world components.
	 */
	WORLD_PLUGIN,

	/**
	 * Elements meant to be game-given hints and guides which overlay the game
	 * world.
	 */
	WORLD_OVERLAY,

	/**
	 * Elements which should be object foreground effects on the game world.
	 */
	WORLD_FOREGROUND,

	/**
	 * Elements and objects placed on the game world.
	 */
	WORLD_OBJECTS,

	/**
	 * Elements which should be object background effects on the game world.
	 */
	WORLD_BACKGROUND,

	/**
	 * Elements which should be ground effects on the game world, such as shadows.
	 */
	WORLD_GROUND,

	/**
	 * The only element rendered on this level should be the Plane level details and
	 * the "map" itself.
	 */
	PLANE,

	/**
	 * The lowest level.
	 */
	VOID;

}
