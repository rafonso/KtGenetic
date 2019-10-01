package rafael.ktgenetic

import kotlin.math.pow
import kotlin.math.sqrt

class MutationTuner<C: Chromosome<*>>(val environment: Environment<*, C>): ProcessorListener {

    private val minimunVariation = 0.01

    private val maximumVariation = 0.05

    private fun calculateVariationProportion(chromosomes: List<Chromosome<*>>): Double {
        val averageFitness = chromosomes.pMap { it.fitness }.sum() / chromosomes.size
        val averageFitnessDeviation = sqrt(
                chromosomes.pMap { (it.fitness - averageFitness).pow(2.0) }.sum() /
                        (chromosomes.size * (chromosomes.size - 1))
        )

        return averageFitnessDeviation / averageFitness
    }

    private fun adjustMutationFactor(proportion: Double) {
        if (proportion < minimunVariation && (environment.mutationFactor <= 0.99)) {
            environment.mutationFactor += 0.01
        } else if ((proportion > maximumVariation) && (environment.mutationFactor >= 0.01)) {
            environment.mutationFactor = (environment.mutationFactor - 0.01).coerceAtLeast(0.01)
        }
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.GENERATION_EVALUATED) {
            val proportion = calculateVariationProportion(event.population)
            adjustMutationFactor(proportion)
        }
    }

}