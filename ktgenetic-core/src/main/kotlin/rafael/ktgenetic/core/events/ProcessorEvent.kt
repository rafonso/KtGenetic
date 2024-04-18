package rafael.ktgenetic.core.events

import rafael.ktgenetic.core.Chromosome
import java.time.LocalDateTime

/**
 * Alias for ProcessorEvent with a wildcard type parameter.
 */
typealias GenerationEvent = ProcessorEvent<*>

/**
 * Represents an event that occurred during the evolutionary process in the [rafael.ktgenetic.core.processor.GeneticProcessor].
 *
 *
 * This class is based on the Observer/Listener design pattern.
 * More information about this design pattern can be found at: https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 *
 * @property eventType The type of the event.
 * @property generation The generation number.
 * @property population The population of chromosomes for this generation.
 * @property error An optional Exception that may have been thrown during the event (may be null).
 */
data class ProcessorEvent<out C : Chromosome<*>>(
    val eventType: TypeProcessorEvent,
    val generation: Int,
    val population: List<C>,
    val error: Throwable? = null
) {
    /**
     * The date and time when the event was created.
     */
    val dateTime: LocalDateTime = LocalDateTime.now()

    /**
     * The statistics for the generation associated with this event.
     * The statistics are calculated lazily, i.e., they are calculated and cached the first time this property is accessed.
     */
    val statistics: GenerationStatistics by lazy { getStatistics(this) }

}

/**
 * Creates a new instance of a Waiting Event.
 *
 * @return A [ProcessorEvent]  of type [TypeProcessorEvent.WAITING], generation [Int.MIN_VALUE] and empty [ProcessorEvent.population]
 */
internal fun waitingEvent() = ProcessorEvent(TypeProcessorEvent.WAITING, Int.MIN_VALUE, emptyList())
