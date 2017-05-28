package rafael.ktgenetic

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogProcessorListener : ProcessorListener {

    private val CONSOLE_SIZE = 120

    private val log: Logger = LoggerFactory.getLogger("ProcessorLog")

    private var currentGeneration: Int = 0
    private var maxGenerations: Int = 0

    private fun populationToConsole(population: List<Word>): String {
        val wordsByLine = CONSOLE_SIZE / (population[0].toString().length + 1)

        return population.mapIndexed({ index, word ->
            if (index % wordsByLine == 0) ("\n$word") else ("$word")
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
                val target = event.value as String
                log.info("Starting. Target: {}", target)
            }
            ProcessorEventEnum.FIRST_GENERATION_CREATED -> {
                log(log.isTraceEnabled, {
                    val population = event.value as List<Word>
                    log.trace("First Generation: {}", populationToConsole(population))
                })
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
            ProcessorEventEnum.REPRODUCED -> {
                log(log.isTraceEnabled, {
                    val children = event.value as List<Word>
                    log.trace("Generation $currentGeneration - Reproduced: {}", populationToConsole(children))
                })
            }
            ProcessorEventEnum.FITNESS_CALCULATING -> {
                log(log.isTraceEnabled, {
                    val population = event.value as List<Word>
                    log.trace("Generation $currentGeneration - Calculating Fitness: {}", populationToConsole(population))
                })
            }
            ProcessorEventEnum.FITNESS_CALCULATED -> {
                log(log.isTraceEnabled, {
                    log.trace("Generation $currentGeneration - Fitness Calculated")
                })
            }
            ProcessorEventEnum.SELECTING -> {
                log(log.isTraceEnabled, {
                    log.trace("Generation $currentGeneration - Selecting ...")
                })
            }
            ProcessorEventEnum.SELECTED -> {
                log(log.isDebugEnabled, {
                    val selected = event.value as List<Word>
                    log.trace("Generation $currentGeneration - Fitted selected: {}", populationToConsole(selected))
                })
            }
            ProcessorEventEnum.GENERATION_EVALUATED -> {
                log(log.isDebugEnabled, {
                    val selected = event.value as List<Word>
                    val averageFitness = selected.map { it.fitness }.sum() / selected.size

                    log.debug("Generation %d - Best Option: %s. General Fitness %.3f".format(currentGeneration, selected[0], averageFitness))
                })
            }
            ProcessorEventEnum.ENDED_BY_GENERATIONS -> {
                log(log.isDebugEnabled, {
                    log.trace("Processing concluded by Generations concluded: $maxGenerations")
                })
            }
            ProcessorEventEnum.ENDED_BY_FITNESS -> {
                log(log.isDebugEnabled, {
                    log.debug("Word found: {}", (event.value as Word).value)
                })
            }
            else -> error("Event not recognized: $event")
        }
    }
}