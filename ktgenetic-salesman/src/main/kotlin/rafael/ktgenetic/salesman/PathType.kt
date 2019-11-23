package rafael.ktgenetic.salesman

enum class PathType(
    val delta: Int,
    val createNewPathHandler: (points: List<Point>, startPoint: Point?, endPoint: Point?) -> PathHandler
) {

    OPEN(1, { points, _, _ -> OpenPathHandler(points) }),
    OPEN_START(1, { points, startPoint, _ -> OpenPathHandlerWithStart(points, startPoint!!) }),
    OPEN_END(1, { points, _, endPoint -> OpenPathHandlerWithEnd(points, endPoint!!) }),
    OPEN_START_END(
        1,
        { points, startPoint, endPoint -> OpenPathHandlerWithStartAndEnd(points, startPoint!!, endPoint!!) }),
    CLOSED(0, { points, _, _ -> ClosedPathHandler(points) }),
    CLOSED_START(0, { points, startPoint, _ -> ClosedPathHandlerWithStart(points, startPoint!!) })

}