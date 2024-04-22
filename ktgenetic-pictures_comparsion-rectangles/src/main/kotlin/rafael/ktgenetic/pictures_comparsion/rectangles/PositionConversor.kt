package rafael.ktgenetic.pictures_comparsion.rectangles

/**
 * Interface for converting positions between real picture and resized picture.
 */
interface PositionConversor {

    /**
     * Converts a position from the resized picture to the real picture.
     *
     * @param pos The position in the resized picture.
     * @return The corresponding position in the real picture.
     */
    fun toRealPicturePosition(pos: Position): Position

    /**
     * Converts a position from the real picture to the resized picture.
     *
     * @param pos The position in the real picture.
     * @return The corresponding position in the resized picture.
     */
    fun toResizedPicurePosition(pos: Position): Position
}

/**
 * A simple position converter that does not change the position.
 */
class SimplePositionConversor : PositionConversor {
    override fun toRealPicturePosition(pos: Position): Position = pos

    override fun toResizedPicurePosition(pos: Position): Position = pos
}

/**
 * A position converter that scales positions according to given factors.
 *
 * @property xFactor The scaling factor for the x-coordinate.
 * @property yFactor The scaling factor for the y-coordinate.
 */
class ProportionalPositionConversor(private val xFactor: Double, private val yFactor: Double) : PositionConversor {

    /**
     * Converts a position from the resized picture to the real picture using the scaling factors.
     *
     * @param pos The position in the resized picture.
     * @return The corresponding position in the real picture.
     */
    override fun toRealPicturePosition(pos: Position): Position =
            Position((pos.x * xFactor).toInt(), (pos.y * yFactor).toInt())

    /**
     * Converts a position from the real picture to the resized picture using the scaling factors.
     *
     * @param pos The position in the real picture.
     * @return The corresponding position in the resized picture.
     */
    override fun toResizedPicurePosition(pos: Position): Position =
            Position((pos.x / xFactor).toInt(), (pos.y / yFactor).toInt())
}