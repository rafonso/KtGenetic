package rafael.ktgenetic.sudoku

import rafael.ktgenetic.*

class SudokuEnvironment(override val mutationFactor: Double = 0.01,
                        override val maxGenerations: Int = Int.MAX_VALUE,
                        override val generationSize: Int = 10
) : Environment<Cell, Puzzle> {

    override fun getFirstGeneration(): List<Puzzle> = (0 until generationSize)
            .map { cells.shuffle() }
            .map { Puzzle(it) }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(Puzzle.maxPos)

    override fun cutIntoPieces(sequence: List<Cell>, cutPositions: Pair<Int, Int>):
            Triple<List<Cell>, List<Cell>, List<Cell>> =
            makeCuttingIntoPieces(sequence, cutPositions)

    override fun executeMutation(sequence: List<Cell>): List<Cell> = sequence.randomSwap()

    override fun getNewGenetotype(sequence: List<Cell>): Puzzle = Puzzle(sequence)

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