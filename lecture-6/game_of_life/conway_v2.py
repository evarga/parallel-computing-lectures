"""
Variant of the base class using less auxiliary memory for preparing the next board.
This approach may improve a bit the performance, too.
"""

import numpy as np
from conway_base import Cell, ConwayBase


class ConwayV2(ConwayBase):
    def create_buffers(self):
        self.board = np.pad(self.board, 1, 'constant')
        self._row_upper = np.empty(self.board.shape[1], self.board.dtype)
        self._row_central = np.empty(self.board.shape[1], self.board.dtype)
        self._row_lower = np.empty(self.board.shape[1], self.board.dtype)

    # If we would like to apply loop unrolling to speed things up (by calculating
    # two time steps at once), then we would need a patch of 5x5. The number of
    # such different patches is 2**25 = 33,554,432. The single time step patch of 3x3
    # was easy to handle, since all 2**9 possibilities had been expressed via
    # intuitive rules. This isn't the case with 5x5, though. One way would be to store
    # all transitions inside a hash map and perform fast lookups.
    #
    # HOMEWORK
    # --------
    # Apply the above mentioned preprocessing idea for our classical 3x3 patch and observe
    # the speedup.
    # 
    # Hint: https://stackoverflow.com/questions/16589791/most-efficient-property-to-hash-for-numpy-array
    def prepare_next_board(self, steps):
        for _ in range(steps):
            self._row_upper[:] = self.board[0]
            self._row_central[:] = self.board[1]
            self._row_lower[:] = self.board[2]

            for i in range(1, self._n + 1):
                for j in range(1, self._n + 1):
                    num_live_neighbors = self._row_upper[j - 1]   + self._row_upper[j] + self._row_upper[j + 1] + \
                                         self._row_central[j - 1] +                      self._row_central[j + 1] + \
                                         self._row_lower[j - 1]   + self._row_lower[j] + self._row_lower[j + 1]

                    if self._row_central[j] == Cell.LIVE:
                        if num_live_neighbors < 2 or num_live_neighbors > 3:
                            self.board[i, j] = Cell.DEAD
                    else:
                        if num_live_neighbors == 3:
                            self.board[i, j] = Cell.LIVE

                self._row_upper[:] = self._row_central
                self._row_central[:] = self._row_lower
                if i < self._n:
                    self._row_lower[:] = self.board[i + 2]
        return self.get_real_board()


if __name__ == '__main__':
    game = ConwayV2(ConwayV2.parse_command_line_args())
    game.simulate()
