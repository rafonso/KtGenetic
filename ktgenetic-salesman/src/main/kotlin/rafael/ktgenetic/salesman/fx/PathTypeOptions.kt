package rafael.ktgenetic.salesman.fx

import javafx.scene.control.ContextMenu
import javafx.util.StringConverter
import rafael.ktgenetic.salesman.PathType
import tornadofx.action
import tornadofx.item
import tornadofx.separator
import java.math.BigInteger

private fun factorial(n: Int) = (1..n).fold(BigInteger.ONE) { prod, i -> prod * i.toBigInteger() }

private fun cleanCircle(circlePoint: CirclePoint) {
    circlePoint.typeProperty.value = CirclePointType.COMMON
}

private fun addCommon(circlePoint: CirclePoint, menu: ContextMenu) {
    menu.item("Revert to Common Point") {
        action {
            cleanCircle(circlePoint)
        }
    }
}

private fun addSeparator(menu: ContextMenu) {
    with(menu) {
        separator()
    }
}

private fun addStart(circlePoint: CirclePoint, otherCircles: () -> List<CirclePoint>, menu: ContextMenu) {
    menu.item("Set as Start Point") {
        action {
            otherCircles()
                .filter(::selectStart)
                .forEach(::cleanCircle)
            circlePoint.typeProperty.value = CirclePointType.START
        }
    }
}

fun selectStart(circlePoint: CirclePoint): Boolean = circlePoint.typeProperty.value == CirclePointType.START

private fun validateStartPresence(circles: List<CirclePoint>) {
    check(circles.any(::selectStart)) {
        "At least One Point must be set as Start"
    }
}

private fun addEnd(circlePoint: CirclePoint, otherCircles: () -> List<CirclePoint>, menu: ContextMenu) {
    menu.item("Set as End Point") {
        action {
            otherCircles()
                .filter(::selectEnd)
                .forEach(::cleanCircle)
            circlePoint.typeProperty.value = CirclePointType.END
        }
    }
}

private fun validateEndPresence(circles: List<CirclePoint>) {
    check(circles.any(::selectEnd)) {
        "At least One Point must be set as End"
    }
}

fun selectEnd(circlePoint: CirclePoint): Boolean = circlePoint.typeProperty.value == CirclePointType.END

private fun selectStartEnd(circlePoint: CirclePoint): Boolean = selectStart(circlePoint) || selectEnd(circlePoint)

enum class PathTypeOptions(
    val type: PathType,
    val title: String,
    val maxPossiblePaths: (Int) -> BigInteger,
    val validateCircles: (List<CirclePoint>) -> Unit,
    private val typeSelector: (CirclePoint) -> Boolean,
    val handleTypeChoice: (CirclePoint, () -> List<CirclePoint>, ContextMenu) -> Unit = { _, _, _ -> }
) {
    // TODO: verificar melhor o cálculo de caminhos possíveis
    OPEN(PathType.OPEN, "Open", { n -> factorial(n - 1) }, { }, ::selectStartEnd),
    OPEN_START(PathType.OPEN_START, "Open With Start",
        { n -> factorial(n - 1) + BigInteger.ONE },
        ::validateStartPresence,
        ::selectEnd,
        { circlePoint, otherCircles, contextMenu ->
            addSeparator(contextMenu)
            addStart(circlePoint, otherCircles, contextMenu)
            addCommon(circlePoint, contextMenu)
        }),
    OPEN_END(PathType.OPEN_END, "Open With End",
        { n -> factorial(n - 1) + BigInteger.ONE },
        ::validateEndPresence,
        ::selectStart,
        { circlePoint, otherCircles, contextMenu ->
            addSeparator(contextMenu)
            addEnd(circlePoint, otherCircles, contextMenu)
            addCommon(circlePoint, contextMenu)
        }),
    OPEN_START_END(
        PathType.OPEN_START_END,
        "Open With Start and End",
        { n -> factorial(n - 1) + BigInteger.ONE + BigInteger.ONE },
        { circles ->
            validateStartPresence(circles)
            validateEndPresence(circles)
        },
        { false },
        { circlePoint, otherCircles, contextMenu ->
            addSeparator(contextMenu)
            addStart(circlePoint, otherCircles, contextMenu)
            addEnd(circlePoint, otherCircles, contextMenu)
            addCommon(circlePoint, contextMenu)
        }),
    CLOSED(PathType.CLOSED, "Closed", { n -> factorial(n) }, { }, ::selectStartEnd),
    CLOSED_START(PathType.CLOSED_START, "Closed With Start",
        { n -> factorial(n) },
        ::validateStartPresence,
        ::selectEnd,
        { circlePoint, otherCircles, contextMenu ->
            addSeparator(contextMenu)
            addStart(circlePoint, otherCircles, contextMenu)
            addCommon(circlePoint, contextMenu)
        });

    fun handleSelected(circles: List<CirclePoint>) {
        circles.filter(typeSelector).forEach(::cleanCircle)
    }

}

object PathTypeOptionsStingConverter : StringConverter<PathTypeOptions>() {

    override fun toString(option: PathTypeOptions?): String = option!!.title

    override fun fromString(string: String?): PathTypeOptions =
            PathTypeOptions.values().first { it.title == string }

}