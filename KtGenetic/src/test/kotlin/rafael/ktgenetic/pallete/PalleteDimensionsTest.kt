package rafael.ktgenetic.pallete

import org.junit.Test
import kotlin.test.assertEquals

class PalleteDimensionsTest {

    @Test fun dimension0() {
        val dimensions0 = PalleteDimensions(0, 0)

        assertEquals(0.0, dimensions0.center.col)
        assertEquals(0.0, dimensions0.center.row)
        assertEquals(0, dimensions0.points.size)
        assertEquals(0, dimensions0.distanceFromCenter.size)
        assertEquals(0, dimensions0.blocksByRow.size)
        assertEquals(0, dimensions0.blocksByColumn.size)
        //assertEquals(0, dimensions0.blockToIndex(0, 0))
    }

    @Test fun dimension1_1() {
        val dimensions = PalleteDimensions(1, 1)

        assertEquals(0.5, dimensions.center.col)
        assertEquals(0.5, dimensions.center.row)
        assertEquals(1, dimensions.points.size)
        assertEquals(1, dimensions.distanceFromCenter.size)
        assertEquals(0.0, dimensions.distanceFromCenter[0])
        assertEquals(1, dimensions.blocksByRow.size)
        assertEquals(1, dimensions.blocksByColumn.size)
        assertEquals(0, dimensions.blockToIndex(0, 0))
    }

    @Test fun dimension2_2() {
        val dimensions = PalleteDimensions(2, 2)

        assertEquals(1.0, dimensions.center.col)
        assertEquals(1.0, dimensions.center.row)
        assertEquals(4, dimensions.points.size)
        assertEquals(Point(0.5, 0.5), dimensions.points[0])
        assertEquals(Point(0.5, 1.5), dimensions.points[1])
        assertEquals(Point(1.5, 0.5), dimensions.points[2])
        assertEquals(Point(1.5, 1.5), dimensions.points[3])
        assertEquals(4, dimensions.distanceFromCenter.size)
        assertEquals(Math.sqrt(2.0) / 2, dimensions.distanceFromCenter[0])
        assertEquals(dimensions.rows, dimensions.blocksByRow.size)
        assertEquals(dimensions.cols, dimensions.blocksByColumn.size)
        assertEquals(3, dimensions.blockToIndex(1, 1))
    }

    @Test fun dimension3_2() {
        val dimensions = PalleteDimensions(3, 2)

        assertEquals(1.0, dimensions.center.col)
        assertEquals(1.5, dimensions.center.row)
        assertEquals(6, dimensions.points.size)
        assertEquals(Point(0.5, 0.5), dimensions.points[0])
        assertEquals(Point(0.5, 1.5), dimensions.points[1])
        assertEquals(Point(1.5, 0.5), dimensions.points[2])
        assertEquals(Point(1.5, 1.5), dimensions.points[3])
        assertEquals(6, dimensions.distanceFromCenter.size)
        assertEquals(Math.sqrt(5.0) / 2, dimensions.distanceFromCenter[0])
        assertEquals(dimensions.rows, dimensions.blocksByRow.size)
        assertEquals(dimensions.cols, dimensions.blocksByColumn.size)
    }



}