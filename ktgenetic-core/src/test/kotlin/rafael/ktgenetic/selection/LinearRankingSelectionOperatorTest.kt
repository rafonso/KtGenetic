package rafael.ktgenetic.selection

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class LinearRankingSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        LinearRankingSelectionOperator(generationSize, allowRepetition)

    @Disabled
    @Order(20)
    override fun `Empty Parents with generation positive throws IndexOutOfBoundsException`() {
    }

    @Test
    @Order(26)
    fun `Empty Parents with generation positive throws IllegalArgumentException`() {
        val operator = createOperator(5, true)
        val parents = emptyList<TemplateChromosome>()

        assertException(parents, operator, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(60)
    override fun `Singleton parent with Generation size 2 with no repetition creates children with size 1`() {
    }

    @Test
    @Order(66)
    fun `Singleton parent with Generation size 2 with no repetition throws AssertionError`() {
        val operator = createOperator(2, false)
        val parent = TemplateChromosome("only", 0.5)
        val parents = listOf(parent)

        assertException(parents, operator, AssertionError::class.java)
    }

    @Disabled
    @Order(140)
    override fun `Generation size equals to parents size with no repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(146)
    fun `Generation size equals to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(super.parentsDefault.size, false, AssertionError::class.java)
    }

    @Disabled
    @Order(160)
    override fun `Generation size greater to parents size with no repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(166)
    fun `Generation size greater to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(super.parentsDefault.size + 5, false, AssertionError::class.java)
    }

}
