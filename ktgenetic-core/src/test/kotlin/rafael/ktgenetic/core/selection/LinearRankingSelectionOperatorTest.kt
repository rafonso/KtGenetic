package rafael.ktgenetic.core.selection

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import rafael.ktgenetic.core.TemplateChromosome

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
        super.testWithSizeError(ParentsType.EMPTY, 5, true, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(60)
    override fun `Singleton parent with Generation size 2 with no repetition creates children with size 1`() {
    }

    @Test
    @Order(66)
    fun `Singleton parent with Generation size 2 with no repetition throws AssertionError`() {
        super.testWithSizeError(ParentsType.SINGLE, 2, false, AssertionError::class.java)
    }

    @Disabled
    @Order(140)
    override fun `Generation size equals to parents size with no repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(146)
    fun `Generation size equals to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(ParentsType.SHUFFLE, sizeDefault, false, AssertionError::class.java)
    }

    @Disabled
    @Order(160)
    override fun `Generation size greater to parents size with no repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(166)
    fun `Generation size greater to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(ParentsType.SHUFFLE, sizeDefault + 5, false, AssertionError::class.java)
    }

}
