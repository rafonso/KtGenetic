package rafael.ktgenetic.camouflage

import rafael.ktgenetic.Chromosome

fun validateColor(x: Int, name: String) {
    require(x in 0..MAX_COLOR_VALUE) { "$name must be between 0 and 255" }
}

const val MAX_COLOR_VALUE = 255

const val MAX_COLOR_VALUE_D = (MAX_COLOR_VALUE * MAX_COLOR_VALUE).toDouble()

/**
 * Represents the color in a pixel. Named as 'Kolor" to avoid confusing with JavaFX `Color`.
 *
 * @param r Red value
 * @param g Green value
 * @param b Blue value
 */
data class Kolor(val r: Int, val g: Int, val b: Int) : Chromosome<Int>() {

    val rDouble = r.toDouble() / MAX_COLOR_VALUE
    val gDouble = g.toDouble() / MAX_COLOR_VALUE
    val bDouble = b.toDouble() / MAX_COLOR_VALUE

    init {
        validateColor(r, "R")
        validateColor(g, "G")
        validateColor(b, "B")
    }

    override val content = listOf(r, g, b)

    override fun valueToString(): String = "(%3d,%3d,%3d)".format(r, g, b)

    override fun toString() = super.toString()

    fun distanceTo(other: Kolor): Double {
        val deltaR = (this.rDouble - other.rDouble)
        val deltaG = (this.gDouble - other.gDouble)
        val deltaB = (this.bDouble - other.bDouble)

        return (deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB)
    }

}
