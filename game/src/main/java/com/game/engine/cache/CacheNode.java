package com.game.engine.cache;

/**
 * A node stored in a {@link Cache} which emulates a node from a Doubly-Linked
 * List data structure
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
public class CacheNode {

	/**
	 * The identifier for this cache node. Typically a path.
	 */
	public final String key;

	/**
	 * The object stored in this node.
	 */
	Object value;

	/**
	 * The object before this.
	 */
	CacheNode prev;

	/**
	 * The object after this.
	 */
	CacheNode next;

	/**
	 * Initialize a Cache Node.
	 *
	 * @param key   - the key to this cache node
	 * @param value - a stored value
	 */
	public CacheNode(String key, Object value) {
		this.key = key;
		this.value = value;
		this.prev = null;
		this.next = null;
	}

	/**
	 * @return whether this node has a previous node
	 */
	public boolean hasPrevious() {
		return this.prev != null;
	}

	/**
	 * @return whether this node has a next node
	 */
	public boolean hasNext() {
		return this.next != null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append('{');
		if (this.prev == null || this.next == null) {
			sb.append('[');
			if (this.prev == null) {
				sb.append('H');
			}
			if (this.next == null) {
				sb.append('T');
			}
			sb.append(']');
		}
		sb.append('"');
		sb.append(this.key);
		sb.append('"');
		sb.append(':');
		sb.append((this.value == null) ? "null" : this.value.getClass().getTypeName());
		sb.append('}');

		return sb.toString();
	}

}