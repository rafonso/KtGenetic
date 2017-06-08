package rafael.ktgenetic

/**
 * Represents the type of event that occurs during the evolutionary process in [GeneticProcessor]. Each type of event is
 * accompanied by a specific kind of object in [ProcessorEvent] class.
 */
enum class ProcessorEventEnum {
    /**
     * Stating of processing. Companion: Tha max number of generations ([Environment.maxGenerations])
     */
    STARTING,
    /**
     * The first generation will be created. Companion: None.
     */
    FIRST_GENERATION_CREATING,
    /**
     * The first generation was created. Companion: First generation population ([Chromosome]).
     */
    FIRST_GENERATION_CREATED,
    /**
     * A generation is about to be analyzed. Companion: the current generation number.
     */
    GENERATION_EVALUATING,
    /**
     * The member of the population will be crossed to generate the children. Companion: the current population
     * ([Chromosome]).
     */
    REPRODUCING,
    /**
     * 2 [Chromosome]s are about to cross. Companion: The 2 Chromosome to be crossed.
     */
    CROSSING,
    /**
     * 2 [Chromosome]s were crossed. Companion: The 2 Chromosome created from crosseding.
     */
    CROSSED,
    /**
     * The children of the population was generated. Companion: the created children ([Chromosome]).
     */
    REPRODUCED,
    /**
     * The children of the population will be submitted to mutation. Companion: the created children ([Chromosome]).
     */
    MUTATION_EXECUTING,
    /**
     * The children of the population were submitted to mutation. Companion: the mutated children ([Chromosome]).
     */
    MUTATION_EXECUTED,
    /**
     * The children of the population will have their [Chromosome.fitness] calculated. Companion: the created children
     * ([Chromosome])
     * with their [Chromosome.fitness] as zero.
     */
    FITNESS_CALCULATING,
    /**
     * The children of the population had their [Chromosome.fitness] calculated. Companion: the created children
     * ([Chromosome]) with their [Chromosome.fitness] filled.
     */
    FITNESS_CALCULATED,
    /**
     * The children will selected according their [Chromosome.fitness]. Companion: the created children [Chromosome])
     * with their [Chromosome.fitness] filled.
     */
    SELECTING,
    /**
     * The children where selected according their [Chromosome.fitness]; just those with the greatest [Chromosome.fitness]
     * survived to the next generation. Companion: the surviving children [Chromosome]).
     */
    SELECTED,
    /**
     * The generation was analyzed; the generation number was incremented and the selected children will be the
     * population of the next generation. Companion: the population of the next generation
     */
    GENERATION_EVALUATED,
    /**
     * Processing ended because the generation number reached the maximum number of generations indicated by
     * [Environment.maxGenerations]. Companion: The last Generation.
     */
    ENDED_BY_FITNESS,
    /**
     * Processing ended because the desired criteria indicated by [Environment.resultFound] was reached.
     * Companion: The last Generation.
     */
    ENDED_BY_GENERATIONS
}