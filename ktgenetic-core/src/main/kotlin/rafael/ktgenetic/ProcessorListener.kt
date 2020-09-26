package rafael.ktgenetic

/**
 * Listens a [ProcessorEvent] fired from [rafael.ktgenetic.selection.GeneticProcessor].
 *
 * Implementation based on https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 */
interface ProcessorListener {

    /**
     * Process an eventType
     *
     * @param event Event fired from [GeneticProcessor].
     */
    fun onEvent(event: ProcessorEvent<*>)

}

object NoListener: ProcessorListener {
    override fun onEvent(event: ProcessorEvent<*>) {
        // Do nothing
    }
}
