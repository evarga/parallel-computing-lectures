"""
Variant of the base class with parallelized, pipelined, and vectorized operations.

The technique pertaining to convolution was reused from https://stackoverflow.com/a/36968434.

Try executing this program with (the other variants will crawl):
python3 conway_v3.py --board-size 160 --interval 20 --configuration patterns/garden-of-eden.cells 30 30
"""

import numpy as np
import dask.array as da
from scipy.ndimage import convolve
from conway_base import Cell, ConwayBase


class ConwayV3(ConwayBase):
    def create_buffers(self):
        self.board = da.from_array(self.board, chunks=("auto", "auto"))
        self._mask = np.ones((3, 3))
        self._mask[1, 1] = 0

    def _process_cell(self, block, block_id=None):
        rows, cols = block.shape
        start_row = block_id[0] * rows
        start_col = block_id[1] * cols
        # We presume that this slicing will fit into memory.
        board_slice = self.board[start_row:(start_row + rows), start_col:(start_col + cols)].compute()

        # Apply the rules of the game.
        block[np.logical_or(block < 2, block > 3)] = Cell.DEAD
        block[block == 3] = Cell.LIVE
        block[block == 2] = board_slice[block == 2]
        return block

    def prepare_next_board(self, steps):
        for _ in range(steps):
            num_live_neighbors = self.board.map_overlap(convolve, depth=1, boundary='none', 
                                                        weights=self._mask, mode='constant', cval=0)
            next_board = num_live_neighbors.map_blocks(self._process_cell, dtype=np.int).compute()
            self.board = da.from_array(next_board, chunks=("auto", "auto"))
        return next_board


if __name__ == '__main__':
    game = ConwayV3(ConwayV3.parse_command_line_args())
    game.simulate()
