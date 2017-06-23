package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.OrderedGene

typealias Box = OrderedGene<Int>

typealias Boxes = List<Box>

data class Balance(override val content: Boxes, private val dimensions: BalanceDimensions) : Chromosome<Box>() {

    private fun calcHalfMasses(): Pair<Int, Int> {
        val leftMass = dimensions.halves.first.map { content[it].value }.sum()
        val rightMass = dimensions.halves.second.map { content[it].value }.sum()

        return Pair(leftMass, rightMass)
    }

    val totalMass: Int
        get() = content.map { it.value }.sum()

    val centerOfMass: Double by lazy {
        (0 until content.size).map { dimensions.blocks[it] * content[it].value }.sum() /
                totalMass
    }

    val momentOfInertia: Double by lazy {
        (0 until content.size).
                map { dimensions.distanceFromCenter[it] * dimensions.distanceFromCenter[it] * content[it].value }.
                sum()
    }

    val halfMasses: Pair<Int, Int> = calcHalfMasses()

    override fun valueToString(): String = "[CM = %.3f, MI = %2.3f, HM = %s - %s]".
            format(centerOfMass, momentOfInertia, halfMasses, content.map { it.value })

    override fun toString() = super.toString()

}
