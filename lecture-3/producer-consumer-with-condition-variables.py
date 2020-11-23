# Idea for this example was based on the LinkedIn course "Python Parallel Programming Solutions" by Giancarlo Zaccone.

from random import randint
from threading import Condition, RLock, Thread 
from time import sleep

items = []
items_lock = RLock()
buffer_not_empty, buffer_not_full = Condition(items_lock), Condition(items_lock)


class Consumer(Thread):
    def __init__(self, consumer_id):
        Thread.__init__(self)
        self._consumer_id = consumer_id

    def consume(self):
        with items_lock:
            while not items:
                print("Consumer %d is waiting..." % self._consumer_id)
                buffer_not_empty.wait()
            print("Consumer %d consumed item %d" % (self._consumer_id, items.pop()))
            buffer_not_full.notify()

    def run(self):
        for _ in range(20):
            sleep(randint(1, 5))
            self.consume()


class Producer(Thread):
    def __init__(self, producer_id):
        Thread.__init__(self)
        self._producer_id = producer_id

    def produce(self):
        with items_lock:
            while len(items) == 10:
                print("Producer %d is waiting..." % self._producer_id)
                buffer_not_full.wait()
            item = randint(1, 100)
            print("Producer %d has produced item %d" % (self._producer_id, item))
            items.append(item)
            buffer_not_empty.notify()

    def run(self):
        for i in range(20):
            sleep(randint(1, 5))
            self.produce()


if __name__ == "__main__":
        producer1, producer2 = Producer(1), Producer(2)
        consumer1, consumer2 = Consumer(1), Consumer(2)
        producer1.start()
        producer2.start()
        consumer1.start()
        consumer2.start()
        producer1.join()
        producer2.join()
        consumer1.join()
        consumer2.join()
        print("Simulation is done!")
