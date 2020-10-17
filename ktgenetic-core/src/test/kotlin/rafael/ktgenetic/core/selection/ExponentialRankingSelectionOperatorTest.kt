package rafael.ktgenetic.core.selection

import rafael.ktgenetic.core.TemplateChromosome

class ExponentialRankingSelectionOperatorTest : AbstractSelectionOperatorTest() {

    override fun createOperator(
        generationSize: Int,
        allowRepetition: Boolean
    ): SelectionOperator<TemplateChromosome> =
        ExponentialRankingSelectionOperator(generationSize, allowRepetition)

}
