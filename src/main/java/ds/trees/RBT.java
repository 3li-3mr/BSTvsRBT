package ds.trees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBT implements TreeStructure {

    private static final Logger logger = LoggerFactory.getLogger(RBT.class);
    static final boolean VALIDATE = false;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;
    private final Node nil;

    private static class Node {
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
        return true;
    }

    @Override
    public boolean delete(int v) {
        return false;
    }

    @Override
    public boolean contains(int v) {
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
    }

    private void leftRotate(Node x) {
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