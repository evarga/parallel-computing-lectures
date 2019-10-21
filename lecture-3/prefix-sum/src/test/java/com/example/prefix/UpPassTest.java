package com.example.prefix;

import org.junit.Test;

import static org.junit.Assert.*;

public class UpPassTest extends CommonSetup {
    @Test
    public void rangeSumsAreProperlyCalculated() {
        UpPass up = new UpPass(referenceInput, 1);

        for (int i = 1; i <= referenceInput.length; i++)
            assertEquals(referenceOutput[i - 1], up.rangeSum(0, i));
        assertEquals(referenceInput[2] + referenceInput[3] + referenceInput[4] + referenceInput[5], up.rangeSum(2, 6));
    }

    @Test
    public void treeIsProperlyBuiltWithCutoffEqualOrLargerThanInputSize() {
        UpPass up = new UpPass(referenceInput, 500);
        Tree tree = up.buildTree();
        assertNotNull(tree);

        Tree.Node node = tree.getNode(0);
        assertArrayEquals(node.r, new int[] {0, tree.getNumLeaves() * 500});
        assertEquals(75, node.s);
        assertEquals(0, node.leftFrom.longValue());
    }

    @Test
    public void treeIsProperlyBuiltWithCutoffSmallerThanInputSize() {
        UpPass up = new UpPass(referenceInput, 1);
        Tree tree = up.buildTree();
        assertNotNull(tree);

        Tree.Node node = tree.getNode(0);
        assertArrayEquals(node.r, new int[] {0, tree.getNumLeaves()});
        assertEquals(75, node.s);
        assertEquals(0, node.leftFrom.longValue());

        node = tree.getNode(1);
        assertArrayEquals(node.r, new int[] {0, 8});
        assertEquals(60, node.s);
        assertEquals(0, node.leftFrom.longValue());

        node = tree.getNode(5);
        assertArrayEquals(node.r, new int[] {8, 12});
        assertEquals(15, node.s);
        assertEquals(0, node.leftFrom.longValue());

        node = tree.getNode(9);
        assertArrayEquals(node.r, new int[] {4, 6});
        assertEquals(30, node.s);
        assertEquals(0, node.leftFrom.longValue());
    }
}