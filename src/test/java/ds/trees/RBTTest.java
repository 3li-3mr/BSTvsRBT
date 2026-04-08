package ds.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RBTTest {

    private TreeStructure tree;

    @BeforeEach
    void setUp() {
        tree = new RBT();
    }

    @Test
    void testInsertAndSize() {
        assertEquals(0, tree.size(), "Initial size should be 0");

        assertTrue(tree.insert(10), "Inserting 10 should succeed");
        assertTrue(tree.insert(5), "Inserting 5 should succeed");
        assertTrue(tree.insert(15), "Inserting 15 should succeed");

        assertEquals(3, tree.size(), "Size should be 3 after 3 unique inserts");

        // Test Duplicates
        assertFalse(tree.insert(10), "Inserting duplicate 10 should return false");
        assertEquals(3, tree.size(), "Size should not increase after duplicate insert");
    }

    @Test
    void testContains() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);

        assertTrue(tree.contains(10), "Tree should contain 10");
        assertTrue(tree.contains(5), "Tree should contain 5");
        assertTrue(tree.contains(15), "Tree should contain 15");

        assertFalse(tree.contains(99), "Tree should not contain 99");
        assertFalse(tree.contains(0), "Tree should not contain 0");
    }

    @Test
    void testInOrderTraversal() {
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(5);
        tree.insert(15);

        int[] expected = {5, 10, 15, 20, 30};
        int[] actual = tree.inOrder();

        assertArrayEquals(expected, actual, "inOrder traversal should return a sorted array");
    }

    @Test
    void testHeight() {
        assertEquals(0, tree.height(), "Empty tree height should be 0");

        tree.insert(10);
        assertEquals(1, tree.height(), "Root-only tree height should be 1");

        tree.insert(5);
        tree.insert(15);
        assertEquals(2, tree.height(), "Balanced 3-node tree height should be 2");

        tree.insert(20);
        tree.insert(25);
        assertEquals(3, tree.height(), "Unbalanced right-heavy tree height should be 4");
    }

    @Test
    void testDeleteLeafNode() {
        tree.insert(10);
        tree.insert(5);  // Leaf
        tree.insert(15); // Leaf

        assertTrue(tree.delete(5), "Deleting leaf 5 should succeed");
        assertFalse(tree.contains(5), "Tree should no longer contain 5");
        assertEquals(2, tree.size(), "Size should decrease to 2");
        assertArrayEquals(new int[]{10, 15}, tree.inOrder());
    }

    @Test
    void testDeleteNodeWithOneChild() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(3);

        assertTrue(tree.delete(5), "Deleting node 5 (one child) should succeed");
        assertFalse(tree.contains(5));
        assertTrue(tree.contains(3), "Child 3 should still be in the tree");
        assertArrayEquals(new int[]{3, 10}, tree.inOrder());
    }

    @Test
    void testDeleteNodeWithTwoChildren() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(12);
        tree.insert(18);

        assertTrue(tree.delete(15), "Deleting node 15 (two children) should succeed");
        assertFalse(tree.contains(15));
        assertTrue(tree.contains(12));
        assertTrue(tree.contains(18));

        int[] expected = {5, 10, 12, 18};
        assertArrayEquals(expected, tree.inOrder(), "InOrder should reflect proper successor replacement");
    }

    @Test
    void testDeleteRootNode() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);

        assertTrue(tree.delete(10), "Deleting root 10 should succeed");
        assertFalse(tree.contains(10));
        assertEquals(2, tree.size());
        assertArrayEquals(new int[]{5, 15}, tree.inOrder());
    }

    @Test
    void testDeleteNonExistentNode() {
        tree.insert(10);
        tree.insert(5);

        assertFalse(tree.delete(99), "Deleting non-existent node should return false");
        assertEquals(2, tree.size(), "Size should not change");
    }
}