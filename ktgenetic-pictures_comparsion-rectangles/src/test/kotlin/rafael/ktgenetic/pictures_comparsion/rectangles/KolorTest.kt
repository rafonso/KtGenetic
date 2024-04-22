package rafael.ktgenetic.pictures_comparsion.rectangles

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class KolorTest {

    @Test
    fun `validateColor should not throw exception for valid color values`() {
        assertDoesNotThrow { validateColor(0, "R") }
        assertDoesNotThrow { validateColor(255, "G") }
        assertDoesNotThrow { validateColor(128, "B") }
    }

    @Test
    fun `validateColor should throw exception for invalid color values`() {
        assertThrows<IllegalArgumentException> { validateColor(-1, "R") }
        assertThrows<IllegalArgumentException> { validateColor(256, "G") }
    }

    @Test
    fun `toString should return formatted color values`() {
        val kolor = Kolor(255, 128, 0)
        assertEquals("(255,128,  0)", kolor.toString())
    }

    @Test
    fun `distanceTo should return correct distance for different colors`() {
        val kolor1 = Kolor(255, 255, 255)
        val kolor2 = Kolor(0, 0, 0)
        assertEquals(3 * MAX_COLOR_VALUE_D, kolor1.distanceTo(kolor2))
    }

    @Test
    fun `distanceTo should return zero for same colors`() {
        val kolor1 = Kolor(128, 128, 128)
        val kolor2 = Kolor(128, 128, 128)
        assertEquals(0, kolor1.distanceTo(kolor2))
    }
}