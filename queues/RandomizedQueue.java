import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final double RADIO = 0.25;
    private Item[] array;
    private int size;
    private int capacity;

    // construct an empty randomized queue
    public RandomizedQueue() {
        int originalSize = 8;
        array = (Item[]) new Object[originalSize];
        capacity = originalSize;
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("The item shall not be null!");
        }
        if (size == capacity) {
            resize(capacity * 2);
        }
        array[size] = item;
        size++;
    }

    private void resize(int newCapacity) {
        Item[] newArray = (Item[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
        capacity = newCapacity;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        Item res = array[index];
        array[index] = array[size - 1];
        array[size - 1] = null;
        size--;
        if (size > 0 && (double) size / capacity <= RADIO) {
            resize(capacity / 2);
        }
        return res;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        return array[index];
    }

    @Override
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] copyArray;
        private int remain;

        public RandomizedQueueIterator() {
            copyArray = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                copyArray[i] = array[i];
            }
            StdRandom.shuffle(copyArray);
            remain = size;
        }

        @Override
        public boolean hasNext() {
            return (remain != 0);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported!");
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No next!");
            }
            remain--;
            return copyArray[remain];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        for (int i = 0; i < 18; i++) {
            rq.enqueue("A" + i);
        }
        StdOut.println("first iterator");
        for (String s : rq) {
            StdOut.print(s + " ");
        }
        StdOut.println("\n");

        StdOut.println("second iterator ");
        for (String s : rq) {
            StdOut.print(s + " ");
        }
        StdOut.println("\n");

        for (int i = 0; i < 18; i++) {
            StdOut.print("deque ");
            StdOut.print(rq.dequeue());
            StdOut.println(". remain " + rq.size() + " elements. now capacity " + rq.capacity);
        }
    }

}