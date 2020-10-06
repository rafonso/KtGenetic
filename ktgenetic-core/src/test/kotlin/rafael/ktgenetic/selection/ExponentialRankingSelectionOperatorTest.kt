package rafael.ktgenetic.selection

import rafael.ktgenetic.TemplateChromosome

class ExponentialRankingSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        ExponentialRankingSelectionOperator(generationSize, allowRepetition)

}
