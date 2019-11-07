package rafael.ktgenetic.pictures_comparsion

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class BitmapTest {

    private val legalBitmap = Bitmap(1, 1, 1, 1, 1)

    private fun testIllegalBitmap(x: Int = 1, y: Int = 1, r: Int = 1, g: Int = 1, b: Int = 1) {
        assertThrows(IllegalArgumentException::class.java) {
            legalBitmap.copy(Position(x, y), Kolor(r, g, b))
        }
    }

    @Test
    fun xNegative() {
        testIllegalBitmap(x = -1)
    }

    @Test
    fun x0() {
        assertEquals(0, legalBitmap.copy(position = legalBitmap.position.copy(x = 0)).position.x)
    }

    @Test
    fun xValid() {
        assertEquals(
            Int.MAX_VALUE,
            legalBitmap.copy(position = legalBitmap.position.copy(x = Int.MAX_VALUE)).position.x
        )
    }

    @Test
    fun yNegative() {
        testIllegalBitmap(y = -1)
    }

    @Test
    fun y0() {
        assertEquals(0, legalBitmap.copy(position = legalBitmap.position.copy(y = 0)).position.y)
    }

    @Test
    fun yValid() {
        assertEquals(
            Int.MAX_VALUE,
            legalBitmap.copy(position = legalBitmap.position.copy(y = Int.MAX_VALUE)).position.y
        )
    }

    @Test
    fun rNegative() {
        testIllegalBitmap(r = -1)
    }

    @Test
    fun r0() {
        assertEquals(0, legalBitmap.copy(kolor = legalBitmap.kolor.copy(r = 0)).kolor.r)
    }

    @Test
    fun rValid() {
        assertEquals(125, legalBitmap.copy(kolor = legalBitmap.kolor.copy(r = 125)).kolor.r)
    }

    @Test
    fun rMax() {
        assertEquals(255, legalBitmap.copy(kolor = legalBitmap.kolor.copy(r = 255)).kolor.r)
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
        assertEquals(0, legalBitmap.copy(kolor = legalBitmap.kolor.copy(g = 0)).kolor.g)
    }

    @Test
    fun gValid() {
        assertEquals(125, legalBitmap.copy(kolor = legalBitmap.kolor.copy(g = 125)).kolor.g)
    }

    @Test
    fun gMax() {
        assertEquals(255, legalBitmap.copy(kolor = legalBitmap.kolor.copy(g = 255)).kolor.g)
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
        assertEquals(0, legalBitmap.copy(kolor = legalBitmap.kolor.copy(b = 0)).kolor.b)
    }

    @Test
    fun bValid() {
        assertEquals(125, legalBitmap.copy(kolor = legalBitmap.kolor.copy(b = 125)).kolor.b)
    }

    @Test
    fun bMax() {
        assertEquals(255, legalBitmap.copy(kolor = legalBitmap.kolor.copy(b = 255)).kolor.b)
    }

    @Test
    fun bGreaterThanMax() {
        testIllegalBitmap(b = 256)
    }

}