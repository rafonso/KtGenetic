package rafael.ktgenetic.nqueens

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.geneticRandom
import rafael.ktgenetic.randomSwap

class BoardEnvironment(val boardSize: Int,
                       override val maxGenerations: Int = Int.MAX_VALUE,
                       override val generationSize: Int = 10,
                       override var mutationFactor: Double = 0.01
) : Environment<Int, Board> {

    override fun getFirstGeneration(): List<Board> {
        val sequence = IntRange(0, boardSize - 1).toList()
        val firstGeneration = mutableSetOf(Board(sequence))

        while (firstGeneration.size < generationSize) {
            val temp = sequence.toMutableList()
            temp.shuffle(geneticRandom)
            firstGeneration.add(Board(temp.toList()))
        }

        return firstGeneration.toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(boardSize)

    override fun executeMutation(sequence: List<Int>): List<Int> = sequence.randomSwap()

    override fun createNewChromosome(sequence: List<Int>): Board = Board(sequence)

    override fun calculateFitness(chromosome: Board): Double {

        tailrec fun verifyCollision(priorRow: Int, priorDiagonal: Int, deltaColumn: Int): Int {
            val currentRow = priorRow + 1
            val currentDiagonal = priorDiagonal + deltaColumn

            return when {
                currentRow >= boardSize || currentDiagonal < 0 || currentDiagonal >= boardSize -> 0
                chromosome.content[currentRow] == currentDiagonal                              -> 1
                else                                                                           -> verifyCollision(currentRow, currentDiagonal, deltaColumn)
            }
        }

        chromosome.collisions = IntRange(0, boardSize - 1)
                .map { row ->
                    verifyCollision(row, chromosome.content[row], 1) + verifyCollision(row, chromosome.content[row], -1)
                }
                .sum()

        return 1.0 / (1 + chromosome.collisions)
    }

    override fun resultFound(genotypes: List<Board>): Boolean = (genotypes[0].collisions == 0)

}