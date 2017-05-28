package rafael.ktgenetic

import java.util.*
import kotlin.collections.LinkedHashSet

class GeneticProcessor(val parameter: ProcessorParameters) {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val random = Random()

    private val range = ' '.rangeTo('~') + 192.toChar().rangeTo(255.toChar())

    private val fitnessFunction: StringFitness = EqualCharsFitness()

    private val breeder = Breeder()

    val wordComparator = WordFitnessComparator()

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    private fun randomChar(): Char = range[random.nextInt(range.size)]

    private fun createRandomWord(length: Int): String = 1.rangeTo(length).map { _ -> randomChar() }.joinToString("")

    private fun terminate(population: List<Word>, target: String): Boolean = population.any { it.value.equals(target) }

    private fun createFirstGeneration(target: String) = 1.rangeTo(parameter.childrenToSave)
            .map { _ -> createRandomWord(target.length) }
            .sorted().map { Word(it) }

    private fun createChidrenOf(parent1: Word, population: List<Word>): List<Word> {
        return population.flatMap { parent2 -> breeder.cross(parent1.value, parent2.value) }.map { Word(it) }
    }

    private fun resultFound(population: List<Word>, target: String): Boolean = population[0].value.equals(target)

    public fun process(target: String): List<Word> {
        notifyEvent(ProcessorEvent(ProcessorEventEnum.STARTING, parameter.maxGenerations))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATING, target))
        var population = createFirstGeneration(target)
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATED, population))

        var generation = 1
        while (!resultFound(population, target) && (generation <= parameter.maxGenerations)) { //}terminate(population, target)) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATING, generation))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCING, population))
            val children: List<Word> = population
                    .flatMap { parent1 -> population.flatMap { parent2 -> breeder.cross(parent1.value, parent2.value) } }
                    .map { Word(it) }
            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATING, children))
            // Calculate Fitness
            children.forEach({
                it.fitness = fitnessFunction.calculate(it.value, target)
            })
            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTING, children))
            val selected = children
                    .sortedWith(wordComparator)
                    .reversed()
                    .subList(0, parameter.childrenToSave)
            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTED, selected))


//            log.trace(generation.toString())
            population = selected
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATED, population))
            generation++
        }

        if(generation <= parameter.maxGenerations) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_FITNESS, population[0]))
        } else {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_GENERATIONS))
        }
        return population
    }

    public fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    public fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)


}