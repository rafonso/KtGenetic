package rafael.ktgenetic.camouflage.fx

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.Background
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.scene.text.Text
import rafael.ktgenetic.camouflage.Kolor
import rafael.ktgenetic.camouflage.WHITE
import tornadofx.*

private const val SIZE_FACTOR = 1.1
private const val FONT_FACTOR = 0.15

class CircleNode(radius: Double, circleId: Int) : StackPane() {

    private val kolorProperty = SimpleObjectProperty(WHITE)
    var kolor: Kolor by kolorProperty

    private val circle = Circle(radius).also { c ->
        c.fill = Color.TRANSPARENT
        c.stroke = Color.BLACK
    }

    private val txtCircle = Text().also {
        it.font = Font.font(radius * FONT_FACTOR)
    }

    private val showData: (String) -> Unit =
        if (radius >= 50.0) { string ->
            txtCircle.text = string
            txtCircle.fill = (circle.fill as Color).invert().brighter()
        }
        else { string -> circle.tooltip(string) }

    init {
        super.setBackground(Background.EMPTY)
        super.getChildren().add(circle)
        super.getChildren().add(txtCircle)
        super.setId("circle-%04d".format(circleId))

        val size = SIZE_FACTOR * 2 * radius
        super.setWidth(size)
        super.setHeight(size)

        kolorProperty.onChange { k -> kolorChanged(k!!) }
        kolorChanged(this.kolor)
    }

    private fun kolorChanged(kolor: Kolor) {
        circle.fill = kolor.color
        circle.stroke = kolor.color.invert()
        circle.strokeWidth = 1 + kolor.fitness
        showData(
            """
            |${formatRgbColor(circle.fill!! as Color)}
            |${formatHsbColor(circle.fill!! as Color)}
            |${formatHexColor(circle.fill!! as Color)}
            |Fitness: ${"%.5f".format(kolor.fitness)}
        """.trimMargin()
        )
    }

}
