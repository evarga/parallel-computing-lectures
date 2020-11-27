/**
 * API for a prime finder instance that returns the number of primes in the given interval
 * and prints them out. Objects are supposed to be created via the matching factory.
 */
public interface PrimeFinder {
    /**
     * Returns the primes found by this instance for the given interval [3, n].
     *
     * @return the string printout of primes.
     * @see #getRightEnd()
     */
    String printPrimes();

    /**
     * Returns the number of primes found by this instance for the given interval.
     *
     * @return the number of prime numbers in the interval [3, n].
     * @see #getRightEnd()
     */
    int getNumberOfPrimes();

    /**
     * Returns back the right side of the search interval.
     *
     * @return the right end of the search interval. The lower bound is always 3.
     */
    int getRightEnd();
}
