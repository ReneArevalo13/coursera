import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private int capacity;
    private Item[] array;

    public RandomizedQueue() {
        array = (Item[]) new Object[1];
        size = 0;
        capacity = 1;
    }

    private class RandomListIterator implements Iterator<Item> {
        private int current;
        private int initialSize;
        private int[] order;

        public RandomListIterator() {
            initialSize = size;
            current = 0;
            order = new int[size];
            for (int i = 0; i < size; i++) {
                order[i] = i;
            }
            StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return current < size || size == 0;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item item = array[order[current]];
            if (size() != initialSize) {
                throw new java.util.ConcurrentModificationException();
            }
            current++;
            return item;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    private void shrinkCapacity() {
        capacity /= 2;
        Item[] newArray = (Item[]) new Object[capacity / 2];
        int i = 0;
        for (Item item : array) {
            newArray[i++] = item;
        }
        array = newArray;
    }

    private void expandCapacity() {
        capacity *= 2;
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i <= size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        if (size + 1 == capacity) {
            expandCapacity();
        }
        array[size++] = item;
    }

    public Item dequeue() {
        // remove and return a random item
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        int randomIndex = StdRandom.uniformInt(0, size);
        Item item = array[randomIndex];
        size--;
        array[randomIndex] = array[size];
        array[size] = null;

        if (capacity / 2 == size) {
            shrinkCapacity();
        }
        return item;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Item sample() {
        // return (but do not remove) a random item
        if (size() == 0) {
            throw new java.util.NoSuchElementException();
        }
        return array[StdRandom.uniformInt(0, size)];
    }

    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomListIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        System.out.println("-isempty " + rq.isEmpty());

        rq.enqueue("one");
        rq.enqueue("two");
        rq.enqueue("three");
        rq.enqueue("four");
        System.out.println("-initial " + rq.size());

        System.out.println("-dequeue " + rq.dequeue());

        for (String s : rq) {
            System.out.println("  " + s);
        }

        System.out.println("-dequeued size " + rq.size());

        System.out.println("-sample " + rq.sample());
        System.out.println("-sample " + rq.sample());
        System.out.println("-sample " + rq.sample());

        System.out.println("-random iterator");
        for (String s : rq) {
            System.out.println(s);
        }

        System.out.println("-random iterator one more time");
        for (String s : rq) {
            System.out.println(s);
        }
    }
}
