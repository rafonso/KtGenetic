package rafael.ktgenetic.processor

import rafael.ktgenetic.*
import rafael.ktgenetic.selection.SelectionOperator

/**
 * Executes the evolutionary process.
 */
abstract class GeneticProcessor<G, C : Chromosome<G>>(val environment: Environment<G, C>,
                                                      private val selectionOperator: SelectionOperator<C>) {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    var continueProcessing = true

    private fun notifyEvent(event: ProcessorEvent<*>) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    protected fun basicCrossing(pieces1: ListPieces<G>, pieces2: ListPieces<G>): List<C> = listOf(
            environment.createNewChromosome(pieces2.left + pieces1.core + pieces2.right),
            environment.createNewChromosome(pieces1.left + pieces2.core + pieces1.right)
    )

    protected abstract fun executeCrossing(pieces1: ListPieces<G>, pieces2: ListPieces<G>): List<C>

    private fun executeMutation(chromosome: C): C = if (Math.random() < environment.mutationFactor) environment.createNewChromosome(environment.executeMutation(chromosome.content))
    else chromosome


    private fun cross(generation: Int, parent1: C, parent2: C): List<C> {
        notifyEvent(ProcessorEvent(TypeProcessorEvent.CROSSING, generation, listOf(parent1, parent2)))

        val cutPositions = environment.getCutPositions()

        val pieces1 = environment.cutIntoPieces(parent1.content, cutPositions)
        val pieces2 = environment.cutIntoPieces(parent2.content, cutPositions)

        val children = executeCrossing(pieces1, pieces2)

        notifyEvent(ProcessorEvent(TypeProcessorEvent.CROSSED, generation, children.toList()))

        return children.toList()
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

        notifyEvent(ProcessorEvent(TypeProcessorEvent.GENERATION_EVALUATING, generation, parents))

        notifyEvent(ProcessorEvent(TypeProcessorEvent.REPRODUCING, generation, parents))
        val children = (parents.indices).pFlatMap { i ->
            ((i + 1) until parents.size).pFlatMap { j ->
                cross(generation, parents[i], parents[j])
            }
        } + parents

        notifyEvent(ProcessorEvent(TypeProcessorEvent.MUTATION_EXECUTING, generation, children))
        val mutated = children.pMap { executeMutation(it) }

        notifyEvent(ProcessorEvent(TypeProcessorEvent.FITNESS_CALCULATING, generation, mutated))
        // Calculate Fitness
        mutated.forEach {
            it.fitness = environment.calculateFitness(it)
        }

        notifyEvent(ProcessorEvent(TypeProcessorEvent.SELECTING, generation, mutated))
        val selected = selectionOperator.select(mutated.sortedBy { it.fitness }.reversed())

        notifyEvent(ProcessorEvent(TypeProcessorEvent.GENERATION_EVALUATED, generation, selected))

        return processGeneration(generation + 1, selected)
    }


    fun process(): ProcessorEvent<C> {
        notifyEvent(ProcessorEvent(TypeProcessorEvent.STARTING, environment.maxGenerations, listOf()))

        notifyEvent(ProcessorEvent(TypeProcessorEvent.FIRST_GENERATION_CREATING, 0, listOf()))
        val population = environment.getFirstGeneration()

        val result = processGeneration(1, population)
        notifyEvent(result)

        return result
    }

    fun stop() {
        continueProcessing = false
    }

    fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}