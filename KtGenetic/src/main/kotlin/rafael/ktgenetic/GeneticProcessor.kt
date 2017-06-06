package rafael.ktgenetic

/**
 * Executes the evolutionary process.
 */
open class GeneticProcessor<G, C : Chromosome<G>>(val environment: Environment<G, C>) {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val genotypeComparator = ChromosomeFitnessComparator<G, C>()

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    open protected fun <G, C : Chromosome<G>> executeCrossing(
            pieces1: Triple<List<G>, List<G>, List<G>>,
            pieces2: Triple<List<G>, List<G>, List<G>>):
            Pair<List<G>, List<G>> =
            Pair(
                    pieces2.first + pieces1.second + pieces2.third,
                    pieces1.first + pieces2.second + pieces1.third
            )

    private fun cross(parent1: List<G>, parent2: List<G>):
            List<List<G>> {

        fun submitMutation(segment: List<G>): List<G> =
                if (Math.random() < environment.mutationFactor)
                    environment.executeMutation(segment)
                else segment

        notifyEvent(ProcessorEvent(ProcessorEventEnum.CROSSING, Pair(parent1, parent2)))

        val cutPositions = environment.getCutPositions()

        val pieces1 = environment.cutIntoPieces(parent1, cutPositions)
        val pieces2 = environment.cutIntoPieces(parent2, cutPositions)

        val pieces1AfterMutation = Triple(submitMutation(pieces1.first), submitMutation(pieces1.second), submitMutation(pieces1.third))
        val pieces2AfterMutation = Triple(submitMutation(pieces2.first), submitMutation(pieces2.second), submitMutation(pieces2.third))

        // Crossing
        val children = executeCrossing(pieces1AfterMutation, pieces2AfterMutation)

        notifyEvent(ProcessorEvent(ProcessorEventEnum.CROSSED, children))

        return children.toList()
    }

    private tailrec fun processGeneration(generation: Int, parents: List<C>): Pair<Int, List<C>> {
        if (environment.resultFound(parents) || (generation > environment.maxGenerations)) {
            return Pair(generation, parents)
        }

        notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATING, generation))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCING, parents))
        val children: List<C> = (0 until parents.size).flatMap { i ->
            (i until parents.size).flatMap { j ->
                cross(parents[i].content, parents[j].content)
            }
        }.map { environment.getNewGenetotype(it) }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATING, children))
        // Calculate Fitness
        children.forEach {
            it.fitness = environment.calculateFitness(it.content)
        }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTING, children))
        val selected = children
                .sortedWith(genotypeComparator)
                .reversed()
                .subList(0, environment.generationSize)
        notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTED, selected))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATED, selected))

        return processGeneration(generation + 1, selected)
    }

    public fun process(): List<C> {
        notifyEvent(ProcessorEvent(ProcessorEventEnum.STARTING, environment.maxGenerations))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATING))
        val population = environment.getFirstGeneration() // .map { environment.getNewGenetotype(it) }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATED, population))

        val (generation, finalPopulation) = processGeneration(1, population)

        if (generation <= environment.maxGenerations) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_FITNESS, finalPopulation[0]))
        } else {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_GENERATIONS, finalPopulation[0]))
        }

        return finalPopulation
    }

    public fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    public fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}