package rafael.ktgenetic.pictures_comparsion.fx

import javafx.event.EventHandler
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.pictures_comparsion.Bitmap
import rafael.ktgenetic.pictures_comparsion.Screen
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*
import java.io.File
import java.io.FileInputStream

class PicturesComparsionViewApp : App(PicturesComparsionView::class)

class PicturesComparsionView : GeneticView<Bitmap, Screen>("Pictures Comparsion", GeneticProcessorChoice.SIMPLE) {

    // INPUT COMPONENTS

    private val btnImage = button {
        text = "Select"
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

    init {
        super.currentStage!!.isResizable = false

        btnImage.onAction = EventHandler {
            selectOriginalImage()
        }
        addComponent("Image", btnImage)

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
        }
        pnlGeneratedImage.add(generatedImageView)

        imagesPanes.add(pnlOriginalImage, 0, 0)
        imagesPanes.add(pnlGeneratedImage, 1, 0)

        root.center = imagesPanes
    }

    private fun selectOriginalImage() {
        val imageFileChooser = FileChooser()
        imageFileChooser.extensionFilters += FileChooser.ExtensionFilter("JPG / PNG Images", "*.jpg", "*.png")
        val fileImage: File? = imageFileChooser.showOpenDialog(super.currentWindow)
        if (fileImage != null) {
            originalImageView.image = Image(FileInputStream(fileImage))
            println("${originalImageView.fitWidth}, ${originalImageView.fitHeight}")
            originalImageView.fitHeight = pnlOriginalImage.prefHeight
            originalImageView.fitWidth = pnlOriginalImage.prefWidth

            generatedImageView.fitWidth = originalImageView.fitWidth
            generatedImageView.fitHeight = originalImageView.fitHeight

            originalImageView.setOnMouseMoved { event ->
                val color = originalImageView.image.pixelReader.getColor(event.x.toInt(), event.y.toInt())
                println("${event.x}, ${event.y}: $color")
//                generatedImageView.
            }
        }
    }

    override fun validate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Bitmap, Screen> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fillOwnComponent(genome: List<Screen>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetComponents() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}