package rafael.ktgenetic.pictures_comparsion.rectangles

import kotlin.math.abs

/**
 * Represents a rectangle with a specific color.
 *
 * @property upperCorner The upper corner of the rectangle.
 * @property lesserCorner The lower corner of the rectangle.
 * @property kolor The color of the rectangle.
 */
data class Rectangle(val upperCorner: Position, val lesserCorner: Position, val kolor: Kolor) {

    /**
     * The height of the rectangle, calculated as the absolute difference between the y-coordinates of the upper and lower corners.
     */
    val height = abs(upperCorner.y - lesserCorner.y)

    /**
     * The width of the rectangle, calculated as the absolute difference between the x-coordinates of the upper and lower corners.
     */
    val width = abs(upperCorner.x - lesserCorner.x)

    /**
     * Returns a string representation of the rectangle, including its upper corner, lower corner, and color.
     *
     * @return A string representation of the rectangle.
     */
    override fun toString(): String = "[%s,%s,%s]".format(upperCorner, lesserCorner, kolor)
}