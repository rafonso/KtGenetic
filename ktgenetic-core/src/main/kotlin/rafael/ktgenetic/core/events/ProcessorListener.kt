package rafael.ktgenetic.core.events

/**
 * Listens a [ProcessorEvent] fired from [rafael.ktgenetic.core.processor.GeneticProcessor].
 *
 *
 * This interface is based on the Observer/Listener design pattern.
 * More information about this design pattern can be found at: https://github.com/dbacinski/Design-Patterns-In-Kotlin#observer--listener
 *
 * Implementations of this interface should override the onEvent function to handle the ProcessorEvent.
 */
interface ProcessorListener {

    /**
     * Handles a ProcessorEvent.
     *
     * This function is called when a ProcessorEvent is fired from the GeneticProcessor.
     * Implementations of this function should contain the logic for handling the event.
     *
     * @param event The ProcessorEvent fired from the GeneticProcessor.
     */
    fun onEvent(event: ProcessorEvent<*>)

}

/**
 * A No-Operation (Noop) implementation of the ProcessorListener interface.
 *
 * This object is an implementation of the ProcessorListener interface that does nothing when a ProcessorEvent is received.
 * It can be used when a ProcessorListener is required, but no action needs to be taken on a ProcessorEvent.
 */
object NoListener: ProcessorListener {
    /**
     * Handles a ProcessorEvent by doing nothing.
     *
     * This function overrides the onEvent function in the ProcessorListener interface.
     * When called, it does nothing.
     *
     * @param event The ProcessorEvent fired from the GeneticProcessor.
     */
    override fun onEvent(event: ProcessorEvent<*>) {
        // Do nothing
    }
}