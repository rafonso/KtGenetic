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

    private fun <G, C :  Chromosome<G>> cross(parent1: List<G>, parent2: List<G>, environment: Environment<G, C>): List<List<G>> {

        fun submitMutation(segment: List<G>): List<G> =
                if (Math.random() < environment.mutationFactor)
                    environment.executeMutation(segment)
                else segment

        val cutPositions = environment.getCutPositions()

        val (parent1Segment1, parent1Core, parent1Segment2) = environment.cutIntoPieces(parent1, cutPositions)
        val (parent2Segment1, parent2Core, parent2Segment2) = environment.cutIntoPieces(parent2, cutPositions)

        val parent1Piece1Selected = submitMutation(parent1Segment1)
        val parent1Piece2Selected = submitMutation(parent1Segment2)
        val parent2Piece1Selected = submitMutation(parent2Segment1)
        val parent2Piece2Selected = submitMutation(parent2Segment2)

        // Crossing
        val child1 = parent2Piece1Selected + parent1Core + parent2Piece2Selected
        val child2 = parent1Piece1Selected + parent2Core + parent1Piece2Selected

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