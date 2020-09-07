import java.util.BitSet;

public class Sieve {
    private static final int DEFAULT_UPPER_BOUND = 8000000;

    private final BitSet sieve;
    private final int upperBound;

    public Sieve(int n) {
        sieve = new BitSet(n);
        sieve.set(2, n);
        upperBound = (int) Math.ceil(Math.sqrt(n));
        findPrimes();
    }

    public void printPrimes() {
        System.out.println(sieve.toString());
    }

    public int getNumberOfPrimes() {
        return sieve.cardinality();
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

        Sieve sieve = new Sieve(size);
        System.out.println("Number of primes = " + sieve.getNumberOfPrimes());

        if (size <= 100)
            sieve.printPrimes();
    }
}
