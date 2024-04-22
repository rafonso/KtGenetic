package rafael.ktgenetic.pictures_comparsion.rectangles

/**
 * Validates the coordinate value to be non-negative.
 *
 * @param pos The coordinate value to be validated.
 * @param name The name of the coordinate.
 * @throws IllegalArgumentException if the coordinate value is negative.
 */
private fun validateCoord(pos: Int, name: String) {
    require(pos >= 0) { "$name coordinate must not be lesser than 0" }
}

/**
 * Represents a position in a 2D space.
 *
 * @property x The x-coordinate of the position (should be >= 0).
 * @property y The y-coordinate of the position (should be >= 0).
 */
data class Position(val x: Int, val y: Int) {

    init {
        validateCoord(x, "x")
        validateCoord(y, "y")
    }

    /**
     * Returns a string representation of the position in the format "(x,y)".
     */
    override fun toString(): String = "(%4d,%4d)".format(x, y)

}
