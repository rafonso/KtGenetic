package rafael.ktgenetic.selection

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class TournamentSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        TournamentSelectionOperator(generationSize, allowRepetition)

    @Disabled
    @Order(20)
    override fun `Empty Parents with generation positive throws IndexOutOfBoundsException`() {
    }

    @Test
    @Order(24)
    fun `Empty Parents with generation positive throws IllegalArgumentException`() {
        super.testWithSizeError(ParentsType.EMPTY, 5, true, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(40)
    override fun `Singleton Parent with generation size 1 generates another singleton List`() {
    }

    @Test
    @Order(44)
    fun `Singleton Parent with generation size 1 throws IllegalArgumentException`() {
        super.testWithSizeError(ParentsType.SINGLE, 1, true, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(50)
    override fun `Singleton parent with Generation size 2 with repetition creates children with repetition`() {
    }

    @Test
    @Order(54)
    fun `Singleton Parent with generation size 2 throws IllegalArgumentException`() {
        super.testWithSizeError(ParentsType.SINGLE, 2, true, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(60)
    override fun `Singleton parent with Generation size 2 with no repetition creates children with size 1`() {
    }

    @Test
    @Order(64)
    fun `Singleton parent with Generation size 2 with no repetition throws AssertionError`() {
        super.testWithSizeError(ParentsType.SINGLE, 2, false, AssertionError::class.java)
    }

    @Disabled
    @Order(140)
    override fun `Generation size equals to parents size with no repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(144)
    fun `Generation size equals to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(ParentsType.SHUFFLE, sizeDefault, false, AssertionError::class.java)
    }


    @Disabled
    @Order(160)
    override fun `Generation size greater to parents size with no repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(164)
    fun `Generation size greater to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(ParentsType.SHUFFLE, sizeDefault + 5, false, AssertionError::class.java)
    }

}
