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
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.pictures_comparsion.Bitmap
import rafael.ktgenetic.pictures_comparsion.PixelsGenerator
import rafael.ktgenetic.pictures_comparsion.Screen
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.lang.Double.min
import java.time.Duration
import java.time.Instant
import java.util.prefs.Preferences


class PicturesComparsionViewApp : App(PicturesComparsionView::class)

class PicturesComparsionView : GeneticView<Bitmap, Screen>("Pictures Comparsion", GeneticProcessorChoice.SIMPLE) {

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
        items = FXCollections.observableArrayList(IntProgression.fromClosedRange(10, 90, 5).toList())
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

    var canvas: Canvas = Canvas(0.0, 0.0)

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
//            onMouseMoved = EventHandler { ev ->
//                println("pnlOriginalImage: ${ev.x}, ${ev.y}")
//            }
        }
        with(pnlGeneratedImage) {
            prefWidthProperty().bind(imagesPanes.prefWidthProperty().divide(2))
            prefHeightProperty().bind(imagesPanes.prefHeightProperty())
//            onMouseMoved = EventHandler { ev ->
//                println("pnlGeneratedImage: ${ev.x}, ${ev.y}")
//            }
        }

        with(originalImageView) {
            isPreserveRatio = true
            isSmooth = true
        }
        pnlOriginalImage.add(originalImageView)

        with(generatedImageView) {
            isPreserveRatio = true
            isSmooth = true
            onMouseClicked = EventHandler { ev ->
                val color = generatedImageView.image.pixelReader.getColor(ev.x.toInt(), ev.y.toInt())
                println("%3d %3d".format(ev.x.toInt(), ev.y.toInt(), color))
//                generatedImageView.
            }

        }
//        pnlGeneratedImage.add(generatedImageView)
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
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        val pixelWriter = gc.pixelWriter

        val generator = PixelsGenerator(canvas.width.toInt(), canvas.height.toInt(), cmbCoverage.value.toDouble() / 100)
        val t0 = Instant.now()
        val bitmaps = generator.createBitmaps()
        val t1 = Instant.now()
        println("Bytes: " + Duration.between(t0, t1))
        bitmaps.forEach { bitmap ->
            pixelWriter.setColor(bitmap.x, bitmap.y, Color.rgb(bitmap.r, bitmap.g, bitmap.b))
        }
        val t2 = Instant.now()
        println("Pixels: " + Duration.between(t1, t2))

        val t3 = Instant.now()
        println("Reload: " + Duration.between(t2, t3))
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

                Triple(w, h, "redimensioned")
            } else {
                originalImageView.fitHeight = 0.0
                originalImageView.fitWidth = 0.0

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
        val imageFileChooser = FileChooser()
        imageFileChooser.initialDirectory = File(pathImage)
        imageFileChooser.extensionFilters += FileChooser.ExtensionFilter("JPG / PNG Images", "*.jpg", "*.png")

        return imageFileChooser.showOpenDialog(super.currentWindow)
    }

    override fun validate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<Bitmap, Screen> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fillOwnComponent(genome: List<Screen>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetComponents() {
        originalImageView.image = null
        lblImage.text = null
        lblImageDimensions.text = null
        cmbCoverage.value = null
        pnlGeneratedImage.clear()
        canvas = Canvas(0.0, 0.0)
    }
}