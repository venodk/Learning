package gemini;

import java.util.HashMap;

public class Cache {
    private final HashMap<String, String> store;

    public Cache() {
         this.store = new HashMap<>();
    }

    public String get(String key) {
        // null check is an AI suggestion.
        if (key == null) {
            return null;
        }
        return store.get(key);
    }

    public void put(String key, String value) {
        // null check is an AI suggestion.
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        store.put(key, value);
    }

    public void delete(String key) {
        // null check is an AI suggestion.
        if (key == null) {
            return;
        }
        store.remove(key);
    }

    public int size() {
        return store.size();
    }
}
