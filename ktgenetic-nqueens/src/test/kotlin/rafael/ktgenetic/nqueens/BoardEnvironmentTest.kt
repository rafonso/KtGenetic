package rafael.ktgenetic.nqueens

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BoardEnvironmentTest {

    /**
     * 0 Q....
     * 1 ....Q
     * 2 ..Q..
     * 3 ...Q.
     * 4 .Q...
     */
    @Test
    fun calculateFitnessCollisionDiagonalLesserToGreater() {
        // 2
        val board = Board(listOf(2, 0, 4, 1, 3), piece = Piece.QUEEN)
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val fitness = environment.calculateFitness(board)

        assertEquals(2.0 / 4.0, fitness)
    }

    /**
     * 0 -|-|-|-|Q
     * 1 -|-|-|Q|-
     * 2 -|Q|-|-|-
     * 3 Q|-|-|-|-
     * 4 -|-|Q|-|-
     */
    @Test
    fun calculateFitnessCollisionDiagonalGreaterToLesser() {
        // 2
        val board = Board(listOf(4,3,1,0,2), piece = Piece.QUEEN)
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val fitness = environment.calculateFitness(board)

        assertEquals(2.0 / 4.0, fitness)
    }

    /**
     * 0 ..Q..
     * 1 ...Q.
     * 2 .Q...
     * 3 ....Q
     * 4 Q....
     */
    @Test
    fun calculateFitnessCollisionBothDiagonals() {
        // 2
        val board = Board(listOf(2, 3, 1, 4, 0), piece = Piece.QUEEN)
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val fitness = environment.calculateFitness(board)

        assertEquals(2.0 / 4.0, fitness)
    }

    /**
     * 0 ...Q.
     * 1 .Q...
     * 2 ....Q
     * 3 ..Q..
     * 4 Q....
     */
    @Test
    fun calculateFitnessNoCollisions() {
        val board = Board(listOf(3, 1, 4, 2, 0), piece = Piece.QUEEN)
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val fitness = environment.calculateFitness(board)

        assertEquals(1.0, fitness)
    }

}