package rafael.ktgenetic.core

import rafael.ktgenetic.core.events.ProcessorEvent
import kotlin.math.pow
import kotlin.math.sqrt

data class GenerationStatistics internal constructor(
    val bestFitness: Double,
    val averageFitness: Double,
    val averageFitnessDeviation: Double
)

internal fun <C : Chromosome<*>> getStatistics(event: ProcessorEvent<C>): GenerationStatistics {
    val finesses = event.population.map { it.fitness }

    val bestFitness = finesses.maxOrNull()!!
    val averageFitness = finesses.sum() / finesses.size
    val averageFitnessDeviation = sqrt(
        finesses.map { (it - averageFitness).pow(2) }.sum() /
                (finesses.size * (finesses.size - 1))
    )

    return GenerationStatistics(bestFitness, averageFitness, averageFitnessDeviation)
}
