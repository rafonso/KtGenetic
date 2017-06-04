package rafael.ktgenetic

/**
 * Executes the evolutionary process.
 */
class GeneticProcessor<G, C :  Chromosome<G>>() {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val genotypeComparator = ChromosomeFitnessComparator<G, C>()

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    private fun <G, C :  Chromosome<G>> cross(parent1: List<G>, parent2: List<G>, environment: Environment<G, C>):
            List<List<G>> {

        fun submitMutation(segment: List<G>): List<G> =
                if (Math.random() < environment.mutationFactor)
                    environment.executeMutation(segment)
                else segment

        notifyEvent(ProcessorEvent(ProcessorEventEnum.CROSSING, Pair(parent1, parent2)))

        val cutPositions = environment.getCutPositions()

        val (tail1Left, parent1Core, tail1Right) = environment.cutIntoPieces(parent1, cutPositions)
        val (tail2Left, parent2Core, tail2Right) = environment.cutIntoPieces(parent2, cutPositions)

        val tail1LeftFinal = submitMutation(tail1Left)
        val tail1RightFinal = submitMutation(tail1Right)
        val tail2LeftFinal = submitMutation(tail2Left)
        val tail2RightFinal = submitMutation(tail2Right)

        // Crossing
        val child1 = environment.joinPieces(tail2LeftFinal, parent1Core, tail2RightFinal)
        val child2 = environment.joinPieces(tail1LeftFinal, parent2Core, tail1RightFinal)

        notifyEvent(ProcessorEvent(ProcessorEventEnum.CROSSED, Pair(child1, child2)))

        return listOf(child1, child2)
    }

    public  fun process(environment: Environment<G, C>): List<C> {
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