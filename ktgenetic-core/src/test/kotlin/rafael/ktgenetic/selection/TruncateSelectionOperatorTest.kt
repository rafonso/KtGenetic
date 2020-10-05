package rafael.ktgenetic.selection

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class TruncateSelectionOperatorTest : AbstractSelectionOperatorTest() {

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
        super.testWithSizeError(ParentsType.SINGLE, 2, true, IndexOutOfBoundsException::class.java)
    }

    @Disabled
    @Order(150)
    override fun `Generation size greater to parents size with repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(152)
    fun `Generation size greater to parents size with repetition throws IndexOutOfBoundsException`() {
        super.testWithSizeError(
            ParentsType.SHUFFLE,
            sizeDefault + 5,
            true,
            IndexOutOfBoundsException::class.java
        )
    }

}
