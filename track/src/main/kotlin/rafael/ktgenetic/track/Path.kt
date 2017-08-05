package rafael.ktgenetic.track

import rafael.ktgenetic.Chromosome

data class Point(val hPos: Int = 0, val vPos: Int = 0) {

    operator fun plus(direction: Direction): Point =
            Point(this.hPos + direction.horizontal, this.vPos + direction.vertical)

}

data class Path(override val content: List<Direction>) : Chromosome<Direction>() {

    private var _status: PathStatus = PathStatus.WAITING

    private lateinit var _track: List<Point>

    val status: PathStatus
        get() = _status

    val track: List<Point>
        get() = _track

    fun calculatePath(hLimit: Int, vLimit: Int) {
        assert(status == PathStatus.WAITING, { -> "Path just Calculated" })

        fun isStuck(currentPoint: Point): Boolean {
            return currentPoint.hPos < 0 || currentPoint.vPos < 0 || currentPoint.hPos > hLimit || currentPoint.vPos > vLimit
        }

        fun arrivedToTarget(currentPoint: Point): Boolean = (currentPoint.hPos == hLimit) && (currentPoint.vPos == vLimit)

        tailrec fun calculate(currentTrack: List<Point>): Pair<List<Point>, PathStatus> {
            val nextPoint = currentTrack.last() + content[currentTrack.size - 1]
            val nextTrack = currentTrack + nextPoint

            return if (isStuck(nextPoint)) {
                Pair(currentTrack, PathStatus.STUCK)
            } else if (arrivedToTarget(nextPoint)) {
                Pair(nextTrack, PathStatus.TARGETED)
            } else if (nextTrack.size > content.size) {
                Pair(nextTrack, PathStatus.SUSPENDED)
            } else {
                calculate(nextTrack)
            }
        }

        val (t, s) = calculate(listOf(Point(0, 0)))
        _track = t
        _status = s
    }

}