package rafael.ktgenetic.fx

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import rafael.ktgenetic.core.events.GenerationEvent
import rafael.ktgenetic.core.events.TypeProcessorEvent
import tornadofx.*
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

/**
 * Class responsible for displaying the statistics of a genetic algorithm.
 *
 * @property pnlOutput The output panel.
 * @property lblGeneration The label for the current generation.
 * @property lblBestFitness The label for the best fitness.
 * @property lblAverageFitness The label for the average fitness.
 * @property lblTime The label for the elapsed time.
 * @property cmbUpdateChart The combo box for selecting the update frequency of the chart.
 * @property lineChartFitness The line chart for displaying the fitness over time.
 * @property yAxisChartFitness The y-axis of the fitness chart.
 */
internal class StatisticsView(
    @Suppress("unused") private val pnlOutput: Pane,
    private val lblGeneration: Label,
    private val lblBestFitness: Label,
    private val lblAverageFitness: Label,
    private val lblTime: Label,
    private val cmbUpdateChart: ComboBox<Int>,
    private val lineChartFitness: LineChart<Int, Double>,
    private val yAxisChartFitness: NumberAxis
) : ChangeListener<GenerationEvent> {

    private val yAxisBounds = listOf(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.99, 0.999, 1.0)

    private val currentLowerBoundIndex = intProperty(0)
    private val currentUpperBoundIndex = intProperty(yAxisBounds.lastIndex)

    private val formatter = SimpleDateFormat("mm:ss.SSS")

    private lateinit var averageSeries: XYChart.Series<Int, Double>
    private lateinit var bestSeries: XYChart.Series<Int, Double>

    private lateinit var t0: LocalDateTime

    private lateinit var updateChart: (Int) -> Boolean

    init {
        cmbUpdateChart.valueProperty().onChange { multiple ->
            updateChart = if (multiple == 1) { _ -> true } else { g -> (g % multiple!! == 0) }
        }
        cmbUpdateChart.value = 1

        lineChartFitness.yAxis.isTickMarkVisible = false
        yAxisChartFitness.tooltip(
            "To adjust lower value scroll mouse. " +
                    "To adjust upper value scroll mouse with CTRL pressed."
        )
        yAxisChartFitness.tickLabelFormatter
        yAxisChartFitness.onMouseClicked = EventHandler { yAxisClicked(it) }
        yAxisChartFitness.onScroll = EventHandler { yAxisScrolled(it) }
        currentLowerBoundIndex.onChange { adjustFitnessYAxis() }
        currentUpperBoundIndex.onChange { adjustFitnessYAxis() }
    }

    private fun adjustFitnessYAxis() {
        yAxisChartFitness.lowerBound = yAxisBounds[currentLowerBoundIndex.value]
        yAxisChartFitness.upperBound = yAxisBounds[currentUpperBoundIndex.value]
        yAxisChartFitness.tickUnit = (yAxisChartFitness.upperBound - yAxisChartFitness.lowerBound) / 10
    }

    private fun yAxisScrolled(event: ScrollEvent?) {
        if (event!!.isControlDown) {
            if ((event.deltaY > 0.0) && (currentUpperBoundIndex < yAxisBounds.lastIndex)) {
                currentUpperBoundIndex += 1
            } else if ((event.deltaY < 0.0) && (currentUpperBoundIndex > (currentLowerBoundIndex + 1))) {
                currentUpperBoundIndex -= 1
            }
        } else {
            if ((event.deltaY > 0.0) && (currentLowerBoundIndex < (currentUpperBoundIndex - 1))) {
                currentLowerBoundIndex += 1
            } else if ((event.deltaY < 0.0) && (currentLowerBoundIndex > 0)) {
                currentLowerBoundIndex -= 1
            }
        }
    }

    private fun yAxisClicked(event: MouseEvent) {
        if (event.clickCount == 2 && currentLowerBoundIndex.value > 0) {
            currentLowerBoundIndex.value = 0
            currentUpperBoundIndex.value = yAxisBounds.lastIndex
        }
    }

    /**
     * Resets the view to its initial state.
     */
    fun reset() {
        lblGeneration.textProperty().unbind()
        lblGeneration.text = ""
        lblTime.textProperty().unbind()
        lblTime.text = ""
        lblBestFitness.textProperty().unbind()
        lblBestFitness.text = ""
        lblAverageFitness.textProperty().unbind()
        lblAverageFitness.text = ""
        cmbUpdateChart.value = 1
        lineChartFitness.data.clear()
        yAxisChartFitness.upperBound = 1.0
        yAxisChartFitness.lowerBound = 0.0
        yAxisChartFitness.tickUnit = 0.1

        lineChartFitness.data.clear()
        (lineChartFitness.xAxis as NumberAxis).upperBound = 100.0
    }

    /**
     * Handles the starting of the genetic algorithm.
     *
     * @param startingTime The time at which the algorithm started.
     */
    private fun starting(startingTime: LocalDateTime) {
        t0 = startingTime

        lineChartFitness.data.clear()

        averageSeries = XYChart.Series<Int, Double>()
        averageSeries.name = "Average"
        lineChartFitness.data.add(averageSeries)

        bestSeries = XYChart.Series<Int, Double>()
        bestSeries.name = "Best"
        lineChartFitness.data.add(bestSeries)

    }

    /**
     * Displays the data of the current generation.
     *
     * @param event The event containing the generation data.
     */
    private fun showGenerationData(event: GenerationEvent) {
        val dt = Date(Duration.between(t0, event.dateTime).toMillis())
        lblTime.text = formatter.format(dt)

        lblGeneration.text = event.generation.toString()

        if (event.population.isNotEmpty()) {
            lblBestFitness.text = "%.4f".format(event.statistics.bestFitness)
            lblAverageFitness.text =
                "%.4f (%.4f)".format(event.statistics.averageFitness, event.statistics.averageFitnessDeviation)

            if (updateChart(event.generation)) {
                averageSeries.data.add(XYChart.Data(event.generation, event.statistics.averageFitness))
                bestSeries.data.add(XYChart.Data(event.generation, event.statistics.bestFitness))
            }
        }
    }

    /**
     * Handles a change in the generation event.
     *
     * @param observable The observable value that has changed.
     * @param oldValue The old value of the generation event.
     * @param newValue The new value of the generation event.
     */
    override fun changed(
        observable: ObservableValue<out GenerationEvent>?,
        oldValue: GenerationEvent?,
        newValue: GenerationEvent?
    ) {
        val event = newValue!!
        when {
            event.eventType == TypeProcessorEvent.STARTING ->
                Platform.runLater { starting(event.dateTime) }
            event.isEvaluating() ->
                Platform.runLater { showGenerationData(event) }
        }
    }

}
