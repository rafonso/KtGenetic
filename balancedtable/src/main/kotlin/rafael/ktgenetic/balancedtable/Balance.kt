package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.OrderedGene

typealias Box = OrderedGene<Int>

typealias Boxes = List<Box>

data class Balance(override val content: Boxes, private val dimensions: BalanceDimensions) : Chromosome<Box>() {

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

    override fun valueToString(): String = "[CM = %.3f, MI = %2.3f - %s]".
            format(centerOfMass, momentOfInertia, content.map { it.value })

    val halfMasses: Pair<Int, Int> by lazy {
        val leftMass = dimensions.half.first.map { content[it].value }.sum()
        val rightMass = dimensions.half.second.map { content[it].value }.sum()

        Pair(leftMass, rightMass)
    }

    override fun toString() = super.toString()

}
