package com.example.pack;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.logging.Logger;

/**
 * Performs a parallel pack operation, which is like an order preserving filtering action.
 *
 * @see java.util.Arrays#parallelPrefix(int[], IntBinaryOperator)
 * @see java.util.Arrays#parallelSetAll(int[], IntUnaryOperator)
 */
public final class Pack {
    private static final Logger log = Logger.getLogger(Pack.class.getName());
    private static final int SEQUENTIAL_CUTOFF = 500;

    private final int[] input;
    private final int startOffsetInput;
    private final IntPredicate predicate;
    private final int[] bitSum;
    private final int startOffsetOutput;
    private final AtomicInteger numSelectedElements = new AtomicInteger(0);
    private int[] output;

    private Pack(int[] input, int low, int high, IntPredicate predicate) {
        this(input, low, high, predicate, null, 0);
    }

    private Pack(int[] input, int low, int high, IntPredicate predicate, int[] output, int startOffset) {
        assert input != null && input.length > 0 && predicate != null;
        this.input = input;
        startOffsetInput = low;
        this.predicate = predicate;
        this.bitSum = new int[high - low];    // Let us pretend that this is an O(1) operation.
        this.output = output;
        startOffsetOutput = startOffset;
    }

    private class OutputBuilder extends RecursiveAction {
        private final int low;
        private final int high;

        OutputBuilder(int low, int high) {
            assert low >=0 && low < high && high <= bitSum.length;
            this.low = low;
            this.high = high;
        }

        private void moveElements() {
            log.entering(Pack.class.getName(), "moveElements", new Object[] {low, high});

            // It is important to avoid contention over the shared counter numSelectedElements. Therefore,
            // you should avoid to frequently increment it inside the loop.
            int count = 0;
            for (int i = low; i < high; i++)
                if ((i == 0 && bitSum[i] > 0) || (i > 0 && bitSum[i - 1] < bitSum[i])) {
                    output[startOffsetOutput + bitSum[i] - 1] = input[i + startOffsetInput];
                    count++;
                }
            if (count > 0)
                numSelectedElements.addAndGet(count);
        }

        @Override
        protected void compute() {
            if (high - low <= SEQUENTIAL_CUTOFF)
                moveElements();
            else {
                int mid = low + (high - low) / 2;
                OutputBuilder left  = new OutputBuilder(low, mid);
                OutputBuilder right = new OutputBuilder(mid, high);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }

    static class Pair {
        final int[] output;
        final int numSelectedElements;

        Pair(int[] output, int numSelectedElements) {
            this.output = output;
            this.numSelectedElements = numSelectedElements;
            log.exiting(Pair.class.getName(), "Pair", numSelectedElements);
        }
    }

    private Pair buildOutput() {
        // Executes step 1 of the pack operation: generating the bit vector where 1 is at positions for all elements
        // that must be moved into the output.
        Arrays.parallelSetAll(bitSum, i -> predicate.test(input[i + startOffsetInput]) ? 1 : 0);

        // Executes step 2 of the pack operation: calculates prefix sum over the bit vector.
        Arrays.parallelPrefix(bitSum, (x, y) -> x + y);

        // Executes step 3 of the pack operation: produces the final output based upon the bit sum array.
        if (output == null)
            output = new int[bitSum[bitSum.length - 1]];
        ForkJoinPool.commonPool().invoke(new OutputBuilder(0, bitSum.length));
        return new Pair(output, numSelectedElements.get());
    }

    /**
     * Performs the parallel pack operation. The input array is processed inside the sub-range [low, high).
     *
     * @param input the read-only input array.
     * @param low the left endpoint of the input sub-range.
     * @param right the right endpoint of the input sub-range.
     * @param predicate the predicate function that determines whether to include or exclude an element.
     * @return the new output array with selected elements.
     * @throws IllegalArgumentException if the input array is empty or the sub-range is invalid.
     * @throws NullPointerException if the input array or predicate is <code>null</code>.
     */
    public static int[] parallelPack(int[] input, int low, int high, IntPredicate predicate) {
        if (input == null || predicate == null)
            throw new NullPointerException("You must supply an input array and predicate function!");
        if (input.length == 0)
            throw new IllegalArgumentException("You must pass a non-empty input array!");
        if (low < 0 || low >= high || high > input.length)
            throw new IllegalArgumentException("The sub-range [low, high) is invalid!");

        return new Pack(input, low, high, predicate).buildOutput().output;
    }

    /**
     * Performs the parallel pack operation. The input array is processed inside the sub-range [low, high).
     *
     * @param input the read-only input array.
     * @param low the left endpoint of the input sub-range.
     * @param right the right endpoint of the input sub-range.
     * @param predicate the predicate function that determines whether to include or exclude an element.
     * @param output the pre-allocated output array to store the selected elements.
     * @param startOffset the beginning offset into the output array, i.e., from this position will parallelPack put elements.
     * @return the number of selected elements.
     * @throws IllegalArgumentException if the input or output array is empty or the starting offset is invalid,
     * or the sub-range is invalid.
     * @throws NullPointerException if the input array, predicate, or output array is <code>null</code>.
     */
    public static int parallelPack(int[] input, int low, int high, IntPredicate predicate, int[] output, int startOffset) {
        if (input == null || predicate == null || output == null)
            throw new NullPointerException("You must supply an input array, predicate function, and output array!");
        if (input.length == 0 || output.length == 0)
            throw new IllegalArgumentException("You must pass non-empty input and output arrays!");
        if (low < 0 || low >= high || high > input.length)
            throw new IllegalArgumentException("The sub-range [low, high) is invalid!");
        if (startOffset < 0 || startOffset >= output.length)
            throw new IllegalArgumentException("The start offset is invalid!");

        return new Pack(input, low, high, predicate, output, startOffset).buildOutput().numSelectedElements;
    }
}
