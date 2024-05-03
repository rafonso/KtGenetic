package rafael.ktgenetic.pictures_comparsion.rectangles


private tailrec fun getPositions(
    index: Int,
    lastPosition: Int,
    delta: Int,
    remainder: Int,
    maxPosition: Int,
    positions: IntArray
): IntArray {
    if (index == positions.lastIndex) {
        positions[index] = maxPosition
        return positions
    }

    val newPosition = lastPosition + delta + (if (index < remainder + 1) 1 else 0)
    positions[index] = newPosition
    return getPositions(index + 1, newPosition, delta, remainder, maxPosition, positions)
}

fun createRetangles(width: Int, height: Int, cols: Int, rows: Int): List<Pair<Position, Position>> {
    val horizontalPositions = getPositions(1, 0, width / cols, width % cols, width, IntArray(cols + 1))

    println(horizontalPositions.toList())

    val verticalPositions = getPositions(1, 0, height / rows, height % rows, height, IntArray(rows + 1))

    println(verticalPositions.toList())

    val result = (0 until cols).flatMap { col ->
        (0 until rows).map { row ->
            Pair(
                Position(horizontalPositions[col], verticalPositions[row]),
                Position(horizontalPositions[col + 1], verticalPositions[row + 1])
            )
        }
    }
    println(result)

    return result
}
