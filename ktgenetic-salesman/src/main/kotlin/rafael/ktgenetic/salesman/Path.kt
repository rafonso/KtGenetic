package rafael.ktgenetic.salesman

import rafael.ktgenetic.core.Chromosome

typealias Segment = Set<Point>

fun toSegment(p1: Point, p2: Point): Segment = setOf(p1, p2)

fun toSegment(x1: Int, y1: Int, x2: Int, y2: Int) = Pair(Point(x1, y1), Point(x2, y2))

typealias Vector = Pair<Point, Point>

data class Path(override val content: List<Point>, val pathType: PathType) : Chromosome<Point>() {

    constructor(vararg points: Point, pathType: PathType = PathType.OPEN) : this(points.toList(), pathType)

    val pathPoints: List<Vector> by lazy {
        (0 until content.size - 1).map { Pair(content[it], content[it + 1]) }
    }

    val width: Double by lazy {
        pathPoints.sumOf { DistanceRepository.getDistance(it.first, it.second) }
    }

    override fun compareTo(other: Chromosome<Point>): Int {
        var diff = super.compareTo(other)
        if (diff == 0) {
            diff = if (this.content == other.content)
                0
            else
                this.content.indices
                    .map { Point.comparePoints(this.content[it], other.content[it]) }
                    .first { it != 0 }
        }
        return diff
    }

}
