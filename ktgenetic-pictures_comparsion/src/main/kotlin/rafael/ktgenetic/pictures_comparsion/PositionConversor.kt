package rafael.ktgenetic.pictures_comparsion

interface PositionConversor {

    fun toRealPicturePosition(pos: Position): Position

    fun toRealPicturePosition(x: Int, y: Int): Position

}

class SimplePositionConversor : PositionConversor {

    override fun toRealPicturePosition(pos: Position): Position = pos

    override fun toRealPicturePosition(x: Int, y: Int): Position = Position(x, y)

}

class ProportionalPositionConversor(private val xFactor: Double, private val yFactor: Double) : PositionConversor {

    override fun toRealPicturePosition(pos: Position): Position =
        Position((pos.x * xFactor).toInt(), (pos.y * yFactor).toInt())

    override fun toRealPicturePosition(x: Int, y: Int): Position =
        Position((x * xFactor).toInt(), (y * yFactor).toInt())

}