package rafael.ktgenetic.balancedtable.fx

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import rafael.ktgenetic.Environment
import rafael.ktgenetic.balancedtable.Balance
import rafael.ktgenetic.balancedtable.BalanceEnvironment
import rafael.ktgenetic.balancedtable.Box
import rafael.ktgenetic.fx.ChomosomeToFitnessCellString
import rafael.ktgenetic.fx.ChromosomeToCellString
import rafael.ktgenetic.fx.GeneticView
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

    val txfBalance: TextField = TextField()

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
        val fitnessColumn = TableColumn<Balance, String>("Fitness")
        fitnessColumn.prefWidth = 50.0
        fitnessColumn.cellValueFactory = ChomosomeToFitnessCellString()
        fitnessColumn.styleClass.add("mono")

        val balanceColumn = TableColumn<Balance, String>("Balance")
        balanceColumn.prefWidth = 500.0
        balanceColumn.cellValueFactory = ChromosomeToCellString({ c -> c.content.map { it.value }.
                joinToString(separator = " ", transform = { "%3d".format(it) }) })
        balanceColumn.styleClass.add("mono")

        val cmColumn = TableColumn<Balance, String>("CM")
        cmColumn.prefWidth = 50.0
        cmColumn.cellValueFactory = ChromosomeToCellString({ c -> "%.3f".format((c as Balance).centerOfMass) })
        cmColumn.styleClass.add("mono")

        val miColumn = TableColumn<Balance, String>("MI")
        miColumn.prefWidth = 75.0
        miColumn.cellValueFactory = ChromosomeToCellString({ c -> "%2.3f".format((c as Balance).momentOfInertia) })
        miColumn.styleClass.add("mono")

        val hmColumn = TableColumn<Balance, String>("HM")
        hmColumn.prefWidth = 100.0
        hmColumn.cellValueFactory = ChromosomeToCellString({ c -> (c as Balance).halfMasses.toString() })
        hmColumn.styleClass.add("mono")

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
