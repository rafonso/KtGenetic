package rafael.ktgenetic.fx

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.Stage
import rafael.ktgenetic.core.utils.LogLevel.INFO
import rafael.ktgenetic.core.*
import rafael.ktgenetic.core.events.*
import rafael.ktgenetic.core.events.waitingEvent
import rafael.ktgenetic.core.processor.GeneticCrossingType
import rafael.ktgenetic.core.processor.GeneticProcessor
import rafael.ktgenetic.core.selection.SelectionOperatorChoice
import rafael.ktgenetic.core.utils.configureLog
import tornadofx.*
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

internal fun GenerationEvent.isEvaluating() =
    ((this.eventType == TypeProcessorEvent.GENERATION_EVALUATING) || this.eventType.ended)

abstract class GeneticView<G, C : Chromosome<G>>(title: String, private val crossingType: GeneticCrossingType) :
    View(title), ProcessorListener, ChangeListener<GenerationEvent> {
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
    private val cmbUpdateChart          : ComboBox<Int>                     by fxid()
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
        cmbUpdateChart,
        lineChartFitness,
        yAxisChartFitness
    )

    protected val selectedOperator = inputsView.selectionOperator

    private val processorEventProperty = waitingEvent().toProperty()
    private var processorEvent by processorEventProperty

    init {
        primaryStage.icons.add(geneticIcon)

        @Suppress("LeakingThis")
        processorEventProperty.addListener(this)
        processorEventProperty.addListener(inputsView)
        processorEventProperty.addListener(statisticsView)

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

    private fun showError(event: ProcessorEvent<*>) {
        val error = event.error!!

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        error.printStackTrace(pw)
        val stackTrace = sw.toString()

        Alert(Alert.AlertType.ERROR).also {
            it.title = "Execution Error at generation ${event.generation}"
            it.headerText = if (error.message == null) "" else error.message
            it.initOwner(super.primaryStage)
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

    override fun onEvent(event: GenerationEvent) {
        processorEvent = event
    }

    fun startProcessing() {
        try {
            validate()

            inputsView.enabledComponents = alwaysEnabledComponents()
            inputsView.disableInputComponents(true)
            val operatorChoice = inputsView.selectionOperator

            val environment: Environment<G, C> =
                getEnvironment(inputsView.generations, inputsView.population, inputsView.mutationFactor)
            val selectionOperator = operatorChoice.chooseSelectionOperator(
                environment,
                inputsView.hasElitism,
                inputsView.hasRepeatedValues
            )
            val processor = GeneticProcessor(crossingType, environment, selectionOperator)
            processor.addListener(this)

            task = GeneticTask(processor)

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

        processorEvent = waitingEvent()
    }

    override fun changed(
        observable: ObservableValue<out GenerationEvent>?,
        oldValue: GenerationEvent?,
        newValue: GenerationEvent?
    ) {
        val event = newValue!!
        when {
            event.eventType == TypeProcessorEvent.ERROR ->
                runLater { showError(event) }
            event.isEvaluating() ->
                runLater {
                    @Suppress("UNCHECKED_CAST")
                    fillOwnComponent(event.population as List<C>)
                }
        }

    }

}
