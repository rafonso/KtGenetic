package rafael.ktgenetic.core.utils

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

    @Test
    fun replaceNegativeIndex() {
        val l = listOf(1, 2, 3, 4)
        assertThrows(IndexOutOfBoundsException::class.java) {
            l.replace(-2, 10)
        }
    }

    @Test
    fun replaceOutOfBoundIndex() {
        val l = listOf(1, 2, 3, 4)
        assertThrows(IndexOutOfBoundsException::class.java) {
            l.replace(20, 10)
        }
    }

    @Test
    fun replaceValidIndexWithNull() {
        val original = listOf(1, 2, 3, 4)
        val copy = original.replace(2, null)

        assertNotSame(original, copy)
        assertSame(original[0], copy[0])
        assertSame(original[1], copy[1])
        assertNull(copy[2])
        assertSame(original[3], copy[3])
    }

    @Test
    fun replaceValidIndexWithValue() {
        val original = listOf(1, 2, 3, 4)
        val copy = original.replace(2, 10)

        assertNotSame(original, copy)
        assertSame(original[0], copy[0])
        assertSame(original[1], copy[1])
        assertEquals(10, copy[2])
        assertSame(original[3], copy[3])
    }

    @Test
    fun testPMap() {
        val original = listOf(1, 2, 3, 4)
        val copy = original.pMap { it * 2 }

        original.indices.forEach {
            assertEquals(original[it] * 2, copy[it])
        }
    }

    @Test
    fun testPFlatMap() {
        val original = listOf(1, 2, 3, 4)
        val copy = original.pFlatMap { listOf(it * 2, it * 3) }

        assertEquals(original.size * 2, copy.size)
        original.indices.forEach {
            assertEquals(original[it] * 2, copy[it * 2])
            assertEquals(original[it] * 3, copy[it * 2 + 1])
        }
    }

}
