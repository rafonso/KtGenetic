package rafael.ktgenetic.sudoku

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BasicConflictCounterTest {

    private fun assertConflicts(conflicts: List<Conflict>, vararg expectedConflicts: Conflict) {
        assertEquals(expectedConflicts.toList(), conflicts)
    }

    @Test
    fun noConflicts() {
        val grid = Grid(
            listOf(
                listOf(1, 2, 3, 4),
                listOf(4, 3, 2, 1),
                listOf(3, 4, 1, 2),
                listOf(2, 1, 4, 3)
            )
        )

        val conflicts = BasicConflictCounter().calculateConflicts(grid)

        assertTrue(conflicts.isEmpty()) {
            "Should not have conflicts, but were: $conflicts"
        }
    }

    @Test
    fun conflictColumn() {
        val grid = Grid(
            listOf(
                listOf(1, 2, 3, 4),
                listOf(4, 3, 2, 1),
                listOf(3, 4, 1, 2),
                listOf(1, 2, 4, 3)
            )
        )

        val conflicts = BasicConflictCounter().calculateConflicts(grid)

        assertConflicts(
            conflicts,
            Conflict(0, 0, 3, 0, 1, TypeConflict.COLUMN),
            Conflict(0, 1, 3, 1, 2, TypeConflict.COLUMN)
        )
    }

    @Test
    fun conflictColumnAndBoxes() {
        val grid = Grid(
            listOf(
                grid4Symbols,
                grid4Symbols,
                grid4Symbols,
                grid4Symbols
            )
        )

        val conflicts = BasicConflictCounter().calculateConflicts(grid)

        assertConflicts(
            conflicts,

            Conflict(0, 0, 1, 0, 1, TypeConflict.COLUMN),
            Conflict(1, 0, 2, 0, 1, TypeConflict.COLUMN),
            Conflict(2, 0, 3, 0, 1, TypeConflict.COLUMN),

            Conflict(0, 1, 1, 1, 2, TypeConflict.COLUMN),
            Conflict(1, 1, 2, 1, 2, TypeConflict.COLUMN),
            Conflict(2, 1, 3, 1, 2, TypeConflict.COLUMN),

            Conflict(0, 2, 1, 2, 3, TypeConflict.COLUMN),
            Conflict(1, 2, 2, 2, 3, TypeConflict.COLUMN),
            Conflict(2, 2, 3, 2, 3, TypeConflict.COLUMN),

            Conflict(0, 3, 1, 3, 4, TypeConflict.COLUMN),
            Conflict(1, 3, 2, 3, 4, TypeConflict.COLUMN),
            Conflict(2, 3, 3, 3, 4, TypeConflict.COLUMN),

            Conflict(0, 0, 1, 0, 1, TypeConflict.BOX),
            Conflict(0, 1, 1, 1, 2, TypeConflict.BOX),
            Conflict(0, 2, 1, 2, 3, TypeConflict.BOX),
            Conflict(0, 3, 1, 3, 4, TypeConflict.BOX),
            Conflict(2, 0, 3, 0, 1, TypeConflict.BOX),
            Conflict(2, 1, 3, 1, 2, TypeConflict.BOX),
            Conflict(2, 2, 3, 2, 3, TypeConflict.BOX),
            Conflict(2, 3, 3, 3, 4, TypeConflict.BOX)
        )
    }

    @Test
    fun conflictBoxes() {
        val grid = Grid(
            listOf(
                listOf(4, 5, 6, 9, 7, 3, 1, 2, 8),
                listOf(1, 3, 2, 8, 6, 4, 5, 7, 9),
                listOf(8, 7, 3, 5, 1, 2, 9, 4, 6), // Conflict Here

                listOf(7, 4, 3, 1, 8, 9, 6, 5, 2),
                listOf(9, 8, 5, 3, 2, 6, 4, 1, 7),
                listOf(2, 6, 1, 7, 4, 5, 8, 9, 3),

                listOf(3, 1, 7, 2, 5, 8, 9, 6, 4),
                listOf(5, 9, 4, 6, 3, 7, 2, 8, 1),
                listOf(6, 2, 8, 4, 9, 1, 7, 3, 5)
            )
        )

        val conflicts = BasicConflictCounter().calculateConflicts(grid)

        assertConflicts(
            conflicts, //
            Conflict(2, 2, 3, 2, 3, TypeConflict.COLUMN),
            Conflict(2, 6, 6, 6, 9, TypeConflict.COLUMN),
            Conflict(1, 1, 2, 2, 3, TypeConflict.BOX),
            Conflict(1, 8, 2, 6, 9, TypeConflict.BOX)
        )
    }
}