package rafael.ktgenetic.salesman.fx

import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val defaultArrowHeadSize = 10.0

val bestPaint: Paint = Color.BLACK
val secondPaint: Paint = Color.GREY  // rgb(168, 168, 168, 0.66)

// Code based on https://gist.github.com/kn0412/2086581e98a32c8dfa1f69772f14bca4
class Arrow(startX: Double, startY: Double, endX: Double, endY: Double, arrowFill: Paint = bestPaint) : Path() {

    init {
        strokeProperty().bind(fillProperty())
        fill = arrowFill

        //Line
        elements.add(MoveTo(startX, startY))
        elements.add(LineTo(endX, endY))

        //ArrowHead
        val angle = atan2(endY - startY, endX - startX) - Math.PI / 2.0
        val sin = sin(angle)
        val cos = cos(angle)
        //point1
        val x1 = (-1.0 / 2.0 * cos + sqrt(3.0) / 2 * sin) * defaultArrowHeadSize + endX
        val y1 = (-1.0 / 2.0 * sin - sqrt(3.0) / 2 * cos) * defaultArrowHeadSize + endY
        //point2
        val x2 = (1.0 / 2.0 * cos + sqrt(3.0) / 2 * sin) * defaultArrowHeadSize + endX
        val y2 = (1.0 / 2.0 * sin - sqrt(3.0) / 2 * cos) * defaultArrowHeadSize + endY

        elements.add(LineTo(x1, y1))
        elements.add(LineTo(x2, y2))
        elements.add(LineTo(endX, endY))
    }

}
