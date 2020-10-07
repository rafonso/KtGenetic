package rafael.ktgenetic

import kotlin.math.pow
import kotlin.math.sqrt

data class GenerationStatistics<out C : Chromosome<*>>(
    val bestFitness: Double,
    val averageFitness: Double,
    val averageFitnessDeviation: Double,
    val median: C
)

internal fun <C : Chromosome<*>> getStatistics(event: ProcessorEvent<C>): GenerationStatistics<C> {
    val finesses = event.population.map { it.fitness }

    val bestFitness = finesses.maxOrNull()!!
    val averageFitness = finesses.sum() / finesses.size
    val averageFitnessDeviation = sqrt(
        finesses.map { (it - averageFitness).pow(2) }.sum() /
                (finesses.size * (finesses.size - 1))
    )
    val median = event.population[event.population.size / 2]

    return GenerationStatistics(bestFitness, averageFitness, averageFitnessDeviation, median)
}
