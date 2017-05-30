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
     * The first generation was created. Companion: First generation population ([Genotype]).
     */
    FIRST_GENERATION_CREATED,
    /**
     * A generation is about to be analyzed. Companion: the current generation number.
     */
    GENERATION_EVALUATING,
    /**
     * The member of the population will be crossed to generate the children. Companion: the current population
     * ([Genotype]).
     */
    REPRODUCING,
    /**
     * The children of the population was generated. Companion: the created children ([Genotype]).
     */
    REPRODUCED,
    /**
     * The children of the population will have their [Genotype.fitness] calculated. Companion: the created children
     * ([Genotype])
     * with their [Genotype.fitness] as zero.
     */
    FITNESS_CALCULATING,
    /**
     * The children of the population had their [Genotype.fitness] calculated. Companion: the created children
     * ([Genotype]) with their [Genotype.fitness] filled.
     */
    FITNESS_CALCULATED,
    /**
     * The children will selected according their [Genotype.fitness]. Companion: the created children [Genotype])
     * with their [Genotype.fitness] filled.
     */
    SELECTING,
    /**
     * The children where selected according their [Genotype.fitness]; just those with the greatest [Genotype.fitness]
     * survived to the next generation. Companion: the surviving children [Genotype]).
     */
    SELECTED,
    /**
     * The generation was analyzed; the generation number was incremented and the selected children will be the
     * population of the next generation. Companion: the population of the next generation
     */
    GENERATION_EVALUATED,
    /**
     * Processing ended because the generation number reached the maximum number of generations indicated by
     * [Environment.maxGenerations]. Companion: The genotype with the highest [Genotype.fitness]
     */
    ENDED_BY_FITNESS,
    /**
     * Processing ended because the desired criteria indicated by [Environment.resultFound] was reached.
     * Companion: The genotype with the highest [Genotype.fitness]
     */
    ENDED_BY_GENERATIONS
}