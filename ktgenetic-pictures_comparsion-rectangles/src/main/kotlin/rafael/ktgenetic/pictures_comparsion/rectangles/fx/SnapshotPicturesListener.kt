package rafael.ktgenetic.pictures_comparsion.rectangles.fx

import javafx.embed.swing.SwingFXUtils
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import rafael.ktgenetic.ProcessorEvent
import rafael.ktgenetic.ProcessorListener
import rafael.ktgenetic.TypeProcessorEvent
import rafael.ktgenetic.selection.SelectionOperatorChoice
import tornadofx.runLater
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

const val DIR_FORMAT = "%s " +// Nome do arquivo original
        "OPERATOR=%s " + // Selection Operator
        "ROWS=%02d " +// Quantidade de linhas
        "COLS=%02d " + // Quantidade de colunas
        "MAX_GEN=%04d " + // Quantidade de Gerações
        "GEN_SIZE=%04d " + // Tamanho da geração
        "MUT_FACT=%.1f " + // Fator de Mutação
        "%s" // Data/Hora da geração

class SnapshotPicturesListener(
    rows: Int, cols: Int,
    maxGenerations: Int,
    generationSize: Int,
    mutationFactor: Double,
    selectionOperator: SelectionOperatorChoice,
    originalImageName: String,
    originalImage: Image,
    snapshotBaseDir: Path,
    private val canvas: Canvas,
    private val generationsSnapshot: Int
) : ProcessorListener {

    var generationEvaluator: (Int) -> Boolean = { generation ->
        val firstGeneration = (generation == 1)
        if (firstGeneration) {
            generationEvaluator = this::isGenerationValid
        }
        firstGeneration
    }

    private val snapshotDir: Path

    init {
        val imageName = originalImageName.substringBeforeLast(".")
        val dateTimeCreation =
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace(":", "").substringBeforeLast('.')

        val dirName = DIR_FORMAT.format(
            imageName,
            selectionOperator,
            rows,
            cols,
            maxGenerations,
            generationSize,
            mutationFactor,
            dateTimeCreation
        )
        snapshotDir = Files.createDirectory(snapshotBaseDir.resolve(dirName))

        saveImage(originalImage, "original")
    }

    private fun saveImage(image: Image, imageName: String) {
        val imageFile = snapshotDir.resolve("$imageName.jpg").toFile()

        val renderedImage = BufferedImage(image.width.toInt(), image.height.toInt(), BufferedImage.TYPE_INT_RGB)
        ImageIO.write(SwingFXUtils.fromFXImage(image, renderedImage), "jpg", imageFile)
    }

    private fun isGenerationValid(generation: Int) = (generation % generationsSnapshot == 0)

    override fun onEvent(event: ProcessorEvent<*>) {
        if ((event.eventType == TypeProcessorEvent.GENERATION_EVALUATED) && generationEvaluator(event.generation)) {
            runLater {
                val image = WritableImage(canvas.width.toInt(), canvas.height.toInt())
                canvas.snapshot(null, image)

                val fileName = "%04d_F=%.4f".format(event.generation, event.population.first().fitness)

                saveImage(image, fileName)
            }
        }
    }

}
