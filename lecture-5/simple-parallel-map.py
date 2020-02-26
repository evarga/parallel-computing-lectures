from mpi4py import MPI

ROOT_RANK = 0

comm = MPI.COMM_WORLD
size = comm.Get_size()
rank = comm.Get_rank()

# This is the set of initial numbers to be squared in parallel.
numbers = list(range(1, size + 1)) if rank == ROOT_RANK else None

# Distribute the numbers among available processors.
number = comm.scatter(numbers, root=ROOT_RANK)
print("Process = %d" % rank + " received  = %d " % number)

# Send back the squared result to the root process.
squared_numbers = comm.gather(number * number, root=ROOT_RANK)
if rank == ROOT_RANK:
    print("Sum of squared numbers is %d" % sum(squared_numbers))
