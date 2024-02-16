import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int n = 0;

    public RandomizedQueue() {
        queue = (Item[]) new Object[2];

    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int size) {
        Item[] copy = (Item[]) new Object[size];
        for (int i = 0; i < n; i++) {
            copy[i] = queue[i];
        }
        queue = copy;

    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null");
        }
        if (n == queue.length) {
            resize(2 * queue.length);
        }
        queue[n++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int randomIndex = StdRandom.uniformInt(0, n);
        Item item = queue[randomIndex];

        if (randomIndex != n - 1) {
            queue[randomIndex] = queue[n - 1];
        }
        queue[n - 1] = null;
        n--;

        if (n > 0 && n == queue.length / 4) {
            resize(queue.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int randomIndex = StdRandom.uniformInt(0, n);
        return queue[randomIndex];
    }

    // https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/
    public Iterator<Item> iterator() {
        //  Fisher–Yates shuffle Algorithm
        for (int i = n - 1; i > 0; i--) {
            // Pick a random index from 0 to i
            int j = StdRandom.uniformInt(i + 1);
            // Swap arr[i] with the element at random index
            Item temp = queue[i];
            queue[i] = queue[j];
            queue[j] = temp;
        }
        return new RandomizedQueueIterator<Item>(queue, n);
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private Item[] items;
        private int size;
        private int current = 0;

        public RandomizedQueueIterator(final Item[] items, int size) {
            this.items = items;
            this.size = size;
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return items[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
        r.enqueue(0);
        r.enqueue(1);
        r.enqueue(2);
        r.enqueue(3);
        r.enqueue(4);
        r.enqueue(5);

        System.out.printf("Dequeue 1: %d%n", r.dequeue());
        System.out.printf("Dequeue 2: %d%n", r.dequeue());
        System.out.printf("Sample: %d%n", r.sample());
        System.out.printf("isEmpty: %b%n", r.isEmpty());
        System.out.printf("Size: %d%n", r.size());

        for (Integer i : r) {
            StdOut.print(i + " ");
        }
    }
}
