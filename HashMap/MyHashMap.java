package HashMap;

public class MyHashMap<K, V> {
    private Object[] hashArray;
    private int size;
    private MyHashSet<K> keySet;

    public MyHashMap() {
        size = 0;
        hashArray = new Object[100];
        keySet = new MyHashSet<K>();
    }

    public V put(K key, V value) {
        keySet.add(key);
        int location = key.hashCode() % hashArray.length;
        hashArray[location] = value;
        size++;
        return value;
    }

    @SuppressWarnings("unchecked")
    public V get(Object key) {
        int location = key.hashCode() % hashArray.length;
        return (V) hashArray[location];

    }

    public K get(int hashCode) {
        int location = hashCode % hashArray.length;
        return keySet.get(location);
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }

    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        int location = key.hashCode() % hashArray.length;
        keySet.remove(key);
        V temp = (V) hashArray[location];
        hashArray[location] = null;
        size--;
        return temp;
    }

    public int size() {
        return size;
    }

    public String toString() {
        String ret = "";
        for (int i = 0; i < hashArray.length; i++) {
            if (hashArray[i] != null) {
                ret += hashArray[i].toString();
            }
        }
        return ret;
    }

    public void clear() {
        hashArray = new Object[100];
        keySet = new MyHashSet<K>();
        size = 0;
    }
}
