"""Variant of the base class using less auxiliary memory for preparing the next board."""

import numpy as np
from conway_base import Cell, ConwayBase

class ConwayV2(ConwayBase):
    def prepare_next_board(self):
        row_upper = self._board[0].copy()
        row_central = self._board[1].copy()
        row_lower = self._board[2].copy()

        for i in range(1, self._n + 1):
            for j in range(1, self._n + 1):
                num_live_neighbors = row_upper[j - 1]   + row_upper[j] + row_upper[j + 1] + \
                                     row_central[j - 1] +                row_central[j + 1] + \
                                     row_lower[j - 1]   + row_lower[j] + row_lower[j + 1]

                if row_central[j] == Cell.LIVE:
                    if num_live_neighbors < 2 or num_live_neighbors > 3:
                        self._board[i, j] = Cell.DEAD
                else:
                    if num_live_neighbors == 3:
                        self._board[i, j] = Cell.LIVE

            row_upper[:] = row_central
            row_central[:] = row_lower
            if i < self._n:
                row_lower[:] = self._board[i + 2]

if __name__ == '__main__':
    game = ConwayV2(ConwayV2.parse_command_line_args())
    game.simulate()
