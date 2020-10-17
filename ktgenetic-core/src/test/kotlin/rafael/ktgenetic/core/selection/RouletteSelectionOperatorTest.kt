package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.TemplateChromosome

class RouletteSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        RouletteSelectionOperator(generationSize, allowRepetition)

}
