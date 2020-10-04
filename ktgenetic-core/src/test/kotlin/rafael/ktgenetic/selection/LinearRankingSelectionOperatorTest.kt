package rafael.ktgenetic.selection

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order

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

    @Disabled
    @Order(60)
    override fun `Singleton parent with Generation size 2 with no repetition creates children with size 1`() {
    }

    @Disabled
    @Order(140)
    override fun `Generation size equals to parents size with no repetition`() {
    }

    @Disabled
    @Order(160)
    override fun `Generation size greater to parents size with no repetition`() {
    }

}
