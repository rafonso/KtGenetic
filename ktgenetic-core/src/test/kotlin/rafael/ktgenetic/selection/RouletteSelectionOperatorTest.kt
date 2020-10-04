package rafael.ktgenetic.selection

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RouletteSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        RouletteSelectionOperator(generationSize, allowRepetition)

}
