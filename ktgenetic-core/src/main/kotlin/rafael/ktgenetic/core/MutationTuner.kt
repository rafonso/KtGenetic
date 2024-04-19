package rafael.ktgenetic.core

import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.ProcessorListener
import rafael.ktgenetic.core.events.TypeProcessorEvent
import rafael.ktgenetic.core.utils.mainLogger
import java.util.function.Supplier

// Minimum variation for mutation factor adjustment
private const val minimunVariation = 0.01

// Maximum variation for mutation factor adjustment
private const val maximumVariation = 0.05

/**
 * Class responsible for tuning the mutation factor in a genetic algorithm.
 *
 * The mutation factor is adjusted based on the proportion of the average fitness deviation to the average fitness.
 * If the proportion is less than a minimum variation and the mutation factor is not already at its maximum (0.99),
 * the mutation factor is increased. If the proportion is greater than a maximum variation and the mutation factor
 * is not already at its minimum (0.01), the mutation factor is decreased.
 *
 * @param C The type of the chromosomes in the environment.
 * @property environment The environment in which the genetic algorithm is running.
 */
class MutationTuner<C : Chromosome<*>>(val environment: Environment<*, C>) : ProcessorListener {

    /**
     * Adjusts the mutation factor based on the given proportion.
     *
     * @param proportion The proportion of the average fitness deviation to the average fitness.
     */
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

    /**
     * Handles a ProcessorEvent.
     *
     * If the event type is GENERATION_EVALUATED, the mutation factor is adjusted based on the proportion of the
     * average fitness deviation to the average fitness.
     *
     * @param event The ProcessorEvent to handle.
     */
    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.GENERATION_EVALUATED) {
            val proportion = event.statistics.averageFitnessDeviation / event.statistics.averageFitness
            adjustMutationFactor(proportion)
        }
    }

}
