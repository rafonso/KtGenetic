package rafael.ktgenetic.pallete

import org.junit.jupiter.api.Test
import kotlin.math.sqrt
import kotlin.test.assertEquals

class PointTest {

    @Test fun point0() {
        val pt = Point(0.0, 0.0)

        assertEquals(0.0, pt.col)
        assertEquals(0.0, pt.row)
    }

    @Test fun distance1() {
        val pt0 = Point(0.0, 0.0)
        val pt1 = Point(0.0, 1.0)
        val pt2 = Point(1.0, 1.0)

        assertEquals(1.0, pt0.distance(pt1))
        assertEquals(pt1.distance(pt0), pt0.distance(pt1))
        assertEquals(sqrt(2.0), pt0.distance(pt2))
    }

    @Test fun test345() {
        val pt0 = Point(0.0, 0.0)
        val pt1 = Point(3.0, 4.0)

        assertEquals(5.0, pt0.distance(pt1))
    }

}