package pac;

import java.util.Iterator;

/** Class Stack
 * Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne
 * <a href='https://algs4.cs.princeton.edu/13stacks/index.php#1.2'>1.3   Bags, Queues, and Stacks</a>
 */

public class Stack<T> implements Iterable<T> {
    private Node first;
    private int n;

    public Stack() {}

    private class Node {
        T item;
        Node next;
    }

    private class ListIterator implements Iterator<T> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {}

        @Override
        public T next() {
            T item = current.item;
            current = current.next;

            return item;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void push(T item) {
        Node oldFirst = first;

        first = new Node();
        first.item = item;
        first.next = oldFirst;
        n++;
    }

    public T pop() {
        T item = first.item;
        first = first.next;
        n--;

        return item;
    }

    public T peek() {
        return first.item;
    }
}
