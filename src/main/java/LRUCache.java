import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final int capacity;
    private final LinkedHashMap<K, V> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // Set accessOrder to true to maintain access order
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                // The anonymous class has access to the outer class's 'capacity' field
                return size() > LRUCache.this.capacity;
            }
        };
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public int size() {
        return cache.size();
    }

    public void clear() {
        cache.clear();
    }

    public static void main(String[] args) {
        // Example usage of LRUCache
        LRUCache<Integer, String> lruCache = new LRUCache<>(3);

        lruCache.put(1, "One");
        lruCache.put(2, "Two");
        lruCache.put(3, "Three");

        System.out.println("LRU Cache: " + lruCache);

        // Accessing element 2, moving it to the most recently used position
        lruCache.get(2);

        // Adding a new element 4, which will evict the least recently used element 1
        lruCache.put(4, "Four");

        System.out.println("LRU Cache after eviction: " + lruCache);
    }
}
