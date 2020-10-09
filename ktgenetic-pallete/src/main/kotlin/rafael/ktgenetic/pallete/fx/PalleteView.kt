package rafael.ktgenetic.pallete.fx

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.fx.chromosomeToTableColumn
import rafael.ktgenetic.fx.fitnessToTableColumn
import rafael.ktgenetic.pallete.*
import rafael.ktgenetic.processor.GeneticCrossingType
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

class PalleteViewApp : App(PalleteView::class)

class PalleteView : GeneticView<Box, Pallete>("Pallete", GeneticCrossingType.ORDERED) {

    // INPUT COMPONENTS

    private val numbers = FXCollections.observableArrayList((0..20).toMutableList())

    private val cmbRows: ComboBox<Int> = combobox {
        items = numbers
        value = 0
    }

    private val cmbCols: ComboBox<Int> = combobox {
        items = numbers
        value = 0
    }

    private val txfPallete: TextField = textfield {
        prefWidth = 600.0
    }

    // OUTPUT COMPONENTS

    private val colorsByBox: MutableMap<Int, Pair<String, String>> = mutableMapOf()

    private val balanceTable: TableView<Pallete> = tableview {
        val classes = listOf("mono")
        val fitnessColumn = fitnessToTableColumn<Box, Pallete>(50.0, classes)
        val cmColumn = chromosomeToTableColumn<Box, Pallete>("CM", 100.0, classes) {
            it.centerOfMass.toString()
        }
        val miColumn = chromosomeToTableColumn<Box, Pallete>("MI", 50.0, classes) {
            "%.0f".format(it.momentOfInertia)
        }
        val fbhmColumn = chromosomeToTableColumn<Box, Pallete>("FBHM", 100.0, classes) {
            it.frontBackHalfMasses.toString()
        }
        val rlhmColumn = chromosomeToTableColumn<Box, Pallete>("RLHM", 100.0, classes) {
            it.rightLeftHalfMasses.toString()
        }
        val palleteColumn = chromosomeToTableColumn<Box, Pallete>("Pallete", 500.0, classes) { c ->
            c.content.map { it.value }.joinToString(separator = " ", transform = { "%3d".format(it) })
        }

        prefWidth = Control.USE_COMPUTED_SIZE
        columns.addAll(fitnessColumn, cmColumn, miColumn, fbhmColumn, rlhmColumn, palleteColumn)
    }

    private val gridBestPallete = GridPane()

    private lateinit var dimensions: PalleteDimensions

    init {
        addComponent("Rows", cmbRows)
        addComponent("Columns", cmbCols)
        addComponent("Initial Configuration", txfPallete, 3)

        root.center = borderpane {
            paddingAll = 10.0
            top = flowpane {
                alignment = Pos.TOP_CENTER
                minHeight = 100.0
                add(gridBestPallete)
            }
            center = balanceTable
        }
    }

    private fun handleBestPallete(bestPallete: Pallete) {

        fun addBoxToGrid(index: Int, box: Box): Triple<Label, Int, Int> {
            val label = label {
                val (back, front) = colorsByBox[box.value]!!

                text = "%3d".format(box.value)
                style = "-fx-background-color: #$back; -fx-text-fill: #$front;"
                styleClass += "mono-right"
            }
            val (row, col) = dimensions.indexToPosition(index)

            return Triple(label, col, row)
        }

        bestPallete.content.mapIndexed(::addBoxToGrid).forEach { (label, col, row) ->
            gridBestPallete.add(label, col, row)
        }
    }

    private fun fillColorsByBox(weights: List<Int>) {
        val max = weights.maxOrNull()!!.toDouble()
        colorsByBox.clear()
        weights.forEach { colorsByBox[it] = gradients[((it / max) * 10).toInt()] }
    }

    override fun validate() {
        if (txfPallete.text.matches(Regex("^((\\d+) *)+$"))) {
            if (cmbCols.value > 0 && cmbRows.value > 0) {
                val quantBoxes = txfPallete.text.trim().split(Regex(" +")).size
                val palleteCapacity = cmbCols.value * cmbRows.value
                check(quantBoxes <= palleteCapacity) {
                    "There are more boxes ($quantBoxes) than the Pallete capacity " +
                            "(${cmbRows.value} rows x ${cmbCols.value} columns = $palleteCapacity)"
                }
            }
        } else {
            throw IllegalStateException("Weights incorrect. " +
                    "Please provide the weights to be processed (ex: \"2 4 10 0 5 2\")")
        }
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Box, Pallete> {
        val weights = txfPallete.text.trim().split(Regex(" +")).map { Integer.parseInt(it) }
        val (normalizedPallet, palleteDimensions) = getPallete(weights, cmbRows.value, cmbCols.value)
        fillColorsByBox(normalizedPallet)
        dimensions = palleteDimensions
        gridBestPallete.clear()

        return PalleteEnvironment(normalizedPallet, palleteDimensions, //
                maxGenerations,
                generationSize,
                mutationFactor)
    }

    override fun fillOwnComponent(genome: List<Pallete>) {
        handleBestPallete(genome[0])
        balanceTable.items = FXCollections.observableArrayList(genome)
    }

    override fun resetComponents() {
        cmbRows.value = 0
        cmbCols.value = 0
        txfPallete.text = ""
        gridBestPallete.clear()
        balanceTable.items = FXCollections.emptyObservableList()
    }

}
