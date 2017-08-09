package rafael.ktgenetic.track.fx

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.track.Direction
import rafael.ktgenetic.track.Path
import rafael.ktgenetic.track.TrackEnvironment
import tornadofx.*


class TrackViewApp : App(TrackView::class)

class TrackView : GeneticView<Direction, Path>("Track", GeneticProcessorChoice.SIMPLE) {

    private val distanceFactors = FXCollections.observableArrayList((1..10).toList())

    private val lblCanvasWidth = Label("width")

    private val cmbDistanceFactor = ComboBox<Int>(distanceFactors)

    private val lblCanvasHeight = Label("height")

    private val lblTrackLength = Label("Length")

    private val lblMouseCanvasPosition = Label("")

    private val canvas = Canvas()

    private val trackLenght: IntegerProperty = SimpleIntegerProperty(this, "Track Length")

    private val mouseCanvasXPosition: IntegerProperty = SimpleIntegerProperty(canvas, "xCanvas")

    private val mouseCanvasYPosition: IntegerProperty = SimpleIntegerProperty(canvas, "yCanvas")

    private val distanceFactor: IntegerProperty = SimpleIntegerProperty(this, "distanceFactor")


    init {
        mouseCanvasXPosition.addListener { _ -> onCanvasMousePositionChanged() }
        mouseCanvasYPosition.addListener { _ -> onCanvasMousePositionChanged() }

        trackLenght.bind(distanceFactor.multiply(canvas.widthProperty().add(canvas.heightProperty())))

        lblCanvasWidth.textProperty().bind(canvas.widthProperty().asString("%4.0f"))
        addComponent("Width", lblCanvasWidth)

        lblCanvasHeight.textProperty().bind(canvas.heightProperty().asString("%4.0f"))
        addComponent("Height", lblCanvasHeight)

        cmbDistanceFactor.valueProperty().addListener { event ->
            if (event is SimpleObjectProperty<*>) {
                distanceFactor.value = (event.value as Int)
            }
        }
        cmbDistanceFactor.value = 2
        addComponent("Track Lenght Factor", cmbDistanceFactor)

        lblTrackLength.textProperty().bind(trackLenght.asString("%5d"))
        lblTrackLength.alignment = Pos.BOTTOM_LEFT
        addComponent("Distance To Go", lblTrackLength)

        val wrapperPane = Pane()
        wrapperPane.border = Border(BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))

        configureCanvas(wrapperPane)
        // Put canvas in the center of the window (*)
        wrapperPane.children.add(canvas)

        val borderPane = BorderPane()
        // Create a wrapper Pane first
        borderPane.center = wrapperPane
        borderPane.bottom = lblMouseCanvasPosition

        root.center = borderPane

        draw(canvas)
    }

    private inline fun yToYCanvas(y: Int) = canvas.height.toInt() - y

    private fun configureCanvas(wrapperPane: Pane) {
        // Bind the width/height property so that the size of the Canvas will be
        // resized as the window is resized
        canvas.widthProperty().bind(wrapperPane.widthProperty())
        canvas.heightProperty().bind(wrapperPane.heightProperty())
        canvas.setOnMouseMoved { event ->
            mouseCanvasXPosition.value = event.x.toInt()
            mouseCanvasYPosition.value = yToYCanvas(event.y.toInt())
        }
        canvas.setOnMouseExited { _ ->
            mouseCanvasXPosition.value = Integer.MIN_VALUE
            mouseCanvasYPosition.value = Integer.MIN_VALUE
        }
        // redraw when resized
        canvas.widthProperty().addListener { _ -> draw(canvas) }
        canvas.heightProperty().addListener { _ -> draw(canvas) }
    }

    private fun onCanvasMousePositionChanged() {
        lblMouseCanvasPosition.text =
                if (mouseCanvasXPosition.value > 0 && mouseCanvasYPosition.value > 0) {
                    "%4d x %4d".format(mouseCanvasXPosition.value, mouseCanvasYPosition.value)
                } else {
                    ""
                }
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

        // JVM property: -Dshowgrid
        if (System.getProperty("showgrid") == null) {
            canvas.style = "-fx-background-color: black;";
        } else {
            val radio = 30.0
            val radio2 = 2 * radio
            gc.clearRect(0.0, 0.0, width.toDouble(), height.toDouble())
            gc.stroke = Color.RED
            gc.strokeLine(0.0, 0.0, width.toDouble(), height.toDouble())
            gc.strokeLine(0.0, height.toDouble(), width.toDouble(), 0.0)
            gc.fill = Color.BLUE
            gc.fillOval(-radio, -radio, radio2, radio2)
            gc.fillOval((-radio + width), -radio, radio2, radio2)
            gc.fillOval(-radio, (-radio + height), radio2, radio2)
            gc.fillOval((-radio + width), (-radio + height), radio2, radio2)
        }
    }

    override fun validate() {
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Direction, Path> {
        return TrackEnvironment(canvas.width.toInt(), canvas.height.toInt(), trackLenght.intValue(), maxGenerations, generationSize, mutationFactor)
    }

    override fun fillOwnComponent(genome: List<Path>) {
        primaryStage.isResizable = false;


        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetComponents() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        primaryStage.isResizable = true;
    }

}
