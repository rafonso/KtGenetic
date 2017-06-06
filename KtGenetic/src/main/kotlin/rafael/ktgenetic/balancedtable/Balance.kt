package rafael.ktgenetic.balancedtable

import rafael.ktgenetic.Chromosome

typealias Boxes = List<Box>

data class Balance(override val content: Boxes) : Chromosome<Box>() {

    private fun distance(i: Int, pos: Double): Double = Math.abs(i + 0.5 - pos)

    val totalMass: Int
        get() = content.map { it.weight }.sum()

    val centerOfMass: Double by lazy {
        content.mapIndexed({ index, (weight) -> distance(index, 0.0) * weight }).sum().toDouble() /
                totalMass
    }

    val momentOfInertia: Double by lazy {

        fun individualInertia(i: Int, weight: Int): Double {
            val distanceFromCM = distance(i, centerOfMass)
            return content[i].weight * distanceFromCM * distanceFromCM
        }

        content.mapIndexed { index, (weight) -> individualInertia(index, weight) }.sum()
    }

    override fun toString(): String = "[CM = %.3f, MI = %2.3f - %s]".
            format(centerOfMass, momentOfInertia, content.map { it.weight })

}
