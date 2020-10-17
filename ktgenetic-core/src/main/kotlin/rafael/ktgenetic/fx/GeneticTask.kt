package rafael.ktgenetic.fx

import javafx.concurrent.Task
import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.utils.LogProcessorListener
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.processor.GeneticProcessor


internal class GeneticTask<C : Chromosome<*>>(private val processor: GeneticProcessor<*, C>) :
    Task<ProcessorEvent<C>>() {

    override fun call(): ProcessorEvent<C> {
        processor.addListener(LogProcessorListener())

        return processor.process()
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        processor.continueProcessing = false
        return super.cancel(mayInterruptIfRunning)
    }

}
