import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class TestGround {
    private int index;
    private RedBlackBST<Integer, Integer> store;

    TestGround() {
        index = 0;
        store = new RedBlackBST<Integer, Integer>();
    }

    public void append(Integer item) {
        store.put(index++, item);
    }

    public void removeFront() {
        store.deleteMin();
    }

    public Integer get(int i) {
        int key = store.rank(i);
        return store.get(key);
    }

    public void delete(int i) {
        int rank = store.rank(i);
        store.delete(rank);


        // if (i <= store.size()) {
        //     int rank = store.rank(i);
        //     if (store.contains(rank)) {
        //         StdOut.print("Deleting ");
        //         StdOut.print(rank);
        //         StdOut.print("\n");
        //         store.delete(rank);
        //     }
        //     else {
        //         StdOut.print("Deleting ");
        //         StdOut.print(store.ceiling(rank));
        //         StdOut.print("\n");
        //         store.delete(store.ceiling(rank));
        //     }
        // }
    }

    public void print() {
        StdOut.println("Printing BST");
        for (int key : store.keys()) {
            StdOut.print(key);
            StdOut.print(",");
            StdOut.print(store.get(key));
            StdOut.print("\n");
        }
    }

    public static void main(String[] args) {
        TestGround n = new TestGround();
        n.append(0);
        n.append(1);
        n.append(4);
        n.print();
        n.delete(2);
        n.append(5);
        n.print();
        n.delete(0);
        n.delete(0);
        n.delete(0);
        n.print();
        n.append(1);
        n.append(2);
        n.append(3);
        n.print();
        n.delete(0);
        n.delete(1);
        n.print();
    }
}
