package rafael.ktgenetic.nqueens.fx

import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Control
import javafx.scene.control.Spinner
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.BorderPane
import javafx.util.Callback
import rafael.ktgenetic.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.fx.chromosomeToTableColumn
import rafael.ktgenetic.fx.fitnessToTableColumn
import rafael.ktgenetic.nqueens.Board
import rafael.ktgenetic.nqueens.BoardEnvironment
import rafael.ktgenetic.processor.GeneticProcessorChoice
import tornadofx.*

class NQueensApp : App(NQueensView::class)

class NQueensView : GeneticView<Int, Board>("N Queens", GeneticProcessorChoice.ORDERED) {

    // INPUT COMPONENTS

    private val numberOfRowsSelector = Spinner<Int>(5, 200, 8)

    // OUTPUT COMPONENTS

    private val boardTable: TableView<Board> = TableView()

    private var rowNumberFormat: String = ""


    init {
        numberOfRowsSelector.onScroll = EventHandler<ScrollEvent> { event ->
            val delta = if (event.deltaY > 0) 1 else -1
            val step = if (event.isControlDown) 10 else 1

            numberOfRowsSelector.increment(delta * step)
        }
        addComponent("Board Size", numberOfRowsSelector)

        val classes = listOf("mono")
        val fitnessColumn = fitnessToTableColumn<Int, Board>(50.0, classes)
        val collisionsColumn = chromosomeToTableColumn<Int, Board>("Collisions",
                { it.collisions.toString() }, 100.0, classes)
        val boardColumn = chromosomeToTableColumn<Int, Board>("Board",
                { it.content.joinToString(separator = " ", transform = { col -> rowNumberFormat.format(col) }) }, 500.0, classes)

        boardTable.prefWidth = Control.USE_COMPUTED_SIZE
        boardTable.columns.addAll(fitnessColumn, collisionsColumn, boardColumn)
        boardTable.rowFactory = Callback {
            val row = TableRow<Board>()
            row.onMouseClicked = EventHandler { evt ->
                if (evt.clickCount == 2 && !row.isEmpty) {
                    showBoard(row.item)
                }
            }
            row
        }

        val pnlBest = BorderPane()
        pnlBest.padding = Insets(10.0, 10.0, 10.0, 10.0)
        pnlBest.center = boardTable

        root.center = pnlBest
    }

    private fun showBoard(board: Board) {
        ShowBoardDialog(board).showAndWait()
    }

    override fun validate() {
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Int, Board> {
        rowNumberFormat = "%${numberOfRowsSelector.value.toString().length}d"

        return BoardEnvironment(numberOfRowsSelector.value.toInt(), maxGenerations, generationSize, mutationFactor)
    }

    override fun fillOwnComponent(genome: List<Board>) {
        boardTable.items = FXCollections.observableArrayList(genome)
    }

    override fun resetComponents() {
        numberOfRowsSelector.valueFactory.value = 8
        boardTable.items = FXCollections.emptyObservableList()
    }
}