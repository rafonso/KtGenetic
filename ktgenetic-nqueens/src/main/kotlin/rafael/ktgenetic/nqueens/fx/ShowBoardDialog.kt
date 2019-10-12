package rafael.ktgenetic.nqueens.fx

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import rafael.ktgenetic.nqueens.Board
import tornadofx.*

private const val idHouseFormat = "house_%d_%d"
private const val sharpIdHouseFormat = "#$idHouseFormat"

private const val queen = "\u2655"

private const val queenColorNormal = "black"
private const val queenColorCollision = "red"

private const val whiteHouseColor = "lightyellow"
private const val blackHouseColor = "lightgreen"
private val colors = arrayOf(
        arrayOf(blackHouseColor, whiteHouseColor),
        arrayOf(whiteHouseColor, blackHouseColor)
)

private const val houseSide = 20.0

private val deltas = listOf(-1, 0, 1)
private val deltaPaths = deltas
        .flatMap { dr -> deltas.map { dc -> listOf(dr, dc) } }
        .filterNot { (deltaR, deltaC) -> deltaR == 0 && deltaC == 0 }

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

    private fun fillRow(boardPane: GridPane, row: Int, queenPos: Int) = (this.board.content.indices)
            .map { col ->
                label {
                    id = idHouseFormat.format(row, col)
                    style {
                        fillCss()
                    }
                    prefWidth = houseSide
                    prefHeight = prefWidth
                    text = if (col == queenPos) queen else ""
                    textAlignment = TextAlignment.CENTER
                    alignment = Pos.CENTER
                    background = Background(BackgroundFill(Color.valueOf(colors[row % 2][col % 2]), null, null))
                    onMouseEntered = EventHandler {
                        style {
                            fillCss(BorderStrokeStyle.SOLID, 1)
                        }
                        changeHouse(row, col, true)
                    }
                    onMouseExited = EventHandler {
                        style {
                            fillCss()
                        }
                        changeHouse(row, col, false)
                    }
                }
            }.forEachIndexed { col, label ->
                boardPane.add(label, col, row)
            }

    private fun changeHouse(row: Int, col: Int, highlight: Boolean) {

        tailrec fun getDiagonals(priorRow: Int, priorColumn: Int, deltaRow: Int, deltaColumn: Int, diagonals: List<Pair<Int, Int>> = listOf()): List<Pair<Int, Int>> {
            val currentRow = priorRow + deltaRow
            val currentColumn = priorColumn + deltaColumn
            val limit = (currentRow < 0) || (currentRow >= this.board.size) || (currentColumn < 0) || (currentColumn >= this.board.size)

            return if (limit)
                diagonals
            else
                getDiagonals(currentRow, currentColumn, deltaRow, deltaColumn, diagonals + Pair(currentRow, currentColumn))
        }

        val (borderStroke, bWidth, queenColor) = if (highlight)
            Triple(BorderStrokeStyle.DASHED, 1, queenColorCollision)
        else
            Triple(BorderStrokeStyle.NONE, 0, queenColorNormal)

        deltaPaths
                .flatMap { (deltaRow, deltaCol) -> getDiagonals(row, col, deltaRow, deltaCol) }
                .map { (r, c) -> sharpIdHouseFormat.format(r, c) }
                .map { id -> super.getDialogPane().lookup(id) }
                .forEach { label ->
                    label.style {
                        fillCss(borderStroke, bWidth, queenColor)
                    }
                }
    }

    private fun InlineCss.fillCss(borderStroke: BorderStrokeStyle = BorderStrokeStyle.NONE, bWidth: Int = 0,
                                  textColor: String = queenColorNormal) {
        borderStyle = multi(borderStroke)
        borderWidth = multi(box(bWidth.px))
        textFill = c(textColor)
    }

}