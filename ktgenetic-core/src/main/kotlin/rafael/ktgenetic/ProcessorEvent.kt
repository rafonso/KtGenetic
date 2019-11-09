package rafael.ktgenetic

/**
 * Represents a eventType occurred during the evolutionary process in [rafael.ktgenetic.processor.GeneticProcessor].
 *
 * Implementation based on https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 *
 * @param eventType Event type.
 * @param generation the generation number
 * @param population This `generation` population
 * @param error Eventual [Exception] thrown (may be null)
 */
data class ProcessorEvent<out C: Chromosome<*>>(val eventType: TypeProcessorEvent,
                                                val generation: Int,
                                                val population: List<C>,
                                                val error: Throwable? = null)