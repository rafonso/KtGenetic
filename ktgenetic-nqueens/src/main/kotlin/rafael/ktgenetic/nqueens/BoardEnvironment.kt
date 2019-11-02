package rafael.ktgenetic.nqueens

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.randomSwap

class BoardEnvironment(val boardSize: Int,
                       private val piece: Piece,
                       override val maxGenerations: Int = Int.MAX_VALUE,
                       override val generationSize: Int = 10,
                       override var mutationFactor: Double = 0.01
) : Environment<Int, Board> {

    override fun getFirstGeneration(): List<Board> {

        tailrec fun generateBoards(boards: Set<Board>): Set<Board> =
                when (generationSize) {
                    boards.size -> boards
                    else        -> generateBoards(boards + Board(piece.createContent(boardSize), piece))
                }

        return generateBoards(setOf()).toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(boardSize)

    override fun executeMutation(sequence: List<Int>): List<Int> = sequence.randomSwap()

    override fun createNewChromosome(sequence: List<Int>): Board = Board(sequence, piece)

    override fun calculateFitness(chromosome: Board): Double {
        chromosome.collisions = piece.calculateCollisions(chromosome)

        return (boardSize - 1 - chromosome.collisions.size).toDouble() / (boardSize - 1)
    }

    override fun resultFound(genotypes: List<Board>): Boolean = (genotypes[0].numOfCollisions == 0)

}