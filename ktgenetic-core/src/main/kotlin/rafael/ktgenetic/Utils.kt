package rafael.ktgenetic

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.*

fun createRandomPositions(maxPos: Int, initialPos: Int = 0): Pair<Int, Int> {
    val pos1 = initialPos + geneticRandom.nextInt(maxPos - 2)
    val pos2 = if (pos1 == (maxPos - 2))
        (maxPos - 1)
    else
        (pos1 + 1 + geneticRandom.nextInt(maxPos - 1 - pos1))

    return Pair(pos1, pos2)
}

val geneticRandom = Random.asJavaRandom()

/**
 * Generate a random [Int] from 0 to a max (exclusive)
 *
 * @param maxExclusive the max value
 * @return random [Int] from 0 until `maxExclusive` - 1
 */
fun randomIntExclusive(maxExclusive: Int) = geneticRandom.nextInt(maxExclusive)

/**
 * Generate a random [Int] from 0 to a max (inclusive)
 *
 * @param maxValue the max value
 * @return random [Int] from 0 until `maxValue`
 */
fun randomIntInclusive(maxValue: Int) = randomIntExclusive(maxValue + 1)

fun createCutPositions(maxPos: Int): Pair<Int, Int> = createRandomPositions(maxPos, 1)

fun <G> makeCuttingIntoPieces(sequence: List<G>, cutPositions: Pair<Int, Int>):
        ListPieces<G> =
    ListPieces(
        sequence.subList(0, cutPositions.first),
        sequence.subList(cutPositions.first, cutPositions.second),
        sequence.subList(cutPositions.second, sequence.size)
    )

/**
 * From a list of [Chromosome]s, it returns the best [Chromosome.fitness], the average fitness and the deviation of this average.
 *
 * @param chromosomes Chromosomes to be evaluated
 * @return A [Triple] compound by the best [Chromosome.fitness], the average fitness and the deviation of this average.
 */
fun getBestAverageDeviationFitness(chromosomes: List<Chromosome<*>>): Triple<Double, Double, Double> {
    val finesses = chromosomes.map { it.fitness }

    val bestFitness = finesses.maxOrNull()!!
    val averageFitness = finesses.sum() / finesses.size
    val averageFitnessDeviation = sqrt(
        finesses.map { (it - averageFitness).pow(2.0) }.sum() /
                (finesses.size * (finesses.size - 1))
    )

    return Triple(bestFitness, averageFitness, averageFitnessDeviation)
}

