package rafael.ktgenetic.nqueens.fx

import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.Control
import javafx.scene.control.Spinner
import javafx.scene.control.TableView
import rafael.ktgenetic.core.Environment
import rafael.ktgenetic.fx.GeneticView
import rafael.ktgenetic.fx.chromosomeToTableColumn
import rafael.ktgenetic.fx.fitnessToTableColumn
import rafael.ktgenetic.nqueens.Board
import rafael.ktgenetic.nqueens.BoardEnvironment
import rafael.ktgenetic.nqueens.Piece
import rafael.ktgenetic.core.processor.GeneticCrossingType
import tornadofx.*

abstract class NPiecesView (private val piece: Piece, _title: String, _processorChoice: GeneticCrossingType): GeneticView<Int, Board>(_title, _processorChoice) {

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

        onMouseClicked = EventHandler { event ->
            if(event.clickCount == 2 && !selectionModel.isEmpty) {
                ShowBoardDialog(selectionModel.selectedItem).showAndWait()
            }
        }

        prefWidth = Control.USE_COMPUTED_SIZE
        columns.addAll(fitnessColumn, collisionsColumn, boardColumn)
    }

    private var rowNumberFormat: String = ""

    init {
        addComponent("Board Size", numberOfRowsSelector)

        this.root.center = borderpane {
            paddingAll = 10.0
            center = boardTable
        }
    }

    override fun validate() {
    }

    override fun getEnvironment(maxGenerations: Int, generationSize: Int, mutationFactor: Double): Environment<Int, Board> {
        rowNumberFormat = "%${numberOfRowsSelector.value.toString().length}d"

        return BoardEnvironment(numberOfRowsSelector.value.toInt(), piece, maxGenerations, generationSize, mutationFactor)
    }

    override fun fillOwnComponent(genome: List<Board>) {
        boardTable.items = FXCollections.observableArrayList(genome)
    }

    override fun resetComponents() {
        numberOfRowsSelector.valueFactory.value = 8
        boardTable.items = FXCollections.emptyObservableList()
    }

}
