package rafael.ktgenetic.pictures_comparsion

/**
 * Indicates a [Kolor] in a specific [Position].
 *
 * @param position Color Position
 * @param kolor Color Value
 */
data class Bitmap(val position: Position, val kolor: Kolor, private var _distance: Double = -1.0) {

    constructor(x: Int, y: Int, r: Int, g: Int, b: Int) : this(Position(x, y), Kolor(r, g, b))

    var distance: Double
        get() = _distance
        internal set(value) {
            _distance = value
        }

    override fun toString(): String = "[%s=%s]".format(position, kolor)

}