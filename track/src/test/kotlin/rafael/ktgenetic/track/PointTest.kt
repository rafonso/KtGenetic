package rafael.ktgenetic.track

import org.junit.*
import kotlin.test.*

class PointTest {

    private fun testDirectionsFrom0(h: Int, v: Int, vararg directions: Direction) {
        val p = directions.fold(Point()) { p, d -> p + d }

        assertEquals(h, p.hPos)
        assertEquals(v, p.vPos)
    }

    @Test
    public fun defaultPointAs0() = testDirectionsFrom0(0, 0)

    @Test
    public fun movePointToLeft() = testDirectionsFrom0(-1, 0, Direction.L)

    @Test
    public fun movePointToTop() = testDirectionsFrom0(0, 1, Direction.T)

    @Test
    public fun movePointToBottom() = testDirectionsFrom0(0, -1, Direction.B)

    @Test
    public fun movePointToRight() = testDirectionsFrom0(1, 0, Direction.R)

    @Test
    public fun movePointASClosedCicle() = testDirectionsFrom0(0, 0,
            Direction.T, Direction.R, Direction.B, Direction.L)


}