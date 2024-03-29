package rafael.ktgenetic.pallete

import kotlin.math.sqrt

private fun getHalves(positions: Int, blocks: Map<Int, List<Int>>): Pair<List<Int>, List<Int>> {

    fun getBlocks(start: Int, end: Int) = blocks.filter { (start until end).contains(it.key) }.flatMap { it.value }.sorted()

    val firstHalfEnd = positions / 2
    val secondHalfBegin = firstHalfEnd + (if (positions % 2 == 0) 0 else 1)

    return Pair(getBlocks(0, firstHalfEnd), getBlocks(secondHalfBegin, positions))
}

data class Point(val row: Double, val col: Double) {

    fun distance(p: Point): Double {
        val deltaVertical = row - p.row
        val deltaHorizontal = col - p.col

        return sqrt(deltaHorizontal * deltaHorizontal + deltaVertical * deltaVertical)
    }

    override fun toString(): String = "(%2.2f, %2.2f)".format(row, col)

}

data class PalleteDimensions(val rows: Int, val cols: Int) {

    val center = Point(rows.toDouble() / 2, cols.toDouble() / 2)

    val points: List<Point> = (0 until rows).flatMap { r -> (0 until cols).map { c -> Point(r + 0.5, c + 0.5) } }

    val distanceFromCenter: List<Double> = points.map { point -> point.distance(center) }

    val greatestDistanceFromCenter: Double = distanceFromCenter.maxOrNull() ?: 0.0

    val blocksByRow: Map<Int, List<Int>> =
        (0 until rows).associateWith { r ->
            ((r * cols) until ((r + 1) * cols)).toList()
        }

    val blocksByColumn: Map<Int, List<Int>> =
        (0 until cols).associateWith { c ->
            (c until rows * cols step cols).toList()
        }

    val frontAndBackHalves: Pair<List<Int>, List<Int>> = getHalves(rows, blocksByRow)

    val leftAndRightHalves: Pair<List<Int>, List<Int>> = getHalves(cols, blocksByColumn)

    fun blockToIndex(row: Int, col: Int): Int {
        assert((row >= 0) && (row < rows) && (col >= 0) && (col < cols)
        ) { "Position row = $row, col = $col irregular" }

        return cols * row + col
    }

    fun indexToPosition(index: Int): Pair<Int, Int> = Pair(index / cols, index % cols)

}
