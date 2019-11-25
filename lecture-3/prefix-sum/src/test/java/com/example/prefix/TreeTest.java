package com.example.prefix;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTest extends CommonSetup {
    @Test
    public void allocateTreeWithVariousInputSizesAndCutoffOne() {
        assertEquals(1, new Tree(1, 1).getSize());
        assertEquals(3, new Tree(2, 1).getSize());
        assertEquals(7, new Tree(4, 1).getSize());
        assertEquals(15, new Tree(5, 1).getSize());
    }

    @Test
    public void allocateOfTreeWithVariousInputSizesAndCutoffThree() {
        assertEquals(1, new Tree(1, 3).getSize());
        assertEquals(1, new Tree(2, 3).getSize());
        assertEquals(3, new Tree(4, 3).getSize());
        assertEquals(15, new Tree(17, 3).getSize());
    }

    @Test
    public void retrieveChildIndexes() {
        Tree tree = new Tree(7, 1);
        assertEquals(1, tree.getLeftChildIndex(0));
        assertEquals(2, tree.getRightChildIndex(0));
        assertEquals(5, tree.getLeftChildIndex(2));
        assertEquals(12, tree.getRightChildIndex(5));
    }

    @Test
    public void leafsAreProperlyIdentified() {
        Tree tree = new Tree(7, 1);
        assertFalse(tree.isLeafIndex(0));
        assertFalse(tree.isLeafIndex(3));
        assertFalse(tree.isLeafIndex(6));
        assertTrue(tree.isLeafIndex(7));
        assertTrue(tree.isLeafIndex(14));
    }

    @Test
    public void numberOfLeavesIsCorrectlyReturned() {
        Tree tree = new Tree(7, 1);
        assertEquals(8, tree.getNumLeaves());
    }

    @Test
    public void nodeCanPrettyPrintIntoString() {
        Tree.Node node = new Tree.Node(3, 7, 23);
        node.fromSiblingBranch = Long.valueOf(8);
        assertEquals("Node(low=3,high=7,s=23,fromSibling=8)", node.toString());
    }
}