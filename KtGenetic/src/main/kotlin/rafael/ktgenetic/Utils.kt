package rafael.ktgenetic

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import java.util.*

private fun createRandomPositions(maxPos: Int, initialPos: Int): Pair<Int, Int> {
    val pos1 = initialPos + geneticRandom.nextInt(maxPos - 2)
    val pos2 = if (pos1 == (maxPos - 2))
        (maxPos - 1)
    else
        (pos1 + 1 + geneticRandom.nextInt(maxPos - 1 - pos1))

    return Pair(pos1, pos2)
}

val geneticRandom = Random()

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
    val fitnesses = chromosomes.map { it.fitness }

    val bestFitness = fitnesses.max()
    val averageFitness = fitnesses.sum() / fitnesses.size
    val averageFitnessDeviation = Math.sqrt(
            fitnesses.map { Math.pow(it - averageFitness, 2.0) }.sum() /
                    (fitnesses.size * (fitnesses.size - 1))
    )

    return Triple(bestFitness!!, averageFitness, averageFitnessDeviation)
}

