package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.OrderedGene

typealias Box = OrderedGene<Int>

typealias Boxes = List<Box>

data class Balance(override val content: Boxes, private val dimensions: BalanceDimensions) : Chromosome<Box>() {

    val totalMass: Int
        get() = content.map { it.value }.sum()

    val centerOfMass: Double by lazy {
        (0 until content.size).map { dimensions.positions[it] * content[it].value }.sum() /
                totalMass
    }

    val momentOfInertia: Double by lazy {
        (0 until content.size).
                map { dimensions.distanceFromCenter[it] * dimensions.distanceFromCenter[it] * content[it].value }.
                sum()
    }

    override fun valueToString(): String = "[CM = %.3f, MI = %2.3f - %s]".
            format(centerOfMass, momentOfInertia, content.map { it.value })

    override fun toString() = super.toString()

}
