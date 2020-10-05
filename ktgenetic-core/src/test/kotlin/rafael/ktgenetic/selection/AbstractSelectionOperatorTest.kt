package rafael.ktgenetic.selection

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import rafael.ktgenetic.Chromosome
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private const val EPSLON = 0.00000001

const val REPETITION = 10

private val singleChromosome = TemplateChromosome("only", 0.5)

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

val sizeDefault = parentsDefault.size

abstract class AbstractSelectionOperatorTest {

    protected enum class ParentsType(
        val getParents: () -> List<TemplateChromosome>,
        val validate: (List<Chromosome<*>>, Boolean) -> Unit
    ) {
        EMPTY({ emptyList() }, { selected, _ ->
            assertTrue(selected.isEmpty())
        }),
        SINGLE({ listOf(singleChromosome) }, { selected, _ ->
            selected.forEach { c -> assertEquals(singleChromosome, c, selected.toString()) }
        }),
        SHUFFLE({ parentsDefault.shuffled() }, { selected, hasRepetition ->
            val comparator: (Double, Double) -> Boolean = if (hasRepetition) { x, y -> x <= y } else { x, y -> x < y }
            val complMessage = if (hasRepetition) "lesser" else "lesser or equals"

            (2..selected.lastIndex).forEach { index ->
                assertTrue(
                    comparator(selected[index].fitness, selected[index - 1].fitness),
                    "Fitness of Chromossome $index $complMessage then its prior: $selected"
                )
            }
        })
    }

    private fun testWithSize(parentsType: ParentsType, size: Int, hasRepetition: Boolean) {
        val operator = createOperator(size, hasRepetition)
        val parents = parentsType.getParents()

        val selected = operator.select(parents)

        parentsType.validate(selected, hasRepetition)
    }

    protected inline fun <reified E : Throwable> testWithSizeError(
        parentsType: ParentsType,
        size: Int,
        hasRepetition: Boolean,
        exClass: Class<E>
    ) {
        val operator = createOperator(size, hasRepetition)
        val parents = parentsType.getParents()

        assertThrows<E> {
            val selected = operator.select(parents)

            fail("it should not have selected an empty list: $selected")
        }
    }


    protected abstract fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome>

    @Test
    @Order(10)
    fun `Empty Parents with generation size 0 generates an Empty List`() {
        testWithSize(ParentsType.EMPTY, 0, true)
    }

    @Test
    @Order(20)
    open fun `Empty Parents with generation positive throws IndexOutOfBoundsException`() {
        this.testWithSizeError(ParentsType.EMPTY, 5, true, IndexOutOfBoundsException::class.java)
    }

    @Test
    @Order(30)
    fun `Singleton Parent with generation size 0 generates an empty List`() {
        testWithSize(ParentsType.EMPTY, 0, true)
    }

    @Test
    @Order(40)
    open fun `Singleton Parent with generation size 1 generates another singleton List`() {
        testWithSize(ParentsType.SINGLE, 1, true)
    }

    @Test
    @Order(50)
    open fun `Singleton parent with Generation size 2 with repetition creates children with repetition`() {
        testWithSize(ParentsType.SINGLE, 2, true)
    }

    @Test
    @Order(60)
    open fun `Singleton parent with Generation size 2 with no repetition creates children with size 1`() {
        testWithSize(ParentsType.SINGLE, 2, false)
    }

    @RepeatedTest(REPETITION)
    @Order(70)
    fun `Generation size 2 with repetition`() {
        testWithSize(ParentsType.SHUFFLE, 2, true)
    }

    @RepeatedTest(REPETITION)
    @Order(80)
    fun `Generation size 2 with no repetition`() {
        testWithSize(ParentsType.SHUFFLE, 2, false)
    }

    @RepeatedTest(REPETITION)
    @Order(90)
    open fun `Generation size 5 with repetition`() {
        testWithSize(ParentsType.SHUFFLE, 5, true)
    }

    @RepeatedTest(REPETITION)
    @Order(100)
    fun `Generation size 5 with no repetition`() {
        testWithSize(ParentsType.SHUFFLE, 5, false)
    }

    @RepeatedTest(REPETITION)
    @Order(110)
    open fun `Generation size 10 with repetition`() {
        testWithSize(ParentsType.SHUFFLE, 10, true)
    }

    @RepeatedTest(REPETITION)
    @Order(120)
    fun `Generation size 10 with no repetition`() {
        testWithSize(ParentsType.SHUFFLE, 10, false)
    }

    @RepeatedTest(REPETITION)
    @Order(130)
    open fun `Generation size equals to parents size with repetition`() {
        testWithSize(ParentsType.SHUFFLE, parentsDefault.size, true)
    }

    @RepeatedTest(REPETITION)
    @Order(140)
    open fun `Generation size equals to parents size with no repetition`() {
        testWithSize(ParentsType.SHUFFLE, parentsDefault.size, false)
    }

    @RepeatedTest(REPETITION)
    @Order(150)
    open fun `Generation size greater to parents size with repetition`() {
        testWithSize(ParentsType.SHUFFLE, parentsDefault.size + 5, true)
    }

    @RepeatedTest(REPETITION)
    @Order(160)
    open fun `Generation size greater to parents size with no repetition`() {
        testWithSize(ParentsType.SHUFFLE, parentsDefault.size + 5, false)
    }

}
