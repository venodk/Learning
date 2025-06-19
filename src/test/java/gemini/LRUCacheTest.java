package gemini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LRUCacheTest {

    @Test
    void testPutAndGet() {
        LRUCache cache = new LRUCache(10);
        assertEquals(0, cache.size());
        assertEquals(10, cache.getCapacity());
        String key = "Fox";
        String value = "Fox jumped over the fence";
        cache.put(key, value);
        assertEquals(1, cache.size());
        assertEquals(10, cache.getCapacity());
        assertEquals(value, cache.get(key));
        assertEquals(1, cache.size());
        assertEquals(10, cache.getCapacity());
    }

    @Test
    void testPutNullKey() {
        LRUCache cache = new LRUCache(2);
        assertThrows(IllegalArgumentException.class, () -> cache.put(null, "value"));
    }

    @Test
    void testGetNonExistentKey() {
        LRUCache cache = new LRUCache(1);
        assertNull(cache.get("NonExistentKey"));
    }

    @Test
    void testCacheCreationWithInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new LRUCache(0));
    }

    @Test
    void testLRUEviction() {
        LRUCache cache = new LRUCache(2);
        String key1 = "Key1";
        String value1 = "value1";
        cache.put(key1, value1);

        String key2 = "Key2";
        String value2 = "value2";
        cache.put(key2, value2);

        String key3 = "Key3";
        String value3 = "value3";
        cache.put(key3, value3);

        assertEquals(2, cache.size());
        assertNull(cache.get(key1));
    }

    @Test
    void testGetNullKey() {
        LRUCache cache = new LRUCache(2);
        assertNull(cache.get(null));
    }

    // AI added tests
    @Test
    void testUpdateKey() {
        LRUCache cache = new LRUCache(2);
        String key1 = "Key1";
        String value1 = "value1";
        cache.put(key1, value1);
        String newValue1 = "newValue1";
        cache.put(key1, newValue1);
        assertEquals(newValue1, cache.get(key1));
        assertEquals(1, cache.size());
    }

    @Test
    void testLRUEvictionAfterGet() {
        LRUCache cache = new LRUCache(2);
        String key1 = "Key1";
        String value1 = "value1";
        cache.put(key1, value1);
        String key2 = "Key2";
        String value2 = "value2";
        cache.put(key2, value2);

        cache.get(key1); // Use key1, making it most recently used
        String key3 = "Key3";
        String value3 = "value3";
        cache.put(key3, value3);  // key2 should be evicted

        assertNull(cache.get(key2));
        assertEquals(value1, cache.get(key1));
        assertEquals(value3, cache.get(key3));
        assertEquals(2, cache.size());
    }
}
