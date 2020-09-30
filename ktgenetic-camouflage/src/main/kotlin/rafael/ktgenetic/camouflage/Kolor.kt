package rafael.ktgenetic.camouflage

import javafx.scene.paint.Color
import rafael.ktgenetic.Chromosome
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

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

    private val rDouble = r.toDouble() / MAX_COLOR_VALUE
    private val gDouble = g.toDouble() / MAX_COLOR_VALUE
    private val bDouble = b.toDouble() / MAX_COLOR_VALUE

    val color = Color.color(rDouble, gDouble, bDouble)

    init {
        validateColor(r, "R")
        validateColor(g, "G")
        validateColor(b, "B")
    }

    override val content = listOf(r, g, b)

    override fun valueToString(): String = "(%3d,%3d,%3d)".format(r, g, b)

    override fun toString() = super.toString()

}

val WHITE = Kolor(MAX_COLOR_VALUE, MAX_COLOR_VALUE, MAX_COLOR_VALUE)
