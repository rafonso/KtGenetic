package rafael.ktgenetic.balancedtable.fx

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.Control
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import rafael.ktgenetic.Environment
import rafael.ktgenetic.balancedtable.Balance
import rafael.ktgenetic.balancedtable.BalanceEnvironment
import rafael.ktgenetic.balancedtable.Box
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.fx.chromosomeToTableColumn
import rafael.ktgenetic.fx.fitnessToTableColumn
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

/**
 * Gradient from white to red. extracted from http://www.perbang.dk/rgbgradient/
 */
private val gradients = listOf(
        Pair("FFFFFF", "000000"),
        Pair("FFE5E5", "000000"),
        Pair("FFCCCC", "000000"),
        Pair("FFB2B2", "000000"),
        Pair("FF9999", "000000"),
        Pair("FF7F7F", "000000"),
        Pair("FF6666", "000000"),
        Pair("FF4C4C", "000000"),
        Pair("FF3333", "FFFFFF"),
        Pair("FF1919", "FFFFFF"),
        Pair("FF0000", "FFFFFF")
)

class BalanceViewApp : App(BalanceView::class)

class BalanceView : GeneticView<Box, Balance>("Balance", GeneticProcessorChoice.ORDERED) {

    // INPUT COMPONENTS

    val txfBalance: TextField = TextField()

    // OUTPUT COMPONENTS

    val pnlBestBalance: Pane = HBox(10.0)

    val balanceTable: TableView<Balance> = TableView()

    val colorsByBox: MutableMap<Int, Pair<String, String>> = mutableMapOf()

    init {
        txfBalance.prefWidth = 600.0
        addComponent("Initial Configuration", txfBalance, 5)

        fillBalanceTable()

        pnlBestBalance.prefHeight = 20.0

        val pnlBalance = BorderPane()
        pnlBalance.padding = Insets(10.0, 10.0, 10.0, 10.0)
        pnlBalance.top = pnlBestBalance
        pnlBalance.center = balanceTable

        root.center = pnlBalance
    }

    private fun fillBalanceTable() {
        val classes = listOf("mono")

        val fitnessColumn = fitnessToTableColumn<Box, Balance>(50.0, classes)

        val balanceColumn = chromosomeToTableColumn<Box, Balance>("Balance",
                { c -> c.content.map { it.value }.joinToString(separator = " ", transform = { "%3d".format(it) }) },
                500.0,
                classes)

        val cmColumn = chromosomeToTableColumn<Box, Balance>("CM",
                { c -> "%.3f".format(c.centerOfMass) },
                50.0,
                classes)

        val miColumn = chromosomeToTableColumn<Box, Balance>("MI",
                { c -> "%2.3f".format(c.momentOfInertia) },
                75.0,
                classes)

        val hmColumn = chromosomeToTableColumn<Box, Balance>("HM",
                { c -> c.halfMasses.toString() },
                100.0,
                classes)

        balanceTable.prefWidth = Control.USE_COMPUTED_SIZE
        balanceTable.columns.addAll(fitnessColumn, cmColumn, miColumn, hmColumn, balanceColumn)
    }

    private fun fillColorsByBox(weights: List<Int>) {
        val max = weights.max()!!.toDouble()
        colorsByBox.clear()
        weights.forEach { colorsByBox.put(it, gradients[((it / max) * 10).toInt()]) }
    }

    override fun resetComponents() {
        txfBalance.text = ""
        pnlBestBalance.clear()
        balanceTable.items = FXCollections.emptyObservableList()
    }

    override fun validate() {
        if (!txfBalance.text.matches(Regex("^((\\d+) *)+$"))) {
            throw IllegalStateException("Weights incorrect. " +
                    "Please provide the weights to be processed (ex: \"2 4 10 0 5 2\")")
        }
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Box, Balance> {
        val weights = txfBalance.text.trim().split(Regex(" +")).map { Integer.parseInt(it) }
        fillColorsByBox(weights)

        return BalanceEnvironment(
                weights, //
                maxGenerations,
                generationSize,
                mutationFactor)
    }

    private fun boxToLabel(b: Box): Label {
        val label = Label("%3d".format(b.value))
        val (back, front) = colorsByBox[b.value]!!
        label.style = "-fx-background-color: #$back; -fx-text-fill: #$front;"
        label.styleClass.add("mono-right")

        return label
    }

    override fun fillOwnComponent(genome: List<Balance>) {
        balanceTable.items = FXCollections.observableArrayList(genome)
        pnlBestBalance.clear()
        genome[0].content.forEach { pnlBestBalance.add(boxToLabel(it)) }
    }

}
//   0  15 17  18  22  24  26 27 19 29  28 25  11  3  2 20   4   5   8  1 23 21 16 14  13   9 10   7  12   6
// 152 190 41 151 149 122 174 50 22 45 195 93 167 52 14 96 124 167 140 99 35 39 70 74 155 197 47 128 171 178
