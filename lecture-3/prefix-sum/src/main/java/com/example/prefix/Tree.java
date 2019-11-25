package com.example.prefix;

// The binary tree used for computing the prefix/suffix sum.
final class Tree {
    // Node that stores the range [i, j), sum over this range, and sibling side sum (fromSiblingBranch field) fields.
    static class Node {
        final int[] r;
        final long s;
        // For prefix sum this field is equivalent to fromLeft and for suffix sum to fromRight.
        // When code is properly structured then the opportunity for reuse increases, too.
        Long fromSiblingBranch;

        Node(int i, int j, long s) {
            assert i >= 0 && i < j;
            r = new int[] {i, j};
            this.s = s;
            fromSiblingBranch = Long.valueOf(0);
        }

        @Override
        public String toString() {
            return String.format("Node(low=%d,high=%d,s=%d,fromSibling=%d)", r[0], r[1], s, fromSiblingBranch);
        }
    };

    // The binary tree is represented in a compact array form, whose length is rounded up to the nearest power of 2.
    // This allows very efficient mapping of array indexes to nodes of a tree.
    private final Node[] nodes;

    private static int roundToNearestPowerOfTwo(int n) {
        assert n > 0;
        int candidate = Integer.highestOneBit(n);
        return n == candidate ? n : candidate << 1;
    }

    Tree(int inputSize, int sequentialCutoff) {
        assert inputSize > 0 && sequentialCutoff > 0;

        int numLeaves = (int) Math.ceil((float) inputSize / sequentialCutoff);
        int treeSize = 2 * roundToNearestPowerOfTwo(numLeaves) - 1;
        nodes = new Node[treeSize];
    }

    int getSize() {
        return nodes.length;
    }

    private void checkIndex(int i) {
        assert i >= 0 && i < nodes.length;
    }

    Node getNode(int i) {
        checkIndex(i);
        return nodes[i];
    }

    void setNode(int i, Node node) {
        checkIndex(i);
        assert node != null;
        nodes[i] = node;
    }

    int getLeftChildIndex(int i) {
        checkIndex(i);
        return 2 * i + 1;
    }

    int getRightChildIndex(int i) {
        checkIndex(i);
        return 2 * (i + 1);
    }

    boolean isLeafIndex(int i) {
        checkIndex(i);
        return i >= nodes.length / 2;
    }

    int getNumLeaves() {
        return (getSize() + 1) / 2;
    }
}
