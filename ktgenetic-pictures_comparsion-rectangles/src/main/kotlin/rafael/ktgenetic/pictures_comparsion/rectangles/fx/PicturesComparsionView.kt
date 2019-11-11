package rafael.ktgenetic.pictures_comparsion.rectangles.fx

import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.VPos
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.pictures_comparsion.*
import rafael.ktgenetic.pictures_comparsion.rectangles.*
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.lang.Double.min
import java.util.prefs.Preferences

val noCanvas = Canvas(0.0, 0.0)

fun Color.toKolor(): Kolor = Kolor((255 * this.red).toInt(), (255 * this.green).toInt(), (255 * this.blue).toInt())

fun Kolor.toColor(): Color = Color.rgb(r, g, b)

class PicturesComparsionViewApp : App(PicturesComparsionView::class)

class PicturesComparsionView : GeneticView<Rectangle, Screen>("Pictures Comparsion", GeneticProcessorChoice.SIMPLE) {

    private var positionConversor: PositionConversor = SimplePositionConversor()

    // INPUT COMPONENTS

    private val btnImage = button {
        text = "Select"
    }

    private val lblImage = label {
        style {
            fontWeight = FontWeight.BOLD
            vAlignment = VPos.BOTTOM
            vgrow = Priority.ALWAYS
        }
    }

    private val lblImageDimensions = label {
        style = lblImage.style
    }

    private val cmbColumns = combobox<Int> {
        items = FXCollections.observableArrayList((1..21).toList())
        value = 5
        onAction = EventHandler {
            if (originalImageView.image != null) {
                drawGridPreview()
            }
        }
    }

    private val cmbRows = combobox<Int> {
        items = FXCollections.observableArrayList((1..21).toList())
        value = 5
        onAction = EventHandler {
            if (originalImageView.image != null) {
                drawGridPreview()
            }
        }
    }

    // OUTPUT COMPONENTS

    private val originalImageView: ImageView = ImageView()

    private val pnlOriginalImage: Pane = pane {
        style {
            borderStyle = multi(BorderStrokeStyle.SOLID)
            borderWidth += box(1.px)
        }
    }

    private val generatedImageView: ImageView = ImageView()

    private val pnlGeneratedImage = pane {
        style {
            borderStyle = multi(BorderStrokeStyle.DOTTED)
            borderWidth += box(1.px)
        }
    }

    private var canvas: Canvas = noCanvas

    init {
        super.currentStage!!.isResizable = false

        btnImage.onAction = EventHandler {
            selectOriginalImage()
        }

        addComponent("Image", btnImage)
        addComponent("Image Name", lblImage)
        addComponent("Image Dimensions", lblImageDimensions)
        addComponent("Rows", cmbRows)
        addComponent("Columns", cmbColumns)

        val imagesPanes = gridpane {
            prefWidth = 800.0
            prefHeight = 450.0
            hgap = 5.0
        }

        with(pnlOriginalImage) {
            prefWidthProperty().bind(imagesPanes.prefWidthProperty().divide(2))
            prefHeightProperty().bind(imagesPanes.prefHeightProperty())
        }
        with(pnlGeneratedImage) {
            prefWidthProperty().bind(imagesPanes.prefWidthProperty().divide(2))
            prefHeightProperty().bind(imagesPanes.prefHeightProperty())
        }

        with(originalImageView) {
            isPreserveRatio = true
            isSmooth = true
        }
        pnlOriginalImage.add(originalImageView)

        with(generatedImageView) {
            isPreserveRatio = true
            isSmooth = true
        }

        imagesPanes.add(pnlOriginalImage, 0, 0)
        imagesPanes.add(pnlGeneratedImage, 1, 0)

        root.center = imagesPanes
    }

    private fun drawGridPreview() {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        gc.fill = Color.WHITE
        gc.stroke = Color.PINK

        val deltaW = canvas.width / cmbColumns.value
        val deltaH = canvas.height / cmbRows.value

        (0 until cmbColumns.value + 1).forEach { cl ->
            val w0 = cl * deltaW
            (0 until cmbRows.value + 1).map { rw ->
                val h0 = rw * deltaH
                gc.strokeRect(w0, h0, deltaW, deltaH)
            }
        }
    }

    private fun selectOriginalImage() {
        val prefs = Preferences.userRoot().node(this.javaClass.name)
        val pathImage = prefs.get("imageDir", System.getProperty("user.home"))

        chooseImageFile(pathImage)?.let { fileImage ->
            loadOriginalImage(fileImage)
            prefs.put("imageDir", fileImage.parent)
        }
    }

    private fun loadOriginalImage(fileImage: File) {
        originalImageView.image = Image(FileInputStream(fileImage))

        val data =
                if (originalImageView.image.width > pnlOriginalImage.prefWidth || originalImageView.image.height > pnlOriginalImage.prefWidth) {
                    originalImageView.fitHeight = pnlOriginalImage.prefHeight
                    originalImageView.fitWidth = pnlOriginalImage.prefWidth

                    // https://stackoverflow.com/questions/39408845/how-to-get-width-height-of-displayed-image-in-javafx-imageview
                    val aspectRatio = originalImageView.image.width / originalImageView.image.height
                    val w = min(originalImageView.fitWidth, originalImageView.fitHeight * aspectRatio)
                    val h = min(originalImageView.fitHeight, originalImageView.fitWidth / aspectRatio)

                    positionConversor =
                            ProportionalPositionConversor(
                                originalImageView.image.width / w,
                                originalImageView.image.height / h
                            )

                    Triple(w, h, "redimensioned")
                } else {
                    originalImageView.fitHeight = 0.0
                    originalImageView.fitWidth = 0.0

                    positionConversor = SimplePositionConversor()

                    Triple(originalImageView.image.width, originalImageView.image.height, "original")
                }

        canvas = canvas {
            width = data.first
            height = data.second
        }
        pnlGeneratedImage.clear()
        pnlGeneratedImage.add(canvas)

        lblImage.text = fileImage.name
        lblImageDimensions.text = "%3d x %3d (%s)".format(
            canvas.width.toInt(),
            canvas.height.toInt(),
            data.third
        )

        drawGridPreview()
    }

    private fun chooseImageFile(pathImage: String): File? {
        val imageFileChooser = FileChooser().apply {
            initialDirectory = File(pathImage)
            title = "Choose an Image File"
            extensionFilters += FileChooser.ExtensionFilter("JPG / PNG Images", "*.jpg", "*.png")
        }

        return imageFileChooser.showOpenDialog(super.currentWindow)
    }

    override fun validate() {
        checkNotNull(originalImageView.image) {
            "Please, choose an Image"
        }
    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Rectangle, Screen> {
        val originalBitmaps: Array<Array<Kolor>> = (0 until canvas.height.toInt()).map { row ->
            (0 until canvas.width.toInt()).map { col ->
                originalImageView.image.pixelReader.getColor(col, row).toKolor()
            }.toTypedArray()
        }.toTypedArray()

        return ScreenEnvironment(
            originalBitmaps,
            cmbRows.value, cmbColumns.value,
            maxGenerations,
            generationSize,
            mutationFactor
        )
    }

    override fun fillOwnComponent(genome: List<Screen>) {
        val bestScreen = genome[0]

        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        bestScreen.content.forEach { tile ->
            gc.fill = tile.kolor.toColor()
            gc.fillRect(
                tile.upperCorner.x.toDouble(),
                tile.upperCorner.y.toDouble(),
                tile.width.toDouble(),
                tile.height.toDouble()
            )
        }
    }

    override fun resetComponents() {
        originalImageView.image = null
        lblImage.text = null
        lblImageDimensions.text = null
        cmbRows.value = 5
        cmbColumns.value = 5
        pnlGeneratedImage.clear()
        canvas = noCanvas
    }

}