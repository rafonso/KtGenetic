package rafael.ktgenetic.track

import org.junit.Test
import java.lang.IndexOutOfBoundsException
import kotlin.test.assertEquals

class PathTest {

    private fun testPath(directions: List<Direction>, expectedTrack: List<Point>, expectedStatus: PathStatus) {
        val path = Path(directions)

        path.calculatePath(5, 5)

        assertEquals(expectedTrack, path.track)
        assertEquals(expectedStatus, path.status)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun zeroDirections() = testPath(listOf(), listOf(Point(0, 0)), PathStatus.SUSPENDED)

    @Test
    fun reverse() = testPath(listOf(Direction.B), listOf(Point(0, 0)), PathStatus.STUCK)

    @Test
    fun reverse2() = testPath(listOf(Direction.B, Direction.T), listOf(Point(0, 0)), PathStatus.STUCK)

    @Test
    fun goTo1_0() = testPath(listOf(Direction.R), listOf(Point(0, 0), Point(1, 0)), PathStatus.SUSPENDED)

    @Test
    fun goTo1_1() = testPath(listOf(Direction.R, Direction.T), listOf(Point(0, 0), Point(1, 0), Point(1, 1)), PathStatus.SUSPENDED)

    @Test
    fun turnAround1() = testPath(
            listOf(Direction.T, Direction.R, Direction.B, Direction.L),
            listOf(Point(0, 0), Point(0, 1), Point(1, 1), Point(1, 0), Point(0, 0)),
            PathStatus.SUSPENDED
    )

    @Test
    fun goToTargetZigZag() = testPath(
            listOf(Direction.T, Direction.R, Direction.T, Direction.R, Direction.T, Direction.R, Direction.T, Direction.R, Direction.T, Direction.R),
            listOf(
                    Point(0, 0), Point(0, 1),
                    Point(1, 1), Point(1, 2),
                    Point(2, 2), Point(2, 3),
                    Point(3, 3), Point(3, 4),
                    Point(4, 4), Point(4, 5),
                    Point(5, 5)
            ),
            PathStatus.TARGETED
    )

    @Test(expected = AssertionError::class)
    fun redoPathAfterExecution() {
        val path = Path(listOf(Direction.B, Direction.T))
        path.calculatePath(5, 5)
        path.calculatePath(5, 5)
    }

}