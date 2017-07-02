package rafael.ktgenetic.fx

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.chart.LineChart
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.util.StringConverter
import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.Environment
import rafael.ktgenetic.processor.GeneticProcessorChoice
import rafael.ktgenetic.selection.SelectionOperatorChoice
import tornadofx.*


abstract class GeneticView<G, C : Chromosome<G>>(title: String, val processorChoice: GeneticProcessorChoice) : View(title) {
    override val root: BorderPane by fxml("/view/Genetic.fxml")

    private val values: ObservableList<Int> = FXCollections.observableArrayList(1, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
    private val mutationFactors: ObservableList<Double> = FXCollections.observableArrayList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
    private val maxColumns = 5

    protected val pnlInput: GridPane                                    by fxid()
    private val pnlOutput: GridPane                                     by fxid()
    private val pnlButtons: GridPane                                    by fxid()
    private val cmbGenerations: ComboBox<Int>                           by fxid()
    private val cmbPopulation: ComboBox<Int>                            by fxid()
    private val cmbMutationFactor: ComboBox<Double>                     by fxid()
    private val cmbSelectionOperator: ComboBox<SelectionOperatorChoice> by fxid()
    private val btnStop: Button                                         by fxid()
    private val btnStart: Button                                        by fxid()
    private val lblGeneration: Label                                    by fxid()
    private val lblBestFitness: Label                                   by fxid()
    private val lblTime: Label                                          by fxid()
    private val lineChartFitness: LineChart<Int, Double>                by fxid()

    private var lastRow = 1
    private var lastColumn = 0

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
    }

    private fun disableInputComponents(disable: Boolean) {
        btnStart.isDisable = disable
        btnStop.isDisable = !disable
        pnlInput.children.filtered { it != pnlButtons }.forEach { it.isDisable = disable }
    }

    protected abstract fun validate()

    protected abstract fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<G, C>

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

    fun startProcessing() {
        try {
            validate()

            val environment: Environment<G, C> = getEnvironment(cmbGenerations.value, cmbPopulation.value, cmbMutationFactor.value)
            disableInputComponents(true)
            val selectionOperator = cmbSelectionOperator.value.chooseSelectionOperator(environment)
            val processor = processorChoice.newInstance(environment, selectionOperator)
        } catch (e: IllegalStateException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Validation Error!"
            alert.headerText = "Can not proceed Processing"
            alert.contentText = e.message
            alert.showAndWait()
        }
    }

    fun stopProcessing() {
        disableInputComponents(false)
    }
}

class SelectionOperatorConverter : StringConverter<SelectionOperatorChoice>() {

    override fun toString(choice: SelectionOperatorChoice?): String = choice!!.description

    override fun fromString(string: String?): SelectionOperatorChoice = SelectionOperatorChoice.values().first { it.description == string }

}