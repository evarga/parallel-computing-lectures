from multiprocessing import Pool, cpu_count
import random, time, sys


# This method uses the same functional paradigm as Pool/map.
def monte_carlo_sim(num_trials):
    """Returns the number of x,y pairs in [0, 1] that lie inside the unit circle."""

    return sum(map(lambda t: 1 if t[0]**2 + t[1]**2 <= 1 else 0, 
                   map(lambda _: (random.random(), random.random()), range(num_trials))))


def estimate_pi(num_trials):
    def report_result(method, start_time, result):
        import unicodedata

        print("\n%s: With %d trials and in %f seconds %s = %f" % (method, 
                                                                num_trials, 
                                                                time.time() - start_time, 
                                                                unicodedata.lookup('GREEK SMALL LETTER PI'), 
                                                                result))

    # The trick below (passing the start time) only works in languages with eager evaluation of parameters.
    report_result('Sequential', 
                  time.time(), 
                  4 * monte_carlo_sim(num_trials) / num_trials)

    start_time = time.time()
    # Step 1: distribute work among processes into chunks as evenly as possible.
    num_cpus = cpu_count()
    print('\nNumber of cores =', num_cpus)
    trials = [num_trials // num_cpus] * num_cpus
    for i in range(num_trials % num_cpus):
        trials[i] += 1

    # Step 2: setup the pool of processes and handle chunks in parallel.
    pool = Pool(processes=num_cpus)
    subtotals = pool.map(monte_carlo_sim, trials)

    # Step 3: merge subtotals into the final result.
    report_result('Parallel',
                  start_time,
                  4 * sum(subtotals) / num_trials)


if __name__ == '__main__':
    if len(sys.argv) > 1:
        estimate_pi(int(sys.argv[1]))
    else:
        print('Usage: python3 estimate_pi.py <number of trials>')
        sys.exit(1)
