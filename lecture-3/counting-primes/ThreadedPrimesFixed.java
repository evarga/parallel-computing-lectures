/*
 * Source: http://faculty.knox.edu/dbunde/teaching/threadIntro/ThreadedPrimesFixed.java
 *
 * Version of ThreadedPrimes.java that fixes its defects.  Has each
 * thread keep a private count of the number of primes found,
 * combining the results only at the end.  Also, divides the work
 * between the threads more evenly (the threads test 4k+1 and 4k+3
 * instead of giving one thread all the low numbers and the other all
 * the high numbers).
 */

public class ThreadedPrimesFixed {
    private static final long DEFAULT_UPPER_BOUND = 8000000L;
    private static int sharedPCount;
    private static final Object lock = new Object();

    static class PrimeFinder implements Runnable {
        private final long from;
        private final long to;
        int localPCount;

        public PrimeFinder(long from, long to) {
            this.from = from;
            this.to = to;
        }

        public void run() {
            long nextCand = from;

            while (nextCand < to) {
                if (isPrime(nextCand)) {
                    localPCount++;
                }
                nextCand += 4;
            }

            synchronized(lock) {
                sharedPCount += localPCount;
            }
        }
    }

    public static boolean isPrime(long num) {
        if (num % 2 == 0)
            return false;

        int limit = (int) Math.sqrt(num);
        for (long i = 3; i <= limit; i += 2) {
            if (num % i == 0)
                return false;
        }
        return true;
    }

    public static void main(String args[]) throws InterruptedException {
        sharedPCount = 1;
        long upper_bound = DEFAULT_UPPER_BOUND;

        if (args.length == 1)
            upper_bound = Long.parseLong(args[0]);

        PrimeFinder p1 = new PrimeFinder(3, upper_bound);
        Thread t1 = new Thread(p1);
        PrimeFinder p2 = new PrimeFinder(5, upper_bound);
        Thread t2 = new Thread(p2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(sharedPCount + " primes found");
    }
}
