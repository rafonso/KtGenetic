package rafael.ktgenetic

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet

class GeneticProcessor(val parameter: ProcessorParameters) {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val log: Logger = LoggerFactory.getLogger("GeneticProcessor")

    val random = Random()

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({it.onEvent(event)})
    }

    private fun randomChar(): Char = range[random.nextInt(range.size)]

    private fun createRandomWord(length: Int): String = 1.rangeTo(length).map { _ -> randomChar() }.joinToString("")

    private fun terminate(population: List<Word>, target: String): Boolean = population.any { it.value.equals(target) }

    private fun createFirstGeneration(target: String) = 1.rangeTo(parameter.childrenToSave)
            .map { _ -> createRandomWord(target.length) }
            .sorted().map { Word(it) }

    public fun process(target: String): String {
        notifyEvent(ProcessorEvent(ProcessorEventEnum.STARTING, parameter.maxGenerations))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATING, target))
        var population = createFirstGeneration(target)
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATED, population))

        var generation = 1
        while (true && (generation <= parameter.maxGenerations)) { //}terminate(population, target)) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATING, generation))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCING, population))
            val children: List<Word> = population
            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATING, children))
            // Calculate Fitness
            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTING, children))
            val selected = children
            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTED, selected))


//            log.trace(generation.toString())
            population = selected
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATED, population))
            generation++
        }

        notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_GENERATIONS))
        return ""
    }

    public fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    public fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)


}