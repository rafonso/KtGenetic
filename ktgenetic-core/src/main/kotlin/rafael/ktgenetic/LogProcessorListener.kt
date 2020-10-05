package rafael.ktgenetic

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.Supplier
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Emits log messages according the [ProcessorEvent].
 */
class LogProcessorListener : ProcessorListener {

    private val size = 120

    private val log: Logger = LogManager.getLogger("LOG")

    private fun populationToConsole(population: List<Chromosome<*>>): String {

        val genotypesByLine =
            if (population[0].valueToString().length < size) size / (population[0].valueToString().length + 1)
            else 1

        return population.mapIndexed { index, genotype ->
            if (index % genotypesByLine == 0) ("\n${genotype.valueToString()}") else (genotype.valueToString())
        }.joinToString(separator = " ")
    }

    private fun debugGenerationEvaluated(event: ProcessorEvent<*>) {
        if (!log.isDebugEnabled) {
            return
        }

        val selected = event.population
        val averageFitness = selected.pMap { it.fitness }.average()
        val averageFitnessDeviation = sqrt(
            selected.pMap { (it.fitness - averageFitness).pow(2.0) }.sum() /
                    (selected.size * (selected.size - 1))
        )
        val bestOption = selected.maxByOrNull { it.fitness }!!

        log.debug(
            "Gen %3d - AF %.3f (%.3f). BF: %.3f. Best Opt: %s".format(
                event.generation,
                averageFitness,
                averageFitnessDeviation,
                bestOption.fitness,
                bestOption
            )
        )
    }

    private fun traceEvent(event: ProcessorEvent<*>, eventDescription: String) =
        log.trace(Supplier { "Generation ${event.generation} - $eventDescription : ${populationToConsole(event.population)}" })

    private fun tracerCross(event: ProcessorEvent<*>, eventDescription: String) =
        log.log(
            TRACER,
            Supplier { "Generation ${event.generation} - $eventDescription: ${event.population[0].valueToString()} x ${event.population[1].valueToString()}" }
        )

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
        }
    }

}
