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
        val board = Board(listOf(4, 3, 1, 0, 2), piece = Piece.QUEEN)
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


    @Test
    fun `getFirstGeneration should return correct number of boards`() {
        val environment = BoardEnvironment(5, Piece.QUEEN, generationSize = 10)
        val firstGeneration = environment.getFirstGeneration()
        assertEquals(10, firstGeneration.size)
    }

    @Test
    fun `getCutPositions should return valid cut positions`() {
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val (cutPosition1, cutPosition2) = environment.getCutPositions()
        assert(cutPosition1 in 1 until 5)
        assert(cutPosition2 in 1 until 5)
        assert(cutPosition1 < cutPosition2)
    }

    @Test
    fun `executeMutation should change the sequence`() {
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val originalSequence = listOf(1, 2, 3, 4, 5)
        val mutatedSequence = environment.executeMutation(originalSequence)
        assert(originalSequence != mutatedSequence)
    }

    @Test
    fun `createNewChromosome should return a valid board`() {
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val sequence = listOf(0, 1, 2, 3, 4)
        val board = environment.createNewChromosome(sequence)
        assertEquals(sequence, board.content)
        assertEquals(Piece.QUEEN, board.piece)
    }

    @Test
    fun `calculateFitness should return correct fitness for a board with no collisions`() {
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val board = Board(listOf(0, 2, 4, 1, 3), Piece.QUEEN)
        val fitness = environment.calculateFitness(board)
        assertEquals(1.0, fitness)
    }

    @Test
    fun `calculateFitness should return correct fitness for a board with collisions`() {
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val board = Board(listOf(0, 1, 2, 3, 4), Piece.QUEEN)
        val fitness = environment.calculateFitness(board)
        assert(fitness < 1.0)
    }

    @Test
    fun `resultFound should return false for a board with collisions`() {
        val environment = BoardEnvironment(5, Piece.QUEEN)
        val board = Board(listOf(0, 1, 2, 3, 4), Piece.QUEEN)
        val result = environment.resultFound(listOf(board))
        assertEquals(false, result)
    }

}