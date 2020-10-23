package rafael.ktgenetic.core

import rafael.ktgenetic.core.utils.randomIntExclusive
import kotlin.random.Random

class TemplateChromosome(override val content: List<Char>, fit: Double) : Chromosome<Char>() {

    companion object {
        fun createRamdomChromossomes(size: Int) = (1..size).map {
            TemplateChromosome(
                String(
                    charArrayOf(
                        'A' + randomIntExclusive(26),
                        'A' + randomIntExclusive(26),
                        'A' + randomIntExclusive(26)
                    )
                ), Random.nextDouble()
            )
        }
    }

    constructor(word: String, fit: Double) : this(word.toList(), fit)

    constructor(word: String) : this(word.toList(), 0.0)

    init {
        this.fitness = fit
    }

    fun toWord() = content.joinToString("")

    override fun toString(): String = "[%s, %.10f]".format(toWord(), this.fitness)

}
