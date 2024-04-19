package rafael.ktgenetic.camouflage

import javafx.scene.paint.Color
import rafael.ktgenetic.core.Chromosome

/**
 * Validates the color value.
 *
 * @param x The color value to validate.
 * @param name The name of the color.
 * @throws IllegalArgumentException if the color value is not between 0 and 255.
 */
fun validateColor(x: Int, name: String) {
    require(x in 0..MAX_COLOR_VALUE) { "$name must be between 0 and 255" }
}

// Maximum value a color component can have.
const val MAX_COLOR_VALUE = 255

/**
 * Extension function to convert a JavaFX Color to a Kolor.
 *
 * @return The Kolor representation of the JavaFX Color.
 */
fun Color.toKolor() = Kolor(
    (this.red * MAX_COLOR_VALUE).toInt(),
    (this.green * MAX_COLOR_VALUE).toInt(),
    (this.blue * MAX_COLOR_VALUE).toInt()
)

/**
 * Represents the color in a pixel. Named as 'Kolor' to avoid confusing with JavaFX `Color`.
 *
 * @property r Red value
 * @property g Green value
 * @property b Blue value
 */
data class Kolor(val r: Int, val g: Int, val b: Int) : Chromosome<Int>() {

    // The red value as a double.
    private val rDouble = r.toDouble() / MAX_COLOR_VALUE
    // The green value as a double.
    private val gDouble = g.toDouble() / MAX_COLOR_VALUE
    // The blue value as a double.
    private val bDouble = b.toDouble() / MAX_COLOR_VALUE

    // The JavaFX Color representation of the Kolor.
    val color = Color.color(rDouble, gDouble, bDouble)!!

    init {
        validateColor(r, "R")
        validateColor(g, "G")
        validateColor(b, "B")
    }

    // The list of color values.
    override val content = listOf(r, g, b)

    /**
     * Converts the color values to a string.
     *
     * @return The string representation of the color values.
     */
    override fun valueToString(): String = "(%3d,%3d,%3d)".format(r, g, b)

    // The string representation of the Kolor.
    override fun toString() = super.toString()

}

// The Kolor representation of white.
val WHITE = Kolor(MAX_COLOR_VALUE, MAX_COLOR_VALUE, MAX_COLOR_VALUE)