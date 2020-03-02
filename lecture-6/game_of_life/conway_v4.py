"""
The GPU variant of the base class. The visualization is a bit slower, since we all the time 
transfer content from device into host memory.

Try executing this program with:
python3 conway_v4.py --board-size 160 --interval 20 --configuration patterns/garden-of-eden.cells 30 30
"""

import numpy as np
import cupy as cp
import dask.array as da
from cupyx.scipy.ndimage import convolve
from conway_base import Cell
from conway_v3 import ConwayV3


class ConwayV4(ConwayV3):
    def get_real_board(self):
        return self.board[1:(self._n + 1), 1:(self._n + 1)].map_blocks(cp.asnumpy)

    def create_buffers(self):
        self.board = da.from_array(self.board, chunks=("auto", "auto")).map_blocks(cp.asarray)
        self._mask = cp.ones((3, 3))
        self._mask[1, 1] = 0

    def prepare_next_board(self, steps):
        for _ in range(steps):
            num_live_neighbors = self.board.map_overlap(convolve, depth=1, boundary='none', 
                                                        weights=self._mask, mode='constant', cval=0, 
                                                        dtype=cp.ndarray)
            next_board = num_live_neighbors.map_blocks(self._process_cell, dtype=cp.int).compute()
            self.board = da.from_array(next_board, chunks=("auto", "auto"))
        return cp.asnumpy(next_board)


if __name__ == '__main__':
    game = ConwayV4(ConwayV4.parse_command_line_args())
    game.simulate()
