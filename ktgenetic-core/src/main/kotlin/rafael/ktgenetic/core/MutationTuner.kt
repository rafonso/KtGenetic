package rafael.ktgenetic.core

import org.apache.logging.log4j.util.Supplier
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.ProcessorListener
import rafael.ktgenetic.core.events.TypeProcessorEvent
import rafael.ktgenetic.core.utils.mainLogger
import rafael.ktgenetic.core.utils.pMap
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Mutation tuner
 *
 * @param C Chromossome Type
 * @property environment
 * @constructor Create empty Mutation tuner
 */
class MutationTuner<C : Chromosome<*>>(val environment: Environment<*, C>) : ProcessorListener {

    private val minimunVariation = 0.01

    private val maximumVariation = 0.05

    private fun calculateVariationProportion(chromosomes: List<Chromosome<*>>): Double {
        val averageFitness = chromosomes.pMap { it.fitness }.average()
        val averageFitnessDeviation = sqrt(
            chromosomes.pMap { (it.fitness - averageFitness).pow(2.0) }.sum() /
                    (chromosomes.size * (chromosomes.size - 1))
        )

        return averageFitnessDeviation / averageFitness
    }

    private fun adjustMutationFactor(proportion: Double) {
        val delta = if ((proportion < minimunVariation) && (environment.mutationFactor <= 0.99)) {
            1
        } else if ((proportion > maximumVariation) && (environment.mutationFactor >= 0.01)) {
            -1
        } else {
            0
        }

        if (delta != 0) {
            val oldValue = environment.mutationFactor
            environment.mutationFactor = (environment.mutationFactor + (delta * 0.01)).coerceAtLeast(0.01)
            mainLogger.debug(Supplier {
                "Mutation Factor changed from %.2f to %.2f".format(
                    oldValue,
                    environment.mutationFactor
                )
            })
        }
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.GENERATION_EVALUATED) {
            val proportion = calculateVariationProportion(event.population)
            adjustMutationFactor(proportion)
        }
    }

}
