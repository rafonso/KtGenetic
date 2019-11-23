package rafael.ktgenetic.salesman.fx

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.geometry.VPos
import javafx.scene.control.ComboBox
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import rafael.ktgenetic.Environment
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.TypeProcessorEvent
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.salesman.Path
import rafael.ktgenetic.salesman.Point
import rafael.ktgenetic.salesman.SalesmanEnvironment
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.util.prefs.Preferences

const val MIN_POINTS_TO_START = 5

class SalesmanViewApp : App(SalesmanView::class)

class SalesmanView : GeneticView<Point, Path>("Salesman", GeneticProcessorChoice.ORDERED) {

    // INPUT COMPONENTS

    private val cmbPathType = combobox(values = PathTypeOptions.values().toList()) {
        tooltip = tooltip("Specifies if the produced Path must be open or closed.")
        value = PathTypeOptions.OPEN
        converter = PathTypeOptionsStingConverter
        onAction = EventHandler {
            fillLblPossiblePaths(circles.size)
            circles.forEach(::addContextMenu)
            (it.source as ComboBox<PathTypeOptions>).value.handleSelected(circles)
        }
    }

    private val btnImage = button {
        text = "Select"
        tooltip = tooltip("Select an optional background Image. JPG or PNG formats.")
        onAction = EventHandler {
            selectBackgroundImage()
        }
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
            fillLblPossiblePaths(change.list.size)
            lblNumberOfPoints.text = if (change.list.isEmpty()) "" else "Number of Points: ${change.list.size}"

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

    private fun circlePointToPoint(c: CirclePoint) = Point(c.centerX.toInt(), c.centerY.toInt())

    private fun fillLblPossiblePaths(size: Int) {
        lblNumberOfPossiblePaths.text = if (size <= 1) {
            ""
        } else {
            val numberOfPossiblePaths = cmbPathType.value.maxPossiblePaths(size)
            if (size > 13) { // 13! = 6.227.020.800
                "Possible Paths: %.2e".format(numberOfPossiblePaths.toBigDecimal())
            } else {
                "Possible Paths: %,d".format(numberOfPossiblePaths)
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

    private fun addContextMenu(circle: CirclePoint) {
        cmbPathType.value.handleTypeChoice(circle,
            { circles.toList() },
            circle.contextmenu {
                item("Delete") {
                    action {
                        circles.remove(circle)
                    }
                }
            })
    }

    override fun validate() {
        check(circles.size >= MIN_POINTS_TO_START) {
            "You need at least $MIN_POINTS_TO_START points"
        }
        cmbPathType.value.validateCircles(circles)
    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Point, Path> =
            SalesmanEnvironment(
                circles.map(this::circlePointToPoint),
                cmbPathType.value!!.type,
                circles.filter(::selectStart).mapNotNull(this::circlePointToPoint).firstOrNull(),
                circles.filter(::selectEnd).mapNotNull(this::circlePointToPoint).firstOrNull(),
                maxGenerations,
                generationSize,
                mutationFactor
            )


    override fun fillOwnComponent(genome: List<Path>) {

        fun paintPath(path: Path, pathFill: Paint) {
            path.pathPoints.map {
                Arrow(
                    it.first.x.toDouble(),
                    it.first.y.toDouble(),
                    it.second.x.toDouble(),
                    it.second.y.toDouble(),
                    pathFill
                )
            }.forEach { canvasPane.add(it) }
        }

        primaryStage.isResizable = false

        canvasPane.children.removeIf {
            it !is CirclePoint
        }

        val bestPath = genome[0]
        paintPath(bestPath, bestPaint)
        lblDistance.text = "Length = %6.0f".format(bestPath.width)

        paintPath(genome[1], secondPaint)
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
        cmbPathType.value = PathTypeOptions.OPEN
        lblImage.text = ""
        canvasPane.children.clear()
        circles.clear()
        lblDistance.text = ""
    }

}
