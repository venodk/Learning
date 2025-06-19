import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

interface CacheReplacementPolicy<K, V> {
    void onPut(K key, V value, Map<K, V> cache);

    void onGet(K key, Map<K, V> cache);

    K evict(Map<K, V> cache);
}

class LRUCacheReplacementPolicy<K, V> implements CacheReplacementPolicy<K, V> {
    @Override
    public void onPut(K key, V value, Map<K, V> cache) {
        // No specific action needed for LRU on a put operation
    }

    @Override
    public void onGet(K key, Map<K, V> cache) {
        // No specific action needed for LRU on a get operation
    }

    @Override
    public K evict(Map<K, V> cache) {
        // Evict the least recently used entry based on access order
        return cache.keySet().iterator().next();
    }
}

class CacheLine<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public CacheLine(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class SetAssociativeCache<K, V> {
    private final int numSets;
    private final int setSize;
    private final Map<Integer, CacheLine<K, V>> sets;
    private final CacheReplacementPolicy<K, V> replacementPolicy;

    public SetAssociativeCache(int numSets, int setSize, CacheReplacementPolicy<K, V> replacementPolicy) {
        this.numSets = numSets;
        this.setSize = setSize;
        this.sets = new ConcurrentHashMap<>(); // Use ConcurrentHashMap for thread safety

        for (int i = 0; i < numSets; i++) {
            sets.put(i, new CacheLine<>(setSize));
        }

        this.replacementPolicy = replacementPolicy;
    }

    public synchronized V get(K key) {
        int setIndex = key.hashCode() % numSets;
        CacheLine<K, V> set = sets.get(setIndex);

        if (set.containsKey(key)) {
            replacementPolicy.onGet(key, set);
            return set.get(key);
        }

        return null;
    }

    public synchronized void put(K key, V value) {
        int setIndex = key.hashCode() % numSets;
        CacheLine<K, V> set = sets.get(setIndex);

        if (set.size() >= setSize) {
            K evictedKey = replacementPolicy.evict(set);
            set.remove(evictedKey);
        }

        set.put(key, value);
        replacementPolicy.onPut(key, value, set);
    }

    public static void main(String[] args) {
        SetAssociativeCache<Integer, String> cache = new SetAssociativeCache<>(4, 2, new LRUCacheReplacementPolicy<>());

        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        cache.put(4, "Four");

        System.out.println("Value for key 2: " + cache.get(2));
        cache.put(5, "Five");

        System.out.println("Value for key 1: " + cache.get(1));
    }
}

