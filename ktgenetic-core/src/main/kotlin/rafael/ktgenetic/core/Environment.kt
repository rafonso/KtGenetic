package rafael.ktgenetic.core

import rafael.ktgenetic.core.utils.ListPieces
import rafael.ktgenetic.core.utils.makeCuttingIntoPieces

/**
 * Indicates the parameters to be used during the processing in [rafael.ktgenetic.core.processor.GeneticProcessor].
 */
interface Environment<G, C : Chromosome<G>> {

    /**
     * Value from 0 to 1 indicating the probability to mutate some [Chromosome].
     */
    var mutationFactor: Double

    /**
     * The Max number of generations.
     */
    val maxGenerations: Int

    /**
     * The size of each generation
     */
    val generationSize: Int

    fun getFirstGeneration(): List<C>

    fun getCutPositions(): Pair<Int, Int>

    fun cutIntoPieces(sequence: List<G>, cutPositions: Pair<Int, Int>): ListPieces<G> =
        makeCuttingIntoPieces(sequence, cutPositions)

    fun executeMutation(sequence: List<G>): List<G>

    fun createNewChromosome(sequence: List<G>): C

    fun calculateFitness(chromosome: C): Double

    fun resultFound(genotypes: List<C>): Boolean = false

}
