package rafael.ktgenetic.salesman

import rafael.ktgenetic.Chromosome

typealias Segment = Set<Point>

fun toSegment(p1: Point, p2: Point): Segment = setOf(p1, p2)

typealias Vector = Pair<Point, Point>

data class Path(override val content: List<Point>, val pathType: PathType) : Chromosome<Point>() {

    constructor(vararg points: Point, pathType: PathType = PathType.OPEN) : this(points.toList(), pathType)

    val pathPoints: List<Vector> by lazy {
        (0 until content.size - 1).map { Pair(content[it], content[it + 1]) } + pathType.endPath(content)
    }

    val width: Double by lazy {
        pathPoints.map { DistanceRepository.getDistance(it.first, it.second) }.sum()
    }

}