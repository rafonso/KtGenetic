package rafael.ktgenetic.sudoku

// TODO: Create a interface
class BasicConflictCounter {

    private fun calculateColumnConflicts(grid: Grid, col: Int): List<Conflict> {
        val column = grid.columns[col]

        return column.indices.flatMap { row1 ->
            when (val row2 = (row1 + 1 until column.size).firstOrNull { row2 -> column[row1] == column[row2] }) {
                null -> emptyList()
                else -> listOf(Conflict(row1, col, row2, col, column[row1], TypeConflict.COLUMN))
            }
        }
    }

    private fun calculateBoxConflicts(grid: Grid, box: Int): List<Conflict> {
        val boxPositions = getBoxesPositions(grid.size)[box]

        return boxPositions.indices.flatMap { index1 ->
            val pos1 = boxPositions[index1]
            val value = grid.rows[pos1.row][pos1.col]

            (index1 + 1 until boxPositions.size)
                .map { boxPositions[it] }
                .filter { pos2 -> value == grid.rows[pos2.row][pos2.col] }
                .map { pos2 -> Conflict(pos1, pos2, value, TypeConflict.BOX) }
        }
    }

    fun calculateConflicts(grid: Grid): List<Conflict> =
        (0 until grid.size).flatMap {
            calculateColumnConflicts(
                grid,
                it
            )
        } + (0 until grid.size).flatMap { calculateBoxConflicts(grid, it) }


}