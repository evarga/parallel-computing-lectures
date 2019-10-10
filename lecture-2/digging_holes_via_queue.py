from multiprocessing import Lock, Queue
import time
from random import randint
from spawn_processes import spawn_procs, wait_for_all


def dig(q, lock, worker_name):
    """Performs the work of a digger."""

    hole_id_1 = q.get()        
    hole_id_2 = q.get()        
    with lock:
        print("Hiddy-ho! I'm worker", worker_name, 'and today I have to dig holes', (hole_id_1, hole_id_2))

    # Wait random number of seconds to simulate digging.
    time.sleep(randint(1, 5))
    with lock:
        print("Hurray! I'm worker", worker_name, 'and today I have finished my work on digging holes', (hole_id_1, hole_id_2))



def assign_diggers():
    """Assigns separate processes to simulate digging holes. It waits for workers to finish their job."""

    worker_names = [chr(ord('A') + i) for i in range(10)]
    lock = Lock()
    q = Queue()
    processes = []

    for worker_name in worker_names:
        processes.extend(spawn_procs(1, dig, q, lock, worker_name))

    # Wait two seconds to demonstrate that child processes will be blocked on get().
    time.sleep(2)

    # Send hole identifiers to diggers (each will pick up two holes to work on). Notice how these
    # numbers get mapped to diggers.
    for i in range(0, 2 * len(worker_names), 2):
        q.put(i)
        q.put(i + 1)
    q.close()

    wait_for_all(processes)
    print("All done!")


if __name__ == '__main__':
    assign_diggers()
