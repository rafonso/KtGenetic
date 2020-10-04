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
        val operator = createOperator(5, true)
        val parents = emptyList<TemplateChromosome>()

        assertException(parents, operator, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(40)
    override fun `Singleton Parent with generation size 1 generates another singleton List`() {
    }

    @Test
    @Order(44)
    fun `Singleton Parent with generation size 1 throws IllegalArgumentException`() {
        val operator = createOperator(1, true)
        val parents = listOf(TemplateChromosome("only", 0.5))

        assertException(parents, operator, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(50)
    override fun `Singleton parent with Generation size 2 with repetition creates children with repetition`() {
    }

    @Test
    @Order(54)
    fun `Singleton Parent with generation size 2 throws IllegalArgumentException`() {
        val operator = createOperator(2, true)
        val parent = TemplateChromosome("only", 0.5)
        val parents = listOf(parent)

        assertException(parents, operator, IllegalArgumentException::class.java)
    }

    @Disabled
    @Order(60)
    override fun `Singleton parent with Generation size 2 with no repetition creates children with size 1`() {
    }

    @Test
    @Order(64)
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
    @Order(144)
    fun `Generation size equals to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(parentsDefault.size, false, AssertionError::class.java)
    }


    @Disabled
    @Order(160)
    override fun `Generation size greater to parents size with no repetition`() {
    }

    @RepeatedTest(REPETITION)
    @Order(164)
    fun `Generation size greater to parents size with no repetition throws AssertionError`() {
        super.testWithSizeError(parentsDefault.size + 5, false, AssertionError::class.java)
    }

}
/*
[
[1.00 =, 1,0000000000],0
[1.00 <, 0,9999999900],1
[0.75 >, 0,7500000100],2
[0.75 A, 0,7500000000],3
[0.75 <, 0,7499999900],4
[0.50 =, 0,5000000000],5
[0.25 >, 0,2500000100],6
[0.25 B, 0,2500000000],7
[0.25 A, 0,2500000000],8
[0.25 <, 0,2499999900],
[0.00 >, 0,0000000100],
[0.00 =, 0,0000000000]
]
 */
