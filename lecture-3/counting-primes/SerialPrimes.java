/*
 * Source: http://faculty.knox.edu/dbunde/teaching/threadIntro/SerialPrimes.java
 *
 * The easiest way to time execution is to open a terminal window in IntelliJ IDEA
 * and execute: time java -cp out/production/counting-primes/ SerialPrimes
 */

public class SerialPrimes {
    private static final long DEFAULT_UPPER_BOUND = 8000000L;

    // Returns whether the input number is prime or not.
    public static boolean isPrime(long num) {
        int limit = (int) Math.sqrt(num);

        for (long i = 3; i <= limit; i += 2) {
            if (num % i == 0)
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int pCount = 1;     // number of primes found (starting with 2)
        long nextCand = 3;  // next number to consider
        long upper_bound = DEFAULT_UPPER_BOUND;

        if (args.length == 1)
            upper_bound = Long.parseLong(args[0]);

        while (nextCand < upper_bound) {
            if (isPrime(nextCand)) {
                pCount++;
            }
            nextCand += 2;
        }

        System.out.println(pCount + " primes found");
    }
}
