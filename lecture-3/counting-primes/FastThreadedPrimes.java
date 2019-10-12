/*
 * Solution for the HW assignment: http://faculty.knox.edu/dbunde/teaching/threadIntro/hw7.pdf.
 *
 * The fully multi-threaded solution would have a race condition when accessing and updating
 * elements of the primes array. Both readers and writes would need to use locks and the
 * incurred overhead would be much bigger than the benefits of using threads.
 */

import java.util.ArrayList;


public class FastThreadedPrimes {
	private static final long DEFAULT_UPPER_BOUND = 8000000L;
	static int sharedPCount;
	static Object lock = new Object();

	static class PrimeFinder implements Runnable {
		long from;
		long to;
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

			synchronized (lock) {
				sharedPCount += localPCount;
			}
		}
	}

    public static final ArrayList<Long> primes = new ArrayList<Long>();

	// Returns whether num is prime.
	// Requires that primes contains smaller primes in order.
	// Assumes that num is odd.
    public static boolean isPrime(long num) {
		long limit = (long) Math.sqrt(num);

		long val = 2L;
		int i = 1;  //start at 1 since 2 is first and num is odd
		while (i < primes.size() && (val = primes.get(i)) <= limit) {
			if (num % val == 0)
				return false;
			i++;
		}
		return true;
    }

	public static void main(String args[]) throws InterruptedException {
		primes.add(2L);
		long nextCand = 3;
		long upper_bound = DEFAULT_UPPER_BOUND;

		if (args.length == 1)
			upper_bound = Long.parseLong(args[0]);

		long threshold = (long) Math.sqrt(upper_bound);

		while (nextCand < threshold) {
			if (isPrime(nextCand)) {
				primes.add(nextCand);
			}
			nextCand += 2;
		}

		sharedPCount = primes.size();

		if (threshold % 2 == 0)
			threshold++;

		PrimeFinder p1 = new PrimeFinder(threshold, upper_bound);
		Thread t1 = new Thread(p1);
		PrimeFinder p2 = new PrimeFinder(threshold + 2, upper_bound);
		Thread t2 = new Thread(p2);

		t1.start();
		t2.start();

		t1.join();
		t2.join();

		System.out.println(sharedPCount + " primes found");
    }
}
