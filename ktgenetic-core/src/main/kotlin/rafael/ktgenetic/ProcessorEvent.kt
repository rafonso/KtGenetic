package rafael.ktgenetic

import java.time.LocalDateTime

typealias GenerationEvent = ProcessorEvent<*>

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
data class ProcessorEvent<out C : Chromosome<*>>(
    val eventType: TypeProcessorEvent,
    val generation: Int,
    val population: List<C>,
    val error: Throwable? = null
) {
    val dateTime: LocalDateTime = LocalDateTime.now()

    val statistics: GenerationStatistics by lazy { getStatistics(this) }

}

/**
 * Creates a new instance of a Waiting Event.
 *
 * @return A [ProcessorEvent]  of type [TypeProcessorEvent.WAITING], generation [Int.MIN_VALUE] and empty [ProcessorEvent.population]
 */
internal fun waitingEvent() = ProcessorEvent(TypeProcessorEvent.WAITING, Int.MIN_VALUE, emptyList())
