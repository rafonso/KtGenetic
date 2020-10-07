package rafael.ktgenetic.fx

import javafx.scene.Node
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
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

    // @formatter:off
    private val pnlInput                : GridPane                          by fxid()
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
    private val pnlOutput               : Pane                              by fxid()
    private val lblGeneration           : Label                             by fxid()
    private val lblBestFitness          : Label                             by fxid()
    private val lblAverageFitness       : Label                             by fxid()
    private val lblTime                 : Label                             by fxid()
    private val lineChartFitness        : LineChart<Int, Double>            by fxid()
    private val yAxisChartFitness       : NumberAxis                        by fxid()
    // @formatter:on

    private lateinit var task: GeneticTask<C>

    private val inputsView: InputsView = InputsView(
        pnlInput,
        pnlButtons,
        cmbGenerations,
        cmbPopulation,
        cmbMutationFactor,
        cmbSelectionOperator,
        chbElitism,
        chbRepeatedValues,
        btnStop,
        btnReset,
        btnStart
    )

    private val statisticsView: StatisticsView = StatisticsView(
        pnlOutput,
        lblGeneration,
        lblBestFitness,
        lblAverageFitness,
        lblTime,
        lineChartFitness,
        yAxisChartFitness
    )

    protected val selectedOperator = inputsView.selectionOperator

    init {
        primaryStage.icons.add(geneticIcon)

        configureLog(INFO)
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

    protected fun addComponent(title: String, component: Node, colspan: Int = 1): Label =
        inputsView.addComponent(title, component, colspan)

    protected fun addComponent(component: Node, colspan: Int = 1): Label =
        inputsView.addComponent("", component, colspan)

    override fun onEvent(event: ProcessorEvent<*>) {
        if (event.eventType.ended) {
            inputsView.disableInputComponents(false)
        }
    }

    fun startProcessing() {
        try {
            validate()

            val environment: Environment<G, C> =
                getEnvironment(inputsView.generations, inputsView.population, inputsView.mutationFactor)
            inputsView.disableInputComponents(true, alwaysEnabledComponents())
            val operatorChoice = inputsView.selectionOperator
            val selectionOperator = operatorChoice.chooseSelectionOperator(
                environment,
                inputsView.hasElitism,
                inputsView.hasRepeatedValues
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
        inputsView.disableInputComponents(false)
    }

    fun reset() {
        inputsView.reset()
        statisticsView.reset()
        resetComponents()
    }

}

