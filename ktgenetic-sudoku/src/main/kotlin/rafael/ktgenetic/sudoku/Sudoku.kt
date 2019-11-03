package rafael.ktgenetic.sudoku

import kotlin.math.sqrt

private val boxesBySize = mutableMapOf<Int, List<List<Position>>>()

private fun calculateBoxPositions(size: Int): List<List<Position>> {
    val boxSize = sqrt(size.toDouble()).toInt()

    // Validation
    require((boxSize * boxSize) == size) {
        "Size $size is not a perfect square"
    }

    return (0 until size).map { box ->
        val boxRow = box / boxSize
        val boxCol = box % boxSize

        val row0 = boxRow * boxSize
        val row1 = row0 + boxSize
        val col0 = boxCol * boxSize
        val col1 = col0 + boxSize

        (row0 until row1).flatMap { row ->
            (col0 until col1).map { col -> Position(row, col) }
        }
    }
}

fun getBoxesPositions(size: Int): List<List<Position>> =
    boxesBySize.computeIfAbsent(size) {
        calculateBoxPositions(size)
    }

fun rowToString(row: Row) = row.joinToString(separator = ",", prefix = "", postfix = "")

typealias Element = Int

typealias Row = List<Element>

enum class TypeConflict {
    COLUMN, BOX
}

data class Position(val row: Int, val col: Int)

data class Conflict(val pos1: Position, val pos2: Position, val typeConflict: TypeConflict)