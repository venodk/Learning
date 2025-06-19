package gemini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {

    @Test
    void testPutAndGet() {
        Cache cache = new Cache();
        String key = "Fox";
        String value = "Fox jumped over the fence";
        assertEquals(0, cache.size());
        cache.put(key, value);
        assertEquals(1, cache.size());
        assertEquals(value, cache.get(key));
    }

    @Test
    void testPutNullKey() {
        Cache cache = new Cache();
        assertThrows(IllegalArgumentException.class, () -> cache.put(null, "value"));
    }

    @Test
    void testGetNonExistentKey() {
        Cache cache = new Cache();
        assertNull(cache.get("NonExistentKey"));
    }

    @Test
    void testPutWithDuplicateKey() {
        Cache cache = new Cache();
        String key = "Fox";
        String value = "Fox jumped over the fence";
        assertEquals(0, cache.size());
        cache.put(key, value);
        assertEquals(1, cache.size());
        String updatedValue = "new value";
        cache.put(key, updatedValue);
        assertEquals(1, cache.size());
        assertEquals("new value", cache.get(key));
    }

    @Test
    void testDeleteKey() {
        Cache cache = new Cache();
        String key = "Fox";
        String value = "Fox jumped over the fence";
        cache.put(key, value);
        assertEquals(value, cache.get(key));
        cache.delete(key);
        assertNull(cache.get(key));
    }

    @Test
    void testDeleteNonExistentKey() {
        Cache cache = new Cache();
        assertEquals(0, cache.size());
        cache.delete("NonExistentKey");
        assertEquals(0, cache.size());
    }

    @Test
    void testGetNullKey() {
        Cache cache = new Cache();
        assertNull(cache.get(null));
    }

    @Test
    void testDeleteNullKey() {
        Cache cache = new Cache();
        cache.delete(null); // No exception, as per the current implementation
        assertEquals(0, cache.size());
    }
}
