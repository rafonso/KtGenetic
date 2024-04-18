package rafael.ktgenetic.core.events

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rafael.ktgenetic.core.TemplateChromosome
import kotlin.math.sqrt

class ProcessorEventTest {

    @Test
    fun `statistics should calculate correct statistics for given population`() {
        val population = listOf(
            TemplateChromosome("A", 1.0),
            TemplateChromosome("B", 2.0),
            TemplateChromosome("C", 3.0),
            TemplateChromosome("D", 4.0),
            TemplateChromosome("E", 5.0)
        )
        val event = ProcessorEvent(TypeProcessorEvent.GENERATION_EVALUATED, 1, population)

        val statistics = event.statistics

        assertEquals(5.0, statistics.bestFitness)
        assertEquals(3.0, statistics.averageFitness)
        assertEquals(sqrt(2.0) / 2, statistics.averageFitnessDeviation)
    }

    @Test
    fun `statistics should handle single chromosome population`() {
        val population = listOf(TemplateChromosome("A", 1.0))
        val event = ProcessorEvent(TypeProcessorEvent.GENERATION_EVALUATED, 1, population)

        val statistics = event.statistics

        assertEquals(1.0, statistics.bestFitness)
        assertEquals(1.0, statistics.averageFitness)
        assertEquals(Double.NaN, statistics.averageFitnessDeviation)
    }

    @Test
    fun `statistics should handle population with identical fitness`() {
        val population = listOf(
            TemplateChromosome("B", 2.0),
            TemplateChromosome("B", 2.0),
            TemplateChromosome("B", 2.0),
            TemplateChromosome("B", 2.0),
            TemplateChromosome("B", 2.0),
        )
        val event = ProcessorEvent(TypeProcessorEvent.GENERATION_EVALUATED, 1, population)

        val statistics = event.statistics

        assertEquals(2.0, statistics.bestFitness)
        assertEquals(2.0, statistics.averageFitness)
        assertEquals(0.0, statistics.averageFitnessDeviation)
    }

}