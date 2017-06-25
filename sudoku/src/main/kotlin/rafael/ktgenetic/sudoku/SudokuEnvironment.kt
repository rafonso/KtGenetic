package rafael.ktgenetic.sudoku

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.randomSwap
import rafael.ktgenetic.shuffle

class SudokuEnvironment(override val maxGenerations: Int = Int.MAX_VALUE,
                        override val generationSize: Int = 10,
                        override var mutationFactor: Double = 0.01
) : Environment<Cell, Puzzle> {

    override fun getFirstGeneration(): List<Puzzle> =
            (0 until generationSize).map { cells.shuffle() }.map { Puzzle(it) }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(Puzzle.maxPos)

    override fun executeMutation(sequence: List<Cell>): List<Cell> = sequence.randomSwap()

    override fun createNewChromosome(sequence: List<Cell>): Puzzle = Puzzle(sequence)

    override fun calculateFitness(sequence: List<Cell>): Double {

        fun getCorrectSets(set: List<List<Int>>): Int = set.map {
            positions ->
            if (positions.map { sequence[it].value }.toSet().size == 9) 1 else 0
        }.sum()

        val correctRows = getCorrectSets(Puzzle.rows)
        val correctColumns = getCorrectSets(Puzzle.cols)
        val correctSectors = getCorrectSets(Puzzle.sectors)

        return (correctColumns + correctRows + correctSectors).toDouble() / 27
    }

    override fun resultFound(genotypes: List<Puzzle>): Boolean =
            (genotypes.firstOrNull { it.fitness == 1.0 } != null)


}