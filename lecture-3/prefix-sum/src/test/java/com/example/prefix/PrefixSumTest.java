package com.example.prefix;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class PrefixSumTest extends CommonSetup {
    @Test(expected = IllegalArgumentException.class)
    public void failToComputePrefixSumWithNullInputArray() {
        PrefixSum.parallelPrefixSum(null, 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failToComputePrefixSumWithEmptyInputArray() {
        PrefixSum.parallelPrefixSum(new long[0], 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failToComputePrefixSumWithWrongCutoff() {
        PrefixSum.parallelPrefixSum(referenceInput, -1);
    }

    @Test
    public void generatePrefixSumWithMinimalCutoff() {
        long[] newOutput = referenceInput;
        PrefixSum.parallelPrefixSum(referenceInput, 1);
        assertArrayEquals(referenceOutput, newOutput);
    }

    @Test
    public void generatePrefixSumWithCutoffSmalledThanInputSize() {
        long[] newOutput = referenceInput;
        PrefixSum.parallelPrefixSum(referenceInput, referenceInput.length / 2);
        assertArrayEquals(referenceOutput, newOutput);
    }

    @Test
    public void generatePrefixSumWithDefaultCutoff() {
        long[] newOutput = referenceInput;
        PrefixSum.parallelPrefixSum(referenceInput, null);
        assertArrayEquals(referenceOutput, newOutput);
    }
}