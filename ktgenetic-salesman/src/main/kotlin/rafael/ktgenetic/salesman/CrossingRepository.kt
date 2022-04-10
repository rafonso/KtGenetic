package rafael.ktgenetic.salesman

import java.lang.IllegalStateException
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

const val MSG_COLLINEAR_ERROR = "At moment cannot calculate when 2 touching a 1 point are on segment :( %s %s"

object CrossingRepository {

    private enum class Orientation {
        COLINEAR, CLOCKWISE, COUNTERCLOCKWISE
    }

    private val crossings = ConcurrentHashMap<Pair<Vector, Vector>, Boolean>()

    // Based on https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/

    private fun onSegment(p: Point, q: Point, r: Point) =
            (q.x <= max(p.x, r.x)) && (q.x >= min(p.x, r.x)) && (q.y >= min(p.y, r.y)) && (q.y <= max(p.y, r.y))

    private fun orientation(p: Point, q: Point, r: Point): Orientation {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        val value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)

        return when {
            value == 0 -> Orientation.COLINEAR // colinear
            value > 0  -> Orientation.CLOCKWISE
            else       -> Orientation.COUNTERCLOCKWISE
        }
    }

    private fun segmentsTouchingOnlyOnEdge(
        p1: Point,
        q1: Point,
        p2: Point,
        q2: Point,
        collinear: Boolean
    ): Boolean {
        val p1TouchesSegment2 = (p1 == p2) || (p1 == q2)
        val q1TouchesSegment2 = (q1 == p2) || (q1 == q2)

        return if (p1TouchesSegment2 xor q1TouchesSegment2) {
            if (collinear) {
                throw IllegalStateException(MSG_COLLINEAR_ERROR.format(Pair(p1, q1), Pair(p2, q2)))
//                if (p1TouchesSegment2) {
//                    !onSegment(p2, q1, q2) && !onSegment(q2, q1, p2)
//                } else {
//                    !onSegment(p2, p1, q2) && !onSegment(q2, p1, p2)
//                }
            } else true
        } else false
    }

    private fun doIntersect(p1: Point, q1: Point, p2: Point, q2: Point): Boolean {
        // Find the four orientations needed for general and
        // special cases
        val op1q1p2 = orientation(p1, q1, p2)
        val op1q1q2 = orientation(p1, q1, q2)
        val op2q2p1 = orientation(p2, q2, p1)
        val op2q2q1 = orientation(p2, q2, q1)

        return when {
            // General case
            (op1q1p2 != op1q1q2) && (op2q2p1 != op2q2q1)               -> !segmentsTouchingOnlyOnEdge(
                p1,
                q1,
                p2,
                q2,
                false
            )
            // Special Cases
            // p1, q1 and p2 are collinear and p2 lies on segment p1q1
            (op1q1p2 == Orientation.COLINEAR) && onSegment(p1, p2, q1) -> !segmentsTouchingOnlyOnEdge(
                p1,
                q1,
                p2,
                q2,
                true
            )
            // p1, q1 and q2 are collinear and q2 lies on segment p1q1
            (op1q1q2 == Orientation.COLINEAR) && onSegment(p1, q2, q1) -> !segmentsTouchingOnlyOnEdge(
                p1,
                q1,
                p2,
                q2,
                true
            )
            // p2, q2 and p1 are colinear and p1 lies on segment p2q2
            (op2q2p1 == Orientation.COLINEAR) && onSegment(p2, p1, q2) -> !segmentsTouchingOnlyOnEdge(
                p1,
                q1,
                p2,
                q2,
                true
            )
            // p2, q2 and q1 are colinear and q1 lies on segment p2q2
            (op2q2q1 == Orientation.COLINEAR) && onSegment(p2, q1, q2) -> !segmentsTouchingOnlyOnEdge(
                p1,
                q1,
                p2,
                q2,
                true
            )
            // Doesn't fall in any of the above cases
            else                                                       -> false
        }
    }

    private fun calculate(pair: Pair<Vector, Vector>): Boolean {
        val segment1 = pair.first
        val segment2 = pair.second

        return doIntersect(segment1.first, segment1.second, segment2.first, segment2.second)
    }

    fun clear() = crossings.clear()

    fun verifyCrossing(path1: Vector, path2: Vector): Boolean {
        var result = crossings[Pair(path1, path2)]

        if (result == null) {
            result = calculate(Pair(path1, path2))

            crossings[Pair(path1, path2)] = result
            crossings[Pair(Pair(path1.second, path1.first), Pair(path2.second, path2.first))] = result
            crossings[Pair(path2, path1)] = result
            crossings[Pair(Pair(path2.second, path2.first), Pair(path1.second, path1.first))] = result
        }

        return result
    }

}

const val CROSSING_FACTOR = 0.9

enum class CrossingHandler {

    ALLOW {
        override fun calculateCrossFactor(path: Path): Double = 1.0
    },
    PENALIZE {
        override fun calculateCrossFactor(path: Path): Double {
            val totalCrossings = (0 until path.pathPoints.size - 1).sumOf { index1 ->
                val pathPointBase = path.pathPoints[index1]
                (index1 + 1 until path.pathPoints.size).count { index2 ->
                    val pathPoint: Vector = path.pathPoints[index2]

                    CrossingRepository.verifyCrossing(pathPointBase, pathPoint)
                }
            }

            return if (totalCrossings == 0) 1.0 else {
                CROSSING_FACTOR.pow(totalCrossings.toDouble())
            }
        }
    };

    abstract fun calculateCrossFactor(path: Path): Double
}