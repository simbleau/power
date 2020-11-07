package com.game.engine.cache;

import com.game.engine.graphics.obj.Image;

/**
 * An auxiliary memory from which high-speed retrieval is possible
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
public interface Cache {

	/**
	 * @return the size of the cache, measured in objects
	 */
	public int size();

	/**
	 * Clear the entire cache.
	 */
	public void purge();

	/**
	 * Put an object into the cache, if allowed.
	 *
	 * @param key - the key to put into the cache
	 * @param obj - the value to put into the cache
	 * @throws IllegalArgumentException key must not be null
	 */
	public void put(String key, Object obj) throws IllegalArgumentException;

	/**
	 * Remove an object from the cache if it exists.
	 *
	 * @param key - the key to remove from the cache
	 */
	public void remove(String key);

	/**
	 * Determines if the cache contains an object with the given key.
	 *
	 * @param key - the key to search
	 * @return true if the cache contains the given key, false otherwise
	 */
	public boolean contains(String key);

	/**
	 * Returns a {@link CacheNode} from a given key, if it exists.
	 *
	 * @param key - the key to search
	 * @return a {@link CacheNode} or null
	 * @throws IllegalArgumentException key must not be null
	 */
	public CacheNode get(String key) throws IllegalArgumentException;

	/**
	 * A request to fetch an image from cache. If the image does not exist in cache,
	 * this will make a blocking request to retrieve the image.
	 *
	 * @param key - the key to retrieve
	 * @return an image, or null if the image is not in cache and unable to be
	 *         retrieved
	 * @throws IllegalArgumentException key must not be null
	 */
	public Image fetch(String key) throws IllegalArgumentException;
}
