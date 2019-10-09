package rafael.ktgenetic.nqueens.fx

import javafx.collections.FXCollections
import javafx.scene.control.Control
import javafx.scene.control.Spinner
import javafx.scene.control.TableView
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

    private val numberOfRowsSelector: Spinner<Int> = spinner(min = 5, max = 200, initialValue = 8, enableScroll = true)

    // OUTPUT COMPONENTS

    private val boardTable: TableView<Board> = tableview {
        val classes = listOf("mono")
        val fitnessColumn = fitnessToTableColumn<Int, Board>(50.0, classes)
        val collisionsColumn = chromosomeToTableColumn<Int, Board>("Collisions", 100.0, classes) { board ->
            board.numOfCollisions.toString()
        }
        val boardColumn = chromosomeToTableColumn<Int, Board>("Board", 600.0, classes) { board ->
            board.content.joinToString(separator = " ", transform = { col -> rowNumberFormat.format(col) })
        }

        onUserSelect { selectedBoard ->
            ShowBoardDialog(selectedBoard).showAndWait()
        }
        prefWidth = Control.USE_COMPUTED_SIZE
        columns.addAll(fitnessColumn, collisionsColumn, boardColumn)
    }

    private var rowNumberFormat: String = ""


    init {
        addComponent("Board Size", numberOfRowsSelector)

        root.center = borderpane {
            paddingAll = 10.0
            center = boardTable
        }
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