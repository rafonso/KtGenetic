package rafael.ktgenetic

/**
 * Represents a Chromosome with a set of Genes.
 *
 * @param G the gene type of this Chromosome
 * @constructor Creates a new genotype with a initial fitness, by default 0.0
 */
abstract class Chromosome<G>(private var _fitness: Double = 0.0) {

    /**
     * Fitness value indicating how much this genotype value is near from the goal. The greater, better.
     */
    var fitness: Double
        get() = _fitness
        internal set(f) {
            _fitness = f
        }

    /**
     * Chromosome value
     */
    abstract val content: List<G>

    /**
     * String representation of [content] to be used in [toString]
     */
    open fun valueToString(): String = content.toString()

    override fun toString() = "[${"%.3f".format(fitness)}, ${valueToString()}]"

}