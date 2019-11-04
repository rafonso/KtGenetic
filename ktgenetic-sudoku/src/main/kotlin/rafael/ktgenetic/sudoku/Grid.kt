package rafael.ktgenetic.sudoku

import rafael.ktgenetic.Chromosome
import java.lang.StringBuilder

data class Grid(
    override val content: List<Row>,
    private var _conflicts: List<Conflict> = listOf(),
    private var _numOfCollisions: Int = Int.MAX_VALUE
) : Chromosome<Row>() {

    companion object GridUtils {

        fun validate(grid: Grid) {
            grid.content.forEachIndexed { index, row ->
                require(row.size == grid.size) {
                    "Row $index should have size ${grid.size}: $grid"
                }

                val rowValues = mutableSetOf<Element>()
                row.forEach { value ->
                    require(!rowValues.contains(value)) {
                        "Row $index contains repeated value ($value): $grid"
                    }
                    rowValues.add(value)
                }
            }
        }


        fun formatGrid(grid: Grid): String {
            val gridSize = grid.size
            val boxSize = getBoxSizeBySize(gridSize)
            val elementSize = if (gridSize > 10) 3 else 2
            val format = " %${elementSize - 1}d"
            val boxSeparator =
                (("+" + "-".repeat(elementSize).repeat(boxSize)).repeat(boxSize)).replaceFirst('+', '|') + "|\n"

            val sbGrid = StringBuilder()
            grid.rows.forEachIndexed { index, row ->
                if (index % boxSize == 0) {
                    sbGrid.append(boxSeparator)
                }
                row.forEachIndexed { col, value ->
                    if (col % boxSize == 0) {
                        sbGrid.append("|")
                    }
                    sbGrid.append(format.format(value))
                }
                sbGrid.append("|\n")
            }
            sbGrid.append(boxSeparator)

            return sbGrid.toString()
        }

    }

    var conflicts: List<Conflict>
        get() = this._conflicts
        internal set(value) {
            this._conflicts = value
            _numOfCollisions = this._conflicts.size
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
        )}${if (conflicts.isEmpty()) "" else ", Collisions: $conflicts"})"""

}