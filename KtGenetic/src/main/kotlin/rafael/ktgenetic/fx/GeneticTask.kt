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
import rafael.ktgenetic.*
import rafael.ktgenetic.processor.GeneticProcessor
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

internal class GeneticTask<C : Chromosome<*>>(private val processor: GeneticProcessor<*, C>) :
        Task<ProcessorEvent<C>>(), ProcessorListener {

    lateinit var t0: Instant

    val timeProperty: StringProperty = SimpleStringProperty()
    val generationProperty: StringProperty = SimpleStringProperty()
    val bestFitnessProperty: StringProperty = SimpleStringProperty()
    val averageFitnessProperty: StringProperty = SimpleStringProperty()
    val averageData: ObjectProperty<ObservableList<XYChart.Data<Int, Double>>> = SimpleObjectProperty(FXCollections.observableArrayList<XYChart.Data<Int, Double>>())
    val bestData: ObjectProperty<ObservableList<XYChart.Data<Int, Double>>> = SimpleObjectProperty(FXCollections.observableArrayList<XYChart.Data<Int, Double>>())

    override fun call(): ProcessorEvent<C> {
        processor.addListener(this)
        processor.addListener(LogProcessorListener())

        t0 = Instant.now()
        return processor.process()
    }

    private val formatter = SimpleDateFormat("mm:ss.SSS")

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        processor.continueProcessing = false
        return super.cancel(mayInterruptIfRunning)
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        if((event.eventType == TypeProcessorEvent.GENERATION_EVALUATING) || event.eventType.ended) {
            Platform.runLater {
                val dt = Date(Duration.between(t0, Instant.now()).toMillis())
                timeProperty.value = formatter.format(dt)

                generationProperty.value = event.generation.toString()
                if (!event.population.isEmpty()) {
                    val (best, average, deviation) = getBestAverageDeviationFitness(event.population)
                    bestFitnessProperty.value = "%.3f".format(best)
                    averageFitnessProperty.value = "%.3f (%.3f)".format(average, deviation)
                    averageData.value.add(XYChart.Data(event.generation, average))
                    bestData.value.add(XYChart.Data(event.generation, best))
                }
            }
        }
    }

}