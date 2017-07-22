package rafael.ktgenetic.track.fx

import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.canvas.Canvas
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
        // Create a wrapper Pane first
        val wrapperPane = Pane()
        // Put canvas in the center of the window
//        val canvas = Canvas()
        wrapperPane.children.add(trackCanvas)
        // Bind the width/height property to the wrapper Pane
        trackCanvas.widthProperty().bind(wrapperPane.widthProperty())
        trackCanvas.heightProperty().bind(wrapperPane.heightProperty())
        // redraw when resized
        trackCanvas.widthProperty().addListener { event -> draw(trackCanvas) }
        trackCanvas.heightProperty().addListener { event -> draw(trackCanvas) }
        draw(trackCanvas)


        root.center =wrapperPane
    }

    /**
     * Draw crossed red lines which each each end is at the corner of window,
     * and 4 blue circles whose each center is at the corner of the window,
     * so that make it possible to know where is the extent the Canvas draws
     */
    private fun draw(canvas: Canvas) {
        val width = canvas.width.toInt()
        val height = canvas.height.toInt()
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
