import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Node prev;
        Item item;
        Node next;

        public Node(Node p, Item i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private final Node head;
    private final Node tail;
    private int size;

    // construct an empty deque
    public Deque() {
        size = 0;
        head = new Node(null, null, null);
        tail = new Node(head, null, null);
        head.next = tail;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("The added item should not be null!");
        }
        size++;
        Node node = new Node(head, item, head.next);
        head.next.prev = node;
        head.next = node;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("The added item should not be null!");
        }
        size++;
        Node node = new Node(tail.prev, item, tail);
        tail.prev.next = node;
        tail.prev = node;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size <= 0) {
            throw new NoSuchElementException("The Deque is now empty!");
        }
        size--;
        Item res = head.next.item;
        head.next.next.prev = head;
        head.next = head.next.next;
        return res;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size <= 0) {
            throw new NoSuchElementException("The Deque is now empty!");
        }
        size--;
        Item res = tail.prev.item;
        tail.prev.prev.next = tail;
        tail.prev = tail.prev.prev;
        return res;
    }

    @Override
    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node first = head;

        @Override
        public boolean hasNext() {
            return (first.next != tail);
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
            first = first.next;
            return first.item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        StdOut.println("Tests start.");

        Deque<String> dq = new Deque<>();
        StdOut.println("The empty state of dq is " + dq.isEmpty());

        for (int i = 0; i < 5; i++) {
            dq.addFirst("A" + i);
        }
        for (int i = 0; i < 5; i++) {
            dq.addLast("B" + i);
        }
        for (String s : dq) {
            StdOut.println(s);
        }
        StdOut.println("dq has " + dq.size() + " elements in total");
        for (int i = 0; i < 10; i++) {
            StdOut.println(dq.removeFirst());
            StdOut.println(dq.removeLast());
            StdOut.println(dq.size());
        }
    }

}