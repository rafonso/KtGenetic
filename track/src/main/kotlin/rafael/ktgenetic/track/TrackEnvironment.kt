package rafael.ktgenetic.track

import rafael.ktgenetic.Environment
import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.geneticRandom

private const val DISTANCE_FACTOR = 1.0
private const val LENGHT_FACTOR = 1.0

class TrackEnvironment(val width: Int,
                       val height: Int,
                       val trackSize: Int,
                       override val maxGenerations: Int,
                       override val generationSize: Int,
                       override var mutationFactor: Double
) : Environment<Direction, Path> {

    private val target = Point(width, height)

    private val diagonalLenght = target.distance(Point())

    private fun chooseDirection() = Direction.values()[geneticRandom.nextInt(Direction.values().size)]

    override fun getFirstGeneration(): List<Path> = (1..generationSize)
            .map { _ ->
                Path((1..trackSize).map { _ -> chooseDirection() })
            }

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(trackSize)

    override fun executeMutation(sequence: List<Direction>): List<Direction> {
        val mutationPoint = geneticRandom.nextInt(sequence.size)
        val result = sequence.toMutableList()
        result[mutationPoint] = chooseDirection()

        return result.toList()
    }

    override fun createNewChromosome(sequence: List<Direction>): Path = Path(sequence)

    override fun calculateFitness(chromosome: Path): Double {
//        val distanceFactor =

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}