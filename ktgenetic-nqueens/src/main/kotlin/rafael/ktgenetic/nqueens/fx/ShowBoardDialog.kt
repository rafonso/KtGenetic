package rafael.ktgenetic.nqueens.fx

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.GridPane
import rafael.ktgenetic.nqueens.Board
import tornadofx.*

private const val idHouseFormat = "house_%d_%d"
private const val sharpIdHouseFormat = "#$idHouseFormat"

private const val queen = "\u2655"

private const val whiteHouseColor = "lightyellow"
private const val blackHouseColor = "lightgreen"

private const val houseSide = 20

private const val houseStyle = "-fx-pref-width : ${houseSide}px; -fx-pref-height: ${houseSide}px; " +
        "-fx-text-alignment: center; -fx-background-color: %s; "

private const val houseHover     = " -fx-border-style: solid ; -fx-border-width: 1px;"
private const val houseNeighbour = " -fx-border-style: dashed; -fx-border-width: 1px;"

class ShowBoardDialog(private val board: Board) : Dialog<Unit>() {

    init {
        Board.printBoard(board)
        super.setTitle("Board")
        super.setHeaderText("Collisions: ${board.collisions}")

        val boardPane = gridpane {
            vgap = 0.0
            hgap = 0.0
            padding = insets(10)
            minHeight = (board.size + 2.0) * houseSide
            minWidth = minHeight
            maxWidth = board.size * 20.0 * houseSide
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
                        val baseStyle = houseStyle.format(colors[col % 2])
                        id = idHouseFormat.format(row, col)
                        style = baseStyle
                        text = if (col == queenPos) queen else ""
                        alignment = Pos.CENTER
                        onMouseEntered = EventHandler {
                            style = baseStyle + houseHover
                            changeHouse(row, col, true)
                        }
                        onMouseExited = EventHandler {
                            style = style.removeSuffix(houseHover)
                            changeHouse(row, col, false)
                        }
                    }
                }.forEachIndexed { col, label ->
                    boardPane.add(label, col, row)
                }
    }

    private fun changeHouse(row: Int, col: Int, highlight: Boolean) {
        val idTarget = sharpIdHouseFormat.format(row, col)
        val styleAction = if (highlight) { n: Node -> n.style = n.style + houseNeighbour } else { n: Node -> n.style = n.style.removeSuffix(houseNeighbour) }

        fun changeStyles(ids: List<String>) = ids.filterNot { it == idTarget }
                .map { id -> super.getDialogPane().lookup(id) }
                .forEach(styleAction)

        tailrec fun getDiagonals(priorRow: Int, priorColumn: Int, deltaRow: Int, deltaColumn: Int, diagonals: List<Pair<Int, Int>> = listOf()): List<Pair<Int, Int>> {
            val currentRow = priorRow + deltaRow
            val currentColumn = priorColumn + deltaColumn
            return if ((currentRow < 0) || (currentRow >= this.board.size) || (currentColumn < 0) || (currentColumn >= this.board.size))
                diagonals
            else
                getDiagonals(currentRow, currentColumn, deltaRow, deltaColumn, diagonals + Pair(currentRow, currentColumn))
        }

        // Horizontal
        changeStyles(this.board.content.indices.map { currCol -> sharpIdHouseFormat.format(row, currCol) })
        // Vertical
        changeStyles(this.board.content.indices.map { currRow -> sharpIdHouseFormat.format(currRow, col) })
        // Diagonals
        changeStyles(getDiagonals(row, col, -1, -1).map { (r, c) -> sharpIdHouseFormat.format(r, c) })
        changeStyles(getDiagonals(row, col, +1, -1).map { (r, c) -> sharpIdHouseFormat.format(r, c) })
        changeStyles(getDiagonals(row, col, -1, +1).map { (r, c) -> sharpIdHouseFormat.format(r, c) })
        changeStyles(getDiagonals(row, col, +1, +1).map { (r, c) -> sharpIdHouseFormat.format(r, c) })
    }

}