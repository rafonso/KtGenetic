package rafael.ktgenetic.salesman

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.sqrt

object DistanceRepository {

    private val distances = ConcurrentHashMap<Segment, Double>()

    private fun calculate(points: Segment): Double {
        val itPoints = points.iterator()
        val p1 = itPoints.next()
        val p2 = itPoints.next()

        val deltaX = (p1.x - p2.x).toDouble()
        val deltaY = (p1.y - p2.y).toDouble()

        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }

    fun clear() = distances.clear()

    fun getDistance(p1: Point, p2: Point): Double = distances.computeIfAbsent(toSegment(p1, p2), this::calculate)

}