package rafael.ktgenetic.core.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ListPiecesTest {

    companion object {

        private val baseList = listOf(1, 2, 3, 4, 5, 6)

        @JvmStatic
        fun provideTestJoinArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(listOf<Int>(), listOf<Int>(), listOf<Int>(), listOf<Int>()),
            Arguments.of(listOf<Int>(), listOf<Int>(), listOf(8, 9), listOf(8, 9)),
            Arguments.of(listOf<Int>(), listOf(5, 6), listOf<Int>(), listOf(5, 6)),
            Arguments.of(listOf<Int>(), listOf(5, 6), listOf(8, 9), listOf(5, 6, 8, 9)),
            Arguments.of(listOf(1, 2), listOf<Int>(), listOf<Int>(), listOf(1, 2)),
            Arguments.of(listOf(1, 2), listOf<Int>(), listOf(8, 9), listOf(1, 2, 8, 9)),
            Arguments.of(listOf(1, 2), listOf(4, 5), listOf<Int>(), listOf(1, 2, 4, 5)),
            Arguments.of(listOf(1, 2), listOf(4, 5), listOf(8, 9), listOf(1, 2, 4, 5, 8, 9)),
        )

        @JvmStatic
        private fun provideTestMakeCuttingIntoPiecesValidPositions(): Stream<Arguments> = Stream.of(
            Arguments.of(0, 0, listOf<Int>(), listOf<Int>(), baseList),
            Arguments.of(0, 1, listOf<Int>(), listOf(1), listOf(2, 3, 4, 5, 6)),
            Arguments.of(0, 2, listOf<Int>(), listOf(1, 2), listOf(3, 4, 5, 6)),
            Arguments.of(0, 5, listOf<Int>(), listOf(1, 2, 3, 4, 5), listOf(6)),
            Arguments.of(0, 6, listOf<Int>(), listOf(1, 2, 3, 4, 5, 6), listOf<Int>()),

            Arguments.of(1, 1, listOf(1), listOf<Int>(), listOf(2, 3, 4, 5, 6)),
            Arguments.of(1, 2, listOf(1), listOf(2), listOf(3, 4, 5, 6)),
            Arguments.of(1, 5, listOf(1), listOf(2, 3, 4, 5), listOf(6)),
            Arguments.of(1, 6, listOf(1), listOf(2, 3, 4, 5, 6), listOf<Int>()),

            Arguments.of(2, 2, listOf(1, 2), listOf<Int>(), listOf(3, 4, 5, 6)),
            Arguments.of(2, 5, listOf(1, 2), listOf(3, 4, 5), listOf(6)),
            Arguments.of(2, 6, listOf(1, 2), listOf(3, 4, 5, 6), listOf<Int>()),

            Arguments.of(5, 5, listOf(1, 2, 3, 4, 5), listOf<Int>(), listOf(6)),
            Arguments.of(5, 6, listOf(1, 2, 3, 4, 5), listOf(6), listOf<Int>()),

            Arguments.of(6, 6, listOf(1, 2, 3, 4, 5, 6), listOf<Int>(), listOf<Int>()),
        )

        @JvmStatic
        private fun provideTestMakeCuttingIntoPiecesInvalidPositions(): Stream<Arguments> = Stream.of(
            Arguments.of(-2, -1),
            Arguments.of(-2, 0),
            Arguments.of(-2, 3),
            Arguments.of(-2, 6),
            Arguments.of(-2, 8),

            Arguments.of(0, -1),
            Arguments.of(0, 8),

            Arguments.of(3, -1),
            Arguments.of(3, 0),
            Arguments.of(3, 8),

            Arguments.of(6, -1),
            Arguments.of(6, 0),
            Arguments.of(6, 3),
            Arguments.of(6, 8),
        )

        @JvmStatic
        private fun provideTestMakeCuttingIntoPiecesEmptyListInvalidPositions(): Stream<Arguments> = Stream.of(
            Arguments.of(-2, -1),
            Arguments.of(-2, 0),
            Arguments.of(-2, 2),
            Arguments.of(0, -2),
            Arguments.of(0, 2),
            Arguments.of(1, 2),
        )

    }

    @ParameterizedTest
    @MethodSource("provideTestJoinArguments")
    fun testJoin(left: List<Int>, core: List<Int>, right: List<Int>, result: List<Int>) {
        val listPieces = ListPieces(left, core, right)

        assertEquals(result, listPieces.join)
    }

    @Test
    fun testMakeCuttingIntoPiecesEmptyListValidPositions() {
        val result = makeCuttingIntoPieces(listOf<Any>(), Pair(0, 0))

        assertTrue(result.left.isEmpty(), "Left")
        assertTrue(result.core.isEmpty(), "Core")
        assertTrue(result.right.isEmpty(), "Right")
    }

    @ParameterizedTest
    @MethodSource("provideTestMakeCuttingIntoPiecesEmptyListInvalidPositions")
    fun testMakeCuttingIntoPiecesEmptyListInvalidPositions(pos1: Int, pos2: Int) {
        assertThrows(IllegalArgumentException::class.java) {
            val result = makeCuttingIntoPieces(listOf<Any>(), Pair(pos1, pos2))

            fail("It should have thrown an exception for positions $pos1 and $pos2: $result")
        }

    }


    @ParameterizedTest
    @MethodSource("provideTestMakeCuttingIntoPiecesValidPositions")
    fun testMakeCuttingIntoPiecesValidPositions(
        pos1: Int,
        pos2: Int,
        left: List<Int>,
        core: List<Int>,
        right: List<Int>
    ) {
        val result = makeCuttingIntoPieces(baseList, Pair(pos1, pos2))

        assertEquals(left, result.left, "Left")
        assertEquals(core, result.core, "Core")
        assertEquals(right, result.right, "Right")
    }

    @ParameterizedTest
    @MethodSource("provideTestMakeCuttingIntoPiecesInvalidPositions")
    fun testMakeCuttingIntoPiecesInvalidPositions(
        pos1: Int,
        pos2: Int
    ) {
        assertThrows(IllegalArgumentException::class.java) {
            val result = makeCuttingIntoPieces(baseList, Pair(pos1, pos2))

            fail("It should have thrown an exception for positions $pos1 and $pos2: $result")
        }
    }

}
