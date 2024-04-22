package rafael.ktgenetic.pictures_comparsion.rectangles

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class PositionTest {

    @Test
    fun `Position should not throw exception for valid coordinates`() {
        assertDoesNotThrow { Position(0, 0) }
        assertDoesNotThrow { Position(100, 200) }
    }

    @Test
    fun `Position should throw exception for invalid coordinates`() {
        assertThrows<IllegalArgumentException> { Position(-1, 0) }
        assertThrows<IllegalArgumentException> { Position(0, -1) }
        assertThrows<IllegalArgumentException> { Position(-1, -1) }
    }

    @Test
    fun `toString should return formatted coordinates`() {
        val position = Position(100, 200)
        assertEquals("( 100, 200)", position.toString())
    }
}