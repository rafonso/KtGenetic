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

    private val distanceFactors = FXCollections.observableArrayList(0.1, 0.2, 0.3, 0.4, 0.4, 0.6, 0.7, 0.8, 0.9, 1.0)

    private val lblCanvasWidth = Label("width")

    private val cmbWidthFactor = ComboBox<Double>(distanceFactors)

    private val lblCanvasHeight = Label("height")

    private val cmbHeightFactor = ComboBox<Double>(distanceFactors)

    private val lblDistance = Label("Distance")

    private val lblMouseCanvasPosition = Label("")

    private val canvas = Canvas()

    private val distanceToGo: DoubleProperty = SimpleDoubleProperty(this, "Distance to Go")

    private val mouseCanvasXPosition: IntegerProperty = SimpleIntegerProperty(canvas, "xCanvas")

    private val mouseCanvasYPosition: IntegerProperty = SimpleIntegerProperty(canvas, "yCanvas")

    private val widthFactor: DoubleProperty = SimpleDoubleProperty()

    private val heightFactor: DoubleProperty = SimpleDoubleProperty()

    init {
        fun initComboPanel(label: Label, combo: ComboBox<Double>, factor: DoubleProperty, canvasProperty: DoubleProperty): FlowPane {
            label.textProperty().bind(canvasProperty.asString("%4.0f"))
            combo.value = 0.1
            combo.valueProperty().addListener { event ->
                if (event is SimpleObjectProperty<*>) {
                    factor.value = (event.value as Double)
                }
            }
            factor.value = combo.value

            val flowPane = FlowPane()
            flowPane.add(combo)
            flowPane.add(Label(" x "))
            flowPane.add(label)

            return flowPane
        }

        mouseCanvasXPosition.addListener { _ -> onCanvasMousePositionChanged() }
        mouseCanvasYPosition.addListener { _ -> onCanvasMousePositionChanged() }

        distanceToGo.bind(canvas.widthProperty().multiply(widthFactor).multiply(canvas.heightProperty()).multiply(heightFactor))

        addComponent("Width", initComboPanel(lblCanvasWidth, cmbWidthFactor, widthFactor, canvas.widthProperty()))

        addComponent("Height", initComboPanel(lblCanvasHeight, cmbHeightFactor, heightFactor, canvas.heightProperty()))

        lblDistance.textProperty().bind(distanceToGo.asString("%8.0f"))
        lblDistance.alignment = Pos.BOTTOM_LEFT
        addComponent("Distance To Go", lblDistance)


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

    private fun configureCanvas(wrapperPane: Pane) {
        // Bind the width/height property so that the size of the Canvas will be
        // resized as the window is resized
        canvas.widthProperty().bind(wrapperPane.widthProperty())
        canvas.heightProperty().bind(wrapperPane.heightProperty())
        canvas.setOnMouseMoved { event ->
            mouseCanvasXPosition.value = event.x.toInt()
            mouseCanvasYPosition.value = canvas.height.toInt() - event.y.toInt()
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
        return TrackEnvironment(canvas.width.toInt(), canvas.height.toInt(), distanceToGo.intValue(), maxGenerations, generationSize, mutationFactor)
    }

    override fun fillOwnComponent(genome: List<Path>) {
        primaryStage.isResizable = false;
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetComponents() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        primaryStage.isResizable = true;
    }

}
