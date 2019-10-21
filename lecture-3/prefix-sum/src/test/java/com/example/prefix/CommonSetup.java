package com.example.prefix;

import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Arrays;

public class CommonSetup {
    private static final long[] REFERENCE_INPUT = new long[] {6, 4, 16, 10, 16, 14, 2, -8, -10, 20, 5};
    private static final long[] REFERENCE_OUTPUT = new long[] {6, 10, 26, 36, 52, 66, 68, 60, 50, 70, 75};

    long[] referenceInput;
    long[] referenceOutput;

    @BeforeClass
    public static void configureLogging() {
        System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getPath());
    }

    @Before
    public void copyReferenceData() {
        referenceInput = Arrays.copyOf(REFERENCE_INPUT, REFERENCE_INPUT.length);
        referenceOutput = Arrays.copyOf(REFERENCE_OUTPUT, REFERENCE_OUTPUT.length);
    }
}
