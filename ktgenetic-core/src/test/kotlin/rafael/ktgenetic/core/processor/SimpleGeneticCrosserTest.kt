package rafael.ktgenetic.core.processor

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import rafael.ktgenetic.core.TemplateChromosome
import rafael.ktgenetic.core.utils.makeCuttingIntoPieces
import java.util.stream.Stream
import kotlin.test.assertEquals

internal class SimpleGeneticCrosserTest {

    companion object {

        private val parent1 = TemplateChromosome("ABCDEF")

        private val parent2 = TemplateChromosome("123456")

        @JvmStatic
        fun provideWithoutIntersectionValidPositionsArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(0, 0, "123456", "ABCDEF"),
            Arguments.of(0, 1, "A23456", "1BCDEF"),
            Arguments.of(0, 2, "AB3456", "12CDEF"),
            Arguments.of(0, 5, "ABCDE6", "12345F"),
            Arguments.of(0, 6, "ABCDEF", "123456"),

            Arguments.of(1, 1, "123456", "ABCDEF"),
            Arguments.of(1, 2, "1B3456", "A2CDEF"),
            Arguments.of(1, 5, "1BCDE6", "A2345F"),
            Arguments.of(1, 6, "1BCDEF", "A23456"),

            Arguments.of(2, 2, "123456", "ABCDEF"),
            Arguments.of(2, 5, "12CDE6", "AB345F"),
            Arguments.of(2, 6, "12CDEF", "AB3456"),

            Arguments.of(5, 5, "123456", "ABCDEF"),
            Arguments.of(5, 6, "12345F", "ABCDE6"),

            Arguments.of(6, 6, "123456", "ABCDEF"),
        )

    }

    private val environmentStub = GeneticCrosserTestEnvironmentStub()

    private val geneticCrosser = SimpleGeneticCrosser<Char, TemplateChromosome>()

    @ParameterizedTest
    @MethodSource("provideWithoutIntersectionValidPositionsArguments")
    fun withoutIntersectionValidPositions(cutPosition1: Int, cutPosition2: Int, child0: String, child1: String) {
        val cutPositons = Pair(cutPosition1, cutPosition2)
        val pieces1 = makeCuttingIntoPieces(parent1.content, cutPositons)
        val pieces2 = makeCuttingIntoPieces(parent2.content, cutPositons)

        val result = geneticCrosser.executeCrossing(pieces1, pieces2, environmentStub)

        assertEquals(child0, result[0].toWord())
        assertEquals(child1, result[1].toWord())
    }

}
