import time
from spawn_processes import spawn_procs, wait_for_all


def slow_sum_1_to_n(n):
    s = 0
    for i in range(1, n + 1):
        s += i
    return s


def time_process(n):
    print('Calculating time for n =', n)
    start_time = time.time()
    wait_for_all(spawn_procs(1, slow_sum_1_to_n, n))
    elapsed_time = time.time() - start_time
    print('The process took', elapsed_time, 'seconds.')


if __name__ == '__main__':
    # What is happening for n=50 and n=100?
    time_process(50)
    time_process(100)
    time_process(5000000)
    time_process(50000000)
