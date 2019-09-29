package rafael.ktgenetic

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Emits log messages according the [ProcessorEvent].
 */
class LogProcessorListener : ProcessorListener {

    private val SIZE = 120

    private val log: Logger = LogManager.getLogger("LOG")

    private var maxGenerations: Int = 0

    private fun populationToConsole(population: List<Chromosome<*>>): String {

        val genotypesByLine =
                if (population[0].valueToString().length < SIZE) SIZE / (population[0].valueToString().length + 1)
                else 1

        return population.mapIndexed { index, genotype ->
            if (index % genotypesByLine == 0) ("\n${genotype.valueToString()}") else (genotype.valueToString())
        }.joinToString(separator = " ")
    }

    private fun log(correctLogLevel: Boolean, logAction: () -> Unit) {
        if (correctLogLevel) {
            logAction()
        }
    }

    private fun debugGenerationEvaluated(event: ProcessorEvent<*>): () -> Unit {
        return {
            val selected = event.population
            val averageFitness = selected.pMap { it.fitness }.sum() / selected.size
            val averageFitnessDeviation = sqrt(
                    selected.pMap { (it.fitness - averageFitness).pow(2.0) }.sum() /
                            (selected.size * (selected.size - 1))
            )
            val bestOption = selected.maxBy { it.fitness }

            log.debug("Gen %3d - AF %.3f (%.3f). Best Opt: %s".format(event.generation, averageFitness, averageFitnessDeviation, bestOption))
        }
    }

    private fun traceEvent(event: ProcessorEvent<*>, eventDescription: String) =
            log(log.isTraceEnabled) {
                log.trace("Generation ${event.generation} - $eventDescription : ${populationToConsole(event.population)}")
            }

    private fun tracerCross(event: ProcessorEvent<*>, eventDescription: String) = log(log.isEnabled(TRACER)) {
        log.log(TRACER, "Generation ${event.generation} - $eventDescription: ${event.population[0].valueToString()} x ${event.population[1].valueToString()}")
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        when (event.eventType) {
            TypeProcessorEvent.STARTING                  -> log(log.isTraceEnabled) {
                log.trace("Starting. Max generations: ${event.generation}")
            }
            TypeProcessorEvent.FIRST_GENERATION_CREATING -> log(log.isDebugEnabled) {
                log.info("Creating First Generation")
            }
            TypeProcessorEvent.GENERATION_EVALUATING     -> log(log.isDebugEnabled) {
                log.debug("********** Generation ${event.generation} ***********")
            }
            TypeProcessorEvent.REPRODUCING               -> log(log.isTraceEnabled) {
                log.trace("Generation ${event.generation} - Reproducing")
            }
            TypeProcessorEvent.CROSSING                  -> tracerCross(event, "Crossing")
            TypeProcessorEvent.CROSSED                   -> tracerCross(event, "Crossed ")
            TypeProcessorEvent.MUTATION_EXECUTING        -> traceEvent(event, "Mutating")
            TypeProcessorEvent.FITNESS_CALCULATING       -> traceEvent(event, "Calculating Fitness")
            TypeProcessorEvent.SELECTING                 -> traceEvent(event, "Selecting")
            TypeProcessorEvent.GENERATION_EVALUATED      -> log(log.isDebugEnabled, debugGenerationEvaluated(event))
            TypeProcessorEvent.ENDED_BY_GENERATIONS      -> log(log.isDebugEnabled) {
                log.debug("Max Generations reached: $maxGenerations")
            }
            TypeProcessorEvent.ENDED_BY_FITNESS          -> log(log.isDebugEnabled) {
                log.debug("Value found: ${event.population}")
            }
            TypeProcessorEvent.ENDED_BY_INTERRUPTION     -> log(log.isDebugEnabled) {
                log.debug("Interrupted: ${event.population}")
            }
        }
    }

}