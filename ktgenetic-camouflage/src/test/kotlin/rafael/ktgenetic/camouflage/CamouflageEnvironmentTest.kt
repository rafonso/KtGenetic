package rafael.ktgenetic.camouflage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CamouflageEnvironmentTest {

    @Mock
    lateinit var distanceCalculator: KolorDistance

    private lateinit var environment: CamouflageEnvironment

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        environment = CamouflageEnvironment(Kolor(0, 0, 0), distanceCalculator, 10, 10)
    }

    @Test
    fun `getFirstGeneration returns list of size generationSize`() {
        val firstGeneration = environment.getFirstGeneration()
        assertEquals(10, firstGeneration.size)
    }

    @Test
    fun `getCutPositions returns pair with second value one more than first`() {
        val cutPositions = environment.getCutPositions()
        assertEquals(cutPositions.first + 1, cutPositions.second)
    }

    @Test
    fun `executeMutation changes one value in sequence`() {
        val originalSequence = listOf(1, 2, 3)
        val mutatedSequence = environment.executeMutation(originalSequence)
        val changedValues = originalSequence.zip(mutatedSequence).count { it.first != it.second }
        assertEquals(1, changedValues)
    }

    @Test
    fun `createNewChromosome creates Kolor from sequence`() {
        val sequence = listOf(1, 2, 3)
        val kolor = environment.createNewChromosome(sequence)
        assertEquals(Kolor(1, 2, 3), kolor)
    }

    @Test
    fun `calculateFitness uses distanceCalculator`() {
        val kolor = Kolor(1, 2, 3)
        Mockito.`when`(distanceCalculator.distance(kolor, environment.backgroundColor)).thenReturn(10.0)
        val fitness = environment.calculateFitness(kolor)
        assertEquals(0.09090909090909091, fitness)
    }
}