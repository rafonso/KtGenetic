package rafael.ktgenetic.sudoku

import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.core.utils.createCutPositions
import rafael.ktgenetic.core.utils.replace
import kotlin.random.Random

class PuzzleEnvironment(
    val gridSize: Int,
    val strategy: MutationStrategy,
    override val maxGenerations: Int = Int.MAX_VALUE,
    override val generationSize: Int = 10,
    override var mutationFactor: Double = 0.01
) : Environment<Row, Grid> {

    override fun getFirstGeneration(): List<Grid> {
        val dictionary = (1..gridSize).toList()

        tailrec fun createGrid(rows: List<Row> = emptyList()): Grid =
            when (gridSize) {
                rows.size -> Grid(rows)
                else -> createGrid(rows + listOf(dictionary.shuffled(Random)))
            }

        tailrec fun createGrids(grids: Set<Grid> = setOf()): Set<Grid> =
            when (generationSize) {
                grids.size -> grids
                else -> createGrids(grids + createGrid())
            }

        return createGrids().toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(gridSize)

    override fun executeMutation(sequence: List<Row>): List<Row> {
        val index = Random.nextInt(gridSize)
        val originalRow = sequence[index]
        val mutatedRow = strategy.getMutation(originalRow)

        return sequence.replace(index, mutatedRow)
    }

    override fun createNewChromosome(sequence: List<Row>): Grid = Grid(sequence)

    override fun calculateFitness(chromosome: Grid): Double {
        // TODO: BasicConflictCounter or its superinterface must be a class attribute.
        chromosome.conflicts = BasicConflictCounter().calculateConflicts(chromosome)

        val maxConflicts =  getMaxConflictsBySize(chromosome.size)

        return (maxConflicts - chromosome.conflicts.size).toDouble() / maxConflicts
    }

    override fun resultFound(genotypes: List<Grid>): Boolean = (genotypes[0].numOfCollisions == 0)

}
