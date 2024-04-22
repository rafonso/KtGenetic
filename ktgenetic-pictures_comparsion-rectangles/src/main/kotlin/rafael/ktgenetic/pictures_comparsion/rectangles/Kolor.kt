package rafael.ktgenetic.pictures_comparsion.rectangles

/**
 * Validates the color value to be within the range 0 to 255.
 *
 * @param x The color value to be validated.
 * @param name The name of the color.
 * @throws IllegalArgumentException if the color value is not within the range 0 to 255.
 */
fun validateColor(x: Int, name: String) {
    require(x in 0..255) { "$name must be between 0 and 255" }
}


// Maximum value a color can have.
const val MAX_COLOR_VALUE = 255

// Square of the maximum color value.
const val MAX_COLOR_VALUE_D = (MAX_COLOR_VALUE * MAX_COLOR_VALUE)

/**
 * Represents the color in a pixel. Named as 'Kolor' to avoid confusing with JavaFX `Color`.
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

    /**
     * Returns a string representation of the color in the format "(R,G,B)".
     */
    override fun toString(): String = "(%3d,%3d,%3d)".format(r, g, b)

    /**
     * Returns the "distance" between this [Kolor] and another one. This distance is calculate as they were 2 points
     * in a 3D space, using [Kolor.r], [Kolor.g] and [Kolor.b] as coordinate. Actually, it is returned the quadratic
     * distance because there is no necessity to extact the square root.
     *
     * @param other The other kolor
     * @return the distance
     */
    internal fun distanceTo(other: Kolor): Int {
        val deltaR = (this.r - other.r)
        val deltaG = (this.g - other.g)
        val deltaB = (this.b - other.b)

        return (deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB)
    }

}
