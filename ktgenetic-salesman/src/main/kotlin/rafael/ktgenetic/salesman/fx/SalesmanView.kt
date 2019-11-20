package rafael.ktgenetic.salesman.fx

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.geometry.VPos
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import rafael.ktgenetic.Environment
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.TypeProcessorEvent
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.salesman.Path
import rafael.ktgenetic.salesman.PathType
import rafael.ktgenetic.salesman.Point
import rafael.ktgenetic.salesman.SalesmanEnvironment
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.util.prefs.Preferences


class SalesmanViewApp : App(SalesmanView::class)

class SalesmanView : GeneticView<Point, Path>("Salesman", GeneticProcessorChoice.ORDERED) {

    // INPUT COMPONENTS

    private val cmbPathType = combobox(values = PathType.values().toList()) {
        tooltip = tooltip("Specifies if the produced Path must be open or closed.")
    }

    private val btnImage = button {
        text = "Select"
        tooltip = tooltip("Select an optional background Image. JPG or PNG formats.")
    }

    private val lblImage = label {
        style {
            fontWeight = FontWeight.BOLD
            vAlignment = VPos.BOTTOM
            vgrow = Priority.ALWAYS
        }
    }

    // OUTPUT COMPONENTS

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
        prefWidth = 100.0
    }

    private val lblNumberOfPoints = label {
        prefWidth = 200.0
    }

    private val lblNumberOfPossiblePaths = label {
        prefWidth = 200.0
    }

    // OTHER ATTRIBUTES

    private val mouseCanvasXPosition: IntegerProperty = SimpleIntegerProperty(canvasPane, "xCanvas")

    private val mouseCanvasYPosition: IntegerProperty = SimpleIntegerProperty(canvasPane, "yCanvas")

    private val circles = observableListOf<CirclePoint>().also {
        it.addListener(ListChangeListener { change ->
            if (change.list.isEmpty()) {
                lblNumberOfPoints.text = ""
                lblNumberOfPossiblePaths.text = ""
            } else {
                lblNumberOfPoints.text = "Number of Points: ${change.list.size}"
                val numberOfPossiblePaths =
                        (1..change.list.size).fold(BigInteger.ONE) { prod, i -> prod * i.toBigInteger() }
                lblNumberOfPossiblePaths.text = if (change.list.size > 13) { // 13! = 6.227.020.800
                    "Possible Paths: %.2e".format(numberOfPossiblePaths.toBigDecimal())
                } else {
                    "Possible Paths: %,d".format(numberOfPossiblePaths)
                }
            }
            while (change.next()) {
                if (change.wasRemoved()) {
                    canvasPane.children.removeAll(change.removed)
                }
            }
        })
    }

    init {
        addComponent("Path Type", cmbPathType)
        addComponent("Image", btnImage)
        addComponent("Image Name", lblImage)

        btnImage.onAction = EventHandler {
            selectBackgroundImage()
        }
        mouseCanvasXPosition.addListener { _ -> onCanvasMousePositionChanged() }
        mouseCanvasYPosition.addListener { _ -> onCanvasMousePositionChanged() }

        root.center = borderpane {
            center = canvasPane
            bottom = flowpane {
                add(lblMouseCanvasPosition)
                add(lblNumberOfPoints)
                add(lblNumberOfPossiblePaths)
                add(lblDistance)
                hgap = 10.0
            }
        }
    }

    private fun chooseImageFile(pathImage: String): File? {
        val imageFileChooser = FileChooser().apply {
            initialDirectory = File(pathImage)
            title = "Choose an Image File"
            extensionFilters += FileChooser.ExtensionFilter("JPG / PNG Images", "*.jpg", "*.png")
        }

        return imageFileChooser.showOpenDialog(super.currentWindow)
    }

    private fun loadBackgroundImage(fileImage: File) {
        val backgroundImageView = ImageView(Image(FileInputStream(fileImage))).also {
            it.fitWidthProperty().bind(canvasPane.widthProperty())
            it.fitHeightProperty().bind(canvasPane.heightProperty())
        }

        canvasPane.add(backgroundImageView)
        lblImage.text = fileImage.name
    }

    private fun selectBackgroundImage() {
        val prefs = Preferences.userRoot().node(this.javaClass.name)
        val pathImage = prefs.get("imageDir", System.getProperty("user.home"))

        chooseImageFile(pathImage)?.let { fileImage ->
            loadBackgroundImage(fileImage)
            prefs.put("imageDir", fileImage.parent)
        }
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
            addContextMenu(it)
        }

        circles += p
        canvasPane.add(p)
    }

    private fun addContextMenu(circle: Circle) = circle.contextmenu {
        item("Delete") {
            action {
                circles.remove(circle)
            }
        }
//        separator()
    }

    override fun validate() {
        check(circles.size > 4) {
            "You need at least 4 points"
        }
        checkNotNull(cmbPathType.value) {
            "Please, choose a Path Type"
        }
    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Point, Path> =
            SalesmanEnvironment(
                circles.map { c -> Point(c.centerX.toInt(), c.centerY.toInt()) },
                cmbPathType.value,
                maxGenerations,
                generationSize,
                mutationFactor
            )


    override fun fillOwnComponent(genome: List<Path>) {
        primaryStage.isResizable = false

        val bestPath = genome.first()

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

    override fun onEvent(event: ProcessorEvent<*>) {
        super.onEvent(event)

        when {
            event.eventType == TypeProcessorEvent.STARTING -> circles.forEach {
                it.removeEventHandler(MouseEvent.ANY, CirclePointMouseEventHandler)
                it.contextmenu { }
            }
            event.eventType.ended                          ->
                circles.forEach {
                    runLater {
                        it.addEventHandler(MouseEvent.ANY, CirclePointMouseEventHandler)
                        addContextMenu(it)
                    }
                }
        }
    }

    override fun resetComponents() {
        primaryStage.isResizable = true
        canvasPane.children.clear()
        circles.clear()
        lblDistance.text = ""
    }

}
