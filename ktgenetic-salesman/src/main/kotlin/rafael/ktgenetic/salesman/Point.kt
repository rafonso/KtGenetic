package rafael.ktgenetic.salesman

data class Point(val x: Int, val y: Int) {

    override fun toString(): String = "(%3d, %3d)".format(x, y)

}