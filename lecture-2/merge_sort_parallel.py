from multiprocessing import Pool, cpu_count
import random, time, sys
# This import is for convenience, but signifies that linspace should probably move into a utility module.
from integrate import linspace


def merge(left, right):
    """Merges two sorted sublists into a single sorted list."""

    ret = []
    li = ri =0

    while li < len(left) and ri < len(right):
        if left[li] <= right[ri]:
            ret.append(left[li])
            li += 1
        else:
            ret.append(right[ri])
            ri += 1
    if li == len(left):
        ret.extend(right[ri:])
    else:
        ret.extend(left[li:])
    return ret


def merge_sort(lst):
    """Sorts the input list into ascending order."""

    if len(lst) < 2:
        return lst

    half = len(lst) // 2
    # This variant of merge sort uses O(N * log N) memory, since list slicing in Python 3 creates a copy.
    return merge(merge_sort(lst[:half]), merge_sort(lst[half:]))


def is_sorted(lst):
    """Checks whether the input list is sorted."""

    for i in range(len(lst) - 1):
        if lst[i] > lst[i + 1]:
            print(lst)
            return False
    return True


def merge_chunk(t):
    return merge(*t)


def merge_sort_parallel(lst, num_chunks=cpu_count()):
    """Sorts the input list by using num_chunks child processes."""

    if len(lst) < 2:
        return lst

    start_time = time.time()
    # Step 1: distribute work among processes into chunks as evenly as possible.
    endpoints = list(map(int, linspace(0, len(lst), num_chunks)))
    args = [lst[endpoints[i]:endpoints[i + 1]] for i in range(num_chunks)]

    # Step 2: setup the pool of processes and handle chunks in parallel.
    pool = Pool(processes=num_chunks)
    sublists = pool.map(merge_sort, args)

    # Step 3: merge sublist into the final result.
    while len(sublists) > 1:
        args = [(sublists[i], sublists[i + 1]) for i in range(0, len(sublists), 2)]
        sublists = pool.map(merge_chunk, args)

    print('Sorting list of', len(lst), 'elements took', time.time() - start_time, 'seconds.')
    return sublists[0]


if __name__ == '__main__':
    if len(sys.argv) > 1:
        n = int(sys.argv[1])
        assert is_sorted(merge_sort_parallel([random.random() for i in range(n)]))
    else:
        print('Usage: python3 merge_sort_parallel.py <number of elements to sort>')
        sys.exit(1)
