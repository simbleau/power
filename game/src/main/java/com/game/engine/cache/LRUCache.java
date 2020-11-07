package com.game.engine.cache;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import com.game.engine.graphics.obj.Image;

/**
 * A cache used for retrieving resources. This cache uses the LRU (least
 * recently used) algorithm by combining a hashmap and doubly linked list.
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
public class LRUCache implements Cache {

	/**
	 * The most recently used item from cache
	 */
	protected CacheNode head;

	/**
	 * The least recently used item from cache
	 */
	protected CacheNode tail;

	/**
	 * The cache map. Maps keys to cache nodes.
	 */
	protected ConcurrentHashMap<String, CacheNode> map;

	/**
	 * The capacity for the cache
	 */
	protected int capacity;

	/**
	 * Initialize a cache
	 *
	 * @param capacity - the capacity for this cache
	 * @throws IllegalArgumentException capacity must be positive
	 */
	public LRUCache(int capacity) throws IllegalArgumentException {
		if (capacity < 0) {
			throw new IllegalArgumentException("Capacity must be > 0, received: " + capacity);
		}
		this.head = null;
		this.tail = null;
		this.map = new ConcurrentHashMap<String, CacheNode>(capacity);
		this.capacity = capacity;
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public void purge() {
		this.head = null;
		this.tail = null;
		this.map.clear();
	}

	@Override
	public void put(String key, Object obj) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key must not be null");
		}
		CacheNode node = new CacheNode(key, obj);
		put(node);
	}

	@Override
	public void remove(String key) {
		if (key == null) {
			return;
		}
		if (contains(key)) {
			CacheNode node = get(key);
			remove(node);
		}
	}

	@Override
	public boolean contains(String key) {
		if (key == null) {
			return false;
		}
		return this.map.containsKey(key);
	}

	@Override
	public CacheNode get(String key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key must not be null");
		}
		return this.map.get(key);
	}

	@Override
	public Image fetch(String path) throws IllegalArgumentException {
		CacheNode fetched = get(path);
		if (fetched == null) {
			// Cache miss
			return load(path);
		} else {
			// Cache hit
			if (fetched.value instanceof Image) {
				return (Image) fetched.value;
			} else {
				remove(fetched);
				return load(path);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (CacheNode node : this.map.values()) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(node);
		}
		sb.insert(0, '{');
		sb.insert(sb.length(), '}');

		return sb.toString();
	}

	/**
	 * Put a node into the cache
	 *
	 * @param node - the node to put
	 */
	protected synchronized void put(CacheNode node) {
		if (this.head == null) {
			// Cache contains no nodes
			this.head = node;
			this.tail = node;
		} else {
			// Cache contains nodes
			node.next = this.head;
			this.head.prev = node;
			this.head = node;
		}

		// Load into map
		this.map.put(node.key, node);

		// Eviction policy enforcement
		if (size() > this.capacity) {
			pop();
		}
	}

	/**
	 * Remove a node from the cache. This should be done in the event of a cache
	 * error or corrupted node, not to enforce an eviction policy.
	 *
	 * @param node - the node to remove
	 */
	protected synchronized void remove(CacheNode node) {
		this.map.remove(node.key);

		if (this.head == node && this.tail == node) {
			// Is the only element
			this.head = null;
			this.tail = null;
		} else if (this.head == node) {
			// Is a head node
			// Must have next nodes
			CacheNode next = node.next;
			next.prev = null;
			this.head = next;
		} else if (this.tail == node) {
			// Is a tail node
			// Must have previous nodes
			CacheNode previous = node.prev;
			previous.next = null;
			this.tail = previous;
		} else {
			// Node must have previous and next nodes, as it is not head or tail
			CacheNode previous = node.prev;
			CacheNode next = node.next;
			previous.next = next;
			next.prev = previous;
		}

	}

	/**
	 * Remove the least recently used node
	 */
	protected synchronized void pop() {
		if (this.tail != null) {
			remove(this.tail);
		}
	}

	/**
	 * A blocking request to retrieve an image. If the request succeeds, the image
	 * will be cached.
	 *
	 * @param path - the path to retrieve
	 * @return the image retrieved from the path, or null
	 */
	protected Image load(String path) {
		try {
			BufferedImage buf = null;
			URL url = this.getClass().getResource(path);
			if (url != null) {
				// Path refers to a resource
				buf = ImageIO.read(url);
			} else {
				// Path might refer to a file on the file system/local cache
				// Try to retrieve it from the file system
				File file = new File(path);
				if (file.exists()) {
					buf = ImageIO.read(file);
				} else {
					return null;
				}
			}

			Image result = new Image(buf);
			CacheNode node = new CacheNode(path, result);
			put(node);
			return result;
		} catch (Exception e) {
			return null;
		}
	}
}
