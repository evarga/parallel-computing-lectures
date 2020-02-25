# Idea for this example was based on the LinkedIn course "Python Parallel Programming Solutions" by Giancarlo Zaccone.

from queue import Queue
from random import randint
from threading import Thread 
from time import sleep


class Consumer(Thread):
    def __init__(self, consumer_id, queue):
        Thread.__init__(self)
        self._consumer_id = consumer_id
        self._queue = queue

    def consume(self):
        item = self._queue.get()
        print("Consumer %d consumed item %d" % (self._consumer_id, item))
        self._queue.task_done()
        
    def run(self):
        for _ in range(20):
            sleep(randint(1, 5))
            self.consume()

            
class Producer(Thread):
    def __init__(self, producer_id, queue):
        Thread.__init__(self)
        self._producer_id = producer_id
        self._queue = queue

    def produce(self):
        item = randint(1, 100)
        print("Producer %d has produced item %d" % (self._producer_id, item))
        self._queue.put(item)

    def run(self):
        for i in range(20):
            sleep(randint(1, 5))
            self.produce()            


if __name__ == "__main__":
        queue = Queue()
        producer1, producer2 = Producer(1, queue), Producer(2, queue)
        consumer1, consumer2 = Consumer(1, queue), Consumer(2, queue)
        producer1.start()
        producer2.start()
        consumer1.start()
        consumer2.start()
        producer1.join()
        producer2.join()
        consumer1.join()
        consumer2.join()
        print("Simulation is done!")
