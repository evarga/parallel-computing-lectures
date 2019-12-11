from multiprocessing import Pool, cpu_count
import random, time, sys


def riemann_sum(f, a, b, h):
    """Computes the integral of f in [a, b] with step size h."""

    assert h > 0, 'Step size must be a positive number!'
    s = 0
    x = a + h / 2
    while x < b:
        s += h * f(x)
        x += h
    return s


def process_chunk(t):
    return riemann_sum(*t)


def linspace(a, b, num_chunks):
    """Returns equidistant steps in [a, b] whose number matches num_chunks."""

    assert isinstance(num_chunks, int) and num_chunks > 0, 'Number of chunks must be a natural number!'
    h = (b - a) / num_chunks
    return [a + i * h for i in range(num_chunks + 1)]


def integrate(f, a, b, num_slices):
    """Computes the integral of f in [a, b] using Riemann summation with the given number of slices."""

    def report_result(method, start_time, result):
        import unicodedata

        print("\n%s: With %d slices and in %f seconds %s = %f" % (method, 
                                                                  num_slices, 
                                                                  time.time() - start_time, 
                                                                  unicodedata.lookup('INTEGRAL'), 
                                                                  result))

    assert isinstance(num_slices, int) and num_slices > 0, "Number of slices must be a natural number!"
    h = (b - a) / num_slices

    # The trick below (passing the start time) only works in languages with eager evaluation of parameters.
    report_result('Sequential', 
                  time.time(), 
                  riemann_sum(f, a, b, h))

    start_time = time.time()
    # Step 1: distribute work among processes into chunks as evenly as possible.
    num_cpus = cpu_count()
    print('\nNumber of cores =', num_cpus)
    endpoints = linspace(a, b, num_cpus)
    args = []
    for i in range(num_cpus):
        args.append((f, endpoints[i], endpoints[i + 1], h))

    # Step 2: setup the pool of processes and handle chunks in parallel.
    pool = Pool(processes=num_cpus)
    subtotals = pool.map(process_chunk, args)
    pool.close()

    # Step 3: merge subtotals into the final result.
    report_result('Parallel',
                  start_time,
                  sum(subtotals))


if __name__ == '__main__':
    from math import sin

    if len(sys.argv) > 1:
        integrate(sin, 0, 1, int(sys.argv[1]))
    else:
        print('Usage: python3 integrate.py <number of slices>')
        sys.exit(1)
