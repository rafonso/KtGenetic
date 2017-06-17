package rafael.ktgenetic

/**
 * Indicates the parameters to be used during the processing in [GeneticProcessor].
 */
interface Environment<G, C : Chromosome<G>> {

    /**
     *
     */
    val mutationFactor: Double

    val maxGenerations: Int

    val generationSize: Int;

    fun getFirstGeneration(): List<C>

    fun getCutPositions(): Pair<Int, Int>

    fun cutIntoPieces(sequence: List<G>, cutPositions: Pair<Int, Int>): Triple<List<G>, List<G>, List<G>>

    fun executeMutation(sequence: List<G>): List<G>

    fun getNewGenotype(sequence: List<G>): C

    fun calculateFitness(sequence: List<G>): Double

    fun resultFound(genotypes: List<C>): Boolean = false

}