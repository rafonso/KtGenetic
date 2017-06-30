package rafael.ktgenetic.fx

import javafx.scene.chart.LineChart
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.layout.BorderPane
import tornadofx.*

class GeneticViewApp : App(GeneticView::class)

class GeneticView : View("Border Pane Builder Test") {
    override val root: BorderPane by fxml("/view/Genetic.fxml")

    val spnGenerations: Spinner<Int>                by fxid()
    val spnPopulation: Spinner<Int>                 by fxid()
    val spnMutationFactor: Spinner<Double>          by fxid()
    val btnStop: Button                             by fxid()
    val btnStart: Button                            by fxid()
    val lblGeneration: Label                        by fxid()
    val lblBestFitness: Label                       by fxid()
    val lblTime: Label                              by fxid()
    val lineChartFitness: LineChart<Int, Double>    by fxid()

    init {
        spnGenerations.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 100, 100)
        spnPopulation.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 100, 100)
        spnMutationFactor.valueFactory = SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, 0.1, 0.1)
        lineChartFitness.yAxis.isTickMarkVisible = false
    }

}