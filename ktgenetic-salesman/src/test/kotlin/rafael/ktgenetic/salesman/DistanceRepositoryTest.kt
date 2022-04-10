package rafael.ktgenetic.salesman

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DistanceRepositoryTest {

    @Test
    fun test3Points() {
        val p00 = Point(0, 0)
        val p03 = Point(0, 3)
        val p40 = Point(4, 0)

        assertEquals(3.0, DistanceRepository.getDistance(p00, p03))
        assertEquals(4.0, DistanceRepository.getDistance(p00, p40))
        assertEquals(5.0, DistanceRepository.getDistance(p03, p40))

        assertEquals(3.0, DistanceRepository.getDistance(p00, p03))
        assertEquals(4.0, DistanceRepository.getDistance(p00, p40))
        assertEquals(5.0, DistanceRepository.getDistance(p03, p40))

        assertEquals(3.0, DistanceRepository.getDistance(p03, p00))
        assertEquals(4.0, DistanceRepository.getDistance(p40, p00))
        assertEquals(5.0, DistanceRepository.getDistance(p40, p03))
    }


}