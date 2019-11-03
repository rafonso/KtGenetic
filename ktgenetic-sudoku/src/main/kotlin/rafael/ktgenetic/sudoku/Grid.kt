package rafael.ktgenetic.sudoku

import rafael.ktgenetic.Chromosome
import kotlin.math.sqrt

typealias Element = Int

typealias Row = List<Element>

enum class TypeCollision {
    ROW, COLUMN, BOX
}

data class Position(val row: Int, val col: Int)

data class Collision(val pos1: Position, val pos2: Position, val typeCollision: TypeCollision)

data class Grid(
    override val content: List<Row>,
    private var _collisions: List<Collision> = listOf(),
    private var _numOfCollisions: Int = Int.MAX_VALUE
) : Chromosome<Row>() {

    companion object GridUtils {

        private val boxesBySize = mutableMapOf<Int, List<List<Position>>>()

        private fun calculateBoxPositions(size: Int): List<List<Position>> {
            val boxSize = sqrt(size.toDouble()).toInt()

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

        fun validate(grid: Grid) {
            grid.content.forEachIndexed { index, row ->
                require(row.size == grid.size) {
                    "Row $index should have size ${grid.size}: ${rowToString(row)}"
                }

                val rowValues = mutableSetOf<Element>()
                row.forEach { value ->
                    require(!rowValues.contains(value)) {
                        "Row $index contains repeated value ($value): ${rowToString(row)}"
                    }
                    rowValues.add(value)
                }
            }
        }

        fun rowToString(row: Row) = row.joinToString(separator = ",", prefix = "", postfix = "")

        fun getBoxesPositions(size: Int): List<List<Position>> =
            boxesBySize.computeIfAbsent(size) {
                calculateBoxPositions(size)
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

    val rows = this.content

    val columns: List<List<Element>> by lazy {
        this.rows.indices.map { col ->
            this.rows.map { row -> row[col] }
        }
    }

    val boxes: List<List<Element>> by lazy {
        getBoxesPositions(size).map { boxPositions ->
            boxPositions.map { pos -> rows[pos.row][pos.col] }
        }
    }

    init {
        validate(this)
    }

    override fun toString(): String =
        """(${content.joinToString(
            separator = "|",
            transform = ::rowToString
        )}${if (collisions.isEmpty()) "" else ", Collisions: $collisions"})"""

}