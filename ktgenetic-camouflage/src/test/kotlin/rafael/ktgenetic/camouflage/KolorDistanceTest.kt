package rafael.ktgenetic.camouflage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KolorDistanceTest {

    @Test
    fun `RGB distance between same colors is zero`() {
        val kolor1 = Kolor(100, 150, 200)
        val kolor2 = Kolor(100, 150, 200)
        val distance = KolorDistance.RGB.distance(kolor1, kolor2)
        assertEquals(0.0, distance)
    }

    @Test
    fun `RGB distance between different colors is not zero`() {
        val kolor1 = Kolor(100, 150, 200)
        val kolor2 = Kolor(200, 250, 100)
        val distance = KolorDistance.RGB.distance(kolor1, kolor2)
        assert(distance > 0)
    }

    @Test
    fun `HSL distance between same colors is zero`() {
        val kolor1 = Kolor(100, 150, 200)
        val kolor2 = Kolor(100, 150, 200)
        val distance = KolorDistance.HSL.distance(kolor1, kolor2)
        assertEquals(0.0, distance)
    }

    @Test
    fun `HSL distance between different colors is not zero`() {
        val kolor1 = Kolor(100, 150, 200)
        val kolor2 = Kolor(200, 250, 100)
        val distance = KolorDistance.HSL.distance(kolor1, kolor2)
        assert(distance > 0)
    }
}