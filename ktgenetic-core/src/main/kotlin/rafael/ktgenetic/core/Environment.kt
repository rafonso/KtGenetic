package rafael.ktgenetic.core

import rafael.ktgenetic.core.utils.ListPieces
import rafael.ktgenetic.core.utils.makeCuttingIntoPieces

/**
 * Interface representing the environment in which a genetic algorithm operates.
 *
 * The environment defines the parameters to be used during the processing in [rafael.ktgenetic.core.processor.GeneticProcessor].
 *
 * @param G The type of the gene values.
 * @param C The type of the chromosomes in the environment.
 */
interface Environment<G, C : Chromosome<G>> {

    /**
     * Value from 0 to 1 indicating the probability to mutate some [Chromosome].
     */
    var mutationFactor: Double

    /**
     * The maximum number of generations.
     */
    val maxGenerations: Int

    /**
     * The size of each generation
     */
    val generationSize: Int

    /**
     * Generates the first generation of chromosomes.
     *
     * @return A list of chromosomes representing the first generation.
     */
    fun getFirstGeneration(): List<C>

    /**
     * Determines the positions at which a chromosome should be cut during crossover.
     *
     * @return A pair of integers representing the cut positions.
     */
    fun getCutPositions(): Pair<Int, Int>

    /**
     * Cuts a sequence of gene values into pieces at the specified positions.
     *
     * @param sequence The sequence of gene values to cut.
     * @param cutPositions The positions at which to cut the sequence.
     * @return A ListPieces object representing the cut pieces.
     */
    fun cutIntoPieces(sequence: List<G>, cutPositions: Pair<Int, Int>): ListPieces<G> =
        makeCuttingIntoPieces(sequence, cutPositions)

    /**
     * Mutates a sequence of gene values.
     *
     * @param sequence The sequence of gene values to mutate.
     * @return A list of gene values representing the mutated sequence.
     */
    fun executeMutation(sequence: List<G>): List<G>

    /**
     * Creates a new chromosome from a sequence of gene values.
     *
     * @param sequence The sequence of gene values from which to create the chromosome.
     * @return A chromosome created from the sequence of gene values.
     */
    fun createNewChromosome(sequence: List<G>): C

    /**
     * Calculates the fitness of a chromosome.
     *
     * @param chromosome The chromosome for which to calculate the fitness.
     * @return The fitness of the chromosome.
     */
    fun calculateFitness(chromosome: C): Double

    /**
     * Determines whether a solution has been found based on the current generation of chromosomes.
     *
     * @param genotypes The current generation of chromosomes.
     * @return `true` if a solution has been found; `false` otherwise.
     */
    fun resultFound(genotypes: List<C>): Boolean = false

}
