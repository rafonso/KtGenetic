package rafael.ktgenetic.pictures_comparsion

import kotlin.math.sqrt

fun validateColor(x: Int, name: String) {
    require(x in 0..255) { "$name must be between 0 and 255" }
}

/**
 * Represents the color in a pixel. Named as 'Kolor" to avoid confusing with JavaFX `Color`.
 *
 * @param r Red value
 * @param g Green value
 * @param b Blue value
 */
data class Kolor(val r: Int, val g: Int, val b: Int) {

    init {
        validateColor(r, "R")
        validateColor(g, "G")
        validateColor(b, "B")
    }

    override fun toString(): String = "(%3d,%3d,%3d)".format(r, g, b)

    /**
     * Calculate the distance between 2 [Kolor]s, using pythagoras equation.
     *
     * @param other the Other Color
     * @return the distance to the other color
     */
    fun distanceTo(other: Kolor): Double {
        val deltaR = (this.r - other.r)
        val deltaG = (this.g - other.g)
        val deltaB = (this.b - other.b)

        return sqrt(((deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB)).toDouble())
    }

}