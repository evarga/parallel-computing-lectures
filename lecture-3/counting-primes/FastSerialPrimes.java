/*
 * Source: http://faculty.knox.edu/dbunde/teaching/threadIntro/FastSerialPrimes.java
 *
 * Faster version of SerialPrimes.java that exploits fact that you can
 * just test for divisibility by smaller primes.  This version has an additional
 * memory overhead O(π(N)), where π is the prime-counting function.
 */

import java.util.*;


public class FastSerialPrimes {
	private static final long DEFAULT_UPPER_BOUND = 8000000L;
	public static ArrayList<Long> primes;

	// Returns whether num is prime.
	// Requires that primes contains smaller primes in order.
	// Assumes that num is odd.
    public static boolean isPrime(long num) {
		long limit = (long) Math.sqrt(num);

		long val;
		int i = 1;  //start at 1 since 2 is first and num is odd
		while (i < primes.size() && (val = primes.get(i)) <= limit) {
			if (num % val == 0)
				return false;
			i++;
		}
		return true;
    }

    public static void main(String args[]) {
		primes = new ArrayList<Long>();
		primes.add(2L);
		long nextCand = 3;
		long upper_bound = DEFAULT_UPPER_BOUND;

		if (args.length == 1)
			upper_bound = Long.parseLong(args[0]);

		while (nextCand < upper_bound) {
			if (isPrime(nextCand)) {
				primes.add(nextCand);
			}
			nextCand += 2;
		}

		System.out.println(primes.size() + " primes found");
    }
}
