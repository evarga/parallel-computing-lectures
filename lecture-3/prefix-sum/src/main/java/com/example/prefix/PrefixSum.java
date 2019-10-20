package com.example.prefix;

import java.util.Optional;
import java.util.function.LongBinaryOperator;
import java.util.logging.Logger;

/**
 * Computes the sum of every prefix of the input array and returns them in an output array. The class assumes an
 * inclusive sum. The aim here is to showcase how parallel prefix sum is implemented under the hood using the
 * Fork-join paradigm. Java SE 8 already comes with a more generic parallel prefix implementation.
 *
 * @see java.util.Arrays#parallelPrefix(long[], LongBinaryOperator)
 */
public final class PrefixSum {
    private static final Logger log = Logger.getLogger(PrefixSum.class.getName());
    private static final int DEFAULT_SEQUENTIAL_CUTOFF = 500;

    /**
     * Performs the parallel prefix sum operation in-place over the given array.
     *
     * @param input the input array for which to calculate the prefix sum.
     * @param sequentialCutoff the optional threshold (number of elements) below which summation should be done sequentially.
     * @throws IllegalArgumentException if the input array is <code>null</code>/empty or the cutoff value is not a natural number.
     */
    public static void parallelPrefixSum(long[] input, Integer sequentialCutoff) {
        if (input == null || input.length == 0)
            throw new IllegalArgumentException("You must pass a valid input array!");
        sequentialCutoff = Optional.ofNullable(sequentialCutoff).orElse(PrefixSum.DEFAULT_SEQUENTIAL_CUTOFF);
        if (sequentialCutoff < 1)
            throw new IllegalArgumentException("The cutoff must be a natural number!");
        log.fine("PrefixSum created with cutoff = " + sequentialCutoff);

        Tree tree = new UpPass(input, sequentialCutoff).buildTree();
        long[] output = input;
        new DownPass(tree, input, output).buildOutput();
    }
}
