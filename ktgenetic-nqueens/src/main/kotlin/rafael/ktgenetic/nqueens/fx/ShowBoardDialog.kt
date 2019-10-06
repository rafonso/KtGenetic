package rafael.ktgenetic.nqueens.fx

import javafx.geometry.Pos
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.GridPane
import rafael.ktgenetic.nqueens.Board
import tornadofx.*

class ShowBoardDialog(private val board: Board) : Dialog<Unit>() {

    private val queen = "\u2655"

    private val whiteHouseColor = "gray"
    private val blackHouseColor = "lightgray"

    private val houseSide = 20

    private val houseStyle = "-fx-pref-width : ${houseSide}px; -fx-pref-height: ${houseSide}px; " +
            "-fx-text-alignment: center; -fx-background-color: %s;"

    init {
        Board.printBoard(board)
        super.setTitle("Board")
        super.setHeaderText("Collisions: ${board.collisions}")

        val boardPane = gridpane {
            vgap = 0.0
            hgap = 0.0
            padding = insets(10)
            minHeight = (board.content.size + 2.0) * houseSide
            minWidth = minHeight
            maxWidth = board.content.size * 20.0 * houseSide
            maxHeight = maxWidth
            alignment = Pos.CENTER
        }
        (this.board.content).forEachIndexed { row, queenPos -> fillRow(boardPane, row, queenPos) }

        super.getDialogPane().content = boardPane

        val btnClose = ButtonType("OK", ButtonBar.ButtonData.OK_DONE)
        super.getDialogPane().buttonTypes += btnClose
    }

    private fun fillRow(boardPane: GridPane, row: Int, queenPos: Int) {
        val colors = if (row % 2 == 0) arrayOf(blackHouseColor, whiteHouseColor) else arrayOf(whiteHouseColor, blackHouseColor)
        (this.board.content.indices)
                .map { col ->
                    label {
                        style = houseStyle.format(colors[col % 2])
                        text = if (col == queenPos) queen else ""
                        alignment = Pos.CENTER
                    }
                }.forEachIndexed { col, label ->
                    boardPane.add(label, col, row)
                }
    }

}