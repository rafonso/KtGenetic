package rafael.ktgenetic.pallete.fx

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
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

    private lateinit var dimensions: PalleteDimensions

    private val gridBestPallete = GridPane()

    private val cmPattern = "Center of Mass: (%.2f, %.2f)"
    private val lblCm = Label()

    private val miPattern = "Moment of Inertia: %.2f"
    private val lblMi = Label()

    private val fbhmPattern = "Front/Back Half masses: %d / %d"
    private val lblFbhm = Label()

    private val rlhmPattern = "Roght/Left Half masses: %d / %d"
    private val lblRlhm = Label()

    // CM = (1,95, 1,96), MI = 290,500, FBHM = (98, 99), RLHM = (99, 98)

    init {
        cmbRows.value = 0
        addComponent("Rows", cmbRows)

        cmbCols.value = 0
        addComponent("Columns", cmbCols)

        txfPallete.prefWidth = 600.0
        addComponent("Initial Configuration", txfPallete, 3)

        val pnlBestPalleteGrid = FlowPane()
        pnlBestPalleteGrid.alignment = Pos.TOP_CENTER
        pnlBestPalleteGrid.add(gridBestPallete)

        val pnlBestPalleteData = HBox()
        pnlBestPalleteData.add(lblCm)
        pnlBestPalleteData.add(lblMi)
        pnlBestPalleteData.add(lblFbhm)
        pnlBestPalleteData.add(lblRlhm)

        val pnlBest = BorderPane()
        pnlBest.center = pnlBestPalleteGrid
        pnlBest.bottom = pnlBestPalleteData

        root.center = pnlBest
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

        lblCm.text = cmPattern.format(bestPallete.centerOfMass.col, bestPallete.centerOfMass.row)
        lblMi.text = miPattern.format(bestPallete.momentOfInertia)
        lblFbhm.text = fbhmPattern.format(bestPallete.frontBackHalfMasses.first, bestPallete.frontBackHalfMasses.second)
        lblRlhm.text = rlhmPattern.format(bestPallete.rightLeftHalfMasses.first, bestPallete.rightLeftHalfMasses.second)
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
        val bestPallete = genome[0]
        handleBestPallete(bestPallete)
    }

    override fun resetComponents() {
        cmbRows.value = 0
        cmbCols.value = 0
        txfPallete.text = ""
        gridBestPallete.clear()
    }

}