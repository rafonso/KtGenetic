package rafael.ktgenetic.fx

import javafx.beans.property.ReadOnlyProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.util.StringConverter
import rafael.ktgenetic.*
import rafael.ktgenetic.LogLevel.INFO
import rafael.ktgenetic.processor.GeneticCrossingType
import rafael.ktgenetic.processor.GeneticProcessor
import rafael.ktgenetic.selection.SelectionOperatorChoice
import tornadofx.View
import java.util.*

abstract class GeneticView<G, C : Chromosome<G>>(title: String, private val crossingType: GeneticCrossingType) :
    View(title), ProcessorListener {
    final override val root: BorderPane by fxml("/view/Genetic.fxml")

    private val values: ObservableList<Int> =
        FXCollections.observableArrayList(
            1,
            10,
            50,
            100,
            200,
            300,
            400,
            500,
            600,
            700,
            800,
            900,
            1000,
            1500,
            2000,
            2500,
            3000,
            3500,
            4000
        )

    private val mutationFactors: ObservableList<Double> =
        FXCollections.observableArrayList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)

    private val maxColumns = 6

    // @formatter:off
    private val pnlInput                : GridPane                          by fxid()
    private val pnlOutput               : Pane                              by fxid()
    private val pnlButtons              : GridPane                          by fxid()
    private val cmbGenerations          : ComboBox<Int>                     by fxid()
    private val cmbPopulation           : ComboBox<Int>                     by fxid()
    private val cmbMutationFactor       : ComboBox<Double>                  by fxid()
    private val cmbSelectionOperator    : ComboBox<SelectionOperatorChoice> by fxid()
    private val chbElitism              : CheckBox                          by fxid()
    private val chbRepeatedValues       : CheckBox                          by fxid()
    private val btnStop                 : Button                            by fxid()
    private val btnReset                : Button                            by fxid()
    private val btnStart                : Button                            by fxid()
    private val lblGeneration           : Label                             by fxid()
    private val lblBestFitness          : Label                             by fxid()
    private val lblAverageFitness       : Label                             by fxid()
    private val lblTime                 : Label                             by fxid()
    private val lineChartFitness        : LineChart<Int, Double>            by fxid()
    private val yAxisChartFitness       : NumberAxis                        by fxid()
    // @formatter:on

    private var lastRow = 1
    private var lastColumn = 0
    private lateinit var task: GeneticTask<C>

    protected val selectedOperator = cmbSelectionOperator.valueProperty() as ReadOnlyProperty<SelectionOperatorChoice>

    private var statisticsView: StatisticsView = StatisticsView(
        pnlOutput,
        lblGeneration,
        lblBestFitness,
        lblAverageFitness,
        lblTime,
        lineChartFitness,
        yAxisChartFitness
    )

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

        primaryStage.icons.add(geneticIcon)

        configureLog(INFO)
    }

    private fun disableInputComponents(disable: Boolean) {
        btnStart.isDisable = disable
        btnReset.isDisable = disable
        btnStop.isDisable = !disable
        pnlInput.children
            .filtered { it != pnlButtons }
            .filtered { !alwaysEnabledComponents().contains(it) }
            .forEach { it.isDisable = disable }
    }

    private fun showValidationError(e: IllegalStateException) {
        Alert(Alert.AlertType.ERROR).also {
            it.title = "Validation Error!"
            it.headerText = "Can not proceed Processing"
            it.contentText = e.message
            (it.dialogPane.scene.window as Stage).icons.add(geneticIcon)
        }.showAndWait()
    }

    protected abstract fun validate()

    protected abstract fun getEnvironment(
        maxGenerations: Int,
        generationSize: Int,
        mutationFactor: Double
    ): Environment<G, C>

    protected abstract fun fillOwnComponent(genome: List<C>)

    protected abstract fun resetComponents()

    /**
     * Returns the [Node]s that will not be disabled while processing.
     */
    protected open fun alwaysEnabledComponents() = emptyList<Node>()

    protected fun addComponent(title: String, component: Node, colspan: Int = 1): Label {
        assert(colspan <= maxColumns) { "Colspan must be at least $maxColumns. It was $colspan" }

        val panel = FlowPane(Orientation.VERTICAL)
        panel.styleClass.add("panel-control")
        val lblTitle = Label(title)
        panel.add(lblTitle)
        panel.add(component)

        if (lastColumn + colspan > maxColumns) {
            lastRow += 1
            lastColumn = 0
        }

        pnlInput.add(panel, lastColumn, lastRow, colspan, 1)
        lastColumn += colspan

        return lblTitle
    }

    protected fun addComponent(component: Node, colspan: Int = 1): Label = addComponent("", component, colspan)

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType.ended) {
            disableInputComponents(false)
        }
    }

    fun startProcessing() {
        try {
            validate()

            val environment: Environment<G, C> =
                getEnvironment(cmbGenerations.value, cmbPopulation.value, cmbMutationFactor.value)
            disableInputComponents(true)
            val operatorChoice = cmbSelectionOperator.value
            val selectionOperator = operatorChoice.chooseSelectionOperator(
                environment,
                chbElitism.isSelected,
                chbRepeatedValues.isSelected
            )
            val processor = GeneticProcessor(crossingType, environment, selectionOperator)
            processor.addListener(this)

            task = GeneticTask(processor, super.primaryStage, this::fillOwnComponent)

            statisticsView.bind(task)

            Thread(task, "%s-%tT".format(environment.javaClass.simpleName, Date())).start()
        } catch (e: IllegalStateException) {
            showValidationError(e)
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

        statisticsView.reset()

        resetComponents()
    }

}

class SelectionOperatorConverter : StringConverter<SelectionOperatorChoice>() {

    override fun toString(choice: SelectionOperatorChoice?): String = choice!!.description

    override fun fromString(string: String?): SelectionOperatorChoice =
        SelectionOperatorChoice.values().first { it.description == string }

}
