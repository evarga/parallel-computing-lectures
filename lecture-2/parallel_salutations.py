from multiprocessing import current_process, Lock
from spawn_processes import spawn_procs


def say_hi(lock, person_name):
    """Salutes the user with the hello message from the process identified by its name and ID."""
     
    p = current_process()
    with lock:
        print('Hi',  person_name, 'from process', p.name, 'with id =', p.pid)


if __name__ == '__main__':
    person_name = input('Enter your name: ')
    num_processes = int(input('Number of processes to create: '))

    lock = Lock()
    # Salute the user from the main process.
    say_hi(lock, person_name)
    spawn_procs(num_processes, say_hi, lock, person_name)
