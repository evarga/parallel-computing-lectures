"""Variant of the base class using less auxiliary memory for preparing the next board."""

import numpy as np
from conway_base import Cell, ConwayBase

class ConwayV2(ConwayBase):
    def create_buffers(self):
        self._row_upper = np.empty(self._board.shape[1], self._board.dtype)
        self._row_central = np.empty(self._board.shape[1], self._board.dtype)
        self._row_lower = np.empty(self._board.shape[1], self._board.dtype)

    def prepare_next_board(self):
        self._row_upper[:] = self._board[0]
        self._row_central[:] = self._board[1]
        self._row_lower[:] = self._board[2]

        for i in range(1, self._n + 1):
            for j in range(1, self._n + 1):
                num_live_neighbors = self._row_upper[j - 1]   + self._row_upper[j] + self._row_upper[j + 1] + \
                                     self._row_central[j - 1] +                      self._row_central[j + 1] + \
                                     self._row_lower[j - 1]   + self._row_lower[j] + self._row_lower[j + 1]

                if self._row_central[j] == Cell.LIVE:
                    if num_live_neighbors < 2 or num_live_neighbors > 3:
                        self._board[i, j] = Cell.DEAD
                else:
                    if num_live_neighbors == 3:
                        self._board[i, j] = Cell.LIVE

            self._row_upper[:] = self._row_central
            self._row_central[:] = self._row_lower
            if i < self._n:
                self._row_lower[:] = self._board[i + 2]

if __name__ == '__main__':
    game = ConwayV2(ConwayV2.parse_command_line_args())
    game.simulate()
