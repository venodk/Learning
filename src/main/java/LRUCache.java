import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        // Set accessOrder to true to maintain access order
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // Remove the eldest entry when the size exceeds the specified capacity
        return size() > capacity;
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
