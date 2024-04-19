package rafael.ktgenetic.core.processor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rafael.ktgenetic.core.Chromosome

class GeneticCrossingTypeTest {

    @Test
    fun `SIMPLE returns SimpleGeneticCrosser`() {
        val crosser = GeneticCrossingType.SIMPLE.getGeneticCrosser<Char, Chromosome<Char>>()
        assertEquals(SimpleGeneticCrosser::class.java, crosser.javaClass)
    }

    @Test
    fun `ORDERED returns OrderedGeneticCrosser`() {
        val crosser = GeneticCrossingType.ORDERED.getGeneticCrosser<Char, Chromosome<Char>>()
        assertEquals(OrderedGeneticCrosser::class.java, crosser.javaClass)
    }
}