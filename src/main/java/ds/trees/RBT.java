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
        return false;
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
    }

    private void fixDelete(Node x) {
    }

    private void leftRotate(Node x) {
    }

    private void rightRotate(Node x) {
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