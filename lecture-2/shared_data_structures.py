from multiprocessing import Queue
from random import randint
from spawn_processes import spawn_procs, wait_for_all


def add_item(ls):
    # List.append is thread-safe in Python, but here this doesn't apply, as each process works on its own local list.
    ls.append(randint(1, 100))


def shared_sequential_DS():
    ls = []
    add_item(ls)
    add_item(ls)
    print(ls)


def shared_parallel_DS_wrong():
    ls = []
    wait_for_all(spawn_procs(2, add_item, ls))
    print(ls)


def add_item_indirectly(q):
    q.put(randint(1, 100))
    q.close()


def shared_parallel_DS_correct():
    ls = []
    q = Queue()
    spawn_procs(2, add_item_indirectly, q)
    # There is no need to explicitly wait for children via join, since get() is a blocking command.
    ls.append(q.get())
    ls.append(q.get())    
    print(ls)


if __name__ == '__main__':
    print('Trying to populate the shared list from a sequential program.')
    shared_sequential_DS()
    print('\nTrying to populate the shared list from a parallel program (wrong way).')
    shared_parallel_DS_wrong()
    print('\nTrying to populate the shared list from a parallel program (correct way).')
    shared_parallel_DS_correct()
