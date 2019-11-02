package rafael.ktgenetic.nqueens

import rafael.ktgenetic.Chromosome

typealias Collision = Pair<Int, Int>

/**
 *
 */
data class Board(override val content: List<Int>, val piece: Piece, private var _collisions: List<Collision> = listOf(), private var _numOfCollisions: Int = Int.MAX_VALUE) : Chromosome<Int>() {

    companion object BoardUtils {

        private val oddColors = arrayOf(' ', '\u2592')
        private val evenColors = oddColors.reversedArray()

        private fun boardToStringRows(board: Board): List<String> {
            val oddHouses = board.content.indices.map { oddColors[it % 2] }.joinToString("")
            val evenHouses = board.content.indices.map { evenColors[it % 2] }.joinToString("")
            val rowNumberFormat = "%${board.content.size.toString().length}d \u2016"

            return board.content.mapIndexed { row, filledColumn ->
                val rowColors = (if (row % 2 == 0) oddHouses else evenHouses).toCharArray()
                rowColors[filledColumn] = board.piece.code
                rowColors.joinToString(separator = "|", prefix = rowNumberFormat.format(row), postfix = "‖")
            }
        }

        fun printBoard(board: Board) {
            val boardRows = boardToStringRows(board)
            boardRows.forEach(::println)
        }

    }

    var collisions: List<Collision>
        get() = this._collisions
        internal set(value) {
            this._collisions = value
            _numOfCollisions = this._collisions.size
        }

    val numOfCollisions: Int
        get() = _numOfCollisions

    val size: Int
        get() = content.size

    init {
        this.piece.validateBoard(this)
    }

    override fun toString(): String = """(${content.joinToString(separator = "|")}${if (collisions.isEmpty()) "" else ", Collisions: $collisions"})"""
}