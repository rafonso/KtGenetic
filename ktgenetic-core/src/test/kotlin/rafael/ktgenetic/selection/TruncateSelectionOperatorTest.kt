package rafael.ktgenetic.selection

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TruncateSelectionOperatorTest : AbstractSelectionOperatorTest() {

    private fun testOriginalOrderKept(size: Int) {
        val operator = createOperator(size, true)
        val parents = parentsDefault.shuffled()

        val selected = operator.select(parents)

        assertEquals(size, selected.size)
        assertEquals(parents.subList(0, size), selected)
    }

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        TruncateSelectionOperator(generationSize, allowRepetition)

    @Disabled
    @Order(50)
    override fun `Singleton parent with Generation size 2 with repetition creates children with repetition`() {
    }

    @Test
    @Order(52)
    fun `Singleton parent with Generation size 2 with repetition throws IndexOutOfBoundsException`() {
        val operator = createOperator(2, true)
        val parent = TemplateChromosome("only", 0.5)
        val parents = listOf(parent)

        assertException(parents, operator, IndexOutOfBoundsException::class.java)
    }

    @Disabled
    @Order(90)
    override fun `Generation size 5 with repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(92)
    fun `Generation size 5 with repetition keeps original order of parents, not sorting it`() {
        testOriginalOrderKept(5)
    }


    @Disabled
    @Order(110)
    override fun `Generation size 10 with repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(112)
    fun `Generation size 10 with repetition keeps original order of parents, not sorting it`() {
        testOriginalOrderKept(10)
    }

    @Disabled
    @Order(130)
    override fun `Generation size equals to parents size with repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(132)
    fun `Generation size equals to parents size with repetition keeps original order of parents, not sorting it`() {
        testOriginalOrderKept(parentsDefault.size)
    }

    @Disabled
    @Order(150)
    override fun `Generation size greater to parents size with repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(152)
    fun `Generation size greater to parents size with repetition keeps original order of parents, not sorting it`() {
        super.testWithSizeError(parentsDefault.size + 5, true, IndexOutOfBoundsException::class.java)
    }

}
