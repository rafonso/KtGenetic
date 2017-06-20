package rafael.ktgenetic

import rafael.ktgenetic.selection.SelectionStrategy

/**
 * Executes the evolutionary process.
 */
abstract class GeneticProcessor<G, C : Chromosome<G>>(val environment: Environment<G, C>,
                                                  val selectionStrategy: SelectionStrategy<C>) {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val genotypeComparator = ChromosomeFitnessComparator<C>()

    var continueProcessing = true

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    protected fun <G> basicCrossing(pieces1: ListPieces<G>, pieces2: ListPieces<G>): Pair<List<G>, List<G>> = Pair(
            pieces2.left + pieces1.core + pieces2.right,
            pieces1.left + pieces2.core + pieces1.right
    )

    abstract protected fun <G> executeCrossing(pieces1: ListPieces<G>, pieces2: ListPieces<G>): Pair<List<G>, List<G>>

    fun executeMutation(chromosome: C): C = if (Math.random() < environment.mutationFactor) environment.getNewGenotype(environment.executeMutation(chromosome.content))
    else chromosome


    private fun cross(parent1: List<G>, parent2: List<G>): List<List<G>> {
        notifyEvent(ProcessorEvent(ProcessorEventEnum.CROSSING, Pair(parent1, parent2)))

        val cutPositions = environment.getCutPositions()

        val pieces1 = environment.cutIntoPieces(parent1, cutPositions)
        val pieces2 = environment.cutIntoPieces(parent2, cutPositions)

        val children = executeCrossing(pieces1, pieces2)

        notifyEvent(ProcessorEvent(ProcessorEventEnum.CROSSED, children))

        return children.toList()
    }

    private tailrec fun processGeneration(generation: Int, parents: List<C>): Pair<Int, List<C>> {
        if (!continueProcessing || environment.resultFound(parents) || (generation > environment.maxGenerations)) {
            return Pair(generation, parents)
        }

        notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATING, generation))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCING, parents))
        var children: List<C> = (0 until parents.size).flatMap { i ->
            (i until parents.size).flatMap { j ->
                cross(parents[i].content, parents[j].content)
            }
        }.pMap { environment.getNewGenotype(it) }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.MUTATION_EXECUTING, children))
        children = children.pMap { executeMutation(it) }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.MUTATION_EXECUTED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATING, children))
        // Calculate Fitness
        children.forEach {
            it.fitness = environment.calculateFitness(it.content)
        }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTING, children))
        val selected = selectionStrategy.select(children)
        notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTED, selected))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATED, selected))

        return processGeneration(generation + 1, selected)
    }


    fun process(): List<C> {
        notifyEvent(ProcessorEvent(ProcessorEventEnum.STARTING, environment.maxGenerations))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATING))
        val population = environment.getFirstGeneration()
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATED, population))

        val (generation, finalPopulation) = processGeneration(1, population)

        val result = finalPopulation.sortedBy { it.fitness }.reversed()
        val cause = if (!continueProcessing) {
            ProcessorEventEnum.ENDED_BY_INTERRUPTION
        } else if (generation <= environment.maxGenerations) {
            ProcessorEventEnum.ENDED_BY_FITNESS
        } else {
            ProcessorEventEnum.ENDED_BY_GENERATIONS
        }
        notifyEvent(ProcessorEvent(cause, result))

        return result
    }

    fun stop() {
        continueProcessing = false
    }

    fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}