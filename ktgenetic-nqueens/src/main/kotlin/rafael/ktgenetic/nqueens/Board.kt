package rafael.ktgenetic.nqueens

import rafael.ktgenetic.Chromosome

data class Board(override val content: List<Int>, private var _collisions: Int = -1) : Chromosome<Int>() {

    companion object BoardUtils {

        private val oddColors = arrayOf(' ', '\u2592')
        private val evenColors = oddColors.reversedArray()
        private const val queen = 'Q'

        internal fun validateContent(content: List<Int>) {
            val values = mutableSetOf<Int>()
            content.forEach {
                require(it >= 0 && it < content.size) { "Irregular Value: $it. It should be between 0 and ${content.size}" }
                require(!values.contains(it)) { "Repeated Value: $it" }
                values.add(it)
            }
        }

        private fun boardToStringRows(board: Board): List<String> {
            val oddHouses =  board.content.indices.map { oddColors[it % 2] }.joinToString("")
            val evenHouses = board.content.indices.map { evenColors[it % 2] }.joinToString("")
            val rowNumberFormat = "%${board.content.size.toString().length}d \u2016"

            return board.content.mapIndexed { row, filledColumn ->
                val rowColors = (if (row % 2 == 0) oddHouses else evenHouses).toCharArray()
                rowColors[filledColumn] = queen
                rowColors.joinToString(separator = "|", prefix = rowNumberFormat.format(row), postfix = "‖")
            }
        }

        fun printBoard(board: Board) {
            val boardRows = boardToStringRows(board)
            boardRows.forEach(::println)
        }

    }

    var collisions: Int
        get() = this._collisions
        internal set(value) {
            this._collisions = value
        }

    init {
        // Validation
        validateContent(this.content)
    }

    override fun toString(): String = content.joinToString(separator = "|")
}