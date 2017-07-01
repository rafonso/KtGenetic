package rafael.ktgenetic.fx

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.LineChart
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.util.StringConverter
import rafael.ktgenetic.selection.SelectionOperatorChoice
import tornadofx.*


abstract class GeneticView(title: String) : View(title) {
    override val root: BorderPane by fxml("/view/Genetic.fxml")

    private val values: ObservableList<Int> = FXCollections.observableArrayList(1, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
    private val mutationFactors: ObservableList<Double> = FXCollections.observableArrayList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)

    val pnlInput: GridPane                                      by fxid()
    val pnlOutput: GridPane                                     by fxid()
    val pnlButtons: GridPane                                    by fxid()
    val cmbGenerations: ComboBox<Int>                           by fxid()
    val cmbPopulation: ComboBox<Int>                            by fxid()
    val cmbMutationFactor: ComboBox<Double>                     by fxid()
    val cmbSelectionOperator: ComboBox<SelectionOperatorChoice> by fxid()
    val btnStop: Button                                         by fxid()
    val btnStart: Button                                        by fxid()
    val lblGeneration: Label                                    by fxid()
    val lblBestFitness: Label                                   by fxid()
    val lblTime: Label                                          by fxid()
    val lineChartFitness: LineChart<Int, Double>                by fxid()

    init {
        cmbGenerations.items = values
        cmbGenerations.value = 100
        cmbPopulation.items = values
        cmbPopulation.value = 100
        cmbMutationFactor.items = mutationFactors
        cmbMutationFactor.value = 0.1
        lineChartFitness.yAxis.isTickMarkVisible = false
        cmbSelectionOperator.items = FXCollections.observableArrayList(SelectionOperatorChoice.values().toList())
        cmbSelectionOperator.converter = SelectionOperatorConverter()

    }

    private fun disableInputComponents(disable: Boolean) {
        btnStart.isDisable = disable
        btnStop.isDisable = !disable
        pnlInput.children.filtered { it != pnlButtons }.forEach { it.isDisable = disable }
    }

    fun startProcessing() {
        println("START")
        disableInputComponents(true)
        println(cmbSelectionOperator.value)
    }

    fun stopProcessing() {
        println("STOP")
        disableInputComponents(false)
    }
}

class SelectionOperatorConverter : StringConverter<SelectionOperatorChoice>() {

    override fun toString(choice: SelectionOperatorChoice?): String = choice!!.description

    override fun fromString(string: String?): SelectionOperatorChoice = SelectionOperatorChoice.values().first { it.description == string }

}