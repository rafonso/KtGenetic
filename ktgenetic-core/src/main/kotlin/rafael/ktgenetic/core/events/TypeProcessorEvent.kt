package rafael.ktgenetic.core.events

/**
 * Represents the type of eventType that occurs during the evolutionary process in [rafael.ktgenetic.core.processor.GeneticProcessor].
 * Each type of eventType is accompanied by a specific kind of object in [ProcessorEvent] class.
 */
enum class TypeProcessorEvent(val ended: Boolean = false) {
    /**
     * Waiting for processing start.
     */
    WAITING,
    /**
     * Stating of processing.
     */
    STARTING,
    /**
     * The first generation will be created.
     */
    FIRST_GENERATION_CREATING,
    /**
     * A generation is about to be analyzed.
     */
    GENERATION_EVALUATING,
    /**
     * The member of the population will be crossed to generate the children.
     * ([rafael.ktgenetic.core.Chromosome]).
     */
    REPRODUCING,
    /**
     * 2 [rafael.ktgenetic.core.Chromosome]s are about to cross. Companion: The 2 rafael.ktgenetic.core.Chromosome to be crossed.
     */
    CROSSING,
    /**
     * 2 [rafael.ktgenetic.core.Chromosome]s were crossed. Companion: The 2 rafael.ktgenetic.core.Chromosome created from crosseding.
     */
    CROSSED,
    /**
     * The children of the population was generated. Companion: the created children ([rafael.ktgenetic.core.Chromosome]).
     */
    MUTATION_EXECUTING,
    /**
     * The children of the population will have their [rafael.ktgenetic.core.Chromosome.fitness] calculated. Companion: the created children
     * ([rafael.ktgenetic.core.Chromosome])
     * with their [rafael.ktgenetic.core.Chromosome.fitness] as zero.
     */
    FITNESS_CALCULATING,
    /**
     * The children will selected according their [rafael.ktgenetic.core.Chromosome.fitness]. Companion: the created children [rafael.ktgenetic.core.Chromosome])
     * with their [rafael.ktgenetic.core.Chromosome.fitness] filled.
     */
    SELECTING,
    /**
     * The generation was analyzed; the generation number was incremented and the selected children will be the
     * population of the next generation. Companion: the population of the next generation
     */
    GENERATION_EVALUATED,
    /**
     * Processing ended because the generation number reached the maximum number of generations indicated by
     * [rafael.ktgenetic.core.Environment.maxGenerations]. Companion: The last Generation.
     */
    ENDED_BY_FITNESS(true),
    /**
     * Processing ended because the generator was interrupted by calling [GeneticProcessor#stop()].
     * Companion: The last Generation.
     */
    ENDED_BY_INTERRUPTION(true),
    /**
     * Processing ended because the desired criteria indicated by [rafael.ktgenetic.core.Environment.resultFound] was reached.
     * Companion: The last Generation.
     */
    ENDED_BY_GENERATIONS(true),
    /**
     * There was a error that throws an Exception
     */
    ERROR(true)
}
