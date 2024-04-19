package rafael.ktgenetic.camouflage

import javafx.scene.paint.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class KolorTest {

    @Test
    fun `Kolor initialization with valid RGB values`() {
        val kolor = Kolor(100, 150, 200)
        assertEquals(100, kolor.r)
        assertEquals(150, kolor.g)
        assertEquals(200, kolor.b)
    }

    @Test
    fun `Kolor initialization throws exception with invalid R value`() {
        assertThrows(IllegalArgumentException::class.java) {
            Kolor(MAX_COLOR_VALUE + 1, 150, 200)
        }
    }

    @Test
    fun `Kolor initialization throws exception with invalid G value`() {
        assertThrows(IllegalArgumentException::class.java) {
            Kolor(100, MAX_COLOR_VALUE + 1, 200)
        }
    }

    @Test
    fun `Kolor initialization throws exception with invalid B value`() {
        assertThrows(IllegalArgumentException::class.java) {
            Kolor(100, 150, MAX_COLOR_VALUE + 1)
        }
    }

    @Test
    fun `validateColor throws exception with invalid color value`() {
        assertThrows(IllegalArgumentException::class.java) {
            validateColor(MAX_COLOR_VALUE + 1, "Test")
        }
    }

    @Test
    fun `validateColor does not throw exception with valid color value`() {
        validateColor(MAX_COLOR_VALUE, "Test")
    }

    @ParameterizedTest
    @CsvSource("WHITE, 255, 255, 255", "BLACK, 0, 0, 0", "RED, 255, 0, 0", "LIME, 0, 255, 0", "GREEN, 0, 128, 0", "BLUE, 0, 0, 255")
    fun `toKolor converts a JavaFX color to a Kolor`(colorName: String, r: Int, g: Int, b: Int) {
        // gets the JavaFX color from the name
        val javaFXColor = Color.valueOf(colorName)

        val kolor = javaFXColor.toKolor()

        assertEquals(r, kolor.r)
        assertEquals(g, kolor.g)
        assertEquals(b, kolor.b)
    }
}