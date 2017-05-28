package rafael.ktgenetic

class GeneticProcessor<G>() {

    private val listeners: MutableSet<ProcessorListener> = LinkedHashSet()

    val genotypeComparator = GenotypeFitnessComparator<G>()

    private fun notifyEvent(event: ProcessorEvent) {
        listeners.parallelStream().forEach({ it.onEvent(event) })
    }

    public fun cross(parent1: G, parent2: G, parameter: GeneticParameters<G>): List<G> {
        val cutPositions = parameter.getCutPositions()

        val pieces1 = parameter.cutIntoPieces(parent1, cutPositions)
        val pieces2 = parameter.cutIntoPieces(parent2, cutPositions)

        // Crossing
        val child1 = parameter.joinPieces(listOf(parameter.executeMutation(pieces2[0]), pieces1[1], parameter.executeMutation(pieces2[2])))
        val child2 = parameter.joinPieces(listOf(parameter.executeMutation(pieces1[0]), pieces2[1], parameter.executeMutation(pieces1[2])))

        return listOf(child1, child2)
    }

    public fun process(target: String, geneticParameter: GeneticParameters<G>): List<Genotype<G>> {
        notifyEvent(ProcessorEvent(ProcessorEventEnum.STARTING, geneticParameter.maxGenerations))

        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATING))
        var population = geneticParameter.getFirstGeneration().map {  geneticParameter.getNewGenetotype(it)  }
        notifyEvent(ProcessorEvent(ProcessorEventEnum.FIRST_GENERATION_CREATED, population))

        var generation = 1
        while (!geneticParameter.resultFound(population) && (generation <= geneticParameter.maxGenerations)) { //}terminate(population, target)) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATING, generation))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCING, population))
            val children  = population
                    .flatMap {
                        parent1 ->
                        population.flatMap {
                            parent2 ->
                            cross(parent1.value, parent2.value, geneticParameter)
                        }
                    }
                    .map { geneticParameter.getNewGenetotype(it) }
            notifyEvent(ProcessorEvent(ProcessorEventEnum.REPRODUCED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATING, children))
            // Calculate Fitness
            children.forEach({
                it.fitness = geneticParameter.calculateFitness(it.value)
            })
            notifyEvent(ProcessorEvent(ProcessorEventEnum.FITNESS_CALCULATED, children))

            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTING, children))
            val selected = children
                    .sortedWith(genotypeComparator)
                    .reversed()
                    .subList(0, geneticParameter.generationSize)
            notifyEvent(ProcessorEvent(ProcessorEventEnum.SELECTED, selected))


            population = selected
            notifyEvent(ProcessorEvent(ProcessorEventEnum.GENERATION_EVALUATED, population))
            generation++
        }

        if (generation <= geneticParameter.maxGenerations) {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_FITNESS, population[0]))
        } else {
            notifyEvent(ProcessorEvent(ProcessorEventEnum.ENDED_BY_GENERATIONS))
        }
        return population
    }

    public fun addListener(listener: ProcessorListener): Boolean = listeners.add(listener)

    public fun removeListener(listener: ProcessorListener): Boolean = listeners.remove(listener)

}