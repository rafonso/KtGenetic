package rafael.ktgenetic.salesman

import rafael.ktgenetic.createCutPositions
import rafael.ktgenetic.randomSwap

sealed class PathHandler {

    abstract fun createNewPath(): Path

    abstract fun getCutPositions(): Pair<Int, Int>

    abstract fun executeMutation(sequence: List<Point>): List<Point>

}

class OpenPathHandler(private val points: List<Point>) : PathHandler() {

    override fun createNewPath(): Path = Path(points.shuffled(), PathType.OPEN)

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(points.size)

    override fun executeMutation(sequence: List<Point>): List<Point> = sequence.randomSwap()

}

class OpenPathHandlerWithStart(points: List<Point>, startPoint: Point) : PathHandler() {

    private val otherPoints = points - startPoint

    private val starPoint = listOf(startPoint)

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled(), PathType.OPEN_START)

    override fun getCutPositions(): Pair<Int, Int> =
            createCutPositions(otherPoints.size).let { p -> Pair(p.first + 1, p.second + 1) }

    override fun executeMutation(sequence: List<Point>): List<Point> =
            starPoint + sequence.takeLast(sequence.size - 1).randomSwap()

}

class OpenPathHandlerWithEnd(points: List<Point>, private val endPoint: Point) : PathHandler() {

    private val otherPoints = points - endPoint

    override fun createNewPath(): Path = Path(otherPoints.shuffled() + endPoint, PathType.OPEN_END)

    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(otherPoints.size - 1)

    override fun executeMutation(sequence: List<Point>): List<Point> =
            sequence.take(sequence.size - 1).randomSwap() + endPoint

}

class OpenPathHandlerWithStartAndEnd(points: List<Point>, startPoint: Point, private val endPoint: Point) :
    PathHandler() {

    private val otherPoints = points - startPoint - endPoint

    private val starPoint = listOf(startPoint)

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled() + endPoint, PathType.OPEN_START_END)

    override fun getCutPositions(): Pair<Int, Int> =
            createCutPositions(otherPoints.size).let { p -> Pair(p.first + 1, p.second + 1) }

    override fun executeMutation(sequence: List<Point>): List<Point> =
            starPoint + sequence.subList(1, sequence.size - 1).randomSwap() + endPoint
}

class ClosedPathHandler(points: List<Point>) : PathHandler() {

    private val starPoint = listOf(points[0])

    private val otherPoints = points.subList(1, points.size)

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled() + starPoint, PathType.CLOSED)

    // Isso está certo?
    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(otherPoints.size)

    override fun executeMutation(sequence: List<Point>): List<Point> =
            starPoint + sequence.subList(1, sequence.size - 1).randomSwap() + starPoint

}

class ClosedPathHandlerWithStart(points: List<Point>, startPoint: Point) : PathHandler() {

    private val otherPoints = points - startPoint

    private val starPoint = listOf(startPoint)

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled() + starPoint, PathType.CLOSED_START)

    // Isso está certo?
    override fun getCutPositions(): Pair<Int, Int> = createCutPositions(otherPoints.size)

    override fun executeMutation(sequence: List<Point>): List<Point> =
            starPoint + sequence.subList(1, sequence.size - 1).randomSwap() + starPoint

}
