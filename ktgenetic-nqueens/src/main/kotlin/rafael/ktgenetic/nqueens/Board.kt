package rafael.ktgenetic.nqueens

import rafael.ktgenetic.Chromosome

data class Board(override val content: List<Int>) : Chromosome<Int>() {

    constructor(values: Set<Int>) : this(values.toList())

    init {
        content.forEach { require(it >= 0 && it < content.size) }
    }

    override fun toString(): String = content.joinToString(separator = "|")
}