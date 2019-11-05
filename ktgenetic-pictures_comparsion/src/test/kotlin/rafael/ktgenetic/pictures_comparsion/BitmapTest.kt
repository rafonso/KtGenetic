package rafael.ktgenetic.pictures_comparsion

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class BitmapTest {

    private val  legalBitmap = Bitmap(1, 1, 1, 1, 1)

    private fun testIllegalBitmap(x: Int = 1, y: Int = 1, r: Int = 1, g: Int = 1, b: Int = 1) {
        assertThrows(IllegalArgumentException::class.java) {
            legalBitmap.copy(x, y, r, g, b)
        }
    }

    @Test
    fun xNegative() {
        testIllegalBitmap(x = -1)
    }

    @Test
    fun x0() {
        assertEquals(0, legalBitmap.copy(x = 0).x)
    }

    @Test
    fun xValid() {
        assertEquals(Int.MAX_VALUE, legalBitmap.copy(x = Int.MAX_VALUE).x)
    }

    @Test
    fun yNegative() {
        testIllegalBitmap(y = -1)
    }

    @Test
     fun y0() {
        assertEquals(0, legalBitmap.copy(y = 0).y)
    }

    @Test
    fun yValid() {
        assertEquals(Int.MAX_VALUE, legalBitmap.copy(y = Int.MAX_VALUE).y)
    }

    @Test
    fun rNegative() {
        testIllegalBitmap(r = -1)
    }

    @Test
    fun r0() {
        assertEquals(0, legalBitmap.copy(r = 0).r)
    }

    @Test
    fun rValid() {
        assertEquals(125, legalBitmap.copy(r = 125).r)
    }

    @Test
    fun rMax() {
        assertEquals(255, legalBitmap.copy(r = 255).r)
    }

    @Test
    fun rGreaterThanMax() {
        testIllegalBitmap(r = 256)
    }

    @Test
    fun gNegative() {
        testIllegalBitmap(g = -1)
    }

    @Test
    fun g0() {
        assertEquals(0, legalBitmap.copy(g = 0).g)
    }

    @Test
    fun gValid() {
        assertEquals(125, legalBitmap.copy(g = 125).g)
    }

    @Test
    fun gMax() {
        assertEquals(255, legalBitmap.copy(g = 255).g)
    }

    @Test
    fun gGreaterThanMax() {
        testIllegalBitmap(g = 256)
    }

    @Test
    fun bNegative() {
        testIllegalBitmap(b = -1)
    }

    @Test
    fun b0() {
        assertEquals(0, legalBitmap.copy(b = 0).b)
    }

    @Test
    fun bValid() {
        assertEquals(125, legalBitmap.copy(b = 125).b)
    }

    @Test
    fun bMax() {
        assertEquals(255, legalBitmap.copy(b = 255).b)
    }

    @Test
    fun bGreaterThanMax() {
        testIllegalBitmap(b = 256)
    }

}