import unittest
from unittest import mock
import numpy as np
from conway_base import Cell, ConwayBase

class ConwayBaseTestCase(unittest.TestCase):
    def setUp(self):
        args = mock.Mock()
        args.n = 9
        args.interval = 200
        args.i = 1
        args.j = 1
        args.config_filename = 'patterns/exercise1.cells'
        self._game = ConwayBase(args)
        self._game.create_buffers()

    def test_board_is_properly_loaded(self):
        expected_board = np.full((11, 11), Cell.DEAD)
        expected_board[1, 1] = Cell.LIVE
        expected_board[1, 2] = Cell.LIVE
        expected_board[2, 1] = Cell.LIVE
        expected_board[2, 2] = Cell.LIVE
        expected_board[2, 5] = Cell.LIVE
        expected_board[2, 6] = Cell.LIVE
        expected_board[2, 7] = Cell.LIVE
        np.testing.assert_array_equal(expected_board, self._game._board)

    def test_board_is_properly_prepared_for_next_generation(self):
        self._game.prepare_next_board()
        expected_board = np.full((9, 9), Cell.DEAD)
        expected_board[0, 0] = Cell.LIVE
        expected_board[0, 1] = Cell.LIVE
        expected_board[1, 0] = Cell.LIVE
        expected_board[1, 1] = Cell.LIVE
        expected_board[0, 5] = Cell.LIVE
        expected_board[1, 5] = Cell.LIVE
        expected_board[2, 5] = Cell.LIVE
        np.testing.assert_array_equal(expected_board, self._game._get_unpadded_board())

if __name__ == '__main__':
    unittest.main()