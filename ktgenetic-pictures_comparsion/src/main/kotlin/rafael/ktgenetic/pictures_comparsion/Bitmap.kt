package rafael.ktgenetic.pictures_comparsion

data class Bitmap(val x: Int, val y: Int, val r: Int, val g: Int, val b: Int, private var _distance: Double = -1.0) {

//    val color: Color by lazy { Color.rgb(r, g, b) }

    init {
        fun validateCoord(pos: Int, name: String) {
            require(pos >= 0) { "$name coordinate must not be lesser than 0" }
        }

        fun validateColor(x: Int, name: String) {
            require(x in 0..255) { "$name must be between 0 and 255" }
        }

        validateCoord(x, "X")
        validateCoord(y, "Y")
        validateColor(r, "R")
        validateColor(g, "G")
        validateColor(b, "B")
    }

    var distance: Double
        get() = _distance
        internal set(value) {
            _distance = value
        }

}