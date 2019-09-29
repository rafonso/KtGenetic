package rafael.ktgenetic

/**
 * Represents a eventType ocurred during the evolutionary process in [GeneticProcessor].
 *
 * Implementation based on https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 *
 * @param eventType Event type.
 * @param value The companion value to eventType type. See [TypeProcessorEvent] for more details.
 */
data class ProcessorEvent<out C: Chromosome<*>>(val eventType: TypeProcessorEvent,
                                                val generation: Int,
                                                val population: List<C>)