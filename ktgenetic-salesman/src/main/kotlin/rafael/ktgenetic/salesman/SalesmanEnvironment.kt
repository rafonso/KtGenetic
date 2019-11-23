package rafael.ktgenetic.salesman

import rafael.ktgenetic.Environment
import kotlin.math.sqrt

//const val PENALITY_FACTOR = 0.95

class SalesmanEnvironment(
    private val points: List<Point>,
    private val pathType: PathType,
    startPoint: Point?,
    endPoint: Point?,
    override val maxGenerations: Int,
    override val generationSize: Int,
    override var mutationFactor: Double
) : Environment<Point, Path> {

    private val maxDistance: Double by lazy {
        // TODO: Change calculo, deve ser um ponto com (minX, minY) e outro com (maX, maxY)
        val minX = points.minBy { it.x }!!.x
        val minY = points.minBy { it.y }!!.y
        val maxX = points.maxBy { it.x }!!.x
        val maxY = points.maxBy { it.y }!!.y

        val deltaX = (maxX - minX).toDouble()
        val deltaY = (maxY - minY).toDouble()

        sqrt(deltaX * deltaX + deltaY * deltaY) * (points.size - pathType.delta)
    }

    private val pathHandler = pathType.createNewPathHandler(points, startPoint, endPoint)

    init {
        DistanceRepository.clear()
    }

    override fun getFirstGeneration(): List<Path> {

        tailrec fun generatePath(paths: Set<Path>): Set<Path> =
                when (generationSize) {
                    paths.size -> paths
                    else       -> generatePath(paths + pathHandler.createNewPath())
                }

        return generatePath(emptySet()).toList()
    }

    override fun getCutPositions(): Pair<Int, Int> = pathHandler.getCutPositions()

    override fun executeMutation(sequence: List<Point>): List<Point> = pathHandler.executeMutation(sequence)

    override fun createNewChromosome(sequence: List<Point>): Path = Path(sequence, pathType)

    override fun calculateFitness(chromosome: Path): Double = (maxDistance - chromosome.width) / maxDistance

}