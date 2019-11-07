package rafael.ktgenetic.pictures_comparsion

interface PositionConversor {

    fun toRealPicturePosition(pos: Position): Position

}

class SimplePositionConversor: PositionConversor {
    override fun toRealPicturePosition(pos: Position): Position = pos
}