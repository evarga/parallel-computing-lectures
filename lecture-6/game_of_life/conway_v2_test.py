import unittest
from unittest import mock
from conway_base_test import ConwayBaseTestCase
from conway_v2 import ConwayV2

class ConwayV2TestCase(ConwayBaseTestCase):
    def setUp(self):
        args = mock.Mock()
        args.n = 9
        args.interval = 200
        args.i = 1
        args.j = 1
        args.config_filename = 'patterns/exercise1.cells'
        self._game = ConwayV2(args)
        self._game.create_buffers()

if __name__ == '__main__':
    unittest.main()