package rafael.ktgenetic

/**
 * Indicates the parameters to be used during the processing in [GeneticProcessor].
 */
interface GeneticParameters<G> {

    /**
     *
     */
    val mutationFactor: Double

    val maxGenerations: Int

    val generationSize: Int;

    fun getFirstGeneration(): List<G>;

    fun getCutPositions(): Pair<Int, Int>

    fun cutIntoPieces(gene: G, cutPositions: Pair<Int, Int>): Array<G>

    fun executeMutation(segment: G): G

    fun joinPieces(pieces: List<G>): G

    fun getNewGenetotype(genotype: G): Genotype<G>

    fun calculateFitness(genotype: G): Double

    fun resultFound(genotypes: List<Genotype<G>>): Boolean = false

}