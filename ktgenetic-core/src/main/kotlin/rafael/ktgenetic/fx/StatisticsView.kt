package rafael.ktgenetic.fx

import javafx.event.EventHandler
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import rafael.ktgenetic.Chromosome
import tornadofx.*


internal class StatisticsView(
    @Suppress("unused") private val pnlOutput: Pane,
    private val lblGeneration: Label,
    private val lblBestFitness: Label,
    private val lblAverageFitness: Label,
    private val lblTime: Label,
    private val lineChartFitness: LineChart<Int, Double>,
    private val yAxisChartFitness: NumberAxis
) {

    private val yAxisBounds = listOf(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.99, 0.999, 1.0)

//    private val averageFitnessByGeneration: MutableList<Double> = mutableListOf()
//    private val bestFitnessByGeneration: MutableList<Double> = mutableListOf()

    private val currentLowerBoundIndex = intProperty(0)
    private val currentUpperBoundIndex = intProperty(yAxisBounds.lastIndex)

    init {
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

    private fun createSeries(): Pair<XYChart.Series<Int, Double>, XYChart.Series<Int, Double>> {
        lineChartFitness.data.clear()

        val averageSeries = XYChart.Series<Int, Double>()
        averageSeries.name = "Average"
        lineChartFitness.data.add(averageSeries)

        val bestSeries = XYChart.Series<Int, Double>()
        bestSeries.name = "Best"
        lineChartFitness.data.add(bestSeries)

        return Pair(averageSeries, bestSeries)
    }

    fun <C : Chromosome<*>> bind(task: GeneticTask<C>) {
        lblGeneration.textProperty().bind(task.generationProperty)
        lblBestFitness.textProperty().bind(task.bestFitnessProperty)
        lblTime.textProperty().bind(task.timeProperty)
        lblAverageFitness.textProperty().bind(task.averageFitnessProperty)

        val (averageSeries, bestSeries) = createSeries()

        averageSeries.dataProperty().bind(task.averageData)
        bestSeries.dataProperty().bind(task.bestData)
    }

    fun reset() {
        lblGeneration.textProperty().unbind()
        lblGeneration.text = ""
        lblTime.textProperty().unbind()
        lblTime.text = ""
        lblBestFitness.textProperty().unbind()
        lblBestFitness.text = ""
        lblAverageFitness.textProperty().unbind()
        lblAverageFitness.text = ""
        lineChartFitness.data.clear()
        yAxisChartFitness.upperBound = 1.0
        yAxisChartFitness.lowerBound = 0.0
        yAxisChartFitness.tickUnit = 0.1
    }


}
