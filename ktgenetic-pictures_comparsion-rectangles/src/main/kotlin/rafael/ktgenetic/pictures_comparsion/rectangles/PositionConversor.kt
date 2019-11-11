package rafael.ktgenetic.pictures_comparsion.rectangles

interface PositionConversor {

    fun toRealPicturePosition(pos: Position): Position

    fun toRealPicturePosition(x: Int, y: Int): Position = toRealPicturePosition(Position(x, y))

    fun toResizedPicurePosition(pos: Position): Position

    fun toResizedPicurePosition(x: Int, y: Int): Position = toResizedPicurePosition(Position(x, y))

}

class SimplePositionConversor : PositionConversor {
    override fun toRealPicturePosition(pos: Position): Position = pos

    override fun toResizedPicurePosition(pos: Position): Position = pos

}

class ProportionalPositionConversor(private val xFactor: Double, private val yFactor: Double) : PositionConversor {

    override fun toRealPicturePosition(pos: Position): Position =
            Position((pos.x * xFactor).toInt(), (pos.y * yFactor).toInt())

    override fun toResizedPicurePosition(pos: Position): Position =
            Position((pos.x / xFactor).toInt(), (pos.y / yFactor).toInt())
}