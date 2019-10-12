/*
 * Source: http://faculty.knox.edu/dbunde/teaching/threadIntro/ThreadedPrimes.java
 *
 * Version of threaded prime counter to give students. Doesn't (yet) work.
 */

public class ThreadedPrimesWrongV1 {
    private static final long DEFAULT_UPPER_BOUND = 8000000L;

    public static int pCount;

    static class PrimeFinder implements Runnable {
        private long from;
        private long to;

        public PrimeFinder(long from, long to) {
            this.from = from;
            this.to = to;
        }

        public void run() {
            long nextCand = from;

            while (nextCand < to) {
                if (isPrime(nextCand)) {
                    pCount++;
                }
                nextCand += 2;
            }
        }
    }

    public static boolean isPrime(long num) {
        int limit = (int) Math.sqrt(num);

        for (long i = 2; i <= limit; i++) {
            if(num % i == 0)
                return false;
        }
        return true;
    }

    public static void main(String args[]) throws InterruptedException {
        pCount = 1;
        long upper_bound = DEFAULT_UPPER_BOUND;

        if (args.length == 1)
            upper_bound = Long.parseLong(args[0]);

        long half = upper_bound / 2;
        if (half % 2 == 0)
            half++;

        PrimeFinder p1 = new PrimeFinder(3, half - 1);
        Thread t1 = new Thread(p1);
        PrimeFinder p2 = new PrimeFinder(half, upper_bound);
        Thread t2 = new Thread(p2);

        t1.start();
        t2.start();

        System.out.println(pCount + " primes found");
    }
}
