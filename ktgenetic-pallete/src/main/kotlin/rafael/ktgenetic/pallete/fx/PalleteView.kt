package rafael.ktgenetic.pallete.fx

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.fx.chromosomeToTableColumn
import rafael.ktgenetic.fx.fitnessToTableColumn
import rafael.ktgenetic.pallete.*
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

class PalleteViewApp : App(PalleteView::class)

class PalleteView : GeneticView<Box, Pallete>("Pallete", GeneticProcessorChoice.ORDERED) {

    // INPUT COMPONENTS

    private val numbers = FXCollections.observableArrayList((0..20).toMutableList())

    private val cmbRows: ComboBox<Int> = ComboBox(numbers)

    private val cmbCols: ComboBox<Int> = ComboBox(numbers)

    private val txfPallete: TextField = TextField()

    // OUTPUT COMPONENTS

    private val colorsByBox: MutableMap<Int, Pair<String, String>> = mutableMapOf()

    private val balanceTable: TableView<Pallete> = TableView()

    private val gridBestPallete = GridPane()

    private lateinit var dimensions: PalleteDimensions

    init {
        cmbRows.value = 0
        addComponent("Rows", cmbRows)

        cmbCols.value = 0
        addComponent("Columns", cmbCols)

        txfPallete.prefWidth = 600.0
        addComponent("Initial Configuration", txfPallete, 3)

        val pnlBestPalleteGrid = FlowPane()
        pnlBestPalleteGrid.alignment = Pos.TOP_CENTER
        pnlBestPalleteGrid.minHeight = 100.0
        pnlBestPalleteGrid.add(gridBestPallete)

        fillBalanceTable()

        val pnlBest = BorderPane()
        pnlBest.padding = Insets(10.0, 10.0, 10.0, 10.0)
        pnlBest.top = pnlBestPalleteGrid
        pnlBest.center = balanceTable

        root.center = pnlBest
    }

    private fun fillBalanceTable() {
        val classes = listOf("mono")

        val fitnessColumn = fitnessToTableColumn<Box, Pallete>(50.0, classes)

        val cmColumn = chromosomeToTableColumn<Box, Pallete>("CM",
                { it.centerOfMass.toString() },
                100.0,
                classes)

        val miColumn = chromosomeToTableColumn<Box, Pallete>("MI",
                { "%.0f".format(it.momentOfInertia) },
                50.0,
                classes)

        val fbhmColumn = chromosomeToTableColumn<Box, Pallete>("FBHM",
                { it.frontBackHalfMasses.toString() },
                100.0,
                classes)

        val rlhmColumn = chromosomeToTableColumn<Box, Pallete>("RLHM",
                { it.rightLeftHalfMasses.toString() },
                100.0,
                classes)

        val palleteColumn = chromosomeToTableColumn<Box, Pallete>("Pallete",
                { c ->
                    c.content.map { it.value }.
                            joinToString(separator = " ", transform = { "%3d".format(it) })
                },
                500.0,
                classes)

        balanceTable.prefWidth = Control.USE_COMPUTED_SIZE
        balanceTable.columns.addAll(fitnessColumn, cmColumn, miColumn, fbhmColumn, rlhmColumn, palleteColumn)
    }

    private fun handleBestPallete(bestPallete: Pallete) {

        fun addBoxToGrid(index: Int, box: Box) {
            val label = Label("%3d".format(box.value))
            val (back, front) = colorsByBox[box.value]!!
            label.style = "-fx-background-color: #$back; -fx-text-fill: #$front;"
            label.styleClass.add("mono-right")

            val (row, col) = dimensions.indexToPosition(index)

            gridBestPallete.add(label, col, row)
        }

        bestPallete.content.forEachIndexed(::addBoxToGrid)
    }

    private fun fillColorsByBox(weights: List<Int>) {
        val max = weights.max()!!.toDouble()
        colorsByBox.clear()
        weights.forEach { colorsByBox.put(it, gradients[((it / max) * 10).toInt()]) }
    }

    override fun validate() {
        if (txfPallete.text.matches(Regex("^((\\d+) *)+$"))) {
            if (cmbCols.value > 0 && cmbRows.value > 0) {
                val quantBoxes = txfPallete.text.trim().split(Regex(" +")).size
                val palleteCapacity = cmbCols.value * cmbRows.value
                if (quantBoxes > palleteCapacity) {
                    throw IllegalStateException("There are more boxes ($quantBoxes) than the Pallete capacity " +
                            "(${cmbRows.value} rows x ${cmbCols.value} columns = $palleteCapacity)")
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