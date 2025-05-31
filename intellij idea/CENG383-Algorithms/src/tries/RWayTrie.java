package tries;

import java.util.*;

public class RWayTrie<Value> {

    private static final int R = 256;  // extended ASCII

    private Node root = new Node();

    private class Node {
        Value val;
        @SuppressWarnings("unchecked")
        Node[] next = (Node[]) new RWayTrie<?>.Node[R];

    }

    // Public insert method
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    // Recursive insert method
    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d); // d-th character
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    // Public get method (returns value of the key)
    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;  // value cast not needed due to generic class
    }

    // Recursive get method
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    // Public contains method
    public boolean contains(String key) {
        return get(key) != null;
    }
}
