package rafael.ktgenetic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ListExtensionsKtTest {

    @Test
    fun swapDifferentPositions() {
        val l = listOf(1, 2, 3, 4)
        val swapped =  l.swap(1, 3)

        assertEquals(listOf(1, 4, 3, 2), swapped)
    }

    @Test
    fun swapEqualsPositions() {
        val l = listOf(1, 2, 3, 4)
        val swapped =  l.swap(3, 3)

        assertSame(l, swapped)
    }

    @Test
    fun swapIrregularPositions() {
        val l = listOf(1, 2, 3, 4)
        assertThrows(IndexOutOfBoundsException::class.java) {
            l.swap(1, 5)
        }
    }

    @Test
    fun randomSwapListSize0() {
        val l = listOf<Any>()

        val swapped = l.randomSwap()

        assertTrue(swapped.isEmpty())
        assertSame(l, swapped)
    }

    @Test
    fun randomSwapListSize1() {
        val l = listOf<Any>(100)

        val swapped = l.randomSwap()

        assertEquals(1, swapped.size)
        assertSame(l, swapped)
    }

    @Test
    fun randomSwapListSize2() {
        val l = listOf<Any>(100, 8000)

        val swapped = l.randomSwap()

        assertEquals(2, swapped.size)
        assertSame(l[0], swapped[1])
        assertSame(l[1], swapped[0])
        assertNotSame(l, swapped)
    }

}