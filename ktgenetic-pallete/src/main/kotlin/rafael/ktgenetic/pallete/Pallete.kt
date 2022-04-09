@file:Suppress("DuplicatedCode", "DuplicatedCode")

package rafael.ktgenetic.pallete

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.OrderedGene

private fun getCenterOfMassOfRow(boxes: Boxes, row: Int, dimensions: PalleteDimensions): Double {
    val rowMass = (0 until dimensions.cols).sumOf { c -> boxes[dimensions.blockToIndex(row, c)].value }
    if (rowMass == 0) {
        return dimensions.cols.toDouble() / 2
    }
    val weightedMass = (0 until dimensions.cols).sumOf { c -> (c + 0.5) * boxes[dimensions.blockToIndex(row, c)].value }
    return weightedMass / rowMass
}

private fun getCenterOfMassOfColumn(boxes: Boxes, column: Int, dimensions: PalleteDimensions): Double {
    val colMass = (0 until dimensions.rows).sumOf { r -> boxes[dimensions.blockToIndex(r, column)].value }
    if (colMass == 0) {
        return dimensions.rows.toDouble() / 2
    }
    val weightedMass =
        (0 until dimensions.rows).sumOf { r -> (r + 0.5) * boxes[dimensions.blockToIndex(r, column)].value }
    return weightedMass / colMass
}

typealias Box = OrderedGene<Int>

typealias Boxes = List<Box>

data class Pallete(override val content: Boxes, private val dimensions: PalleteDimensions) : Chromosome<Box>() {

    private fun calcHalfMasses(halves: Pair<List<Int>, List<Int>>): Pair<Int, Int> {
        val mass1 = halves.first.sumOf { content[it].value }
        val mass2 = halves.second.sumOf { content[it].value }

        return Pair(mass1, mass2)
    }

    val totalMass: Int by lazy {
        content.sumOf { it.value }
    }

    val centerOfMass: Point by lazy {
        val cmRow = (0 until dimensions.rows).sumOf { r ->
            getCenterOfMassOfRow(content, r, dimensions)
        } / dimensions.rows
        val cmCol = (0 until dimensions.cols).sumOf { c ->
            getCenterOfMassOfColumn(content, c, dimensions)
        } / dimensions.cols

        Point(cmRow, cmCol)
    }

    val momentOfInertia =
            content.mapIndexed { index, box ->
                box.value * dimensions.distanceFromCenter[index] * dimensions.distanceFromCenter[index]
            }.sum()

    val palleteToString: String by lazy {
        boxesToString("\n")
    }

    val frontBackHalfMasses: Pair<Int, Int> = calcHalfMasses(dimensions.frontAndBackHalves)

    val rightLeftHalfMasses: Pair<Int, Int> = calcHalfMasses(dimensions.leftAndRightHalves)

    private fun boxesToString(rowSeparator: String): String {
        return (0 until dimensions.rows).joinToString(separator = rowSeparator) { r ->
            (0 until dimensions.cols).joinToString(separator = " ") { c ->
                "%3d".format(content[r * dimensions.cols + c].value)
            }
        }
    }

    override fun valueToString(): String = "[CM = %s, MI = %2.3f, FBHM = %s, RLHM = %s - %s]".format(centerOfMass, momentOfInertia, frontBackHalfMasses, rightLeftHalfMasses, boxesToString("|"))

    override fun toString() = super.toString()

}


