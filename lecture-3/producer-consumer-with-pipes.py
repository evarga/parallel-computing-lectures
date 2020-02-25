# Idea for this example was based on the LinkedIn course "Python Parallel Programming Solutions" by Giancarlo Zaccone.

from random import randint
from multiprocessing import Pipe, Process 
from time import sleep


class Consumer(Process):
    def __init__(self, consumer_id, input_channel):
        Process.__init__(self)
        self._consumer_id = consumer_id
        self._input_channel = input_channel

    def consume(self):
        try:
            item = self._input_channel.recv()
            print("Consumer %d consumed item %d" % (self._consumer_id, item))
        except EOFError:
            print("No more items to consume for consumer %d!" % self._consumer_id)
        
    def run(self):
        for _ in range(20):
            sleep(randint(1, 5))
            self.consume()

            
class Producer(Process):
    def __init__(self, producer_id, output_channel):
        Process.__init__(self)
        self._producer_id = producer_id
        self._output_channel = output_channel

    def produce(self):
        item = randint(1, 100)
        print("Producer %d has produced item %d" % (self._producer_id, item))
        self._output_channel.send(item)

    def run(self):
        for i in range(20):
            sleep(randint(1, 5))
            self.produce()            


if __name__ == "__main__":
        input_channel1, output_channel1 = Pipe(False)
        input_channel2, output_channel2 = Pipe(False)
        try:
            producer1, producer2 = Producer(1, output_channel1), Producer(2, output_channel2)
            consumer1, consumer2 = Consumer(1, input_channel1), Consumer(2, input_channel2)
            consumer1.start()
            consumer2.start()
            producer1.start()
            producer2.start()
            producer1.join()
            producer2.join()
            consumer1.join()
            consumer2.join()
        finally:
            input_channel1.close()
            output_channel1.close()
            input_channel2.close()
            output_channel2.close()
        print("Simulation is done!")
