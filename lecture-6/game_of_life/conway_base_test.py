import unittest
from unittest import mock
import numpy as np
from conway_base import Cell, ConwayBase


class ConwayBaseTestCase(unittest.TestCase):
    def setUp(self):
        args = mock.Mock()
        args.n = 9
        args.interval = 200
        args.i = 0
        args.j = 0
        args.config_filename = 'patterns/exercise1.cells'
        self._game = ConwayBase(args)

    def test_board_is_properly_loaded(self):
        expected_board = np.full((9, 9), Cell.DEAD)
        expected_board[0, 0] = Cell.LIVE
        expected_board[0, 1] = Cell.LIVE
        expected_board[1, 0] = Cell.LIVE
        expected_board[1, 1] = Cell.LIVE
        expected_board[1, 4] = Cell.LIVE
        expected_board[1, 5] = Cell.LIVE
        expected_board[1, 6] = Cell.LIVE
        np.testing.assert_array_equal(expected_board, self._game.board)

    def test_board_is_properly_prepared_for_next_generation(self):
        self._game.create_buffers()
        expected_board = np.full((9, 9), Cell.DEAD)
        expected_board[0, 0] = Cell.LIVE
        expected_board[0, 1] = Cell.LIVE
        expected_board[1, 0] = Cell.LIVE
        expected_board[1, 1] = Cell.LIVE
        expected_board[0, 5] = Cell.LIVE
        expected_board[1, 5] = Cell.LIVE
        expected_board[2, 5] = Cell.LIVE
        np.testing.assert_array_equal(expected_board, self._game.prepare_next_board(1))


if __name__ == '__main__':
    unittest.main()