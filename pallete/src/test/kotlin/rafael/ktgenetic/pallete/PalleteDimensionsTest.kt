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

    @Test fun frontAndBackHalves1_1() {
        val dimensions = PalleteDimensions(1, 1)
        val halves = dimensions.frontAndBackHalves
        assertEquals(Pair(listOf(), listOf()), halves)
    }

    @Test fun frontAndBackHalves2_1() {
        val dimensions = PalleteDimensions(2, 1)
        val halves = dimensions.frontAndBackHalves
        assertEquals(Pair(listOf(0), listOf(1)), halves)
    }

    @Test fun frontAndBackHalves1_2() {
        val dimensions = PalleteDimensions(1, 2)
        val halves = dimensions.frontAndBackHalves
        assertEquals(Pair(listOf(), listOf()), halves)
    }

    @Test fun frontAndBackHalves2_2() {
        val dimensions = PalleteDimensions(2, 2)
        val halves = dimensions.frontAndBackHalves
        assertEquals(Pair(listOf(0, 1), listOf(2, 3)), halves)
    }

    @Test fun frontAndBackHalves3_3() {
        val dimensions = PalleteDimensions(3, 3)
        val halves = dimensions.frontAndBackHalves
        assertEquals(Pair(listOf(0, 1, 2), listOf(6, 7, 8)), halves)
    }

    @Test fun frontAndBackHalves4_5() {
        val dimensions = PalleteDimensions(4, 5)
        val halves = dimensions.frontAndBackHalves
        assertEquals(Pair(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), listOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)), halves)
    }

    @Test fun frontAndBackHalves5_4() {
        val dimensions = PalleteDimensions(5, 4)
        val halves = dimensions.frontAndBackHalves
        assertEquals(Pair(listOf(0, 1, 2, 3, 4, 5, 6, 7), listOf(12, 13, 14, 15, 16, 17, 18, 19)), halves)
    }

    @Test fun leftAndRightHalves1_1() {
        val dimensions = PalleteDimensions(1, 1)
        val halves = dimensions.leftAndRightHalves
        assertEquals(Pair(listOf(), listOf()), halves)
    }

    @Test fun leftAndRightHalves2_1() {
        val dimensions = PalleteDimensions(2, 1)
        val halves = dimensions.leftAndRightHalves
        assertEquals(Pair(listOf(), listOf()), halves)
    }

    @Test fun leftAndRightHalves1_2() {
        val dimensions = PalleteDimensions(1, 2)
        val halves = dimensions.leftAndRightHalves
        assertEquals(Pair(listOf(0), listOf(1)), halves)
    }

    @Test fun leftAndRightHalves2_2() {
        val dimensions = PalleteDimensions(2, 2)
        val halves = dimensions.leftAndRightHalves
        assertEquals(Pair(listOf(0, 2), listOf(1, 3)), halves)
    }

    @Test fun leftAndRightHalves3_3() {
        val dimensions = PalleteDimensions(3, 3)
        val halves = dimensions.leftAndRightHalves
        assertEquals(Pair(listOf(0, 3, 6), listOf(2, 5, 8)), halves)
    }

    @Test fun leftAndRightHalves4_5() {
        val dimensions = PalleteDimensions(4, 5)
        val halves = dimensions.leftAndRightHalves
        assertEquals(Pair(listOf(0, 1, 5, 6, 10, 11, 15, 16), listOf(3, 4, 8, 9, 13, 14, 18, 19)), halves)
    }

    @Test fun leftAndRightHalves5_4() {
        val dimensions = PalleteDimensions(5, 4)
        val halves = dimensions.leftAndRightHalves
        assertEquals(Pair(listOf(0, 1, 4, 5, 8, 9, 12, 13, 16, 17), listOf(2, 3, 6, 7, 10, 11, 14, 15, 18, 19)), halves)
    }

}