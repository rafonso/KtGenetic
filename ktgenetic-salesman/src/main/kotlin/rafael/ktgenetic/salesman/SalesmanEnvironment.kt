package rafael.ktgenetic.salesman

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.randomSwap
import kotlin.math.sqrt

class SalesmanEnvironment(
    private val points: List<Point>,
    override val maxGenerations: Int,
    override val generationSize: Int,
    override var mutationFactor: Double
) : Environment<Point, Path> {

    private val maxDistance: Double by lazy {
        val minX = points.minBy { it.x }!!.x
        val minY = points.minBy { it.y }!!.y
        val maxX = points.maxBy { it.x }!!.x
        val maxY = points.maxBy { it.y }!!.y

        val deltaX = (maxX - minX).toDouble()
        val deltaY = (maxY - minY).toDouble()

        sqrt(deltaX * deltaX + deltaY * deltaY) * (points.size - 1)
    }

    init {
        DistanceRepository.clear()
    }

    override fun getFirstGeneration(): List<Path> {

        tailrec fun generatePath(paths: Set<Path>): Set<Path> =
                when (generationSize) {
                    paths.size -> paths
                    else       -> generatePath(paths + Path(points.shuffled()))
                }

        return generatePath(emptySet()).toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(points.size)

    override fun executeMutation(sequence: List<Point>): List<Point> = sequence.randomSwap()

    override fun createNewChromosome(sequence: List<Point>): Path = Path(sequence)

    override fun calculateFitness(chromosome: Path): Double = (maxDistance - chromosome.width) / maxDistance

}