package rafael.ktgenetic.core

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import rafael.ktgenetic.core.events.GenerationStatistics
import rafael.ktgenetic.core.events.ProcessorEvent
import rafael.ktgenetic.core.events.TypeProcessorEvent
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class MutationTunerTest {

    @Mock
    private lateinit var environment: Environment<*, TemplateChromosome>

    @Mock
    private lateinit var event: ProcessorEvent<*>

    @Mock
    private lateinit var statistics: GenerationStatistics


    private lateinit var mutationTuner: MutationTuner<TemplateChromosome>

    @BeforeEach
    fun setUp() {
        // Create the MutationTuner with the mocked environment
        mutationTuner = MutationTuner(environment)
    }

    @Test
    fun `does Not Change Mutation Factor When EventType Is Not GENERATION_EVALUATED`() {
        // sets up the mock environment to return a mutation factor of 0.5
        `when`(environment.mutationFactor).thenReturn(0.5)
        `when`(event.eventType).thenReturn(TypeProcessorEvent.ENDED_BY_FITNESS)

        mutationTuner.onEvent(event)

        assertEquals(0.5, environment.mutationFactor)

        verify(event, never()).statistics
        verify(environment, never()).mutationFactor = anyDouble()
    }


    @Test
    fun `mutation factor increases when proportion is less than minimum variation and mutation factor is not maximum`() {
        `when`(environment.mutationFactor).thenReturn(0.98)
        `when`(event.eventType).thenReturn(TypeProcessorEvent.GENERATION_EVALUATED)
        `when`(event.statistics).thenReturn(statistics)
        `when`(statistics.averageFitnessDeviation).thenReturn(0.009)
        `when`(statistics.averageFitness).thenReturn(1.0)

        mutationTuner.onEvent(event)

        verify(environment).mutationFactor = 0.99
    }

    @Test
    fun `mutation factor decreases when proportion is more than maximum variation and mutation factor is not minimum`() {
        `when`(environment.mutationFactor).thenReturn(0.02)
        `when`(event.eventType).thenReturn(TypeProcessorEvent.GENERATION_EVALUATED)
        `when`(event.statistics).thenReturn(statistics)
        `when`(statistics.averageFitnessDeviation).thenReturn(0.06)
        `when`(statistics.averageFitness).thenReturn(1.0)

        mutationTuner.onEvent(event)

        verify(environment).mutationFactor = 0.01
    }

    @ParameterizedTest
    @CsvSource("0.03, 0.90", "0.005, 0.995", "0.06, 0.02", "0.06, 0.005")
    fun `mutation factor remains the same when proportion is within variation range`(averageFitnessDeviation: Double, mutationFactor: Double) {
        `when`(environment.mutationFactor).thenReturn(mutationFactor)
        `when`(event.eventType).thenReturn(TypeProcessorEvent.GENERATION_EVALUATED)
        `when`(event.statistics).thenReturn(statistics)
        `when`(statistics.averageFitnessDeviation).thenReturn(averageFitnessDeviation)
        `when`(statistics.averageFitness).thenReturn(1.0)

        mutationTuner.onEvent(event)

        assertEquals(mutationFactor, environment.mutationFactor)
    }

}