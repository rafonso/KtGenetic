package rafael.ktgenetic.nqueens

import kotlin.random.Random

/**
 * Enum class representing the different types of pieces that can be placed on the board.
 *
 * @property symbol The symbol representing the piece.
 */
enum class Piece(val symbol: String) {

    /**
     * Represents a queen piece.
     */
    QUEEN("\u2655") {
        /**
         * Validates the board for a queen piece.
         *
         * @param board The board to validate.
         */
        override fun validateBoard(board: Board) {
            val content = board.content
            val values = mutableSetOf<Int>()

            content.forEach {
                require(it >= 0 && it < content.size) { "Irregular Value: $it. It should be between 0 and ${content.size}" }
                require(!values.contains(it)) { "Repeated Value: $it" }
                values.add(it)
            }
        }

        /**
         * Creates the content for a queen piece.
         *
         * @param boardSize The size of the board.
         * @return A list of integers representing the content.
         */
        override fun createContent(boardSize: Int): List<Int> = (0 until boardSize).shuffled(Random)


    },
    /**
     * Represents a bishop piece.
     */
    BISHOP("\u2657") {
        /**
         * Validates the board for a bishop piece.
         *
         * @param board The board to validate.
         */
        override fun validateBoard(board: Board) {
            val content = board.content

            content.forEach {
                require(it >= 0 && it < content.size) { "Irregular Value: $it. It should be between 0 and ${content.size}" }
            }
        }

        /**
         * Creates the content for a bishop piece.
         *
         * @param boardSize The size of the board.
         * @return A list of integers representing the content.
         */
        override fun createContent(boardSize: Int): List<Int> = (0 until boardSize).map { Random.nextInt(boardSize) }

    };

    /**
     * The code representing the piece.
     */
    val code: Char by lazy {
        this.name[0]
    }

    /**
     * Abstract function to create the content for a piece.
     *
     * @param boardSize The size of the board.
     * @return A list of integers representing the content.
     */
    abstract fun createContent(boardSize: Int): List<Int>

    /**
     * Calculates the collisions for a piece on the board.
     *
     * @param board The board.
     * @return A list of collisions.
     */
    fun calculateCollisions(board: Board): List<Collision> {
        tailrec fun verifyCollision(initialRow: Int, priorRow: Int, priorDiagonal: Int, deltaColumn: Int): List<Collision> {
            val currentRow = priorRow + 1
            val currentDiagonal = priorDiagonal + deltaColumn
            val ended = currentRow >= board.size || currentDiagonal < 0 || currentDiagonal >= board.size

            return when {
                ended                                        -> emptyList()
                board.content[currentRow] == currentDiagonal -> listOf(Collision(initialRow, currentRow))
                else                                         -> verifyCollision(initialRow, currentRow, currentDiagonal, deltaColumn)
            }
        }

        return (0 until board.size)
                .flatMap { row ->
                    verifyCollision(row, row, board.content[row], 1) +
                            verifyCollision(row, row, board.content[row], -1)
                }
    }

    /**
     * Abstract function to validate the board for a piece.
     *
     * @param board The board to validate.
     */
    abstract fun validateBoard(board: Board)

}