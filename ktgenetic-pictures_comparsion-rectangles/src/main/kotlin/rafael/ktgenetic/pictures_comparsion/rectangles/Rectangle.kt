package rafael.ktgenetic.pictures_comparsion.rectangles

import kotlin.math.abs

data class Rectangle(val upperCorner: Position, val lesserCorner: Position, val kolor: Kolor) {

    val height = abs(upperCorner.y - lesserCorner.y)

    val width = abs(upperCorner.x - lesserCorner.x)

    override fun toString(): String = "[%s,%s,%s]".format(upperCorner, lesserCorner, kolor)

}