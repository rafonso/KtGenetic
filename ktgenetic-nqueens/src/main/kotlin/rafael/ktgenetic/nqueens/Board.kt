package rafael.ktgenetic.nqueens

import rafael.ktgenetic.core.Chromosome

typealias Collision = Pair<Int, Int>

/**
 * Represents a board in the N-Queens problem.
 *
 * @property content A list of integers representing the positions of the queens on the board.
 * @property piece The piece (queen) placed on the board.
 * @property _collisions A list of collisions (pairs of queens that can attack each other).
 * @property _numOfCollisions The number of collisions on the board.
 */
data class Board(
    override val content: List<Int>,
    val piece: Piece,
    private var _collisions: List<Collision> = listOf(),
    private var _numOfCollisions: Int = Int.MAX_VALUE
) : Chromosome<Int>() {

    companion object BoardUtils {

        private val oddColors = arrayOf(' ', '\u2592')
        private val evenColors = oddColors.reversedArray()

        /**
         * Converts the board to a list of strings, each representing a row on the board.
         *
         * @param board The board to convert.
         * @return A list of strings representing the board.
         */
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

        /**
         * Prints the board to the console.
         *
         * @param board The board to print.
         */
        fun printBoard(board: Board) {
            val boardRows = boardToStringRows(board)
            boardRows.forEach(::println)
        }

    }

    /**
     * A list of collisions on the board.
     */
    var collisions: List<Collision>
        get() = this._collisions
        internal set(value) {
            this._collisions = value
            _numOfCollisions = this._collisions.size
        }

    /**
     * The number of collisions on the board.
     */
    val numOfCollisions: Int
        get() = _numOfCollisions


    /**
     * The size of the board.
     */
    val size: Int
        get() = content.size

    init {
        this.piece.validateBoard(this)
    }

    /**
     * Compares this board to another chromosome.
     *
     * @param other The other chromosome to compare to.
     * @return A negative integer, zero, or a positive integer as this board is less than, equal to, or greater than the specified chromosome.
     */
    override fun compareTo(other: Chromosome<Int>): Int {
        var diff = super.compareTo(other)
        if (diff == 0) {
            diff = if (this.content == other.content)
                0
            else
                this.content.indices.map {
                    other.content[it].compareTo(
                        this.content[it]
                    )
                }.first { it != 0 }
        }
        return diff
    }

    /**
     * Returns a string representation of the board.
     *
     * @return A string representation of the board.
     */
    override fun toString(): String =
        """(${content.joinToString(separator = "|")}${if (collisions.isEmpty()) "" else ", Collisions: $collisions"})"""

}
