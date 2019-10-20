package com.example.prefix;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DownPassTest extends CommonSetup {
    @Test
    public void generatePrefixSumFromSingleNodeTree() {
        Tree.Node node = new Tree.Node(0, referenceInput.length, 0);
        Tree tree = new Tree(1, referenceInput.length);
        tree.setNode(0, node);

        long[] newOutput = referenceInput;
        new DownPass(tree, referenceInput, newOutput).buildOutput();
        assertArrayEquals(referenceOutput, newOutput);
    }
}