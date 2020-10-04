package rafael.ktgenetic.selection

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order

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

    @Disabled
    @Order(90)
    override fun `Generation size 5 with repetition`() {
    }

    @Disabled
    @Order(110)
    override fun `Generation size 10 with repetition`() {
    }

    @Disabled
    @Order(130)
    override fun `Generation size equals to parents size with repetition`() {
    }

    @Disabled
    @Order(150)
    override fun `Generation size greater to parents size with repetition`() {
    }

}
