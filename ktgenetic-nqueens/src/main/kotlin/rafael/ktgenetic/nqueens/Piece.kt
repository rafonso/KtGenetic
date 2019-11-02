package rafael.ktgenetic.nqueens

import rafael.ktgenetic.geneticRandom

enum class Piece(val symbol: String) {

    QUEEN("\u2655") {
        override fun validateBoard(board: Board) {
            val content = board.content
            val values = mutableSetOf<Int>()

            content.forEach {
                require(it >= 0 && it < content.size) { "Irregular Value: $it. It should be between 0 and ${content.size}" }
                require(!values.contains(it)) { "Repeated Value: $it" }
                values.add(it)
            }
        }

        override fun createContent(boardSize: Int): List<Int> = (0 until boardSize).shuffled(geneticRandom)


    },
    BISHOP("\u2657") {
        override fun validateBoard(board: Board) {
            val content = board.content

            content.forEach {
                require(it >= 0 && it < content.size) { "Irregular Value: $it. It should be between 0 and ${content.size}" }
            }
        }

        override fun createContent(boardSize: Int): List<Int> = (0 until boardSize).map { geneticRandom.nextInt(boardSize) }

    };

    val code: Char by lazy {
        this.name[0]
    }

    abstract fun createContent(boardSize: Int): List<Int>

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

    abstract fun validateBoard(board: Board)

}