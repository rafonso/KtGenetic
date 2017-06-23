package rafael.ktgenetic.balancedtable

import kotlin.test.assertEquals
import org.junit.Test

class BalanceDimensionsTest {

    private fun testHalves(size: Int, expectedHalves: Pair<List<Int>, List<Int>>) {
        val dimensions = BalanceDimensions(size)
        val halves = dimensions.halves

        assertEquals(expectedHalves, halves)
    }

    @Test fun half01() {
        testHalves(1, Pair(listOf(), listOf()))
    }

    @Test fun half02() {
        testHalves(2, Pair(listOf(0), listOf(1)))
    }

    @Test fun half03() {
        testHalves(3, Pair(listOf(0), listOf(2)))
    }

    @Test fun half09() {
        testHalves(9, Pair(listOf(0, 1, 2, 3), listOf(5, 6, 7, 8)))
    }

    @Test fun half10() {
        testHalves(10, Pair(listOf(0, 1, 2, 3, 4), listOf(5, 6, 7, 8, 9)))
    }

    @Test fun half29() {
        testHalves(29,
                Pair(
                        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),
                        listOf(15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)
                )
        )
    }
}