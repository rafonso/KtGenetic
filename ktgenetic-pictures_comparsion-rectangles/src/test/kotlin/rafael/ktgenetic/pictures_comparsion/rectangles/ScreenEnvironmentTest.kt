package rafael.ktgenetic.pictures_comparsion.rectangles

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class ScreenEnvironmentTest {
    val originalBitmaps = arrayOf(
        arrayOf(Kolor(255, 0, 0), Kolor(0, 255, 0), Kolor(0, 0, 255)),
        arrayOf(Kolor(255, 255, 0), Kolor(0, 255, 255), Kolor(255, 0, 255)),
        arrayOf(Kolor(255, 255, 255), Kolor(128, 128, 128), Kolor(0, 0, 0))
    )
    val rows = 3
    val cols = 3

    @Test
    fun `getFirstGeneration should return list of screens with random colors`() {
        val screenEnvironment = ScreenEnvironment(originalBitmaps, rows, cols)
        val firstGeneration = screenEnvironment.getFirstGeneration()
        assertNotEquals(firstGeneration[0].content[0].kolor, firstGeneration[1].content[0].kolor)
    }

    @Test
    fun `getCutPositions should return valid cut positions`() {
        val screenEnvironment = ScreenEnvironment(originalBitmaps, rows, cols)
        val cutPositions = screenEnvironment.getCutPositions()
        assertTrue(cutPositions.first < cutPositions.second)
    }

    @Test
    fun `executeMutation should change color of one rectangle`() {
        val screenEnvironment = ScreenEnvironment(originalBitmaps, rows, cols)
        val originalSequence = listOf(Rectangle(Position(0, 0), Position(1, 1), Kolor(255, 255, 255)))
        val mutatedSequence = screenEnvironment.executeMutation(originalSequence)
        assertNotEquals(originalSequence[0].kolor, mutatedSequence[0].kolor)
    }

    @Test
    fun `createNewChromosome should return new screen with given sequence`() {
        val screenEnvironment = ScreenEnvironment(originalBitmaps, rows, cols)
        val sequence = listOf(Rectangle(Position(0, 0), Position(1, 1), Kolor(255, 255, 255)))
        val newScreen = screenEnvironment.createNewChromosome(sequence)
        assertEquals(sequence, newScreen.content)
    }

    @Disabled("Analyse better ...")
    @Test
    fun `calculateFitness should return valid fitness value`() {
        val screenEnvironment = ScreenEnvironment(originalBitmaps, rows, cols)
        val screen = Screen(listOf(Rectangle(Position(0, 0), Position(1, 1), Kolor(255, 255, 255))))
        val fitness = screenEnvironment.calculateFitness(screen)
        assertTrue(fitness in 0.0..1.0, "Fitness value should be between 0 and 1, but was $fitness")
    }
}