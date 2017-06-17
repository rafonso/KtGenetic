package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.OrderedGene

typealias Box = OrderedGene<Int>

typealias Boxes = List<Box>

data class Balance(override val content: Boxes) : Chromosome<Box>() {

    private fun distance(i: Int, pos: Double): Double = Math.abs(i + 0.5 - pos)

    val totalMass: Int
        get() = content.map { it.value }.sum()

    val centerOfMass: Double by lazy {
        content.mapIndexed({ index, (value) -> distance(index, 0.0) * value }).sum().toDouble() /
                totalMass
    }

    val momentOfInertia: Double by lazy {

        fun individualInertia(i: Int): Double {
            val distanceFromCM = distance(i, centerOfMass)
            return content[i].value * distanceFromCM * distanceFromCM
        }

        content.mapIndexed { index, _ -> individualInertia(index) }.sum()
    }

    override fun toString(): String = "[CM = %.3f, MI = %2.3f - %s]".
            format(centerOfMass, momentOfInertia, content.map { it.value })

}
