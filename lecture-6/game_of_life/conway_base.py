"""
A class that serves as a baseline for Conway's Game of Life. It has a template method next_generation 
that is supposed to be inherited by advanced child classes. The basic version contains a simple 
sequential algorithm to advance cells. Furthermore, it also contains some visualization routines.

The initial board configuration is loaded from an external textual file that follows the format as 
described at https://www.conwaylife.com (you can also download myriad of patterns). The RLE
compressed format isn't supported yet.

The starting point for this implementation was https://github.com/electronut/pp/blob/master/conway/conway.py.
"""

from enum import IntEnum
import sys, argparse
import numpy as np
import matplotlib.pyplot as plt 
import matplotlib.animation as animation

class Cell(IntEnum):
    LIVE = 1
    DEAD = 0

class ConwayBase():
    def __init__(self, args):
        self._n = args.n
        self._board = np.zeros((self._n, self._n), dtype = np.int)
        self._interval = args.interval
        self._movie_filename = args.movie_filename
        self._start_pos_y = args.i
        self._start_pos_x = args.j
        self._load_configuration(args.config_filename)

    def _load_configuration(self, config_filename):
        i = self._start_pos_y
        j = self._start_pos_x
        with open(config_filename, 'r') as f:
            for line in f:
                # If the current line is a comment, then skip it.
                if line[0] == '!':
                    continue
                for sym in line:
                    if sym == 'O':
                        self._board[i, j] = Cell.LIVE
                    j += 1
                i += 1
                j = self._start_pos_x

    def simulate(self):
        self._create_buffers()

        fig, self._ax = plt.subplots()
        self._ax.axis('off')
        ani = animation.FuncAnimation(fig,
                                      self._next_generation,
                                      init_func=self._init_animation,
                                      interval=self._interval,
                                      blit=True,
                                      save_count=100)

        if self._movie_filename:
            Writer = animation.writers['ffmpeg']
            writer = Writer(fps=10, bitrate=1800)
            ani.save(self._movie_filename + '.mp4', writer=writer)

        plt.show()

    def _init_animation(self):
        self._img = self._ax.imshow(self._get_real_board(), interpolation='nearest')
        return self._img,

    def _next_generation(self, frame):
        # Return the initial image on the first frame. Otherwise, we will loose it from the recorded movie file.
        if frame == 0:
            return self._img,

        self._img.set_data(self._prepare_next_board())
        return self._img,

    def _get_real_board(self):
        """Retrieves the real board as a NumPy array (without any extras added)."""

        return self._board[1:(self._n + 1), 1:(self._n + 1)]        

    def _create_buffers(self):
        """This method should create necessary buffers to be used by the prepare_next_board method."""

        self._board = np.pad(self._board, 1, 'constant')
        self._next_board = np.empty(self._board.shape, self._board.dtype)

    def _prepare_next_board(self):
        """
        Prepares the padded board in-place to reflect the next generation of cells. 
        This method is supposed to be overridden by child classes together with create_buffers.
        """

        self._next_board[:, :] = self._board
        for i in range(1, self._n + 1):
            for j in range(1, self._n + 1):
                num_live_neighbors = self._board[i, j - 1] + self._board[i, j + 1] + \
                                     self._board[i - 1, j] + self._board[i + 1, j] + \
                                     self._board[i - 1, j - 1] + self._board[i - 1, j + 1] + \
                                     self._board[i + 1, j - 1] + self._board[i + 1, j + 1]

                if self._board[i, j] == Cell.LIVE:
                    if num_live_neighbors < 2 or num_live_neighbors > 3:
                        self._next_board[i, j] = Cell.DEAD
                else:
                    if num_live_neighbors == 3:
                        self._next_board[i, j] = Cell.LIVE
        self._board[:, :] = self._next_board
        return self._get_real_board()

    @staticmethod
    def parse_command_line_args():
        parser = argparse.ArgumentParser(
            description="Runs Conway's Game of Life simulation as described in the TPDC book (chapter 10).",
            epilog='By default the board will contain objects as given in Exercise 1 from the book.')
        parser.add_argument(
            'i',
            metavar='X',
            type=int,
            help='The upper left x coordinate of the pattern.')
        parser.add_argument(
            'j',
            metavar='Y',
            type=int,
            help='The upper left y coordinate of the pattern.')
        parser.add_argument(
            '--configuration',
            metavar='filename',
            default='exercise1.cells',
            dest='config_filename',
            help='The board configuration (pattern) filename.')
        parser.add_argument(
            '--board-size', 
            type=int, 
            dest='n',
            metavar='N',
            default=50,
            help='The size of the square board in number of cells. The upper left corner has coordinates (0, 0).')
        parser.add_argument(
            '--movie', 
            metavar='filename',
            dest='movie_filename',
            help='The name (without extension) of the movie file in MP4 format to store the simulation result.')
        parser.add_argument(
            '--interval', 
            type=int,
            dest='interval',
            metavar='N',
            default=200,
            help='The refresh interval (speed of simulation) in milliseconds.')
        return parser.parse_args()

if __name__ == '__main__':
    game = ConwayBase(ConwayBase.parse_command_line_args())
    game.simulate()
