package rafael.ktgenetic.sudoku

import rafael.ktgenetic.Chromosome
import rafael.ktgenetic.OrderedGene

typealias Cell = OrderedGene<Int>

val cells: List<Cell> = Puzzle.values.flatMap { id -> Puzzle.values.map { value -> Cell(id * 10 + value, value) } }

data class Puzzle(override val content: List<Cell>) : Chromosome<Cell>() {

    companion object {

        val maxPos = 81

        val values = (1..9)
        val positions = (0 until 9)

        val rows: List<List<Int>> = positions.map { row -> positions.map { col -> row * 9 + col } }

        val cols: List<List<Int>> = positions.map { col -> positions.map { row -> row * 9 + col } }

        val sectors: List<List<Int>> = listOf(
                listOf(0, 1, 2, 9, 10, 11, 18, 19, 20),
                listOf(3, 4, 5, 12, 13, 14, 21, 22, 23),
                listOf(6, 7, 8, 15, 16, 17, 24, 25, 26),
                listOf(27, 28, 29, 36, 37, 38, 45, 46, 47),
                listOf(30, 31, 32, 39, 40, 41, 48, 49, 50),
                listOf(33, 34, 35, 42, 43, 44, 51, 52, 53),
                listOf(54, 55, 56, 63, 64, 65, 72, 73, 74),
                listOf(57, 58, 59, 66, 67, 68, 75, 76, 77),
                listOf(60, 61, 62, 69, 70, 71, 78, 79, 80)
        )
    }

    private fun rowToString(row: Int): String {
        val pos = rows[row]

        return "${content[pos[0]].value}${content[pos[1]].value}${content[pos[2]].value}|" +
                "${content[pos[3]].value}${content[pos[4]].value}${content[pos[5]].value}|" +
                "${content[pos[6]].value}${content[pos[7]].value}${content[pos[8]].value}"
    }

    val formatted: String by lazy {
        """
        |${rowToString(0)}
        |${rowToString(1)}
        |${rowToString(2)}
        |---+---+---
        |${rowToString(3)}
        |${rowToString(4)}
        |${rowToString(5)}
        |---+---+---
        |${rowToString(6)}
        |${rowToString(7)}
        |${rowToString(8)}
        """.trimMargin()
    }

    override fun valueToString() =
            "${rowToString(0)}/${rowToString(1)}/${rowToString(2)}*" +
                    "${rowToString(3)}/${rowToString(4)}/${rowToString(5)}*" +
                    "${rowToString(6)}/${rowToString(7)}/${rowToString(8)}"

    override fun toString(): String = super.toString()

}