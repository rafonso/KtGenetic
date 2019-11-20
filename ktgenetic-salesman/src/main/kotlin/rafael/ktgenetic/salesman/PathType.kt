package rafael.ktgenetic.salesman

enum class PathType(
    val delta: Int,
    @Deprecated("Remove") val endPath: (List<Point>) -> List<Vector>,
    val createNewPathHandler: (points: List<Point>, startPoint: Point?, endPoint: Point?) -> PathHandler
) {

    OPEN(1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> OpenPathHandler(points) }),
    OPEN_START(1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> OpenPathHandlerWithStart(points) }),
    OPEN_END(1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> OpenPathHandlerWithEnd(points) }),
    OPEN_START_END(1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> OpenPathHandlerWithStartAndEnd(points) }),
    CLOSED(0, { paths -> listOf(Pair(paths.last(), paths.first())) },
        { points: List<Point>, _: Point?, _: Point? -> ClosedPathHandler(points) }),
    CLOSED_START(0, { paths -> listOf(Pair(paths.last(), paths.first())) },
        { points: List<Point>, _: Point?, _: Point? -> ClosedPathHandlerWithStart(points) })

}