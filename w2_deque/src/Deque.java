import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Corner cases.
 * Throw an IllegalArgumentException if the client calls either addFirst() or addLast() with a null
 * argument.
 * Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast
 * when the deque is empty.
 * Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator
 * when there are no more items to return.
 * Throw an UnsupportedOperationException if the client calls the remove() method in the iterator.
 * <p>
 * Unit testing.
 * Your main() method must call directly every public constructor and method to help verify that
 * they work as prescribed
 * (e.g., by printing results to standard output).
 * <p>
 * Performance requirements.
 * Your deque implementation must support each deque operation (including construction) in constant
 * worst-case time.
 * A deque containing n items must use at most 48n + 192 bytes of memory.
 * Additionally, your iterator implementation must support each operation (including construction)
 * in constant worst-case time.
 */
public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first = null;
    private Node<Item> last = null;
    private int size = 0;

    public Deque() {
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null");
        }

        Node<Item> newNode = new Node<>(item);
        if (first != null) {
            Node<Item> currentFirst = first;
            newNode.next = currentFirst;
            currentFirst.prev = newNode;
        }
        else {
            last = newNode;
        }

        first = newNode;
        size++;
    }

    public void addLast(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null");
        }

        Node<Item> newNode = new Node<>(item);
        if (last != null) {
            Node<Item> currentlast = last;
            newNode.prev = currentlast;
            currentlast.next = newNode;
        }
        else {
            first = newNode;
        }

        last = newNode;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Node<Item> currentFirst = first;
        first = currentFirst.next;
        if (first == null) {
            last = null;
        }
        else {
            first.prev = null;
        }
        size--;
        return currentFirst.getAndDestroy();
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Node<Item> currentLast = last;
        last = currentLast.prev;
        if (last == null) {
            first = null;
        }
        else {
            last.next = null;
        }
        size--;
        return currentLast.getAndDestroy();
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item i = current.element;
            current = current.next;
            return i;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node<Item> {
        private Item element;
        private Node<Item> next;
        private Node<Item> prev;

        public Node(Item element) {
            this.element = element;
        }

        private Item getAndDestroy() {
            try {
                next = null;
                prev = null;
                return element;
            }
            finally {
                element = null;
            }
        }
    }

    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();
        d.addFirst(2);
        d.addFirst(1);
        d.addLast(3);
        d.addLast(4);
        for (Integer i : d) {
            StdOut.print(i + " ");
        }
        StdOut.println("\r\n " + d.size());
        StdOut.println(d.isEmpty());
        StdOut.println(d.removeFirst());
        StdOut.println(d.removeLast());
        for (Integer i : d) {
            StdOut.print(i + " ");
        }
    }
}
