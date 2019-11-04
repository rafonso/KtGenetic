package rafael.ktgenetic.sudoku

import kotlin.math.sqrt

private val boxesBySize = mutableMapOf<Int, List<List<Position>>>()

private val boxSizeBySize = mutableMapOf<Int, Int>()

private val maxConflictsBySize = mutableMapOf<Int, Int>()

private fun calculateBoxPositions(size: Int): List<List<Position>> {
    val boxSize = getBoxSizeBySize(size)

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

fun getMaxConflictsBySize(size: Int): Int =
    maxConflictsBySize.computeIfAbsent(size) {
        val boxSize = getBoxSizeBySize(size)

        return@computeIfAbsent size * ((size - 1) + boxSize * (boxSize - 1))
    }

fun getBoxSizeBySize(size: Int): Int =
    boxSizeBySize.computeIfAbsent(size) {
        val result = sqrt(size.toDouble()).toInt()

        // Validation
        require((result * result) == size) {
            "Size $size is not a perfect square"
        }

        return@computeIfAbsent result
    }

fun rowToString(row: Row) = row.joinToString(separator = ",", prefix = "", postfix = "")

typealias Element = Int

typealias Row = List<Element>

enum class TypeConflict {
    COLUMN, BOX
}

data class Position(val row: Int, val col: Int) {
    override fun toString(): String = "($row, $col)"
}

/**
 * Indicates the positions where
 *
 * @property pos1 First Position
 * @property pos2 Second Position
 * @property value repeated value
 * @property typeConflict
 */
data class Conflict(val pos1: Position, val pos2: Position, val value: Element, val typeConflict: TypeConflict) {

    /**
     *
     */
    constructor(row1: Int, col1: Int, row2: Int, col2: Int, value: Element, typeConflict: TypeConflict) :
            this(Position(row1, col1), Position(row2, col2), value, typeConflict)

}