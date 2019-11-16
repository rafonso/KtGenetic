package rafael.ktgenetic.salesman

enum class PathType(val description: String, val delta: Int, val endPath: (List<Point>) -> List<Segment>) {

    OPEN("Open", 1, { emptyList() }),
    CLOSED("Closed", 0, { paths -> listOf(Pair(paths.last(), paths.first())) })
}