package rafael.ktgenetic.pictures_comparsion.rectangles

import kotlin.math.sqrt

fun validateColor(x: Int, name: String) {
    require(x in 0..255) { "$name must be between 0 and 255" }
}

//const val MAX_COLOR_DISTANCE = 0xFFFFFF

const val MAX_COLOR_VALUE = 255

const val MAX_COLOR_VALUE_D = MAX_COLOR_VALUE.toDouble()

//const val MAX_COLOR_DISTANCE: Int = 3 * 255 * 255

/**
 * Represents the color in a pixel. Named as 'Kolor" to avoid confusing with JavaFX `Color`.
 *
 * @param r Red value
 * @param g Green value
 * @param b Blue value
 */
data class Kolor(val r: Int, val g: Int, val b: Int) {

//    val longValue: Int by lazy {
//        r.shl(16) + g.shl(8) + b
//    }

    init {
        validateColor(r, "R")
        validateColor(g, "G")
        validateColor(b, "B")
    }

    override fun toString(): String = "(%3d,%3d,%3d)".format(r, g, b)

    fun distanceTo(other: Kolor): Double {
        val deltaR = (this.r - other.r).toDouble()
        val deltaG = (this.g - other.g).toDouble()
        val deltaB = (this.b - other.b).toDouble()

        return sqrt((deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB))
    }

}