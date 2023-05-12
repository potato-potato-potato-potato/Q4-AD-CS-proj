package DDLList;

public class Node<E> {
    private E data;
    private Node<E> next;
    private Node<E> prev;

    public Node(E obj) {
        data = obj;
        next = null;
        prev = null;
    }

    public E get() {
        return data;
    }

    public Node<E> next() {
        return next;
    }

    public Node<E> prev() {
        return prev;
    }

    public void setNext(Node<E> n) {
        next = n;
    }

    public void setPrev(Node<E> n) {
        prev = n;
    }

    public String toString() {
        return data.toString();
    }

}
