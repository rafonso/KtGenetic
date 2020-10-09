package rafael.ktgenetic.processor

import rafael.ktgenetic.*
import rafael.ktgenetic.selection.SelectionOperator

/**
 * Executes the evolutionary process.
 *
 * @param crossingType Crossing Type
 * @param environment Environment with selecion rules
 * @param selectionOperator [SelectionOperator] to be used
 */
class GeneticProcessor<G, C : Chromosome<G>>(
    crossingType: GeneticCrossingType,
    val environment: Environment<G, C>,
    private val selectionOperator: SelectionOperator<C>
) {

    private val geneticCrosser = crossingType.getGeneticCrosser<G, C>()

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    var continueProcessing = true

    private fun notifyEvent(event: ProcessorEvent<*>) {
        listeners.parallelStream().forEach { it.onEvent(event) }
    }

    private fun notifyEvent(eventType: TypeProcessorEvent, generation: Int, population: List<C>) {
        notifyEvent(ProcessorEvent(eventType, generation, population))
    }

    private fun executeMutation(chromosome: C): C =
        if (Math.random() < environment.mutationFactor) environment.createNewChromosome(
            environment.executeMutation(chromosome.content)
        )
        else chromosome


    private fun cross(parent1: C, parent2: C): List<C> {
        val cutPositions = environment.getCutPositions()

        val pieces1 = environment.cutIntoPieces(parent1.content, cutPositions)
        val pieces2 = environment.cutIntoPieces(parent2.content, cutPositions)

        return geneticCrosser.executeCrossing(pieces1, pieces2, environment)
    }

    private fun inferEndProcessingType(generation: Int): TypeProcessorEvent =
        if (!continueProcessing) {
            TypeProcessorEvent.ENDED_BY_INTERRUPTION
        } else if (generation <= environment.maxGenerations) {
            TypeProcessorEvent.ENDED_BY_FITNESS
        } else {
            TypeProcessorEvent.ENDED_BY_GENERATIONS
        }

    private tailrec fun processGeneration(generation: Int, parents: List<C>): ProcessorEvent<C> {
        if (!continueProcessing || environment.resultFound(parents) || (generation > environment.maxGenerations)) {
            return ProcessorEvent(inferEndProcessingType(generation), generation - 1, parents)
        }

        val selected = processPopulation(generation, parents)

        return processGeneration(generation + 1, selected)
    }

    private fun processPopulation(generation: Int, parents: List<C>): List<C> {
        try {
            notifyEvent(TypeProcessorEvent.GENERATION_EVALUATING, generation, parents)

            notifyEvent(TypeProcessorEvent.REPRODUCING, generation, parents)
            val children = (parents.indices).pFlatMap { i ->
                ((i + 1) until parents.size).pFlatMap { j ->
                    notifyEvent(TypeProcessorEvent.CROSSING, generation, listOf(parents[i], parents[j]))
                    cross(parents[i], parents[j]).also {
                        notifyEvent(TypeProcessorEvent.CROSSED, generation, it)
                    }
                }
            } + parents

            notifyEvent(TypeProcessorEvent.MUTATION_EXECUTING, generation, children)
            val mutated = children.pMap { executeMutation(it) }

            notifyEvent(TypeProcessorEvent.FITNESS_CALCULATING, generation, mutated)
            // Calculate Fitness
            mutated.forEach {
                it.fitness = environment.calculateFitness(it)
            }

            notifyEvent(TypeProcessorEvent.SELECTING, generation, mutated)
            val selected = selectionOperator.select(mutated.sortedBy { it.fitness }.reversed())

            notifyEvent(TypeProcessorEvent.GENERATION_EVALUATED, generation, selected)
            return selected
        } catch (e: Exception) {
            notifyEvent(ProcessorEvent(TypeProcessorEvent.ERROR, generation, parents, e))
            throw e
        }
    }


    fun process(): ProcessorEvent<C> {
        notifyEvent(TypeProcessorEvent.STARTING, environment.maxGenerations, listOf())

        notifyEvent(TypeProcessorEvent.FIRST_GENERATION_CREATING, 0, listOf())
        val population = environment.getFirstGeneration()

        return processGeneration(1, population).also { notifyEvent(it) }
    }

    fun stop() {
        continueProcessing = false
    }

    fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    @Suppress("unused")
    fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}
