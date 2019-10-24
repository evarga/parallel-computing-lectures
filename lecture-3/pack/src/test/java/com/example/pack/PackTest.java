package com.example.pack;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

import static org.junit.Assert.assertArrayEquals;

public class PackTest {
    private static final int[] REFERENCE_INPUT = new int[] {17, 4, 6, 8, 11, 5, 13, 13, 19, 0, 24};

    @BeforeClass
    public static void configureLogging() {
        System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getPath());
    }

    // Tests checking the application of defensive programming regarding arguments have been omitted for brevity.

    @Test
    public void preserveAllElementsLargerThanTenWithNewOutput() {
        int[] output = Pack.parallelPack(REFERENCE_INPUT, 0, REFERENCE_INPUT.length, v -> v > 10);
        assertArrayEquals(new int[] {17, 11, 13, 13, 19, 24}, output);
    }

    @Test
    public void preserveAllElementsLargerThanTenWithNewOutputInsideSubRange() {
        int[] output = Pack.parallelPack(REFERENCE_INPUT, 3, 7, v -> v > 10);
        assertArrayEquals(new int[] {11, 13}, output);
    }

    @Test
    public void preserveAllEvenElementsWithNewOutput() {
        int[] output = Pack.parallelPack(REFERENCE_INPUT, 0, REFERENCE_INPUT.length, v -> v % 2 == 0);
        assertArrayEquals(new int[] {4, 6, 8, 0, 24}, output);
    }

    // This class implements the parallel quick sort algorithm and serves as a test case for the Pack class.
    private static class ParallelQuickSort extends RecursiveAction {
        private static final Logger log = Logger.getLogger(ParallelQuickSort.class.getName());
        private static final Random rnd = new Random();

        private final int[] input;
        private final int[] output;
        private final int low;
        private final int high;

        ParallelQuickSort(int[] input, int[] output, int low, int high) {
            this.input = input;
            this.output = output;
            this.low = low;
            this.high = high;
        }

        // Partitions an array around the pivot element and returns an array whose first element is leftHigh
        // and second is rightLow. The left partition is [0, leftHigh) and the right one is [rightLow, N). N is
        // the length of the input array. The previous ranges are relative to the range [low, high).
        private int[] partitionAroundPivot() {
            final int pivot = input[low + rnd.nextInt(high - low)];

            int numSelectedElements = Pack.parallelPack(input, low, high, v -> v < pivot, output, low);
            int leftHigh = numSelectedElements;
            numSelectedElements += Pack.parallelPack(input, low, high, v -> v == pivot, output, low + numSelectedElements);
            int rightLow = numSelectedElements;
            if (low + numSelectedElements < output.length)
                Pack.parallelPack(input, low, high, v -> v > pivot, output, low + numSelectedElements);

            // We must ensure that input and output match for the sub-range [low, high), since they will be
            // swapped in the next iteration. This array copy operation just increases the constant factor, i.e.
            // doesn't impact the asymptotic running time.
            System.arraycopy(output, low, input, low, high - low);

            log.exiting(ParallelQuickSort.class.getName(), "partitionAroundPivot",
                    String.format("Pivot: %d\nInput([%d,%d]): %s\nOutput: %s",
                            Integer.valueOf(pivot), low, high, Arrays.toString(input), Arrays.toString(output)));
            return new int[] {leftHigh, rightLow};
        }

        @Override
        protected void compute() {
            if (high - low > 1) {
                int[] bounds = partitionAroundPivot();
                ParallelQuickSort left = new ParallelQuickSort(output, input, low, low + bounds[0]);
                ParallelQuickSort right = new ParallelQuickSort(output, input, low + bounds[1], high);
                left.fork();
                right.compute();
                left.join();
            }
        }

        public static void sort(int[] input) {
            ForkJoinPool.commonPool().invoke(new ParallelQuickSort(input, new int[input.length], 0, input.length));
        }
    }

    @Test
    public void sortArrayWithDuplicateElements() {
        int[] referenceInputCopy = Arrays.copyOf(REFERENCE_INPUT, REFERENCE_INPUT.length);
        ParallelQuickSort.sort(referenceInputCopy);
        assertArrayEquals(new int[] {0, 4, 5, 6, 8, 11, 13, 13, 17, 19, 24}, referenceInputCopy);
    }
}