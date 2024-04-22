package rafael.ktgenetic.nqueens

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BoardTest {

    private fun testIrregularBoards(vararg values: Int) {
        assertThrows(IllegalArgumentException::class.java) {
            val b = Board(values.toList(), Piece.QUEEN)

            fail(b.toString())
        }
    }

    @Test
    fun repeatedValues1() {
        testIrregularBoards(0, 1, 1)
    }

    @Test
    fun repeatedValues2() {
        testIrregularBoards(0, 1, 2, 5, 4, 3, 2)
    }

    @Test
    fun valuesOutOfRange1() {
        testIrregularBoards(0, 10, 1)
    }

    @Test
    fun valuesOutOfRange2() {
        testIrregularBoards(0, -2, 1)
    }


    @Test
    fun `board initialization should validate board`() {
        val exception = assertThrows<IllegalArgumentException> {
            Board(listOf(0, 0, 2, 3, 4), Piece.QUEEN)
        }
        assertEquals("Repeated Value: 0", exception.message)
    }

    @Test
    fun `collisions should be empty for a board with no collisions`() {
        val board = Board(listOf(0, 2, 4, 1, 3), Piece.QUEEN)
        assertTrue(board.collisions.isEmpty())
    }

    @Test
    fun `numOfCollisions should not be zero for a board with collisions`() {
        val board = Board(listOf(0, 1, 2, 3, 4), Piece.QUEEN)
        assertNotEquals(0, board.numOfCollisions)
    }

    @Test
    fun `compareTo should return zero for two identical boards`() {
        val board1 = Board(listOf(0, 2, 4, 1, 3), Piece.QUEEN)
        val board2 = Board(listOf(0, 2, 4, 1, 3), Piece.QUEEN)
        assertEquals(0, board1.compareTo(board2))
    }

    @Test
    fun `compareTo should not return zero for two different boards`() {
        val board1 = Board(listOf(0, 2, 4, 1, 3), Piece.QUEEN)
        val board2 = Board(listOf(0, 1, 2, 3, 4), Piece.QUEEN)
        assertNotEquals(0, board1.compareTo(board2))
    }

}