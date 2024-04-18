package rafael.ktgenetic.core

/**
 * Represents a Chromosome with a set of Genes.
 *
 * @param G the gene type of this Chromosome
 * @constructor Creates a new genotype with a initial fitness, by default 0.0
 */
abstract class Chromosome<G>(private var _fitness: Double = 0.0): Comparable<Chromosome<G>> {

    /**
     * Fitness value indicating how much this genotype value is near from the goal. The greater, better.
     */
    var fitness: Double
        get() = _fitness
    // "internal" removed
        set(f) {
            _fitness = f
        }

    /**
     * Chromosome value
     */
    abstract val content: List<G>

    /**
     * String representation of [content] to be used in [toString]
     *
     * @return A string representation of the content.
     */
    open fun valueToString(): String = content.toString()

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    override fun toString() = "[${"%.3f".format(fitness)}, ${valueToString()}]"

    /**
     * Compares this object with the specified object for order.
     *
     * @param other The object to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    override fun compareTo(other: Chromosome<G>): Int = this.fitness.compareTo(other.fitness)

}
