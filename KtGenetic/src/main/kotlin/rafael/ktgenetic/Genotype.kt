package rafael.ktgenetic

/**
 * Represents a genotype.
 *
 * @param G the type of this genotype
 * @constructor Creates a new genotype with a initial fitness, by default 0.0
 */
abstract class Genotype<G>(private var _fitness: Double = 0.0) {

    /**
     * Fitness value indicating how much this genotype value is near from the goal. The greater, better.
     */
    var fitness: Double
        get() = _fitness
        set(f) {
            _fitness = f
        }

    /**
     * Genotype value
     */
    abstract val value: G;

    /**
     * String representation of [value] to be used in [toString]
     */
    abstract protected fun valueToString(): String;

    override fun toString() = "[${valueToString()}, ${"%.3f".format(fitness)}]"

}
