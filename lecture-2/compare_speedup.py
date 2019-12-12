from math import sqrt
from random import randint
from time import perf_counter
from spawn_processes import spawn_procs, wait_for_all


def slow_sum_of_sqrt_random_nums(n):
    s = 0
    for i in range(n):
        s += sqrt(randint(1, 100))
    return s


def speedup(num_processes, n):
    print("\nCalculating parallel speedup with %d processors for n=%d" % (num_processes, n))

    start_time = perf_counter()
    wait_for_all(spawn_procs(num_processes, slow_sum_of_sqrt_random_nums, n // num_processes))
    elapsed_par_time = perf_counter() - start_time
    print('The parallel variant took', elapsed_par_time, 'seconds.')

    start_time = perf_counter()
    slow_sum_of_sqrt_random_nums(n)
    elapsed_seq_time = perf_counter() - start_time
    print('The sequential variant took', elapsed_seq_time, 'seconds.')

    print('Speedup is', elapsed_seq_time / elapsed_par_time)


if __name__ == '__main__':
    speedup(2, 50)
    speedup(10, 50)
    speedup(2, 5000000)
    speedup(4, 5000000)
    speedup(10, 5000000)
