package HashMap;

import java.io.Serializable;
import java.util.Iterator;

public class MyHashSet<E> implements Iterable<E>, Serializable {
    private int size = 0;
    private Object[] hashTable;

    public MyHashSet() {
        hashTable = new Object[1000];
    }

    public boolean add(E obj) {
        int location = obj.hashCode() % hashTable.length;
        if (hashTable[location] == null) {
            hashTable[location] = obj;
            size++;
            return true;
        }
        return false;
    }

    public void clear() {
        hashTable = new Object[10];
        size = 0;
    }

    public boolean contains(Object obj) {
        int location = obj.hashCode() % hashTable.length;
        return (hashTable[location] != null);
    }

    public boolean remove(Object obj) {
        int location = obj.hashCode() % hashTable.length;
        if (hashTable[location] != null) {
            hashTable[location] = null;
            size--;
            return true;
        }
        return false;
    }

    public String toString() {
        String ret = "";
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null) {
                ret += hashTable[i].toString() + ", ";
            }
        }
        return ret;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        int location = index % hashTable.length;
        if (hashTable[location] == null) {
            System.out.println("No object at index " + index);
        }
        return hashTable[location] == null ? null : (E) hashTable[location];
    }

    @SuppressWarnings("unchecked")
    public E[] toArray() {
        E[] ret = (E[]) new Object[size];
        int index = 0;
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] != null) {
                ret[index++] = (E) hashTable[i];
            }
        }
        return ret;

    }

    // Iterator implementation using anonymous inner class
    // https://www.youtube.com/watch?v=DwtPWZn6T1A
    // because Iterable is implemented the abstarct method iterator() is required
    // because Iterator is also a abstract class the method hasNext() and next() are
    // required
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                while (index < hashTable.length && hashTable[index] == null) {
                    index++;
                }
                return index < hashTable.length;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                return (E) hashTable[index++];
            }
        };
    }
}