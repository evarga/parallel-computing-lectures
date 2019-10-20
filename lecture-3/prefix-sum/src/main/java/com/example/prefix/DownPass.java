package com.example.prefix;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

// Implements the second pass of the parallel Prefix Sum algorithm.
class DownPass {
    private static final Logger log = Logger.getLogger(DownPass.class.getName());

    private final Tree tree;
    private final long[] input;
    private final long[] output;

    DownPass(Tree tree, long[] input, long[] output) {
        assert tree != null && input != null && input.length > 0 && output != null && output.length == input.length;
        this.tree = tree;
        this.input = input;
        this.output = output;
    }

    private class OutputBuilder extends RecursiveAction {
        private final int nodeIndex;

        OutputBuilder(int nodeIndex) {
            this.nodeIndex = nodeIndex;
        }

        // This method puts final output values at corresponding indices.
        private void processLeaves(int low, int high, long prevSum) {
            log.entering(DownPass.class.getName(), "processLeaves", new Object[] {low, high, prevSum});

            for (int i = low; i < high; i++) {
                long currSum = prevSum + input[i];
                output[i] = currSum;
                prevSum = currSum;
            }
        }

        @Override
        protected void compute() {
            Tree.Node node = tree.getNode(nodeIndex);
            log.fine("Processing node " + node);

            if (tree.isLeafIndex(nodeIndex))
                processLeaves(node.r[0], node.r[1], node.leftFrom);
            else {
                int leftChildIndex = tree.getLeftChildIndex(nodeIndex);
                int rightChildIndex = tree.getRightChildIndex(nodeIndex);
                Tree.Node leftChild = tree.getNode(leftChildIndex);
                Tree.Node rightChild = tree.getNode(rightChildIndex);

                leftChild.leftFrom = node.leftFrom;
                rightChild.leftFrom = node.leftFrom + leftChild.s;

                OutputBuilder left = new OutputBuilder(leftChildIndex);
                OutputBuilder right = new OutputBuilder(rightChildIndex);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }

    void buildOutput() {
        ForkJoinPool.commonPool().invoke(new OutputBuilder(0));
    }
}
