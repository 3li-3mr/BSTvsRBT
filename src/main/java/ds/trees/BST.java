package ds.trees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BST implements TreeStructure {

    private static final Logger logger = LoggerFactory.getLogger(BST.class);

    public static class Validator {
        private static int countNodes(Node root){
            if(root == null){
                return 0;
            }
            return 1 + countNodes(root.right) + countNodes(root.left);
        }

        public static void check(BST tree) {
            if (tree.size() != countNodes(tree.root)) {
                throw new RuntimeException("Size mismatch!");
            }
            validateBST(tree.root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        private static void validateBST(Node node, int min, int max) {
            if (node == null) return;
            if (node.value <= min || node.value >= max) {
                throw new RuntimeException("BST property violated at value: " + node.value);
            }
            validateBST(node.left, min, node.value);
            validateBST(node.right, node.value, max);
        }
    }
    static final boolean VALIDATE = false;

    private Node root;
    private int size;

    static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public BST() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean insert(int v) {
        Node x = new Node(v);
        Node y = root;
        if(y == null){
            root = x;
            this.size++;
            return true;
        }
        while(y != null){
            if(v == y.value){
                return false;
            }
            if(v < y.value){
                if(y.left == null){
                    y.left = x;
                    this.size++;
                    break;
                }
                y = y.left;
            }
            else{
                if(y.right == null){
                    y.right = x;
                    this.size++;
                    break;
                }
                y = y.right;
            }
        }
        if (VALIDATE) Validator.check(this);
        return true;
    }

    @Override
    public boolean delete(int v) {
        // finding x and parent
        Node x = root;
        Node p = null;
        while(x != null){
            if(x.value == v){
                break;
            }
            else if(v < x.value){
                p = x;
                x = x.left;
            }
            else{
                p = x;
                x = x.right;
            }
        }
        if(x == null) return false;
        // 0 or 1 child cases
        if(x.left == null){
            if (p == null) {
                root = x.right;
            } else if(p.left == x){
                p.left = x.right;
            } else{
                p.right = x.right;
            }
            this.size--;
            return true;
        }
        if(x.right == null){
            if (p == null) {
                root = x.left;
            } else if(p.left == x){
                p.left = x.left;
            } else{
                p.right = x.left;
            }
            this.size--;
            return true;
        }
        // 2 children
        // getting successor and parent
        Node suc = x.right;
        Node suc_p = x;
        while(suc.left != null){
            suc_p = suc;
            suc = suc.left;
        }
        x.value = suc.value;
        if(suc_p == x){
            x.right = suc.right;
        }
        else{
            suc_p.left = suc.right;
        }
        this.size--;
        if (VALIDATE) Validator.check(this);
        return true;
    }

    @Override
    public boolean contains(int v) {
        Node x = root;
        while(x != null){
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

    private void inOrderHelper(Node root, int[] arr, int[] index){
        if(root == null){
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
        if(root == null){
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
