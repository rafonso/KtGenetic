package rafael.ktgenetic

/**
 * Represents a event ocurred during the evolutionary process in [GeneticProcessor].
 *
 * Implementation based on https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 *
 * @param event Event type.
 * @param value The companion value to event type. See [ProcessorEventEnum] for more details.
 */
data class ProcessorEvent(val event: ProcessorEventEnum, val value: Any = "")