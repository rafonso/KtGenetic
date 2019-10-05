package rafael.ktgenetic.nqueens

import rafael.ktgenetic.Chromosome

data class Board(override val content: List<Int>, private var _collisions: Int = -1) : Chromosome<Int>() {

    constructor(values: Set<Int>) : this(values.toList())

    var collisions: Int
        get() = this._collisions
        internal set(value) {
            this._collisions = value
        }

    init {
        content.forEach { require(it >= 0 && it < content.size) }
    }

    override fun toString(): String = content.joinToString(separator = "|")
}