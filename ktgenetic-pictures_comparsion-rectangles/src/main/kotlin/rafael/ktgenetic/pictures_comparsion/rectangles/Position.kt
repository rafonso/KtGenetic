package rafael.ktgenetic.pictures_comparsion.rectangles

import kotlin.math.sqrt

private fun validateCoord(pos: Int, name: String) {
    require(pos >= 0) { "$name coordinate must not be lesser than 0" }
}

/**
 * Pixel position
 *
 * @param x x coordinate (should be >= 0)
 * @param y y coordinate (should be >= 0)
 */
data class Position(val x: Int, val y: Int) {

    init {
        validateCoord(x, "x")
        validateCoord(y, "y")
    }

    override fun toString(): String = "(%4d,%4d)".format(x, y)

    /**
     * Calculate the distance between 2 [Position]s, using pythagoras equation.
     *
     * @param otherPosition the Other position
     * @return the distance to the other position
     */
    fun distanceTo(otherPosition: Position): Double {
        val deltaX = (this.x - otherPosition.x).toDouble()
        val deltaY = (this.y - otherPosition.y).toDouble()

        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }

}