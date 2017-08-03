package rafael.ktgenetic.track.fx

import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.track.Direction
import rafael.ktgenetic.track.Path
import tornadofx.*

class TrackViewApp : App(TrackView::class)

class TrackView : GeneticView<Direction, Path>("Track", GeneticProcessorChoice.SIMPLE) {

    private val trackCanvas = Canvas()
/*
        style {
//            borderColor += box(Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW)
            backgroundColor = MultiValue<Paint>(arrayOf(Color.RED))
            bo
        }
*/


    init {
        /*
        trackCanvas.onMouseMoved = EventHandler { println(it.toString()) }
        trackCanvas.style = """
        |-fx-padding: 10;
        |-fx-border-style: solid inside;
        |-fx-border-width: 2;
        |-fx-border-insets: 5;
        |-fx-border-radius: 5;
        |-fx-border-color: blue;
""".trimMargin()

*/
        val borderPane = BorderPane()
        val label = Label("teste")
        // Create a wrapper Pane first
        val wrapperPane = Pane()
        borderPane.center = wrapperPane
        borderPane.bottom = label
        // Put canvas in the center of the window (*)
        val canvas = Canvas()
        wrapperPane.children.add(canvas)
        // Bind the width/height property so that the size of the Canvas will be
        // resized as the window is resized
        canvas.widthProperty().bind(wrapperPane.widthProperty())
        canvas.heightProperty().bind(wrapperPane.heightProperty())
        // redraw when resized
        canvas.widthProperty().addListener { event -> draw(canvas) }
        canvas.heightProperty().addListener { event -> draw(canvas) }
        canvas.setOnMouseMoved { event ->
            val y = canvas.height - event.y
            label.text = event.x.toString() + " x " + y
        }
        canvas.setOnMouseExited { event -> label.text = "" }
        draw(canvas)

        root.center =borderPane

    }

    /**
     * https://stackoverflow.com/questions/37678704/how-to-embed-javafx-canvas-into-borderpane
     * Draw crossed red lines which each each end is at the corner of window,
     * and 4 blue circles whose each center is at the corner of the window,
     * so that make it possible to know where is the extent the Canvas draws
     */
    private fun draw(canvas: Canvas) {
        val width = canvas.width.toInt()
        val height = canvas.height.toInt()
        println("$width X $height")
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, width.toDouble(), height.toDouble())
        gc.stroke = Color.RED
        gc.strokeLine(0.0, 0.0, width.toDouble(), height.toDouble())
        gc.strokeLine(0.0, height.toDouble(), width.toDouble(), 0.0)
        gc.fill = Color.BLUE
        gc.fillOval(-30.0, -30.0, 60.0, 60.0)
        gc.fillOval((-30 + width).toDouble(), -30.0, 60.0, 60.0)
        gc.fillOval(-30.0, (-30 + height).toDouble(), 60.0, 60.0)
        gc.fillOval((-30 + width).toDouble(), (-30 + height).toDouble(), 60.0, 60.0)
    }

    override fun validate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Direction, Path> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fillOwnComponent(genome: List<Path>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetComponents() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
