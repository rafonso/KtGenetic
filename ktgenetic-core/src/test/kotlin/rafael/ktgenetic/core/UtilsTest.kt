package rafael.ktgenetic.core

import org.junit.jupiter.api.Test
import rafael.ktgenetic.core.utils.createCutPositions
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun testCreateCutPositions() {
        val maxPos = 100

        fun test() {
            val positions = createCutPositions(maxPos)
            assertTrue(positions.first >= 1, "First position should be greater and equals than 1: $positions")
            assertTrue(positions.first < (maxPos - 1), "First size should be lesser then the ${maxPos - 1}: $positions")
            assertTrue(positions.first < positions.second, "First size should be lesser then the second one: $positions")
            assertTrue(positions.second <= (maxPos - 1), "Second size should be lesser then $maxPos: $positions")
        }

        (1..1000).forEach { _ -> test() }
    }

}
