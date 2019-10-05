package rafael.ktgenetic.nqueens

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BoardTest {

//    @Test
//    fun repeatedValues() {
//        val l = listOf(0, 1, 1)
//        assertThrows(IllegalArgumentException::class.java) {
//            val b = Board(l)
//
//            fail(b.toString())
//        }
//    }

    @Test
    fun valuesOutOfRange() {
        val l = listOf(0, 10, 1)
        assertThrows(IllegalArgumentException::class.java) {
            val b = Board(l)

            fail(b.toString())
        }
    }

}