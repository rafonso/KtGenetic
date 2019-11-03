package rafael.ktgenetic.sudoku

import rafael.ktgenetic.Chromosome

data class Grid(
    override val content: List<Row>,
    private var _conflicts: List<Conflict> = listOf(),
    private var _numOfCollisions: Int = Int.MAX_VALUE
) : Chromosome<Row>() {

    companion object GridUtils {

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