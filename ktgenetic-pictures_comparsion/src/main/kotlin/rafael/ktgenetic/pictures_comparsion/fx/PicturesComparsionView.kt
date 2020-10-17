package rafael.ktgenetic.pictures_comparsion.fx

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
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.pictures_comparsion.*
import rafael.ktgenetic.core.processor.GeneticCrossingType
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.lang.Double.min
import java.util.prefs.Preferences

val noCanvas = Canvas(0.0, 0.0)

fun Color.toKolor(): Kolor = Kolor((255 * this.red).toInt(), (255 * this.green).toInt(), (255 * this.blue).toInt())

fun Kolor.toColor(): Color = Color.rgb(r, g, b)

class PicturesComparsionViewApp : App(PicturesComparsionView::class)

class PicturesComparsionView : GeneticView<Bitmap, Screen>("Pictures Comparsion", GeneticCrossingType.SIMPLE) {

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

    private val cmbCoverage = combobox<Int> {
        items = FXCollections.observableArrayList(arrayOf(2, 4, 5, 10, 20, 25, 50, 100).toList())
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
        addComponent("Coverage (%)", cmbCoverage)

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
//            onMouseClicked = EventHandler { ev ->
//                val pos = positionConversor.toRealPicturePosition(ev.x.toInt(), ev.y.toInt())
//                val color = generatedImageView.image.pixelReader.getColor(pos.x, pos.y)
//
//                val b = Bitmap(ev.x.toInt(), ev.y.toInt(), color.red.toInt(), color.green.toInt(), color.blue.toInt())
//
//                println(b)
//            }

        }
        pnlGeneratedImage.onMouseClicked = EventHandler { ev ->
            if (originalImageView.image != null && ev.clickCount == 2 && cmbCoverage.value != null) {
                fillGeneratedImage()
            }
        }

        imagesPanes.add(pnlOriginalImage, 0, 0)
        imagesPanes.add(pnlGeneratedImage, 1, 0)

        root.center = imagesPanes
    }

    private fun fillGeneratedImage() {
        val generator = PixelsGenerator(canvas.width.toInt(), canvas.height.toInt(), cmbCoverage.value.toDouble() / 100)
        val bitmaps = generator.createBitmaps()
        println("bitmaps.size=" + bitmaps.size)

        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        val pixelWriter = gc.pixelWriter

        bitmaps.forEach { bitmap ->
            pixelWriter.setColor(
                bitmap.position.x,
                bitmap.position.y,
                Color.rgb(bitmap.kolor.r, bitmap.kolor.g, bitmap.kolor.b)
            )
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
//        originalImageView.setOnMouseMoved { ev ->
//            val pos = Position(ev.x.toInt(), ev.y.toInt())
//            val realImagePosition = positionConversor.toRealPicturePosition(pos)
//            val kolor = originalImageView.image.pixelReader.getColor(realImagePosition.x, realImagePosition.y).toKolor()
//
//            val b = Bitmap(pos, kolor)
//
//            println(b)
//        }

        val data =
            if (originalImageView.image.width > pnlOriginalImage.prefWidth || originalImageView.image.height > pnlOriginalImage.prefWidth) {
                originalImageView.fitHeight = pnlOriginalImage.prefHeight
                originalImageView.fitWidth = pnlOriginalImage.prefWidth

                // https://stackoverflow.com/questions/39408845/how-to-get-width-height-of-displayed-image-in-javafx-imageview
                val aspectRatio = originalImageView.image.width / originalImageView.image.height
                val w = min(originalImageView.fitWidth, originalImageView.fitHeight * aspectRatio)
                val h = min(originalImageView.fitHeight, originalImageView.fitWidth / aspectRatio)

                positionConversor =
                    ProportionalPositionConversor(originalImageView.image.width / w, originalImageView.image.height / h)

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
        checkNotNull(cmbCoverage.value) {
            "Please, choose a coverage"
        }
    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Bitmap, Screen> {
        val originalBitmaps: Array<Array<Kolor>> = (0 until canvas.height.toInt()).map { row ->
            (0 until canvas.width.toInt()).map { col ->
                originalImageView.image.pixelReader.getColor(col, row).toKolor()
            }.toTypedArray()
        }.toTypedArray()

        return ScreenEnvironment(
            originalBitmaps,
            cmbCoverage.value / 100.0,
            maxGenerations,
            generationSize,
            mutationFactor
        )
    }

    override fun fillOwnComponent(genome: List<Screen>) {
        val bestScreen = genome[0]

        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        val pixelWriter = gc.pixelWriter

        bestScreen.content.forEach { bitmap ->
            pixelWriter.setColor(bitmap.position.y, bitmap.position.x, bitmap.kolor.toColor())
        }
    }

    override fun resetComponents() {
        originalImageView.image = null
        lblImage.text = null
        lblImageDimensions.text = null
        cmbCoverage.value = null
        pnlGeneratedImage.clear()
        canvas = noCanvas
    }

}
