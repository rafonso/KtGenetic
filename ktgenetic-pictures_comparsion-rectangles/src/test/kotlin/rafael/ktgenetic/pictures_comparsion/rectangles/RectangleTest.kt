package rafael.ktgenetic.pictures_comparsion.rectangles

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RectangleTest {

    @Test
    fun `Rectangle should correctly calculate width and height`() {
        val rectangle = Rectangle(Position(1, 1), Position(4, 5), Kolor(255, 255, 255))
        assertEquals(3, rectangle.width)
        assertEquals(4, rectangle.height)
    }

    @Test
    fun `Rectangle should correctly calculate width and height for reversed corners`() {
        val rectangle = Rectangle(Position(4, 5), Position(1, 1), Kolor(255, 255, 255))
        assertEquals(3, rectangle.width)
        assertEquals(4, rectangle.height)
    }

    @Test
    fun `toString should return formatted rectangle`() {
        val rectangle = Rectangle(Position(1, 1), Position(4, 5), Kolor(255, 255, 255))
        assertEquals("[(   1,   1),(   4,   5),(255,255,255)]", rectangle.toString())
    }
}