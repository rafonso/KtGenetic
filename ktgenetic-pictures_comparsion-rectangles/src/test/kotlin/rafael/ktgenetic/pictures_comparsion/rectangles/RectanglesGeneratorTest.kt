package rafael.ktgenetic.pictures_comparsion.rectangles

import org.junit.jupiter.api.Test

internal class RectanglesGeneratorTest {

    @Test
    fun `w=10, h=10, cols=2, rows=2`() {
        val rectangles = createRetangles(10, 10, 2, 2)
        val expected = listOf(
            Pair(Position(0, 0), Position(5, 5)), Pair(Position(5, 0), Position(10, 5)),
            Pair(Position(0, 5), Position(5, 10)), Pair(Position(5, 5), Position(10, 10))
        )
        assert(rectangles == expected) { "rectangles: $rectangles" }
    }

    @Test
    fun `w=10, h=10, cols=3, rows=2`() {
        val rectangles = createRetangles(10, 10, 3, 2)
        val expected = listOf(
            Pair(Position(0, 0), Position(3, 5)),
            Pair(Position(3, 0), Position(6, 5)),
            Pair(Position(6, 0), Position(10, 5)),
            Pair(Position(0, 5), Position(3, 10)),
            Pair(Position(3, 5), Position(6, 10)),
            Pair(Position(6, 5), Position(10, 10))
        )
        assert(rectangles == expected) { "rectangles: $rectangles" }
    }

    @Test
    fun `w=10, h=10, cols=2, rows=3`() {
        val rectangles = createRetangles(10, 10, 2, 3)
        val expected = listOf(
            Pair(Position(0, 0), Position(5, 3)),
            Pair(Position(5, 0), Position(10, 3)),
            Pair(Position(0, 3), Position(5, 6)),
            Pair(Position(5, 3), Position(10, 6)),
            Pair(Position(0, 6), Position(5, 10)),
            Pair(Position(5, 6), Position(10, 10))
        )
        assert(rectangles == expected) { "rectangles: $rectangles" }
    }

    @Test
    fun `w=10, h=10, cols=3, rows=3`() {
        val rectangles = createRetangles(10, 10, 3, 3)
        val expected = listOf(
            Pair(Position(0, 0), Position(3, 3)),
            Pair(Position(3, 0), Position(6, 3)),
            Pair(Position(6, 0), Position(10, 3)),
            Pair(Position(0, 3), Position(3, 6)),
            Pair(Position(3, 3), Position(6, 6)),
            Pair(Position(6, 3), Position(10, 6)),
            Pair(Position(0, 6), Position(3, 10)),
            Pair(Position(3, 6), Position(6, 10)),
            Pair(Position(6, 6), Position(10, 10))
        )
        assert(rectangles == expected) { "rectangles: $rectangles" }
    }

    @Test
    fun `w=10, h=10, cols=4, rows=3`() {
        val rectangles = createRetangles(10, 10, 4, 3)
        val expected = listOf(
            Pair(Position(0, 0), Position(2, 3)),
            Pair(Position(2, 0), Position(5, 3)),
            Pair(Position(5, 0), Position(7, 3)),
            Pair(Position(7, 0), Position(10, 3)),
            Pair(Position(0, 3), Position(2, 6)),
            Pair(Position(2, 3), Position(5, 6)),
            Pair(Position(5, 3), Position(7, 6)),
            Pair(Position(7, 3), Position(10, 6)),
            Pair(Position(0, 6), Position(2, 10)),
            Pair(Position(2, 6), Position(5, 10)),
            Pair(Position(5, 6), Position(7, 10)),
            Pair(Position(7, 6), Position(10, 10))
        )
        assert(rectangles == expected) { "rectangles: $rectangles" }
    }


}