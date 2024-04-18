package rafael.ktgenetic.console

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.TypeProcessorEvent
import rafael.ktgenetic.core.events.ProcessorListener
import rafael.ktgenetic.core.processor.GeneticProcessor
import java.util.*

/**
 * ConsoleProcessorListener is a class that listens to the events of a GeneticProcessor and provides console interaction.
 *
 * @property processor The GeneticProcessor that this listener is attached to.
 * @constructor Creates a new ConsoleProcessorListener with the given GeneticProcessor.
 */
class ConsoleProcessorListener<C: Chromosome<*>>(val processor: GeneticProcessor<*, C>) : ProcessorListener {

    private val scanner = Scanner(System.`in`)

    private var generationsToWait: Int = 0

    private var currentGeneration: Int = 0

    /**
     * Handles the events from the GeneticProcessor.
     *
     * @param event The event from the GeneticProcessor.
     */
    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.GENERATION_EVALUATED) {
            currentGeneration++
            if (currentGeneration >= generationsToWait) {
                println("Type <Enter> to process the next generation. " +
                        "Type n to process 'n' generations. " +
                        "Type 0 to process until the end. " +
                        "Type x to interrupt the program")
                val input = scanner.nextLine()
                if(input.matches(Regex("\\d+"))) {
                    val x = Integer.parseInt(input)
                    if(x == 0) {
                        generationsToWait = Int.MAX_VALUE
                    } else {
                        generationsToWait = x
                        currentGeneration = 0
                    }
                } else if(input == "x") {
                    processor.stop()
                }
            }
        }
    }

}
