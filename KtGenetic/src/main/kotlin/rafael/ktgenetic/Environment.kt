package rafael.ktgenetic

/**
 * Indicates the parameters to be used during the processing in [GeneticProcessor].
 */
interface Environment<G, C :  Chromosome<G>> {

    /**
     *
     */
    val mutationFactor: Double

    val maxGenerations: Int

    val generationSize: Int;

    fun getFirstGeneration(): List<C>;

    fun getCutPositions(): Pair<Int, Int>

    fun cutIntoPieces(gene: G, cutPositions: Pair<Int, Int>): Triple<G, G, G>

    fun executeMutation(gene: G): G

    fun joinPieces(segments: List<G>): G

    fun getNewGenetotype(gene: G): C

    fun calculateFitness(gene: G): Double

    fun resultFound(genotypes: List<C>): Boolean = false

}