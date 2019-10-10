from multiprocessing import Process


# Thankfully to the usage of variable number of arguments, spawn_procs is oblivious what
# is passed to the target procedure of a process (future extensions of it would not impact spawn_procs).
def spawn_procs(num_processes, target, *args):
    """
    Explicitly spawns specified number of independent child processes. On a multi-core
    CPU they may run in parallel. The Process objects are returned inside a list.
    """

    processes = []
    for i in range(num_processes):
        p = Process(target=target, name="child-%d" % i, args=args)
        processes.append(p)
        p.start()
    return processes

def wait_for_all(processes):
    """Waits for all processes to finish their work."""

    for p in processes:
        p.join()
