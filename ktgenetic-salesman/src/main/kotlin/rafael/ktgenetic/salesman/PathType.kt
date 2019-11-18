package rafael.ktgenetic.salesman

enum class PathType(
    val description: String,
    val delta: Int,
    @Deprecated("Remove") val endPath: (List<Point>) -> List<Vector>,
    val createNewPathHandler: (points: List<Point>, startPoint: Point?, endPoint: Point?) -> PathHandler
) {

    OPEN("Open", 1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> OpenPathHandler(points) }),
    OPEN_START("Open With Start", 1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> OpenPathHandlerWithStart(points) }),
    OPEN_END("Open With End", 1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> OpenPathHandlerWithEnd(points) }),
    OPEN_START_END("Open With Star and End", 1, { emptyList() },
        { points: List<Point>, _: Point?, _: Point? -> rafael.ktgenetic.salesman.OpenPathHandlerWithStartAndEnd(points) }),
    CLOSED("Closed", 0, { paths -> listOf(Pair(paths.last(), paths.first())) },
        { points: List<Point>, _: Point?, _: Point? -> ClosedPathHandler(points) }),
    CLOSED_START("Closed With Start", 0, { paths -> listOf(Pair(paths.last(), paths.first())) },
        { points: List<Point>, _: Point?, _: Point? -> ClosedPathHandlerWithStart(points) })

}