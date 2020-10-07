package rafael.ktgenetic.fx

import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.scene.chart.XYChart
import javafx.scene.control.Alert
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.stage.Window
import rafael.ktgenetic.*
import rafael.ktgenetic.processor.GeneticProcessor
import tornadofx.label
import tornadofx.textarea
import tornadofx.vbox
import tornadofx.vgrow
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*


internal class GeneticTask<C : Chromosome<*>>(
    private val processor: GeneticProcessor<*, C>,
    private val root: Window,
    private val fillOwnComponent: (List<C>) -> Unit
) :
    Task<ProcessorEvent<C>>(), ProcessorListener {

    private lateinit var t0: LocalDateTime

    val timeProperty            : StringProperty = SimpleStringProperty()
    val generationProperty      : StringProperty = SimpleStringProperty()
    val bestFitnessProperty     : StringProperty = SimpleStringProperty()
    val averageFitnessProperty  : StringProperty = SimpleStringProperty()
    val averageData             : ObjectProperty<ObservableList<XYChart.Data<Int, Double>>> =
        SimpleObjectProperty(FXCollections.observableArrayList<XYChart.Data<Int, Double>>())
    val bestData                : ObjectProperty<ObservableList<XYChart.Data<Int, Double>>> =
        SimpleObjectProperty(FXCollections.observableArrayList<XYChart.Data<Int, Double>>())

    override fun call(): ProcessorEvent<C> {
        processor.addListener(this)
        processor.addListener(LogProcessorListener())

        t0 = LocalDateTime.now()
        return processor.process()
    }

    private val formatter = SimpleDateFormat("mm:ss.SSS")

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        processor.continueProcessing = false
        return super.cancel(mayInterruptIfRunning)
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType == TypeProcessorEvent.ERROR) {
            Platform.runLater { showError(event) }
        } else if ((event.eventType == TypeProcessorEvent.GENERATION_EVALUATING) || event.eventType.ended) {
            Platform.runLater { showGenerationData(event) }
        }
    }

    private fun showError(event: ProcessorEvent<*>) {
        val error = event.error!!

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        error.printStackTrace(pw)
        val stackTrace = sw.toString()

        Alert(Alert.AlertType.ERROR).also {
            it.title = "Execution Error at generation ${event.generation}"
            it.headerText = if(error.message == null) "" else error.message
            it.initOwner(root)
            it.dialogPane.content = vbox {
                label { text = "Stack Trace:" }
                textarea {
                    text = stackTrace
                    font = Font.font("monospaced")
                    vgrow = Priority.ALWAYS
                }
            }
            (it.dialogPane.scene.window as Stage).icons.add(geneticIcon)
            it.isResizable = true
        }.showAndWait()
    }

    private fun showGenerationData(event: ProcessorEvent<*>) {
        val dt = Date(Duration.between(t0, event.dateTime).toMillis())
        timeProperty.value = formatter.format(dt)

        generationProperty.value = event.generation.toString()
        if (event.population.isNotEmpty()) {
            bestFitnessProperty.value = "%.4f".format(event.statistics.bestFitness)
            averageFitnessProperty.value = "%.4f (%.4f)".format(event.statistics.averageFitness, event.statistics.averageFitnessDeviation)
            averageData.value.add(XYChart.Data(event.generation, event.statistics.averageFitness))
            bestData.value.add(XYChart.Data(event.generation, event.statistics.bestFitness))

            @Suppress("UNCHECKED_CAST")
            fillOwnComponent(event.population as List<C>)
        }
    }

}
