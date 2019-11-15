package rafael.ktgenetic.salesman

import rafael.ktgenetic.Chromosome

data class Path(override val content: List<Point>) : Chromosome<Point>() {

    constructor(vararg points: Point) : this(points.toList())

    val width: Double by lazy {
        (0 until content.size - 1).map { DistanceRepository.getDistance(content[it], content[it + 1]) }.sum()
    }

}