package rafael.ktgenetic

/**
 * Indicates the parameters to be used during the processing in [GeneticProcessor].
 */
interface Environment<G, N :  Genotype<G>> {

    /**
     *
     */
    val mutationFactor: Double

    val maxGenerations: Int

    val generationSize: Int;

    fun getFirstGeneration(): List<N>;

    fun getCutPositions(): Pair<Int, Int>

    fun cutIntoPieces(gene: G, cutPositions: Pair<Int, Int>): Triple<G, G, G>

    fun executeMutation(gene: G): G

    fun joinPieces(segments: List<G>): G

    fun getNewGenetotype(gene: G): N

    fun calculateFitness(gene: G): Double

    fun resultFound(genotypes: List<N>): Boolean = false

}