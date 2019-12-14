package rafael.ktgenetic.salesman

data class Point(val x: Int, val y: Int) {

    companion object {

        fun comparePoints(p1: Point, p2: Point): Int = if (p1.x != p2.x) p1.x.compareTo(p2.x) else p1.y.compareTo(p2.y)

    }

    override fun toString(): String = "(%3d, %3d)".format(x, y)

}
