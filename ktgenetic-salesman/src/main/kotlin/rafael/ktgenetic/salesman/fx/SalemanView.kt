package rafael.ktgenetic.salesman.fx

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.salesman.Path
import rafael.ktgenetic.salesman.Point
import rafael.ktgenetic.salesman.SalesmanEnvironment
import tornadofx.*


class SalemanViewApp : App(SalemanView::class)

class SalemanView : GeneticView<Point, Path>("Salesman", GeneticProcessorChoice.ORDERED) {

    private val canvasPane = pane {
        border = Border(
            BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
            )
        )
        onMouseMoved = EventHandler { event ->
            mouseCanvasXPosition.value = event.x.toInt()
            mouseCanvasYPosition.value = event.y.toInt()
        }

        onMouseExited = EventHandler {
            mouseCanvasXPosition.value = Integer.MIN_VALUE
            mouseCanvasYPosition.value = Integer.MIN_VALUE
        }
        onMouseClicked = EventHandler { event ->
            if (event.clickCount == 2) {
                addPoint(event)
            }
        }
    }

    private val lblMouseCanvasPosition = label {
        prefWidth = 100.0
    }

    private val lblDistance = label {
    }

    private val mouseCanvasXPosition: IntegerProperty = SimpleIntegerProperty(canvasPane, "xCanvas")

    private val mouseCanvasYPosition: IntegerProperty = SimpleIntegerProperty(canvasPane, "yCanvas")

    private val circles = mutableListOf<Circle>()

    private val lines = mutableListOf<Line>()


    init {
        mouseCanvasXPosition.addListener { _ -> onCanvasMousePositionChanged() }
        mouseCanvasYPosition.addListener { _ -> onCanvasMousePositionChanged() }

        val borderPane = borderpane {
            center = canvasPane
            bottom = flowpane {
                add(lblMouseCanvasPosition)
                add(lblDistance)
            }
        }

        root.center = borderPane
    }

    private fun onCanvasMousePositionChanged() {
        lblMouseCanvasPosition.text =
                if (mouseCanvasXPosition.value > 0 && mouseCanvasYPosition.value > 0) {
                    "%4d x %4d".format(mouseCanvasXPosition.value, mouseCanvasYPosition.value)
                } else {
                    ""
                }
    }

    private fun addPoint(event: MouseEvent) {
        val p = CirclePoint(event.x, event.y).also {
            it.id = "pt${System.currentTimeMillis()}"
        }
        p.addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            canvasPane.children.removeAll(lines.toTypedArray())
            lines.clear()
        }

        circles += p
        canvasPane.add(p)
    }

    override fun validate() {
        check(circles.size > 4) {
            "You need at least 4 points"
        }
    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Point, Path> {
        return SalesmanEnvironment(
            circles.map { c -> Point(c.centerX.toInt(), c.centerY.toInt()) },
            maxGenerations,
            generationSize,
            mutationFactor
        )
    }

    override fun fillOwnComponent(genome: List<Path>) {
        primaryStage.isResizable = false

        val bestPath = genome.first()
        println(bestPath.width)

        canvasPane.children.removeIf {
            it is Line
        }

        bestPath.pathPoints.map {
            Line(
                it.first.x.toDouble(),
                it.first.y.toDouble(),
                it.second.x.toDouble(),
                it.second.y.toDouble()
            )
        }.forEach { canvasPane.add(it) }
        lblDistance.text = "Length = %6.0f".format(bestPath.width)
    }

    override fun resetComponents() {
        primaryStage.isResizable = true
        canvasPane.children.clear()
        circles.clear()
        lblDistance.text = ""
    }

}
