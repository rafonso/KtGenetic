package rafael.ktgenetic.balancedtable.fx

import javafx.collections.FXCollections
import javafx.scene.control.Control
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import rafael.ktgenetic.Environment
import rafael.ktgenetic.balancedtable.Balance
import rafael.ktgenetic.balancedtable.BalanceEnvironment
import rafael.ktgenetic.balancedtable.Box
import rafael.ktgenetic.fx.ChomosomeToFitnessCellString
import rafael.ktgenetic.fx.ChromosomeToCellString
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*


class BalanceViewApp : App(BalanceView::class)

class BalanceView : GeneticView<Box, Balance>("Balance", GeneticProcessorChoice.ORDERED) {
    val balanceTable: TableView<Balance> = TableView<Balance>()

    val txfBalance: TextField = TextField()

    init {
        txfBalance.prefWidth = 600.0
        addComponent("Initial Configuration", txfBalance, 5)

        val fitnessColumn = TableColumn<Balance, String>("Fitness")
        fitnessColumn.prefWidth = 100.0
        fitnessColumn.cellValueFactory = ChomosomeToFitnessCellString()

        val balanceColumn = TableColumn<Balance, String>("Balance")
        balanceColumn.prefWidth = 500.0
        balanceColumn.cellValueFactory = ChromosomeToCellString({c -> c.content.map { it.value }.toString()})

        val cmColumn = TableColumn<Balance, String>("CM")
        cmColumn.prefWidth = 50.0
        cmColumn.cellValueFactory = ChromosomeToCellString({c  -> "%.3f".format((c as Balance).centerOfMass)})

        balanceTable.prefWidth = Control.USE_COMPUTED_SIZE
        balanceTable.columns.addAll(fitnessColumn, balanceColumn, cmColumn)

        root.center = balanceTable
    }

    override fun validate() {
        if (!txfBalance.text.matches(Regex("^((\\d+) *)+$"))) {
            throw IllegalStateException("Weights incorrect. " +
                    "Please provide the weights to be processed (ex: \"2 4 10 0 5 2\")")
        }
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Box, Balance> {
        val weights = txfBalance.text.trim().split(Regex(" +")).map { Integer.parseInt(it) }

        return BalanceEnvironment(
                weights, //
                maxGenerations,
                generationSize,
                mutationFactor)
    }

    override fun fillOwnComponent(genome: List<Balance>) {
        balanceTable.items = FXCollections.observableArrayList(genome)
    }

}
/*
000 FFFFFF
010 FFE5E5
020 FFCCCC
030 FFB2B2
040 FF9999
050 FF7F7F
060 FF6666
070 FF4C4C
080 FF3333
090 FF1919
100 FF0000
 */
// 0 15 17 18 22 24 26 27 19 29 28 25 11 3 2 20 4 5 8 1 23 21 16 14 13 9 10 7 12 6