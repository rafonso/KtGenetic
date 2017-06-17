package rafael.ktgenetic

/**
 * Executes the evolutionary process.
 */
open class GeneticProcessor<G, C : Chromosome<G>>(val environment: Environment<G, C>) {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val genotypeComparator = ChromosomeFitnessComparator<G, C>()

    var continueProcessing = true

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    open protected fun <G> executeCrossing(pieces1: Triple<List<G>, List<G>, List<G>>, pieces2: Triple<List<G>, List<G>, List<G>>): Pair<List<G>, List<G>> = Pair(pieces2.first + pieces1.second + pieces2.third, pieces1.first + pieces2.second + pieces1.third)

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

    open protected fun select(children: List<C>): List<C> {
        return children.sortedWith(genotypeComparator).reversed().subList(0, environment.generationSize)
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
        }.pmap { environment.getNewGenotype(it) }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.MUTATION_EXECUTING, children))
        children = children.pmap { executeMutation(it) }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.MUTATION_EXECUTED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATING, children))
        // Calculate Fitness
        children.forEach {
            it.fitness = environment.calculateFitness(it.content)
        }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATED, children))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTING, children))
        val selected = select(children)
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

        val cause = if (!continueProcessing) {
            ProcessorEventEnum.ENDED_BY_INTERRUPTION
        } else if (generation <= environment.maxGenerations) {
            ProcessorEventEnum.ENDED_BY_FITNESS
        } else {
            ProcessorEventEnum.ENDED_BY_GENERATIONS
        }
        notifyEvent(ProcessorEvent(cause, finalPopulation))

        return finalPopulation
    }

    fun stop() {
        continueProcessing = false
    }

    fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}