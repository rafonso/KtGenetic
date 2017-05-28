package rafael.ktgenetic

abstract class Genotype<G>(private var _fitness: Double = 0.0) {

    var fitness: Double
        get() = _fitness
        set(f) {
            _fitness = f
        }

    abstract val value: G;

    abstract protected fun valueToString(): String;

    override fun toString() = "[${valueToString()}, ${"%.3f".format(fitness)}]"

}

open class GenotypeFitnessComparator<G>: Comparator<Genotype<G>> {
    override fun compare(g1: Genotype<G>, g2: Genotype<G>): Int = g1.fitness.compareTo(g2.fitness)
}
