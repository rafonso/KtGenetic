package rafael.ktgenetic.nqueens

import rafael.ktgenetic.Chromosome

data class Board(override val content: List<Int>, private var _collisions: Int = -1) : Chromosome<Int>() {

    var collisions: Int
        get() = this._collisions
        internal set(value) {
            this._collisions = value
        }

    init {
        // Validation
        val values = mutableSetOf<Int>()
        content.forEach {
            require(it >= 0 && it < content.size) { "Irregular Value: $it. It should be between 0 and ${content.size}" }
            require(!values.contains(it)) { "Repeated Value: $it" }
            values.add(it)
        }
    }

    override fun toString(): String = content.joinToString(separator = "|")
}