package rafael.ktgenetic

/**
 * Listens a [ProcessorEvent] fired from [GeneticProcessor].
 *
 * Implementation based on https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 */
interface ProcessorListener {

    /**
     * Process an event
     *
     * @param event Event fired from [GeneticProcessor].
     */
    fun onEvent(event: ProcessorEvent)
}
