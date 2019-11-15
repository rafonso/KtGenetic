package rafael.ktgenetic.salesman

import kotlin.math.sqrt

object DistanceRepository {

    private val distances = mutableMapOf<Set<Point>, Double>()

    private fun calculate(points: Set<Point>): Double {
        val itPoints = points.iterator()
        val p1 = itPoints.next()
        val p2 = itPoints.next()

        val deltaX = (p1.x - p2.x).toDouble()
        val deltaY = (p1.y - p2.y).toDouble()

        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }

    fun clear() = distances.clear()

    fun getDistance(p1: Point, p2: Point): Double = distances.computeIfAbsent(setOf(p1, p2)) {
        calculate(it)
    }


}