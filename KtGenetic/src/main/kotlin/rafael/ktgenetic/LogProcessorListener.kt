package rafael.ktgenetic

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import rafael.ktgenetic.console.TRACER

/**
 * Emits log messages according the [ProcessorEvent].
 */
class LogProcessorListener<out G, C : Chromosome<G>> : ProcessorListener {

    private val CONSOLE_SIZE = 120

    private val log: Logger = LogManager.getLogger("LOG")

    private var currentGeneration: Int = 0
    private var maxGenerations: Int = 0


    private fun chromosomeToString(c: Chromosome<C>) = c.toString()

    private fun chromosomeToValueString(c: Chromosome<C>) = c.valueToString()


    private fun populationToConsole(population: List<Chromosome<C>>, toString: (Chromosome<C>) -> String): String {

        val genotypesByLine =
                if (toString(population[0]).length < CONSOLE_SIZE) CONSOLE_SIZE / (toString(population[0]).length + 1)
                else 1

        return population.mapIndexed({ index, genotype ->
            if (index % genotypesByLine == 0) ("\n${toString(genotype)}") else (toString(genotype))
        }
        ).joinToString(separator = " ") // { separator = " " }
    }

    private fun log(correctLogLevel: Boolean, logAction: () -> Unit) {
        if (correctLogLevel) {
            logAction()
        }
    }

    override fun onEvent(event: ProcessorEvent) {
        when (event.event) {
            ProcessorEventEnum.STARTING -> {
                maxGenerations = event.value as Int
                log.info("Starting. Max generations: {}", maxGenerations)
            }
            ProcessorEventEnum.FIRST_GENERATION_CREATING -> {
                log(log.isDebugEnabled, {
                    //                val target = event.value as String
                    log.info("Creating First Generation")
                })
            }
            ProcessorEventEnum.FIRST_GENERATION_CREATED -> {
                log(log.isTraceEnabled) {
                    val population = event.value as List<Chromosome<C>>
                    log.trace("First Generation: {}", populationToConsole(population, this::chromosomeToValueString))
                }
            }
            ProcessorEventEnum.GENERATION_EVALUATING -> {
                currentGeneration = event.value as Int
                log(log.isDebugEnabled, {
                    log.debug("********** Generation $currentGeneration ***********")
                })
            }
            ProcessorEventEnum.REPRODUCING -> {
                log(log.isTraceEnabled, {
                    log.trace("Generation $currentGeneration - Reproducing")
                })
            }
            ProcessorEventEnum.CROSSING -> {
                log(log.isEnabled(TRACER), {
                    val (parent1, parent2) = event.value as Pair<List<G>, List<G>>
                    log.log(TRACER, "Generation $currentGeneration - Crossing: {} x {}", parent1, parent2)
                })
            }
            ProcessorEventEnum.CROSSED -> {
                log(log.isEnabled(TRACER), {
                    val (children1, children2) = event.value as Pair<List<G>, List<G>>
                    log.log(TRACER, "Generation $currentGeneration - Crossed : {} x {}", children1, children2)
                })
            }
            ProcessorEventEnum.REPRODUCED -> {
                log(log.isTraceEnabled, {
                    val children = event.value as List<Chromosome<C>>
                    log.trace("Generation $currentGeneration - Reproduced: {}", populationToConsole(children, this::chromosomeToValueString))
                })
            }
            ProcessorEventEnum.MUTATION_EXECUTING -> {
                log(log.isTraceEnabled, {
                    val children = event.value as List<Chromosome<C>>
                    log.trace("Generation $currentGeneration - Mutating: {}", populationToConsole(children, this::chromosomeToValueString))
                })
            }
            ProcessorEventEnum.MUTATION_EXECUTED -> {
                log(log.isTraceEnabled, {
                    val children = event.value as List<Chromosome<C>>
                    log.trace("Generation $currentGeneration - Mutaded: {}", populationToConsole(children, this::chromosomeToValueString))
                })
            }
            ProcessorEventEnum.FITNESS_CALCULATING -> {
                log(log.isTraceEnabled, {
                    val population = event.value as List<Chromosome<C>>
                    log.trace("Generation $currentGeneration - Calculating Fitness: {}", populationToConsole(population, this::chromosomeToValueString))
                })
            }
            ProcessorEventEnum.FITNESS_CALCULATED -> {
                log(log.isTraceEnabled, {
                    val children = event.value as List<Chromosome<C>>
                    log.trace("Generation $currentGeneration - Fitness Calculated: {}", populationToConsole(children, this::chromosomeToString))
                })
            }
            ProcessorEventEnum.SELECTING -> {
                log(log.isTraceEnabled, {
                    val selected = event.value as List<Chromosome<C>>
                    log.trace("Generation $currentGeneration - Selecting {}", populationToConsole(selected, this::chromosomeToString))
                })
            }
            ProcessorEventEnum.SELECTED -> {
                log(log.isDebugEnabled, {
                    val selected = event.value as List<Chromosome<C>>
                    log.trace("Generation $currentGeneration - Fitted selected: {}", populationToConsole(selected, this::chromosomeToString))
                })
            }
            ProcessorEventEnum.GENERATION_EVALUATED -> {
                log(log.isDebugEnabled, {
                    val selected = event.value as List<Chromosome<C>>
                    val averageFitness = selected.map { it.fitness }.sum() / selected.size
                    val averageFitnessDeviation = Math.sqrt(
                            selected.map { Math.pow(it.fitness - averageFitness, 2.0) }.sum() /
                                    (selected.size * (selected.size - 1))
                    )
                    val bestOption = selected.maxBy { it.fitness }

                    log.debug(("Generation %3d - Average Fitness %.3f (%.3f). " +
                            "Best Option: %s").format(currentGeneration, averageFitness, averageFitnessDeviation, bestOption))
                })
            }
            ProcessorEventEnum.ENDED_BY_GENERATIONS -> {
                log(log.isDebugEnabled, {
                    log.trace("Processing concluded by Generations concluded: $maxGenerations")
                })
            }
            ProcessorEventEnum.ENDED_BY_FITNESS -> {
                log(log.isDebugEnabled, {
                    log.debug("Value found: {}", event.value)
                })
            }
            ProcessorEventEnum.ENDED_BY_INTERRUPTION -> {
                log(log.isDebugEnabled, {
                    log.debug("Interrupted: {}", event.value)
                })
            }
            else -> error("Event not recognized: $event")
        }
    }

}