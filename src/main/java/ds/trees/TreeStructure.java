package ds.trees;

import java.util.List;

public interface TreeStructure {
    boolean insert(int v);
    boolean delete(int v);
    boolean contains(int v);
    int[] inOrder();
    int height();
    int size();
}