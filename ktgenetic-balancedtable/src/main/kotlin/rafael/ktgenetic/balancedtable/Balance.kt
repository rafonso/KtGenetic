package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.core.Chromosome
import rafael.ktgenetic.core.OrderedGene

typealias Box = OrderedGene<Int>

typealias Boxes = List<Box>

data class Balance(override val content: Boxes, private val dimensions: BalanceDimensions) : Chromosome<Box>() {

    private fun calcHalfMasses(): Pair<Int, Int> {
        val leftMass = dimensions.halves.first.sumOf { content[it].value }
        val rightMass = dimensions.halves.second.sumOf { content[it].value }

        return Pair(leftMass, rightMass)
    }

    val totalMass: Int
        get() = content.sumOf { it.value }

    val centerOfMass: Double by lazy {
        (content.indices).sumOf { dimensions.blocks[it] * content[it].value } /
                totalMass
    }

    val momentOfInertia: Double by lazy {
        (content.indices).sumOf { dimensions.distanceFromCenter[it] * dimensions.distanceFromCenter[it] * content[it].value }
    }

    val halfMasses: Pair<Int, Int> = calcHalfMasses()

    override fun valueToString(): String = "[CM = %.3f, MI = %2.3f, HM = %s - %s]".
            format(centerOfMass, momentOfInertia, halfMasses, content.map { it.value })

    override fun toString() = super.toString()

}
