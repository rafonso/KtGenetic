package rafael.ktgenetic.selection

import rafael.ktgenetic.Chromosome

class TemplateChromosome(override val content: List<Char>, fit: Double) : Chromosome<Char>() {

    constructor(word: String, fit: Double) : this(word.toList(), fit)

    init {
        this.fitness = fit
    }

    override fun toString(): String = "[%s, %.10f]".format(content.joinToString(""), this.fitness)
}
