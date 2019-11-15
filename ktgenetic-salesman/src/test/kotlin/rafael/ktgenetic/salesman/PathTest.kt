package rafael.ktgenetic.salesman

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.math.sqrt

internal class PathTest {

    @BeforeEach
    fun setup() {
        DistanceRepository.clear()
    }

    @Test
    fun path1Point() {
        val path = Path(Point(5, 5))

        assertEquals(0.0, path.width)
    }

    @Test
    fun path2Points() {
        assertEquals(10.0, Path(Point(5, 5), Point(15, 5)).width)
        assertEquals(5.0, Path(Point(0, 3), Point(4, 0)).width)
    }

    @Test
    fun path3Points() {
        assertEquals(10.0 + sqrt(50.0), Path(Point(5, 5), Point(15, 5), Point(10, 0)).width)
        assertEquals(10.0 + 2 * sqrt(50.0), Path(Point(5, 5), Point(15, 5), Point(10, 0), Point(5, 5)).width)
        assertEquals(9.0, Path(Point(0, 3), Point(4, 0), Point(0, 0)).width)
    }

    @Test
    fun path4Points() {
        assertEquals(10.0 + sqrt(50.0) + 10, Path(Point(5, 5), Point(15, 5), Point(10, 0), Point(10, 10)).width)
        assertEquals(11.0, Path(Point(0, 3), Point(4, 3), Point(4, 0), Point(0, 0)).width)
    }


}