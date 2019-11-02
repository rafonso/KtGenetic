package rafael.ktgenetic.nqueens

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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

}