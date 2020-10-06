package rafael.ktgenetic.selection

import rafael.ktgenetic.TemplateChromosome

class RouletteSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        RouletteSelectionOperator(generationSize, allowRepetition)

}
