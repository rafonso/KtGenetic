package rafael.ktgenetic.salesman

sealed class PathHandler {

    abstract fun createNewPath(): Path

}

class OpenPathHandler(private val points: List<Point>) : PathHandler() {

    override fun createNewPath(): Path = Path(points.shuffled(), PathType.OPEN)

}

// TODO: Use starPoint as Constructor Parameter
class OpenPathHandlerWithStart(points: List<Point> /*, private val starPoint?: Point */) : PathHandler() {

    private val starPoint = listOf(points.first())

    private val otherPoints = points - starPoint

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled(), PathType.OPEN)

}

// TODO: Use endPoint as Constructor Parameter
class OpenPathHandlerWithEnd(points: List<Point> /*, private val endPoint?: Point */) : PathHandler() {

    private val endPoint = points.last()

    private val otherPoints = points - endPoint

    override fun createNewPath(): Path = Path(otherPoints.shuffled() + endPoint, PathType.OPEN)

}

// TODO: Use starPoint and endPoint as Constructor Parameters
class OpenPathHandlerWithStartAndEnd(points: List<Point> /*, private val starPoint?: Point, private val endPoint?: Point */) :
    PathHandler() {

    private val starPoint = listOf(points.first())

    private val endPoint = points.last()

    private val otherPoints = points - starPoint - endPoint

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled() + endPoint, PathType.OPEN)

}


class ClosedPathHandler(points: List<Point>) : PathHandler() {

    private val starPoint = listOf(points[0])

    private val otherPoints = points.subList(1, points.size)

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled(), PathType.CLOSED)

}

// TODO: Use starPoint as Constructor Parameter
class ClosedPathHandlerWithStart(points: List<Point> /*, private val endPoint?: Point */) : PathHandler() {

    private val starPoint = listOf(points[0])

    private val otherPoints = points - starPoint

    override fun createNewPath(): Path = Path(starPoint + otherPoints.shuffled(), PathType.CLOSED)

}
