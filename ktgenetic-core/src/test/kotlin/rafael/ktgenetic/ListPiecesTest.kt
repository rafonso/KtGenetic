package rafael.ktgenetic

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ListPiecesTest {

    @Test
    fun joinEmptyLists() {
        val l1 = listOf<Any>()
        val l2 = listOf<Any>()
        val l3 = listOf<Any>()

        val listPieces = ListPieces(l1, l2, l3)

        assertTrue(listPieces.join.isEmpty())
    }
}