package gemini;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache {
    private final LinkedHashMap<String, String> store;
    private final int capacity;

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than 0.");
        }
        this.capacity = capacity;
        this.store = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> entry) {
                return size() > capacity;
            }
        };
    }

    public String get(String key) {
        if (key == null) {
            return null;
        }
        return store.get(key);
    }

    public void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        store.put(key, value);
    }

    public int size() {
        return store.size();
    }

    public int getCapacity() {
        return capacity;
    }
}
