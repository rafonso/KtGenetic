package rafael.ktgenetic.salesman

enum class Direction(val description: String,
                     val vertical: Int,
                     val horizontal: Int) {

    T("Top", 1, 0),
    B("Bottom", -1, 0),
    R("Right", 0, 1),
    L("Left", 0, -1)

}