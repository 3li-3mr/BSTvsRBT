package ds.trees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBT implements TreeStructure {

    private static final Logger logger = LoggerFactory.getLogger(RBT.class);

    public static class Validator{
        private static int countNodes(Node root, Node nil) {
            if (root == nil) return 0;
            return 1 + countNodes(root.left, nil) + countNodes(root.right, nil);
        }

        public static void check(RBT tree){
            if(tree.root == tree.nil) return;
            if (tree.size() != countNodes(tree.root, tree.nil)) {
                throw new RuntimeException("Size mismatch! Expected: " + tree.size);
            }
            validateBST(tree.root, tree.nil, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if(tree.root.color == RED) throw new RuntimeException("Root must be black!");
            checkRedred(tree.root, tree.nil);
            checkBlackHeight(tree.root, tree.nil);
        }

        private static void validateBST(Node node, Node nil, int min, int max) {
            if (node == nil) return;
            if (node.value <= min || node.value >= max) {
                throw new RuntimeException("BST property violated at value: " + node.value);
            }
            validateBST(node.left, nil, min, node.value);
            validateBST(node.right, nil, node.value, max);
        }

        private static void checkRedred(Node root, Node nil){
            if(root == nil) return;
            if(root.color == RED){
                if(root.left.color == RED || root.right.color == RED){
                    throw new RuntimeException("Red-Red violation at node: " + root.value);
                }
            }
            checkRedred(root.left, nil);
            checkRedred(root.right, nil);
        }
        private static int checkBlackHeight(Node root, Node nil){
            if(root == nil) return 1;
            int left = checkBlackHeight(root.left, nil);
            int right = checkBlackHeight(root.right, nil);
            if(left != right) throw new RuntimeException("Black Height violation at node: " + root.value);
            return (root.color == BLACK) ? left + 1 : left;
        }
    }
    static final boolean VALIDATE = false;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;
    private final Node nil;

    static class Node {
        int value;
        boolean color;
        Node left;
        Node right;
        Node parent;

        Node(int value) {
            this.value = value;
            this.color = RED;
        }
    }

    public RBT() {
        nil = new Node(0);
        nil.color = BLACK;
        nil.left = null;
        nil.right = null;

        this.root = nil;
        this.size = 0;
    }

    @Override
    public boolean insert(int v) {
        logger.debug("Inserting value: {}", v);
        Node x = new Node(v);
        x.parent = nil;
        x.left = nil;
        x.right = nil;

        Node y = root;
        if(y == nil){
            root = x;
            root.color = BLACK;
            this.size++;
            return true;
        }
        while(y != nil){
            if(v == y.value){
                return false;
            }
            if(v < y.value){
                if(y.left == nil){
                    y.left = x;
                    x.parent = y;
                    this.size++;
                    break;
                }
                y = y.left;
            }
            else{
                if(y.right == nil){
                    y.right = x;
                    x.parent = y;
                    this.size++;
                    break;
                }
                y = y.right;
            }
        }
        fixInsert(x);

        if (VALIDATE) Validator.check(this);
        return true;
    }

    @Override
    public boolean delete(int v) {
        logger.debug("Deleting value: {}", v);
        Node z = root;
        while (z != nil) {
            if (v == z.value) break;
            z = (v < z.value) ? z.left : z.right;
        }

        if (z == nil) return false;

        Node y = z;
        Node x;
        boolean yOriginalColor = y.color;

        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        size--;
        if (yOriginalColor == BLACK) {
            fixDelete(x);
        }
        if (VALIDATE) Validator.check(this);
        return true;
    }

    // Helper to replace one subtree with another
    private void transplant(Node u, Node v) {
        if (u.parent == nil) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != nil) node = node.left;
        return node;
    }

    @Override
    public boolean contains(int v) {
        logger.trace("Searching for value: {}", v);
        Node x = root;
        while(x != nil){
            if(x.value == v){
                return true;
            }
            else if(v < x.value){
                x = x.left;
            }
            else{
                x = x.right;
            }
        }
        return false;
    }

    private void fixInsert(Node k) {
        while(k.parent.color == RED) {
            if (k.parent == k.parent.parent.left) {
                if (k.parent.parent.right.color == RED) {
                    k.parent.color = BLACK;
                    k.parent.parent.right.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    rightRotate(k.parent.parent);
                }
            } else {
                if (k.parent.parent.left.color == RED) {
                    k.parent.color = BLACK;
                    k.parent.parent.left.color = BLACK;
                    k.parent.parent.color = RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = BLACK;
                    k.parent.parent.color = RED;
                    leftRotate(k.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void fixDelete(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node s = x.parent.right;

                // case 1
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }
                // case 2
                if (s.left.color == BLACK && s.right.color == BLACK) {
                    s.color = RED;
                    x = x.parent;
                } else {
                    // case 3
                    if (s.right.color == BLACK) {
                        s.left.color = BLACK;
                        s.color = RED;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    // case 4
                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    s.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node s = x.parent.left;
                if (s.color == RED) {
                    s.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == BLACK && s.left.color == BLACK) {
                    s.color = RED;
                    x = x.parent;
                } else {
                    if (s.left.color == BLACK) {
                        s.right.color = BLACK;
                        s.color = RED;
                        leftRotate(s);
                        s = x.parent.left;
                    }
                    s.color = x.parent.color;
                    x.parent.color = BLACK;
                    s.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    private void leftRotate(Node x) {
        logger.debug("Left rotating around node: {}", x.value);
        Node y = x.right;
        if(y == nil) return;
        x.right = y.left;
        if(y.left != nil) y.left.parent = x;

        y.parent = x.parent;
        if(x.parent == nil){
            root = y;
        }
        else if(x == x.parent.left){
            x.parent.left = y;
        }
        else{
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        logger.debug("Right rotating around node: {}", x.value);
        Node y = x.left;
        if(y == nil) return;
        x.left = y.right;
        if(y.right != nil) y.right.parent = x;

        y.parent = x.parent;
        if(x.parent == nil){
            root = y;
        }
        else if(x == x.parent.left){
            x.parent.left = y;
        }
        else{
            x.parent.right = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void inOrderHelper(Node root, int[] arr, int[] index){
        if(root == nil){
            return;
        }
        inOrderHelper(root.left, arr, index);
        arr[index[0]] = root.value;
        index[0]++;
        inOrderHelper(root.right, arr, index);
    }

    @Override
    public int[] inOrder() {
        int[] arr = new int[this.size];
        int[] index = {0};
        inOrderHelper(root, arr, index);
        return arr;
    }

    private int heightHelper(Node root){
        if(root == nil){
            return 0;
        }
        return 1 + Math.max(heightHelper(root.left), heightHelper(root.right));
    }

    @Override
    public int height() {
        return heightHelper(root);
    }

    @Override
    public int size() {
        return this.size;
    }
}