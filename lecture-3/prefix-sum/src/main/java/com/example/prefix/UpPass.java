package com.example.prefix;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

// Implements the first pass of the parallel Prefix Sum algorithm.
class UpPass {
    private static final Logger log = Logger.getLogger(UpPass.class.getName());

    private final long[] input;
    private final int sequentialCutoff;
    private final Tree tree;

    UpPass(long[] input, int sequentialCutoff) {
        assert input != null && input.length > 0 && sequentialCutoff > 0;
        this.input = input;
        this.sequentialCutoff = sequentialCutoff;
        tree = new Tree(input.length, sequentialCutoff);
    }

    // Calculates the sum of input array elements in the range [i, j).
    long rangeSum(int i, int j) {
        log.entering(UpPass.class.getName(), "rangeSum", new Object[] {i, j});
        assert i >= 0 && i < j && j <= input.length;

        long s = input[i];
        for (int k = i + 1; k < j; k++)
            s += input[k];

        log.exiting(UpPass.class.getName(), "rangeSum", Long.valueOf(s));
        return s;
    }

    private class TreeBuilder extends RecursiveTask<Long> {
        private final int nodeIndex;
        private final int low;
        private final int high;

        TreeBuilder(int nodeIndex, int low, int high) {
            this.nodeIndex = nodeIndex;
            this.low = low;
            this.high = high;
        }

        @Override
        protected Long compute() {
            log.entering(UpPass.class.getName(), "compute", new Object[] {nodeIndex, low, high});

            long totalSum;
            if (high - low <= sequentialCutoff) {
                totalSum = rangeSum(low, high);
            } else {
                int mid = low + (high - low) / 2;
                TreeBuilder left  = new TreeBuilder(tree.getLeftChildIndex(nodeIndex), low, mid);
                TreeBuilder right = new TreeBuilder(tree.getRightChildIndex(nodeIndex), mid, high);
                left.fork();
                long rightSum = right.compute();
                long leftSum = left.join();
                totalSum = leftSum + rightSum;
            }

            Tree.Node node = new Tree.Node(low, high, totalSum);
            tree.setNode(nodeIndex, node);
            log.exiting(UpPass.class.getName(), "compute", node);
            return totalSum;
        }
    }

    Tree buildTree() {
        ForkJoinPool.commonPool().invoke(new TreeBuilder(0, 0, input.length));
        return tree;
    }
}
