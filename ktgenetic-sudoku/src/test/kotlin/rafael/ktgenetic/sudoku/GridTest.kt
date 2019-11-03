package rafael.ktgenetic.sudoku

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rafael.ktgenetic.replace

internal class GridTest {

    private fun testGridWithRowIncorrectSizeRepeatedValue(rows: List<Row>, expectedMessage: String) {
        try {
            val grid = Grid(rows)

            kotlin.test.fail(grid.toString())
        } catch (e: IllegalArgumentException) {
            MatcherAssert.assertThat(
                e.message,
                CoreMatchers.startsWith(expectedMessage)
            )
        }
    }

    private fun testGridWithRowIncorrectSize(wrongRow: Int, vararg rows: Row) {
        testGridWithRowIncorrectSizeRepeatedValue(rows.toList(), "Row $wrongRow should have size ${rows.size}")
    }

    private fun testGridWithRepeatedValue(wrongRow: Int, repeatedValue: Element, vararg rows: Row) {
        testGridWithRowIncorrectSizeRepeatedValue(
            rows.toList(),
            "Row $wrongRow contains repeated value ($repeatedValue)"
        )
    }

    private fun testGridColumn(row: Row, quantRows: Int) {
        val grid = Grid(MutableList(quantRows) { row })

        (0 until grid.size).forEach { col ->
            val expectedValues = MutableList(grid.size) { (col + 1) }
            assertEquals(expectedValues.toList(), grid.columns[col]) {
                "Column $col - expected $expectedValues but was: ${grid.columns[col]}"
            }
        }
    }

    private fun testGridBox(grid: Grid, boxIndex: Int, vararg expectedValues: Element) {
        val boxValues = grid.boxes[boxIndex]

        assertEquals(expectedValues.toList(), boxValues) {
            "Box $boxIndex - expected ${expectedValues.toList()} but was: $boxValues"
        }
    }

    // ===================== GRID 4 - START =====================

    @Test
    fun grid4RowSizeLesser() {
        testGridWithRowIncorrectSize(
            2,
            grid4Symbols,
            grid4Symbols.reversed(),
            grid4Symbols - 3,
            grid4Symbols.shuffled()
        )
    }

    @Test
    fun grid4RowSizeGreater() {
        testGridWithRowIncorrectSize(
            1,
            grid4Symbols,
            grid4Symbols.reversed() + 2,
            grid4Symbols.shuffled(),
            grid4Symbols.shuffled()
        )
    }

    @Test
    fun grid4RepeatedValue() {
        testGridWithRepeatedValue(
            1,
            4,
            grid4Symbols,
            grid4Symbols.reversed().replace(3, 4),
            grid4Symbols.shuffled(),
            grid4Symbols.shuffled()
        )
    }

    @Test
    fun grid4Columns() {
        testGridColumn(grid4Symbols, 4)
    }

    @Test
    fun grid4Boxes() {
        val grid = Grid(
            listOf(
                listOf(1, 2, 3, 4),
                listOf(4, 1, 2, 3),
                //////////////////
                listOf(4, 3, 2, 1),
                listOf(3, 2, 1, 4)
            )
        )

        testGridBox(grid, 0, 1, 2, 4, 1)
        testGridBox(grid, 1, 3, 4, 2, 3)
        /////////////////////
        testGridBox(grid, 2, 4, 3, 3, 2)
        testGridBox(grid, 3, 2, 1, 1, 4)
    }

    // ===================== GRID 4 - END =====================

    // ===================== GRID 9 - START =====================

    @Test
    fun grid9RowSizeLesser() {
        testGridWithRowIncorrectSize(
            5,
            grid9Symbols,
            grid9Symbols.reversed(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled() - 4,
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled()
        )
    }

    @Test
    fun grid9RowSizeGreater() {
        testGridWithRowIncorrectSize(
            6,
            grid9Symbols,
            grid9Symbols.reversed(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled() + 9,
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled()
        )
    }

    @Test
    fun grid9RepeatedValue() {
        testGridWithRepeatedValue(
            5,
            8,
            grid9Symbols,
            grid9Symbols.reversed(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.replace(0, 8),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled(),
            grid9Symbols.shuffled()
        )
    }

    @Test
    fun grid9Columns() {
        testGridColumn(grid9Symbols, 9)
    }

    @Test
    fun grid9Boxes() {
        val grid = Grid(
            listOf(
                listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                listOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
                listOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
                /////////
                listOf(9, 8, 7, 6, 5, 4, 3, 2, 1),
                listOf(1, 9, 8, 7, 6, 5, 4, 3, 2),
                listOf(2, 1, 9, 8, 7, 6, 5, 4, 3),
                /////////
                listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                listOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
                listOf(3, 4, 5, 6, 7, 8, 9, 1, 2)
            )
        )

        testGridBox(grid, 0, 1, 2, 3, 9, 1, 2, 8, 9, 1)
        testGridBox(grid, 1, 4, 5, 6, 3, 4, 5, 2, 3, 4)
        testGridBox(grid, 2, 7, 8, 9, 6, 7, 8, 5, 6, 7)
        /////////
        testGridBox(grid, 3, 9, 8, 7, 1, 9, 8, 2, 1, 9)
        testGridBox(grid, 4, 6, 5, 4, 7, 6, 5, 8, 7, 6)
        testGridBox(grid, 5, 3, 2, 1, 4, 3, 2, 5, 4, 3)
        /////////
        testGridBox(grid, 6, 1, 2, 3, 2, 3, 4, 3, 4, 5)
        testGridBox(grid, 7, 4, 5, 6, 5, 6, 7, 6, 7, 8)
        testGridBox(grid, 8, 7, 8, 9, 8, 9, 1, 9, 1, 2)
    }

    // ===================== GRID 9 - END =====================

    // ===================== GRID 16 - START =====================

    @Test
    fun grid16RowSizeLesser() {
        testGridWithRowIncorrectSize(
            13,
            grid16Symbols,
            grid16Symbols.reversed(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled() - 0x0A,
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled()
        )
    }

    @Test
    fun grid16RowSizeGreater() {
        testGridWithRowIncorrectSize(
            11,
            grid16Symbols,
            grid16Symbols.reversed(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled() + 0x05,
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled()
        )
    }

    @Test
    fun grid16RepeatedValue() {
        testGridWithRepeatedValue(
            14,
            0x0F,
            grid16Symbols,
            grid16Symbols.reversed(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.shuffled(),
            grid16Symbols.reversed().replace(9, 0x0F),
            grid16Symbols.shuffled()
        )
    }

    @Test
    fun grid16Columns() {
        testGridColumn(grid16Symbols, 16)
    }

    @Test
    fun grid16Boxes() {
        val grid = Grid(
            listOf(
                listOf(0x01, 0x02, 0x03, 0x04,  0x05, 0x06, 0x07, 0x08,  0x09, 0x0A, 0x0B, 0x0C,  0x0D, 0x0E, 0x0F, 0x10),
                listOf(0x10, 0x01, 0x02, 0x03,  0x04, 0x05, 0x06, 0x07,  0x08, 0x09, 0x0A, 0x0B,  0x0C, 0x0D, 0x0E, 0x0F),
                listOf(0x0F, 0x10, 0x01, 0x02,  0x03, 0x04, 0x05, 0x06,  0x07, 0x08, 0x09, 0x0A,  0x0B, 0x0C, 0x0D, 0x0E),
                listOf(0x0E, 0x0F, 0x10, 0x01,  0x02, 0x03, 0x04, 0x05,  0x06, 0x07, 0x08, 0x09,  0x0A, 0x0B, 0x0C, 0x0D),
                /////////                                                                         
                listOf(0x10, 0x0F, 0x0E, 0x0D,  0x0C, 0x0B, 0x0A, 0x09,  0x08, 0x07, 0x06, 0x05,  0x04, 0x03, 0x02, 0x01),
                listOf(0x01, 0x10, 0x0F, 0x0E,  0x0D, 0x0C, 0x0B, 0x0A,  0x09, 0x08, 0x07, 0x06,  0x05, 0x04, 0x03, 0x02),
                listOf(0x02, 0x01, 0x10, 0x0F,  0x0E, 0x0D, 0x0C, 0x0B,  0x0A, 0x09, 0x08, 0x07,  0x06, 0x05, 0x04, 0x03),
                listOf(0x03, 0x02, 0x01, 0x10,  0x0F, 0x0E, 0x0D, 0x0C,  0x0B, 0x0A, 0x09, 0x08,  0x07, 0x06, 0x05, 0x04),
                /////////                                                                         
                listOf(0x02, 0x03, 0x04, 0x05,  0x06, 0x07, 0x08, 0x09,  0x0A, 0x0B, 0x0C, 0x0D,  0x0E, 0x0F, 0x10, 0x01),
                listOf(0x03, 0x04, 0x05, 0x06,  0x07, 0x08, 0x09, 0x0A,  0x0B, 0x0C, 0x0D, 0x0E,  0x0F, 0x10, 0x01, 0x02),
                listOf(0x04, 0x05, 0x06, 0x07,  0x08, 0x09, 0x0A, 0x0B,  0x0C, 0x0D, 0x0E, 0x0F,  0x10, 0x01, 0x02, 0x03),
                listOf(0x05, 0x06, 0x07, 0x08,  0x09, 0x0A, 0x0B, 0x0C,  0x0D, 0x0E, 0x0F, 0x10,  0x01, 0x02, 0x03, 0x04),
                /////////                                                                         
                listOf(0x0F, 0x0E, 0x0D, 0x0C,  0x0B, 0x0A, 0x09, 0x08,  0x07, 0x06, 0x05, 0x04,  0x03, 0x02, 0x01, 0x10),
                listOf(0x0E, 0x0D, 0x0C, 0x0B,  0x0A, 0x09, 0x08, 0x07,  0x06, 0x05, 0x04, 0x03,  0x02, 0x01, 0x10, 0x0F),
                listOf(0x0D, 0x0C, 0x0B, 0x0A,  0x09, 0x08, 0x07, 0x06,  0x05, 0x04, 0x03, 0x02,  0x01, 0x10, 0x0F, 0x0E),
                listOf(0x0C, 0x0B, 0x0A, 0x09,  0x08, 0x07, 0x06, 0x05,  0x04, 0x03, 0x02, 0x01,  0x10, 0x0F, 0x0E, 0x0D)
            )
        )

        testGridBox(grid, 0x0, 0x1, 0x2, 0x3, 0x4, 0x10, 0x1, 0x2, 0x3, 0xF, 0x10, 0x1, 0x2, 0xE, 0xF, 0x10, 0x1)
        testGridBox(grid, 0x1, 0x5, 0x6, 0x7, 0x8, 0x4, 0x5, 0x6, 0x7, 0x3, 0x4, 0x5, 0x6, 0x2, 0x3, 0x4, 0x5)
        testGridBox(grid, 0x2, 0x9, 0xA, 0xB, 0xC, 0x8, 0x9, 0xA, 0xB, 0x7, 0x8, 0x9, 0xA, 0x6, 0x7, 0x8, 0x9)
        testGridBox(grid, 0x3, 0xD, 0xE, 0xF, 0x10, 0xC, 0xD, 0xE, 0xF, 0xB, 0xC, 0xD, 0xE, 0xA, 0xB, 0xC, 0xD)
        ///////////
        testGridBox(grid, 0x4, 0x10, 0xF, 0xE, 0xD, 0x1, 0x10, 0xF, 0xE, 0x2, 0x1, 0x10, 0xF, 0x3, 0x2, 0x1, 0x10)
        testGridBox(grid, 0x5, 0xC, 0xB, 0xA, 0x9, 0xD, 0xC, 0xB, 0xA, 0xE, 0xD, 0xC, 0xB, 0xF, 0xE, 0xD, 0xC)
        testGridBox(grid, 0x6, 0x8, 0x7, 0x6, 0x5, 0x9, 0x8, 0x7, 0x6, 0xA, 0x9, 0x8, 0x7, 0xB, 0xA, 0x9, 0x8)
        testGridBox(grid, 0x7, 0x4, 0x3, 0x2, 0x1, 0x5, 0x4, 0x3, 0x2, 0x6, 0x5, 0x4, 0x3, 0x7, 0x6, 0x5, 0x4)
        ///////////
        testGridBox(grid, 0x8, 0x2, 0x3, 0x4, 0x5, 0x3, 0x4, 0x5, 0x6, 0x4, 0x5, 0x6, 0x7, 0x5, 0x6, 0x7, 0x8)
        testGridBox(grid, 0x9, 0x6, 0x7, 0x8, 0x9, 0x7, 0x8, 0x9, 0xA, 0x8, 0x9, 0xA, 0xB, 0x9, 0xA, 0xB, 0xC)
        testGridBox(grid, 0xA, 0xA, 0xB, 0xC, 0xD, 0xB, 0xC, 0xD, 0xE, 0xC, 0xD, 0xE, 0xF, 0xD, 0xE, 0xF, 0x10)
        testGridBox(grid, 0xB, 0xE, 0xF, 0x10, 0x1, 0xF, 0x10, 0x1, 0x2, 0x10, 0x1, 0x2, 0x3, 0x1, 0x2, 0x3, 0x4)
        ///////////
        testGridBox(grid, 0xC, 0xF, 0xE, 0xD, 0xC, 0xE, 0xD, 0xC, 0xB, 0xD, 0xC, 0xB, 0xA, 0xC, 0xB, 0xA, 0x9)
        testGridBox(grid, 0xD, 0xB, 0xA, 0x9, 0x8, 0xA, 0x9, 0x8, 0x7, 0x9, 0x8, 0x7, 0x6, 0x8, 0x7, 0x6, 0x5)
        testGridBox(grid, 0xE, 0x7, 0x6, 0x5, 0x4, 0x6, 0x5, 0x4, 0x3, 0x5, 0x4, 0x3, 0x2, 0x4, 0x3, 0x2, 0x1)
        testGridBox(grid, 0xF, 0x3, 0x2, 0x1, 0x10, 0x2, 0x1, 0x10, 0xF, 0x1, 0x10, 0xF, 0xE, 0x10, 0xF, 0xE, 0xD)
    }

    // ===================== GRID 16 - END =====================

}