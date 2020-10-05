package rafael.ktgenetic

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ListPiecesTest {

    private fun testJoin(left: List<Any>, core: List<Any>, right: List<Any>, vararg join: Any) {
        val listPieces = ListPieces(left, core, right)

        assertEquals(join.toList(), listPieces.join)
    }

    @Test
    fun joinLeftEmptyCoreEmptyLeftEmpty() {
        testJoin(listOf(), listOf(), listOf())
    }

    @Test
    fun joinLeftEmptyCoreEmptyLeftFilled() {
        testJoin(listOf(), listOf(), listOf(8, 9), 8, 9)
    }

    @Test
    fun joinLeftEmptyCoreFilledLeftEmpty() {
        testJoin(listOf(), listOf(5, 6), listOf(), 5, 6)
    }

    @Test
    fun joinLeftEmptyCoreFilledLeftFilled() {
        testJoin(listOf(), listOf(5, 6), listOf(8, 9), 5, 6, 8, 9)
    }

    @Test
    fun joinLeftFilledCoreEmptyLeftEmpty() {
        testJoin(listOf(1, 2), listOf(), listOf(), 1, 2)
    }

    @Test
    fun joinLeftFilledCoreEmptyLeftFilled() {
        testJoin(listOf(1, 2), listOf(), listOf(8, 9), 1, 2, 8, 9)
    }

    @Test
    fun joinLeftFilledCoreFilledLeftEmpty() {
        testJoin(listOf(1, 2), listOf(4, 5), listOf(), 1, 2, 4, 5)
    }

    @Test
    fun joinLeftFilledCoreFilledLeftFilled() {
        testJoin(listOf(1, 2), listOf(4, 5), listOf(8, 9), 1, 2, 4, 5, 8, 9)
    }

}
