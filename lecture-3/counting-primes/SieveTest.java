import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SieveTest {
    @ParameterizedTest
    @MethodSource("createSieveExamples")
    void countPrimes(int expected, int example) {
        assertEquals(expected, new Sieve(example).getNumberOfPrimes());
    }

    private static Stream<Arguments> createSieveExamples() {
        return Stream.of(
                Arguments.of(2, 3),
                Arguments.of(2, 4),
                Arguments.of(4, 10),
                Arguments.of(25, 100),
                Arguments.of(539_777, 8_000_000)
        );
    }

    @Test
    void retrieveRightSideOfInterval() {
        assertEquals(7, new Sieve(7).getRightEnd());
    }

    @Test
    void printPrimes() {
        assertEquals("{2, 3, 5, 7}", new Sieve(10).printPrimes());
    }
}