package rafael.ktgenetic.track

import rafael.ktgenetic.Chromosome

data class Point(val hPos: Int = 0, val vPos: Int = 0) {

    operator fun plus(direction: Direction): Point =
            Point(this.hPos + direction.horizontal, this.vPos + direction.vertical)

    fun distance(p: Point): Double {
        val deltaW = this.hPos - p.hPos
        val deltaH = this.vPos - p.vPos

        return Math.sqrt((deltaW * deltaW + deltaH * deltaH).toDouble())
    }

    override fun toString(): String = "(%4d, %4d)".format(hPos, vPos)

}

data class Path(override val content: List<Direction>) : Chromosome<Direction>() {

    private var _status: PathStatus = PathStatus.WAITING

    private lateinit var _track: List<Point>

    val status: PathStatus
        get() = _status

    val track: List<Point>
        get() = _track

    fun calculatePath(hLimit: Int, vLimit: Int) {
        assert(status==PathStatus.WAITING, { -> "Path just Calculated" })

        fun isStuck(currentPoint: Point): Boolean {
            return currentPoint.hPos < 0 || currentPoint.vPos < 0 || currentPoint.hPos > hLimit || currentPoint.vPos > vLimit
        }

        fun arrivedToTarget(currentPoint: Point): Boolean = (currentPoint.hPos==hLimit) && (currentPoint.vPos==vLimit)

        tailrec fun calculate(track: MutableList<Point>): Pair<List<Point>, PathStatus> {
            val nextPoint = track.last() + content[track.size - 1]

            if (isStuck(nextPoint)) {
                return Pair(track, PathStatus.STUCK)
            }

            track += nextPoint
            return if (arrivedToTarget(nextPoint)) {
                Pair(track, PathStatus.TARGETED)
            } else if (track.size > content.size) {
                Pair(track, PathStatus.SUSPENDED)
            } else {
                calculate(track)
            }
        }

        val (t, s) = calculate(mutableListOf(Point(0, 0)))
        _track = t
        _status = s
    }

    override fun valueToString(): String =
            if (status==PathStatus.WAITING) {
                content.joinToString(separator = "", prefix = "[", postfix = "]")
            } else {
                "$status[${track.size} - ${track.joinToString(separator = "")}]"
            }

    override fun toString(): String {
        return super.toString()
    }

}