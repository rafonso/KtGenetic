package rafael.ktgenetic.selection

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import rafael.ktgenetic.Chromosome
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

const val EPSLON = 0.00000001

const val REPETITION = 10

class RouletteSelectionOperatorTest {


    private val parentsDefault = listOf(
        TemplateChromosome("1.00 =", 1.00),
        TemplateChromosome("1.00 <", 1.00 - EPSLON),
        TemplateChromosome("0.75 >", 0.75 + EPSLON),
        TemplateChromosome("0.75 A", 0.75),
        TemplateChromosome("0.75 B", 0.75),
        TemplateChromosome("0.75 C", 0.75),
        TemplateChromosome("0.75 <", 0.75 - EPSLON),
        TemplateChromosome("0.50 =", 0.50),
        TemplateChromosome("0.25 >", 0.25 + EPSLON),
        TemplateChromosome("0.25 A", 0.25),
        TemplateChromosome("0.25 B", 0.25),
        TemplateChromosome("0.25 C", 0.25),
        TemplateChromosome("0.25 <", 0.25 - EPSLON),
        TemplateChromosome("0.00 >", 0.00 + EPSLON),
        TemplateChromosome("0.00 =", 0.00)
    )

    private fun assertWithRepetition(selected: List<Chromosome<*>>) {
        (2..selected.lastIndex).forEach { index ->
            assertTrue(
                selected[index].fitness <= selected[index - 1].fitness,
                "Fitness of Chromossome $index lesser then its prior: $selected"
            )
        }
    }

    private fun assertWithNoRepetition(selected: List<Chromosome<*>>) {
        (2..selected.lastIndex).forEach { index ->
            assertTrue(
                selected[index].fitness < selected[index - 1].fitness,
                "Fitness of Chromossome $index lesser or equals then its prior: $selected"
            )
        }
    }

    private fun testWithSize(size: Int, hasRepetition: Boolean) {
        val operator = createOperator(size, hasRepetition)
        val parents = parentsDefault.shuffled()

        val selected = operator.select(parents)

        if (hasRepetition) {
            assertWithRepetition(selected)
        } else {
            assertWithNoRepetition(selected)
        }
    }

    protected fun createOperator(generationSize: Int, allowRepetition: Boolean): SelectionOperator<TemplateChromosome> =
        RouletteSelectionOperator(generationSize, allowRepetition)


    @Test
    fun `Empty Parents with generation size 0 generates an Empty List`() {
        val operator = createOperator(0, true)
        val parents = emptyList<TemplateChromosome>()

        val selected = operator.select(parents)

        assertTrue(selected.isEmpty())
    }

    @Test
    fun `Empty Parents with generation positive throws IndexOutOfBoundsException`() {
        val operator = createOperator(5, true)
        val parents = emptyList<TemplateChromosome>()

        assertThrows<IndexOutOfBoundsException> {
            val selected = operator.select(parents)

            fail("it should not have selected an empty list: $selected")
        }
    }

    @Test
    fun `Singleton Parent with generation size 0 generates an empty List`() {
        val operator = createOperator(0, true)
        val origin = listOf(TemplateChromosome("only", 0.5))

        val selected = operator.select(origin)

        assertTrue(selected.isEmpty())
    }

    @Test
    fun `Singleton Parent with generation size 1 generates another singleton List`() {
        val operator = createOperator(1, true)
        val parents = listOf(TemplateChromosome("only", 0.5))

        val selected = operator.select(parents)

        assertEquals(parents, selected)
    }

    @Test
    fun `Singleton parent with Generation size 2 with repetition creates children with repetition`() {
        val operator = createOperator(2, true)
        val parent = TemplateChromosome("only", 0.5)
        val parents = listOf(parent)

        val selected = operator.select(parents)

        assertEquals(listOf(parent, parent), selected)
    }

    @Test
    fun `Singleton parent with Generation size 2 with no repetition creates children with size 1`() {
        val operator = createOperator(2, false)
        val parent = TemplateChromosome("only", 0.5)
        val parents = listOf(parent)

        val selected = operator.select(parents)

        assertEquals(parents, selected)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size 2 with repetition`() {
        testWithSize(2, true)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size 2 with no repetition`() {
        testWithSize(2, false)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size 5 with repetition`() {
        testWithSize(5, true)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size 5 with no repetition`() {
        testWithSize(5, false)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size 10 with repetition`() {
        testWithSize(10, true)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size 10 with no repetition`() {
        testWithSize(10, false)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size equals to parents size with repetition`() {
        testWithSize(parentsDefault.size, true)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size equals to parents size with no repetition`() {
        testWithSize(parentsDefault.size, false)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size greater to parents size with repetition`() {
        testWithSize(parentsDefault.size + 5, true)
    }

    @RepeatedTest(REPETITION)
    fun `Generation size greater to parents size with no repetition`() {
        testWithSize(parentsDefault.size + 5, false)
    }
}
