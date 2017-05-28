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
