package myArrayList;

import java.util.Iterator;

public class MyArrayList<E> implements Iterable<E> {
    private Object[] array;
    private int size = 10;// default size
    private int itemNum;// index of the next item to be added

    public MyArrayList() {
        this.array = new Object[size];
        itemNum = 0;
    }

    public boolean arrayAtCompacity() {
        if (itemNum >= size) {
            return true;
        } else {
            return false;
        }
    }

    public boolean add(E obj) {
        try {
            if (arrayAtCompacity()) {
                Object[] temp = new Object[size + 10];
                size += 10;
                for (int i = 0; i < itemNum; i++) {
                    temp[i] = array[i];
                }
                array = temp;

            }

            array[itemNum] = obj;
            itemNum++;

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index > itemNum) {
            return null;
        } else {
            return (E) array[index];
        }
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        Object temp = array[index];
        array[index] = null;
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        itemNum--;
        return (E) temp;
    }

    @SuppressWarnings("unchecked")
    public E remove(E obj) {
        Object temp;
        int search;
        for (search = 0; search < itemNum; search++) {
            if (array[search] != null && array[search].equals(obj)) {
                temp = array[search];
                array[search] = null;
                for (int i = search; i < size - 1; i++) {
                    array[i] = array[i + 1];
                }
                itemNum--;
                return (E) temp;
            }
        }

        itemNum--;
        return null;
    }

    public void set(int index, E obj) {
        array[index] = obj;
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < itemNum; i++) {
            temp += array[i] + ", ";
        }
        return temp;
    }

    public int size() {
        return itemNum;
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
                return index < itemNum;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                return (E) array[index++];
            }

        };
    }

}
