package rafael.ktgenetic.core.utils

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.ProcessorListener
import rafael.ktgenetic.core.events.TypeProcessorEvent
import java.util.function.Supplier

/**
 * Emits log messages according the [ProcessorEvent].
 *
 * This class implements the ProcessorListener interface and overrides the onEvent function to log ProcessorEvents.
 * The log messages are emitted using the Apache Log4j logging library.
 */
class LogProcessorListener : ProcessorListener {

    private val size = 120

    private val log: Logger = LogManager.getLogger("LOG")

    /**
     * Converts a population of chromosomes to a console-friendly string.
     *
     * @param population The population of chromosomes.
     * @return A string representation of the population.
     */
    private fun populationToConsole(population: List<Chromosome<*>>): String {

        val genotypesByLine =
            if (population[0].valueToString().length < size) size / (population[0].valueToString().length + 1)
            else 1

        return population.mapIndexed { index, genotype ->
            if (index % genotypesByLine == 0) ("\n${genotype.valueToString()}") else (genotype.valueToString())
        }.joinToString(separator = " ")
    }

    /**
     * Logs the statistics of a generation when it has been evaluated.
     *
     * @param event The ProcessorEvent containing the generation data.
     */
    private fun debugGenerationEvaluated(event: ProcessorEvent<*>) {
        if (!log.isDebugEnabled) {
            return
        }

        log.debug(
            "Gen %3d - AF %.3f (%.3f). BF: %.3f. Best Opt: %s".format(
                event.generation,
                event.statistics.averageFitness,
                event.statistics.averageFitnessDeviation,
                event.statistics.bestFitness,
                event.population.first()
            )
        )
    }

    /**
     * Logs a ProcessorEvent with a custom event description.
     *
     * @param event The ProcessorEvent to log.
     * @param eventDescription The custom description for the event.
     */
    private fun traceEvent(event: ProcessorEvent<*>, eventDescription: String) =
        log.trace(Supplier { "Generation ${event.generation} - $eventDescription : ${populationToConsole(event.population)}" })

    /**
     * Logs a ProcessorEvent related to chromosome crossing with a custom event description.
     *
     * @param event The ProcessorEvent to log.
     * @param eventDescription The custom description for the event.
     */
    private fun tracerCross(event: ProcessorEvent<*>, eventDescription: String) =
        log.log(
            TRACER,
            Supplier { "Generation ${event.generation} - $eventDescription: ${event.population[0].valueToString()} x ${event.population[1].valueToString()}" }
        )

    /**
     * Handles a ProcessorEvent by logging it.
     *
     * This function is called when a ProcessorEvent is fired from the GeneticProcessor.
     * It logs the event using the Apache Log4j logging library.
     *
     * @param event The ProcessorEvent fired from the GeneticProcessor.
     */
    override fun onEvent(event: ProcessorEvent<*>) {
        when (event.eventType) {
            TypeProcessorEvent.STARTING ->
                log.trace(Supplier { "Starting. Max generations: ${event.generation}" })
            TypeProcessorEvent.FIRST_GENERATION_CREATING ->
                if (log.isDebugEnabled) log.info("Creating First Generation")
            TypeProcessorEvent.GENERATION_EVALUATING ->
                log.debug(Supplier { "********** Generation ${event.generation} ***********" })
            TypeProcessorEvent.REPRODUCING ->
                log.trace(Supplier { "Generation ${event.generation} - Reproducing" })
            TypeProcessorEvent.CROSSING ->
                tracerCross(event, "Crossing")
            TypeProcessorEvent.CROSSED ->
                tracerCross(event, "Crossed ")
            TypeProcessorEvent.MUTATION_EXECUTING ->
                traceEvent(event, "Mutating")
            TypeProcessorEvent.FITNESS_CALCULATING ->
                traceEvent(event, "Calculating Fitness")
            TypeProcessorEvent.SELECTING ->
                traceEvent(event, "Selecting")
            TypeProcessorEvent.GENERATION_EVALUATED ->
                debugGenerationEvaluated(event)
            TypeProcessorEvent.ENDED_BY_GENERATIONS ->
                log.debug(Supplier { "Max Generations reached" })
            TypeProcessorEvent.ENDED_BY_FITNESS ->
                log.debug(Supplier { "Value found: ${event.population}" })
            TypeProcessorEvent.ENDED_BY_INTERRUPTION ->
                log.debug(Supplier { "Interrupted: ${event.population}" })
            TypeProcessorEvent.ERROR ->
                log.error(
                    "Generation ${event.generation} - ${event.error!!.message} : ${populationToConsole(event.population)}"
                    , event.error
                )
            TypeProcessorEvent.WAITING -> {
                // Do nothing
            }
        }
    }

}
