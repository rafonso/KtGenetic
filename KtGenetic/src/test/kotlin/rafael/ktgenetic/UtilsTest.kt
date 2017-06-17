package rafael.ktgenetic

import org.junit.Test
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    public fun testCreateCutPositions() {
        val maxPos = 100

        fun test() {
            val positions = createCutPositions(maxPos)
            assertTrue(positions.first >= 1, "First position should be greater and equals than 1: $positions")
            assertTrue(positions.first < (maxPos - 1), "First positions should be lesser then the ${maxPos - 1}: $positions")
            assertTrue(positions.first < positions.second, "First positions should be lesser then the second one: $positions")
            assertTrue(positions.second <= (maxPos - 1), "Second positions should be lesser then $maxPos: $positions")
        }

        (1..1000).forEach { test() }
    }

}