package rafael.ktgenetic.pictures_comparsion.rectangles

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PositionConversorTest {

    @Test
    fun `SimplePositionConversor should not change the position`() {
        val simpleConversor = SimplePositionConversor()
        val position = Position(100, 200)
        assertEquals(position, simpleConversor.toRealPicturePosition(position))
        assertEquals(position, simpleConversor.toResizedPicurePosition(position))
    }

    @Test
    fun `ProportionalPositionConversor should correctly scale the position`() {
        val proportionalConversor = ProportionalPositionConversor(2.0, 3.0)
        val position = Position(100, 200)
        assertEquals(Position(200, 600), proportionalConversor.toRealPicturePosition(position))
        assertEquals(Position(50, 66), proportionalConversor.toResizedPicurePosition(Position(100, 200)))
    }
}