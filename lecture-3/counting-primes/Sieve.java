/*
 * Source: http://faculty.knox.edu/dbunde/teaching/threadIntro/Sieve.java
 *
 * Alternate prime counting program using the Sieve of Eratosthenes.
 * Original version was written by John M. Hunt (John.Hunt@covenant.edu).
 *
 * Keeps an array of booleans indicating if each value is a prime.
 *
 * Note: this variant has the largest memory overhead O(N). There is no free lunch!
 */

public class Sieve {
    private static final int DEFAULT_UPPER_BOUND = 8000000;

    public static void printSieve(boolean a[]) {
        for (int i = 2; i < a.length; i++) {
            if (a[i] == true) {
                System.out.print(i + ", ");
            }
        }
    }

    public static int countSieve(boolean a[]) {
        int count = 0;
        for (int i = 2; i < a.length; i++) {
            if (a[i] == true) {
                count++;
            }
        }
        return count;
    }

    /*
     * For each identified prime (true value), cross out all of its multiples.
     */
    public static void runSieve(boolean a[]) {
        for (int i = 2; i < a.length; i++) {
            if (a[i] == true) {
                for (int n = i + i; n < a.length; n = n + i) {
                    a[n] = false;
                }
            }
        }
    }

    public static boolean[] initSieve(int size) {
        boolean nums[] = new boolean[size];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = true;
        }
        return nums;
    }

    public static void main(String args[]) {
        int size = DEFAULT_UPPER_BOUND;

        if (args.length == 1)
            size = Integer.parseInt(args[0]);

        boolean a[] = initSieve(size);
        runSieve(a);
        System.out.println("number of primes = " + countSieve(a));

        if (size <= 100)
            printSieve(a);
    }
}
