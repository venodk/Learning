import java.util.HashMap;
import java.util.Map;

public class LRUCacheImpl<K, V> {
    private final Map<K, Entry<K, V>> map;
    private final Entry<K, V> head, tail;
    private final int capacity;

    public LRUCacheImpl(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        head = new Entry<>(null, null);
        tail = new Entry<>(null, null);
        head.after = tail;
        tail.before = head;
    }

    public V get(K key) {
        Entry<K, V> entry = map.get(key);
        if (null == entry) return null;

        if (head.after != entry) {
            removeFromList(entry);
            addToFront(entry);
        }
        return entry.val;
    }

    public void put(K key, V value) {
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            entry.val = value;
            if (head.after != entry) {
                removeFromList(entry);
                addToFront(entry);
            }
        } else {
            entry = new Entry<>(key, value);
            addToFront(entry);
            map.put(key, entry);
            if (map.size() > capacity) {
                Entry<K, V> toRemove = tail.before;
                removeFromList(toRemove);
                map.remove(toRemove.key);
            }
        }
    }

    private void addToFront(Entry<K, V> entry) {
        Entry<K, V> next = head.after;
        next.before = entry;
        entry.after = next;
        entry.before = head;
        head.after = entry;
    }

    private void removeFromList(Entry<K, V> entry) {
        Entry<K, V> prev = entry.before;
        Entry<K, V> next = entry.after;
        prev.after = next;
        next.before = prev;
        entry.after = null;
        entry.before = null;
    }

    private static class Entry<K, V> {
        K key;
        V val;
        Entry<K, V> before, after;
        public Entry(K key, V val) {
            this.key = key;
            this.val = val;
            before = null;
            after = null;
        }
    }
}
