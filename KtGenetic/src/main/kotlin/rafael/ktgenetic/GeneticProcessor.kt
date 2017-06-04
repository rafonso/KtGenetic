package rafael.ktgenetic

/**
 * Executes the evolutionary process.
 */
class GeneticProcessor<G, C : Chromosome<G>>() {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val genotypeComparator = ChromosomeFitnessComparator<G, C>()

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    protected fun <G, C : Chromosome<G>> executeCrossing(
            pieces1: Triple<List<G>, List<G>, List<G>>,
            pieces2: Triple<List<G>, List<G>, List<G>>,
            environment: Environment<G, C>):
            Pair<List<G>, List<G>> =
            Pair(
                    pieces2.first + pieces1.second + pieces2.third,
                    pieces1.first + pieces1.second + pieces1.third
            )

    private fun <G, C : Chromosome<G>> cross(parent1: List<G>, parent2: List<G>, environment: Environment<G, C>):
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
        val children = executeCrossing(pieces1AfterMutation, pieces2AfterMutation, environment)

        notifyEvent(ProcessorEvent(ProcessorEventEnum.CROSSED, children))

        return children.toList()
    }

    public fun process(environment: Environment<G, C>): List<C> {
        notifyEvent(ProcessorEvent(ProcessorEventEnum.STARTING, environment.maxGenerations))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATING))
        var population = environment.getFirstGeneration() // .map { environment.getNewGenetotype(it) }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATED, population))

        var generation = 1
        while (!environment.resultFound(population) && (generation <= environment.maxGenerations)) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATING, generation))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCING, population))
            val children = population
                    .flatMap {
                        parent1 ->
                        population.flatMap {
                            parent2 ->
                            cross(parent1.content, parent2.content, environment)
                        }
                    }
                    .map { environment.getNewGenetotype(it) }
            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATING, children))
            // Calculate Fitness
            children.forEach({
                it.fitness = environment.calculateFitness(it.content)
            })
            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTING, children))
            val selected = children
                    .sortedWith(genotypeComparator)
                    .reversed()
                    .subList(0, environment.generationSize)
            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTED, selected))

            population = selected
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATED, population))
            generation++
        }

        if (generation <= environment.maxGenerations) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_FITNESS, population[0]))
        } else {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_GENERATIONS, population[0]))
        }

        return population
    }

    public fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    public fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}