package rafael.ktgenetic.salesman

import rafael.ktgenetic.Chromosome

data class Path(override val content: List<Point>) : Chromosome<Point>() {

    constructor(vararg points: Point) : this(points.toList())

    val pathPoints: List<Pair<Point, Point>> by lazy {
        (0 until content.size - 1).map { Pair(content[it], content[it + 1]) }
    }

    val width: Double by lazy {
        pathPoints.map { DistanceRepository.getDistance(it.first, it.second) }.sum()
    }

}