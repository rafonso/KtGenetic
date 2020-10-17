package rafael.ktgenetic.core

class TemplateChromosome(override val content: List<Char>, fit: Double) : Chromosome<Char>() {

    constructor(word: String, fit: Double) : this(word.toList(), fit)

    constructor(word: String) : this(word.toList(), 0.0)

    init {
        this.fitness = fit
    }

    fun toWord() = content.joinToString("")

    override fun toString(): String = "[%s, %.10f]".format(toWord(), this.fitness)

}
