import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int numItems;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        numItems = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null && last == null);
    }

    // return the number of items on the deque
    public int size() {
        return numItems;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            Node newNode = new Node();
            newNode.item = item;
            if (isEmpty()) {
                newNode.next = null;
                newNode.prev = null;
                first = newNode;
                last = newNode;
            }
            else {
                Node oldFirst = first;
                first = newNode;
                first.next = oldFirst;
                first.prev = null;
                oldFirst.prev = first;
            }
            ++numItems;
        }

    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            Node newNode = new Node();
            newNode.item = item;
            if (isEmpty()) {
                newNode.next = null;
                newNode.prev = null;
                first = newNode;
                last = newNode;
            }
            else {
                newNode.prev = last;
                last = newNode;
                last.next = null;
            }
            ++numItems;
        }

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        else {
            Item removedItem = first.item;
            if (size() == 1) {
                first = null;
                last = null;
            }
            else {
                first = first.next;
                first.prev = null;
            }
            --numItems;
            return removedItem;
        }


    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        else {
            Item removedItem = last.item;
            if (size() == 1) {
                first = null;
                last = null;
            }
            else {
                last = last.prev;
                last.next = null;
            }
            --numItems;
            return removedItem;
        }

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> stack = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                stack.addFirst(item);
            else if (!stack.isEmpty())
                StdOut.print(stack.removeFirst() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }

}
