package rafael.ktgenetic.pallete

import rafael.ktgenetic.Chromosome


private fun getCenterOfMassOfRow(boxes: Boxes, row: Int, dimensions: PalleteDimensions): Double {
    val rowMass = (0 until dimensions.cols).map { c -> boxes[dimensions.positionToIndex(row, c)].weight }.sum()
    if (rowMass == 0) {
        return dimensions.cols.toDouble() / 2
    }
    val weightedMass = (0 until dimensions.cols).map { c -> (c + 0.5) * boxes[dimensions.positionToIndex(row, c)].weight }.sum()
    return weightedMass / rowMass
}

private fun getCenterOfMassOfColumn(boxes: Boxes, column: Int, dimensions: PalleteDimensions): Double {
    val colMass = (0 until dimensions.rows).map { r -> boxes[dimensions.positionToIndex(r, column)].weight }.sum()
    if (colMass == 0) {
        return dimensions.rows.toDouble() / 2
    }
    val weightedMass = (0 until dimensions.rows).map { r -> (r + 0.5) * boxes[dimensions.positionToIndex(r, column)].weight }.sum()
    return weightedMass / colMass
}

typealias Boxes = List<Box>

data class Pallete(override val content: Boxes, private val dimensions: PalleteDimensions) : Chromosome<Box>() {

    val centerOfMass: Point by lazy {
        val cmRow = (0 until dimensions.rows).map {
            r ->
            getCenterOfMassOfRow(content, r, dimensions)
        }.sum() / dimensions.rows
        val cmCol = (0 until dimensions.cols).map {
            c ->
            getCenterOfMassOfColumn(content, c, dimensions)
        }.sum() / dimensions.cols

        Point(cmRow, cmCol)
    }

    val momentOfInertia =
            content.mapIndexed {
                index, box ->
                box.weight * dimensions.distanceFromCenter[index] * dimensions.distanceFromCenter[index]
            }.sum()

    val palleteToString: String by lazy {
        boxesToString("\n")
    }

    private fun boxesToString(rowSeparator: String): String {
        return (0 until dimensions.rows).map {
            r ->
            (0 until dimensions.cols).map { c ->
                "%3d".format(content[r * dimensions.cols + c].weight)
            }.joinToString(separator = " ")
        }.joinToString(separator = rowSeparator)
    }

    override fun valueToString(): String {
        return boxesToString("|")
    }

    override fun toString() = super.toString()

}


