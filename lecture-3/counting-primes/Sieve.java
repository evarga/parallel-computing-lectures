import java.util.BitSet;

public class Sieve implements PrimeFinder {
    private static final int DEFAULT_UPPER_BOUND = 8_000_000;

    private final BitSet sieve;
    private final int upperBound;
    private final int right;

    public Sieve(int n) {
        if (n < 3)
            throw new IllegalArgumentException("The search interval is [3, n].");
        right = n;
        if ((n & 1) == 1)
            n++;
        sieve = new BitSet(n);
        sieve.set(2, n);
        upperBound = (int) Math.ceil(Math.sqrt(n));
        findPrimes();
    }

    @Override
    public String printPrimes() {
        return sieve.toString();
    }

    @Override
    public int getNumberOfPrimes() {
        return sieve.cardinality();
    }

    @Override
    public int getRightEnd() {
        return right;
    }

    private void findPrimes() {
        for (int i = 2; i <= upperBound; i++)
            if (sieve.get(i))
                for (int j = i * i; j < sieve.length(); j += i)
                    sieve.clear(j);
    }

    public static void main(String args[]) {
        int size = DEFAULT_UPPER_BOUND;

        if (args.length == 1)
            size = Integer.parseInt(args[0]); // Assume size is >= 2.

        PrimeFinder sieve = new Sieve(size);
        System.out.println("Number of primes = " + sieve.getNumberOfPrimes());

        if (size <= 100)
            sieve.printPrimes();
    }
}
