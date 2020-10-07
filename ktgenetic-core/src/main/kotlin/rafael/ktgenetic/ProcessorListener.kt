package rafael.ktgenetic

/**
 * Listens a [ProcessorEvent] fired from [rafael.ktgenetic.processor.GeneticProcessor].
 *
 * Implementation based on https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 */
interface ProcessorListener {

    /**
     * Process an eventType
     *
     * @param event Event fired from [rafael.ktgenetic.processor.GeneticProcessor].
     */
    fun onEvent(event: ProcessorEvent<*>)

}

/**
 * A Noop [ProcessorListener]
 */
object NoListener: ProcessorListener {
    override fun onEvent(event: ProcessorEvent<*>) {
        // Do nothing
    }
}
