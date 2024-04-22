package rafael.ktgenetic.nqueens

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PieceTest {

    @Test
    fun `createContent should return list of unique integers for QUEEN`() {
        val content = Piece.QUEEN.createContent(5)
        assertEquals(5, content.size)
        assertEquals(5, content.toSet().size)
    }

    @Test
    fun `createContent should return list of integers for BISHOP`() {
        val content = Piece.BISHOP.createContent(5)
        assertEquals(5, content.size)
    }

    @Test
    fun `validateBoard should pass for valid QUEEN board`() {
        val board = Board(listOf(0, 2, 4, 1, 3), Piece.QUEEN)
        assertDoesNotThrow { Piece.QUEEN.validateBoard(board) }
    }

    @Test
    fun `validateBoard should throw for invalid QUEEN board`() {
        assertThrows<IllegalArgumentException> { Board(listOf(0, 0, 4, 1, 3), Piece.QUEEN) }
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 8])
    fun `validateBoard should throw for invalid BISHOP board`(pos: Int) {
        assertThrows<IllegalArgumentException> { Board(listOf(0, 0, pos, 1, 3), Piece.BISHOP) }
    }

    @Test
    fun `validateBoard should pass for valid BISHOP board`() {
        val board = Board(listOf(0, 0, 4, 1, 3), Piece.BISHOP)
        assertDoesNotThrow { Piece.BISHOP.validateBoard(board) }
    }

    @Test
    fun `piece symbol should be correct`() {
        assertEquals("\u2655", Piece.QUEEN.symbol)
        assertEquals("\u2657", Piece.BISHOP.symbol)
    }

    @Test
    fun `piece Code should be correct`() {
        assertEquals('Q', Piece.QUEEN.code)
        assertEquals('B', Piece.BISHOP.code)
    }

}