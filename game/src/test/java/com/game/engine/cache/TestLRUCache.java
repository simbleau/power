package com.game.engine.cache;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.graphics.obj.Image;

/**
 * Tests an {@link LRUCache}
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public class TestLRUCache {

	/**
	 * Test {@link LRUCache#LRUCache(int)}.
	 */
	@Test
	public void testConstructor1() {
		// Test with bad amount
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new LRUCache(-1);
		});

		// Buffer for constructor tests
		LRUCache buf;

		// Test on a large amount
		buf = new LRUCache(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, buf.capacity);

		// Test on 0
		buf = new LRUCache(0);
		Assert.assertEquals(0, buf.capacity);
	}

	/**
	 * Test {@link LRUCache#contains(String)}.
	 */
	@Test
	public void testContains() {
		Cache buf = new LRUCache(1);

		// null never exists
		Assert.assertFalse(buf.contains("null"));

		// Test existing objects
		buf.put("test", null);
		Assert.assertTrue(buf.contains("test"));
		buf.remove("test");
		Assert.assertFalse(buf.contains("test"));
	}

	/**
	 * Test {@link LRUCache#fetch(String)}.
	 */
	@Test
	public void testFetch() {
		Cache buf = new LRUCache(1);

		// Fetching a null key should throw an exception
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			buf.fetch(null);
		});

		// Insert an image to the cache via fetch
		String testPath = Paths.get("src", "test", "resources", "pixel.png").toString();
		buf.fetch(testPath);

		// Ensure fetch loaded the image in
		Assert.assertTrue(buf.contains(testPath));
		// Ensure it's an image
		Assert.assertTrue(buf.get(testPath).value instanceof Image);
	}

	/**
	 * Test {@link LRUCache#get(String)}.
	 */
	@Test
	public void testGet() {
		Cache buf = new LRUCache(1);

		// Getting a null key should throw an exception
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			buf.get(null);
		});

		// Insert dummy data
		buf.put("test", null);

		// Test get
		CacheNode node = buf.get("test");
		Assert.assertNotNull(node);
		Assert.assertNull(node.value);

		// Ensure we got the reference by changing the value and re-retrieving it
		node.value = "value";
		node = buf.get("test");
		Assert.assertEquals("value", node.value);
	}

	/**
	 * Test {@link LRUCache#purge()}.
	 */
	@Test
	public void testPurge() {
		// Load buffer
		Cache buf = new LRUCache(3);
		buf.put("test1", null);
		buf.put("test2", null);
		buf.put("test3", null);

		// Test purge
		Assert.assertEquals(3, buf.size());
		buf.purge();
		Assert.assertEquals(0, buf.size());

		// Make sure they're able to be retrieved
		Assert.assertNull(buf.get("test1"));
		Assert.assertNull(buf.get("test2"));
		Assert.assertNull(buf.get("test3"));
	}

	/**
	 * Test {@link LRUCache#put(String, Object)}.
	 */
	@Test
	public void testPut() {
		Cache buf = new LRUCache(1);

		// Inserting a null key should throw an exception
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			buf.put(null, null);
		});

		// Test new object added
		buf.put("test", null);
		Assert.assertNull(buf.get("test").value);

		// Override the input
		buf.put("test", "value");
		Assert.assertEquals(buf.get("test").value, "value");
	}

	/**
	 * Test {@link LRUCache#remove(String)}.
	 */
	@Test
	public void testRemove() {
		Cache buf = new LRUCache(1);

		// Attempt to remove something that doesn't exist
		buf.remove("test");
		Assert.assertFalse(buf.contains("test"));

		// Attempt to remove something that does exist
		buf.put("test", null);
		buf.remove("test");
		Assert.assertFalse(buf.contains("test"));
	}

	/**
	 * Test {@link LRUCache#size()}.
	 */
	@Test
	public void testSize() {
		// Start buf with size 0
		Cache buf = new LRUCache(0);

		// Insert an element - It should drop immediately
		Assert.assertEquals(0, buf.size());
		buf.put("test", null);
		Assert.assertEquals(0, buf.size());

		// Make buf size to 1
		buf = new LRUCache(1);

		// Insert an element - It should be added
		Assert.assertEquals(0, buf.size());
		buf.put("test", null);
		Assert.assertEquals(1, buf.size());

		// Insert an element again - This time it should drop
		// e.g. size should still be 1
		buf.put("test2", null);
		Assert.assertEquals(1, buf.size());
	}

	/**
	 * Ensure the LRU data removal policy is followed.
	 */
	@Test
	public void testDataRemovalPolicy() {
		Cache buf = new LRUCache(1);

		// Test LRU data removal policy
		buf.put("test1", null);
		Assert.assertTrue(buf.contains("test1"));

		// LRU data police removal declares test1 will be popped for test2
		buf.put("test2", null);
		Assert.assertFalse(buf.contains("test1"));
		Assert.assertTrue(buf.contains("test2"));
	}
}
