package rafael.ktgenetic.pallete

data class Point(val row: Double, val col: Double) {

    fun distance(p: Point): Double {
        val deltaVertical = row - p.row
        val deltaHorizontal = col - p.col

        return Math.sqrt(deltaHorizontal * deltaHorizontal + deltaVertical * deltaVertical)
    }

    override fun toString(): String = "(%2.2f, %2.2f)".format(row, col)

}

data class PalleteDimensions(val rows: Int, val cols: Int) {

    val center = Point(rows.toDouble() / 2, cols.toDouble() / 2)

    val points: List<Point> = (0 until rows).flatMap { r -> (0 until cols).map { c -> Point(r + 0.5, c + 0.5) } }

    val distanceFromCenter: List<Double> = points.mapNotNull { point -> point.distance(center) }

    val greatestDistanceFromCenter: Double = distanceFromCenter.max() ?: 0.0

    val positionsByRow: Map<Int, List<Int>> =
            (0 until rows).map {
                r ->
                Pair<Int, List<Int>>(r, ((r * cols) until ((r + 1) * cols)).toList()) // + (if (row > 0) 1 else 0) })
            }.toMap()

    val positionsByColumn: Map<Int, List<Int>> =
            (0 until cols).map {
                c ->
                Pair<Int, List<Int>>(c, (c until rows * cols step cols).toList())
            }.toMap()

    fun positionToIndex(row: Int, col: Int): Int {
        assert((row >= 0) && (row < rows) && (col >= 0) && (col < cols),
                { "Position row = $row, col = $col irregular" })

        return cols * row + col
    }

}