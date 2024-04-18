package rafael.ktgenetic.core

import rafael.ktgenetic.core.events.ProcessorEvent
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Represents the statistics of a generation in a genetic algorithm.
 *
 * @property bestFitness The best fitness value in the generation.
 * @property averageFitness The average fitness value in the generation.
 * @property averageFitnessDeviation The standard deviation of the fitness values in the generation.
 */
data class GenerationStatistics internal constructor(
    val bestFitness: Double,
    val averageFitness: Double,
    val averageFitnessDeviation: Double
)

/**
 * Calculates the statistics of a generation based on a ProcessorEvent.
 *
 * The statistics include the best fitness value, the average fitness value, and the standard deviation of the fitness values.
 *
 * @param event The ProcessorEvent containing the generation data.
 * @return The statistics of the generation.
 */
internal fun <C : Chromosome<*>> getStatistics(event: ProcessorEvent<C>): GenerationStatistics {
    val finesses = event.population.map { it.fitness }

    val bestFitness = finesses.maxOrNull()!!
    val averageFitness = finesses.sum() / finesses.size
    val averageFitnessDeviation = sqrt(
        finesses.sumOf { (it - averageFitness).pow(2) } /
                (finesses.size * (finesses.size - 1))
    )

    return GenerationStatistics(bestFitness, averageFitness, averageFitnessDeviation)
}
