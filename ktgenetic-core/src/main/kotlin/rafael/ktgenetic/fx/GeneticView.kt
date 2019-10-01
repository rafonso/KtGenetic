package rafael.ktgenetic.fx

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.util.StringConverter
import rafael.ktgenetic.*
import rafael.ktgenetic.LogLevel.DEBUG
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.selection.SelectionOperatorChoice
import tornadofx.*
import java.time.Instant

abstract class GeneticView<G, C : Chromosome<G>>(title: String, private val processorChoice: GeneticProcessorChoice) : View(title), ProcessorListener {
    override val root: BorderPane by fxml("/view/Genetic.fxml")

    private val values: ObservableList<Int> = FXCollections.observableArrayList(1, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
    private val mutationFactors: ObservableList<Double> = FXCollections.observableArrayList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
    private val maxColumns = 5

    private val pnlInput: GridPane                                      by fxid()
    private val pnlOutput: GridPane                                     by fxid()
    private val pnlButtons: GridPane                                    by fxid()
    private val cmbGenerations: ComboBox<Int>                           by fxid()
    private val cmbPopulation: ComboBox<Int>                            by fxid()
    private val cmbMutationFactor: ComboBox<Double>                     by fxid()
    private val cmbSelectionOperator: ComboBox<SelectionOperatorChoice> by fxid()
    private val btnStop: Button                                         by fxid()
    private val btnReset: Button                                        by fxid()
    private val btnStart: Button                                        by fxid()
    private val lblGeneration: Label                                    by fxid()
    private val lblBestFitness: Label                                   by fxid()
    private val lblAverageFitness: Label                                by fxid()
    private val lblTime: Label                                          by fxid()
    private val lineChartFitness: LineChart<Int, Double>                by fxid()
    private val yAxisChartFitness: NumberAxis                           by fxid()

    private var lastRow = 1
    private var lastColumn = 0
    private lateinit var t0: Instant
    private lateinit var task: GeneticTask<C>

    private val averageFitnessByGeneration: MutableList<Double> = mutableListOf()
    private val bestFitnessByGeneration: MutableList<Double> = mutableListOf()

    init {
        cmbGenerations.items = values
        cmbGenerations.value = 100

        cmbPopulation.items = values
        cmbPopulation.value = 100

        cmbMutationFactor.items = mutationFactors
        cmbMutationFactor.value = 0.1

        cmbSelectionOperator.items = FXCollections.observableArrayList(SelectionOperatorChoice.values().toList())
        cmbSelectionOperator.value = SelectionOperatorChoice.TRUNCATE
        cmbSelectionOperator.converter = SelectionOperatorConverter()

        lineChartFitness.yAxis.isTickMarkVisible = false
        yAxisChartFitness.onMouseClicked = EventHandler { yAxisClicked(it) }
        yAxisChartFitness.onScroll = EventHandler { yAxisScrolled(it) }

        primaryStage.icons.add(Image("/view/dna_helix_genetic_gene-32.png"))

        configureLog(DEBUG)
    }

    private fun yAxisScrolled(event: ScrollEvent?) {

        fun adjust(signal: Int) {
            val newLowerBound = yAxisChartFitness.lowerBound + signal * 0.1
            if(newLowerBound >= 0.0 && newLowerBound < 1.0 ) {
                yAxisChartFitness.lowerBound = newLowerBound
                yAxisChartFitness.tickUnit = (yAxisChartFitness.upperBound - yAxisChartFitness.lowerBound) / 10
            }
        }

        if (event!!.deltaY > 0.0) {
            adjust(+1)
        } else if (event.deltaY < 0.0) {
            adjust(-1)
        }
    }

    private fun yAxisClicked(event: MouseEvent) {
        if (event.clickCount == 2) {
            yAxisChartFitness.lowerBound = 0.0
            yAxisChartFitness.tickUnit = (yAxisChartFitness.upperBound - yAxisChartFitness.lowerBound) / 10
        }
    }

    private fun disableInputComponents(disable: Boolean) {
        btnStart.isDisable = disable
        btnReset.isDisable = disable
        btnStop.isDisable = !disable
        pnlInput.children.filtered { it != pnlButtons }.forEach { it.isDisable = disable }
    }

    private fun showError(e: IllegalStateException) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Validation Error!"
        alert.headerText = "Can not proceed Processing"
        alert.contentText = e.message
        alert.showAndWait()
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

    private fun makeBind(task: GeneticTask<C>, averageSeries: XYChart.Series<Int, Double>, bestSeries: XYChart.Series<Int, Double>) {
        lblGeneration.textProperty().bind(task.generationProperty)
        lblBestFitness.textProperty().bind(task.bestFitnessProperty)
        lblTime.textProperty().bind(task.timeProperty)
        lblAverageFitness.textProperty().bind(task.averageFitnessProperty)
        averageSeries.dataProperty().bind(task.averageData)
        bestSeries.dataProperty().bind(task.bestData)
    }

    protected abstract fun validate()

    protected abstract fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<G, C>

    protected abstract fun fillOwnComponent(genome: List<C>)

    protected abstract fun resetComponents()

    protected fun addComponent(title: String, component: Node, colspan: Int = 1) {
        assert(colspan <= 5) // , () -> {"Colspan must be at least 5. It was $colspan"})

        val panel = FlowPane(Orientation.VERTICAL)
        panel.styleClass.add("panel-control")
        panel.add(Label(title))
        panel.add(component)

        if (lastColumn + colspan > maxColumns) {
            lastRow += 1
            lastColumn = 0
        }

        pnlInput.add(panel, lastColumn, lastRow, colspan, 1)
        lastColumn += colspan
    }

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType.ended) {
            disableInputComponents(false)
        }
    }

    fun startProcessing() {
        try {
            validate()

            val environment: Environment<G, C> = getEnvironment(cmbGenerations.value, cmbPopulation.value, cmbMutationFactor.value)
            disableInputComponents(true)
            val operatorChoice = cmbSelectionOperator.value
            val selectionOperator = operatorChoice.chooseSelectionOperator(environment)
            val processor = processorChoice.newInstance(environment, selectionOperator)
            processor.addListener(this)

            val (averageSeries, bestSeries) = createSeries()

            task = GeneticTask(processor, this::fillOwnComponent)

            makeBind(task, averageSeries, bestSeries)

            Thread(task).start()
        } catch (e: IllegalStateException) {
            showError(e)
        }
    }

    fun stopProcessing() {
        task.cancel(true)
        disableInputComponents(false)
    }

    fun reset() {
        cmbSelectionOperator.selectionModel.selectFirst()
        cmbGenerations.value = 100
        cmbMutationFactor.value = 0.1
        cmbPopulation.value = 100

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

        resetComponents()
    }
}

class SelectionOperatorConverter : StringConverter<SelectionOperatorChoice>() {

    override fun toString(choice: SelectionOperatorChoice?): String = choice!!.description

    override fun fromString(string: String?): SelectionOperatorChoice = SelectionOperatorChoice.values().first { it.description == string }

}