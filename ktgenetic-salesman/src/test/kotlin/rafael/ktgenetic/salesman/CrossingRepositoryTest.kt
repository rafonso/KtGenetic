package rafael.ktgenetic.salesman

import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

internal class CrossingRepositoryTest {

    private val verticalSegment = toSegment(50, 25, 50, 75)

    private val horizontalSegment = toSegment(25, 50, 75, 50)

    private val obliqueSegment = toSegment(25, 25, 75, 75)

    private fun testCrossing(segment0: Vector, x1: Int, y1: Int, x2: Int, y2: Int, expectedResult: Boolean) {
        val segment1 = toSegment(x1, y1, x2, y2)

        assertEquals(expectedResult, CrossingRepository.verifyCrossing(segment0, segment1)) {
            "Verify Crossing $segment0 X $segment1"
        }
    }

    @BeforeEach
    fun setUp() {
        CrossingRepository.clear()
    }

    // VERTICAL SEGMENT - START

    @Test
    fun colinearToVerticalLine() {
        testCrossing(verticalSegment, 50, 10, 50, 20, false) // Colinear - start Before 1st Point
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                verticalSegment,
                50,
                10,
                50,
                25,
                false
            )
        } // Touching Point
        testCrossing(verticalSegment, 50, 10, 50, 50, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(verticalSegment, 50, 10, 50, 75, true) }
        testCrossing(verticalSegment, 50, 10, 50, 90, true)
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                verticalSegment,
                50,
                25,
                50,
                50,
                true
            )
        } // Colinear - Start at 1st Point
        testCrossing(verticalSegment, 50, 25, 50, 75, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(verticalSegment, 50, 25, 50, 90, true) }
        testCrossing(verticalSegment, 50, 50, 50, 60, true) // Colinear - Start inside segment
        assertThrows(IllegalStateException::class.java) { testCrossing(verticalSegment, 50, 50, 50, 75, true) }
        testCrossing(verticalSegment, 50, 50, 50, 90, true)
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                verticalSegment,
                50,
                75,
                50,
                90,
                false
            )
        } // Touching Point
        testCrossing(verticalSegment, 50, 80, 50, 90, false) // Colinear - Start after 2nd Point
    }

    @Test
    fun parallelToVerticalLine() {
        testCrossing(verticalSegment, 20, 10, 20, 20, false) // Parallel - start Before 1st Point
        testCrossing(verticalSegment, 20, 10, 20, 25, false)
        testCrossing(verticalSegment, 20, 10, 20, 50, false)
        testCrossing(verticalSegment, 20, 10, 20, 75, false)
        testCrossing(verticalSegment, 20, 10, 20, 90, false)
        testCrossing(verticalSegment, 20, 25, 20, 50, false) // Parallel - Start at 1st Point
        testCrossing(verticalSegment, 20, 25, 20, 75, false)
        testCrossing(verticalSegment, 20, 25, 20, 90, false)
        testCrossing(verticalSegment, 20, 50, 20, 60, false) // Parallel - Start inside segment
        testCrossing(verticalSegment, 20, 50, 20, 75, false)
        testCrossing(verticalSegment, 20, 50, 20, 90, false) // Colinear - Start at 2nd Point
        testCrossing(verticalSegment, 20, 75, 20, 90, false) // Parallel - Start at 2nd Point
        testCrossing(verticalSegment, 20, 80, 20, 90, false) // Parallel - Start after 2nd Point
    }

    @Test
    fun obliqueToVerticalLine() {
        testCrossing(verticalSegment, 10, 10, 20, 20, false) // Oblique - start Before 1st Point
        testCrossing(verticalSegment, 10, 10, 25, 25, false)
        testCrossing(verticalSegment, 10, 10, 30, 30, false)
        testCrossing(verticalSegment, 10, 10, 50, 50, true)
        testCrossing(verticalSegment, 10, 10, 60, 60, true)
        testCrossing(verticalSegment, 10, 10, 75, 75, true)
        testCrossing(verticalSegment, 10, 10, 90, 90, true)
        testCrossing(verticalSegment, 25, 25, 25, 25, false) // Oblique - start at 1st Point
        testCrossing(verticalSegment, 25, 25, 30, 30, false)
        testCrossing(verticalSegment, 25, 25, 50, 50, true)
        testCrossing(verticalSegment, 25, 25, 60, 60, true)
        testCrossing(verticalSegment, 25, 25, 75, 75, true)
        testCrossing(verticalSegment, 25, 25, 90, 90, true)
        testCrossing(verticalSegment, 30, 30, 30, 30, false) // Oblique - start after 1st Point
        testCrossing(verticalSegment, 30, 30, 50, 50, true)
        testCrossing(verticalSegment, 30, 30, 60, 60, true)
        testCrossing(verticalSegment, 30, 30, 75, 75, true)
        testCrossing(verticalSegment, 30, 30, 90, 90, true)
        testCrossing(verticalSegment, 50, 50, 60, 60, true)// Oblique - start inside Segment
        testCrossing(verticalSegment, 50, 50, 75, 75, true)
        testCrossing(verticalSegment, 50, 50, 90, 90, true)
        testCrossing(verticalSegment, 60, 60, 70, 70, false) // Oblique - start after half of Segment
        testCrossing(verticalSegment, 60, 60, 75, 75, false)
        testCrossing(verticalSegment, 60, 60, 90, 90, false)
        testCrossing(verticalSegment, 75, 75, 75, 75, false) // Oblique - start at 2nd point
        testCrossing(verticalSegment, 75, 75, 90, 90, false)
        testCrossing(verticalSegment, 80, 80, 90, 90, false) // Oblique - after 2nd point
        testCrossing(verticalSegment, 25, 0, 50, 25, false) // Touching
        testCrossing(verticalSegment, 50, 75, 75, 100, false)
    }

    @Test
    fun perpendicularToVerticalLine() {
        testCrossing(verticalSegment, 25, 50, 40, 50, false) // start before 1st point
        testCrossing(verticalSegment, 25, 50, 50, 50, true)
        testCrossing(verticalSegment, 25, 50, 75, 50, true)
        testCrossing(verticalSegment, 50, 50, 75, 50, true)
        testCrossing(verticalSegment, 60, 50, 75, 50, false)
        testCrossing(verticalSegment, 20, 25, 50, 25, false) // Touching
        testCrossing(verticalSegment, 50, 75, 75, 75, false)
    }

    // HORIZONTAL SEGMENT - START

    @Test
    fun colinearToHorizontalLine() {
        testCrossing(horizontalSegment, 10, 50, 20, 50, false)
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                horizontalSegment,
                10,
                50,
                25,
                50,
                false
            )
        }// Touching
        testCrossing(horizontalSegment, 10, 50, 50, 50, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(horizontalSegment, 10, 50, 75, 50, true) }
        testCrossing(horizontalSegment, 10, 50, 90, 50, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(horizontalSegment, 25, 50, 50, 50, true) }
        testCrossing(horizontalSegment, 25, 50, 75, 50, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(horizontalSegment, 25, 50, 90, 50, true) }
        testCrossing(horizontalSegment, 50, 50, 70, 50, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(horizontalSegment, 50, 50, 75, 50, true) }
        testCrossing(horizontalSegment, 50, 50, 90, 50, true)
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                horizontalSegment,
                75,
                50,
                90,
                50,
                false
            )
        } // Touching
        testCrossing(horizontalSegment, 80, 50, 90, 50, false)
    }

    @Test
    fun parallelToHorizontalLine() {
        testCrossing(horizontalSegment, 10, 40, 20, 40, false) // Parallel - start Before 1st Point
        testCrossing(horizontalSegment, 10, 40, 25, 40, false)
        testCrossing(horizontalSegment, 10, 40, 50, 40, false)
        testCrossing(horizontalSegment, 10, 40, 75, 40, false)
        testCrossing(horizontalSegment, 10, 40, 90, 40, false)
        testCrossing(horizontalSegment, 25, 40, 50, 40, false) // Parallel - Start at 1st Point
        testCrossing(horizontalSegment, 25, 40, 75, 40, false)
        testCrossing(horizontalSegment, 25, 40, 90, 40, false)
        testCrossing(horizontalSegment, 50, 40, 60, 40, false) // Parallel - Start inside segment
        testCrossing(horizontalSegment, 50, 40, 75, 40, false)
        testCrossing(horizontalSegment, 50, 40, 90, 40, false)
        testCrossing(horizontalSegment, 75, 40, 90, 40, false) // Parallel - Start at 2nd Point
        testCrossing(horizontalSegment, 80, 40, 90, 40, false) // Parallel - Start after 2nd Point
    }

    @Test
    fun obliqueToHorizontalLine() {
        testCrossing(horizontalSegment, 10, 10, 20, 20, false) // Oblique - start Before 1st Point
        testCrossing(horizontalSegment, 10, 10, 25, 25, false)
        testCrossing(horizontalSegment, 10, 10, 30, 30, false)
        testCrossing(horizontalSegment, 10, 10, 50, 50, true)
        testCrossing(horizontalSegment, 10, 10, 60, 60, true)
        testCrossing(horizontalSegment, 10, 10, 75, 75, true)
        testCrossing(horizontalSegment, 10, 10, 90, 90, true)
        testCrossing(horizontalSegment, 25, 25, 25, 25, false) // Oblique - start at 1st Point
        testCrossing(horizontalSegment, 25, 25, 30, 30, false)
        testCrossing(horizontalSegment, 25, 25, 50, 50, true)
        testCrossing(horizontalSegment, 25, 25, 60, 60, true)
        testCrossing(horizontalSegment, 25, 25, 75, 75, true)
        testCrossing(horizontalSegment, 25, 25, 90, 90, true)
        testCrossing(horizontalSegment, 30, 30, 30, 30, false) // Oblique - start after 1st Point
        testCrossing(horizontalSegment, 30, 30, 50, 50, true)
        testCrossing(horizontalSegment, 30, 30, 60, 60, true)
        testCrossing(horizontalSegment, 30, 30, 75, 75, true)
        testCrossing(horizontalSegment, 30, 30, 90, 90, true)
        testCrossing(horizontalSegment, 50, 50, 60, 60, true)// Oblique - start inside Segment
        testCrossing(horizontalSegment, 50, 50, 75, 75, true)
        testCrossing(horizontalSegment, 50, 50, 90, 90, true)
        testCrossing(horizontalSegment, 60, 60, 70, 70, false) // Oblique - start after half of Segment
        testCrossing(horizontalSegment, 60, 60, 75, 75, false)
        testCrossing(horizontalSegment, 60, 60, 90, 90, false)
        testCrossing(horizontalSegment, 75, 75, 75, 75, false) // Oblique - start at 2nd point
        testCrossing(horizontalSegment, 75, 75, 90, 90, false)
        testCrossing(horizontalSegment, 80, 80, 90, 90, false) // Oblique - after 2nd point
        testCrossing(horizontalSegment, 10, 30, 25, 50, false) // Touching
        testCrossing(horizontalSegment, 75, 50, 85, 60, false)
    }

    @Test
    fun perpendicularToHorizontalLine() {
        testCrossing(horizontalSegment, 40, 10, 40, 40, false)
        testCrossing(horizontalSegment, 40, 10, 40, 50, true)
        testCrossing(horizontalSegment, 40, 10, 40, 90, true)
        testCrossing(horizontalSegment, 40, 50, 40, 90, true)
        testCrossing(horizontalSegment, 40, 60, 40, 90, false)
        testCrossing(horizontalSegment, 25, 30, 25, 50, false) // Touching
        testCrossing(horizontalSegment, 75, 50, 75, 70, false)
    }

    // OBLIQUE SEGMENT - START

    @Test
    fun colinearToObliqueLine() {
        testCrossing(obliqueSegment, 10, 10, 20, 20, false) // Colinear - start Before 1st Point
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                obliqueSegment,
                10,
                10,
                25,
                25,
                false
            )
        } // Touching
        testCrossing(obliqueSegment, 10, 10, 50, 50, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(obliqueSegment, 10, 10, 75, 75, true) }
        testCrossing(obliqueSegment, 10, 10, 90, 90, true)
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                obliqueSegment,
                25,
                25,
                50,
                50,
                true
            )
        } // Colinear - Start at 1st Point
        testCrossing(obliqueSegment, 25, 25, 75, 75, true)
        assertThrows(IllegalStateException::class.java) { testCrossing(obliqueSegment, 25, 25, 90, 90, true) }
        testCrossing(obliqueSegment, 50, 50, 60, 60, true) // Colinear - Start inside segment
        assertThrows(IllegalStateException::class.java) { testCrossing(obliqueSegment, 50, 50, 75, 75, true) }
        testCrossing(obliqueSegment, 50, 50, 90, 90, true)
        assertThrows(IllegalStateException::class.java) {
            testCrossing(
                obliqueSegment,
                75,
                75,
                90,
                90,
                false
            )
        } // Touching
        testCrossing(obliqueSegment, 80, 80, 90, 90, false) // Colinear - Start after 2nd Point
    }

    @Test
    fun parallelToObliqueLine() {
        testCrossing(obliqueSegment, 0, 15, 5, 20, false) // Parallel - start Before 1st Point
        testCrossing(obliqueSegment, 0, 15, 25, 40, false)
        testCrossing(obliqueSegment, 0, 15, 50, 65, false)
        testCrossing(obliqueSegment, 0, 15, 75, 90, false)
        testCrossing(obliqueSegment, 0, 15, 90, 105, false)
        testCrossing(obliqueSegment, 25, 40, 50, 60, false) // Parallel - Start at 1st Point
        testCrossing(obliqueSegment, 25, 40, 75, 90, false)
        testCrossing(obliqueSegment, 25, 40, 90, 105, false)
        testCrossing(obliqueSegment, 50, 65, 70, 85, false) // Parallel - Start inside segment
        testCrossing(obliqueSegment, 50, 65, 75, 90, false)
        testCrossing(obliqueSegment, 50, 65, 90, 105, false)
        testCrossing(obliqueSegment, 75, 90, 90, 105, false) // Parallel - Start at 2nd Point
        testCrossing(obliqueSegment, 80, 95, 90, 105, false) // Parallel - Start after 2nd Point
    }

    @Test
    fun obliqueToObliqueLine() {
        testCrossing(obliqueSegment, 10, 50, 20, 50, false) // Oblique - start Before 1st Point
        testCrossing(obliqueSegment, 10, 50, 25, 50, false)
        testCrossing(obliqueSegment, 10, 50, 30, 50, false)
        testCrossing(obliqueSegment, 10, 50, 50, 50, true)
        testCrossing(obliqueSegment, 10, 50, 60, 50, true)
        testCrossing(obliqueSegment, 10, 50, 75, 50, true)
        testCrossing(obliqueSegment, 10, 50, 90, 50, true)
        testCrossing(obliqueSegment, 25, 50, 25, 50, false) // Oblique - start at 1st Point
        testCrossing(obliqueSegment, 25, 50, 30, 50, false)
        testCrossing(obliqueSegment, 25, 50, 50, 50, true)
        testCrossing(obliqueSegment, 25, 50, 60, 50, true)
        testCrossing(obliqueSegment, 25, 50, 75, 50, true)
        testCrossing(obliqueSegment, 25, 50, 90, 50, true)
        testCrossing(obliqueSegment, 30, 50, 30, 50, false) // Oblique - start after 1st Point
        testCrossing(obliqueSegment, 30, 50, 50, 50, true)
        testCrossing(obliqueSegment, 30, 50, 60, 50, true)
        testCrossing(obliqueSegment, 30, 50, 75, 50, true)
        testCrossing(obliqueSegment, 30, 50, 90, 50, true)
        testCrossing(obliqueSegment, 50, 50, 50, 50, true) // Oblique - start inside Segment
        testCrossing(obliqueSegment, 50, 50, 60, 50, true)
        testCrossing(obliqueSegment, 50, 50, 75, 50, true)
        testCrossing(obliqueSegment, 50, 50, 90, 50, true)
        testCrossing(obliqueSegment, 60, 50, 70, 50, false) // Oblique - start after half of Segment
        testCrossing(obliqueSegment, 60, 50, 75, 50, false)
        testCrossing(obliqueSegment, 60, 50, 90, 50, false)
        testCrossing(obliqueSegment, 75, 50, 75, 50, false) // Oblique - start at 2nd point
        testCrossing(obliqueSegment, 75, 50, 90, 50, false)
        testCrossing(obliqueSegment, 80, 50, 90, 50, false) // Oblique - after 2nd point
        testCrossing(obliqueSegment, 15, 25, 25, 25, false) // touching
        testCrossing(obliqueSegment, 75, 75, 90, 75, false)
    }

    @Test
    fun perpendicularToObliqueLine() {
        testCrossing(obliqueSegment, 10, 70, 20, 60, false)
        testCrossing(obliqueSegment, 10, 70, 25, 55, false)
        testCrossing(obliqueSegment, 10, 70, 40, 40, true)
        testCrossing(obliqueSegment, 10, 70, 50, 30, true)
        testCrossing(obliqueSegment, 10, 70, 75, 5, true)
        testCrossing(obliqueSegment, 10, 70, 90, -10, true)

        testCrossing(obliqueSegment, 25, 55, 35, 45, false)
        testCrossing(obliqueSegment, 25, 55, 40, 40, true)
        testCrossing(obliqueSegment, 25, 55, 50, 30, true)
        testCrossing(obliqueSegment, 25, 55, 75, 5, true)
        testCrossing(obliqueSegment, 25, 55, 90, -10, true)

        testCrossing(obliqueSegment, 35, 45, 40, 40, true)
        testCrossing(obliqueSegment, 35, 45, 50, 30, true)
        testCrossing(obliqueSegment, 35, 45, 75, 5, true)
        testCrossing(obliqueSegment, 35, 45, 90, -10, true)

        testCrossing(obliqueSegment, 40, 40, 40, 40, true)
        testCrossing(obliqueSegment, 40, 40, 50, 30, true)
        testCrossing(obliqueSegment, 40, 40, 75, 5, true)
        testCrossing(obliqueSegment, 40, 40, 90, -10, true)

        testCrossing(obliqueSegment, 45, 35, 50, 30, false)
        testCrossing(obliqueSegment, 45, 35, 75, 5, false)
        testCrossing(obliqueSegment, 45, 35, 90, -10, false)

        testCrossing(obliqueSegment, 75, 5, 90, -10, false)
        testCrossing(obliqueSegment, 80, 0, 90, -10, false)

        testCrossing(obliqueSegment, 15, 35, 25, 25, false) // Touching
        testCrossing(obliqueSegment, 75, 75, 85, 65, false)
    }


}